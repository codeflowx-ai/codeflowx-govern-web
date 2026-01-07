"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  BarChart3,
  DollarSign,
  PieChart,
  TrendingUp,
  Users,
  AlertTriangle,
  CheckCircle2,
  Clock,
  Target,
} from "lucide-react";

interface PortfolioMetrics {
  totalProjects: number;
  activeProjects: number;
  completedProjects: number;
  totalBudget: number;
  spentBudget: number;
  totalResources: number;
  allocatedResources: number;
  riskProjects: number;
  onTimeProjects: number;
}

const mockMetrics: PortfolioMetrics = {
  totalProjects: 24,
  activeProjects: 18,
  completedProjects: 6,
  totalBudget: 2500000,
  spentBudget: 1850000,
  totalResources: 156,
  allocatedResources: 142,
  riskProjects: 3,
  onTimeProjects: 15,
};

export default function ProjectPortfolioDashboardPage() {
  const { t } = useTranslation();

  const budgetPercentage = (mockMetrics.spentBudget / mockMetrics.totalBudget) * 100;
  const resourceUtilization = (mockMetrics.allocatedResources / mockMetrics.totalResources) * 100;
  const completionRate = (mockMetrics.completedProjects / mockMetrics.totalProjects) * 100;
  const onTimeRate = (mockMetrics.onTimeProjects / mockMetrics.activeProjects) * 100;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-start gap-3 mb-4">
          <BarChart3 className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("projects.portfolioDashboard.title", "Dashboard de Portfolio de Proyectos")}
          </h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.portfolioDashboard.totalProjects", "Total Proyectos")}
              </CardTitle>
              <Target className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockMetrics.totalProjects}</div>
              <p className="text-xs text-muted-foreground">
                {mockMetrics.activeProjects} {t("projects.portfolioDashboard.active", "activos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.portfolioDashboard.budget", "Presupuesto")}
              </CardTitle>
              <DollarSign className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">
                ${(mockMetrics.spentBudget / 1000000).toFixed(2)}M
              </div>
              <p className="text-xs text-muted-foreground">
                {budgetPercentage.toFixed(1)}% {t("projects.portfolioDashboard.used", "utilizado")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.portfolioDashboard.resources", "Recursos")}
              </CardTitle>
              <Users className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockMetrics.allocatedResources}</div>
              <p className="text-xs text-muted-foreground">
                {resourceUtilization.toFixed(1)}% {t("projects.portfolioDashboard.utilization", "utilización")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.portfolioDashboard.completion", "Completados")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockMetrics.completedProjects}</div>
              <p className="text-xs text-muted-foreground">
                {completionRate.toFixed(1)}% {t("projects.portfolioDashboard.rate", "tasa")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <PieChart className="w-5 h-5 text-primary" />
                {t("projects.portfolioDashboard.budgetBreakdown", "Desglose de Presupuesto")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span>{t("projects.portfolioDashboard.spent", "Gastado")}</span>
                  <span className="font-medium">${(mockMetrics.spentBudget / 1000).toLocaleString()}K</span>
                </div>
                <div className="w-full bg-muted rounded-full h-2">
                  <div
                    className="bg-primary h-2 rounded-full transition-all duration-500"
                    style={{ width: `${budgetPercentage}%` }}
                  />
                </div>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span>{t("projects.portfolioDashboard.remaining", "Restante")}</span>
                  <span className="font-medium">
                    ${((mockMetrics.totalBudget - mockMetrics.spentBudget) / 1000).toLocaleString()}K
                  </span>
                </div>
                <div className="w-full bg-muted rounded-full h-2">
                  <div
                    className="bg-green-500 h-2 rounded-full transition-all duration-500"
                    style={{ width: `${100 - budgetPercentage}%` }}
                  />
                </div>
              </div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5 text-primary" />
                {t("projects.portfolioDashboard.projectStatus", "Estado de Proyectos")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50">
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4 text-green-500" />
                  <span>{t("projects.portfolioDashboard.onTime", "A Tiempo")}</span>
                </div>
                <span className="font-bold">{mockMetrics.onTimeProjects}</span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50">
                <div className="flex items-center gap-2">
                  <Clock className="w-4 h-4 text-yellow-500" />
                  <span>{t("projects.portfolioDashboard.inProgress", "En Progreso")}</span>
                </div>
                <span className="font-bold">{mockMetrics.activeProjects - mockMetrics.onTimeProjects}</span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50">
                <div className="flex items-center gap-2">
                  <AlertTriangle className="w-4 h-4 text-red-500" />
                  <span>{t("projects.portfolioDashboard.atRisk", "En Riesgo")}</span>
                </div>
                <span className="font-bold">{mockMetrics.riskProjects}</span>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}


