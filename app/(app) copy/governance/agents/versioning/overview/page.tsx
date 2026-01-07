"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { GitBranch, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface Version {
  id: number;
  versionNumber: string;
  status: string;
  releaseDate: string;
  changes: number;
}

export default function VersioningOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [versions, setVersions] = useState<Version[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/versioning/list");
      const data = await response.json();
      setVersions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading versions:", error);
      setVersions([
        { id: 1, versionNumber: "v2.1.0", status: "ACTIVE", releaseDate: "2024-01-15", changes: 45 },
        { id: 2, versionNumber: "v2.2.0", status: "PENDING", releaseDate: "2024-01-20", changes: 32 },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredVersions = versions.filter((item) =>
    item.versionNumber.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <GitBranch className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <GitBranch className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.versioning.title", "Versionado")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.versioning.subtitle", "Gestión de versionado")}
          </p>
        </div>
        <Button variant="outline" size="sm" className="flex items-center gap-2" onClick={() => router.push("/governance/agents/versioning/create")}>
            <Plus className="h-4 w-4" />
            {t("agents.versioning.register", "Registrar Versión")}
          </Button>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-3 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardBody className="p-4">
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t("common.search", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
              />
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.versioning.list", "Listado de Versiones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredVersions.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.versioning.versionNumber", "Version Number")}</th>
                    <th className="text-left p-2">{t("common.status", "Status")}</th>
                    <th className="text-left p-2">{t("agents.versioning.releaseDate", "Release Date")}</th>
                    <th className="text-left p-2">{t("agents.versioning.changes", "Changes")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredVersions.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {item.versionNumber}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            item.status === "ACTIVE"
                              ? "bg-green-500/20 text-green-500"
                              : "bg-yellow-500/20 text-yellow-500"
                          }`}
                        >
                          {item.status}
                        </span>
                      </td>
                      <td className="p-2">{item.releaseDate}</td>
                      <td className="p-2">{item.changes}</td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
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
        </CardBody>
      </Card>
    </div>
  );
}


