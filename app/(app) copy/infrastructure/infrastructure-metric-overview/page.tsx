"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Eye,
  Search,
  TrendingUp,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface InfrastructureMetric {
  id: number;
  resourceName: string;
  metricType: string;
  metricValue: number;
  unit: string;
  timestamp: string;
}

const mockMetrics: InfrastructureMetric[] = [
  {
    id: 1,
    resourceName: "Production Load Balancer",
    metricType: "CPU_USAGE",
    metricValue: 45.2,
    unit: "percent",
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    id: 2,
    resourceName: "Main Database Cluster",
    metricType: "MEMORY_USAGE",
    metricValue: 78.5,
    unit: "percent",
    timestamp: "2024-01-15T10:00:00Z",
  },
];

export default function InfrastructureMetricOverviewPage() {   const { t } = useTranslation();
  const [metrics, setMetrics] = useState<InfrastructureMetric[]>(mockMetrics);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredMetrics = metrics.filter((metric) =>
    metric.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    metric.metricType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Activity className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.infrastructureMetricOverview", "Infrastructure Metric Overview")}
            </h1>
          </div>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search metrics...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredMetrics.map((metric) => (
            <Card
              key={metric.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{metric.resourceName}</CardTitle>
                <Badge variant="outline" className="mt-2">{metric.metricType}</Badge>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">{t("infrastructure.metricValue", "Value")}</span>
                    <span className="text-2xl font-bold text-foreground">
                      {metric.metricValue} {metric.unit}
                    </span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <TrendingUp className="w-4 h-4" />
                    {new Date(metric.timestamp).toLocaleString()}
                  </div>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => window.location.href = `/infrastructure/infrastructure-metric-detail?id=${metric.id}`}
                >
                  <Eye className="w-4 h-4 mr-2" />
                  {t("common.details", "Details")}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredMetrics.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <Activity className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noMetricsFound", "No metrics found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


