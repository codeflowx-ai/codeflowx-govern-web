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
  GitBranch,
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
            <GitBranch className="w-8 h-8 text-primary" />
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
              <GitBranch className="w-5 h-5 text-primary" />
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
