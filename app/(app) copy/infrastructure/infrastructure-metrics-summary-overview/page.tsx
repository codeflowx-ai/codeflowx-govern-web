"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  BarChart3,
  Cpu,
  Database,
  HardDrive,
  TrendingUp,
} from "lucide-react";

interface MetricsSummary {
  totalResources: number;
  averageCpuUsage: number;
  averageMemoryUsage: number;
  averageStorageUsage: number;
  totalNetworkIn: number;
  totalNetworkOut: number;
}

const mockSummary: MetricsSummary = {
  totalResources: 24,
  averageCpuUsage: 53.1,
  averageMemoryUsage: 66.2,
  averageStorageUsage: 34.7,
  totalNetworkIn: 4352,
  totalNetworkOut: 7936,
};

export default function InfrastructureMetricsSummaryOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between"><div className="flex items-center gap-3">
          <BarChart3 className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("infrastructure.infrastructureMetricsSummaryOverview", "Infrastructure Metrics Summary")}
          </h1>
        </div></div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="border-border">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Activity className="w-4 h-4" />
                {t("infrastructure.totalResources", "Total Resources")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-foreground">
                {mockSummary.totalResources}
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Cpu className="w-4 h-4" />
                {t("infrastructure.avgCpuUsage", "Avg CPU Usage")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-foreground">
                {mockSummary.averageCpuUsage.toFixed(1)}%
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Database className="w-4 h-4" />
                {t("infrastructure.avgMemoryUsage", "Avg Memory Usage")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-foreground">
                {mockSummary.averageMemoryUsage.toFixed(1)}%
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <HardDrive className="w-4 h-4" />
                {t("infrastructure.avgStorageUsage", "Avg Storage Usage")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-foreground">
                {mockSummary.averageStorageUsage.toFixed(1)}%
              </div>
            </CardContent>
          </Card>
        </div>

        <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="w-5 h-5 text-primary" />
              {t("infrastructure.networkUsage", "Network Usage")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div className="p-4 rounded-lg bg-muted/50">
                <div className="text-sm text-muted-foreground mb-1">
                  {t("infrastructure.networkIn", "Network In")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {(mockSummary.totalNetworkIn / 1024).toFixed(2)} GB/s
                </div>
              </div>
              <div className="p-4 rounded-lg bg-muted/50">
                <div className="text-sm text-muted-foreground mb-1">
                  {t("infrastructure.networkOut", "Network Out")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {(mockSummary.totalNetworkOut / 1024).toFixed(2)} GB/s
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


