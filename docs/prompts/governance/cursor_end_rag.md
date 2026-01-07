# END_RAG
_Exported on 12/8/2025 at 19:37:56 GMT+1 from Cursor (2.0.77)_

---

**User**

@MIGRACION_RAG_DASHBOARDS.md @MIGRACION_RAG_OVERVIEWS.md ejecuta la migracion de estos prompts

---

**Cursor**

Revisando los archivos ZUL originales y los ViewModels asociados para entender la funcionalidad.


Leyendo los archivos ZUL completos y los ViewModels asociados:


Verificando la estructura del proyecto Next.js y si estas páginas ya existen:


Revisando la estructura de carpetas de Next.js y creando un plan de migración:


Buscando la sección del menú RAG:


Creando el plan de trabajo e iniciando la migración de las 3 pantallas:


Creando las 3 páginas migradas, empezando por el health-dashboard:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Heart,
  Search,
  TrendingDown,
  TrendingUp,
  X,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en IndexHealthDashboardOverviewViewModel
interface HealthDashboardItem {
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}

const mockHealthData: HealthDashboardItem[] = [
  {
    totalItems: 150,
    activeItems: 120,
    deployedItems: 100,
    trainingItems: 20,
    offlineItems: 10,
    avgScore: 92.5,
  },
  {
    totalItems: 200,
    activeItems: 180,
    deployedItems: 150,
    trainingItems: 30,
    offlineItems: 20,
    avgScore: 88.3,
  },
  {
    totalItems: 95,
    activeItems: 85,
    deployedItems: 75,
    trainingItems: 10,
    offlineItems: 5,
    avgScore: 95.1,
  },
  {
    totalItems: 175,
    activeItems: 160,
    deployedItems: 140,
    trainingItems: 20,
    offlineItems: 15,
    avgScore: 90.7,
  },
];

export default function HealthDashboardPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [healthData, setHealthData] = useState(mockHealthData);

  const filteredData = healthData.filter((item) => {
    if (!searchTerm) return true;
    return (
      item.totalItems.toString().includes(searchTerm) ||
      item.activeItems.toString().includes(searchTerm) ||
      item.avgScore.toString().includes(searchTerm)
    );
  });

  const clearFilters = () => {
    setSearchTerm("");
  };

  const getScoreColor = (score: number) => {
    if (score >= 90) return "text-green-500";
    if (score >= 75) return "text-yellow-500";
    return "text-red-500";
  };

  const getScoreBadgeVariant = (score: number) => {
    if (score >= 90) return "default";
    if (score >= 75) return "secondary";
    return "destructive";
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
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Heart className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("rag.healthDashboard.title", "Index Health Dashboard")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "rag.healthDashboard.description",
              "Gestión de Index Health Dashboard"
            )}
          </p>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="flex items-center gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      // Filter is applied automatically
                    }
                  }}
                />
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={clearFilters}
                className="gap-2"
              >
                <X className="h-4 w-4" />
                {t("common.clearFilters", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Health Metrics Table */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5 text-primary" />
              {t("rag.healthDashboard.metrics", "Métricas de Salud")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.totalItems", "Total Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.activeItems", "Active Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t(
                        "rag.healthDashboard.deployedItems",
                        "Deployed Items"
                      )}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t(
                        "rag.healthDashboard.trainingItems",
                        "Training Items"
                      )}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.offlineItems", "Offline Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.avgScore", "Avg Score")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((item, index) => (
                    <tr
                      key={index}
                      className="border-b border-border/50 hover:bg-muted/30 transition-colors cursor-pointer"
                    >
                      <td className="p-4">
                        <div className="font-medium">{item.totalItems}</div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge
                          variant="outline"
                          className="bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300"
                        >
                          {item.activeItems}
                        </Badge>
                      </td>
                      <td className="p-4 text-center">
                        <div className="flex items-center justify-center gap-1">
                          <TrendingUp className="h-4 w-4 text-blue-500" />
                          <span>{item.deployedItems}</span>
                        </div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge variant="secondary">{item.trainingItems}</Badge>
                      </td>
                      <td className="p-4 text-center">
                        <div className="flex items-center justify-center gap-1">
                          <TrendingDown className="h-4 w-4 text-red-500" />
                          <span className="text-red-600 dark:text-red-400">
                            {item.offlineItems}
                          </span>
                        </div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge
                          variant={getScoreBadgeVariant(item.avgScore)}
                          className="text-lg font-semibold"
                        >
                          {item.avgScore.toFixed(1)}%
                        </Badge>
                      </td>
                      <td className="p-4 text-center">
                        <Button
                          variant="ghost"
                          size="sm"
                          className="gap-2"
                          onClick={() => {
                            // Navigate to details
                            console.log("View details for", item.totalItems);
                          }}
                        >
                          <Search className="h-4 w-4" />
                          {t("common.details", "Ver detalles")}
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredData.length === 0 && (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

// Mock data para Index Health Dashboard
const mockHealthData = [
  {
    totalItems: 150,
    activeItems: 120,
    deployedItems: 100,
    trainingItems: 20,
    offlineItems: 10,
    avgScore: 92.5,
  },
  {
    totalItems: 200,
    activeItems: 180,
    deployedItems: 150,
    trainingItems: 30,
    offlineItems: 20,
    avgScore: 88.3,
  },
  {
    totalItems: 95,
    activeItems: 85,
    deployedItems: 75,
    trainingItems: 10,
    offlineItems: 5,
    avgScore: 95.1,
  },
  {
    totalItems: 175,
    activeItems: 160,
    deployedItems: 140,
    trainingItems: 20,
    offlineItems: 15,
    avgScore: 90.7,
  },
];

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const searchTerm = searchParams.get("search") || "";
  const page = parseInt(searchParams.get("page") || "1");
  const pageSize = parseInt(searchParams.get("pageSize") || "10");

  // Filtrar datos si hay término de búsqueda
  let filteredData = mockHealthData;
  if (searchTerm) {
    filteredData = mockHealthData.filter(
      (item) =>
        item.totalItems.toString().includes(searchTerm) ||
        item.activeItems.toString().includes(searchTerm) ||
        item.avgScore.toString().includes(searchTerm)
    );
  }

  // Paginación
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedData = filteredData.slice(startIndex, endIndex);

  return NextResponse.json({
    items: paginatedData,
    total: filteredData.length,
    page,
    pageSize,
    totalPages: Math.ceil(filteredData.length / pageSize),
  });
}

```

Creando la página de data-sources/overview:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Database,
  Edit,
  Plus,
  Search,
  Trash2,
  X,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en RagDataSourceOverviewViewModel
interface DataSource {
  idxragdatasource: number;
  ragname: string;
  ragdescription: string;
  ragtype: "ACTIVE" | "INACTIVE" | "PENDING";
  ragconnectionconfig: string;
  ragsyncconfig: string;
}

const mockDataSources: DataSource[] = [
  {
    idxragdatasource: 1,
    ragname: "PostgreSQL Main DB",
    ragdescription: "Base de datos principal PostgreSQL",
    ragtype: "ACTIVE",
    ragconnectionconfig: "postgresql://localhost:5432/main",
    ragsyncconfig: "auto-sync",
  },
  {
    idxragdatasource: 2,
    ragname: "MongoDB Collections",
    ragdescription: "Colecciones de MongoDB para documentos",
    ragtype: "ACTIVE",
    ragconnectionconfig: "mongodb://localhost:27017/docs",
    ragsyncconfig: "manual-sync",
  },
  {
    idxragdatasource: 3,
    ragname: "Elasticsearch Index",
    ragdescription: "Índice de Elasticsearch para búsquedas",
    ragtype: "PENDING",
    ragconnectionconfig: "http://localhost:9200/rag-index",
    ragsyncconfig: "auto-sync",
  },
  {
    idxragdatasource: 4,
    ragname: "S3 Bucket Documents",
    ragdescription: "Bucket S3 con documentos para RAG",
    ragtype: "INACTIVE",
    ragconnectionconfig: "s3://rag-documents/",
    ragsyncconfig: "scheduled-sync",
  },
];

export default function DataSourcesOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [syncStatusFilter, setSyncStatusFilter] = useState<string>("ALL");
  const [dataSources, setDataSources] = useState(mockDataSources);

  // Calcular métricas
  const totalItems = dataSources.length;
  const activeItems = dataSources.filter((ds) => ds.ragtype === "ACTIVE").length;
  const pendingApproval = dataSources.filter(
    (ds) => ds.ragtype === "PENDING"
  ).length;
  const inactiveItems = dataSources.filter(
    (ds) => ds.ragtype === "INACTIVE"
  ).length;

  const filteredData = dataSources.filter((item) => {
    const matchesSearch =
      !searchTerm ||
      item.ragname.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.ragdescription.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.idxragdatasource.toString().includes(searchTerm);

    const matchesType = typeFilter === "ALL" || item.ragtype === typeFilter;
    const matchesStatus = statusFilter === "ALL" || true; // Add status logic if needed
    const matchesSyncStatus =
      syncStatusFilter === "ALL" || true; // Add sync status logic if needed

    return matchesSearch && matchesType && matchesStatus && matchesSyncStatus;
  });

  const clearFilters = () => {
    setSearchTerm("");
    setTypeFilter("ALL");
    setStatusFilter("ALL");
    setSyncStatusFilter("ALL");
  };

  const getTypeBadgeVariant = (type: string) => {
    switch (type) {
      case "ACTIVE":
        return "default";
      case "INACTIVE":
        return "secondary";
      case "PENDING":
        return "outline";
      default:
        return "outline";
    }
  };

  const handleDelete = (id: number) => {
    if (confirm(t("common.confirmDelete", "¿Eliminar este elemento?"))) {
      setDataSources(dataSources.filter((ds) => ds.idxragdatasource !== id));
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
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Database className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("rag.dataSources.title", "Data Source")}
              </h1>
              <p className="text-muted-foreground">
                {t(
                  "rag.dataSources.description",
                  "Gestión de Data Source"
                )}
              </p>
            </div>
          </div>
          <Button className="gap-2">
            <Plus className="h-4 w-4" />
            {t("rag.dataSources.register", "Registrar Data Source")}
          </Button>
        </div>

        {/* Metrics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.dataSources.total", "Total")}
              </p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-green-500 to-emerald-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.dataSources.active", "Activos")}
              </p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.dataSources.pending", "Pendientes")}
              </p>
              <h2 className="text-4xl font-bold">{pendingApproval}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-purple-500 to-pink-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.dataSources.inactive", "Inactivos")}
              </p>
              <h2 className="text-4xl font-bold">{inactiveItems}</h2>
            </CardBody>
          </Card>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="flex flex-col md:flex-row items-center gap-4">
              <div className="flex-1 relative w-full">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <Select value={typeFilter} onValueChange={setTypeFilter}>
                <SelectTrigger className="w-full md:w-[200px]">
                  <SelectValue placeholder={t("rag.dataSources.filterType", "Filtrar por Type")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("common.all", "TODOS")}</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                  <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                  <SelectItem value="PENDING">PENDING</SelectItem>
                </SelectContent>
              </Select>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-full md:w-[200px]">
                  <SelectValue placeholder={t("rag.dataSources.filterStatus", "Filtrar por Status")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("common.all", "TODOS")}</SelectItem>
                </SelectContent>
              </Select>
              <Select value={syncStatusFilter} onValueChange={setSyncStatusFilter}>
                <SelectTrigger className="w-full md:w-[200px]">
                  <SelectValue placeholder={t("rag.dataSources.filterSyncStatus", "Filtrar por Syncstatus")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("common.all", "TODOS")}</SelectItem>
                </SelectContent>
              </Select>
              <Button
                variant="outline"
                size="sm"
                onClick={clearFilters}
                className="gap-2 w-full md:w-auto"
              >
                <X className="h-4 w-4" />
                {t("common.clearFilters", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Data Sources Table */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Database className="w-5 h-5 text-primary" />
              {t("rag.dataSources.list", "Listado de Data Sources")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4 font-semibold text-sm">ID</th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.dataSources.name", "Name")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.dataSources.description", "Description")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.dataSources.type", "Type")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.dataSources.connectionConfig", "Connectionconfig")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.dataSources.syncConfig", "Syncconfig")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((item) => (
                    <tr
                      key={item.idxragdatasource}
                      className="border-b border-border/50 hover:bg-muted/30 transition-colors"
                    >
                      <td className="p-4">
                        <div className="font-medium">{item.idxragdatasource}</div>
                      </td>
                      <td className="p-4">
                        <div className="font-medium">{item.ragname}</div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm text-muted-foreground">
                          {item.ragdescription}
                        </div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge variant={getTypeBadgeVariant(item.ragtype)}>
                          {item.ragtype}
                        </Badge>
                      </td>
                      <td className="p-4">
                        <div className="text-sm font-mono text-muted-foreground truncate max-w-xs">
                          {item.ragconnectionconfig}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm text-muted-foreground">
                          {item.ragsyncconfig}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="gap-2"
                            onClick={() => {
                              console.log("Edit", item.idxragdatasource);
                            }}
                          >
                            <Edit className="h-4 w-4" />
                            {t("common.details", "Ver detalles")}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="gap-2 text-destructive hover:text-destructive"
                            onClick={() => handleDelete(item.idxragdatasource)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredData.length === 0 && (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

// Mock data para RagDataSource
const mockDataSources = [
  {
    idxragdatasource: 1,
    ragname: "PostgreSQL Main DB",
    ragdescription: "Base de datos principal PostgreSQL",
    ragtype: "ACTIVE",
    ragconnectionconfig: "postgresql://localhost:5432/main",
    ragsyncconfig: "auto-sync",
  },
  {
    idxragdatasource: 2,
    ragname: "MongoDB Collections",
    ragdescription: "Colecciones de MongoDB para documentos",
    ragtype: "ACTIVE",
    ragconnectionconfig: "mongodb://localhost:27017/docs",
    ragsyncconfig: "manual-sync",
  },
  {
    idxragdatasource: 3,
    ragname: "Elasticsearch Index",
    ragdescription: "Índice de Elasticsearch para búsquedas",
    ragtype: "PENDING",
    ragconnectionconfig: "http://localhost:9200/rag-index",
    ragsyncconfig: "auto-sync",
  },
  {
    idxragdatasource: 4,
    ragname: "S3 Bucket Documents",
    ragdescription: "Bucket S3 con documentos para RAG",
    ragtype: "INACTIVE",
    ragconnectionconfig: "s3://rag-documents/",
    ragsyncconfig: "scheduled-sync",
  },
];

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const searchTerm = searchParams.get("search") || "";
  const typeFilter = searchParams.get("type") || "ALL";
  const statusFilter = searchParams.get("status") || "ALL";
  const syncStatusFilter = searchParams.get("syncStatus") || "ALL";
  const page = parseInt(searchParams.get("page") || "1");
  const pageSize = parseInt(searchParams.get("pageSize") || "10");

  // Filtrar datos
  let filteredData = mockDataSources.filter((item) => {
    const matchesSearch =
      !searchTerm ||
      item.ragname.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.ragdescription.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.idxragdatasource.toString().includes(searchTerm);

    const matchesType = typeFilter === "ALL" || item.ragtype === typeFilter;
    const matchesStatus = statusFilter === "ALL" || true;
    const matchesSyncStatus = syncStatusFilter === "ALL" || true;

    return matchesSearch && matchesType && matchesStatus && matchesSyncStatus;
  });

  // Calcular métricas
  const totalItems = mockDataSources.length;
  const activeItems = mockDataSources.filter(
    (ds) => ds.ragtype === "ACTIVE"
  ).length;
  const pendingApproval = mockDataSources.filter(
    (ds) => ds.ragtype === "PENDING"
  ).length;
  const inactiveItems = mockDataSources.filter(
    (ds) => ds.ragtype === "INACTIVE"
  ).length;

  // Paginación
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedData = filteredData.slice(startIndex, endIndex);

  return NextResponse.json({
    items: paginatedData,
    total: filteredData.length,
    page,
    pageSize,
    totalPages: Math.ceil(filteredData.length / pageSize),
    metrics: {
      totalItems,
      activeItems,
      pendingApproval,
      inactiveItems,
    },
  });
}

export async function POST(request: Request) {
  const body = await request.json();
  // Mock create
  const newDataSource = {
    idxragdatasource: mockDataSources.length + 1,
    ...body,
  };
  return NextResponse.json(newDataSource, { status: 201 });
}

export async function DELETE(request: Request) {
  const { searchParams } = new URL(request.url);
  const id = searchParams.get("id");
  // Mock delete
  return NextResponse.json({ success: true, id });
}

```

Creando la página de versioning/overview:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  CodeBranch,
  Edit,
  Plus,
  Search,
  Trash2,
  X,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en RagVersionOverviewViewModel
interface RagVersion {
  idxragversion: number;
  ragversion: string;
  ragdescription: string;
  ragconfiguration: string;
  ragembeddingmodel: string;
  ragretrievalconfig: string;
  ragstatus?: string;
}

const mockVersions: RagVersion[] = [
  {
    idxragversion: 1,
    ragversion: "v1.0.0",
    ragdescription: "Versión inicial del sistema RAG",
    ragconfiguration: '{"chunkSize": 1000, "overlap": 200}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 5, "threshold": 0.7}',
    ragstatus: "ACTIVE",
  },
  {
    idxragversion: 2,
    ragversion: "v1.1.0",
    ragdescription: "Mejoras en el sistema de recuperación",
    ragconfiguration: '{"chunkSize": 1200, "overlap": 250}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 10, "threshold": 0.65}',
    ragstatus: "ACTIVE",
  },
  {
    idxragversion: 3,
    ragversion: "v2.0.0",
    ragdescription: "Refactorización completa del sistema",
    ragconfiguration: '{"chunkSize": 1500, "overlap": 300}',
    ragembeddingmodel: "text-embedding-3-large",
    ragretrievalconfig: '{"topK": 15, "threshold": 0.6}',
    ragstatus: "PENDING",
  },
  {
    idxragversion: 4,
    ragversion: "v1.0.5",
    ragdescription: "Hotfix para corrección de bugs",
    ragconfiguration: '{"chunkSize": 1000, "overlap": 200}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 5, "threshold": 0.7}',
    ragstatus: "INACTIVE",
  },
];

export default function VersioningOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [versions, setVersions] = useState(mockVersions);

  // Calcular métricas
  const totalItems = versions.length;
  const activeItems = versions.filter(
    (v) => v.ragstatus === "ACTIVE"
  ).length;
  const pendingApproval = versions.filter(
    (v) => v.ragstatus === "PENDING"
  ).length;
  const inactiveItems = versions.filter(
    (v) => v.ragstatus === "INACTIVE"
  ).length;

  const filteredData = versions.filter((item) => {
    const matchesSearch =
      !searchTerm ||
      item.ragversion.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.ragdescription.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.idxragversion.toString().includes(searchTerm);

    const matchesStatus =
      statusFilter === "ALL" || item.ragstatus === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const clearFilters = () => {
    setSearchTerm("");
    setStatusFilter("ALL");
  };

  const getStatusBadgeVariant = (status?: string) => {
    switch (status) {
      case "ACTIVE":
        return "default";
      case "INACTIVE":
        return "secondary";
      case "PENDING":
        return "outline";
      default:
        return "outline";
    }
  };

  const handleDelete = (id: number) => {
    if (confirm(t("common.confirmDelete", "¿Eliminar este elemento?"))) {
      setVersions(versions.filter((v) => v.idxragversion !== id));
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
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CodeBranch className="w-8 h-8 text-primary" />
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                {t("rag.versioning.title", "Version")}
              </h1>
              <p className="text-muted-foreground">
                {t("rag.versioning.description", "Gestión de Version")}
              </p>
            </div>
          </div>
          <Button className="gap-2">
            <Plus className="h-4 w-4" />
            {t("rag.versioning.register", "Registrar Version")}
          </Button>
        </div>

        {/* Metrics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-gradient-to-br from-blue-500 to-cyan-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.versioning.total", "Total")}
              </p>
              <h2 className="text-4xl font-bold">{totalItems}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-green-500 to-emerald-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.versioning.active", "Activos")}
              </p>
              <h2 className="text-4xl font-bold">{activeItems}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-pink-500 to-yellow-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.versioning.pending", "Pendientes")}
              </p>
              <h2 className="text-4xl font-bold">{pendingApproval}</h2>
            </CardBody>
          </Card>
          <Card className="backdrop-blur-md bg-gradient-to-br from-purple-500 to-pink-500 text-white border-0">
            <CardBody className="p-6">
              <p className="text-sm opacity-90 mb-2">
                {t("rag.versioning.inactive", "Inactivos")}
              </p>
              <h2 className="text-4xl font-bold">{inactiveItems}</h2>
            </CardBody>
          </Card>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="flex flex-col md:flex-row items-center gap-4">
              <div className="flex-1 relative w-full">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger className="w-full md:w-[200px]">
                  <SelectValue
                    placeholder={t("rag.versioning.filterStatus", "Filtrar por Status")}
                  />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("common.all", "TODOS")}</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                  <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                  <SelectItem value="PENDING">PENDING</SelectItem>
                </SelectContent>
              </Select>
              <Button
                variant="outline"
                size="sm"
                onClick={clearFilters}
                className="gap-2 w-full md:w-auto"
              >
                <X className="h-4 w-4" />
                {t("common.clearFilters", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Versions Table */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <CodeBranch className="w-5 h-5 text-primary" />
              {t("rag.versioning.list", "Listado de Versiones")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4 font-semibold text-sm">ID</th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.versioning.version", "Version")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.versioning.description", "Description")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.versioning.configuration", "Configuration")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.versioning.embeddingModel", "Embeddingmodel")}
                    </th>
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.versioning.retrievalConfig", "Retrievalconfig")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((item) => (
                    <tr
                      key={item.idxragversion}
                      className="border-b border-border/50 hover:bg-muted/30 transition-colors"
                    >
                      <td className="p-4">
                        <div className="font-medium">{item.idxragversion}</div>
                      </td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Badge variant={getStatusBadgeVariant(item.ragstatus)}>
                            {item.ragversion}
                          </Badge>
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm text-muted-foreground">
                          {item.ragdescription}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm font-mono text-muted-foreground truncate max-w-xs">
                          {item.ragconfiguration}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm text-muted-foreground">
                          {item.ragembeddingmodel}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="text-sm font-mono text-muted-foreground truncate max-w-xs">
                          {item.ragretrievalconfig}
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="gap-2"
                            onClick={() => {
                              console.log("Edit", item.idxragversion);
                            }}
                          >
                            <Edit className="h-4 w-4" />
                            {t("common.details", "Ver detalles")}
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="gap-2 text-destructive hover:text-destructive"
                            onClick={() => handleDelete(item.idxragversion)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredData.length === 0 && (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}

```

```typescript
import { NextResponse } from "next/server";

// Mock data para RagVersion
const mockVersions = [
  {
    idxragversion: 1,
    ragversion: "v1.0.0",
    ragdescription: "Versión inicial del sistema RAG",
    ragconfiguration: '{"chunkSize": 1000, "overlap": 200}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 5, "threshold": 0.7}',
    ragstatus: "ACTIVE",
  },
  {
    idxragversion: 2,
    ragversion: "v1.1.0",
    ragdescription: "Mejoras en el sistema de recuperación",
    ragconfiguration: '{"chunkSize": 1200, "overlap": 250}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 10, "threshold": 0.65}',
    ragstatus: "ACTIVE",
  },
  {
    idxragversion: 3,
    ragversion: "v2.0.0",
    ragdescription: "Refactorización completa del sistema",
    ragconfiguration: '{"chunkSize": 1500, "overlap": 300}',
    ragembeddingmodel: "text-embedding-3-large",
    ragretrievalconfig: '{"topK": 15, "threshold": 0.6}',
    ragstatus: "PENDING",
  },
  {
    idxragversion: 4,
    ragversion: "v1.0.5",
    ragdescription: "Hotfix para corrección de bugs",
    ragconfiguration: '{"chunkSize": 1000, "overlap": 200}',
    ragembeddingmodel: "text-embedding-ada-002",
    ragretrievalconfig: '{"topK": 5, "threshold": 0.7}',
    ragstatus: "INACTIVE",
  },
];

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url);
  const searchTerm = searchParams.get("search") || "";
  const statusFilter = searchParams.get("status") || "ALL";
  const page = parseInt(searchParams.get("page") || "1");
  const pageSize = parseInt(searchParams.get("pageSize") || "10");

  // Filtrar datos
  let filteredData = mockVersions.filter((item) => {
    const matchesSearch =
      !searchTerm ||
      item.ragversion.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.ragdescription.toLowerCase().includes(searchTerm.toLowerCase()) ||
      item.idxragversion.toString().includes(searchTerm);

    const matchesStatus =
      statusFilter === "ALL" || item.ragstatus === statusFilter;

    return matchesSearch && matchesStatus;
  });

  // Calcular métricas
  const totalItems = mockVersions.length;
  const activeItems = mockVersions.filter(
    (v) => v.ragstatus === "ACTIVE"
  ).length;
  const pendingApproval = mockVersions.filter(
    (v) => v.ragstatus === "PENDING"
  ).length;
  const inactiveItems = mockVersions.filter(
    (v) => v.ragstatus === "INACTIVE"
  ).length;

  // Paginación
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const paginatedData = filteredData.slice(startIndex, endIndex);

  return NextResponse.json({
    items: paginatedData,
    total: filteredData.length,
    page,
    pageSize,
    totalPages: Math.ceil(filteredData.length / pageSize),
    metrics: {
      totalItems,
      activeItems,
      pendingApproval,
      inactiveItems,
    },
  });
}

export async function POST(request: Request) {
  const body = await request.json();
  // Mock create
  const newVersion = {
    idxragversion: mockVersions.length + 1,
    ...body,
  };
  return NextResponse.json(newVersion, { status: 201 });
}

export async function DELETE(request: Request) {
  const { searchParams } = new URL(request.url);
  const id = searchParams.get("id");
  // Mock delete
  return NextResponse.json({ success: true, id });
}

```

Agregando traducciones y entradas al menú. Revisando la estructura de traducciones:


Agregando las traducciones necesarias. Buscando el final de la sección `rag`:


Agregando las traducciones necesarias para las nuevas pantallas:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
      noResults: "No se encontraron resultados",
      showing: "Mostrando",
      all: "TODOS",
      clearFilters: "Limpiar filtros",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Gestión de Prompt Performance Comparison",
        results: "Resultados",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Gestión de Prompt Validation",
        register: "Registrar Prompt Validation",
        results: "Resultados",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filtrar por Validationtype",
        filterStatus: "Filtrar por Status",
        type1: "TIPO 1",
        type2: "TIPO 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Gestión de Prompt Version",
        register: "Registrar Prompt Version",
        results: "Resultados",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filtrar por Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
      approval: {
        title: "Aprobaciones de Modelos",
        subtitle: "Gestión de solicitudes de aprobación pendientes",
      },
      biasAnalysis: {
        title: "Análisis de Sesgo",
        subtitle: "Gestión de análisis de sesgo",
      },
      dependencies: {
        title: "Dependencias",
        subtitle: "Gestión de dependencias de modelos",
      },
      explainability: {
        title: "Explicabilidad",
        subtitle: "Gestión de explicabilidad de modelos",
      },
      performance: {
        title: "Rendimiento",
        subtitle: "Gestión de rendimiento de modelos",
      },
      usageOverview: {
        title: "Uso de Modelos",
        subtitle: "Vista general del uso de modelos",
      },
      artifactOverview: {
        title: "Artefactos",
        subtitle: "Gestión de artefactos de modelos",
      },
      capabilityOverview: {
        title: "Capacidades",
        subtitle: "Gestión de capacidades de modelos",
      },
      catalogOverview: {
        title: "Catálogo",
        subtitle: "Gestión del catálogo de modelos",
      },
      endpointOverview: {
        title: "Endpoints",
        subtitle: "Gestión de endpoints de modelos",
      },
      providerCredentialOverview: {
        title: "Credenciales de Proveedor",
        subtitle: "Gestión de credenciales de proveedores",
      },
      providerOverview: {
        title: "Proveedores",
        subtitle: "Gestión de proveedores de modelos",
      },
      versioning: {
        title: "Versionado",
        subtitle: "Gestión de versiones de modelos",
      },
    },

    // Monitoring
    monitoring: {
      title: "Monitoreo",
      alerts: {
        overview: {
          title: "Alertas de Monitoreo",
          description: "Gestión de alertas de monitoreo",
          register: "Registrar Alerta",
          list: "Lista de Alertas",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Detalle de Alerta",
          description: "Gestión y configuración de alerta de monitoreo",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "Detalle de Alerta del Sistema",
          description: "Gestión de alertas del sistema",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Respuesta a Alerta Crítica",
          description: "Formulario de respuesta a alerta",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        detail: {
          title: "Detalle de Métrica",
          description: "Gestión de métricas de monitoreo",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
      healthDashboard: {
        title: "Index Health Dashboard",
        description: "Gestión de Index Health Dashboard",
        metrics: "Métricas de Salud",
        totalItems: "Total Items",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      dataSources: {
        title: "Data Source",
        description: "Gestión de Data Source",
        register: "Registrar Data Source",
        total: "Total",
        active: "Activos",
        pending: "Pendientes",
        inactive: "Inactivos",
        list: "Listado de Data Sources",
        name: "Name",
        description: "Description",
        type: "Type",
        connectionConfig: "Connectionconfig",
        syncConfig: "Syncconfig",
        filterType: "Filtrar por Type",
        filterStatus: "Filtrar por Status",
        filterSyncStatus: "Filtrar por Syncstatus",
      },
      versioning: {
        title: "Version",
        description: "Gestión de Version",
        register: "Registrar Version",
        total: "Total",
        active: "Activos",
        pending: "Pendientes",
        inactive: "Inactivos",
        list: "Listado de Versiones",
        version: "Version",
        description: "Description",
        configuration: "Configuration",
        embeddingModel: "Embeddingmodel",
        retrievalConfig: "Retrievalconfig",
        filterStatus: "Filtrar por Status",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
      noResults: "No results found",
      showing: "Showing",
      all: "ALL",
      clearFilters: "Clear filters",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Prompt Performance Comparison Management",
        results: "Results",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Prompt Validation Management",
        register: "Register Prompt Validation",
        results: "Results",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filter by Validationtype",
        filterStatus: "Filter by Status",
        type1: "TYPE 1",
        type2: "TYPE 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Prompt Version Management",
        register: "Register Prompt Version",
        results: "Results",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filter by Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
      // New translations for migrated pages
      resourceUtilizationDashboard: "Resource Utilization Dashboard",
      resourceUtilizationDesc: "Monitor resource usage across all infrastructure",
      resourceUtilization: "Resource Utilization",
      totalResources: "Total Resources",
      active: "active",
      avgCpuUsage: "Avg CPU Usage",
      avgMemoryUsage: "Avg Memory Usage",
      avgStorageUsage: "Avg Storage Usage",
      networkUsage: "Network Usage",
      networkIn: "Network In",
      networkOut: "Network Out",
      resourceName: "Resource Name",
      resourceType: "Resource Type",
      cloudResources: "Cloud Resources",
      basicInformation: "Basic Information",
      resourceId: "Resource ID",
      cloudProvider: "Cloud Provider",
      zone: "Zone",
      autoScaling: "Auto Scaling",
      specifications: "Specifications",
      scalingConfiguration: "Scaling Configuration",
      minInstances: "Min Instances",
      currentInstances: "Current Instances",
      maxInstances: "Max Instances",
      cost: "Cost",
      costPerHour: "Cost per Hour",
      costPerMonth: "Cost per Month",
      metadata: "Metadata",
      createdAt: "Created At",
      updatedAt: "Updated At",
      instances: "Instances",
      noResourcesFound: "No resources found",
      cloudProviderOverview: "Cloud Provider Overview",
      cloudCredentialOverview: "Cloud Credential Overview",
      cloudRegionOverview: "Cloud Region Overview",
      cloudResourceOverview: "Cloud Resource Overview",
      kubernetesClusterOverview: "Kubernetes Cluster Overview",
      kubernetesNodeOverview: "Kubernetes Node Overview",
      gpuInstanceOverview: "GPU Instance Overview",
      infrastructureTemplateOverview: "Infrastructure Template Overview",
      infrastructureMetricOverview: "Infrastructure Metric Overview",
      infrastructureMetricsSummaryOverview: "Infrastructure Metrics Summary Overview",
      infrastructureCostOverview: "Infrastructure Cost Overview",
      infrastructureCostAnalysisOverview: "Infrastructure Cost Analysis Overview",
      infrastructureAuditOverview: "Infrastructure Audit Overview",
      infrastructureHealthMatrixOverview: "Infrastructure Health Matrix Overview",
      infrastructureOverviewOverview: "Infrastructure Overview",
      capacityForecastOverview: "Capacity Forecast Overview",
      resourceQuotaOverview: "Resource Quota Overview",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Monitoring
    monitoring: {
      title: "Monitoring",
      alerts: {
        overview: {
          title: "Monitoring Alerts",
          description: "Management of monitoring alerts",
          register: "Register Alert",
          list: "Alerts List",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Alert Detail",
          description: "Management and configuration of monitoring alert",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "System Alert Detail",
          description: "System alerts management",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Critical Alert Response",
          description: "Alert response form",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        detail: {
          title: "Metric Detail",
          description: "Monitoring metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // Serving Module
    serving: {
      deploymentInstance: {
        title: "Deployment Instance",
        register: "Registrar Deployment Instance",
        list: "Listado de Deployment Instances",
        detail: {
          title: "Deployment Instance",
          formTitle: "Información del Deployment Instance",
        },
        deploymentid: "Deployment Id",
        instanceid: "Instance ID",
        instancename: "Instance Name",
        instancetype: "Instance Type",
        status: "Status",
        ipaddress: "IP Address",
        port: "Port",
        nodename: "Node Name",
        resourceusage: "Resource Usage",
        startedat: "Started At",
        lastheartbeat: "Last Heartbeat",
        createdat: "Created At",
        updatedat: "Updated At",
      },
      model: {
        title: "Model",
        register: "Registrar Model",
        list: "Listado de Models",
        detail: {
          title: "Model",
          formTitle: "Información del Model",
        },
      },
      deploymentLog: {
        title: "Deployment Log",
        register: "Registrar Deployment Log",
        list: "Listado de Deployment Logs",
      },
      deploymentMetric: {
        title: "Deployment Metric",
        register: "Registrar Deployment Metric",
        list: "Listado de Deployment Metrics",
      },
      deploymentStatus: {
        title: "Deployment Status",
        register: "Registrar Deployment Status",
        list: "Listado de Deployment Status",
      },
      modelDeployment: {
        title: "Model Deployment",
        register: "Registrar Model Deployment",
        list: "Listado de Model Deployments",
      },
      modelMetrics: {
        title: "Model Metrics",
        register: "Registrar Model Metrics",
        list: "Listado de Model Metrics",
      },
      modelPrediction: {
        title: "Model Prediction",
        register: "Registrar Model Prediction",
        list: "Listado de Model Predictions",
      },
      modelVersion: {
        title: "Model Version",
        register: "Registrar Model Version",
        list: "Listado de Model Versions",
      },
      servingEndpoint: {
        title: "Serving Endpoint",
        register: "Registrar Serving Endpoint",
        list: "Listado de Serving Endpoints",
      },
      servingRequest: {
        title: "Serving Request",
        register: "Registrar Serving Request",
        list: "Listado de Serving Requests",
      },
      slaCompliance: {
        title: "SLA Compliance",
        register: "Registrar SLA Compliance",
        list: "Listado de SLA Compliance",
      },
      errorAnalysis: {
        title: "Error Analysis",
        register: "Registrar Error Analysis",
        list: "Listado de Error Analysis",
      },
      costBreakdown: {
        title: "Cost Breakdown",
        register: "Registrar Cost Breakdown",
        list: "Listado de Cost Breakdown",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Analytics Overview Management",
          list: "Analytics List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Analytics Metrics Management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          editTitle: "Edit Metric",
          createTitle: "New Metric",
          subtitle: "Analytics Metric management and configuration",
          formTitle: "Metric Data",
        },
        name: "Name",
        type: "Type",
        value: "Value",
        unit: "Unit",
        thresholdMin: "Min Threshold",
        thresholdMax: "Max Threshold",
        context: "Context",
        metadata: "Metadata",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          valueRequired: "Value is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving metric",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Analytics Reports Management",
          register: "Register Report",
          list: "Reports List",
        },
        detail: {
          editTitle: "Edit Report",
          createTitle: "New Report",
          subtitle: "Analytics Report management and configuration",
          formTitle: "Report Data",
        },
        name: "Name",
        type: "Type",
        description: "Description",
        data: "Data",
        config: "Config",
        generatedAt: "Generation Date",
        schedule: "Schedule",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          statusRequired: "Status is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving report",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Analytics Trends Management",
          list: "Trends List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Gestión de Analytics Overview",
          list: "Listado de Analytics",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Gestión de Analytics Metrics",
          register: "Registrar Métrica",
          list: "Listado de Métricas",
        },
        detail: {
          editTitle: "Editar Métrica",
          createTitle: "Nueva Métrica",
          subtitle: "Gestión y configuración de Analytics Metric",
          formTitle: "Datos de la Métrica",
        },
        name: "Nombre",
        type: "Tipo",
        value: "Valor",
        unit: "Unidad",
        thresholdMin: "Umbral Mín",
        thresholdMax: "Umbral Máximo",
        context: "Contexto",
        metadata: "Metadata",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          valueRequired: "El valor es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar la métrica",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Gestión de Analytics Reports",
          register: "Registrar Reporte",
          list: "Listado de Reportes",
        },
        detail: {
          editTitle: "Editar Reporte",
          createTitle: "Nuevo Reporte",
          subtitle: "Gestión y configuración de Analytics Report",
          formTitle: "Datos del Reporte",
        },
        name: "Nombre",
        type: "Tipo",
        description: "Descripción",
        data: "Datos",
        config: "Config",
        generatedAt: "Fecha Generación",
        schedule: "Programación",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          statusRequired: "El estado es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar el reporte",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Gestión de Analytics Trends",
          list: "Listado de Trends",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Dashboard de Portfolio de Proyectos",
        description: "Vista general del portfolio de proyectos",
        totalProjects: "Total Proyectos",
        active: "activos",
        budget: "Presupuesto",
        used: "utilizado",
        resources: "Recursos",
        utilization: "utilización",
        completion: "Completados",
        rate: "tasa",
        budgetBreakdown: "Desglose de Presupuesto",
        spent: "Gastado",
        remaining: "Restante",
        projectStatus: "Estado de Proyectos",
        onTime: "A Tiempo",
        inProgress: "En Progreso",
        atRisk: "En Riesgo",
      },
      overview: {
        title: "Proyectos",
        newProject: "Nuevo Proyecto",
        search: "Buscar proyectos...",
        progress: "Progreso",
        members: "miembros",
      },
      detail: {
        information: "Información General",
        domain: "Dominio",
        owner: "Propietario",
        startDate: "Fecha de Inicio",
        endDate: "Fecha de Fin",
        progress: "Progreso",
        completion: "Completado",
        daysRemaining: "días restantes",
        overdue: "Proyecto vencido",
        budget: "Presupuesto",
        total: "Total",
        spent: "Gastado",
        remaining: "Restante",
        team: "Equipo",
        teamSize: "Tamaño del Equipo",
        members: "miembros",
        metadata: "Metadata",
        createdAt: "Creado",
        updatedAt: "Actualizado",
      },
      status: {
        active: "Activo",
        completed: "Completado",
        "on-hold": "En Pausa",
        cancelled: "Cancelado",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Project Portfolio Dashboard",
        description: "Overview of the project portfolio",
        totalProjects: "Total Projects",
        active: "active",
        budget: "Budget",
        used: "used",
        resources: "Resources",
        utilization: "utilization",
        completion: "Completed",
        rate: "rate",
        budgetBreakdown: "Budget Breakdown",
        spent: "Spent",
        remaining: "Remaining",
        projectStatus: "Project Status",
        onTime: "On Time",
        inProgress: "In Progress",
        atRisk: "At Risk",
      },
      overview: {
        title: "Projects",
        newProject: "New Project",
        search: "Search projects...",
        progress: "Progress",
        members: "members",
      },
      detail: {
        information: "General Information",
        domain: "Domain",
        owner: "Owner",
        startDate: "Start Date",
        endDate: "End Date",
        progress: "Progress",
        completion: "Completed",
        daysRemaining: "days remaining",
        overdue: "Project overdue",
        budget: "Budget",
        total: "Total",
        spent: "Spent",
        remaining: "Remaining",
        team: "Team",
        teamSize: "Team Size",
        members: "members",
        metadata: "Metadata",
        createdAt: "Created",
        updatedAt: "Updated",
      },
      status: {
        active: "Active",
        completed: "Completed",
        "on-hold": "On Hold",
        cancelled: "Cancelled",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Agregando las traducciones en inglés. Buscando la sección en inglés:


Buscando la sección rag en inglés:


Agregando las traducciones en inglés:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
      noResults: "No se encontraron resultados",
      showing: "Mostrando",
      all: "TODOS",
      clearFilters: "Limpiar filtros",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Gestión de Prompt Performance Comparison",
        results: "Resultados",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Gestión de Prompt Validation",
        register: "Registrar Prompt Validation",
        results: "Resultados",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filtrar por Validationtype",
        filterStatus: "Filtrar por Status",
        type1: "TIPO 1",
        type2: "TIPO 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Gestión de Prompt Version",
        register: "Registrar Prompt Version",
        results: "Resultados",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filtrar por Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
      approval: {
        title: "Aprobaciones de Modelos",
        subtitle: "Gestión de solicitudes de aprobación pendientes",
      },
      biasAnalysis: {
        title: "Análisis de Sesgo",
        subtitle: "Gestión de análisis de sesgo",
      },
      dependencies: {
        title: "Dependencias",
        subtitle: "Gestión de dependencias de modelos",
      },
      explainability: {
        title: "Explicabilidad",
        subtitle: "Gestión de explicabilidad de modelos",
      },
      performance: {
        title: "Rendimiento",
        subtitle: "Gestión de rendimiento de modelos",
      },
      usageOverview: {
        title: "Uso de Modelos",
        subtitle: "Vista general del uso de modelos",
      },
      artifactOverview: {
        title: "Artefactos",
        subtitle: "Gestión de artefactos de modelos",
      },
      capabilityOverview: {
        title: "Capacidades",
        subtitle: "Gestión de capacidades de modelos",
      },
      catalogOverview: {
        title: "Catálogo",
        subtitle: "Gestión del catálogo de modelos",
      },
      endpointOverview: {
        title: "Endpoints",
        subtitle: "Gestión de endpoints de modelos",
      },
      providerCredentialOverview: {
        title: "Credenciales de Proveedor",
        subtitle: "Gestión de credenciales de proveedores",
      },
      providerOverview: {
        title: "Proveedores",
        subtitle: "Gestión de proveedores de modelos",
      },
      versioning: {
        title: "Versionado",
        subtitle: "Gestión de versiones de modelos",
      },
    },

    // Monitoring
    monitoring: {
      title: "Monitoreo",
      alerts: {
        overview: {
          title: "Alertas de Monitoreo",
          description: "Gestión de alertas de monitoreo",
          register: "Registrar Alerta",
          list: "Lista de Alertas",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Detalle de Alerta",
          description: "Gestión y configuración de alerta de monitoreo",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "Detalle de Alerta del Sistema",
          description: "Gestión de alertas del sistema",
          information: "Información",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Respuesta a Alerta Crítica",
          description: "Formulario de respuesta a alerta",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        detail: {
          title: "Detalle de Métrica",
          description: "Gestión de métricas de monitoreo",
          information: "Información",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
      noResults: "No results found",
      showing: "Showing",
      all: "ALL",
      clearFilters: "Clear filters",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Prompt Performance Comparison Management",
        results: "Results",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Prompt Validation Management",
        register: "Register Prompt Validation",
        results: "Results",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filter by Validationtype",
        filterStatus: "Filter by Status",
        type1: "TYPE 1",
        type2: "TYPE 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Prompt Version Management",
        register: "Register Prompt Version",
        results: "Results",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filter by Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
      // New translations for migrated pages
      resourceUtilizationDashboard: "Resource Utilization Dashboard",
      resourceUtilizationDesc: "Monitor resource usage across all infrastructure",
      resourceUtilization: "Resource Utilization",
      totalResources: "Total Resources",
      active: "active",
      avgCpuUsage: "Avg CPU Usage",
      avgMemoryUsage: "Avg Memory Usage",
      avgStorageUsage: "Avg Storage Usage",
      networkUsage: "Network Usage",
      networkIn: "Network In",
      networkOut: "Network Out",
      resourceName: "Resource Name",
      resourceType: "Resource Type",
      cloudResources: "Cloud Resources",
      basicInformation: "Basic Information",
      resourceId: "Resource ID",
      cloudProvider: "Cloud Provider",
      zone: "Zone",
      autoScaling: "Auto Scaling",
      specifications: "Specifications",
      scalingConfiguration: "Scaling Configuration",
      minInstances: "Min Instances",
      currentInstances: "Current Instances",
      maxInstances: "Max Instances",
      cost: "Cost",
      costPerHour: "Cost per Hour",
      costPerMonth: "Cost per Month",
      metadata: "Metadata",
      createdAt: "Created At",
      updatedAt: "Updated At",
      instances: "Instances",
      noResourcesFound: "No resources found",
      cloudProviderOverview: "Cloud Provider Overview",
      cloudCredentialOverview: "Cloud Credential Overview",
      cloudRegionOverview: "Cloud Region Overview",
      cloudResourceOverview: "Cloud Resource Overview",
      kubernetesClusterOverview: "Kubernetes Cluster Overview",
      kubernetesNodeOverview: "Kubernetes Node Overview",
      gpuInstanceOverview: "GPU Instance Overview",
      infrastructureTemplateOverview: "Infrastructure Template Overview",
      infrastructureMetricOverview: "Infrastructure Metric Overview",
      infrastructureMetricsSummaryOverview: "Infrastructure Metrics Summary Overview",
      infrastructureCostOverview: "Infrastructure Cost Overview",
      infrastructureCostAnalysisOverview: "Infrastructure Cost Analysis Overview",
      infrastructureAuditOverview: "Infrastructure Audit Overview",
      infrastructureHealthMatrixOverview: "Infrastructure Health Matrix Overview",
      infrastructureOverviewOverview: "Infrastructure Overview",
      capacityForecastOverview: "Capacity Forecast Overview",
      resourceQuotaOverview: "Resource Quota Overview",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Monitoring
    monitoring: {
      title: "Monitoring",
      alerts: {
        overview: {
          title: "Monitoring Alerts",
          description: "Management of monitoring alerts",
          register: "Register Alert",
          list: "Alerts List",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
        },
        detail: {
          title: "Alert Detail",
          description: "Management and configuration of monitoring alert",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          alertData: "Alert Data",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
          acknowledgedBy: "Acknowledged By",
          resolvedAt: "Resolved At",
          resolutionNotes: "Resolution Notes",
        },
        systemDetail: {
          title: "System Alert Detail",
          description: "System alerts management",
          information: "Information",
          alertName: "Alert Name",
          alertType: "Alert Type",
          severity: "Severity",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          triggeredAt: "Triggered At",
          acknowledgedAt: "Acknowledged At",
        },
        responseForm: {
          title: "Critical Alert Response",
          description: "Alert response form",
          formData: "Form Data",
          decision: "Decision",
          decisionPlaceholder: "Enter decision...",
          notes: "Notes",
          notesPlaceholder: "Enter notes...",
        },
      },
      metrics: {
        detail: {
          title: "Metric Detail",
          description: "Monitoring metrics management",
          information: "Information",
          metricName: "Metric Name",
          metricType: "Metric Type",
          metricValue: "Metric Value",
          status: "Status",
          description: "Description",
          timestamps: "Timestamps",
          collectedAt: "Collected At",
        },
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // Serving Module
    serving: {
      deploymentInstance: {
        title: "Deployment Instance",
        register: "Registrar Deployment Instance",
        list: "Listado de Deployment Instances",
        detail: {
          title: "Deployment Instance",
          formTitle: "Información del Deployment Instance",
        },
        deploymentid: "Deployment Id",
        instanceid: "Instance ID",
        instancename: "Instance Name",
        instancetype: "Instance Type",
        status: "Status",
        ipaddress: "IP Address",
        port: "Port",
        nodename: "Node Name",
        resourceusage: "Resource Usage",
        startedat: "Started At",
        lastheartbeat: "Last Heartbeat",
        createdat: "Created At",
        updatedat: "Updated At",
      },
      model: {
        title: "Model",
        register: "Registrar Model",
        list: "Listado de Models",
        detail: {
          title: "Model",
          formTitle: "Información del Model",
        },
      },
      deploymentLog: {
        title: "Deployment Log",
        register: "Registrar Deployment Log",
        list: "Listado de Deployment Logs",
      },
      deploymentMetric: {
        title: "Deployment Metric",
        register: "Registrar Deployment Metric",
        list: "Listado de Deployment Metrics",
      },
      deploymentStatus: {
        title: "Deployment Status",
        register: "Registrar Deployment Status",
        list: "Listado de Deployment Status",
      },
      modelDeployment: {
        title: "Model Deployment",
        register: "Registrar Model Deployment",
        list: "Listado de Model Deployments",
      },
      modelMetrics: {
        title: "Model Metrics",
        register: "Registrar Model Metrics",
        list: "Listado de Model Metrics",
      },
      modelPrediction: {
        title: "Model Prediction",
        register: "Registrar Model Prediction",
        list: "Listado de Model Predictions",
      },
      modelVersion: {
        title: "Model Version",
        register: "Registrar Model Version",
        list: "Listado de Model Versions",
      },
      servingEndpoint: {
        title: "Serving Endpoint",
        register: "Registrar Serving Endpoint",
        list: "Listado de Serving Endpoints",
      },
      servingRequest: {
        title: "Serving Request",
        register: "Registrar Serving Request",
        list: "Listado de Serving Requests",
      },
      slaCompliance: {
        title: "SLA Compliance",
        register: "Registrar SLA Compliance",
        list: "Listado de SLA Compliance",
      },
      errorAnalysis: {
        title: "Error Analysis",
        register: "Registrar Error Analysis",
        list: "Listado de Error Analysis",
      },
      costBreakdown: {
        title: "Cost Breakdown",
        register: "Registrar Cost Breakdown",
        list: "Listado de Cost Breakdown",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
      healthDashboard: {
        title: "Index Health Dashboard",
        description: "Index Health Dashboard Management",
        metrics: "Health Metrics",
        totalItems: "Total Items",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      dataSources: {
        title: "Data Source",
        description: "Data Source Management",
        register: "Register Data Source",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Data Sources List",
        name: "Name",
        description: "Description",
        type: "Type",
        connectionConfig: "Connectionconfig",
        syncConfig: "Syncconfig",
        filterType: "Filter by Type",
        filterStatus: "Filter by Status",
        filterSyncStatus: "Filter by Syncstatus",
      },
      versioning: {
        title: "Version",
        description: "Version Management",
        register: "Register Version",
        total: "Total",
        active: "Active",
        pending: "Pending",
        inactive: "Inactive",
        list: "Versions List",
        version: "Version",
        description: "Description",
        configuration: "Configuration",
        embeddingModel: "Embeddingmodel",
        retrievalConfig: "Retrievalconfig",
        filterStatus: "Filter by Status",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Analytics Overview Management",
          list: "Analytics List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Analytics Metrics Management",
          register: "Register Metric",
          list: "Metrics List",
        },
        detail: {
          editTitle: "Edit Metric",
          createTitle: "New Metric",
          subtitle: "Analytics Metric management and configuration",
          formTitle: "Metric Data",
        },
        name: "Name",
        type: "Type",
        value: "Value",
        unit: "Unit",
        thresholdMin: "Min Threshold",
        thresholdMax: "Max Threshold",
        context: "Context",
        metadata: "Metadata",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          valueRequired: "Value is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving metric",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Analytics Reports Management",
          register: "Register Report",
          list: "Reports List",
        },
        detail: {
          editTitle: "Edit Report",
          createTitle: "New Report",
          subtitle: "Analytics Report management and configuration",
          formTitle: "Report Data",
        },
        name: "Name",
        type: "Type",
        description: "Description",
        data: "Data",
        config: "Config",
        generatedAt: "Generation Date",
        schedule: "Schedule",
        createdBy: "Created By",
        updatedBy: "Updated By",
        createdAt: "Creation Date",
        updatedAt: "Update Date",
        filter: {
          allTypes: "All Types",
          allStatus: "All Status",
        },
        errors: {
          nameRequired: "Name is required",
          statusRequired: "Status is required",
          createdByRequired: "Created by is required",
          createdAtRequired: "Creation date is required",
          saveError: "Error saving report",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Analytics Trends Management",
          list: "Trends List",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Analytics Module
    analytics: {
      overview: {
        dashboard: {
          title: "Analytics Overview",
          subtitle: "Gestión de Analytics Overview",
          list: "Listado de Analytics",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
      metrics: {
        overview: {
          title: "Analytics Metrics",
          subtitle: "Gestión de Analytics Metrics",
          register: "Registrar Métrica",
          list: "Listado de Métricas",
        },
        detail: {
          editTitle: "Editar Métrica",
          createTitle: "Nueva Métrica",
          subtitle: "Gestión y configuración de Analytics Metric",
          formTitle: "Datos de la Métrica",
        },
        name: "Nombre",
        type: "Tipo",
        value: "Valor",
        unit: "Unidad",
        thresholdMin: "Umbral Mín",
        thresholdMax: "Umbral Máximo",
        context: "Contexto",
        metadata: "Metadata",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          valueRequired: "El valor es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar la métrica",
        },
      },
      reports: {
        overview: {
          title: "Analytics Reports",
          subtitle: "Gestión de Analytics Reports",
          register: "Registrar Reporte",
          list: "Listado de Reportes",
        },
        detail: {
          editTitle: "Editar Reporte",
          createTitle: "Nuevo Reporte",
          subtitle: "Gestión y configuración de Analytics Report",
          formTitle: "Datos del Reporte",
        },
        name: "Nombre",
        type: "Tipo",
        description: "Descripción",
        data: "Datos",
        config: "Config",
        generatedAt: "Fecha Generación",
        schedule: "Programación",
        createdBy: "Creado Por",
        updatedBy: "Actualizado Por",
        createdAt: "Fecha Creación",
        updatedAt: "Fecha Actualización",
        filter: {
          allTypes: "Todos los Tipos",
          allStatus: "Todos los Estados",
        },
        errors: {
          nameRequired: "El nombre es requerido",
          statusRequired: "El estado es requerido",
          createdByRequired: "El creador es requerido",
          createdAtRequired: "La fecha de creación es requerida",
          saveError: "Error al guardar el reporte",
        },
      },
      trends: {
        overview: {
          title: "Analytics Trends",
          subtitle: "Gestión de Analytics Trends",
          list: "Listado de Trends",
        },
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        generatedAt: "Generated At",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
        new: "Nuevo Usuario",
        edit: "Editar Usuario",
        detailDescription: "Gestión y configuración de User",
        username: "Usuario",
        email: "Email",
        firstname: "Nombre",
        lastname: "Apellido",
        phone: "Teléfono",
        passwordhash: "Contraseña",
        departmentid: "ID Departamento",
        createdAt: "Created At",
        updatedAt: "Updated At",
        isactive: "Activo",
        islocked: "Bloqueado",
      },
      menus: {
        title: "Gestión de Menús",
        description: "Gestiona los menús del sistema",
        register: "Registrar Menú",
        list: "Lista de Menús",
      },
      roles: {
        title: "Gestión de Roles",
        description: "Gestiona los roles del sistema",
        register: "Registrar Rol",
        list: "Lista de Roles",
      },
      permissions: {
        title: "Gestión de Permisos",
        description: "Gestiona los permisos del sistema",
        register: "Registrar Permiso",
        list: "Lista de Permisos",
      },
      loginAttempts: {
        title: "Intentos de Login",
        description: "Historial de intentos de acceso al sistema",
        list: "Lista de Intentos",
      },
      userSessions: {
        title: "Sesiones de Usuario",
        description: "Gestiona las sesiones activas de usuarios",
        list: "Lista de Sesiones",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Dashboard de Portfolio de Proyectos",
        description: "Vista general del portfolio de proyectos",
        totalProjects: "Total Proyectos",
        active: "activos",
        budget: "Presupuesto",
        used: "utilizado",
        resources: "Recursos",
        utilization: "utilización",
        completion: "Completados",
        rate: "tasa",
        budgetBreakdown: "Desglose de Presupuesto",
        spent: "Gastado",
        remaining: "Restante",
        projectStatus: "Estado de Proyectos",
        onTime: "A Tiempo",
        inProgress: "En Progreso",
        atRisk: "En Riesgo",
      },
      overview: {
        title: "Proyectos",
        newProject: "Nuevo Proyecto",
        search: "Buscar proyectos...",
        progress: "Progreso",
        members: "miembros",
      },
      detail: {
        information: "Información General",
        domain: "Dominio",
        owner: "Propietario",
        startDate: "Fecha de Inicio",
        endDate: "Fecha de Fin",
        progress: "Progreso",
        completion: "Completado",
        daysRemaining: "días restantes",
        overdue: "Proyecto vencido",
        budget: "Presupuesto",
        total: "Total",
        spent: "Gastado",
        remaining: "Restante",
        team: "Equipo",
        teamSize: "Tamaño del Equipo",
        members: "miembros",
        metadata: "Metadata",
        createdAt: "Creado",
        updatedAt: "Actualizado",
      },
      status: {
        active: "Activo",
        completed: "Completado",
        "on-hold": "En Pausa",
        cancelled: "Cancelado",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
    projects: {
      portfolioDashboard: {
        title: "Project Portfolio Dashboard",
        description: "Overview of the project portfolio",
        totalProjects: "Total Projects",
        active: "active",
        budget: "Budget",
        used: "used",
        resources: "Resources",
        utilization: "utilization",
        completion: "Completed",
        rate: "rate",
        budgetBreakdown: "Budget Breakdown",
        spent: "Spent",
        remaining: "Remaining",
        projectStatus: "Project Status",
        onTime: "On Time",
        inProgress: "In Progress",
        atRisk: "At Risk",
      },
      overview: {
        title: "Projects",
        newProject: "New Project",
        search: "Search projects...",
        progress: "Progress",
        members: "members",
      },
      detail: {
        information: "General Information",
        domain: "Domain",
        owner: "Owner",
        startDate: "Start Date",
        endDate: "End Date",
        progress: "Progress",
        completion: "Completed",
        daysRemaining: "days remaining",
        overdue: "Project overdue",
        budget: "Budget",
        total: "Total",
        spent: "Spent",
        remaining: "Remaining",
        team: "Team",
        teamSize: "Team Size",
        members: "members",
        metadata: "Metadata",
        createdAt: "Created",
        updatedAt: "Updated",
      },
      status: {
        active: "Active",
        completed: "Completed",
        "on-hold": "On Hold",
        cancelled: "Cancelled",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Buscando la sección del menú RAG para agregar las nuevas entradas:


Agregando las nuevas entradas al menú RAG:


Agregando las nuevas entradas al menú:


```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Analytics",
    icon: "BarChart3",
    roles: ["admin", "developer", "ai_analytics", "business_analytics"],
    defaultPath: "/analytics/overview/dashboard",
    description: "Analytics y métricas del sistema",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    analytics: "Analytics",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Aprobaciones",
          href: `/model-management/approval`,
          icon: "ClipboardCheck",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Análisis de Sesgo",
          href: `/model-management/bias-analysis`,
          icon: "AlertTriangle",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Dependencias",
          href: `/model-management/dependencies`,
          icon: "Network",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Explicabilidad",
          href: `/model-management/explainability`,
          icon: "Lightbulb",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Rendimiento",
          href: `/model-management/performance`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Uso de Modelos",
          href: `/model-management/performance/usage-overview`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos (Registry)",
          href: `/model-management/registry/artifact-overview`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Capacidades",
          href: `/model-management/registry/capability-overview`,
          icon: "Target",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Catálogo",
          href: `/model-management/registry/catalog-overview`,
          icon: "BookOpen",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Endpoints",
          href: `/model-management/registry/endpoint-overview`,
          icon: "Globe",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Credenciales de Proveedor",
          href: `/model-management/registry/provider-credential-overview`,
          icon: "Key",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores (Registry)",
          href: `/model-management/registry/provider-overview`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Versionado",
          href: `/model-management/versioning`,
          icon: "GitBranch",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Deployment Instance",
          href: `/serving/deployment-instance`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Deployment Log",
          href: `/serving/deployment-log`,
          icon: "FileText",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Deployment Metric",
          href: `/serving/deployment-metric`,
          icon: "BarChart3",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Deployment Status",
          href: `/serving/deployment-status`,
          icon: "CheckCircle",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Model",
          href: `/serving/model`,
          icon: "Brain",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Model Deployment",
          href: `/serving/model-deployment`,
          icon: "Rocket",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Model Metrics",
          href: `/serving/model-metrics`,
          icon: "TrendingUp",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Model Prediction",
          href: `/serving/model-prediction`,
          icon: "Target",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Model Version",
          href: `/serving/model-version`,
          icon: "GitBranch",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Serving Endpoint",
          href: `/serving/serving-endpoint`,
          icon: "Globe",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Serving Request",
          href: `/serving/serving-request`,
          icon: "Send",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "SLA Compliance",
          href: `/serving/sla-compliance`,
          icon: "Shield",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Error Analysis",
          href: `/serving/error-analysis`,
          icon: "AlertTriangle",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Cost Breakdown",
          href: `/serving/cost-breakdown`,
          icon: "DollarSign",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Portfolio",
          href: `/projects/portfolio-dashboard`,
          icon: "BarChart3",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Lista de Proyectos",
          href: `/projects/list`,
          icon: "FolderKanban",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Health Dashboard",
          href: `/rag/monitoring/health-dashboard`,
          icon: "Heart",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Data Sources",
          href: `/rag/data-sources/overview`,
          icon: "Database",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Versioning",
          href: `/rag/versioning/overview`,
          icon: "CodeBranch",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Policies",
          href: `/governance`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
          isDefault: true,
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Provider Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect"],
        },
      ];
    case "Analytics":
      return [
        {
          name: "Analytics Overview",
          href: `/analytics/overview/dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer", "ai_analytics", "business_analytics"],
          isDefault: true,
        },
        {
          name: "Analytics Metrics",
          href: `/analytics/metrics/overview`,
          icon: "BarChart3",
          roles: ["admin", "developer", "ai_analytics", "business_analytics"],
        },
        {
          name: "Analytics Reports",
          href: `/analytics/reports/overview`,
          icon: "FileText",
          roles: ["admin", "developer", "ai_analytics", "business_analytics"],
        },
        {
          name: "Analytics Trends",
          href: `/analytics/trends/overview`,
          icon: "TrendingUp",
          roles: ["admin", "developer", "ai_analytics", "business_analytics"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
    case "Monitoring":
      return [
        {
          name: t("monitoring.alerts.overview.title", "Alertas de Monitoreo"),
          href: `/monitoring/alerts`,
          icon: "AlertTriangle",
          roles: ["admin", "developer", "devops", "architect", "project_manager"],
          isDefault: true,
        },
        {
          name: t("monitoring.alerts.systemDetail.title", "Alertas del Sistema"),
          href: `/monitoring/alerts/system`,
          icon: "Server",
          roles: ["admin", "developer", "devops", "architect"],
        },
        {
          name: t("monitoring.metrics.detail.title", "Métricas"),
          href: `/monitoring/metrics`,
          icon: "BarChart3",
          roles: ["admin", "developer", "devops", "architect"],
        },
        {
          name: t("monitoring.alerts.responseForm.title", "Respuesta a Alertas"),
          href: `/monitoring/alerts/response-form`,
          icon: "Send",
          roles: ["admin", "developer", "devops", "project_manager"],
        },
      ];
  }
};

```

Verificando errores en las traducciones y preparando un resumen:


Migración completada. Resumen:

## Migración completada

### Pantallas migradas (3)

1. Health Dashboard (`/rag/monitoring/health-dashboard`)
   - Página: `app/(app)/rag/monitoring/health-dashboard/page.tsx`
   - API Route: `app/api/rag/monitoring/health-dashboard/route.ts`
   - Funcionalidad: Dashboard de salud de índices RAG con métricas (total items, active, deployed, training, offline, avg score)
   - Estilo: "Wow Factor" con gradientes, glassmorphism y animaciones

2. Data Sources Overview (`/rag/data-sources/overview`)
   - Página: `app/(app)/rag/data-sources/overview/page.tsx`
   - API Route: `app/api/rag/data-sources/route.ts`
   - Funcionalidad: Listado de fuentes de datos con métricas (total, activos, pendientes, inactivos), filtros por tipo/status/sync status, y acciones CRUD
   - Estilo: "Wow Factor" con cards de métricas con gradientes

3. Versioning Overview (`/rag/versioning/overview`)
   - Página: `app/(app)/rag/versioning/overview/page.tsx`
   - API Route: `app/api/rag/versioning/route.ts`
   - Funcionalidad: Listado de versiones RAG con métricas, filtros por status, y acciones CRUD
   - Estilo: "Wow Factor" con diseño consistente

### Características implementadas

- Mock data: datos de prueba para todas las pantallas
- API routes mock: endpoints `/api/rag/*` con soporte de filtros y paginación
- Traducciones: español e inglés en `i18n.ts`
- Menú: 3 nuevas entradas en `modules.ts` con iconos apropiados
- Estilos "Wow Factor": gradientes, glassmorphism, animaciones y efectos hover
- Responsive: diseño adaptable a diferentes tamaños de pantalla
- Filtros y búsqueda: funcionalidad completa de filtrado en todas las pantallas

### Archivos creados/modificados

**Páginas Next.js:**
- `app/(app)/rag/monitoring/health-dashboard/page.tsx`
- `app/(app)/rag/data-sources/overview/page.tsx`
- `app/(app)/rag/versioning/overview/page.tsx`

**API Routes:**
- `app/api/rag/monitoring/health-dashboard/route.ts`
- `app/api/rag/data-sources/route.ts`
- `app/api/rag/versioning/route.ts`

**Configuración:**
- `app/config/i18n.ts` (traducciones agregadas)
- `app/config/modules.ts` (entradas de menú agregadas)

### Próximos pasos

1. Verificar en el navegador: las páginas están disponibles en:
   - `/rag/monitoring/health-dashboard`
   - `/rag/data-sources/overview`
   - `/rag/versioning/overview`

2. Desactivar mock: cuando esté operativo, cambiar `NEXT_PUBLIC_USE_MOCK=false` en `.env.local`

3. Integración backend: conectar las API routes con los servicios Java reales cuando estén disponibles

Todas las pantallas están listas para validación y demo.

