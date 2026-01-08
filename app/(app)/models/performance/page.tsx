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
import { SimpleModal } from "@/components/ui/SimpleModal";
import { BarChart3, Eye, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
interface ModelComparison {
  id: number;
  comparisonName: string;
  modelIds: string;
  versionIds: string;
  comparisonMetrics: string;
  comparisonResults: string;
}
export default function PerformancePage() {
  const { t } = useTranslation();
  const [items, setItems] = useState<ModelComparison[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [comparisonTypeFilter, setComparisonTypeFilter] = useState<string>("ALL");
  const [selectedItem, setSelectedItem] = useState<ModelComparison | null>(null);
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
      const mockData: ModelComparison[] = [
        {
          id: 1,
          comparisonName: "GPT-4 vs Claude",
          modelIds: "1,2",
          versionIds: "1.0,2.0",
          comparisonMetrics: "Accuracy, Latency",
          comparisonResults: "GPT-4: 95%, Claude: 93%",
        },
      ];
      setItems(mockData);
      setStats({
        total: mockData.length,
        active: 1,
        pending: 0,
        inactive: 0,
      });
    } catch (error) {
      console.error("Error loading data:", error);
    } finally {
      setLoading(false);
    }
  };
  const filteredItems = items.filter((item) =>
    item.comparisonName.toLowerCase().includes(searchQuery.toLowerCase())
  );
  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <BarChart3 className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.performance.title", "Rendimiento")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t("governance.models.performance.subtitle", "Gestión de rendimiento de modelos")}
        </p>
      </div>
      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.performance.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.performance.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.active}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.performance.metrics.pending", "Pendientes")}
            </p>
            <h2 className="text-2xl font-bold">{stats.pending}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.performance.metrics.inactive", "Inactivos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.inactive}</h2>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <Card className="border-2">
        <CardContent className="p-4">
          <div className="flex gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder={t("governance.models.performance.filters.search", "Buscar...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={comparisonTypeFilter} onValueChange={setComparisonTypeFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.performance.filters.comparisonType", "Filtrar por Comparisontype")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.performance.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="TYPE1">{t("governance.models.performance.filters.type1", "TIPO 1")}</SelectItem>
                <SelectItem value="TYPE2">{t("governance.models.performance.filters.type2", "TIPO 2")}</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>
      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.performance.table.title", "Listado")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.performance.table.noResults", "No hay datos")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.performance.table.id", "ID")}</th>
                    <th className="text-left p-2">{t("governance.models.performance.table.comparisonName", "Comparisonname")}</th>
                    <th className="text-left p-2">{t("governance.models.performance.table.modelIds", "Model Id S")}</th>
                    <th className="text-left p-2">{t("governance.models.performance.table.versionIds", "Version Id S")}</th>
                    <th className="text-left p-2">{t("governance.models.performance.table.comparisonMetrics", "Comparisonmetrics")}</th>
                    <th className="text-left p-2">{t("governance.models.performance.table.comparisonResults", "Comparisonresults")}</th>
                    <th className="text-center p-2">{t("governance.models.performance.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.comparisonName}</td>
                      <td className="p-2">{item.modelIds}</td>
                      <td className="p-2">{item.versionIds}</td>
                      <td className="p-2">{item.comparisonMetrics}</td>
                      <td className="p-2">{item.comparisonResults}</td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button 
                            variant="ghost" 
                            size="sm" 
                            title={t("common.view", "Ver")}
                            onClick={() => setSelectedItem(item)}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button 
                            variant="ghost" 
                            size="sm" 
                            className="text-red-400 hover:text-red-300" 
                            title={t("common.delete", "Eliminar")}
                            onClick={() => {
                              // TODO: Implementar eliminación
                              console.log("Delete comparison:", item.id);
                            }}
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
        </CardContent>
      </Card>

      {/* Modal de Detalles */}
      <SimpleModal
        isOpen={selectedItem !== null}
        onClose={() => setSelectedItem(null)}
        title={selectedItem ? `${t("governance.models.performance.detail.title", "Detalles de la Comparación")} - ${selectedItem.comparisonName}` : ""}
        maxWidth="max-w-5xl"
      >
        {selectedItem && (
          <div className="space-y-6">
            {/* Información General */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.performance.detail.general", "Información General")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.performance.detail.comparisonName", "Nombre de la Comparación")}
                    </label>
                    <p className="text-base font-semibold">{selectedItem.comparisonName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.performance.detail.id", "ID")}
                    </label>
                    <p className="text-base">{selectedItem.id}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Modelos Comparados */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.performance.detail.models", "Modelos Comparados")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.performance.detail.modelIds", "IDs de Modelos")}
                  </label>
                  <p className="text-base font-mono">{selectedItem.modelIds}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.performance.detail.versionIds", "IDs de Versiones")}
                  </label>
                  <p className="text-base font-mono">{selectedItem.versionIds}</p>
                </div>
              </CardContent>
            </Card>

            {/* Métricas de Comparación */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.performance.detail.metrics", "Métricas de Comparación")}</CardTitle>
              </CardHeader>
              <CardContent>
                <div>
                  <label className="text-sm font-medium text-muted-foreground mb-2 block">
                    {t("governance.models.performance.detail.comparisonMetrics", "Métricas Evaluadas")}
                  </label>
                  <div className="flex flex-wrap gap-2">
                    {selectedItem.comparisonMetrics.split(", ").map((metric, idx) => (
                      <Badge key={idx} variant="outline" className="text-sm">
                        {metric}
                      </Badge>
                    ))}
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Resultados de la Comparación */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.performance.detail.results", "Resultados de la Comparación")}</CardTitle>
              </CardHeader>
              <CardContent>
                <div>
                  <label className="text-sm font-medium text-muted-foreground mb-2 block">
                    {t("governance.models.performance.detail.comparisonResults", "Resultados")}
                  </label>
                  <div className="bg-muted/50 p-4 rounded-lg">
                    <p className="text-base whitespace-pre-line">{selectedItem.comparisonResults}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}

