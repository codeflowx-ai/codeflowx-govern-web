"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Package,
  Plus,
  Search,
  Eye,
  Trash2,
  X,
} from "lucide-react";
import { useState, useEffect } from "react";

interface TrainingArtifact {
  id: number;
  trainingJobId: string;
  artifactType: string;
  name: string;
  filepath: string;
}

const mockArtifacts: TrainingArtifact[] = [
  {
    id: 1,
    trainingJobId: "TJ-001",
    artifactType: "MODEL",
    name: "model_v1.pkl",
    filepath: "/artifacts/models/model_v1.pkl",
  },
  {
    id: 2,
    trainingJobId: "TJ-002",
    artifactType: "CHECKPOINT",
    name: "checkpoint_epoch_10.ckpt",
    filepath: "/artifacts/checkpoints/checkpoint_epoch_10.ckpt",
  },
  {
    id: 3,
    trainingJobId: "TJ-003",
    artifactType: "LOG",
    name: "training_log.json",
    filepath: "/artifacts/logs/training_log.json",
  },
];

export default function TrainingArtifactsOverviewPage() {
  const { t } = useTranslation();
  const [artifacts, setArtifacts] = useState<TrainingArtifact[]>(mockArtifacts);
  const [searchTerm, setSearchTerm] = useState("");
  const [artifactTypeFilter, setArtifactTypeFilter] = useState<string>("ALL");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: artifacts.length,
      activeItems: artifacts.filter((a) => a.artifactType === "MODEL").length,
      pendingApproval: artifacts.filter((a) => a.artifactType === "CHECKPOINT").length,
      inactiveItems: artifacts.filter((a) => a.artifactType === "LOG").length,
    });
  }, [artifacts]);

  const filteredArtifacts = artifacts.filter((artifact) => {
    const matchesSearch =
      artifact.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      artifact.trainingJobId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      artifact.filepath.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType =
      artifactTypeFilter === "ALL" || artifact.artifactType === artifactTypeFilter;
    return matchesSearch && matchesType;
  });

  const getArtifactTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      MODEL: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      CHECKPOINT: "bg-green-500/20 text-green-500 border-green-500/50",
      LOG: "bg-purple-500/20 text-purple-500 border-purple-500/50",
    };
    return colors[type] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const clearFilters = () => {
    setSearchTerm("");
    setArtifactTypeFilter("ALL");
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
            <Package className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.artifacts.overview.title", "Training Artifacts")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.artifacts.overview.register", "Registrar Training Artifact")}
          </Button>
        </div>

        {/* Métricas */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">Total</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.totalItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">Activos</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">Pendientes</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{metrics.pendingApproval}</div>
            </CardContent>
          </Card>
          <Card className="border-border">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">Inactivos</CardTitle>
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
                value={artifactTypeFilter}
                onChange={(e) => setArtifactTypeFilter(e.target.value)}
                className="px-3 py-2 rounded-md border border-border bg-background text-foreground"
              >
                <option value="ALL">{t("common.all", "TODOS")}</option>
                <option value="MODEL">MODEL</option>
                <option value="CHECKPOINT">CHECKPOINT</option>
                <option value="LOG">LOG</option>
              </select>
              {(searchTerm || artifactTypeFilter !== "ALL") && (
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
            <CardTitle>{t("training.artifacts.overview.list", "Lista de Artifacts")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.artifacts.overview.trainingJobId", "Training Job ID")}</th>
                    <th className="text-left p-4">{t("training.artifacts.overview.artifactType", "Artifact Type")}</th>
                    <th className="text-left p-4">{t("training.artifacts.overview.name", "Name")}</th>
                    <th className="text-left p-4">{t("training.artifacts.overview.filepath", "Filepath")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredArtifacts.map((artifact) => (
                    <tr key={artifact.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{artifact.id}</td>
                      <td className="p-4">{artifact.trainingJobId}</td>
                      <td className="p-4">
                        <Badge className={getArtifactTypeBadge(artifact.artifactType)}>
                          {artifact.artifactType}
                        </Badge>
                      </td>
                      <td className="p-4">{artifact.name}</td>
                      <td className="p-4 text-sm text-muted-foreground">{artifact.filepath}</td>
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
              {filteredArtifacts.length === 0 && (
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


