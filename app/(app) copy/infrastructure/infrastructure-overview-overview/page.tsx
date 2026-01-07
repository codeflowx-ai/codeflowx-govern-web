"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  Cloud,
  Cpu,
  Database,
  Server,
} from "lucide-react";

interface InfrastructureOverview {
  totalProviders: number;
  totalClusters: number;
  totalGpuInstances: number;
  totalResources: number;
  activeResources: number;
}

const mockOverview: InfrastructureOverview = {
  totalProviders: 8,
  totalClusters: 5,
  totalGpuInstances: 12,
  totalResources: 24,
  activeResources: 18,
};

export default function InfrastructureOverviewOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between"><div className="flex items-center gap-3">
          <Server className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("infrastructure.infrastructureOverviewOverview", "Infrastructure Overview")}
          </h1>
        </div></div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="border-border">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Cloud className="w-5 h-5 text-primary" />
                {t("infrastructure.cloudProviders", "Cloud Providers")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold text-foreground">
                {mockOverview.totalProviders}
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="w-5 h-5 text-primary" />
                {t("infrastructure.kubernetesClusters", "Kubernetes Clusters")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold text-foreground">
                {mockOverview.totalClusters}
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Cpu className="w-5 h-5 text-primary" />
                {t("infrastructure.gpuInstances", "GPU Instances")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold text-foreground">
                {mockOverview.totalGpuInstances}
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Database className="w-5 h-5 text-primary" />
                {t("infrastructure.totalResources", "Total Resources")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold text-foreground">
                {mockOverview.totalResources}
              </div>
              <div className="text-sm text-muted-foreground mt-1">
                {mockOverview.activeResources} {t("infrastructure.active", "active")}
              </div>
            </CardContent>
          </Card>
        </div>

        <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5 text-primary" />
              {t("infrastructure.systemStatus", "System Status")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-3 gap-4">
              <div className="text-center">
                <div className="text-2xl font-bold text-green-500">Operational</div>
                <div className="text-sm text-muted-foreground">Overall Status</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-foreground">99.97%</div>
                <div className="text-sm text-muted-foreground">Uptime</div>
              </div>
              <div className="text-center">
                <div className="text-2xl font-bold text-foreground">1</div>
                <div className="text-sm text-muted-foreground">Active Alerts</div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


