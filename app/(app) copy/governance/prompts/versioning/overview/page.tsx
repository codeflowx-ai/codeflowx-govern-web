"use client";
import { useTranslation } from "@/app/config/i18n";
import { Brain, Search, Eye, Trash2, GitBranch } from "lucide-react";
import { useState, useMemo } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";

interface VersionItem {
  idxpromptversion: number;
  prmversion: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
}

export default function PromptVersionOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  // Mock data
  const mockData: VersionItem[] = [
    {
      idxpromptversion: 1,
      prmversion: "1.0.0",
      prmdescription: "Versión inicial",
      prmcontent: "Contenido del prompt v1.0.0",
      prmparameters: "temperature=0.7, max_tokens=1000",
      prmchanges: "Creación inicial",
    },
    {
      idxpromptversion: 2,
      prmversion: "1.1.0",
      prmdescription: "Mejoras en claridad",
      prmcontent: "Contenido del prompt v1.1.0",
      prmparameters: "temperature=0.7, max_tokens=1200",
      prmchanges: "Mejoras en redacción",
    },
    {
      idxpromptversion: 3,
      prmversion: "1.2.0",
      prmdescription: "Optimización de tokens",
      prmcontent: "Contenido del prompt v1.2.0",
      prmparameters: "temperature=0.8, max_tokens=1500",
      prmchanges: "Optimización de parámetros",
    },
    {
      idxpromptversion: 4,
      prmversion: "2.0.0",
      prmdescription: "Refactorización completa",
      prmcontent: "Contenido del prompt v2.0.0",
      prmparameters: "temperature=0.75, max_tokens=2000",
      prmchanges: "Refactorización mayor",
    },
    {
      idxpromptversion: 5,
      prmversion: "2.1.0",
      prmdescription: "Corrección de bugs",
      prmcontent: "Contenido del prompt v2.1.0",
      prmparameters: "temperature=0.75, max_tokens=2000",
      prmchanges: "Corrección de errores menores",
    },
  ];

  const metrics = useMemo(() => {
    const total = mockData.length;
    const active = mockData.filter((item) => item.prmversion.startsWith("2")).length;
    const pending = 0;
    const inactive = mockData.filter((item) => item.prmversion.startsWith("1")).length;
    return { total, active, pending, inactive };
  }, []);

  const filteredItems = useMemo(() => {
    let filtered = mockData;
    if (searchTerm) {
      filtered = filtered.filter(
        (item) =>
          item.idxpromptversion.toString().includes(searchTerm) ||
          item.prmversion.toLowerCase().includes(searchTerm.toLowerCase()) ||
          item.prmdescription.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (statusFilter !== "ALL") {
      // TODO: Implementar filtro por status cuando esté disponible
    }
    return filtered;
  }, [searchTerm, statusFilter]);

  const paginatedItems = useMemo(() => {
    const start = (currentPage - 1) * pageSize;
    return filteredItems.slice(start, start + pageSize);
  }, [filteredItems, currentPage]);

  const totalPages = Math.ceil(filteredItems.length / pageSize);

  const handleViewDetails = (itemId: number) => {
    // TODO: Implementar navegación a detalles
    console.log("View details for item:", itemId);
  };

  const handleDelete = (itemId: number) => {
    // TODO: Implementar eliminación
    console.log("Delete item:", itemId);
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <GitBranch className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.prompts.versioning.title", "Prompt Versioning")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t(
            "governance.prompts.versioning.subtitle",
            "Gestión de versiones de prompts de IA"
          )}
        </p>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.versioning.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.total}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.versioning.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.active}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.versioning.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.pending}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.versioning.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.inactive}</h2>
          </CardContent>
        </Card>
      </div>

      {/* Filtros */}
      <Card>
        <CardContent className="p-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder={t("governance.prompts.versioning.searchPlaceholder", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    setCurrentPage(1);
                  }
                }}
                className="pl-10"
              />
            </div>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger>
                <SelectValue
                  placeholder={t(
                    "governance.prompts.versioning.filterStatus",
                    "Filtrar por Estado"
                  )}
                />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">
                  {t("governance.prompts.versioning.all", "Todos")}
                </SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Tabla de resultados */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.prompts.versioning.list.title", "Listado")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          {paginatedItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.prompts.versioning.list.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.id", "ID")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.version", "Versión")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.description", "Descripción")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.content", "Contenido")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.parameters", "Parámetros")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.versioning.list.changes", "Cambios")}
                    </th>
                    <th className="text-center p-4">
                      {t("governance.prompts.versioning.list.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedItems.map((item) => (
                    <tr
                      key={item.idxpromptversion}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors cursor-pointer"
                      onClick={() => handleViewDetails(item.idxpromptversion)}
                    >
                      <td className="p-4">{item.idxpromptversion}</td>
                      <td className="p-4">
                        <Badge variant="outline" className="font-semibold">
                          {item.prmversion}
                        </Badge>
                      </td>
                      <td className="p-4">{item.prmdescription}</td>
                      <td className="p-4 max-w-[200px] truncate">{item.prmcontent}</td>
                      <td className="p-4 max-w-[200px] truncate">{item.prmparameters}</td>
                      <td className="p-4 max-w-[200px] truncate">{item.prmchanges}</td>
                      <td className="p-4">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleViewDetails(item.idxpromptversion);
                            }}
                            title={t("common.viewDetails", "Ver detalles")}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="text-red-400 hover:text-red-300"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleDelete(item.idxpromptversion);
                            }}
                            title={t("common.delete", "Eliminar")}
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
          {/* Paginación */}
          {totalPages > 1 && (
            <div className="flex items-center justify-between mt-4">
              <div className="text-sm text-muted-foreground">
                {t("governance.prompts.versioning.list.showing", "Mostrando")}{" "}
                {(currentPage - 1) * pageSize + 1} -{" "}
                {Math.min(currentPage * pageSize, filteredItems.length)}{" "}
                {t("governance.prompts.versioning.list.of", "de")}{" "}
                {filteredItems.length}
              </div>
              <div className="flex gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setCurrentPage((p) => Math.max(1, p - 1))}
                  disabled={currentPage === 1}
                >
                  {t("common.previous", "Anterior")}
                </Button>
                <div className="flex items-center gap-2">
                  {Array.from({ length: totalPages }, (_, i) => i + 1)
                    .filter(
                      (page) =>
                        page === 1 ||
                        page === totalPages ||
                        (page >= currentPage - 1 && page <= currentPage + 1)
                    )
                    .map((page, index, array) => (
                      <div key={page} className="flex items-center gap-1">
                        {index > 0 && array[index - 1] !== page - 1 && (
                          <span className="px-2">...</span>
                        )}
                        <Button
                          variant={currentPage === page ? "primary" : "outline"}
                          size="sm"
                          onClick={() => setCurrentPage(page)}
                        >
                          {page}
                        </Button>
                      </div>
                    ))}
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() =>
                    setCurrentPage((p) => Math.min(totalPages, p + 1))
                  }
                  disabled={currentPage === totalPages}
                >
                  {t("common.next", "Siguiente")}
                </Button>
              </div>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
