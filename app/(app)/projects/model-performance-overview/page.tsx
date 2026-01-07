"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  TrendingUp,
  Brain,
  AlertTriangle,
  CheckCircle2,
  ExternalLink,
  ArrowRight,
} from "lucide-react";
import Link from "next/link";

interface ModelPerformance {
  projectId: number;
  projectName: string;
  modelsCount: number;
  averageAccuracy: number;
  averagePrecision: number;
  averageRecall: number;
  biasDetected: number;
  explainabilityScore: number;
  lastUpdated: string;
}

const mockModelPerformance: ModelPerformance[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    modelsCount: 5,
    averageAccuracy: 0.92,
    averagePrecision: 0.89,
    averageRecall: 0.91,
    biasDetected: 1,
    explainabilityScore: 0.85,
    lastUpdated: "2024-11-20T10:30:00Z",
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    modelsCount: 8,
    averageAccuracy: 0.88,
    averagePrecision: 0.86,
    averageRecall: 0.87,
    biasDetected: 2,
    explainabilityScore: 0.78,
    lastUpdated: "2024-11-19T15:20:00Z",
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    modelsCount: 3,
    averageAccuracy: 0.95,
    averagePrecision: 0.93,
    averageRecall: 0.94,
    biasDetected: 0,
    explainabilityScore: 0.92,
    lastUpdated: "2024-11-18T09:15:00Z",
  },
];

export default function ModelPerformanceOverviewPage() {
  const { t } = useTranslation();

  const totalModels = mockModelPerformance.reduce((sum, p) => sum + p.modelsCount, 0);
  const avgAccuracy = mockModelPerformance.reduce((sum, p) => sum + p.averageAccuracy, 0) / mockModelPerformance.length;
  const totalBiasDetected = mockModelPerformance.reduce((sum, p) => sum + p.biasDetected, 0);
  const avgExplainability = mockModelPerformance.reduce((sum, p) => sum + p.explainabilityScore, 0) / mockModelPerformance.length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <TrendingUp className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.modelPerformance.title", "Vista General de Performance de Modelos")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.modelPerformance.totalModels", "Total Modelos")}
              </CardTitle>
              <Brain className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalModels}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.modelPerformance.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.modelPerformance.avgAccuracy", "Precisión Promedio")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{(avgAccuracy * 100).toFixed(1)}%</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.modelPerformance.acrossAllModels", "en todos los modelos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.modelPerformance.biasDetected", "Bias Detectado")}
              </CardTitle>
              <AlertTriangle className="h-4 w-4 text-yellow-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-yellow-500">{totalBiasDetected}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.modelPerformance.models", "modelos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.modelPerformance.avgExplainability", "Explicabilidad Promedio")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{(avgExplainability * 100).toFixed(1)}%</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.modelPerformance.score", "score")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockModelPerformance.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{project.projectName}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground">{t("projects.modelPerformance.models", "Modelos")}</p>
                    <p className="text-lg font-bold">{project.modelsCount}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("projects.modelPerformance.accuracy", "Precisión")}
                    </p>
                    <p className="text-lg font-bold">{(project.averageAccuracy * 100).toFixed(1)}%</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("projects.modelPerformance.precision", "Precisión")}
                    </p>
                    <p className="text-lg font-bold">{(project.averagePrecision * 100).toFixed(1)}%</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">{t("projects.modelPerformance.recall", "Recall")}</p>
                    <p className="text-lg font-bold">{(project.averageRecall * 100).toFixed(1)}%</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">
                      {t("projects.modelPerformance.explainability", "Explicabilidad")}
                    </p>
                    <p className="text-lg font-bold">{(project.explainabilityScore * 100).toFixed(1)}%</p>
                  </div>
                </div>

                {project.biasDetected > 0 && (
                  <div className="flex items-center gap-2 p-3 rounded-lg bg-yellow-500/10 border border-yellow-500/20">
                    <AlertTriangle className="w-5 h-5 text-yellow-500" />
                    <p className="text-sm text-yellow-500">
                      {t("projects.modelPerformance.biasWarning", "{count} modelo(s) con bias detectado", {
                         count: String(project.biasDetected),
                      })}
                    </p>
                  </div>
                )}

                <div className="flex gap-2 pt-2">
                  <Link href={`/governance/models?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.modelPerformance.viewModels", "Ver Modelos")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/governance/models?projectId=${project.projectId}&tab=performance`}>
                    <Button variant="outline" size="sm">
                      {t("projects.modelPerformance.viewPerformance", "Ver Performance")}
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
