"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { BarChart3, Brain, TrendingUp, Activity } from "lucide-react";
import { useState, useEffect } from "react";

interface DashboardMetrics {
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}

export default function TrainingGovernanceDashboardPage() {
  const { t } = useTranslation();
  const [metrics, setMetrics] = useState<DashboardMetrics>({
    totalItems: 0,
    activeItems: 0,
    deployedItems: 0,
    trainingItems: 0,
    offlineItems: 0,
    avgScore: 0,
  });

  useEffect(() => {
    // Mock data - En producción esto vendría de la API
    setMetrics({
      totalItems: 156,
      activeItems: 89,
      deployedItems: 45,
      trainingItems: 22,
      offlineItems: 12,
      avgScore: 87.5,
    });
  }, []);

  return (
    <div className="min-h-screen bg-background relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <BarChart3 className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.governance.dashboard.title", "Training Overview Dashboard")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("training.governance.dashboard.description", "Monitoreo de experimentos y entrenamiento de modelos")}
          </p>
        </div>

        {/* KPIs */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.totalItems", "Total Items")}
              </CardTitle>
              <Activity className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.totalItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.activeItems", "Active Items")}
              </CardTitle>
              <Brain className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.deployedItems", "Deployed Items")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.deployedItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.trainingItems", "Training Items")}
              </CardTitle>
              <Activity className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.trainingItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.offlineItems", "Offline Items")}
              </CardTitle>
              <Activity className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.offlineItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.governance.dashboard.avgScore", "Average Score")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.avgScore}%</div>
            </CardContent>
          </Card>
        </div>

        {/* Placeholder for charts */}
        <Card className="border-border">
          <CardHeader>
            <CardTitle>
              {t("training.governance.dashboard.charts", "Training Metrics")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className=" py-12 text-muted-foreground">
              {t("training.governance.dashboard.chartsPlaceholder", "Los gráficos de métricas se mostrarán aquí")}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


