"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Activity,
  Edit,
  TrendingUp,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface InfrastructureMetricDetail {
  id: number;
  resourceId: number;
  metricType: string;
  metricValue: number;
  unit: string;
  timestamp: string;
  createdAt: string;
  updatedAt: string;
  resourceName: string;
}

const mockMetric: InfrastructureMetricDetail = {
  id: 1,
  resourceId: 1,
  metricType: "CPU_USAGE",
  metricValue: 45.2,
  unit: "percent",
  timestamp: "2024-01-15T10:00:00Z",
  createdAt: "2024-01-15T10:00:00Z",
  updatedAt: "2024-01-15T10:00:00Z",
  resourceName: "Production Load Balancer",
};

export default function InfrastructureMetricDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const metric = mockMetric;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/infrastructure-metric-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <Activity className="w-8 h-8 text-primary" />
                {metric.resourceName} - {metric.metricType}
              </h1>
            </div>
          </div>
          <Button>
            <Edit className="w-4 h-4 mr-2" />
            {t("common.edit", "Edit")}
          </Button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Activity className="w-5 h-5 text-primary" />
                  {t("infrastructure.metricDetails", "Metric Details")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.metricValue", "Metric Value")}
                    </label>
                    <p className="text-4xl font-bold text-foreground">
                      {metric.metricValue} {metric.unit}
                    </p>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.metricType", "Metric Type")}
                      </label>
                      <p className="text-foreground font-medium">{metric.metricType}</p>
                    </div>
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.timestamp", "Timestamp")}
                      </label>
                      <p className="text-foreground font-medium">
                        {new Date(metric.timestamp).toLocaleString()}
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-primary" />
                  {t("infrastructure.resourceInformation", "Resource Information")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.resourceName", "Resource Name")}
                  </label>
                  <p className="text-foreground font-medium">{metric.resourceName}</p>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Activity className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(metric.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(metric.updatedAt).toLocaleString()}
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}


