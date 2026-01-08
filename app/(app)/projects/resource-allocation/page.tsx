"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Cpu, TrendingUp, PieChart, BarChart3, Brain, Server } from "lucide-react";

interface ResourceAllocation {
  totalGPUs: number;
  allocatedGPUs: number;
  availableGPUs: number;
  utilizationRate: number;
  byProject: Array<{ projectName: string; gpus: number; models: number; percentage: number }>;
  byResourceType: Array<{ type: string; allocated: number; total: number; percentage: number }>;
}

const mockResourceAllocation: ResourceAllocation = {
  totalGPUs: 48,
  allocatedGPUs: 42,
  availableGPUs: 6,
  utilizationRate: 87.5,
  byProject: [
    { projectName: "Proyecto Alpha", gpus: 8, models: 5, percentage: 16.7 },
    { projectName: "Proyecto Beta", gpus: 12, models: 8, percentage: 25.0 },
    { projectName: "Proyecto Gamma", gpus: 6, models: 3, percentage: 12.5 },
    { projectName: "Otros Proyectos", gpus: 16, models: 12, percentage: 45.8 },
  ],
  byResourceType: [
    { type: "GPUs", allocated: 42, total: 48, percentage: 87.5 },
    { type: "Modelos Activos", allocated: 28, total: 35, percentage: 80.0 },
    { type: "Agentes Desplegados", allocated: 15, total: 20, percentage: 75.0 },
    { type: "Storage (TB)", allocated: 120, total: 150, percentage: 80.0 },
  ],
};

export default function ResourceAllocationPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-start gap-3 mb-4">
          <Cpu className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("projects.resourceAllocation.title", "Asignación de Recursos de IA")}
          </h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.resourceAllocation.totalGPUs", "Total GPUs")}
              </CardTitle>
              <Cpu className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockResourceAllocation.totalGPUs}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.resourceAllocation.gpus", "GPUs disponibles")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.resourceAllocation.allocatedGPUs", "GPUs Asignadas")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockResourceAllocation.allocatedGPUs}</div>
              <p className="text-xs text-muted-foreground">
                {mockResourceAllocation.utilizationRate.toFixed(1)}% {t("projects.resourceAllocation.utilization", "utilización")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.resourceAllocation.availableGPUs", "GPUs Disponibles")}
              </CardTitle>
              <Cpu className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockResourceAllocation.availableGPUs}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.resourceAllocation.free", "libres")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.resourceAllocation.utilizationRate", "Tasa de Utilización")}
              </CardTitle>
              <PieChart className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockResourceAllocation.utilizationRate.toFixed(1)}%</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.resourceAllocation.ofTotal", "del total")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <BarChart3 className="w-5 h-5 text-primary" />
                {t("projects.resourceAllocation.byProject", "Asignación por Proyecto")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {mockResourceAllocation.byProject.map((project) => (
                <div key={project.projectName} className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-muted-foreground">{project.projectName}</span>
                    <span className="font-medium">
                      {project.gpus} GPUs ({project.percentage.toFixed(1)}%)
                    </span>
                  </div>
                  <div className="flex items-center gap-2 text-xs text-muted-foreground">
                    <Brain className="w-3 h-3" />
                    <span>{project.models} {t("projects.resourceAllocation.models", "modelos")}</span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${project.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <PieChart className="w-5 h-5 text-primary" />
                {t("projects.resourceAllocation.byResourceType", "Asignación por Tipo de Recurso")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {mockResourceAllocation.byResourceType.map((resource) => (
                <div key={resource.type} className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-muted-foreground">{resource.type}</span>
                    <span className="font-medium">
                      {resource.allocated}/{resource.total} ({resource.percentage.toFixed(1)}%)
                    </span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-2">
                    <div
                      className="bg-primary h-2 rounded-full transition-all duration-500"
                      style={{ width: `${resource.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
