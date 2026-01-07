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
  Network,
  Server,
  TrendingUp,
} from "lucide-react";
import { useState } from "react";

interface ResourceUtilization {
  resourceId: number;
  resourceName: string;
  resourceType: string;
  cpuUsage: number;
  memoryUsage: number;
  storageUsage: number;
  networkIn: number;
  networkOut: number;
  timestamp: string;
}

interface UtilizationSummary {
  totalResources: number;
  activeResources: number;
  averageCpuUsage: number;
  averageMemoryUsage: number;
  averageStorageUsage: number;
  totalNetworkIn: number;
  totalNetworkOut: number;
}

// Mock data
const mockUtilization: ResourceUtilization[] = [
  {
    resourceId: 1,
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    cpuUsage: 45.2,
    memoryUsage: 62.8,
    storageUsage: 12.5,
    networkIn: 1024,
    networkOut: 2048,
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    resourceId: 2,
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    cpuUsage: 78.5,
    memoryUsage: 85.3,
    storageUsage: 68.2,
    networkIn: 512,
    networkOut: 256,
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    resourceId: 3,
    resourceName: "Cache Service",
    resourceType: "CACHE",
    cpuUsage: 32.1,
    memoryUsage: 45.6,
    storageUsage: 23.4,
    networkIn: 2048,
    networkOut: 4096,
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    resourceId: 4,
    resourceName: "Message Queue",
    resourceType: "MESSAGE_QUEUE",
    cpuUsage: 56.7,
    memoryUsage: 71.2,
    storageUsage: 34.8,
    networkIn: 768,
    networkOut: 1536,
    timestamp: "2024-01-15T10:00:00Z",
  },
];

const mockSummary: UtilizationSummary = {
  totalResources: 24,
  activeResources: 18,
  averageCpuUsage: 53.1,
  averageMemoryUsage: 66.2,
  averageStorageUsage: 34.7,
  totalNetworkIn: 4352,
  totalNetworkOut: 7936,
};

export default function ResourceUtilizationDashboardPage() {
  const { t } = useTranslation();
  const [selectedTimeRange, setSelectedTimeRange] = useState("24h");

  const getUsageColor = (usage: number) => {
    if (usage >= 80) return "text-red-500";
    if (usage >= 60) return "text-yellow-500";
    return "text-green-500";
  };

  const getUsageBadgeColor = (usage: number) => {
    if (usage >= 80) return "bg-red-500/20 text-red-500 border-red-500/50";
    if (usage >= 60) return "bg-yellow-500/20 text-yellow-500 border-yellow-500/50";
    return "bg-green-500/20 text-green-500 border-green-500/50";
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header - Título y subtítulo alineados a la izquierda */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <BarChart3 className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("infrastructure.resourceUtilizationDashboard", "Resource Utilization Dashboard")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("infrastructure.resourceUtilizationDesc", "Monitor resource usage across all infrastructure")}
            </p>
          </div>
        </div>

            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <Card className="border-border hover:border-primary/50 transition-all duration-300">
            <CardHeader className="pb-3">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Server className="w-4 h-4" />
                {t("infrastructure.totalResources", "Total Resources")}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-foreground">
                {mockSummary.totalResources}
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                {mockSummary.activeResources} {t("infrastructure.active", "active")}
              </p>
            </CardContent>
          </Card>

              <Card className="border-border hover:border-primary/50 transition-all duration-300">
                <CardHeader className="pb-3">
                  <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                    <Cpu className="w-4 h-4" />
                    {t("infrastructure.avgCpuUsage", "Avg CPU Usage")}
                  </CardTitle>
                </CardHeader>
            <CardContent>
              <div className={`text-2xl font-bold ${getUsageColor(mockSummary.averageCpuUsage)}`}>
                {mockSummary.averageCpuUsage.toFixed(1)}%
              </div>
              <div className="w-full bg-muted rounded-full h-2 mt-2">
                <div
                  className={`h-2 rounded-full transition-all duration-500 ${
                    mockSummary.averageCpuUsage >= 80
                      ? "bg-red-500"
                      : mockSummary.averageCpuUsage >= 60
                      ? "bg-yellow-500"
                      : "bg-green-500"
                  }`}
                  style={{ width: `${mockSummary.averageCpuUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>

              <Card className="border-border hover:border-primary/50 transition-all duration-300">
                <CardHeader className="pb-3">
                  <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                    <Database className="w-4 h-4" />
                    {t("infrastructure.avgMemoryUsage", "Avg Memory Usage")}
                  </CardTitle>
                </CardHeader>
            <CardContent>
              <div className={`text-2xl font-bold ${getUsageColor(mockSummary.averageMemoryUsage)}`}>
                {mockSummary.averageMemoryUsage.toFixed(1)}%
              </div>
              <div className="w-full bg-muted rounded-full h-2 mt-2">
                <div
                  className={`h-2 rounded-full transition-all duration-500 ${
                    mockSummary.averageMemoryUsage >= 80
                      ? "bg-red-500"
                      : mockSummary.averageMemoryUsage >= 60
                      ? "bg-yellow-500"
                      : "bg-green-500"
                  }`}
                  style={{ width: `${mockSummary.averageMemoryUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>

              <Card className="border-border hover:border-primary/50 transition-all duration-300">
                <CardHeader className="pb-3">
                  <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                    <HardDrive className="w-4 h-4" />
                    {t("infrastructure.avgStorageUsage", "Avg Storage Usage")}
                  </CardTitle>
                </CardHeader>
            <CardContent>
              <div className={`text-2xl font-bold ${getUsageColor(mockSummary.averageStorageUsage)}`}>
                {mockSummary.averageStorageUsage.toFixed(1)}%
              </div>
              <div className="w-full bg-muted rounded-full h-2 mt-2">
                <div
                  className={`h-2 rounded-full transition-all duration-500 ${
                    mockSummary.averageStorageUsage >= 80
                      ? "bg-red-500"
                      : mockSummary.averageStorageUsage >= 60
                      ? "bg-yellow-500"
                      : "bg-green-500"
                  }`}
                  style={{ width: `${mockSummary.averageStorageUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>
        </div>

            {/* Network Usage */}
            <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Network className="w-5 h-5 text-primary" />
              {t("infrastructure.networkUsage", "Network Usage")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="p-4 rounded-lg bg-muted/50">
                <div className="text-sm text-muted-foreground mb-1">
                  {t("infrastructure.networkIn", "Network In")}
                </div>
                <div className="text-2xl font-bold text-foreground flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-green-500" />
                  {(mockSummary.totalNetworkIn / 1024).toFixed(2)} GB/s
                </div>
              </div>
              <div className="p-4 rounded-lg bg-muted/50">
                <div className="text-sm text-muted-foreground mb-1">
                  {t("infrastructure.networkOut", "Network Out")}
                </div>
                <div className="text-2xl font-bold text-foreground flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-blue-500" />
                  {(mockSummary.totalNetworkOut / 1024).toFixed(2)} GB/s
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

            {/* Resource Utilization Table */}
            <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5 text-primary" />
              {t("infrastructure.resourceUtilization", "Resource Utilization")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.resourceName", "Resource Name")}
                    </th>
                    <th className="text-left p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.resourceType", "Type")}
                    </th>
                    <th className="text-center p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.cpu", "CPU")}
                    </th>
                    <th className="text-center p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.memory", "Memory")}
                    </th>
                    <th className="text-center p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.storage", "Storage")}
                    </th>
                    <th className="text-center p-3 text-sm font-medium text-muted-foreground">
                      {t("infrastructure.network", "Network")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {mockUtilization.map((resource) => (
                    <tr
                      key={resource.resourceId}
                      className="border-b border-border hover:bg-muted/30 transition-colors"
                    >
                      <td className="p-3">
                        <div className="font-medium text-foreground">
                          {resource.resourceName}
                        </div>
                      </td>
                      <td className="p-3">
                        <Badge variant="outline" className="text-xs">
                          {resource.resourceType}
                        </Badge>
                      </td>
                      <td className="p-3 text-center">
                        <div className="flex flex-col items-center gap-1">
                          <span className={`text-sm font-medium ${getUsageColor(resource.cpuUsage)}`}>
                            {resource.cpuUsage.toFixed(1)}%
                          </span>
                          <div className="w-16 bg-muted rounded-full h-1.5">
                            <div
                              className={`h-1.5 rounded-full ${
                                resource.cpuUsage >= 80
                                  ? "bg-red-500"
                                  : resource.cpuUsage >= 60
                                  ? "bg-yellow-500"
                                  : "bg-green-500"
                              }`}
                              style={{ width: `${resource.cpuUsage}%` }}
                            />
                          </div>
                        </div>
                      </td>
                      <td className="p-3 text-center">
                        <div className="flex flex-col items-center gap-1">
                          <span className={`text-sm font-medium ${getUsageColor(resource.memoryUsage)}`}>
                            {resource.memoryUsage.toFixed(1)}%
                          </span>
                          <div className="w-16 bg-muted rounded-full h-1.5">
                            <div
                              className={`h-1.5 rounded-full ${
                                resource.memoryUsage >= 80
                                  ? "bg-red-500"
                                  : resource.memoryUsage >= 60
                                  ? "bg-yellow-500"
                                  : "bg-green-500"
                              }`}
                              style={{ width: `${resource.memoryUsage}%` }}
                            />
                          </div>
                        </div>
                      </td>
                      <td className="p-3 text-center">
                        <div className="flex flex-col items-center gap-1">
                          <span className={`text-sm font-medium ${getUsageColor(resource.storageUsage)}`}>
                            {resource.storageUsage.toFixed(1)}%
                          </span>
                          <div className="w-16 bg-muted rounded-full h-1.5">
                            <div
                              className={`h-1.5 rounded-full ${
                                resource.storageUsage >= 80
                                  ? "bg-red-500"
                                  : resource.storageUsage >= 60
                                  ? "bg-yellow-500"
                                  : "bg-green-500"
                              }`}
                              style={{ width: `${resource.storageUsage}%` }}
                            />
                          </div>
                        </div>
                      </td>
                      <td className="p-3 text-center">
                        <div className="text-xs text-muted-foreground">
                          <div>↓ {(resource.networkIn / 1024).toFixed(2)} GB/s</div>
                          <div>↑ {(resource.networkOut / 1024).toFixed(2)} GB/s</div>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


