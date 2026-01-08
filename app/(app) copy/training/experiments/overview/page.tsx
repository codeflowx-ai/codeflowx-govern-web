"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { FlaskConical, Plus, Search, Eye, Trash2, X } from "lucide-react";
import { useState, useEffect } from "react";

interface Experiment {
  id: number;
  name: string;
  description: string;
  artifactLocation: string;
  lifecycleStage: string;
  tags: string;
}

const mockExperiments: Experiment[] = [
  {
    id: 1,
    name: "Experiment V1",
    description: "Primer experimento de entrenamiento",
    artifactLocation: "/experiments/v1",
    lifecycleStage: "ACTIVE",
    tags: "ml,training",
  },
  {
    id: 2,
    name: "Experiment V2",
    description: "Segundo experimento mejorado",
    artifactLocation: "/experiments/v2",
    lifecycleStage: "ACTIVE",
    tags: "ml,training,optimized",
  },
];

export default function ExperimentsOverviewPage() {
  const { t } = useTranslation();
  const [experiments, setExperiments] = useState<Experiment[]>(mockExperiments);
  const [searchTerm, setSearchTerm] = useState("");

  const [metrics, setMetrics] = useState({
    totalItems: 0,
    activeItems: 0,
    pendingApproval: 0,
    inactiveItems: 0,
  });

  useEffect(() => {
    setMetrics({
      totalItems: experiments.length,
      activeItems: experiments.filter((e) => e.lifecycleStage === "ACTIVE").length,
      pendingApproval: 0,
      inactiveItems: experiments.filter((e) => e.lifecycleStage === "INACTIVE").length,
    });
  }, [experiments]);

  const filteredExperiments = experiments.filter((experiment) =>
    experiment.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    experiment.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const clearFilters = () => {
    setSearchTerm("");
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
            <FlaskConical className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.experiments.overview.title", "Experiments")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("training.experiments.overview.register", "Registrar Experiment")}
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
              {searchTerm && (
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
            <CardTitle>{t("training.experiments.overview.list", "Lista de Experiments")}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4">{t("common.id", "ID")}</th>
                    <th className="text-left p-4">{t("training.experiments.overview.name", "Name")}</th>
                    <th className="text-left p-4">{t("training.experiments.overview.description", "Description")}</th>
                    <th className="text-left p-4">{t("training.experiments.overview.artifactLocation", "Artifact Location")}</th>
                    <th className="text-left p-4">{t("training.experiments.overview.lifecycleStage", "Lifecycle Stage")}</th>
                    <th className="text-left p-4">{t("training.experiments.overview.tags", "Tags")}</th>
                    <th className="text-left p-4">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredExperiments.map((experiment) => (
                    <tr key={experiment.id} className="border-b border-border/50 hover:bg-background/50 transition-colors">
                      <td className="p-4">{experiment.id}</td>
                      <td className="p-4">{experiment.name}</td>
                      <td className="p-4 text-sm text-muted-foreground">{experiment.description}</td>
                      <td className="p-4 text-sm text-muted-foreground">{experiment.artifactLocation}</td>
                      <td className="p-4">
                        <Badge className="bg-green-500/20 text-green-500 border-green-500/50">
                          {experiment.lifecycleStage}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground">{experiment.tags}</td>
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
              {filteredExperiments.length === 0 && (
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


