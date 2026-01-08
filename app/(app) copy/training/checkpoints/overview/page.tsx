"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Save, Plus, Search, Eye, Trash2, X } from "lucide-react";
import { useState, useEffect } from "react";

interface Checkpoint {
  id: number;
  checkpointName: string;
  epoch: number;
  step: number;
  checkpointType: string;
  path: string;
}

const mockCheckpoints: Checkpoint[] = [
  {
    id: 1,
    checkpointName: "checkpoint_epoch_10",
    epoch: 10,
    step: 1000,
    checkpointType: "ACTIVE",
    path: "/checkpoints/epoch_10",
  },
  {
    id: 2,
    checkpointName: "checkpoint_epoch_20",
    epoch: 20,
    step: 2000,
    checkpointType: "ACTIVE",
    path: "/checkpoints/epoch_20",
  },
  {
    id: 3,
    checkpointName: "checkpoint_epoch_30",
    epoch: 30,
    step: 3000,
    checkpointType: "INACTIVE",
    path: "/checkpoints/epoch_30",
  },
];

export default function CheckpointsOverviewPage() {
  const { t } = useTranslation();
  const [checkpoints, setCheckpoints] = useState<Checkpoint[]>(mockCheckpoints);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState<string>("ALL");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: checkpoints.length,
      activeItems: checkpoints.filter((c) => c.checkpointType === "ACTIVE").length,
      pendingApproval: 0,
      inactiveItems: checkpoints.filter((c) => c.checkpointType === "INACTIVE").length,
    });
  }, [checkpoints]);

  const filteredCheckpoints = checkpoints.filter((checkpoint) => {
    const matchesSearch =
      checkpoint.checkpointName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      checkpoint.path.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "ALL" || checkpoint.checkpointType === typeFilter;
    return matchesSearch && matchesType;
  });

  const getTypeBadge = (type: string) => {
    return type === "ACTIVE"
      ? "bg-green-500/20 text-green-500 border-green-500/50"
      : "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const clearFilters = () => {
    setSearchTerm("");
    setTypeFilter("ALL");
  };

  return (
    <div className="min-h-screen bg-background relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Save className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.checkpoints.overview.title", "Checkpoints")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.checkpoints.overview.register", "Registrar Checkpoint")}
          </Button>
        </div>

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Total</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.totalItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Activos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Pendientes</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.pendingApproval}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium ">Inactivos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.inactiveItems}</div>
            </CardContent>
          </Card>
        </div>

        {/* Filtros */}
        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4 flex-wrap">
              <div className="relative flex-1 min-w-[200px]">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="ACTIVE">ACTIVE</option>
                <option value="INACTIVE">INACTIVE</option>
              </select>
              {(searchTerm || typeFilter !== "ALL") && (
                <Button variant="outline" onClick={clearFilters}>
                  <X className="w-4 h-4 mr-2" />
                  {t("common.clear", "Limpiar")}
                </Button>
              )}
            </div>
          </CardContent>
        </Card>

        {/* Tabla */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle>{t("training.checkpoints.overview.list", "Lista de Checkpoints")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.checkpoints.overview.checkpointName", "Checkpoint Name")}</th>
                    <th className="text-left p-4">{t("training.checkpoints.overview.epoch", "Epoch")}</th>
                    <th className="text-left p-4">{t("training.checkpoints.overview.step", "Step")}</th>
                    <th className="text-left p-4">{t("training.checkpoints.overview.type", "Type")}</th>
                    <th className="text-left p-4">{t("training.checkpoints.overview.path", "Path")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredCheckpoints.map((checkpoint) => (
                    <tr key={checkpoint.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{checkpoint.id}</td>
                      <td className="p-4">{checkpoint.checkpointName}</td>
                      <td className="p-4">{checkpoint.epoch}</td>
                      <td className="p-4">{checkpoint.step}</td>
                      <td className="p-4">
                        <Badge className={getTypeBadge(checkpoint.checkpointType)}>
                          {checkpoint.checkpointType}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground">{checkpoint.path}</td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Button variant="ghost" size="sm">
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button variant="ghost" size="sm">
                            <Trash2 className="w-4 h-4 text-destructive" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredCheckpoints.length === 0 && (
                <div className=" py-12 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


