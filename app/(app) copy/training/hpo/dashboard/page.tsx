"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, Activity, Target, Zap } from "lucide-react";
import { useState, useEffect } from "react";

interface HPODashboardMetrics {
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}

export default function HPODashboardPage() {
  const { t } = useTranslation();
  const [metrics, setMetrics] = useState<HPODashboardMetrics>({
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
      totalItems: 42,
      activeItems: 28,
      deployedItems: 12,
      trainingItems: 15,
      offlineItems: 3,
      avgScore: 92.3,
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
            <Zap className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("training.hpo.dashboard.title", "HPO Progress Dashboard")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("training.hpo.dashboard.description", "Monitoreo de progreso de optimización de hiperparámetros")}
          </p>
        </div>

        {/* KPIs */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.hpo.dashboard.totalItems", "Total Items")}
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
                {t("training.hpo.dashboard.activeItems", "Active Items")}
              </CardTitle>
              <Target className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{metrics.activeItems}</div>
            </CardContent>
          </Card>

          <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("training.hpo.dashboard.deployedItems", "Deployed Items")}
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
                {t("training.hpo.dashboard.trainingItems", "Training Items")}
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
                {t("training.hpo.dashboard.offlineItems", "Offline Items")}
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
                {t("training.hpo.dashboard.avgScore", "Average Score")}
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
              {t("training.hpo.dashboard.charts", "HPO Progress Metrics")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className=" py-12 text-muted-foreground">
              {t("training.hpo.dashboard.chartsPlaceholder", "Los gráficos de progreso HPO se mostrarán aquí")}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


