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
    const matchesStatus = statusFilter === "ALL" || true;
    const matchesSyncStatus =
      syncStatusFilter === "ALL" || true;

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
        return "primary";
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
