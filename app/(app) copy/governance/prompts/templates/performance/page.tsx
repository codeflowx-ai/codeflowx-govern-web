"use client";
import { useTranslation } from "@/app/config/i18n";
import { Brain, Search, Eye, TrendingUp } from "lucide-react";
import { useState, useMemo } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

interface PerformanceItem {
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}

export default function PromptPerformanceComparisonPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  // Mock data
  const mockData: PerformanceItem[] = [
    {
      totalItems: 150,
      activeItems: 120,
      deployedItems: 80,
      trainingItems: 30,
      offlineItems: 10,
      avgScore: 8.5,
    },
    {
      totalItems: 200,
      activeItems: 180,
      deployedItems: 150,
      trainingItems: 20,
      offlineItems: 0,
      avgScore: 9.2,
    },
    {
      totalItems: 95,
      activeItems: 70,
      deployedItems: 50,
      trainingItems: 15,
      offlineItems: 10,
      avgScore: 7.8,
    },
    {
      totalItems: 300,
      activeItems: 280,
      deployedItems: 250,
      trainingItems: 20,
      offlineItems: 0,
      avgScore: 9.5,
    },
    {
      totalItems: 85,
      activeItems: 60,
      deployedItems: 40,
      trainingItems: 20,
      offlineItems: 5,
      avgScore: 7.2,
    },
  ];

  const metrics = useMemo(() => {
    const total = mockData.reduce((sum, item) => sum + item.totalItems, 0);
    const active = mockData.reduce((sum, item) => sum + item.activeItems, 0);
    const deployed = mockData.reduce((sum, item) => sum + item.deployedItems, 0);
    const avgScore = mockData.reduce((sum, item) => sum + item.avgScore, 0) / mockData.length;
    return { total, active, deployed, avgScore };
  }, []);

  const filteredItems = useMemo(() => {
    if (!searchTerm) return mockData;
    return mockData.filter((item) =>
      item.totalItems.toString().includes(searchTerm)
    );
  }, [searchTerm]);

  const paginatedItems = useMemo(() => {
    const start = (currentPage - 1) * pageSize;
    return filteredItems.slice(start, start + pageSize);
  }, [filteredItems, currentPage]);

  const totalPages = Math.ceil(filteredItems.length / pageSize);

  const handleViewDetails = (itemId: number) => {
    // TODO: Implementar navegación a detalles
    console.log("View details for item:", itemId);
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <TrendingUp className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.prompts.performance.title", "Prompt Performance")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {t(
            "governance.prompts.performance.subtitle",
            "Comparación de rendimiento de prompts de IA"
          )}
        </p>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.performance.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.total}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.performance.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.active}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.performance.metrics.deployed", "Desplegados")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.deployed}</h2>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.prompts.performance.metrics.avgScore", "Score Promedio")}
            </p>
            <h2 className="text-2xl font-bold">{metrics.avgScore.toFixed(1)}</h2>
          </CardContent>
        </Card>
      </div>

      {/* Filtros */}
      <Card>
        <CardContent className="p-4">
          <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder={t("governance.prompts.performance.searchPlaceholder", "Buscar...")}
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
          </div>
        </CardContent>
      </Card>

      {/* Tabla de resultados */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.prompts.performance.list.title", "Listado")}
          </CardTitle>
        </CardHeader>
        <CardContent>
          {paginatedItems.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.prompts.performance.list.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.totalItems", "Total Items")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.activeItems", "Activos")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.deployedItems", "Desplegados")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.trainingItems", "En Entrenamiento")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.offlineItems", "Offline")}
                    </th>
                    <th className="text-left p-4">
                      {t("governance.prompts.performance.list.avgScore", "Score Promedio")}
                    </th>
                    <th className="text-center p-4">
                      {t("governance.prompts.performance.list.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {paginatedItems.map((item, index) => (
                    <tr
                      key={index}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors cursor-pointer"
                      onClick={() => handleViewDetails(item.totalItems)}
                    >
                      <td className="p-4">{item.totalItems}</td>
                      <td className="p-4">{item.activeItems}</td>
                      <td className="p-4">{item.deployedItems}</td>
                      <td className="p-4">{item.trainingItems}</td>
                      <td className="p-4">{item.offlineItems}</td>
                      <td className="p-4 font-semibold">{item.avgScore.toFixed(1)}</td>
                      <td className="p-4">
                        <div className="flex items-center justify-center">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              handleViewDetails(item.totalItems);
                            }}
                            title={t("common.viewDetails", "Ver detalles")}
                          >
                            <Eye className="w-4 h-4" />
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
                {t("governance.prompts.performance.list.showing", "Mostrando")}{" "}
                {(currentPage - 1) * pageSize + 1} -{" "}
                {Math.min(currentPage * pageSize, filteredItems.length)}{" "}
                {t("governance.prompts.performance.list.of", "de")}{" "}
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
