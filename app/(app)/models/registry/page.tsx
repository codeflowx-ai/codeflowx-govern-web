"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { BookOpen, Eye, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
interface Catalog {
  id: number;
  modelName: string;
  modelId: string;
  displayName: string;
  description: string;
  version: string;
  status?: string;
  type?: string;
  framework?: string;
}
export default function CatalogOverviewPage() {
  const { t } = useTranslation();
  const [items, setItems] = useState<Catalog[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [licenseTypeFilter, setLicenseTypeFilter] = useState<string>("ALL");
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    pending: 0,
    inactive: 0,
  });
  useEffect(() => {
    loadData();
  }, []);
  const loadData = async () => {
    try {
      setLoading(true);
      const searchParams = new URLSearchParams();
      if (searchQuery) searchParams.append("search", searchQuery);
      if (statusFilter !== "ALL") searchParams.append("status", statusFilter);
      if (licenseTypeFilter !== "ALL") searchParams.append("licenseType", licenseTypeFilter);

      const response = await fetch(
        `/api/v1/bff/compliance/models${searchParams.toString() ? `?${searchParams.toString()}` : ""}`
      );

      if (response.ok) {
        const data: Catalog[] = await response.json();
        setItems(data);
        setStats({
          total: data.length,
          active: data.filter((m) => m.status === "ACTIVE").length,
          pending: data.filter((m) => m.status === "PENDING").length,
          inactive: data.filter((m) => m.status === "INACTIVE").length,
        });
      } else {
        console.error("Error loading models:", response.statusText);
      }
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };
  const filteredItems = items.filter((item) =>
    item.modelName.toLowerCase().includes(searchQuery.toLowerCase())
  );
  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <BookOpen className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.registry.title", "Registro de Modelos")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9 mb-2">
          {t("governance.models.registry.subtitle", "Catálogo de modelos registrados en el sistema")}
        </p>
      </div>

      {/* Action Buttons */}
      <div className="flex items-center justify-end">
        <Button
          onClick={() => { window.location.href = "/models/registry/new"; }}
          className="bg-primary hover:bg-primary/90"
        >
          <Plus className="h-4 w-4 mr-2" />
          {t("governance.models.registry.addButton", "Agregar Modelo")}
        </Button>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.registry.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.registry.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.active}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.registry.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{stats.pending}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.registry.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.inactive}</h2>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.registry.filters.search", "Filtros")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.registry.filters.search", "Búsqueda")}
              </label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.models.registry.filters.searchPlaceholder", "Buscar...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.registry.filters.status", "Estado")}
              </label>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.registry.filters.statusPlaceholder", "Filtrar por Status")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.registry.filters.all", "TODOS")}</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.models.registry.filters.licenseType", "Tipo de Licencia")}
              </label>
              <Select value={licenseTypeFilter} onValueChange={setLicenseTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.models.registry.filters.licenseTypePlaceholder", "Filtrar por Tipo de Licencia")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.models.registry.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="TYPE1">{t("governance.models.registry.filters.type1", "TIPO 1")}</SelectItem>
                  <SelectItem value="TYPE2">{t("governance.models.registry.filters.type2", "TIPO 2")}</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>
      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.registry.table.title", "Listado")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.registry.table.noResults", "No hay datos")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.registry.table.id", "ID")}</th>
                    <th className="text-left p-2">{t("governance.models.registry.table.modelName", "Model Name")}</th>
                    <th className="text-left p-2">{t("governance.models.registry.table.modelId", "Model Id")}</th>
                    <th className="text-left p-2">{t("governance.models.registry.table.displayName", "Displayname")}</th>
                    <th className="text-left p-2">{t("governance.models.registry.table.description", "Description")}</th>
                    <th className="text-left p-2">{t("governance.models.registry.table.version", "Version")}</th>
                    <th className="text-center p-2">{t("governance.models.registry.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.modelName}</td>
                      <td className="p-2">{item.modelId}</td>
                      <td className="p-2">{item.displayName}</td>
                      <td className="p-2">{item.description}</td>
                      <td className="p-2">{item.version}</td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            title={t("common.edit", "Editar")}
                            onClick={() => { window.location.href = `/models/registry/${item.id}?edit=true`; }}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm" className="text-red-400 hover:text-red-300" title={t("common.delete", "Eliminar")}>
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
        </CardContent>
      </Card>
    </div>
  );
}
