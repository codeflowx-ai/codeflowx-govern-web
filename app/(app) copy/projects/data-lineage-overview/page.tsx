"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Network,
  Database,
  Brain,
  Package,
  GitBranch,
  ExternalLink,
  ArrowRight,
} from "lucide-react";
import Link from "next/link";

interface DataLineage {
  projectId: number;
  projectName: string;
  datasetsCount: number;
  modelsCount: number;
  artifactsCount: number;
  versionsCount: number;
  lastUpdated: string;
}

const mockDataLineage: DataLineage[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    datasetsCount: 3,
    modelsCount: 5,
    artifactsCount: 12,
    versionsCount: 8,
    lastUpdated: "2024-11-20T10:30:00Z",
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    datasetsCount: 5,
    modelsCount: 8,
    artifactsCount: 18,
    versionsCount: 12,
    lastUpdated: "2024-11-19T15:20:00Z",
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    datasetsCount: 2,
    modelsCount: 3,
    artifactsCount: 8,
    versionsCount: 5,
    lastUpdated: "2024-11-18T09:15:00Z",
  },
];

export default function DataLineageOverviewPage() {
  const { t } = useTranslation();

  const totalDatasets = mockDataLineage.reduce((sum, p) => sum + p.datasetsCount, 0);
  const totalModels = mockDataLineage.reduce((sum, p) => sum + p.modelsCount, 0);
  const totalArtifacts = mockDataLineage.reduce((sum, p) => sum + p.artifactsCount, 0);
  const totalVersions = mockDataLineage.reduce((sum, p) => sum + p.versionsCount, 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Network className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.dataLineage.title", "Vista General de Trazabilidad de Datos")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.dataLineage.totalDatasets", "Total Datasets")}
              </CardTitle>
              <Database className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalDatasets}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.dataLineage.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.dataLineage.totalModels", "Total Modelos")}
              </CardTitle>
              <Brain className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalModels}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.dataLineage.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.dataLineage.totalArtifacts", "Total Artefactos")}
              </CardTitle>
              <Package className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalArtifacts}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.dataLineage.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.dataLineage.totalVersions", "Total Versiones")}
              </CardTitle>
              <GitBranch className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalVersions}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.dataLineage.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockDataLineage.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{project.projectName}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="flex items-center gap-2">
                    <Database className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">{t("projects.dataLineage.datasets", "Datasets")}</p>
                      <p className="text-lg font-bold">{project.datasetsCount}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Brain className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">{t("projects.dataLineage.models", "Modelos")}</p>
                      <p className="text-lg font-bold">{project.modelsCount}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Package className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.dataLineage.artifacts", "Artefactos")}
                      </p>
                      <p className="text-lg font-bold">{project.artifactsCount}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <GitBranch className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.dataLineage.versions", "Versiones")}
                      </p>
                      <p className="text-lg font-bold">{project.versionsCount}</p>
                    </div>
                  </div>
                </div>

                <div className="flex gap-2 pt-2">
                  <Link href={`/training/datasets?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.dataLineage.viewDatasets", "Ver Datasets")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/governance/models?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.dataLineage.viewModels", "Ver Modelos")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/projects/artifacts/list?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.dataLineage.viewArtifacts", "Ver Artefactos")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/governance/compliance/traceability?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.dataLineage.viewFullTraceability", "Trazabilidad Completa")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/projects/${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("common.details", "Detalles")}
                      <ArrowRight className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
