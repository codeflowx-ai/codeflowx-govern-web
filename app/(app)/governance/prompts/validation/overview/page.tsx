"use client";
import { useTranslation } from "@/app/config/i18n";
import { Brain, Search, Eye, Trash2 } from "lucide-react";
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
interface ValidationItem {
  idxpromptvalidation: number;
  prmvalidationtype: string;
  prmvalidationresult: string;
  prmvalidationscore: number;
  prmvalidationdetails: string;
  prmissuesfound: number;
}
export default function PromptValidationOverviewPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [validationTypeFilter, setValidationTypeFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;
  // Mock data
  const mockData: ValidationItem[] = [
    {
      idxpromptvalidation: 1,
      prmvalidationtype: "ACTIVE",
      prmvalidationresult: "PASSED",
      prmvalidationscore: 9.5,
      prmvalidationdetails: "Validación exitosa",
      prmissuesfound: 0,
    },
    {
      idxpromptvalidation: 2,
      prmvalidationtype: "INACTIVE",
      prmvalidationresult: "FAILED",
      prmvalidationscore: 5.2,
      prmvalidationdetails: "Errores encontrados",
      prmissuesfound: 3,
    },
    {
      idxpromptvalidation: 3,
      prmvalidationtype: "ACTIVE",
      prmvalidationresult: "PASSED",
      prmvalidationscore: 8.8,
      prmvalidationdetails: "Validación correcta",
      prmissuesfound: 0,
    },
    {
      idxpromptvalidation: 4,
      prmvalidationtype: "PENDING",
      prmvalidationresult: "PENDING",
      prmvalidationscore: 0,
      prmvalidationdetails: "En proceso",
      prmissuesfound: 0,
    },
    {
      idxpromptvalidation: 5,
      prmvalidationtype: "ACTIVE",
      prmvalidationresult: "PASSED",
      prmvalidationscore: 9.8,
      prmvalidationdetails: "Excelente validación",
      prmissuesfound: 0,
    },
  ];
  const metrics = useMemo(() => {
    const total = mockData.length;
    const active = mockData.filter((item) => item.prmvalidationtype === "ACTIVE").length;
    const pending = mockData.filter((item) => item.prmvalidationtype === "PENDING").length;
    const inactive = mockData.filter((item) => item.prmvalidationtype === "INACTIVE").length;
    return { total, active, pending, inactive };
  }, []);
  const filteredItems = useMemo(() => {
    let filtered = mockData;
    if (searchTerm) {
      filtered = filtered.filter(
        (item) =>
          item.idxpromptvalidation.toString().includes(searchTerm) ||
          item.prmvalidationdetails.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    if (validationTypeFilter !== "ALL") {
      filtered = filtered.filter(
        (item) => item.prmvalidationtype === validationTypeFilter
      );
    }
    if (statusFilter !== "ALL") {
      filtered = filtered.filter(
        (item) => item.prmvalidationresult === statusFilter
      );
    }
    return filtered;
  }, [searchTerm, validationTypeFilter, statusFilter]);
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
  const getStatusBadgeVariant = (type: string): "primary" | "secondary" | "outline" => {
    if (type === "ACTIVE") return "primary";
    if (type === "INACTIVE") return "secondary";
    return "outline";
  };
  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Brain className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.prompts.validation.title", "Prompt Validation")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t(
            "governance.prompts.validation.subtitle",
            "Gestión y validación de prompts de IA"
          )}
        </p>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.validation.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.total}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.validation.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.active}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.validation.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.pending}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.validation.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.inactive}</h2>
          </CardContent>
        </Card>
      </div>
      {/* Filtros */}
      <Card>
        <CardContent className="p-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder={t("governance.prompts.validation.searchPlaceholder", "Buscar...")}
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
            <Select
              value={validationTypeFilter}
              onValueChange={setValidationTypeFilter}
            >
              <SelectTrigger>
                <SelectValue
                  placeholder={t(
                    "governance.prompts.validation.filterType",
                    "Filtrar por Tipo"
                  )}
                />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">
                  {t("governance.prompts.validation.all", "Todos")}
                </SelectItem>
                <SelectItem value="ACTIVE">
                  {t("governance.prompts.validation.type.active", "Activo")}
                </SelectItem>
                <SelectItem value="INACTIVE">
                  {t("governance.prompts.validation.type.inactive", "Inactivo")}
                </SelectItem>
                <SelectItem value="PENDING">
                  {t("governance.prompts.validation.type.pending", "Pendiente")}
                </SelectItem>
              </SelectContent>
            </Select>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger>
                <SelectValue
                  placeholder={t(
                    "governance.prompts.validation.filterStatus",
                    "Filtrar por Estado"
                  )}
                />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">
                  {t("governance.prompts.validation.all", "Todos")}
                </SelectItem>
                <SelectItem value="PASSED">
                  {t("governance.prompts.validation.status.passed", "Aprobado")}
                </SelectItem>
                <SelectItem value="FAILED">
                  {t("governance.prompts.validation.status.failed", "Fallido")}
                </SelectItem>
                <SelectItem value="PENDING">
                  {t("governance.prompts.validation.status.pending", "Pendiente")}
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
            {t("governance.prompts.validation.list.title", "Listado")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          {paginatedItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.prompts.validation.list.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.id", "ID")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.type", "Tipo")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.result", "Resultado")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.score", "Score")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.details", "Detalles")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.validation.list.issues", "Problemas")}
                    </th>
                    <th className="text-center p-4">
                      {t("governance.prompts.validation.list.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedItems.map((item) => (
                    <tr
                      key={item.idxpromptvalidation}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors cursor-pointer"
                      onClick={() => handleViewDetails(item.idxpromptvalidation)}
                    >
                      <td className="p-4">{item.idxpromptvalidation}</td>
                      <td className="p-4">
                        <Badge
                          variant={getStatusBadgeVariant(item.prmvalidationtype)}
                        >
                          {item.prmvalidationtype}
                        </Badge>
                      </td>
                      <td className="p-4">{item.prmvalidationresult}</td>
                      <td className="p-4">{item.prmvalidationscore.toFixed(1)}</td>
                      <td className="p-4">{item.prmvalidationdetails}</td>
                      <td className="p-4">{item.prmissuesfound}</td>
                      <td className="p-4">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleViewDetails(item.idxpromptvalidation);
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
                              handleDelete(item.idxpromptvalidation);
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
                {t("governance.prompts.validation.list.showing", "Mostrando")}{" "}
                {(currentPage - 1) * pageSize + 1} -{" "}
                {Math.min(currentPage * pageSize, filteredItems.length)}{" "}
                {t("governance.prompts.validation.list.of", "de")}{" "}
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
