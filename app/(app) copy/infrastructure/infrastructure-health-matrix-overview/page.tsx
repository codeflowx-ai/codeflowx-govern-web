"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  CheckCircle,
  Heart,
  XCircle,
} from "lucide-react";

interface HealthStatus {
  resourceName: string;
  resourceType: string;
  status: "HEALTHY" | "WARNING" | "CRITICAL";
  cpuUsage: number;
  memoryUsage: number;
}

const mockHealthStatuses: HealthStatus[] = [
  {
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    status: "HEALTHY",
    cpuUsage: 45.2,
    memoryUsage: 62.8,
  },
  {
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    status: "WARNING",
    cpuUsage: 78.5,
    memoryUsage: 85.3,
  },
  {
    resourceName: "Cache Service",
    resourceType: "CACHE",
    status: "HEALTHY",
    cpuUsage: 32.1,
    memoryUsage: 45.6,
  },
];

export default function InfrastructureHealthMatrixOverviewPage() {
  const { t } = useTranslation();

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "HEALTHY":
        return <CheckCircle className="w-5 h-5 text-green-500" />;
      case "WARNING":
        return <Activity className="w-5 h-5 text-yellow-500" />;
      case "CRITICAL":
        return <XCircle className="w-5 h-5 text-red-500" />;
      default:
        return <Activity className="w-5 h-5 text-gray-500" />;
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      HEALTHY: "bg-green-500/20 text-green-500 border-green-500/50",
      WARNING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      CRITICAL: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between"><div className="flex items-center gap-3">
          <Heart className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("infrastructure.infrastructureHealthMatrixOverview", "Infrastructure Health Matrix")}
          </h1>
        </div></div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockHealthStatuses.map((health, index) => (
            <Card
              key={index}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{health.resourceName}</CardTitle>
                  {getStatusIcon(health.status)}
                </div>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{health.resourceType}</Badge>
                  <Badge className={getStatusBadge(health.status)}>
                    {health.status}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div>
                    <div className="flex justify-between text-sm mb-1">
                      <span className="text-muted-foreground">{t("infrastructure.cpu", "CPU")}</span>
                      <span className="text-foreground font-medium">{health.cpuUsage}%</span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${
                          health.cpuUsage >= 80
                            ? "bg-red-500"
                            : health.cpuUsage >= 60
                            ? "bg-yellow-500"
                            : "bg-green-500"
                        }`}
                        style={{ width: `${health.cpuUsage}%` }}
                      />
                    </div>
                  </div>
                  <div>
                    <div className="flex justify-between text-sm mb-1">
                      <span className="text-muted-foreground">{t("infrastructure.memory", "Memory")}</span>
                      <span className="text-foreground font-medium">{health.memoryUsage}%</span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${
                          health.memoryUsage >= 80
                            ? "bg-red-500"
                            : health.memoryUsage >= 60
                            ? "bg-yellow-500"
                            : "bg-green-500"
                        }`}
                        style={{ width: `${health.memoryUsage}%` }}
                      />
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}


