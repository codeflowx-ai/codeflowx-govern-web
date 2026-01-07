# END_INFRAESTRUCTURA
_Exported on 12/8/2025 at 19:41:46 GMT+1 from Cursor (2.0.77)_

---

**User**

@MIGRACION_INFRASTRUCTURE_DASHBOARDS.md @MIGRACION_INFRASTRUCTURE_DETAILS.md @MIGRACION_INFRASTRUCTURE_OVERVIEWS.md  realiza la migracion de estos prompts

---

**Cursor**

Revisando la estructura del proyecto y los archivos ZUL originales para identificar qué migrar.


Buscando los archivos ZUL en el proyecto suinsit.nova.web:


Creando el plan de trabajo y comenzando la migración. Revisando la estructura existente y los archivos ZUL necesarios:


Creando las páginas faltantes. Empezando por el dashboard y luego los detalles y overviews. Revisando la estructura de carpetas:


Creando las páginas faltantes siguiendo el patrón existente. Empezando por el dashboard y luego los detalles y overviews:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
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
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <BarChart3 className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.resourceUtilizationDashboard", "Resource Utilization Dashboard")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("infrastructure.resourceUtilizationDesc", "Monitor resource usage across all infrastructure")}
          </p>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300">
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
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
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
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
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
                      className="border-b border-border/50 hover:bg-muted/30 transition-colors"
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

```

Creando las páginas de detalles y overviews. Empezando por las más importantes:


Creando las páginas de detalles y overviews de forma sistemática. Empezando por las más importantes y usando plantillas reutilizables:


Creando las páginas más importantes primero. Empezando por detalles y overviews clave, luego actualizaremos el menú y las traducciones. Creando páginas de detalles:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  Activity,
  ArrowLeft,
  Database,
  Edit,
  HardDrive,
  Network,
  Server,
  Settings,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface CloudResourceDetail {
  id: number;
  cloudProviderId: number;
  resourceType: string;
  resourceName: string;
  resourceId?: string;
  description?: string;
  region?: string;
  zone?: string;
  specifications: any;
  costPerHour?: number;
  costPerMonth?: number;
  isAutoScaling: boolean;
  minInstances: number;
  maxInstances: number;
  currentInstances: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

// Mock data
const mockResourceDetail: CloudResourceDetail = {
  id: 1,
  cloudProviderId: 1,
  resourceType: "LOAD_BALANCER",
  resourceName: "Production Load Balancer",
  resourceId: "lb-prod-001",
  description: "Load balancer for production services",
  region: "us-east-1",
  zone: "us-east-1a",
  specifications: {
    type: "Application Load Balancer",
    scheme: "internet-facing",
    ip_address_type: "ipv4",
  },
  costPerHour: 0.0225,
  costPerMonth: 16.2,
  isAutoScaling: true,
  minInstances: 2,
  maxInstances: 10,
  currentInstances: 3,
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function CloudResourceDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";

  const resource = mockResourceDetail;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/resources">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Server className="w-8 h-8 text-primary" />
                {resource.resourceName}
              </h1>
              <p className="text-muted-foreground mt-1">{resource.description}</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge
              variant={resource.isActive ? "default" : "secondary"}
              className={resource.isActive ? "bg-green-500/20 text-green-500 border-green-500/50" : ""}
            >
              {resource.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        {/* Main Content */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Left Column - Main Info */}
          <div className="lg:col-span-2 space-y-6">
            {/* Basic Information */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{resource.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceId", "Resource ID")}
                    </label>
                    <p className="text-foreground font-medium">{resource.resourceId || "N/A"}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{resource.cloudProvider}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.region", "Region")}
                    </label>
                    <p className="text-foreground font-medium">{resource.region || "N/A"}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.zone", "Zone")}
                    </label>
                    <p className="text-foreground font-medium">{resource.zone || "N/A"}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.autoScaling", "Auto Scaling")}
                    </label>
                    <p className="text-foreground font-medium">
                      {resource.isAutoScaling ? t("common.yes", "Yes") : t("common.no", "No")}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Specifications */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Database className="w-5 h-5 text-primary" />
                  {t("infrastructure.specifications", "Specifications")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <pre className="text-sm bg-muted/50 p-4 rounded-lg overflow-x-auto">
                  {JSON.stringify(resource.specifications, null, 2)}
                </pre>
              </CardContent>
            </Card>

            {/* Scaling Configuration */}
            {resource.isAutoScaling && (
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Activity className="w-5 h-5 text-primary" />
                    {t("infrastructure.scalingConfiguration", "Scaling Configuration")}
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="grid grid-cols-3 gap-4">
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.minInstances", "Min Instances")}
                      </label>
                      <p className="text-2xl font-bold text-foreground">{resource.minInstances}</p>
                    </div>
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.currentInstances", "Current Instances")}
                      </label>
                      <p className="text-2xl font-bold text-primary">{resource.currentInstances}</p>
                    </div>
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.maxInstances", "Max Instances")}
                      </label>
                      <p className="text-2xl font-bold text-foreground">{resource.maxInstances}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            )}
          </div>

          {/* Right Column - Sidebar */}
          <div className="space-y-6">
            {/* Cost Information */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <HardDrive className="w-5 h-5 text-primary" />
                  {t("infrastructure.cost", "Cost")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {resource.costPerHour && (
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerHour", "Cost per Hour")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${resource.costPerHour.toFixed(4)}
                    </p>
                  </div>
                )}
                {resource.costPerMonth && (
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerMonth", "Cost per Month")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${resource.costPerMonth.toFixed(2)}
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Metadata */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Network className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(resource.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(resource.updatedAt).toLocaleString()}
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

```

Creando algunas páginas de overview y luego actualizando el menú y las traducciones para incluir todas las nuevas rutas. Continuando con las páginas más importantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Database,
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface CloudResource {
  id: number;
  resourceType: string;
  resourceName: string;
  resourceId?: string;
  region?: string;
  cloudProvider: string;
  isActive: boolean;
  costPerMonth?: number;
  currentInstances: number;
}

// Mock data
const mockResources: CloudResource[] = [
  {
    id: 1,
    resourceType: "LOAD_BALANCER",
    resourceName: "Production Load Balancer",
    resourceId: "lb-prod-001",
    region: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 16.2,
    currentInstances: 3,
  },
  {
    id: 2,
    resourceType: "STORAGE",
    resourceName: "Main Storage Bucket",
    resourceId: "storage-main-001",
    region: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 45.8,
    currentInstances: 1,
  },
  {
    id: 3,
    resourceType: "DATABASE",
    resourceName: "Main Database Cluster",
    resourceId: "db-main-001",
    region: "eu-west-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 234.5,
    currentInstances: 2,
  },
];

export default function CloudResourceOverviewPage() {
  const { t } = useTranslation();
  const [resources, setResources] = useState<CloudResource[]>(mockResources);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredResources = resources.filter((resource) =>
    resource.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    resource.resourceType.toLowerCase().includes(searchTerm.toLowerCase()) ||
    resource.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getResourceTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      LOAD_BALANCER: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      STORAGE: "bg-green-500/20 text-green-500 border-green-500/50",
      DATABASE: "bg-purple-500/20 text-purple-500 border-purple-500/50",
      CACHE: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      MESSAGE_QUEUE: "bg-orange-500/20 text-orange-500 border-orange-500/50",
    };
    return colors[type] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const getStatusBadge = (isActive: boolean) => {
    return isActive ? (
      <Badge className="bg-green-500/20 text-green-500 border-green-500/50">
        {t("common.active", "Active")}
      </Badge>
    ) : (
      <Badge variant="secondary">{t("common.inactive", "Inactive")}</Badge>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.cloudResources", "Cloud Resources")}
            </h1>
          </div>
          <Link href="/infrastructure/resources/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Resource")}
            </Button>
          </Link>
        </div>

        {/* Search and Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search resources...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Resources Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredResources.map((resource) => (
            <Card
              key={resource.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300 hover:shadow-xl"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{resource.resourceName}</CardTitle>
                    <div className="flex items-center gap-2 mb-2">
                      <Badge variant="outline" className={getResourceTypeBadge(resource.resourceType)}>
                        {resource.resourceType}
                      </Badge>
                      {getStatusBadge(resource.isActive)}
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{resource.cloudProvider}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.region", "Region")}</span>
                    <span className="text-foreground font-medium">{resource.region || "N/A"}</span>
                  </div>
                  {resource.costPerMonth && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                      <span className="text-foreground font-medium">${resource.costPerMonth.toFixed(2)}</span>
                    </div>
                  )}
                  {resource.currentInstances > 0 && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.instances", "Instances")}</span>
                      <span className="text-foreground font-medium">{resource.currentInstances}</span>
                    </div>
                  )}
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/cloud-resource-detail?id=${resource.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredResources.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Database className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noResourcesFound", "No resources found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Actualizando el menú y las traducciones para incluir todas las nuevas páginas. Empezando por el menú:


Actualizando el menú de Infrastructure para incluir todas las nuevas páginas:


```typescript
// Configuración centralizada de módulos y roles
// Este archivo es la única fuente de verdad para la configuración de módulos

export interface ModuleConfig {
  name: string;
  icon: any; // Componente de icono
  roles: string[];
  defaultPath: string;
  description?: string;
}

export interface MenuItem {
  name: string;
  href?: string;
  icon: any;
  roles: string[];
  children?: MenuItem[];
  isDefault?: boolean;
}

// Configuración centralizada de módulos
export const modulesConfig: ModuleConfig[] = [
  {
    name: "Dashboard",
    icon: "BarChart3",
    roles: [
      // Roles de negocio (nuevos)
      "agencia",
      "it",
      "oem",
      "business_admin",
      // Roles técnicos (existentes)
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/dashboard",
  },
  {
    name: "Code Playground",
    icon: "Zap",
    roles: ["it",  "developer", "devops"],
    defaultPath: "/code-playground",
  },
  {
    name: "AI Playground",
    icon: "Brain",
    roles: [   "developer", "ai_developer"],
    defaultPath: "/playground/chat",
  },
  {
    name: "RAG System",
    icon: "Database",
    roles: ["agencia", "it", "developer", "ai_analytics"],
    defaultPath: "/rag",
  },
  {
    name: "Model Management",
    icon: "Brain",
    roles: [ "it",  "developer", "ai_developer"],
    defaultPath: "/model-management",
  },
  {
    name: "Community IT",
    icon: "Users",
    roles: ["community_it"],
    defaultPath: "/projects/community",
  },
  {
    name: "Serving",
    icon: "Server",
    roles: ["it", "oem", "devops"],
    defaultPath: "/serving",
  },
  {
    name: "Governance",
    icon: "Shield",
    roles: ["project_manager"],
    defaultPath: "/governance",
  },
  {
    name: "Model Evaluation",
    icon: "Target",
    roles: ["agencia","ai_analytics"],
    defaultPath: "/model-evaluation",
  },
  {
    name: "AI Training",
    icon: "Brain",
    roles: ["ai_analytics",  "it", "oem"],
    defaultPath: "/training",
  },
  {
    name: "Technology Management",
    icon: "Layers",
    roles: ["developer",   "ai_analytics", "architect"],
    defaultPath: "/technology-management",
  },
  {
    name: "Domain Ingestion",
    icon: "Database",
    roles: [
      "admin","it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
    ],
    defaultPath: "/domain-ingestion",
  },
  {
    name: "Fuentes de Información",
    icon: "Database",
    roles: [
      "admin", "agencia", "it",
      "developer",
      "ai_analytics",
      "project_manager",
      "ai_developer",
      "architect",
    ],
    defaultPath: "/data-sources",
    description:
      "Gestión centralizada de fuentes de datos para RAG, dominios y entrenamiento de modelos",
  },
  {
    name: "Plugins",
    icon: "Plug",
    roles: [
      "developer", "it",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
    ],
    defaultPath: "/plugins",
  },
  {
    name: "Training Center",
    icon: "GraduationCap",
    roles: ["admin",    "developer", "viewer", "project_manager"],
    defaultPath: "/training-center",
  },
  {
    name: "Projects",
    icon: "FileText",
    roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
    defaultPath: "/projects",
  },
  // NUEVOS MÓDULOS DEL CORE
  {
    name: "Departments",
    icon: "Building",
    roles: ["admin"],
    defaultPath: "/admin/departments",
    description: "Gestión de departamentos y estructura organizacional",
  },
  {
    name: "User Sessions",
    icon: "Activity",
    roles: ["admin"],
    defaultPath: "/admin/sessions",
    description: "Monitorización y gestión de sesiones de usuario",
  },
  {
    name: "Audit System",
    icon: "FileText",
    roles: ["admin"],
    defaultPath: "/admin/audit",
    description: "Sistema de auditoría y logs del sistema",
  },
  {
    name: "Platform Licenses",
    icon: "Key",
    roles: ["admin"],
    defaultPath: "/admin/licenses",
    description: "Gestión de licencias de la plataforma",
  },
  {
    name: "Platform Nodes",
    icon: "Server",
    roles: ["admin"],
    defaultPath: "/admin/nodes",
    description: "Monitorización de nodos de la plataforma",
  },
  {
    name: "Platform Updates",
    icon: "Download",
    roles: ["admin"],
    defaultPath: "/admin/updates",
    description: "Gestión de actualizaciones de la plataforma",
  },
  {
    name: "Admin",
    icon: "Settings",
    roles: ["admin"],
    defaultPath: "/admin",
  },
  {
    name: "Infrastructure",
    icon: "Server",
    roles: ["admin",  "it", "oem", "devops", "architect"],
    defaultPath: "/infrastructure",
    description:
      "Gestión de infraestructura cloud, clusters K8s y GPU instances",
  },
  {
    name: "Agents",
    icon: "Bot",
    roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
    defaultPath: "/agents/monitoring/dashboard",
    description: "Gestión completa de agentes AI",
  },
  {
    name: "BPMN",
    icon: "Inbox",
    roles: [
      "admin",
      "developer",
      "viewer",
      "business_analytics",
      "ai_analytics",
      "ai_developer",
      "devops",
      "project_manager",
      "architect",
      "ai_architect",
      "agencia",
      "it",
      "oem",
      "business_admin"
    ],
    defaultPath: "/bpmn/task-inbox",
    description: "Bandeja de tareas BPMN y gestión de procesos",
  },
  {
    name: "Core",
    icon: "Settings",
    roles: ["admin", "developer"],
    defaultPath: "/core/admin-dashboard",
    description: "Gestión del sistema core: usuarios, departamentos, roles, permisos",
  },
];

// Función para obtener módulos por roles
export const getModulesByRoles = (roles: string[]): ModuleConfig[] => {
  return modulesConfig.filter((module) =>
    module.roles.some((role) => roles.includes(role))
  );
};

// Función para obtener módulo por ruta
export const getModuleByPath = (pathname: string): string => {
  const path = pathname.split("/")[1];

  const moduleMap: Record<string, string> = {
    dashboard: "Dashboard",
    "code-playground": "Code Playground",
    playground: "AI Playground",
    "model-management": "Model Management",
    serving: "Serving",
    "technology-management": "Technology Management",
    "domain-ingestion": "Domain Ingestion",
    "data-sources": "Fuentes de Información",
    admin: "Admin",
    team: "Admin",
    config: "Admin",
    "training-center": "Training Center",
    plugins: "Plugins",
    projects: "Projects",
    rag: "RAG System",
    "model-evaluation": "Model Evaluation",
    training: "AI Training",
    governance: "Governance",
    bpmn: "BPMN",
    // NUEVAS RUTAS DEL CORE
    departments: "Departments",
    sessions: "User Sessions",
    audit: "Audit System",
    licenses: "Platform Licenses",
    nodes: "Platform Nodes",
    updates: "Platform Updates",
    // INFRASTRUCTURE
    infrastructure: "Infrastructure",
    agents: "Agents",
  };

  return moduleMap[path] || "Dashboard";
};

// Función para validar si un usuario puede acceder a un módulo
export const canAccessModule = (
  userRoles: string[],
  moduleName: string
): boolean => {
  const module = modulesConfig.find((m) => m.name === moduleName);
  return module ? module.roles.some((role) => userRoles.includes(role)) : false;
};

// Función para obtener el menú de un módulo específico
export const getMenuByModule = (moduleName: string): MenuItem[] => {
  switch (moduleName) {
    case "Dashboard":
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "Code Playground":
      return [
        {
          name: "Code Editor",
          href: `/code-playground`,
          icon: "Zap",
          roles: ["admin", "developer", "devops"],
          isDefault: true,
        },
        {
          name: "Snippets",
          href: `/code-playground/snippets`,
          icon: "FileText",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "Templates",
          href: `/code-playground/templates`,
          icon: "ClipboardList",
          roles: ["admin", "developer", "devops"],
        },
        {
          name: "AI Notebook",
          href: `/ai-notebook`,
          icon: "Brain",
          roles: ["admin", "developer", "ai_analytics", "ai_developer"],
          isDefault: false,
        },
      ];
    case "AI Playground":
      return [
        {
          name: "Chat & Text",
          href: `/playground/chat`,
          icon: "MessageCircle",
          roles: ["admin",  "agencia",   "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Image Generation",
          href: `/playground/images`,
          icon: "Image",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Translation",
          href: `/playground/translation`,
          icon: "Layers",
          roles: ["admin",     "developer", "ai_analytics"],
        },
        {
          name: "Smart Routing",
          href: `/playground/smart-routing`,
          icon: "GitBranch",
          roles: ["admin",    "developer", "ai_analytics"],
        },
        {
          name: "Voice & Audio",
          href: `/playground/voice`,
          icon: "Mic",
          roles: ["admin", "developer", "ai_analytics"],
        },
      ];
    case "Model Management":
      return [
        {
          name: "Dashboard",
          href: `/model-management`,
          icon: "BarChart3",
          roles: [ "it",  "developer", "ai_developer"],
          isDefault: true,
        },
        {
          name: "Modelos",
          href: `/model-management/models`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Proveedores",
          href: `/model-management/providers`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Marketplace",
          href: `/model-management/marketplace`,
          icon: "ShoppingCart",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Experimentos",
          href: `/model-management/experiments`,
          icon: "TestTube",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Serving",
          href: `/model-management/serving`,
          icon: "Server",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Artefactos",
          href: `/model-management/artifacts`,
          icon: "Package",
          roles: [ "it",  "developer", "ai_developer"],
        },
        {
          name: "Ejecuciones",
          href: `/model-management/runs`,
          icon: "Activity",
          roles: [ "it",  "developer", "ai_developer"],
        },
      ];
    case "Serving":
      return [
        {
          name: "Deployments",
          href: `/serving`,
          icon: "Server",
          roles: [ "it", "oem", "devops"],
          isDefault: true,
        },
        {
          name: "Monitoring",
          href: `/serving/monitoring`,
          icon: "Activity",
          roles: [ "it", "oem", "devops"],
        },
        {
          name: "Resources",
          href: `/serving/resources`,
          icon: "Database",
          roles: [ "it", "oem", "devops"],
        },
      ];
    case "Technology Management":
      return [
        {
          name: "Dashboard",
          href: `/technology-management`,
          icon: "BarChart3",
          roles: ["developer", "ai_analytics", "architect"],
          isDefault: true,
        },
        {
          name: "Technologies",
          href: `/technology-management/technologies/catalog`,
          icon: "Code",
          roles: ["developer", "ai_analytics", "architect"],
          children: [
            {
              name: "Catálogo",
              href: `/technology-management/technologies/catalog`,
              icon: "Code",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Crear Tecnología",
              href: `/technology-management/technologies/create`,
              icon: "Plus",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Discovery",
              href: `/technology-management/technologies/discovery`,
              icon: "Search",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Datasets",
              href: `/technology-management/technologies/datasets`,
              icon: "Database",
              roles: ["developer", "ai_analytics", "architect"],
            },
            {
              name: "Webscraping",
              href: `/technology-management/technologies/webscraping`,
              icon: "Globe",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Marketplace",
              href: `/technology-management/technologies/marketplace`,
              icon: "ShoppingCart",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Repositories",
              href: `/technology-management/technologies/repositories`,
              icon: "Github",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Architectures",
          href: `/technology-management/architectures/stacks`,
          icon: "Layers",
          roles: ["developer", ,"ai_analytics", "architect"],
          children: [
            {
              name: "Stacks",
              href: `/technology-management/architectures/stacks`,
              icon: "Layers",
              roles: ["developer",, "ai_analytics", "architect"],
            },
            {
              name: "Crear Stack",
              href: `/technology-management/architectures/stacks/create`,
              icon: "Plus",
              roles: ["developer", ,"ai_analytics", "architect"],
            },
            {
              name: "Metodologías",
              href: `/technology-management/architectures/methodologies`,
              icon: "Settings",
              roles: ["developer","ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Specialized Models",
          href: `/technology-management/specialized-models/models`,
          icon: "Brain",
          roles: ["developer",  "ai_analytics", "architect"],
          children: [
            {
              name: "Modelos",
              href: `/technology-management/specialized-models/models`,
              icon: "Brain",
              roles: ["developer",  "ai_analytics", "architect"],
            },
            {
              name: "Entrenamiento",
              href: `/technology-management/specialized-models/training`,
              icon: "Play",
              roles: ["developer",  "ai_analytics", "architect"],
            },
          ],
        },
        {
          name: "Model Evaluation",
          href: `/technology-management/model-evaluation`,
          icon: "Target",
          roles: ["developer", "it", "ai_analytics", "architect"],
        },
      ];
    case "Domain Ingestion":
      return [
        {
          name: "Dashboard",
          href: "/domain-ingestion",
          icon: "BarChart3",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
          isDefault: true,
        },
        {
          name: "Asistente de Dominios",
          href: "/domain-ingestion/wizard",
          icon: "Bot",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Gestionar Dominios",
          href: "/domain-ingestion/manage",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Monitorear Jobs",
          href: "/domain-ingestion/monitor",
          icon: "Activity",
          roles: [
            "admin",   "it", "oem",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
        {
          name: "Configuración",
          href: "/domain-ingestion/settings",
          icon: "Settings",
          roles: [
            "admin",   "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
          ],
        },
      ];
    case "Fuentes de Información":
      return [
        {
          name: "Dashboard",
          href: "/data-sources",
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
          isDefault: true,
        },
        {
          name: "Bases de Datos",
          href: "/data-sources/database",
          icon: "Database",
          roles: [
            "admin",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "APIs",
          href: "/data-sources/api",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Documentos",
          href: "/data-sources/upload-documents",
          icon: "FileText",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
        {
          name: "Web Scraping",
          href: "/data-sources/web-scraping",
          icon: "Globe",
          roles: [
            "admin",  "it",
            "developer",
            "ai_analytics",
            "project_manager",
            "ai_developer",
            "architect","agencia"
          ],
        },
      ];
    // NUEVOS MÓDULOS DEL CORE
    case "Departments":
      return [
        {
          name: "Department Management",
          href: `/admin/departments`,
          icon: "Building",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Department Hierarchy",
          href: `/admin/departments/hierarchy`,
          icon: "Network",
          roles: ["admin"],
        },
        {
          name: "Department Stats",
          href: `/admin/departments/stats`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "User Sessions":
      return [
        {
          name: "Active Sessions",
          href: `/admin/sessions`,
          icon: "Activity",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Session Management",
          href: `/admin/sessions/manage`,
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Session Analytics",
          href: `/admin/sessions/analytics`,
          icon: "BarChart3",
          roles: ["admin"],
        },
      ];
    case "Audit System":
      return [
        {
          name: "Audit Logs",
          href: `/admin/audit`,
          icon: "FileText",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Audit Reports",
          href: `/admin/audit/reports`,
          icon: "BarChart3",
          roles: ["admin"],
        },
        {
          name: "Security Alerts",
          href: `/admin/audit/alerts`,
          icon: "Shield",
          roles: ["admin"],
        },
        {
          name: "Compliance Dashboard",
          href: `/admin/audit/compliance`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
      ];
    case "Platform Licenses":
      return [
        {
          name: "License Management",
          href: `/admin/licenses`,
          icon: "Key",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "License Validation",
          href: `/admin/licenses/validate`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Resource Limits",
          href: `/admin/licenses/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "License History",
          href: `/admin/licenses/history`,
          icon: "Clock",
          roles: ["admin"],
        },
      ];
    case "Platform Nodes":
      return [
        {
          name: "Node Monitoring",
          href: `/admin/nodes`,
          icon: "Server",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Resource Usage",
          href: `/admin/nodes/resources`,
          icon: "Gauge",
          roles: ["admin"],
        },
        {
          name: "GPU Monitoring",
          href: `/admin/nodes/gpu`,
          icon: "Cpu",
          roles: ["admin"],
        },
        {
          name: "Node Health",
          href: `/admin/nodes/health`,
          icon: "Heart",
          roles: ["admin"],
        },
      ];
    case "Platform Updates":
      return [
        {
          name: "Update Management",
          href: `/admin/updates`,
          icon: "Download",
          roles: ["admin"],
          isDefault: true,
        },
        {
          name: "Installation Status",
          href: `/admin/updates/installations`,
          icon: "CheckCircle",
          roles: ["admin"],
        },
        {
          name: "Update Scheduling",
          href: `/admin/updates/schedule`,
          icon: "Calendar",
          roles: ["admin"],
        },
        {
          name: "Rollback Management",
          href: `/admin/updates/rollback`,
          icon: "RotateCcw",
          roles: ["admin"],
        },
      ];
    case "Admin":
      return [
        {
          name: "Admin Panel",
          href: `/admin`,
          icon: "Settings",
          roles: ["admin"],
          isDefault: true,
        },
        { name: "Equipo", href: "/team", icon: "Users", roles: ["admin"] },
        {
          name: "Demo Users",
          href: "/config/demo-users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "User Management",
          href: "/config/users",
          icon: "Users",
          roles: ["admin"],
        },
        {
          name: "Role Configuration",
          href: "/config/roles",
          icon: "Settings",
          roles: ["admin"],
        },
        {
          name: "Menu Management",
          href: "/admin/menu-management",
          icon: "Menu",
          roles: ["admin"],
        },
        // NUEVOS MÓDULOS DEL CORE
        {
          name: "Departments",
          href: "/admin/departments",
          icon: "Building",
          roles: ["admin"],
        },
        {
          name: "User Sessions",
          href: "/admin/sessions",
          icon: "Activity",
          roles: ["admin"],
        },
        {
          name: "Audit System",
          href: "/admin/audit",
          icon: "FileText",
          roles: ["admin"],
        },
        {
          name: "Platform Licenses",
          href: "/admin/licenses",
          icon: "Key",
          roles: ["admin"],
        },
        {
          name: "Platform Nodes",
          href: "/admin/nodes",
          icon: "Server",
          roles: ["admin"],
        },
        {
          name: "Platform Updates",
          href: "/admin/updates",
          icon: "Download",
          roles: ["admin"],
        },
      ];
    case "Plugins":
      return [
        {
          name: "Plugins",
          href: `/plugins`,
          icon: "Plug",
          roles: [
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
          ],
          isDefault: true,
        },
      ];
    case "Training Center":
      return [
        {
          name: "Training Center",
          href: `/training-center`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "viewer", "project_manager"],
          isDefault: true,
        },
      ];
    case "Projects":
      return [
        {
          name: "Dashboard Proyectos",
          href: `/projects`,
          icon: "FileText",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
          isDefault: true,
        },
        {
          name: "Proyectos Conversacionales",
          href: `/projects/conversational`,
          icon: "MessageSquare",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Nuevo Proyecto",
          href: `/projects/conversational/new`,
          icon: "Plus",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
        {
          name: "Proyectos de Generación",
          href: `/projects/generation`,
          icon: "Zap",
          roles: ["agencia", "it", "oem", "business_admin", "admin", "developer", "project_manager"],
        },
      ];
    case "RAG System":
      return [
        {
          name: "RAG Dashboard",
          href: `/rag`,
          icon: "Database",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
          isDefault: true,
        },
        {
          name: "Proyectos RAG",
          href: `/rag/projects`,
          icon: "Brain",
          roles: ["agencia", "it", , "developer", "ai_analytics"],
        },
        {
          name: "Modelos RAG",
          href: `/rag/models`,
          icon: "Zap",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Chat",
          href: `/rag/chat`,
          icon: "MessageSquare",
          roles: ["agencia","developer", "ai_analytics"],
        },
        {
          name: "Reranker",
          href: `/rag/reranker`,
          icon: "TrendingUp",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Chunks",
          href: `/rag/chunks`,
          icon: "Hash",
          roles: ["agencia", "it",  "developer", "ai_analytics"],
        },
        {
          name: "Búsqueda Semántica",
          href: `/rag/search`,
          icon: "Search",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
        {
          name: "Analytics",
          href: `/rag/analytics`,
          icon: "BarChart3",
          roles: ["agencia", "it", "developer", "ai_analytics"],
        },
      ];
    case "Model Evaluation":
      return [
        {
          name: "Evaluations",
          href: `/model-evaluation`,
          icon: "Target",
          roles: ["agencia","ai_analytics"],
          isDefault: true,
        },
        {
          name: "Activity",
          href: `/model-evaluation/activity`,
          icon: "Activity",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Reports",
          href: `/model-evaluation/reports`,
          icon: "FileText",
          roles: ["agencia","ai_analytics"],
        },
        {
          name: "Security",
          href: `/model-evaluation/security`,
          icon: "Shield",
          roles: ["agencia","ai_analytics"],
        },
      ];
    case "AI Training":
      return [
        {
          name: "Training Jobs",
          href: `/training`,
          icon: "Brain",
          roles: ["ai_analytics"],
          isDefault: true,
        },
        {
          name: "Experiments",
          href: `/training/experiments`,
          icon: "TestTube",
          roles: ["ai_analytics"],
        },
        {
          name: "Models",
          href: `/training/models`,
          icon: "Layers",
          roles: ["ai_analytics"],
        },
        {
          name: "Datasets",
          href: `/training/datasets`,
          icon: "Database",
          roles: ["ai_analytics"],
        },
      ];
    case "Governance":
      return [
        {
          name: "Overview",
          href: `/governance/overview`,
          icon: "Shield",
          roles: ["project_manager"],
          isDefault: true,
        },
        {
          name: "Policies",
          href: `/governance/policies`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Security",
          href: `/governance/security`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Monitoring",
          href: `/governance/monitoring`,
          icon: "Activity",
          roles: ["project_manager"],
        },
        {
          name: "Compliance",
          href: `/governance/compliance`,
          icon: "Shield",
          roles: ["project_manager"],
        },
        {
          name: "Compliance Dashboard",
          href: `/governance/compliance/dashboard`,
          icon: "BarChart3",
          roles: ["project_manager"],
        },
        {
          name: "Post-Market Monitoring",
          href: `/governance/compliance/post-market-monitoring`,
          icon: "ChartLine",
          roles: ["project_manager"],
        },
        {
          name: "Sector Dashboard",
          href: `/governance/compliance/sector`,
          icon: "Building2",
          roles: ["project_manager"],
        },
        {
          name: "Conformity Declaration",
          href: `/governance/compliance/conformity-declaration-manager`,
          icon: "Certificate",
          roles: ["project_manager"],
        },
        {
          name: "Conformity Review",
          href: `/governance/compliance/conformity-review`,
          icon: "UserCheck",
          roles: ["project_manager"],
        },
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
        },
      ];
    case "Infrastructure":
      return [
        {
          name: "Infrastructure Overview",
          href: `/infrastructure`,
          icon: "Server",
          roles: ["admin", "devops", "architect", "it", "oem"],
          isDefault: true,
        },
        {
          name: "Resource Utilization Dashboard",
          href: `/infrastructure/resource-utilization-dashboard`,
          icon: "BarChart3",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Providers",
          href: `/infrastructure/providers`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Provider Overview",
          href: `/infrastructure/cloud-provider-overview`,
          icon: "Cloud",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Credentials",
          href: `/infrastructure/credentials`,
          icon: "Key",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Credential Overview",
          href: `/infrastructure/cloud-credential-overview`,
          icon: "Key",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Regions",
          href: `/infrastructure/cloud-region-overview`,
          icon: "Globe",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Resources",
          href: `/infrastructure/resources`,
          icon: "Database",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cloud Resource Overview",
          href: `/infrastructure/cloud-resource-overview`,
          icon: "Database",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Kubernetes Clusters",
          href: `/infrastructure/kubernetes`,
          icon: "Server",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Kubernetes Cluster Overview",
          href: `/infrastructure/kubernetes-cluster-overview`,
          icon: "Server",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Kubernetes Nodes",
          href: `/infrastructure/kubernetes-node-overview`,
          icon: "Server",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "GPU Instances",
          href: `/infrastructure/gpu-instances`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "GPU Instance Overview",
          href: `/infrastructure/gpu-instance-overview`,
          icon: "Cpu",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Deployments",
          href: `/infrastructure/deployments`,
          icon: "Rocket",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Templates",
          href: `/infrastructure/infrastructure-template-overview`,
          icon: "FileText",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Metrics",
          href: `/infrastructure/infrastructure-metric-overview`,
          icon: "Activity",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Metrics Summary",
          href: `/infrastructure/infrastructure-metrics-summary-overview`,
          icon: "BarChart3",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Costs",
          href: `/infrastructure/infrastructure-cost-overview`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Cost Analysis",
          href: `/infrastructure/infrastructure-cost-analysis-overview`,
          icon: "TrendingUp",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Audit",
          href: `/infrastructure/infrastructure-audit-overview`,
          icon: "FileText",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Health Matrix",
          href: `/infrastructure/infrastructure-health-matrix-overview`,
          icon: "Heart",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Infrastructure Overview",
          href: `/infrastructure/infrastructure-overview-overview`,
          icon: "Server",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Capacity Forecast",
          href: `/infrastructure/capacity-forecast-overview`,
          icon: "TrendingUp",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Resource Quotas",
          href: `/infrastructure/resource-quota-overview`,
          icon: "Gauge",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
        {
          name: "Cost Management",
          href: `/infrastructure/cost-management`,
          icon: "DollarSign",
          roles: ["admin", "devops", "architect", "it", "oem"],
        },
      ];
    case "Core":
      return [
        {
          name: "Admin Dashboard",
          href: `/core/admin-dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer"],
          isDefault: true,
        },
        {
          name: "Departamentos",
          href: `/core/departments`,
          icon: "Building2",
          roles: ["admin", "developer"],
        },
        {
          name: "Usuarios",
          href: `/core/users`,
          icon: "Users",
          roles: ["admin", "developer"],
        },
        {
          name: "Roles",
          href: `/core/roles`,
          icon: "Shield",
          roles: ["admin", "developer"],
        },
        {
          name: "Permisos",
          href: `/core/permissions`,
          icon: "Key",
          roles: ["admin", "developer"],
        },
        {
          name: "Menús",
          href: `/core/menus`,
          icon: "Menu",
          roles: ["admin", "developer"],
        },
        {
          name: "Sesiones de Usuario",
          href: `/core/user-sessions`,
          icon: "Activity",
          roles: ["admin", "developer"],
        },
        {
          name: "Intentos de Login",
          href: `/core/login-attempts`,
          icon: "LogIn",
          roles: ["admin", "developer"],
        },
      ];
    default:
      return [
        {
          name: "Dashboard",
          href: `/dashboard`,
          icon: "BarChart3",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "devops",
            "project_manager",
          ],
          isDefault: true,
        },
        {
          name: "Configuración",
          href: `/settings`,
          icon: "Settings",
          roles: ["admin"],
        },
      ];
    case "BPMN":
      return [
        {
          name: "BPMN Task Inbox",
          href: `/bpmn/task-inbox`,
          icon: "Inbox",
          roles: [
            "admin",
            "developer",
            "viewer",
            "business_analytics",
            "ai_analytics",
            "ai_developer",
            "devops",
            "project_manager",
            "architect",
            "ai_architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: true,
        },
        {
          name: "Internal Audit Scheduling",
          href: `/bpmn/forms/internal-audit-scheduling`,
          icon: "Calendar",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Agent - Override Humano",
          href: `/bpmn/forms/agent-approval-human-override`,
          icon: "AlertTriangle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Aprobación de Modelo - Override Humano",
          href: `/bpmn/forms/model-approval-human-override`,
          icon: "Brain",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Revisión Humana de Prompt",
          href: `/bpmn/forms/prompt-human-review`,
          icon: "FileText",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
        {
          name: "Recordatorio SLA HITL",
          href: `/bpmn/forms/hitl-sla-reminder`,
          icon: "AlertCircle",
          roles: [
            "admin",
            "developer",
            "project_manager",
            "architect",
            "agencia",
            "it",
            "oem",
            "business_admin"
          ],
          isDefault: false,
        },
      ];
    case "Community IT":
      return [
        {
          name: "Proyectos Community",
          href: `/projects/community`,
          icon: "FileText",
          roles: ["community_it"],
          isDefault: true,
        },
        {
          name: "Conocimiento Community",
          href: `/rag/community`,
          icon: "Database",
          roles: ["community_it"],
        },
        {
          name: "Modelos Community",
          href: `/modelos/community`,
          icon: "Brain",
          roles: ["community_it"],
        },
      ];
    case "Agents":
      return [
        {
          name: "Dashboard",
          href: `/agents/monitoring/dashboard`,
          icon: "BarChart3",
          roles: ["admin", "developer", "ai_developer", "project_manager", "architect"],
          isDefault: true,
        },
        {
          name: "Alerts",
          href: `/agents/alerts/overview`,
          icon: "AlertTriangle",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Approval",
          href: `/agents/approval/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Bias Detection",
          href: `/agents/bias-detection/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Compliance",
          href: `/agents/compliance/overview`,
          icon: "CheckCircle2",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Decisions",
          href: `/agents/decisions/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Deployment",
          href: `/agents/deployment/overview`,
          icon: "Rocket",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Ethics",
          href: `/agents/ethics/overview`,
          icon: "Heart",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Governance",
          href: `/agents/governance/overview`,
          icon: "Shield",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Interactions",
          href: `/agents/interactions/overview`,
          icon: "Users",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Learning",
          href: `/agents/learning/overview`,
          icon: "GraduationCap",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Monitoring",
          href: `/agents/monitoring/overview`,
          icon: "Activity",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Registry",
          href: `/agents/registry/domain-overview`,
          icon: "Database",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Rollback",
          href: `/agents/rollback/overview`,
          icon: "RotateCcw",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Tools",
          href: `/agents/tools/overview`,
          icon: "Wrench",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Transparency",
          href: `/agents/transparency/overview`,
          icon: "Eye",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Versioning",
          href: `/agents/versioning/overview`,
          icon: "GitBranch",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
        {
          name: "Workflow",
          href: `/agents/workflow/execution-overview`,
          icon: "Workflow",
          roles: ["admin", "developer", "ai_developer", "project_manager"],
        },
      ];
  }
};

```

Revisando las traducciones para añadir las nuevas claves:


Añadiendo las traducciones necesarias para las nuevas páginas:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Buscando la sección de inglés para añadir las traducciones:


Añadiendo las traducciones en inglés:


Añadiendo las traducciones en inglés:


```typescript
// Configuración de internacionalización para CodeflowX
import { useEffect, useState } from "react";

export type Language = "es" | "en";

export interface I18nConfig {
  language: Language;
  fallbackLanguage: Language;
  availableLanguages: Language[];
}

export const i18nConfig: I18nConfig = {
  language: "es",
  fallbackLanguage: "es",
  availableLanguages: ["es", "en"],
};

// Traducciones para la aplicación
export const translations = {
  es: {
    // Navegación y layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infraestructura",
      admin: "Administración",
      aiNotebook: "AI Notebook",
      domainIngestion: "Ingesta de Dominios",
      modelEvaluation: "Evaluación de Modelos",
      ragTraining: "Entrenamiento RAG",
      governance: "Gobernanza",
      settings: "Configuración",
      profile: "Perfil",
      logout: "Cerrar Sesión",
      loading: "Cargando...",
      save: "Guardar",
      cancel: "Cancelar",
      edit: "Editar",
      delete: "Eliminar",
      add: "Añadir",
      search: "Buscar",
      filter: "Filtrar",
      status: "Estado",
      actions: "Acciones",
      details: "Detalles",
      back: "Volver",
      next: "Siguiente",
      previous: "Anterior",
      close: "Cerrar",
      confirm: "Confirmar",
      yes: "Sí",
      help: "Ayuda",
      no: "No",
      active: "Activo",
      inactive: "Inactivo",
      pending: "Pendiente",
      completed: "Completado",
      error: "Error",
      success: "Éxito",
      warning: "Advertencia",
      info: "Información",
      changeLanguage: "Cambiar idioma",
      selectLanguage: "Seleccionar idioma",
      noResults: "No se encontraron resultados",
      showing: "Mostrando",
      all: "TODOS",
      clearFilters: "Limpiar filtros",
    },

    // Prompts
    prompts: {
      performance: {
        title: "Prompt Performance Comparison",
        description: "Gestión de Prompt Performance Comparison",
        results: "Resultados",
        id: "ID",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
        avgScore: "Avg Score",
      },
      validation: {
        title: "Prompt Validation",
        description: "Gestión de Prompt Validation",
        register: "Registrar Prompt Validation",
        results: "Resultados",
        id: "ID",
        validationtype: "Validationtype",
        validationresult: "Validationresult",
        validationscore: "Validationscore",
        validationdetails: "Validationdetails",
        issuesfound: "Issuesfound",
        filterType: "Filtrar por Validationtype",
        filterStatus: "Filtrar por Status",
        type1: "TIPO 1",
        type2: "TIPO 2",
      },
      versioning: {
        title: "Prompt Version",
        description: "Gestión de Prompt Version",
        register: "Registrar Prompt Version",
        results: "Resultados",
        id: "ID",
        version: "Version",
        description: "Description",
        content: "Content",
        parameters: "Parameter S",
        changes: "Changes",
        filterStatus: "Filtrar por Status",
      },
    },

    // Dashboard
    dashboard: {
      title: "Panel de Control",
      welcome: "Bienvenido a CodeflowX",
      stats: "Estadísticas",
      quickActions: "Acciones Rápidas",
      recentProjects: "Proyectos Recientes",
      systemMetrics: "Métricas del Sistema",
      totalProjects: "Total de Proyectos",
      activeModels: "Modelos Activos",
      totalUsers: "Total de Usuarios",
      systemHealth: "Salud del Sistema",
      viewAll: "Ver Todos",
      createNew: "Crear Nuevo",
      lastUpdated: "Última Actualización",
      performance: "Rendimiento",
      efficiency: "Eficiencia",
      reliability: "Confiabilidad",
      activeNodes: "Nodos Activos",
      of: "de",
      plusOneThisMonth: "+1 este mes",
      codeGenerated: "Código Generado",
      lines: "líneas",
      modelsAvailable: "Modelos Disponibles",
      specialized: "especializados",
      plusThreeNew: "+3 nuevos",
      licenseStatus: "Estado de Licencia",
      proPlan: "Plan Pro",
      deployModel: "Desplegar Modelo",
      viewAnalytics: "Ver Analíticas",
      manageUsers: "Gestionar Usuarios",
      systemHealthTitle: "Salud del Sistema",
      cpuUsage: "Uso de CPU",
      memory: "Memoria",
      network: "Red",
      security: "Seguridad",
    },

    // Infraestructura
    infrastructure: {
      title: "Gestión de Infraestructura",
      overview: "Vista General",
      providers: "Proveedores",
      kubernetes: "Clusters Kubernetes",
      gpuInstances: "Instancias GPU",
      deployments: "Despliegues",
      resources: "Recursos",
      credentials: "Credenciales",
      costManagement: "Gestión de Costos",
      systemMonitoring: "Monitoreo del Sistema",
      addProvider: "Añadir Proveedor",
      addCluster: "Añadir Cluster",
      addInstance: "Añadir Instancia",
      clusterName: "Nombre del Cluster",
      instanceType: "Tipo de Instancia",
      region: "Región",
      status: "Estado",
      nodes: "Nodos",
      cpu: "CPU",
      memory: "Memoria",
      storage: "Almacenamiento",
      network: "Red",
      security: "Seguridad",
      monitoring: "Monitoreo",
      logs: "Logs",
      metrics: "Métricas",
      alerts: "Alertas",
      // Nuevas traducciones para la página principal
      cloudProviders: "Proveedores Cloud",
      cloudProvidersDesc:
        "Gestiona proveedores cloud, credenciales y conexiones",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credenciales y API Keys",
      statusMonitoring: "Monitoreo de estado",
      kubernetesDesc: "Administra clusters K8s, nodos y recursos",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Clusters propios",
      nodeMonitoring: "Monitoreo de nodos",
      deploymentManagement: "Gestión de deployments",
      gpuInstancesDesc: "Gestión de instancias GPU para training e inferencia",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Análisis de costes",
      infrastructureDeployments: "Despliegues de Infraestructura",
      deploymentsDesc: "Despliegues de infraestructura y servicios",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Escalado automático",
      costManagementDesc: "Análisis y gestión de costes de infraestructura",
      providerAnalysis: "Análisis por proveedor",
      budgetAlerts: "Alertas de presupuesto",
      reports: "Reportes",
      optimization: "Optimización",
      systemMonitoringDesc:
        "Monitoreo en tiempo real de toda la infraestructura",
      realTimeMetrics: "Métricas en tiempo real",
      automaticAlerts: "Alertas automáticas",
      customDashboards: "Dashboards personalizados",
      centralizedLogs: "Logs centralizados",
      twoWeeksAgo: "hace 2 semanas",
      deployCluster: "Desplegar Cluster",
      launchGpu: "Lanzar GPU",
      viewCosts: "Ver Costes",
      // Nuevas traducciones para páginas migradas
      resourceUtilizationDashboard: "Dashboard de Utilización de Recursos",
      resourceUtilizationDesc: "Monitorea el uso de recursos en toda la infraestructura",
      resourceUtilization: "Utilización de Recursos",
      totalResources: "Total de Recursos",
      active: "activo",
      avgCpuUsage: "Uso Promedio de CPU",
      avgMemoryUsage: "Uso Promedio de Memoria",
      avgStorageUsage: "Uso Promedio de Almacenamiento",
      networkUsage: "Uso de Red",
      networkIn: "Entrada de Red",
      networkOut: "Salida de Red",
      resourceName: "Nombre del Recurso",
      resourceType: "Tipo de Recurso",
      cloudResources: "Recursos Cloud",
      basicInformation: "Información Básica",
      resourceId: "ID del Recurso",
      cloudProvider: "Proveedor Cloud",
      zone: "Zona",
      autoScaling: "Escalado Automático",
      specifications: "Especificaciones",
      scalingConfiguration: "Configuración de Escalado",
      minInstances: "Instancias Mínimas",
      currentInstances: "Instancias Actuales",
      maxInstances: "Instancias Máximas",
      cost: "Costo",
      costPerHour: "Costo por Hora",
      costPerMonth: "Costo por Mes",
      metadata: "Metadatos",
      createdAt: "Creado En",
      updatedAt: "Actualizado En",
      instances: "Instancias",
      noResourcesFound: "No se encontraron recursos",
      cloudProviderOverview: "Vista General de Proveedores Cloud",
      cloudCredentialOverview: "Vista General de Credenciales Cloud",
      cloudRegionOverview: "Vista General de Regiones Cloud",
      cloudResourceOverview: "Vista General de Recursos Cloud",
      kubernetesClusterOverview: "Vista General de Clusters Kubernetes",
      kubernetesNodeOverview: "Vista General de Nodos Kubernetes",
      gpuInstanceOverview: "Vista General de Instancias GPU",
      infrastructureTemplateOverview: "Vista General de Plantillas de Infraestructura",
      infrastructureMetricOverview: "Vista General de Métricas de Infraestructura",
      infrastructureMetricsSummaryOverview: "Vista General de Resumen de Métricas",
      infrastructureCostOverview: "Vista General de Costos de Infraestructura",
      infrastructureCostAnalysisOverview: "Vista General de Análisis de Costos",
      infrastructureAuditOverview: "Vista General de Auditoría de Infraestructura",
      infrastructureHealthMatrixOverview: "Vista General de Matriz de Salud",
      infrastructureOverviewOverview: "Vista General de Infraestructura",
      capacityForecastOverview: "Vista General de Pronóstico de Capacidad",
      resourceQuotaOverview: "Vista General de Cuotas de Recursos",
    },

    // Administración
    admin: {
      title: "Panel de Administración",
      users: "Usuarios",
      roles: "Roles",
      permissions: "Permisos",
      departments: "Departamentos",
      sessions: "Sesiones",
      audit: "Auditoría",
      licenses: "Licencias",
      nodes: "Nodos",
      updates: "Actualizaciones",
      menuManagement: "Gestión de Menús",
      notifications: "Notificaciones",
      systemSettings: "Configuración del Sistema",
      userManagement: "Gestión de Usuarios",
      systemHealth: "Salud del Sistema",
      securitySettings: "Configuración de Seguridad",
      backupRestore: "Respaldo y Restauración",
      systemLogs: "Logs del Sistema",
      performanceMetrics: "Métricas de Rendimiento",
      // Nuevas traducciones para la página principal
      twoHoursAgo: "hace 2 horas",
      twoMinutesAgo: "hace 2 minutos",
      fiveMinutesAgo: "hace 5 minutos",
      tenMinutesAgo: "hace 10 minutos",
      fifteenMinutesAgo: "hace 15 minutos",
      userLogin: "Inicio de sesión de usuario",
      roleUpdated: "Rol actualizado",
      newUserInvited: "Nuevo usuario invitado",
      systemBackupCompleted: "Respaldo del sistema completado",
      configurationUpdated: "Configuración actualizada",
    },

    // Temas
    themes: {
      title: "Temas de CodeflowX",
      changeTheme: "Cambiar tema de CodeflowX",
      useSystemTheme: "Usar tema del sistema",
      trekkerFederation: "🌌 Leka Federación",
      trekkerVulcan: "🖖 Leka Vulcano",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulano",
      trekkerCardassian: "🖖 Leka Cardassiano",
      trekkerBajoran: "🖖 Leka Bajorano",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andoriano",
      trekkerTellarite: "🖖 Leka Tellarita",
      trekkerDenobulan: "🏥 Leka Denobulano",
    },

    // Estados y colores
    status: {
      online: "En Línea",
      offline: "Desconectado",
      maintenance: "Mantenimiento",
      error: "Error",
      warning: "Advertencia",
      info: "Información",
      success: "Éxito",
      pending: "Pendiente",
      running: "Ejecutando",
      stopped: "Detenido",
      starting: "Iniciando",
      stopping: "Deteniendo",
      healthy: "Saludable",
      unhealthy: "No Saludable",
      degraded: "Degradado",
    },

    // Mensajes del sistema
    messages: {
      loadingTheme: "Cargando tema...",
      themeApplied: "Tema aplicado correctamente",
      themeError: "Error al aplicar el tema",
      redirectingToLogin: "Redirigiendo al login...",
      systemActive: "Sistema Activo",
      aiPowered: "AI Powered",
      documentation: "Documentación",
      support: "Soporte",
      status: "Estado",
      welcomeMessage: "Bienvenido, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Gestión de Modelos",
      subtitle: "Visión general del sistema de gestión de modelos de IA",
      totalModels: "Total de Modelos",
      activeModels: "Modelos Activos",
      totalProviders: "Total de Proveedores",
      marketplaceModels: "Modelos del Marketplace",
      recentDownloads: "Descargas Recientes",
      systemHealth: "Salud del Sistema",
      healthy: "Saludable",
      warning: "Advertencia",
      critical: "Crítico",
      approved: "Aprobado",
      pending: "Pendiente",
      rejected: "Rechazado",
      addModel: "Añadir Modelo",
      viewModels: "Ver Modelos",
      manageProviders: "Gestionar Proveedores",
      marketplace: "Marketplace",
      recentActivity: "Actividad Reciente",
      topProviders: "Mejores Proveedores",
      trendingModels: "Modelos en Tendencia",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% este mes",
        activeModels: "Active Models",
        activeModelsChange: "+8% este mes",
        providers: "Providers",
        providersChange: "+3 este mes",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% esta semana",
      },
      systemStatus: {
        title: "Estado del Sistema",
        generalStatus: "Estado General",
        healthy: "Saludable",
        warning: "Advertencia",
        apiModels: "API de Modelos",
        marketplace: "Marketplace",
        providers: "Proveedores",
        operational: "Operativo",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registro, versionado y gestión del ciclo de vida completo de modelos de IA",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integración con múltiples proveedores de servicios de IA y gestión de tokens",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Acceso a +3,000,000 modelos de IA de múltiples marketplaces y proveedores",
        },
        // Página de Models
        models: {
          title: "Model Management",
          subtitle: "Manage all AI models registered in the system",
          actions: {
            newModel: "New Model",
            export: "Export",
            import: "Import",
          },
          stats: {
            totalModels: "Total Models",
            approved: "Approved",
            training: "Training",
            marketplace: "Marketplace",
          },
          tabs: {
            all: "All",
            approved: "Approved",
            draft: "Drafts",
            training: "Training",
            error: "Errors",
            marketplace: "Marketplace",
          },
          filters: {
            searchPlaceholder: "Search models...",
            typePlaceholder: "Model Type",
            statusPlaceholder: "Status",
            providerPlaceholder: "Provider",
            allTypes: "All types",
            allStatuses: "All statuses",
            allProviders: "All providers",
          },
          noModels: "No models registered in the system",
          noModelsFound: "No models found with applied filters",
        },
      },
      // Página de Models
      models: {
        title: "Gestión de Modelos",
        subtitle:
          "Administra todos los modelos de IA registrados en el sistema",
        actions: {
          newModel: "Nuevo Modelo",
          export: "Exportar",
          import: "Importar",
        },
        stats: {
          totalModels: "Total Models",
          approved: "Aprobados",
          training: "Entrenando",
          marketplace: "Marketplace",
        },
        tabs: {
          all: "Todos",
          approved: "Aprobados",
          draft: "Borradores",
          training: "Entrenando",
          error: "Errores",
          marketplace: "Marketplace",
        },
        filters: {
          searchPlaceholder: "Buscar modelos...",
          typePlaceholder: "Tipo de Modelo",
          statusPlaceholder: "Estado",
          providerPlaceholder: "Proveedor",
          allTypes: "Todos los tipos",
          allStatuses: "Todos los estados",
          allProviders: "Todos los proveedores",
        },
        noModels: "No hay modelos registrados en el sistema",
        noModelsFound: "No se encontraron modelos con los filtros aplicados",
      },
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Dashboard de Ingesta de Dominios",
      subtitle: "Monitorea y gestiona tus dominios de datos especializados",
      stats: {
        totalDomains: "Total Dominios",
        totalDocuments: "Total Documentos",
        webPages: "Páginas Web",
        apis: "APIs Conectadas",
        activeDomains: "dominios activos",
        documentsChange: "+12% este mes",
        webChange: "+8% esta semana",
        apisChange: "+3 este mes",
      },
      actions: {
        wizard: "Asistente de Dominios",
        manage: "Gestionar Dominios",
        access: "Acceder",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extrae datos de sitios web y monitoriza cambios",
        },
        uploadDocuments: {
          title: "Subir Documentos",
          description: "Sube y procesa documentos para enriquecer dominios",
        },
        monitor: {
          title: "Monitorización",
          description: "Monitorea el rendimiento y estado de los trabajos",
        },
        settings: {
          title: "Configuración",
          description: "Configura parámetros globales y por dominio",
        },
        configureSources: {
          title: "Configurar Fuentes",
          description: "Configura fuentes de datos y APIs",
        },
      },
      recentJobs: {
        title: "Trabajos Recientes",
      },
      jobStatus: {
        completed: "Completado",
        running: "Ejecutando",
        failed: "Fallido",
        pending: "Pendiente",
        queued: "En Cola",
        processing: "Procesando",
        cancelled: "Cancelado",
        retrying: "Reintentando",
      },
      wizard: {
        steps: {
          basicInfo: "Información Básica",
          dataSources: "Fuentes de Datos",
          compliance: "Cumplimiento",
          advancedFeatures: "Características Avanzadas",
          review: "Revisión",
        },
        templates: {
          title: "Plantillas de Dominio",
          description:
            "Selecciona una plantilla predefinida o crea una personalizada",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Personalizado",
        },
        industries: {
          title: "Industria",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Gobierno",
          manufacturing: "Manufactura",
          education: "Educación",
          legal: "Legal",
          other: "Otro",
        },
        compliance: {
          title: "Marcos de Cumplimiento",
          description: "Selecciona los marcos de cumplimiento aplicables",
          gdpr: "GDPR (Reglamento General de Protección de Datos)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act España (Ley de Inteligencia Artificial)",
          iaActEuropa: "IA Act Europa (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Gestión de Seguridad de la Información)",
          iso27701: "ISO 27701 (Privacidad de la Información)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Estándares ISO de Desarrollo de Software
          iso12207: "ISO 12207 (Ciclo de Vida del Software)",
          iso15504: "ISO 15504 (Evaluación de Procesos de Software)",
          iso20000: "ISO 20000 (Gestión de Servicios TI)",
          iso27002: "ISO 27002 (Controles de Seguridad de la Información)",
          iso27005: "ISO 27005 (Gestión de Riesgos de Seguridad)",
          iso27017: "ISO 27017 (Seguridad en la Nube)",
          iso27018: "ISO 27018 (Protección de Datos en la Nube)",
          iso9001: "ISO 9001 (Sistema de Gestión de Calidad)",
          iso14001: "ISO 14001 (Sistema de Gestión Ambiental)",
          // Normativas Europeas Adicionales
          eidas: "eIDAS (Identificación Electrónica UE)",
          psd2: "PSD2 (Directiva de Servicios de Pago UE)",
          nis2: "NIS2 (Directiva de Seguridad de Redes UE)",
          // Estándares de Calidad de Software
          cmmi: "CMMI (Modelo de Madurez de Capacidades)",
          tickit: "TICKIT (Calidad en TI y Telecomunicaciones)",
          spice: "SPICE (Evaluación de Procesos de Software)",
          custom: "Personalizado",
        },
        dataSources: {
          title: "Tipos de Fuentes de Datos",
          description:
            "Selecciona los tipos de fuentes de datos que utilizarás",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Nombre del Dominio",
          description: "Descripción",
          industry: "Industria",
          owner: "Propietario",
          team: "Equipo",
        },
        advancedFeatures: {
          title: "Características Avanzadas",
          rag: "RAG (Crear datasets)",
          training: "Entrenamiento de Modelos",
          compliance: "Cumplimiento Automático",
          monitoring: "Monitorización Avanzada",
          modelSelection: "Selección de Modelo",
          modelName: "Nombre del Modelo Interno",
          adapter: "Crear Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Modelo Base",
          customModel: "Modelo Personalizado",
        },
        review: {
          title: "Revisión Final",
          description: "Revisa la configuración antes de crear el dominio",
          template: "Plantilla seleccionada",
          industry: "Industria",
          compliance: "Marcos de cumplimiento",
          dataSources: "Fuentes de datos",
          features: "Características habilitadas",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Dominio especializado en análisis de datos financieros y cumplimiento regulatorio",
            features: [
              "RAG habilitado",
              "Cumplimiento GDPR/SOX",
              "Monitorización avanzada",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Dominio para análisis de datos médicos con cumplimiento HIPAA",
            features: [
              "RAG habilitado",
              "Cumplimiento HIPAA",
              "Encriptación avanzada",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Dominio para análisis de comportamiento de compra y recomendaciones",
            features: [
              "RAG habilitado",
              "Análisis de comportamiento",
              "Recomendaciones",
            ],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Dominio para cumplimiento con la IA Act española y regulaciones gubernamentales",
            features: [
              "RAG habilitado",
              "Cumplimiento IA Act",
              "Auditoría completa",
            ],
            compliance: ["IA Act España", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Dominio para análisis de datos IoT en entornos industriales",
            features: [
              "RAG habilitado",
              "Análisis IoT",
              "Monitorización en tiempo real",
            ],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Siguiente",
          previous: "Anterior",
          create: "Crear Dominio",
          cancel: "Cancelar",
        },
      },
      manage: {
        title: "Manage Domains",
        subtitle: "View and manage all your data domains",
        stats: {
          total: "Total",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          inactive: "Inactive",
          processing: "Processing",
          more: "More filters",
        },
        actions: {
          newDomain: "New Domain",
          viewDetails: "View Details",
          edit: "Edit",
          delete: "Delete",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          dataSources: "Data Sources",
          documents: "Documents",
          compliance: "Compliance",
          lastActivity: "Last Activity",
          status: "Status",
          industry: "Industry",
          owner: "Owner",
          createdAt: "Created",
          totalDocuments: "Total Documents",
          processedDocuments: "Processed Documents",
          dataSourcesCount: "Data Sources",
          complianceScore: "Compliance Score",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configure and monitor web data extraction",
        stats: {
          total: "Total",
          active: "Active",
          running: "Running",
          completed: "Completed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          active: "Active",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newJob: "New Job",
          viewDetails: "View Details",
          start: "Start",
          stop: "Stop",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or create a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          metrics: "Metrics",
          schedule: "Schedule",
          lastRun: "Last Run",
          nextRun: "Next Run",
          pagesScraped: "Pages Scraped",
          totalPages: "Total Pages",
          dataExtracted: "Data Extracted",
          errors: "Errors",
          priority: "Priority",
        },
        newJob: {
          title: "New Web Scraping Job",
          name: "Job Name",
          url: "Target URL",
          domain: "Domain",
          schedule: "Schedule",
          priority: "Priority",
          create: "Create Job",
          cancel: "Cancel",
        },
      },
      uploadDocuments: {
        title: "Upload Documents",
        subtitle: "Upload and manage document batches",
        stats: {
          total: "Total",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search batches...",
        },
        filters: {
          all: "All",
          processing: "Processing",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          newBatch: "New Batch",
          viewDetails: "View Details",
          pause: "Pause",
          resume: "Resume",
          configure: "Configure",
        },
        noResults: {
          title: "No batches found",
          description: "Try adjusting the filters or create a new batch",
        },
        details: {
          title: "Batch Details",
          basicInfo: "Basic Information",
          progress: "Progress",
          files: "Files",
          processing: "Processing",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          status: "Status",
          totalFiles: "Total Files",
          processedFiles: "Processed Files",
          totalSize: "Total Size",
          processedSize: "Processed Size",
          createdAt: "Created",
          updatedAt: "Updated",
          processingType: "Processing Type",
          ragEnabled: "RAG Enabled",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          errors: "Errors",
          warnings: "Warnings",
        },
        newBatch: {
          title: "New Document Batch",
          explanation:
            "A batch is a collection of documents that will be processed together. This allows for better organization and tracking of document processing.",
          name: "Batch Name",
          domain: "Domain",
          sector: "Sector",
          priority: "Priority",
          processingType: "Processing Type",
          create: "Create Batch",
          cancel: "Cancel",
        },
      },
      monitor: {
        title: "Monitor",
        subtitle: "Real-time monitoring of ingestion jobs",
        stats: {
          total: "Total",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
        },
        search: {
          placeholder: "Search jobs...",
        },
        filters: {
          all: "All",
          running: "Running",
          completed: "Completed",
          failed: "Failed",
          more: "More filters",
        },
        actions: {
          viewDetails: "View Details",
          configure: "Configure",
        },
        noResults: {
          title: "No jobs found",
          description: "Try adjusting the filters or start a new job",
        },
        details: {
          title: "Job Details",
          basicInfo: "Basic Information",
          performance: "Performance",
          metrics: "Metrics",
          alerts: "Alerts",
          name: "Job Name",
          domain: "Domain",
          type: "Type",
          status: "Status",
          startTime: "Start Time",
          endTime: "End Time",
          duration: "Duration",
          cpu: "CPU",
          memory: "Memory",
          throughput: "Throughput",
          latency: "Latency",
          alerts: "Alerts",
        },
      },
      settings: {
        title: "Settings",
        subtitle: "Configure global and domain-specific settings",
        stats: {
          total: "Total",
          active: "Active",
          configured: "Configured",
          default: "Default",
        },
        search: {
          placeholder: "Search domains...",
        },
        filters: {
          all: "All",
          active: "Active",
          configured: "Configured",
          default: "Default",
          more: "More filters",
        },
        tabs: {
          domains: "Domains",
          global: "Global",
          advanced: "Advanced",
        },
        actions: {
          viewDetails: "View Details",
          save: "Save",
          reset: "Reset",
        },
        noResults: {
          title: "No domains found",
          description: "Try adjusting the filters or create a new domain",
        },
        details: {
          title: "Domain Settings",
          basicInfo: "Basic Information",
          configuration: "Configuration",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) is used to process documents and create training datasets. It performs automatic chunking and tagging of content for AI model training. Domains are business models, not RAG systems.",
          ragEnabled: "RAG Enabled for Datasets",
          trainingEnabled: "Training Enabled",
          complianceEnabled: "Compliance Enabled",
          autoIngestion: "Auto Ingestion",
          qualityThreshold: "Quality Threshold",
          retentionDays: "Retention Days",
          lastUpdated: "Last Updated",
          status: "Status",
        },
        global: {
          general: "General Configuration",
          security: "Security",
          autoBackup: "Auto Backup",
          notifications: "Notifications",
          language: "Language",
          timezone: "Timezone",
          encryption: "Encryption",
          authentication: "Authentication",
          auditLogs: "Audit Logs",
        },
        advanced: {
          performance: "Performance",
          logs: "Logs & Monitoring",
          cacheSize: "Cache Size",
          maxConnections: "Max Connections",
          logLevel: "Log Level",
          monitoring: "Monitoring",
          alerts: "Alerts",
        },
      },
      configureSources: {
        title: "Configure Sources",
        subtitle: "Manage and configure data sources for each domain",
        stats: {
          total: "Total",
          active: "Active",
          api: "APIs",
        },
        search: {
          placeholder: "Search sources...",
        },
        filters: {
          allTypes: "All types",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
          more: "More filters",
        },
        actions: {
          newSource: "New Source",
          viewDetails: "View Details",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
        noResults: {
          title: "No sources found",
          description: "Try adjusting the filters or create a new data source",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          error: "Error",
          maintenance: "Maintenance",
          unknown: "Unknown",
        },
        info: {
          type: "Type:",
          schedule: "Schedule:",
          dataVolume: "Volume:",
          syncStatus: "Sync:",
        },
        features: {
          compliance: "Compliance:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "New Data Source",
          name: "Source Name",
          namePlaceholder: "e.g., API Gateway",
          domain: "Domain",
          selectDomain: "Select domain",
          type: "Source Type",
          selectType: "Select type",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Credentials Type",
          credentialsNone: "None",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Schedule",
          scheduleRealTime: "Real-time",
          schedule15Min: "Every 15 minutes",
          scheduleHour: "Every hour",
          scheduleDaily: "Daily",
          scheduleWeekly: "Weekly",
          processingOptions: "Processing Options",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process data to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          create: "Create Source",
          cancel: "Cancel",
        },
        details: {
          basicInfo: "Basic Information",
          sync: "Synchronization",
          credentials: "Credentials",
          compliance: "Compliance",
          processing: "Processing",
          domain: "Domain:",
          type: "Type:",
          status: "Status:",
          url: "URL:",
          schedule: "Schedule:",
          lastSync: "Last sync:",
          nextSync: "Next sync:",
          dataVolume: "Data volume:",
          credentialType: "Type:",
          lastUpdated: "Last updated:",
          rag: "RAG (Datasets)",
          training: "Training",
          edit: "Edit",
          test: "Test Connection",
          configure: "Configure",
        },
      },
      domainDetails: {
        navigation: {
          back: "Back",
        },
        actions: {
          edit: "Edit",
          settings: "Settings",
          monitor: "Monitor",
        },
        stats: {
          documents: "Documents",
          processed: "Processed",
          dataSources: "Data Sources",
          active: "active",
          compliance: "Compliance",
          compliant: "Compliant",
          successRate: "Success Rate",
          avgTime: "average",
        },
        tabs: {
          overview: "Overview",
          dataSources: "Sources",
          documents: "Documents",
          compliance: "Compliance",
          alerts: "Alerts",
        },
        overview: {
          domainInfo: "Domain Information",
          performance: "Performance",
          ingestionMetrics: "Ingestion Metrics",
          industry: "Industry",
          status: "Status",
          owner: "Owner",
          created: "Created",
          team: "Team",
          dataQuality: "Data Quality",
          rag: "RAG",
          training: "Training",
          lastActivity: "Last Activity",
          daily: "Daily",
          weekly: "Weekly",
          monthly: "Monthly",
        },
        dataSources: {
          title: "Data Sources",
          lastSync: "Last sync:",
        },
        documents: {
          title: "Documents",
        },
        compliance: {
          frameworks: "Compliance Frameworks",
          audits: "Audits",
          status: "Status:",
          lastAudit: "Last Audit",
          nextAudit: "Next Audit",
        },
        alerts: {
          title: "Alerts",
          resolved: "Resolved",
        },
        status: {
          active: "Active",
          inactive: "Inactive",
          maintenance: "Maintenance",
          error: "Error",
          unknown: "Unknown",
        },
        edit: {
          title: "Edit Domain",
          name: "Domain Name",
          industry: "Industry",
          description: "Description",
          save: "Save Changes",
          cancel: "Cancel",
        },
        settings: {
          title: "Domain Settings",
          processing: "Processing",
          ragEnabled: "Enable RAG (Create datasets)",
          ragDescription: "Process documents to create training datasets",
          trainingEnabled: "Enable Training",
          trainingDescription: "Generate content for model training",
          save: "Save Settings",
          cancel: "Cancel",
        },
      },
      manage: {
        title: "Gestionar Dominios",
        subtitle: "Visualiza y gestiona todos tus dominios de datos",
        stats: {
          total: "Total",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          inactive: "Inactivos",
          processing: "Procesando",
          more: "Más filtros",
        },
        actions: {
          newDomain: "Nuevo Dominio",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          delete: "Eliminar",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Detalles del Dominio",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          dataSources: "Fuentes de Datos",
          documents: "Documentos",
          compliance: "Cumplimiento",
          lastActivity: "Última Actividad",
          status: "Estado",
          industry: "Industria",
          owner: "Propietario",
          createdAt: "Creado",
          totalDocuments: "Total Documentos",
          processedDocuments: "Documentos Procesados",
          dataSourcesCount: "Fuentes de Datos",
          complianceScore: "Puntuación de Cumplimiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
        },
      },
      webScraping: {
        title: "Web Scraping",
        subtitle: "Configura y monitoriza la extracción de datos web",
        stats: {
          total: "Total",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newJob: "Nuevo Trabajo",
          viewDetails: "Ver Detalle",
          start: "Iniciar",
          stop: "Detener",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o crear un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          progress: "Progreso",
          metrics: "Métricas",
          schedule: "Programación",
          lastRun: "Última Ejecución",
          nextRun: "Próxima Ejecución",
          pagesScraped: "Páginas Extraídas",
          totalPages: "Total de Páginas",
          dataExtracted: "Datos Extraídos",
          errors: "Errores",
          priority: "Prioridad",
        },
        newJob: {
          title: "Nuevo Trabajo de Web Scraping",
          name: "Nombre del Trabajo",
          url: "URL Objetivo",
          domain: "Dominio",
          schedule: "Programación",
          priority: "Prioridad",
          create: "Crear Trabajo",
          cancel: "Cancelar",
        },
        // Nuevas traducciones para página independiente
        newJobPage: "Nuevo Trabajo de Web Scraping",
        newJobDescription:
          "Configura un nuevo trabajo de extracción de datos web con asignación múltiple",
        basicConfig: "Configuración Básica",
        basicConfigDescription: "Información general del trabajo de scraping",
        jobName: "Nombre del Trabajo",
        jobNamePlaceholder: "Ej: Extracción de productos",
        description: "Descripción",
        descriptionPlaceholder: "Describe qué datos se extraerán y para qué",
        targetUrl: "URL Objetivo",
        advancedConfig: "Configuración Avanzada",
        advancedConfigDescription: "Parámetros técnicos de extracción",
        maxPages: "Máximo de Páginas",
        maxDepth: "Profundidad Máxima",
        waitTime: "Tiempo de Espera",
        userAgent: "User Agent",
        followLinks: "Seguir Enlaces",
        respectRobots: "Respetar robots.txt",
        domainAssignment: "Asignación de Dominios",
        domainAssignmentDescription:
          "Selecciona los dominios que procesarán estos datos",
        selectDomains: "Seleccionar Dominios",
        selectDomainPlaceholder: "Elige un dominio...",
        selectedDomains: "Dominios Seleccionados",
        ragAssignment: "Asignación de Proyectos RAG",
        ragAssignmentDescription:
          "Selecciona los proyectos RAG que utilizarán estos datos",
        selectRAGProjects: "Seleccionar Proyectos RAG",
        selectRAGProjectPlaceholder: "Elige un proyecto RAG...",
        selectedRAGProjects: "Proyectos RAG Seleccionados",
        priorities: {
          low: "Baja",
          medium: "Media",
          high: "Alta",
        },
        schedules: {
          once: "Una vez",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
      },
      uploadDocuments: {
        title: "Subir Documentos",
        subtitle: "Sube y gestiona lotes de documentos",
        stats: {
          total: "Total",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar lotes...",
        },
        filters: {
          all: "Todos",
          processing: "Procesando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          newBatch: "Nuevo Lote",
          viewDetails: "Ver Detalle",
          pause: "Pausar",
          resume: "Reanudar",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron lotes",
          description: "Intenta ajustar los filtros o crear un nuevo lote",
        },
        details: {
          title: "Detalles del Lote",
          basicInfo: "Información Básica",
          progress: "Progreso",
          files: "Archivos",
          processing: "Procesamiento",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          status: "Estado",
          totalFiles: "Total de Archivos",
          processedFiles: "Archivos Procesados",
          totalSize: "Tamaño Total",
          processedSize: "Tamaño Procesado",
          createdAt: "Creado",
          updatedAt: "Actualizado",
          processingType: "Tipo de Procesamiento",
          ragEnabled: "RAG Habilitado",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          errors: "Errores",
          warnings: "Advertencias",
        },
        newBatch: {
          title: "Nuevo Lote de Documentos",
          explanation:
            "Un lote es una colección de documentos que se procesarán juntos. Esto permite una mejor organización y seguimiento del procesamiento de documentos.",
          name: "Nombre del Lote",
          domain: "Dominio",
          sector: "Sector",
          priority: "Prioridad",
          processingType: "Tipo de Procesamiento",
          create: "Crear Lote",
          cancel: "Cancelar",
        },
      },
      monitor: {
        title: "Monitorización",
        subtitle: "Monitorización en tiempo real de trabajos de ingesta",
        stats: {
          total: "Total",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
        },
        search: {
          placeholder: "Buscar trabajos...",
        },
        filters: {
          all: "Todos",
          running: "Ejecutando",
          completed: "Completados",
          failed: "Fallidos",
          more: "Más filtros",
        },
        actions: {
          viewDetails: "Ver Detalle",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron trabajos",
          description: "Intenta ajustar los filtros o iniciar un nuevo trabajo",
        },
        details: {
          title: "Detalles del Trabajo",
          basicInfo: "Información Básica",
          performance: "Rendimiento",
          metrics: "Métricas",
          alerts: "Alertas",
          name: "Nombre del Trabajo",
          domain: "Dominio",
          type: "Tipo",
          status: "Estado",
          startTime: "Hora de Inicio",
          endTime: "Hora de Fin",
          duration: "Duración",
          cpu: "CPU",
          memory: "Memoria",
          throughput: "Rendimiento",
          latency: "Latencia",
          alerts: "Alertas",
        },
      },
      settings: {
        title: "Configuración",
        subtitle: "Configura parámetros globales y por dominio",
        stats: {
          total: "Total",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
        },
        search: {
          placeholder: "Buscar dominios...",
        },
        filters: {
          all: "Todos",
          active: "Activos",
          configured: "Configurados",
          default: "Por Defecto",
          more: "Más filtros",
        },
        tabs: {
          domains: "Dominios",
          global: "Global",
          advanced: "Avanzado",
        },
        actions: {
          viewDetails: "Ver Detalle",
          save: "Guardar",
          reset: "Restablecer",
        },
        noResults: {
          title: "No se encontraron dominios",
          description: "Intenta ajustar los filtros o crear un nuevo dominio",
        },
        details: {
          title: "Configuración del Dominio",
          basicInfo: "Información Básica",
          configuration: "Configuración",
          ragExplanation:
            "RAG (Retrieval-Augmented Generation) se utiliza para procesar documentos y crear datasets de entrenamiento. Realiza chunking y tagging automático del contenido para el entrenamiento de modelos de IA. Los dominios son modelos de negocio, no sistemas RAG.",
          ragEnabled: "RAG Habilitado para Datasets",
          trainingEnabled: "Entrenamiento Habilitado",
          complianceEnabled: "Cumplimiento Habilitado",
          autoIngestion: "Ingesta Automática",
          qualityThreshold: "Umbral de Calidad",
          retentionDays: "Días de Retención",
          lastUpdated: "Última Actualización",
          status: "Estado",
        },
        global: {
          general: "Configuración General",
          security: "Seguridad",
          autoBackup: "Respaldo Automático",
          notifications: "Notificaciones",
          language: "Idioma",
          timezone: "Zona Horaria",
          encryption: "Encriptación",
          authentication: "Autenticación",
          auditLogs: "Logs de Auditoría",
        },
        advanced: {
          performance: "Rendimiento",
          logs: "Logs y Monitorización",
          cacheSize: "Tamaño de Caché",
          maxConnections: "Conexiones Máximas",
          logLevel: "Nivel de Log",
          monitoring: "Monitorización",
          alerts: "Alertas",
        },
      },
      configureSources: {
        title: "Configurar Fuentes",
        subtitle: "Gestiona y configura las fuentes de datos para cada dominio",
        stats: {
          total: "Total",
          active: "Activas",
          api: "APIs",
        },
        search: {
          placeholder: "Buscar fuentes...",
        },
        filters: {
          allTypes: "Todos los tipos",
          api: "API",
          database: "Base de Datos",
          file: "Archivo",
          web: "Web",
          stream: "Stream",
          more: "Más filtros",
        },
        actions: {
          newSource: "Nueva Fuente",
          viewDetails: "Ver Detalle",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
        noResults: {
          title: "No se encontraron fuentes",
          description:
            "Intenta ajustar los filtros o crear una nueva fuente de datos",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          error: "Error",
          maintenance: "Mantenimiento",
          unknown: "Desconocido",
        },
        info: {
          type: "Tipo:",
          schedule: "Programación:",
          dataVolume: "Volumen:",
          syncStatus: "Sincronización:",
        },
        features: {
          compliance: "Cumplimiento:",
          rag: "RAG",
          training: "Training",
        },
        newSource: {
          title: "Nueva Fuente de Datos",
          name: "Nombre de la Fuente",
          namePlaceholder: "Ej: API Gateway",
          domain: "Dominio",
          selectDomain: "Seleccionar dominio",
          type: "Tipo de Fuente",
          selectType: "Seleccionar tipo",
          url: "URL/Endpoint",
          urlPlaceholder: "https://api.example.com/v1",
          credentials: "Tipo de Credenciales",
          credentialsNone: "Ninguna",
          credentialsApiKey: "API Key",
          credentialsOAuth: "OAuth",
          credentialsBasic: "Basic Auth",
          schedule: "Programación",
          scheduleRealTime: "Tiempo Real",
          schedule15Min: "Cada 15 minutos",
          scheduleHour: "Cada hora",
          scheduleDaily: "Diario",
          scheduleWeekly: "Semanal",
          processingOptions: "Opciones de Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription: "Procesar datos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          create: "Crear Fuente",
          cancel: "Cancelar",
        },
        details: {
          basicInfo: "Información Básica",
          sync: "Sincronización",
          credentials: "Credenciales",
          compliance: "Cumplimiento",
          processing: "Procesamiento",
          domain: "Dominio:",
          type: "Tipo:",
          status: "Estado:",
          url: "URL:",
          schedule: "Programación:",
          lastSync: "Última sincronización:",
          nextSync: "Próxima sincronización:",
          dataVolume: "Volumen de datos:",
          credentialType: "Tipo:",
          lastUpdated: "Última actualización:",
          rag: "RAG (Datasets)",
          training: "Entrenamiento",
          edit: "Editar",
          test: "Probar Conexión",
          configure: "Configurar",
        },
      },
      domainDetails: {
        navigation: {
          back: "Volver",
        },
        actions: {
          edit: "Editar",
          settings: "Configuración",
          monitor: "Monitorear",
        },
        stats: {
          documents: "Documentos",
          processed: "Procesados",
          dataSources: "Fuentes de Datos",
          active: "activas",
          compliance: "Cumplimiento",
          compliant: "Cumple",
          successRate: "Tasa de Éxito",
          avgTime: "promedio",
        },
        tabs: {
          overview: "Resumen",
          dataSources: "Fuentes",
          documents: "Documentos",
          compliance: "Cumplimiento",
          alerts: "Alertas",
        },
        overview: {
          domainInfo: "Información del Dominio",
          performance: "Rendimiento",
          ingestionMetrics: "Métricas de Ingesta",
          industry: "Industria",
          status: "Estado",
          owner: "Propietario",
          created: "Creado",
          team: "Equipo",
          dataQuality: "Calidad de Datos",
          rag: "RAG",
          training: "Training",
          lastActivity: "Última Actividad",
          daily: "Diario",
          weekly: "Semanal",
          monthly: "Mensual",
        },
        dataSources: {
          title: "Fuentes de Datos",
          lastSync: "Última sincronización:",
        },
        documents: {
          title: "Documentos",
        },
        compliance: {
          frameworks: "Marcos de Cumplimiento",
          audits: "Auditorías",
          status: "Estado:",
          lastAudit: "Última Auditoría",
          nextAudit: "Próxima Auditoría",
        },
        alerts: {
          title: "Alertas",
          resolved: "Resuelto",
        },
        status: {
          active: "Activo",
          inactive: "Inactivo",
          maintenance: "Mantenimiento",
          error: "Error",
          unknown: "Desconocido",
        },
        edit: {
          title: "Editar Dominio",
          name: "Nombre del Dominio",
          industry: "Industria",
          description: "Descripción",
          save: "Guardar Cambios",
          cancel: "Cancelar",
        },
        settings: {
          title: "Configuración del Dominio",
          processing: "Procesamiento",
          ragEnabled: "Habilitar RAG (Crear datasets)",
          ragDescription:
            "Procesar documentos para crear datasets de entrenamiento",
          trainingEnabled: "Habilitar Training",
          trainingDescription:
            "Generar contenido para entrenamiento de modelos",
          save: "Guardar Configuración",
          cancel: "Cancelar",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Evaluación de Modelos",
      subtitle: "Dashboard de evaluación y testing de modelos de IA",
      completed: "Completado",
      running: "Ejecutando",
      failed: "Fallido",
      pending: "Pendiente",
      unknown: "Desconocido",
      loadingData: "Cargando datos del dashboard...",
      errorLoadingData: "No se pudieron cargar los datos del dashboard",
      pipeline: "Pipeline",
      abTest: "Test A/B",
      security: "Seguridad",
      evaluation: "Evaluación",
      newEvaluation: "Nueva Evaluación",
      viewEvaluation: "Ver Evaluación",
      pipelineModal: "Modal de Pipeline",
      abTestModal: "Modal de Test A/B",
      securityModal: "Modal de Seguridad",
      reportModal: "Modal de Reporte",
    },

    // RAG Training
    ragTraining: {
      title: "Entrenamiento RAG",
      subtitle: "Dashboard de entrenamiento y métricas RAG",
      totalDocuments: "Total de Documentos",
      totalChunks: "Total de Chunks",
      totalSearches: "Total de Búsquedas",
      averageResponseTime: "Tiempo de Respuesta Promedio",
      activeUsers: "Usuarios Activos",
      storageUsed: "Almacenamiento Usado",
      documentsByType: "Documentos por Tipo",
      searchesByDay: "Búsquedas por Día",
      topQueries: "Consultas Principales",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "configuración de base de datos",
      apiEndpoints: "API endpoints",
      authentication: "autenticación",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Data Sources Module
    dataSources: {
      title: "Fuentes de Información",
      subtitle: "Gestión centralizada de fuentes de datos",
      totalDataSources: "Total Fuentes",
      activeSources: "Fuentes Activas",
      totalChunks: "Total Chunks",
      processingJobs: "Jobs Procesando",
      successRate: "Tasa de Éxito",
      vsLastMonth: "vs mes anterior",
      dataSourceTypes: "Tipos de Fuentes",
      sources: "fuentes",
      recentActivity: "Actividad Reciente",
      processingStats: "Estadísticas de Procesamiento",
      completedJobs: "Jobs Completados",
      pendingJobs: "Jobs Pendientes",
      errorJobs: "Jobs con Error",
      avgProcessingTime: "Tiempo Promedio",
      timeAgo: {
        now: "Ahora",
      },
      help: {
        dashboard: {
          title: "Dashboard de Fuentes de Información",
          content: `
            <p>Este dashboard centraliza la gestión de todas las fuentes de datos del sistema.</p>
            <h3>Funcionalidades principales:</h3>
            <ul>
              <li><strong>Estadísticas en tiempo real:</strong> Monitorea el estado de todas las fuentes de datos</li>
              <li><strong>Tipos de fuentes:</strong> Bases de datos, APIs, documentos y web scraping</li>
              <li><strong>Métricas de procesamiento:</strong> Jobs completados, pendientes y con errores</li>
              <li><strong>Actividad reciente:</strong> Últimas operaciones realizadas</li>
            </ul>
            <p>Desde aquí puedes acceder a cada tipo de fuente de datos para configurar, monitorear y gestionar tus conexiones.</p>
          `,
        },
        database: {
          title: "Gestión de Bases de Datos",
          content: `
            <p>Gestiona las conexiones a bases de datos para extraer y procesar información.</p>
            <h3>Tipos soportados:</h3>
            <ul>
              <li><strong>PostgreSQL:</strong> Bases de datos relacionales</li>
              <li><strong>MySQL:</strong> Bases de datos relacionales</li>
              <li><strong>MongoDB:</strong> Bases de datos NoSQL</li>
              <li><strong>Elasticsearch:</strong> Motor de búsqueda y análisis</li>
              <li><strong>Vector DB:</strong> Bases de datos vectoriales para embeddings</li>
              <li><strong>Redis:</strong> Base de datos en memoria</li>
            </ul>
            <h3>Funcionalidades:</h3>
            <ul>
              <li>Configuración de conexiones seguras</li>
              <li>Prueba de conectividad</li>
              <li>Ejecución de consultas SQL</li>
              <li>Asignación a dominios y proyectos RAG</li>
            </ul>
          `,
        },
        api: {
          title: "Gestión de APIs",
          content: `
            <p>Configura y gestiona conexiones a APIs externas para extraer datos.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>OpenAPI/Swagger:</strong> Soporte completo para especificaciones OpenAPI</li>
              <li><strong>Autenticación:</strong> API Key, Basic Auth, Bearer Token</li>
              <li><strong>Configuración avanzada:</strong> Timeouts, reintentos, rate limiting</li>
              <li><strong>Testing:</strong> Prueba de endpoints antes de guardar</li>
            </ul>
            <h3>Flujo de trabajo:</h3>
            <ol>
              <li>Configura la URL del Swagger/OpenAPI</li>
              <li>Establece la autenticación necesaria</li>
              <li>Prueba la conexión</li>
              <li>Asigna a dominios y proyectos RAG</li>
            </ol>
          `,
        },
        documents: {
          title: "Gestión de Documentos",
          content: `
            <p>Gestiona lotes de documentos para procesamiento y análisis.</p>
            <h3>Tipos de documentos soportados:</h3>
            <ul>
              <li><strong>PDF:</strong> Documentos PDF</li>
              <li><strong>DOCX:</strong> Documentos de Word</li>
              <li><strong>TXT:</strong> Archivos de texto plano</li>
              <li><strong>CSV:</strong> Datos tabulares</li>
              <li><strong>JSON:</strong> Datos estructurados</li>
            </ul>
            <h3>Procesamiento:</h3>
            <ul>
              <li>Chunking automático de documentos</li>
              <li>Extracción de metadatos</li>
              <li>Generación de embeddings</li>
              <li>Asignación a proyectos RAG</li>
            </ul>
          `,
        },
        webScraping: {
          title: "Web Scraping",
          content: `
            <p>Configura trabajos de extracción de datos de sitios web.</p>
            <h3>Características:</h3>
            <ul>
              <li><strong>Configuración flexible:</strong> URLs, selectores CSS, XPath</li>
              <li><strong>Programación:</strong> Ejecución manual o programada</li>
              <li><strong>Filtros avanzados:</strong> Por contenido, fecha, tipo</li>
              <li><strong>Monitoreo:</strong> Estado de ejecución y logs</li>
            </ul>
            <h3>Tipos de extracción:</h3>
            <ul>
              <li>Contenido de páginas web</li>
              <li>Datos de formularios</li>
              <li>Enlaces y referencias</li>
              <li>Metadatos y estructura</li>
            </ul>
          `,
        },
      },
    },
    // RAG Module
    rag: {
      dashboard: {
        title: "Dashboard RAG",
        subtitle: "Sistema de Retrieval Augmented Generation",
        totalProjects: "Total Proyectos",
        totalChunks: "Total Chunks",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        activeProjects: "Proyectos Activos",
        vsLastMonth: "vs mes pasado",
        projectsOverview: "Resumen de Proyectos",
        viewAll: "Ver Todos",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "búsquedas",
        models: "Modelos",
        recentActivity: "Actividad Reciente",
        modelUsage: "Uso de Modelos",
        usage: "Uso",
      },
      project: {
        createTitle: "Crear Proyecto RAG",
        createSubtitle: "Configurar un nuevo proyecto RAG",
        editTitle: "Editar Proyecto RAG",
        basicInfo: "Información Básica",
        name: "Nombre del Proyecto",
        namePlaceholder: "Ingresa el nombre del proyecto",
        description: "Descripción",
        descriptionPlaceholder: "Describe el propósito del proyecto RAG",
        isActive: "Proyecto Activo",
        embeddingModel: "Modelo de Embedding",
        modelConfiguration: "Configuración de Modelos",
        rerankerModel: "Modelo de Reranker",
        llmModel: "Modelo LLM",
        ragConfiguration: "Configuración RAG",
        maxChunkSize: "Tamaño Máximo de Chunk",
        chunkOverlap: "Solapamiento de Chunks",
        semanticChunking: "Chunking Semántico",
        statistics: "Estadísticas del Proyecto",
        documents: "Documentos",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Búsquedas",
        avgResponseTime: "Tiempo Promedio",
        successRate: "Tasa de Éxito",
        assignments: "Asignaciones",
        documentAssignments: "Documentos Asignados",
        webscrapingAssignments: "Web Scraping Asignado",
        datasourceAssignments: "Fuentes de Datos Asignadas",
        lekaServerConfig: "Configuración Leka Server",
        lekaEndpoint: "Endpoint Leka Server",
        lekaApiKey: "API Key Leka Server",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "Dashboard RAG",
          content:
            "El Dashboard RAG te permite monitorear el rendimiento de tus proyectos de Retrieval Augmented Generation, incluyendo estadísticas de uso, proyectos activos y actividad reciente.",
        },
        projects: {
          title: "Proyectos RAG",
          content:
            "<h4>¿Qué son los Proyectos RAG?</h4><p>Los proyectos RAG (Retrieval Augmented Generation) son sistemas inteligentes que combinan la recuperación de información con la generación de texto. Te permiten crear chatbots y asistentes que pueden responder preguntas basándose en tus propios documentos y datos.</p><h4>¿Para qué sirven?</h4><ul><li><strong>Chatbots Especializados:</strong> Crea asistentes que conocen tu documentación específica</li><li><strong>Búsqueda Inteligente:</strong> Encuentra información relevante en grandes volúmenes de documentos</li><li><strong>Respuestas Contextualizadas:</strong> Genera respuestas precisas basadas en tu contenido</li><li><strong>Automatización de Soporte:</strong> Reduce la carga de trabajo del equipo de soporte</li><li><strong>Base de Conocimiento Interactiva:</strong> Convierte documentos estáticos en una experiencia conversacional</li></ul><h4>Gestión de Proyectos</h4><p>Desde esta página puedes:</p><ul><li>Crear nuevos proyectos RAG con configuraciones personalizadas</li><li>Editar modelos de IA (embedding, reranker, LLM) para cada proyecto</li><li>Monitorear el rendimiento y estadísticas de uso</li><li>Gestionar las fuentes de datos asignadas a cada proyecto</li><li>Configurar parámetros de chunking y procesamiento</li></ul>",
        },
        createProject: {
          title: "Crear Proyecto RAG",
          content:
            "Crea un nuevo proyecto RAG configurando los modelos de embedding, reranker y LLM, así como los parámetros de chunking y configuración del servidor Leka.",
        },
        editProject: {
          title: "Editar Proyecto RAG",
          content:
            "Edita la configuración de tu proyecto RAG, incluyendo modelos, parámetros de chunking, configuración del servidor Leka y monitorea las estadísticas del proyecto.",
        },
        models: {
          title: "Modelos RAG",
          content:
            "Gestiona los modelos de IA disponibles para tus proyectos RAG, incluyendo modelos de embedding, reranker y LLM de diferentes proveedores.",
        },
        createModel: {
          title: "Crear Modelo RAG",
          content:
            "<h4>Configuración de Modelos de IA</h4><p>Esta página te permite crear y configurar nuevos modelos de IA para tus proyectos RAG. Puedes configurar:</p><ul><li><strong>Información Básica:</strong> Nombre, tipo (Embedding, Reranker, LLM), proveedor y versión</li><li><strong>Configuración Técnica:</strong> Endpoint, API Key, costo por token, máximo de tokens y ventana de contexto</li><li><strong>Opciones Avanzadas:</strong> Modelo local vs remoto, estado activo/inactivo</li></ul><p><strong>Tipos de Modelos:</strong></p><ul><li><strong>Embedding:</strong> Convierte texto en vectores numéricos para búsqueda semántica</li><li><strong>Reranker:</strong> Mejora la relevancia de los resultados de búsqueda</li><li><strong>LLM:</strong> Genera respuestas basadas en el contexto recuperado</li></ul><p>Una vez configurado, puedes probar el modelo antes de guardarlo para asegurar que funciona correctamente.</p>",
        },
        editModel: {
          title: "Editar Modelo RAG",
          content:
            "Edita la configuración de un modelo de IA existente, incluyendo parámetros de rendimiento, costo y configuración del endpoint.",
        },
        chat: {
          title: "Chat RAG",
          content:
            "Interactúa con tus proyectos RAG a través del chat. Selecciona un proyecto y haz preguntas para obtener respuestas basadas en tus documentos.",
        },
        reranker: {
          title: "Reranker RAG",
          content:
            "Configura y gestiona el reranking de resultados de búsqueda para mejorar la relevancia de las respuestas en tus proyectos RAG.",
        },
        chunks: {
          title: "Chunks de Documentos",
          content:
            "Los chunks son fragmentos de documentos procesados que permiten una búsqueda más eficiente. Los auto-tags se generan automáticamente basándose en el contenido, mientras que los tags manuales los puedes añadir, editar o eliminar para mejorar la categorización y recuperación de información específica.",
        },
        search: {
          title: "Búsqueda Semántica",
          content:
            "Realiza búsquedas semánticas avanzadas en tus documentos RAG con diferentes estrategias de búsqueda y filtros.",
        },
        searchTypes: {
          vector: "Vectorial",
          textual: "Textual",
          hybrid: "Híbrida",
          filtered: "Filtrada",
        },
        analytics: {
          title: "Analytics RAG",
          content:
            "Monitorea el rendimiento y métricas de tus proyectos RAG, incluyendo estadísticas de uso, tiempos de respuesta y calidad de resultados.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Mejora la relevancia de los resultados de búsqueda",
        configuration: "Configuración del Reranker",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        model: "Modelo de Reranking",
        selectModel: "Seleccionar modelo",
        topK: "Top K Results",
        threshold: "Umbral de Relevancia",
        testQuery: "Test de Consulta",
        query: "Consulta de Prueba",
        queryPlaceholder: "Ingresa una consulta para probar el reranking...",
        runTest: "Ejecutar Test",
        testHistory: "Historial de Tests",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualiza y gestiona los chunks de documentos",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        chunkType: "Tipo de Chunk",
        selectType: "Seleccionar tipo",
        searchChunks: "Buscar Chunks",
        searchPlaceholder: "Buscar por contenido o tags...",
        totalChunks: "Total Chunks",
        avgSize: "Tamaño Promedio",
        autoTags: "Auto Tags",
        viewDetails: "Ver Detalles",
        filters: "Filtros",
        manualTags: "Tags Manuales",
        newTag: "Nuevo tag...",
        addTag: "Añadir Tag",
        editTags: "Editar Tags",
        removeTag: "Eliminar Tag",
        source: "Fuente",
        relevance: "Relevancia",
        characters: "caracteres",
        createdAt: "Creado",
        saveChanges: "Guardar Cambios",
        cancel: "Cancelar",
      },
      search: {
        title: "Búsqueda Semántica",
        subtitle: "Búsqueda avanzada en documentos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        searchQuery: "Consulta de Búsqueda",
        queryPlaceholder: "Ingresa tu consulta de búsqueda...",
        searchType: "Tipo de Búsqueda",
        selectType: "Seleccionar tipo",
        executeSearch: "Ejecutar Búsqueda",
        searchHistory: "Historial de Búsquedas",
        results: "Resultados",
        relevance: "Relevancia",
        source: "Fuente",
      },
      analytics: {
        title: "Analytics",
        subtitle: "Métricas y rendimiento de proyectos RAG",
        project: "Proyecto RAG",
        selectProject: "Seleccionar proyecto",
        timeRange: "Rango de Tiempo",
        selectRange: "Seleccionar rango",
        refresh: "Actualizar",
        totalSearches: "Total Búsquedas",
        avgResponseTime: "Tiempo Respuesta Promedio",
        successRate: "Tasa de Éxito",
        userSatisfaction: "Satisfacción del Usuario",
        topQueries: "Consultas Más Populares",
        performanceMetrics: "Métricas de Rendimiento",
        usageStats: "Estadísticas de Uso",
      },
    },

    // Governance
    governance: {
      title: "Dashboard de Gobierno Automático",
      subtitle: "Monitorea y gestiona el gobierno de IA en tiempo real",
      refresh: "Actualizar",
      configure: "Configurar",
      low: "BAJO",
      medium: "MEDIO",
      high: "ALTO",
      critical: "CRÍTICO",
      riskLevel: "Nivel de Riesgo",
      compliance: "Cumplimiento",
      audit: "Auditoría",
      policies: "Políticas",
      violations: "Violaciones",
      alerts: "Alertas",
      metrics: "Métricas",
      reports: "Reportes",
      settings: "Configuración",
      overview: {
        title: "Vista General de Políticas",
        subtitle: "Gestión y configuración de políticas de gobierno",
        metrics: {
          total: "Total Políticas",
          active: "Políticas Activas",
          violations: "Violaciones",
          compliance: "Score Cumplimiento",
        },
        filters: {
          title: "Filtros",
          clear: "Limpiar",
          search: {
            placeholder: "Buscar políticas...",
          },
          type: "Todos los Tipos",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          status: "Todos los Estados",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          enforcement: "Todos los Niveles",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
        },
        table: {
          title: "Políticas",
          id: "ID",
          name: "Nombre",
          type: "Tipo",
          enforcement: "Nivel",
          version: "Versión",
          status: "Estado",
          violations: "Violaciones",
          score: "Score",
          actions: "Acciones",
          empty: "No se encontraron políticas",
        },
        button: {
          create: "Crear Política",
        },
        confirmDelete: "¿Eliminar esta política?",
      },
      detail: {
        "title.edit": "Editar Política",
        "title.create": "Nueva Política",
        button: {
          evaluate: "Evaluar",
          compliance: "Verificar Cumplimiento",
          score: "Calcular Score",
        },
        form: {
          title: "Información de la Política",
          name: "Nombre de la Política",
          version: "Versión",
          type: "Tipo",
          "type.security": "Seguridad",
          "type.compliance": "Cumplimiento",
          "type.ethics": "Ética",
          "type.privacy": "Privacidad",
          "type.operational": "Operacional",
          category: "Categoría",
          enforcement: "Nivel de Enforcement",
          "enforcement.mandatory": "Obligatorio",
          "enforcement.recommended": "Recomendado",
          "enforcement.optional": "Opcional",
          "enforcement.informational": "Información",
          description: "Descripción",
          effectiveDate: "Fecha Efectiva",
          expirationDate: "Fecha de Expiración",
          status: "Estado",
          "status.active": "Activo",
          "status.inactive": "Inactivo",
          "status.draft": "Borrador",
          "status.review": "Revisión",
          "status.archived": "Archivado",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Reglas",
          evaluations: "Evaluaciones",
          violations: "Violaciones",
          compliance: "Cumplimiento",
          statistics: "Estadísticas",
          "rules.empty": "No hay reglas definidas",
          "rules.name": "Nombre",
          "rules.condition": "Condición",
          "rules.action": "Acción",
          "rules.priority": "Prioridad",
          "rules.active": "Activa",
          "evaluations.empty": "No hay evaluaciones registradas",
          "evaluations.date": "Fecha",
          "evaluations.result": "Resultado",
          "evaluations.score": "Puntuación",
          "evaluations.evaluator": "Evaluador",
          "violations.empty": "No hay violaciones registradas",
          "violations.type": "Tipo",
          "violations.description": "Descripción",
          "violations.severity": "Severidad",
          "violations.date": "Fecha",
          "violations.status": "Estado",
          "compliance.empty": "No hay evaluaciones de cumplimiento",
          "compliance.average": "Score Promedio",
          "compliance.date": "Fecha",
          "compliance.area": "Área",
          "compliance.score": "Score",
          "compliance.status": "Estado",
          "compliance.findings": "Hallazgos",
          "statistics.rules": "Reglas",
          "statistics.evaluations": "Evaluaciones",
          "statistics.violations": "Violaciones",
          "statistics.score": "Score Actual",
          "statistics.info": "Información de la Política",
          "statistics.createdBy": "Creado por",
          "statistics.createdAt": "Fecha de creación",
          "statistics.lastEvaluation": "Última evaluación",
          "statistics.lastUpdated": "Última actualización",
          "statistics.evaluationStatus": "Estado evaluación",
          "statistics.complianceScore": "Score cumplimiento",
        },
        evaluation: {
          success: "Evaluación completada",
        },
        compliance: {
          success: "Verificación de cumplimiento completada",
        },
        score: {
          success: "Score calculado: {score}",
        },
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "Bandeja de Tareas BPMN",
      subtitle: "Gestiona tus tareas pendientes",
      stats: {
        totalTasks: "Tareas Pendientes",
        assignedToMe: "Asignadas a Mí",
        groupTasks: "De Mis Roles",
      },
      filters: {
        process: "Proceso",
        priority: "Prioridad",
        state: "Estado",
        allProcesses: "Todos los procesos",
        allPriorities: "Todas las prioridades",
        allStates: "Todos los estados",
        assigned: "Asignadas",
        unassigned: "Sin asignar",
        candidate: "Candidatas",
        low: "Baja",
        normal: "Normal",
        high: "Alta",
        urgent: "Urgente",
      },
      table: {
        priority: "Prioridad",
        task: "Tarea",
        process: "Proceso",
        assigned: "Asignado",
        roles: "Roles",
        creation: "Creación",
        dueDate: "Vencimiento",
        actions: "Acciones",
        noTasks: "No hay tareas pendientes",
        unassigned: "Sin asignar",
        openTask: "Abrir tarea",
        claim: "Reclamar",
        assign: "Asignar",
      },
      assignDialog: {
        title: "Asignar Tarea a Usuario",
        task: "Tarea",
        availableUsers: "Usuarios disponibles",
        username: "Usuario",
        fullname: "Nombre Completo",
        email: "Email",
        cancel: "Cancelar",
        assign: "Asignar",
      },
      actions: {
        refresh: "Refrescar",
        clearFilters: "Limpiar filtros",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Programar auditoría interna ISO 42001",
      subtitle: "Complete los datos para programar la auditoría interna",
      formTitle: "Datos de la auditoría",
      project: "Proyecto",
      projectPlaceholder: "Project ID",
      date: "Fecha programada",
      scope: "Alcance",
      scopePlaceholder: "Cláusulas, procesos y sistemas a auditar",
      auditor: "Auditor responsable",
      auditorPlaceholder: "Usuario o rol auditor",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Planificar auditoría",
      saving: "Guardando...",
      errors: {
        scopeRequired: "Debe definir el alcance de la auditoría",
        auditorRequired: "Debe asignar un auditor responsable",
        dateRequired: "Debe seleccionar una fecha",
        submitError: "Error al planificar la auditoría",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Aprobación de Agent - Override Humano",
      subtitle: "Complete los datos para aprobación de agent - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalHumanOverride: {
      title: "Aprobación de Modelo - Override Humano",
      subtitle: "Complete los datos para aprobación de modelo - override humano",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptHumanReview: {
      title: "Revisión Humana de Prompt",
      subtitle: "Complete los datos para revisión humana de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    hitlSlaReminder: {
      title: "Recordatorio SLA HITL",
      subtitle: "Complete los datos para recordatorio sla hitl",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    promptApprovalRequest: {
      title: "Solicitud de Aprobación de Prompt",
      subtitle: "Complete los datos para solicitud de aprobación de prompt",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionApproval: {
      title: "Aprobación de Desmantelamiento IA",
      subtitle: "Complete los datos para aprobación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapApproval: {
      title: "Aprobación de Brecha de Competencia",
      subtitle: "Complete los datos para aprobación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionApproval: {
      title: "Aprobación de Acción Correctiva",
      subtitle: "Complete los datos para aprobación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    approveConformityAssessment: {
      title: "Aprobar Evaluación de Conformidad",
      subtitle: "Complete los datos para aprobar evaluación de conformidad",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    deployerApproval: {
      title: "Aprobación de Despliegue",
      subtitle: "Complete los datos para aprobación de despliegue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    boardDecisionPaper: {
      title: "Decisión del Consejo",
      subtitle: "Complete los datos para decisión del consejo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReview: {
      title: "Revisión de Gestión",
      subtitle: "Complete los datos para revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    llmEvaluationReview: {
      title: "Revisión de Evaluación LLM",
      subtitle: "Complete los datos para revisión de evaluación llm",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ragEvaluationReview: {
      title: "Revisión de Evaluación RAG",
      subtitle: "Complete los datos para revisión de evaluación rag",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelEvaluationReview: {
      title: "Revisión de Evaluación de Modelo",
      subtitle: "Complete los datos para revisión de evaluación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAssessment: {
      title: "Evaluación de Brecha de Competencia",
      subtitle: "Complete los datos para evaluación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionAssessment: {
      title: "Evaluación de Acción Correctiva",
      subtitle: "Complete los datos para evaluación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    performanceReviewDecision: {
      title: "Decisión de Revisión de Rendimiento",
      subtitle: "Complete los datos para decisión de revisión de rendimiento",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    compliance: {
      dashboard: {
        title: "Dashboard Consolidado Compliance",
        subtitle: "Vista consolidada del estado de compliance según EU AI Act",
      },
      postMarketMonitoring: {
        title: "Dashboard Post-Market Monitoring",
        subtitle: "Monitoreo continuo de sistemas de IA según EU AI Act Art. 72",
      },
      sector: {
        title: "Metamodelo sectorial FaaS",
        subtitle: "Visualiza sectores, subsectores y frameworks configurados sin modificar el core de CodeflowX",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Gestión de declaraciones de conformidad según EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Revisión de Compliance",
      subtitle: "Complete los datos para revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    complianceReviewDecision: {
      title: "Decisión de Revisión de Compliance",
      subtitle: "Complete los datos para decisión de revisión de compliance",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewQmsGaps: {
      title: "Revisar Brechas QMS",
      subtitle: "Complete los datos para revisar brechas qms",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeDocumentation: {
      title: "Completar Documentación",
      subtitle: "Complete los datos para completar documentación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasReview: {
      title: "Revisión de Sesgo",
      subtitle: "Complete los datos para revisión de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasMitigationPlan: {
      title: "Plan de Mitigación de Sesgo",
      subtitle: "Complete los datos para plan de mitigación de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    biasUrgentDecision: {
      title: "Decisión Urgente de Sesgo",
      subtitle: "Complete los datos para decisión urgente de sesgo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftAnalysis: {
      title: "Análisis de Deriva",
      subtitle: "Complete los datos para análisis de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    driftReviewDecision: {
      title: "Decisión de Revisión de Deriva",
      subtitle: "Complete los datos para decisión de revisión de deriva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    internalAuditExecution: {
      title: "Ejecución de Auditoría Interna",
      subtitle: "Complete los datos para ejecución de auditoría interna",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewSchedule: {
      title: "Programación de Revisión de Gestión",
      subtitle: "Complete los datos para programación de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    completeMissingElements: {
      title: "Completar Elementos Faltantes",
      subtitle: "Complete los datos para completar elementos faltantes",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineModifications: {
      title: "Definir Modificaciones",
      subtitle: "Complete los datos para definir modificaciones",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    enhancedReview: {
      title: "Revisión Mejorada",
      subtitle: "Complete los datos para revisión mejorada",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    finalReview: {
      title: "Revisión Final",
      subtitle: "Complete los datos para revisión final",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    euRegistration: {
      title: "Registro UE",
      subtitle: "Complete los datos para registro ue",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    reviewRegistrationPackage: {
      title: "Revisar Paquete de Registro",
      subtitle: "Complete los datos para revisar paquete de registro",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    fixValidationErrors: {
      title: "Corregir Errores de Validación",
      subtitle: "Complete los datos para corregir errores de validación",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    manualResolution: {
      title: "Resolución Manual",
      subtitle: "Complete los datos para resolución manual",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    documentIncidentDetails: {
      title: "Documentar Detalles de Incidente",
      subtitle: "Complete los datos para documentar detalles de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    verifyIncidentResolution: {
      title: "Verificar Resolución de Incidente",
      subtitle: "Complete los datos para verificar resolución de incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    rootCauseAnalysis: {
      title: "Análisis de Causa Raíz",
      subtitle: "Complete los datos para análisis de causa raíz",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    defineCorrectiveActions: {
      title: "Definir Acciones Correctivas",
      subtitle: "Complete los datos para definir acciones correctivas",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionImplementation: {
      title: "Implementación de Acción Correctiva",
      subtitle: "Complete los datos para implementación de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    correctiveActionEffectiveness: {
      title: "Efectividad de Acción Correctiva",
      subtitle: "Complete los datos para efectividad de acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    managementReviewActions: {
      title: "Acciones de Revisión de Gestión",
      subtitle: "Complete los datos para acciones de revisión de gestión",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewRequest: {
      title: "Solicitud de Revisión Ética",
      subtitle: "Complete los datos para solicitud de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsReviewReminder: {
      title: "Recordatorio de Revisión Ética",
      subtitle: "Complete los datos para recordatorio de revisión ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsCommitteeReview: {
      title: "Revisión del Comité Ético",
      subtitle: "Complete los datos para revisión del comité ético",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    ethicsMitigationPlan: {
      title: "Plan de Mitigación Ética",
      subtitle: "Complete los datos para plan de mitigación ética",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapImplementation: {
      title: "Implementación de Brecha de Competencia",
      subtitle: "Complete los datos para implementación de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapAdditionalActions: {
      title: "Acciones Adicionales de Brecha de Competencia",
      subtitle: "Complete los datos para acciones adicionales de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    competenceGapRework: {
      title: "Retrabajo de Brecha de Competencia",
      subtitle: "Complete los datos para retrabajo de brecha de competencia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRequest: {
      title: "Solicitud de Desmantelamiento IA",
      subtitle: "Complete los datos para solicitud de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionVerification: {
      title: "Verificación de Desmantelamiento IA",
      subtitle: "Complete los datos para verificación de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    aiDecommissionRetention: {
      title: "Retención de Desmantelamiento IA",
      subtitle: "Complete los datos para retención de desmantelamiento ia",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    modelApprovalReminder: {
      title: "Recordatorio de Aprobación de Modelo",
      subtitle: "Complete los datos para recordatorio de aprobación de modelo",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    datasetReviewReminder: {
      title: "Recordatorio de Revisión de Dataset",
      subtitle: "Complete los datos para recordatorio de revisión de dataset",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    categorizeIncident: {
      title: "Categorizar Incidente",
      subtitle: "Complete los datos para categorizar incidente",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    executeCorrectiveAction: {
      title: "Ejecutar Acción Correctiva",
      subtitle: "Complete los datos para ejecutar acción correctiva",
      formTitle: "Datos del Formulario",
      back: "Volver",
      cancel: "Cancelar",
      submit: "Enviar",
      saving: "Guardando...",
      errors: {
        submitError: "Error al procesar el formulario",
      },
    },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },
  },

  en: {
    // Navigation and layout
    common: {
      dashboard: "Dashboard",
      infrastructure: "Infrastructure",
      admin: "Administration",
      aiNotebook: "AI Notebook",
      domainIngestion: "Domain Ingestion",
      modelEvaluation: "Model Evaluation",
      ragTraining: "RAG Training",
      governance: "Governance",
      settings: "Settings",
      profile: "Profile",
      logout: "Logout",
      loading: "Loading...",
      save: "Save",
      cancel: "Cancel",
      edit: "Edit",
      delete: "Delete",
      add: "Add",
      search: "Search",
      filter: "Filter",
      status: "Status",
      actions: "Actions",
      details: "Details",
      back: "Back",
      next: "Next",
      previous: "Previous",
      close: "Close",
      confirm: "Confirm",
      yes: "Yes",
      no: "No",
      active: "Active",
      inactive: "Inactive",
      pending: "Pending",
      completed: "Completed",
      error: "Error",
      success: "Success",
      warning: "Warning",
      info: "Information",
      changeLanguage: "Change Language",
      selectLanguage: "Select Language",
    },

    // Dashboard
    dashboard: {
      title: "Control Panel",
      welcome: "Welcome to CodeflowX",
      stats: "Statistics",
      quickActions: "Quick Actions",
      recentProjects: "Recent Projects",
      systemMetrics: "System Metrics",
      totalProjects: "Total Projects",
      activeModels: "Active Models",
      totalUsers: "Total Users",
      systemHealth: "System Health",
      viewAll: "View All",
      createNew: "Create New",
      lastUpdated: "Last Updated",
      performance: "Performance",
      efficiency: "Efficiency",
      reliability: "Reliability",
      activeNodes: "Active Nodes",
      of: "of",
      plusOneThisMonth: "+1 this month",
      codeGenerated: "Code Generated",
      lines: "lines",
      modelsAvailable: "Models Available",
      specialized: "specialized",
      plusThreeNew: "+3 new",
      licenseStatus: "License Status",
      proPlan: "Pro Plan",
      deployModel: "Deploy Model",
      viewAnalytics: "View Analytics",
      manageUsers: "Manage Users",
      systemHealthTitle: "System Health",
      cpuUsage: "CPU Usage",
      memory: "Memory",
      network: "Network",
      security: "Security",
    },

    // Runs
    runs: {
      title: "Ejecuciones de Modelos",
      subtitle:
        "Gestiona y monitorea las ejecuciones de experimentos de modelos de IA",
      totalRuns: "Ejecuciones",
      running: "Ejecutando",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        createRun: "Crear Ejecución",
        cloneRun: "Clonar Ejecución",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Ejecuciones",
        thisMonth: "+12 este mes",
        running: "Ejecutando",
        active: "Activas",
        completed: "Completadas",
        successful: "Exitosas",
        failed: "Fallidas",
        issues: "Con problemas",
      },
      searchPlaceholder: "Buscar ejecuciones...",
      selectStatus: "Estado",
      selectExperiment: "Experimento",
      createRun: "Nueva Ejecución",
      tabs: {
        all: "Todas",
        running: "Ejecutando",
        completed: "Completadas",
        failed: "Fallidas",
        scheduled: "Programadas",
      },
      duration: "Duración",
      artifacts: "Artefactos",
      files: "archivos",
      budget: "Presupuesto",
      accuracy: "Precisión",
      actions: {
        view: "Ver",
        edit: "Editar",
        clone: "Clonar",
        artifacts: "Artefactos",
      },
      noRuns: "No se encontraron ejecuciones",
      noRunsDescription:
        "Intenta ajustar los filtros o crear una nueva ejecución",
    },

    // Artifacts
    artifacts: {
      title: "Artefactos de Modelos",
      subtitle:
        "Gestiona y organiza todos los artefactos generados por los experimentos",
      totalArtifacts: "Artefactos",
      systemStatus: {
        operational: "Operacional",
      },
      quickActions: {
        title: "Acciones Rápidas",
        upload: "Subir Artefacto",
        bulkDownload: "Descarga Masiva",
        archive: "Archivar",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artefactos",
        thisMonth: "+25 este mes",
        totalSize: "Tamaño Total",
        storage: "Almacenamiento",
        available: "Disponibles",
        active: "Activos",
        archived: "Archivados",
      },
      searchPlaceholder: "Buscar artefactos...",
      selectType: "Tipo",
      selectStatus: "Estado",
      upload: "Subir",
      types: {
        model: "Modelo",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuración",
        image: "Imagen",
        other: "Otro",
      },
      size: "Tamaño",
      version: "Versión",
      created: "Creado",
      tags: "Etiquetas",
      actions: {
        view: "Ver",
        download: "Descargar",
        edit: "Editar",
        delete: "Eliminar",
      },
      noArtifacts: "No se encontraron artefactos",
      noArtifactsDescription:
        "Intenta ajustar los filtros o subir un nuevo artefacto",
    },

    // Infrastructure
    infrastructure: {
      title: "Infrastructure Management",
      overview: "Overview",
      providers: "Providers",
      kubernetes: "Kubernetes Clusters",
      gpuInstances: "GPU Instances",
      deployments: "Deployments",
      resources: "Resources",
      credentials: "Credentials",
      costManagement: "Cost Management",
      systemMonitoring: "System Monitoring",
      addProvider: "Add Provider",
      addCluster: "Add Cluster",
      addInstance: "Add Instance",
      clusterName: "Cluster Name",
      instanceType: "Instance Type",
      region: "Region",
      status: "Status",
      nodes: "Nodes",
      cpu: "CPU",
      memory: "Memory",
      storage: "Storage",
      network: "Network",
      security: "Security",
      monitoring: "Monitoring",
      logs: "Logs",
      metrics: "Metrics",
      alerts: "Alerts",
      // New translations for main page
      cloudProviders: "Cloud Providers",
      cloudProvidersDesc: "Manage cloud providers, credentials and connections",
      awsGcpAzure: "AWS, GCP, Azure",
      ovhRunpodVast: "OVH, RunPod, Vast.ai",
      credentialsApiKeys: "Credentials and API Keys",
      statusMonitoring: "Status monitoring",
      kubernetesDesc: "Manage K8s clusters, nodes and resources",
      eksGkeAks: "EKS, GKE, AKS",
      ownClusters: "Own clusters",
      nodeMonitoring: "Node monitoring",
      deploymentManagement: "Deployment management",
      gpuInstancesDesc: "GPU instance management for training and inference",
      t4V100A100H100: "T4, V100, A100, H100",
      rtx40903090: "RTX 4090, 3090",
      spotInstances: "Spot instances",
      costAnalysis: "Cost analysis",
      infrastructureDeployments: "Infrastructure Deployments",
      deploymentsDesc: "Infrastructure and service deployments",
      trainingClusters: "Training clusters",
      inferenceServices: "Inference services",
      autoScaling: "Auto scaling",
      costManagementDesc: "Infrastructure cost analysis and management",
      providerAnalysis: "Provider analysis",
      budgetAlerts: "Budget alerts",
      reports: "Reports",
      optimization: "Optimization",
      systemMonitoringDesc: "Real-time monitoring of all infrastructure",
      realTimeMetrics: "Real-time metrics",
      automaticAlerts: "Automatic alerts",
      customDashboards: "Custom dashboards",
      centralizedLogs: "Centralized logs",
      twoWeeksAgo: "2 weeks ago",
      deployCluster: "Deploy Cluster",
      launchGpu: "Launch GPU",
      viewCosts: "View Costs",
      // New translations for migrated pages
      resourceUtilizationDashboard: "Resource Utilization Dashboard",
      resourceUtilizationDesc: "Monitor resource usage across all infrastructure",
      resourceUtilization: "Resource Utilization",
      totalResources: "Total Resources",
      active: "active",
      avgCpuUsage: "Avg CPU Usage",
      avgMemoryUsage: "Avg Memory Usage",
      avgStorageUsage: "Avg Storage Usage",
      networkUsage: "Network Usage",
      networkIn: "Network In",
      networkOut: "Network Out",
      resourceName: "Resource Name",
      resourceType: "Resource Type",
      cloudResources: "Cloud Resources",
      basicInformation: "Basic Information",
      resourceId: "Resource ID",
      cloudProvider: "Cloud Provider",
      zone: "Zone",
      autoScaling: "Auto Scaling",
      specifications: "Specifications",
      scalingConfiguration: "Scaling Configuration",
      minInstances: "Min Instances",
      currentInstances: "Current Instances",
      maxInstances: "Max Instances",
      cost: "Cost",
      costPerHour: "Cost per Hour",
      costPerMonth: "Cost per Month",
      metadata: "Metadata",
      createdAt: "Created At",
      updatedAt: "Updated At",
      instances: "Instances",
      noResourcesFound: "No resources found",
      cloudProviderOverview: "Cloud Provider Overview",
      cloudCredentialOverview: "Cloud Credential Overview",
      cloudRegionOverview: "Cloud Region Overview",
      cloudResourceOverview: "Cloud Resource Overview",
      kubernetesClusterOverview: "Kubernetes Cluster Overview",
      kubernetesNodeOverview: "Kubernetes Node Overview",
      gpuInstanceOverview: "GPU Instance Overview",
      infrastructureTemplateOverview: "Infrastructure Template Overview",
      infrastructureMetricOverview: "Infrastructure Metric Overview",
      infrastructureMetricsSummaryOverview: "Infrastructure Metrics Summary Overview",
      infrastructureCostOverview: "Infrastructure Cost Overview",
      infrastructureCostAnalysisOverview: "Infrastructure Cost Analysis Overview",
      infrastructureAuditOverview: "Infrastructure Audit Overview",
      infrastructureHealthMatrixOverview: "Infrastructure Health Matrix Overview",
      infrastructureOverviewOverview: "Infrastructure Overview",
      capacityForecastOverview: "Capacity Forecast Overview",
      resourceQuotaOverview: "Resource Quota Overview",
    },

    // Administration
    admin: {
      title: "Administration Panel",
      users: "Users",
      roles: "Roles",
      permissions: "Permissions",
      departments: "Departments",
      sessions: "Sessions",
      audit: "Audit",
      licenses: "Licenses",
      nodes: "Nodes",
      updates: "Updates",
      menuManagement: "Menu Management",
      notifications: "Notifications",
      systemSettings: "System Settings",
      userManagement: "User Management",
      systemHealth: "System Health",
      securitySettings: "Security Settings",
      backupRestore: "Backup & Restore",
      systemLogs: "System Logs",
      performanceMetrics: "Performance Metrics",
      // New translations for main page
      twoHoursAgo: "2 hours ago",
      twoMinutesAgo: "2 minutes ago",
      fiveMinutesAgo: "5 minutes ago",
      tenMinutesAgo: "10 minutes ago",
      fifteenMinutesAgo: "15 minutes ago",
      userLogin: "User login",
      roleUpdated: "Role updated",
      newUserInvited: "New user invited",
      systemBackupCompleted: "System backup completed",
      configurationUpdated: "Configuration updated",
    },

    // Themes
    themes: {
      title: "CodeflowX Themes",
      changeTheme: "Change CodeflowX theme",
      useSystemTheme: "Use system theme",
      trekkerFederation: "🌌 Leka Federation",
      trekkerVulcan: "🖖 Leka Vulcan",
      trekkerKlingon: "⚔️ Leka Klingon",
      trekkerRomulan: "🖖 Leka Romulan",
      trekkerCardassian: "🖖 Leka Cardassian",
      trekkerBajoran: "🖖 Leka Bajoran",
      trekkerFerengi: "💰 Leka Ferengi",
      trekkerAndorian: "🖖 Leka Andorian",
      trekkerTellarite: "🖖 Leka Tellarite",
      trekkerDenobulan: "🏥 Leka Denobulan",
    },

    // Status and colors
    status: {
      online: "Online",
      offline: "Offline",
      maintenance: "Maintenance",
      error: "Error",
      warning: "Warning",
      info: "Information",
      success: "Success",
      pending: "Pending",
      running: "Running",
      stopped: "Stopped",
      starting: "Starting",
      stopping: "Stopping",
      healthy: "Healthy",
      unhealthy: "Unhealthy",
      degraded: "Degraded",
    },

    // System messages
    messages: {
      loadingTheme: "Loading theme...",
      themeApplied: "Theme applied successfully",
      themeError: "Error applying theme",
      redirectingToLogin: "Redirecting to login...",
      systemActive: "System Active",
      aiPowered: "AI Powered",
      documentation: "Documentation",
      support: "Support",
      status: "Status",
      welcomeMessage: "Welcome, {name} 👋",
    },

    // Model Management
    modelManagement: {
      title: "Model Management",
      subtitle: "Overview of the AI model management system",
      totalModels: "Total Models",
      activeModels: "Active Models",
      totalProviders: "Total Providers",
      marketplaceModels: "Marketplace Models",
      recentDownloads: "Recent Downloads",
      systemHealth: "System Health",
      healthy: "Healthy",
      warning: "Warning",
      critical: "Critical",
      approved: "Approved",
      pending: "Pending",
      rejected: "Rejected",
      addModel: "Add Model",
      viewModels: "View Models",
      manageProviders: "Manage Providers",
      marketplace: "Marketplace",
      recentActivity: "Recent Activity",
      topProviders: "Top Providers",
      trendingModels: "Trending Models",
      stats: {
        totalModels: "Total Models",
        totalModelsChange: "+12% this month",
        activeModels: "Active Models",
        activeModelsChange: "+8% this month",
        providers: "Providers",
        providersChange: "+3 this month",
        recentDownloads: "Recent Downloads",
        recentDownloadsChange: "+25% this week",
      },
      systemStatus: {
        title: "System Status",
        generalStatus: "General Status",
        healthy: "Healthy",
        warning: "Warning",
        apiModels: "Models API",
        marketplace: "Marketplace",
        providers: "Providers",
        operational: "Operational",
      },
      recentModels: {
        title: "Recent Models",
        noModels: "No recent models",
      },
      typeDistribution: {
        title: "Models by Type",
        viewDetails: "View Details",
        text: "Text",
        vision: "Vision",
        audio: "Audio",
      },
      quickActions: {
        title: "Quick Actions",
        newModel: "New Model",
        addProvider: "Add Provider",
        exploreMarketplace: "Explore Marketplace",
        settings: "Settings",
      },
      systemInfo: {
        title: "System Information",
        modelManagement: {
          title: "Model Management",
          description:
            "Registration, versioning and complete lifecycle management of AI models",
        },
        aiProviders: {
          title: "AI Providers",
          description:
            "Integration with multiple AI service providers and token management",
        },
        marketplace: {
          title: "Marketplace",
          description:
            "Access to +3,000,000 AI models from multiple marketplaces and providers",
        },
      },
    },

    // Runs
    runs: {
      title: "Model Runs",
      subtitle: "Manage and monitor AI model experiment executions",
      totalRuns: "Runs",
      running: "Running",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        createRun: "Create Run",
        cloneRun: "Clone Run",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
      stats: {
        totalRuns: "Total Runs",
        thisMonth: "+12 this month",
        running: "Running",
        active: "Active",
        completed: "Completed",
        successful: "Successful",
        failed: "Failed",
        issues: "With issues",
      },
      searchPlaceholder: "Search runs...",
      selectStatus: "Status",
      selectExperiment: "Experiment",
      createRun: "New Run",
      tabs: {
        all: "All",
        running: "Running",
        completed: "Completed",
        failed: "Failed",
        scheduled: "Scheduled",
      },
      duration: "Duration",
      artifacts: "Artifacts",
      files: "files",
      budget: "Budget",
      accuracy: "Accuracy",
      actions: {
        view: "View",
        edit: "Edit",
        clone: "Clone",
        artifacts: "Artifacts",
      },
      noRuns: "No runs found",
      noRunsDescription: "Try adjusting filters or create a new run",
    },

    // Artifacts
    artifacts: {
      title: "Model Artifacts",
      subtitle: "Manage and organize all artifacts generated by experiments",
      totalArtifacts: "Artifacts",
      systemStatus: {
        operational: "Operational",
      },
      quickActions: {
        title: "Quick Actions",
        upload: "Upload Artifact",
        bulkDownload: "Bulk Download",
        archive: "Archive",
        analytics: "Analytics",
      },
      stats: {
        totalArtifacts: "Total Artifacts",
        thisMonth: "+25 this month",
        totalSize: "Total Size",
        storage: "Storage",
        available: "Available",
        active: "Active",
        archived: "Archived",
      },
      searchPlaceholder: "Search artifacts...",
      selectType: "Type",
      selectStatus: "Status",
      upload: "Upload",
      types: {
        model: "Model",
        dataset: "Dataset",
        checkpoint: "Checkpoint",
        log: "Log",
        config: "Configuration",
        image: "Image",
        other: "Other",
      },
      size: "Size",
      version: "Version",
      created: "Created",
      tags: "Tags",
      actions: {
        view: "View",
        download: "Download",
        edit: "Edit",
        delete: "Delete",
      },
      noArtifacts: "No artifacts found",
      noArtifactsDescription: "Try adjusting filters or upload a new artifact",
    },

    // Domain Ingestion
    domainIngestion: {
      title: "Domain Ingestion Dashboard",
      subtitle: "Monitor and manage your specialized data domains",
      stats: {
        totalDomains: "Total Domains",
        totalDocuments: "Total Documents",
        webPages: "Web Pages",
        apis: "Connected APIs",
        activeDomains: "active domains",
        documentsChange: "+12% this month",
        webChange: "+8% this week",
        apisChange: "+3 this month",
      },
      actions: {
        wizard: "Domain Wizard",
        manage: "Manage Domains",
        access: "Access",
      },
      modules: {
        webScraping: {
          title: "Web Scraping",
          description: "Extract data from websites and monitor changes",
        },
        uploadDocuments: {
          title: "Upload Documents",
          description: "Upload and process documents to enrich domains",
        },
        monitor: {
          title: "Monitoring",
          description: "Monitor job performance and status",
        },
        settings: {
          title: "Settings",
          description: "Configure global and domain-specific parameters",
        },
        configureSources: {
          title: "Configure Sources",
          description: "Configure data sources and APIs",
        },
      },
      recentJobs: {
        title: "Recent Jobs",
      },
      jobStatus: {
        completed: "Completed",
        running: "Running",
        failed: "Failed",
        pending: "Pending",
        queued: "Queued",
        processing: "Processing",
        cancelled: "Cancelled",
        retrying: "Retrying",
      },
      wizard: {
        steps: {
          basicInfo: "Basic Information",
          dataSources: "Data Sources",
          compliance: "Compliance",
          advancedFeatures: "Advanced Features",
          review: "Review",
        },
        templates: {
          title: "Domain Templates",
          description: "Select a predefined template or create a custom one",
          fintech: "Fintech Banking",
          healthcare: "Healthcare Analytics",
          ecommerce: "E-commerce Insights",
          government: "IA Act Compliance",
          manufacturing: "Manufacturing IoT",
          custom: "Custom",
        },
        industries: {
          title: "Industry",
          fintech: "Fintech",
          healthcare: "Healthcare",
          ecommerce: "E-commerce",
          government: "Government",
          manufacturing: "Manufacturing",
          education: "Education",
          legal: "Legal",
          other: "Other",
        },
        compliance: {
          title: "Compliance Frameworks",
          description: "Select applicable compliance frameworks",
          gdpr: "GDPR (General Data Protection Regulation)",
          sox: "SOX (Sarbanes-Oxley Act)",
          pci: "PCI DSS (Payment Card Industry Data Security Standard)",
          hipaa: "HIPAA (Health Insurance Portability and Accountability Act)",
          iaActEspana: "IA Act Spain (Artificial Intelligence Law)",
          iaActEuropa: "IA Act Europe (Artificial Intelligence Act)",
          iso27001: "ISO 27001 (Information Security Management)",
          iso27701: "ISO 27701 (Privacy Information Management)",
          nist: "NIST Cybersecurity Framework",
          soc2: "SOC 2 (Service Organization Control 2)",
          fedramp:
            "FedRAMP (Federal Risk and Authorization Management Program)",
          ccpa: "CCPA (California Consumer Privacy Act)",
          lgpd: "LGPD (Lei Geral de Proteção de Dados)",
          pipeda:
            "PIPEDA (Personal Information Protection and Electronic Documents Act)",
          // Software Development ISO Standards
          iso12207: "ISO 12207 (Software Life Cycle)",
          iso15504: "ISO 15504 (Software Process Assessment)",
          iso20000: "ISO 20000 (IT Service Management)",
          iso27002: "ISO 27002 (Information Security Controls)",
          iso27005: "ISO 27005 (Security Risk Management)",
          iso27017: "ISO 27017 (Cloud Security)",
          iso27018: "ISO 27018 (Cloud Data Protection)",
          iso9001: "ISO 9001 (Quality Management System)",
          iso14001: "ISO 14001 (Environmental Management System)",
          // Additional European Regulations
          eidas: "eIDAS (EU Electronic Identification)",
          psd2: "PSD2 (EU Payment Services Directive)",
          nis2: "NIS2 (EU Network Security Directive)",
          // Software Quality Standards
          cmmi: "CMMI (Capability Maturity Model Integration)",
          tickit: "TICKIT (IT and Telecommunications Quality)",
          spice:
            "SPICE (Software Process Improvement and Capability Determination)",
          custom: "Custom",
        },
        dataSources: {
          title: "Data Source Types",
          description: "Select the types of data sources you will use",
          api: "API",
          database: "Database",
          file: "File",
          web: "Web",
          stream: "Stream",
        },
        basicInfo: {
          name: "Domain Name",
          description: "Description",
          industry: "Industry",
          owner: "Owner",
          team: "Team",
        },
        advancedFeatures: {
          title: "Advanced Features",
          rag: "RAG (Create datasets)",
          training: "Model Training",
          compliance: "Automatic Compliance",
          monitoring: "Advanced Monitoring",
          modelSelection: "Model Selection",
          modelName: "Internal Model Name",
          adapter: "Create Adapter",
          finetuning: "Fine-tuning",
          baseModel: "Base Model",
          customModel: "Custom Model",
        },
        review: {
          title: "Final Review",
          description: "Review the configuration before creating the domain",
          template: "Selected template",
          industry: "Industry",
          compliance: "Compliance frameworks",
          dataSources: "Data sources",
          features: "Enabled features",
        },
        template: {
          fintech: {
            name: "Fintech Banking",
            description:
              "Domain specialized in financial data analysis and regulatory compliance",
            features: [
              "RAG enabled",
              "GDPR/SOX compliance",
              "Advanced monitoring",
            ],
            compliance: ["GDPR", "SOX", "PCI DSS"],
          },
          healthcare: {
            name: "Healthcare Analytics",
            description:
              "Domain for medical data analysis with HIPAA compliance",
            features: [
              "RAG enabled",
              "HIPAA compliance",
              "Advanced encryption",
            ],
            compliance: ["HIPAA", "GDPR"],
          },
          ecommerce: {
            name: "E-commerce Insights",
            description:
              "Domain for purchase behavior analysis and recommendations",
            features: ["RAG enabled", "Behavior analysis", "Recommendations"],
            compliance: ["GDPR"],
          },
          government: {
            name: "IA Act Compliance",
            description:
              "Domain for compliance with Spanish IA Act and government regulations",
            features: ["RAG enabled", "IA Act compliance", "Complete audit"],
            compliance: ["IA Act Spain", "GDPR"],
          },
          manufacturing: {
            name: "Manufacturing IoT",
            description:
              "Domain for IoT data analysis in industrial environments",
            features: ["RAG enabled", "IoT analysis", "Real-time monitoring"],
            compliance: ["ISO 27001"],
          },
        },
        actions: {
          next: "Next",
          previous: "Previous",
          create: "Create Domain",
          cancel: "Cancel",
        },
      },
    },

    // Model Evaluation
    modelEvaluation: {
      title: "Model Evaluation",
      subtitle: "Dashboard for AI model evaluation and testing",
      completed: "Completed",
      running: "Running",
      failed: "Failed",
      pending: "Pending",
      unknown: "Unknown",
      loadingData: "Loading dashboard data...",
      errorLoadingData: "Could not load dashboard data",
      pipeline: "Pipeline",
      abTest: "A/B Test",
      security: "Security",
      evaluation: "Evaluation",
      newEvaluation: "New Evaluation",
      viewEvaluation: "View Evaluation",
      pipelineModal: "Pipeline Modal",
      abTestModal: "A/B Test Modal",
      securityModal: "Security Modal",
      reportModal: "Report Modal",
    },

    // RAG Training
    ragTraining: {
      title: "RAG Training",
      subtitle: "Dashboard for RAG training and metrics",
      totalDocuments: "Total Documents",
      totalChunks: "Total Chunks",
      totalSearches: "Total Searches",
      averageResponseTime: "Average Response Time",
      activeUsers: "Active Users",
      storageUsed: "Storage Used",
      documentsByType: "Documents by Type",
      searchesByDay: "Searches by Day",
      topQueries: "Top Queries",
      pdf: "PDF",
      docx: "DOCX",
      txt: "TXT",
      html: "HTML",
      configuration: "database configuration",
      apiEndpoints: "API endpoints",
      authentication: "authentication",
      middlewareSetup: "middleware setup",
      deployment: "deployment",
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine-tuning and Artifacts unified",
      operational: "Operational",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artifacts",
      storage: "Storage",
      search: "Search...",
      type: "Type",
      technology: "Technology",
      domain: "Domain",
      generic: "Generic",
      quick: {
        newAdapter: "New Adapter",
        newFinetune: "New Fine-tuning",
        importModel: "Import Model",
        analytics: "Analytics",
      },
      promote: "Promote",
      viewRun: "View Run",
      download: "Download",
      metric: "Metric",
      cost: "Cost",
      duration: "Duration",
      startedAt: "Started At",
      size: "Size",
      createdAt: "Created At",
      linked: "Linked",
      detailsPlaceholder: "Detail content (mock)",
    },

    // Models Management
    models: {
      title: "Manage Models",
      subtitle: "Complete lifecycle management of AI models",
      operational: "Operational",
      totalModels: "Total Models",
      approvedModels: "Approved Models",
      activeModels: "Active Models",
      marketplaceModels: "Marketplace",
      search: "Search models...",
      type: "Type",
      status: "Status",
      provider: "Provider",
      text: "Text",
      vision: "Vision",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Draft",
      approved: "Approved",
      ready: "Ready",
      error: "Error",
      noDescription: "No description",
      duplicate: "Duplicate",
      noModels: "No models found",
      createModel: "Create New Model",
      modelDetails: "Model Details",
      editModel: "Edit Model",
      importModel: "Import Model",
      bulkActions: "Bulk Actions",
      analytics: "Model Analytics",
      newModel: "New Model",
      createModelDescription: "Form to create a new model (mock)",
      viewModelDescription: "Detailed view of the model (mock)",
      editModelDescription: "Form to edit the model (mock)",
      importModelDescription: "Form to import model (mock)",
      bulkActionsDescription: "Bulk actions on models (mock)",
      analyticsDescription: "Model metrics and analytics (mock)",
      quick: {
        title: "Quick Actions",
        createModel: "Create Model",
        importModel: "Import Model",
        bulkActions: "Bulk Actions",
        analytics: "Analytics",
      },
    },

    // Training Hub
    training: {
      title: "Training Hub",
      subtitle: "Adapters, Fine‑tuning y Artefactos unificados",
      operational: "Operacional",
      adapters: "Adapters",
      finetuning: "Fine-tuning",
      artifacts: "Artefactos",
      storage: "Storage",
      search: "Buscar...",
      type: "Tipo",
      technology: "Tecnología",
      domain: "Dominio",
      generic: "Genérico",
      quick: {
        newAdapter: "Nuevo Adapter",
        newFinetune: "Nuevo Fine‑tuning",
        importModel: "Importar Modelo",
        analytics: "Analytics",
      },
      promote: "Promover",
      viewRun: "Ver run",
      download: "Descargar",
      metric: "Métrica",
      cost: "Coste",
      duration: "Duración",
      startedAt: "Inicio",
      size: "Tamaño",
      createdAt: "Creado",
      linked: "Vinculado",
      detailsPlaceholder: "Contenido de detalle (mock)",
    },

    // Models Management
    models: {
      title: "Administrar Modelos",
      subtitle: "Gestión completa del ciclo de vida de modelos de IA",
      operational: "Operacional",
      totalModels: "Total Modelos",
      approvedModels: "Modelos Aprobados",
      activeModels: "Modelos Activos",
      marketplaceModels: "Marketplace",
      search: "Buscar modelos...",
      type: "Tipo",
      status: "Estado",
      provider: "Proveedor",
      text: "Texto",
      vision: "Visión",
      audio: "Audio",
      multimodal: "Multimodal",
      draft: "Borrador",
      approved: "Aprobado",
      ready: "Listo",
      error: "Error",
      noDescription: "Sin descripción",
      duplicate: "Duplicar",
      noModels: "No se encontraron modelos",
      createModel: "Crear Nuevo Modelo",
      modelDetails: "Detalles del Modelo",
      editModel: "Editar Modelo",
      importModel: "Importar Modelo",
      bulkActions: "Acciones Masivas",
      analytics: "Analytics de Modelos",
      newModel: "Nuevo Modelo",
      createModelDescription: "Formulario para crear un nuevo modelo (mock)",
      viewModelDescription: "Vista detallada del modelo (mock)",
      editModelDescription: "Formulario para editar el modelo (mock)",
      importModelDescription: "Formulario para importar modelo (mock)",
      bulkActionsDescription: "Acciones masivas sobre modelos (mock)",
      analyticsDescription: "Métricas y analytics de modelos (mock)",
      quick: {
        title: "Acciones Rápidas",
        createModel: "Crear Modelo",
        importModel: "Importar Modelo",
        bulkActions: "Acciones Masivas",
        analytics: "Analytics",
      },
    },

    // BPMN Task Inbox
    taskInbox: {
      title: "BPMN Task Inbox",
      subtitle: "Manage your pending tasks",
      stats: {
        totalTasks: "Pending Tasks",
        assignedToMe: "Assigned to Me",
        groupTasks: "From My Roles",
      },
      filters: {
        process: "Process",
        priority: "Priority",
        state: "State",
        allProcesses: "All processes",
        allPriorities: "All priorities",
        allStates: "All states",
        assigned: "Assigned",
        unassigned: "Unassigned",
        candidate: "Candidate",
        low: "Low",
        normal: "Normal",
        high: "High",
        urgent: "Urgent",
      },
      table: {
        priority: "Priority",
        task: "Task",
        process: "Process",
        assigned: "Assigned",
        roles: "Roles",
        creation: "Creation",
        dueDate: "Due Date",
        actions: "Actions",
        noTasks: "No pending tasks",
        unassigned: "Unassigned",
        openTask: "Open task",
        claim: "Claim",
        assign: "Assign",
      },
      assignDialog: {
        title: "Assign Task to User",
        task: "Task",
        availableUsers: "Available users",
        username: "Username",
        fullname: "Full Name",
        email: "Email",
        cancel: "Cancel",
        assign: "Assign",
      },
      actions: {
        refresh: "Refresh",
        clearFilters: "Clear filters",
      },
    },

    // Internal Audit Scheduling Form
    internalAuditScheduling: {
      title: "Schedule Internal Audit ISO 42001",
      subtitle: "Complete the data to schedule the internal audit",
      formTitle: "Audit Data",
      project: "Project",
      projectPlaceholder: "Project ID",
      date: "Scheduled Date",
      scope: "Scope",
      scopePlaceholder: "Clauses, processes and systems to audit",
      auditor: "Responsible Auditor",
      auditorPlaceholder: "User or auditor role",
      back: "Back",
      cancel: "Cancel",
      submit: "Schedule Audit",
      saving: "Saving...",
      errors: {
        scopeRequired: "You must define the audit scope",
        auditorRequired: "You must assign a responsible auditor",
        dateRequired: "You must select a date",
        submitError: "Error scheduling the audit",
      },

    // BPMN Forms
    agentApprovalHumanOverride: {
      title: "Agent Approval - Human Override",
      subtitle: "Complete the data for agent approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalHumanOverride: {
      title: "Model Approval - Human Override",
      subtitle: "Complete the data for model approval - human override",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptHumanReview: {
      title: "Prompt Human Review",
      subtitle: "Complete the data for prompt human review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    hitlSlaReminder: {
      title: "HITL SLA Reminder",
      subtitle: "Complete the data for hitl sla reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    promptApprovalRequest: {
      title: "Prompt Approval Request",
      subtitle: "Complete the data for prompt approval request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionApproval: {
      title: "AI Decommission Approval",
      subtitle: "Complete the data for ai decommission approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapApproval: {
      title: "Competence Gap Approval",
      subtitle: "Complete the data for competence gap approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionApproval: {
      title: "Corrective Action Approval",
      subtitle: "Complete the data for corrective action approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    approveConformityAssessment: {
      title: "Approve Conformity Assessment",
      subtitle: "Complete the data for approve conformity assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    deployerApproval: {
      title: "Deployer Approval",
      subtitle: "Complete the data for deployer approval",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    boardDecisionPaper: {
      title: "Board Decision Paper",
      subtitle: "Complete the data for board decision paper",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReview: {
      title: "Management Review",
      subtitle: "Complete the data for management review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    llmEvaluationReview: {
      title: "LLM Evaluation Review",
      subtitle: "Complete the data for llm evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ragEvaluationReview: {
      title: "RAG Evaluation Review",
      subtitle: "Complete the data for rag evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelEvaluationReview: {
      title: "Model Evaluation Review",
      subtitle: "Complete the data for model evaluation review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAssessment: {
      title: "Competence Gap Assessment",
      subtitle: "Complete the data for competence gap assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionAssessment: {
      title: "Corrective Action Assessment",
      subtitle: "Complete the data for corrective action assessment",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    performanceReviewDecision: {
      title: "Performance Review Decision",
      subtitle: "Complete the data for performance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    compliance: {
      dashboard: {
        title: "Consolidated Compliance Dashboard",
        subtitle: "Consolidated view of compliance status according to EU AI Act",
      },
      postMarketMonitoring: {
        title: "Post-Market Monitoring Dashboard",
        subtitle: "Continuous monitoring of AI systems according to EU AI Act Art. 72",
      },
      sector: {
        title: "Sectorial FaaS Metamodel",
        subtitle: "View sectors, subsectors and configured frameworks without modifying CodeflowX core",
      },
      conformityDeclaration: {
        title: "EU Declaration of Conformity Manager",
        subtitle: "Management of conformity declarations according to EU AI Act Annex V",
      },
      conformityReview: {
        title: "Conformity Assessment Review",
        subtitle: "Human review required for borderline compliance assessment",
      },
    },
    complianceReview: {
      title: "Compliance Review",
      subtitle: "Complete the data for compliance review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    complianceReviewDecision: {
      title: "Compliance Review Decision",
      subtitle: "Complete the data for compliance review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewQmsGaps: {
      title: "Review QMS Gaps",
      subtitle: "Complete the data for review qms gaps",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeDocumentation: {
      title: "Complete Documentation",
      subtitle: "Complete the data for complete documentation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasReview: {
      title: "Bias Review",
      subtitle: "Complete the data for bias review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasMitigationPlan: {
      title: "Bias Mitigation Plan",
      subtitle: "Complete the data for bias mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    biasUrgentDecision: {
      title: "Bias Urgent Decision",
      subtitle: "Complete the data for bias urgent decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftAnalysis: {
      title: "Drift Analysis",
      subtitle: "Complete the data for drift analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    driftReviewDecision: {
      title: "Drift Review Decision",
      subtitle: "Complete the data for drift review decision",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    internalAuditExecution: {
      title: "Internal Audit Execution",
      subtitle: "Complete the data for internal audit execution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewSchedule: {
      title: "Management Review Schedule",
      subtitle: "Complete the data for management review schedule",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    completeMissingElements: {
      title: "Complete Missing Elements",
      subtitle: "Complete the data for complete missing elements",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineModifications: {
      title: "Define Modifications",
      subtitle: "Complete the data for define modifications",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    enhancedReview: {
      title: "Enhanced Review",
      subtitle: "Complete the data for enhanced review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    finalReview: {
      title: "Final Review",
      subtitle: "Complete the data for final review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    euRegistration: {
      title: "EU Registration",
      subtitle: "Complete the data for eu registration",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    reviewRegistrationPackage: {
      title: "Review Registration Package",
      subtitle: "Complete the data for review registration package",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    fixValidationErrors: {
      title: "Fix Validation Errors",
      subtitle: "Complete the data for fix validation errors",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    manualResolution: {
      title: "Manual Resolution",
      subtitle: "Complete the data for manual resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    documentIncidentDetails: {
      title: "Document Incident Details",
      subtitle: "Complete the data for document incident details",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    verifyIncidentResolution: {
      title: "Verify Incident Resolution",
      subtitle: "Complete the data for verify incident resolution",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    rootCauseAnalysis: {
      title: "Root Cause Analysis",
      subtitle: "Complete the data for root cause analysis",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    defineCorrectiveActions: {
      title: "Define Corrective Actions",
      subtitle: "Complete the data for define corrective actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionImplementation: {
      title: "Corrective Action Implementation",
      subtitle: "Complete the data for corrective action implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    correctiveActionEffectiveness: {
      title: "Corrective Action Effectiveness",
      subtitle: "Complete the data for corrective action effectiveness",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    managementReviewActions: {
      title: "Management Review Actions",
      subtitle: "Complete the data for management review actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewRequest: {
      title: "Ethics Review Request",
      subtitle: "Complete the data for ethics review request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsReviewReminder: {
      title: "Ethics Review Reminder",
      subtitle: "Complete the data for ethics review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsCommitteeReview: {
      title: "Ethics Committee Review",
      subtitle: "Complete the data for ethics committee review",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    ethicsMitigationPlan: {
      title: "Ethics Mitigation Plan",
      subtitle: "Complete the data for ethics mitigation plan",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapImplementation: {
      title: "Competence Gap Implementation",
      subtitle: "Complete the data for competence gap implementation",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapAdditionalActions: {
      title: "Competence Gap Additional Actions",
      subtitle: "Complete the data for competence gap additional actions",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    competenceGapRework: {
      title: "Competence Gap Rework",
      subtitle: "Complete the data for competence gap rework",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRequest: {
      title: "AI Decommission Request",
      subtitle: "Complete the data for ai decommission request",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionVerification: {
      title: "AI Decommission Verification",
      subtitle: "Complete the data for ai decommission verification",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    aiDecommissionRetention: {
      title: "AI Decommission Retention",
      subtitle: "Complete the data for ai decommission retention",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    modelApprovalReminder: {
      title: "Model Approval Reminder",
      subtitle: "Complete the data for model approval reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    datasetReviewReminder: {
      title: "Dataset Review Reminder",
      subtitle: "Complete the data for dataset review reminder",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    categorizeIncident: {
      title: "Categorize Incident",
      subtitle: "Complete the data for categorize incident",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    executeCorrectiveAction: {
      title: "Execute Corrective Action",
      subtitle: "Complete the data for execute corrective action",
      formTitle: "Form Data",
      back: "Back",
      cancel: "Cancel",
      submit: "Submit",
      saving: "Saving...",
      errors: {
        submitError: "Error processing the form",
      },
    },
    },

    // Governance
    governance: {
      title: "Automatic Governance Dashboard",
      subtitle: "Monitor and manage AI governance in real time",
      refresh: "Refresh",
      configure: "Configure",
      low: "LOW",
      medium: "MEDIUM",
      high: "HIGH",
      critical: "CRITICAL",
      riskLevel: "Risk Level",
      compliance: "Compliance",
      audit: "Audit",
      policies: "Policies",
      violations: "Violations",
      alerts: "Alerts",
      metrics: "Metrics",
      reports: "Reports",
      settings: "Settings",
      overview: {
        title: "Policies Overview",
        subtitle: "Management and configuration of governance policies",
        metrics: {
          total: "Total Policies",
          active: "Active Policies",
          violations: "Violations",
          compliance: "Compliance Score",
        },
        filters: {
          title: "Filters",
          clear: "Clear",
          search: {
            placeholder: "Search policies...",
          },
          type: "All Types",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          status: "All Statuses",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          enforcement: "All Levels",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
        },
        table: {
          title: "Policies",
          id: "ID",
          name: "Name",
          type: "Type",
          enforcement: "Level",
          version: "Version",
          status: "Status",
          violations: "Violations",
          score: "Score",
          actions: "Actions",
          empty: "No policies found",
        },
        button: {
          create: "Create Policy",
        },
        confirmDelete: "Delete this policy?",
      },
      detail: {
        "title.edit": "Edit Policy",
        "title.create": "New Policy",
        button: {
          evaluate: "Evaluate",
          compliance: "Check Compliance",
          score: "Calculate Score",
        },
        form: {
          title: "Policy Information",
          name: "Policy Name",
          version: "Version",
          type: "Type",
          "type.security": "Security",
          "type.compliance": "Compliance",
          "type.ethics": "Ethics",
          "type.privacy": "Privacy",
          "type.operational": "Operational",
          category: "Category",
          enforcement: "Enforcement Level",
          "enforcement.mandatory": "Mandatory",
          "enforcement.recommended": "Recommended",
          "enforcement.optional": "Optional",
          "enforcement.informational": "Informational",
          description: "Description",
          effectiveDate: "Effective Date",
          expirationDate: "Expiration Date",
          status: "Status",
          "status.active": "Active",
          "status.inactive": "Inactive",
          "status.draft": "Draft",
          "status.review": "Under Review",
          "status.archived": "Archived",
          metadata: "Metadata (JSON)",
        },
        tabs: {
          rules: "Rules",
          evaluations: "Evaluations",
          violations: "Violations",
          compliance: "Compliance",
          statistics: "Statistics",
          "rules.empty": "No rules defined",
          "rules.name": "Name",
          "rules.condition": "Condition",
          "rules.action": "Action",
          "rules.priority": "Priority",
          "rules.active": "Active",
          "evaluations.empty": "No evaluations registered",
          "evaluations.date": "Date",
          "evaluations.result": "Result",
          "evaluations.score": "Score",
          "evaluations.evaluator": "Evaluator",
          "violations.empty": "No violations registered",
          "violations.type": "Type",
          "violations.description": "Description",
          "violations.severity": "Severity",
          "violations.date": "Date",
          "violations.status": "Status",
          "compliance.empty": "No compliance assessments",
          "compliance.average": "Average Score",
          "compliance.date": "Date",
          "compliance.area": "Area",
          "compliance.score": "Score",
          "compliance.status": "Status",
          "compliance.findings": "Findings",
          "statistics.rules": "Rules",
          "statistics.evaluations": "Evaluations",
          "statistics.violations": "Violations",
          "statistics.score": "Current Score",
          "statistics.info": "Policy Information",
          "statistics.createdBy": "Created by",
          "statistics.createdAt": "Creation date",
          "statistics.lastEvaluation": "Last evaluation",
          "statistics.lastUpdated": "Last update",
          "statistics.evaluationStatus": "Evaluation status",
          "statistics.complianceScore": "Compliance score",
        },
        evaluation: {
          success: "Evaluation completed",
        },
        compliance: {
          success: "Compliance check completed",
        },
        score: {
          success: "Score calculated: {score}",
        },
      },
    },

    // Providers Management
    providers: {
      title: "Proveedores de IA",
      subtitle: "Gestión de proveedores de servicios de IA",
      operational: "Operacional",
      totalProviders: "Total Proveedores",
      activeProviders: "Proveedores Activos",
      openaiProviders: "OpenAI",
      selfHosting: "Self-Hosting",
      search: "Buscar proveedores...",
      type: "Tipo",
      newProvider: "Nuevo Proveedor",
      noProviders: "No se encontraron proveedores",
      createProvider: "Crear Nuevo Proveedor",
      editProvider: "Editar Proveedor",
      providerDetails: "Detalles del Proveedor",
      quick: {
        title: "Acciones Rápidas",
        newProvider: "Nuevo Proveedor",
        importProvider: "Importar Proveedor",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
    },

    // Marketplace Management
    marketplace: {
      title: "Marketplace de Modelos",
      subtitle: "Descubre y descarga modelos de IA de múltiples proveedores",
      operational: "Operacional",
      totalModels: "Total Modelos",
      downloaded: "Descargados",
      downloadedModels: "Modelos Descargados",
      avgRating: "Rating Promedio",
      totalDownloads: "Total Descargas",
      search: "Buscar modelos...",
      source: "Fuente",
      type: "Tipo",
      sort: "Ordenar por",
      noModels: "No se encontraron modelos",
      noDescription: "Sin descripción",
      modelDetails: "Detalles del Modelo",
      download: "Descargar",
      downloaded: "Descargado",
      quick: {
        title: "Acciones Rápidas",
        advancedSearch: "Búsqueda Avanzada",
        downloadQueue: "Cola de Descargas",
        configuration: "Configuración",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        text: "Texto",
        vision: "Visión",
        audio: "Audio",
        multimodal: "Multimodal",
      },
    },

    // Experiments Management
    experiments: {
      title: "Experimentos de Modelos",
      subtitle: "Gestión y seguimiento de experimentos de IA",
      operational: "Operacional",
      totalExperiments: "Total Experimentos",
      runningExperiments: "Ejecutándose",
      completedExperiments: "Completados",
      failedExperiments: "Fallidos",
      search: "Buscar experimentos...",
      type: "Tipo",
      status: "Estado",
      newExperiment: "Nuevo Experimento",
      noExperiments: "No se encontraron experimentos",
      noDescription: "Sin descripción",
      createExperiment: "Crear Nuevo Experimento",
      editExperiment: "Editar Experimento",
      experimentDetails: "Detalles del Experimento",
      quick: {
        title: "Acciones Rápidas",
        newExperiment: "Nuevo Experimento",
        importExperiment: "Importar Experimento",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        planned: "Planificados",
        running: "Ejecutándose",
        completed: "Completados",
        failed: "Fallidos",
      },
    },

    // Model Serving Management
    modelServing: {
      title: "Model Serving",
      subtitle: "Gestión de despliegues y servicios de modelos",
      operational: "Operacional",
      totalDeployments: "Total Deployments",
      runningDeployments: "Ejecutándose",
      stoppedDeployments: "Detenidos",
      failedDeployments: "Fallidos",
      search: "Buscar deployments...",
      type: "Tipo",
      status: "Estado",
      newDeployment: "Nuevo Deployment",
      noDeployments: "No se encontraron deployments",
      noDescription: "Sin descripción",
      start: "Iniciar",
      stop: "Detener",
      createDeployment: "Crear Nuevo Deployment",
      editDeployment: "Editar Deployment",
      deploymentDetails: "Detalles del Deployment",
      quick: {
        title: "Acciones Rápidas",
        newDeployment: "Nuevo Deployment",
        importDeployment: "Importar Deployment",
        bulkConfig: "Config. Masiva",
        analytics: "Analytics",
      },
      tabs: {
        all: "Todos",
        pending: "Pendientes",
        running: "Ejecutándose",
        stopped: "Detenidos",
        failed: "Fallidos",
      },
    },

    // RAG Module
    rag: {
      dashboard: {
        title: "RAG Dashboard",
        subtitle: "Retrieval Augmented Generation System",
        totalProjects: "Total Projects",
        totalChunks: "Total Chunks",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        activeProjects: "Active Projects",
        vsLastMonth: "vs last month",
        projectsOverview: "Projects Overview",
        viewAll: "View All",
        chunks: "chunks",
        embeddings: "embeddings",
        searches: "searches",
        models: "Models",
        recentActivity: "Recent Activity",
        modelUsage: "Model Usage",
        usage: "Usage",
      },
      project: {
        createTitle: "Create RAG Project",
        createSubtitle: "Configure a new RAG project",
        editTitle: "Edit RAG Project",
        basicInfo: "Basic Information",
        name: "Project Name",
        namePlaceholder: "Enter project name",
        description: "Description",
        descriptionPlaceholder: "Describe the purpose of the RAG project",
        isActive: "Active Project",
        embeddingModel: "Embedding Model",
        modelConfiguration: "Model Configuration",
        rerankerModel: "Reranker Model",
        llmModel: "LLM Model",
        ragConfiguration: "RAG Configuration",
        maxChunkSize: "Maximum Chunk Size",
        chunkOverlap: "Chunk Overlap",
        semanticChunking: "Semantic Chunking",
        statistics: "Project Statistics",
        documents: "Documents",
        chunks: "Chunks",
        embeddings: "Embeddings",
        searches: "Searches",
        avgResponseTime: "Average Time",
        successRate: "Success Rate",
        assignments: "Assignments",
        documentAssignments: "Assigned Documents",
        webscrapingAssignments: "Assigned Web Scraping",
        datasourceAssignments: "Assigned Data Sources",
        lekaServerConfig: "Leka Server Configuration",
        lekaEndpoint: "Leka Server Endpoint",
        lekaApiKey: "Leka Server API Key",
        lekaTimeout: "Timeout (ms)",
      },
      help: {
        dashboard: {
          title: "RAG Dashboard",
          content:
            "The RAG Dashboard allows you to monitor the performance of your Retrieval Augmented Generation projects, including usage statistics, active projects, and recent activity.",
        },
        projects: {
          title: "RAG Projects",
          content:
            "<h4>What are RAG Projects?</h4><p>RAG (Retrieval Augmented Generation) projects are intelligent systems that combine information retrieval with text generation. They allow you to create chatbots and assistants that can answer questions based on your own documents and data.</p><h4>What are they used for?</h4><ul><li><strong>Specialized Chatbots:</strong> Create assistants that know your specific documentation</li><li><strong>Intelligent Search:</strong> Find relevant information in large volumes of documents</li><li><strong>Contextualized Responses:</strong> Generate accurate responses based on your content</li><li><strong>Support Automation:</strong> Reduce the workload of your support team</li><li><strong>Interactive Knowledge Base:</strong> Convert static documents into a conversational experience</li></ul><h4>Project Management</h4><p>From this page you can:</p><ul><li>Create new RAG projects with custom configurations</li><li>Edit AI models (embedding, reranker, LLM) for each project</li><li>Monitor performance and usage statistics</li><li>Manage data sources assigned to each project</li><li>Configure chunking and processing parameters</li></ul>",
        },
        createProject: {
          title: "Create RAG Project",
          content:
            "Create a new RAG project by configuring embedding, reranker, and LLM models, as well as chunking parameters and Leka server configuration.",
        },
        editProject: {
          title: "Edit RAG Project",
          content:
            "Edit your RAG project configuration, including models, chunking parameters, Leka server configuration, and monitor project statistics.",
        },
        models: {
          title: "RAG Models",
          content:
            "Manage available AI models for your RAG projects, including embedding, reranker, and LLM models from different providers.",
        },
        createModel: {
          title: "Create RAG Model",
          content:
            "<h4>AI Model Configuration</h4><p>This page allows you to create and configure new AI models for your RAG projects. You can configure:</p><ul><li><strong>Basic Information:</strong> Name, type (Embedding, Reranker, LLM), provider and version</li><li><strong>Technical Configuration:</strong> Endpoint, API Key, cost per token, maximum tokens and context window</li><li><strong>Advanced Options:</strong> Local vs remote model, active/inactive status</li></ul><p><strong>Model Types:</strong></p><ul><li><strong>Embedding:</strong> Converts text into numerical vectors for semantic search</li><li><strong>Reranker:</strong> Improves the relevance of search results</li><li><strong>LLM:</strong> Generates responses based on retrieved context</li></ul><p>Once configured, you can test the model before saving it to ensure it works correctly.</p>",
        },
        editModel: {
          title: "Edit RAG Model",
          content:
            "Edit the configuration of an existing AI model, including performance parameters, cost, and endpoint configuration.",
        },
        chat: {
          title: "RAG Chat",
          content:
            "Interact with your RAG projects through chat. Select a project and ask questions to get answers based on your documents.",
        },
        reranker: {
          title: "RAG Reranker",
          content:
            "Configure and manage search result reranking to improve the relevance of responses in your RAG projects.",
        },
        chunks: {
          title: "Document Chunks",
          content:
            "Chunks are processed document fragments that enable more efficient search. Auto-tags are generated automatically based on content, while manual tags can be added, edited, or removed to improve categorization and retrieval of specific information.",
        },
        search: {
          title: "Semantic Search",
          content:
            "Perform advanced semantic searches in your RAG documents with different search strategies and filters.",
        },
        searchTypes: {
          vector: "Vector",
          textual: "Textual",
          hybrid: "Hybrid",
          filtered: "Filtered",
        },
        analytics: {
          title: "RAG Analytics",
          content:
            "Monitor the performance and metrics of your RAG projects, including usage statistics, response times, and result quality.",
        },
      },
      reranker: {
        title: "Reranker",
        subtitle: "Improve search result relevance",
        configuration: "Reranker Configuration",
        project: "RAG Project",
        selectProject: "Select project",
        model: "Reranking Model",
        selectModel: "Select model",
        topK: "Top K Results",
        threshold: "Relevance Threshold",
        testQuery: "Query Test",
        query: "Test Query",
        queryPlaceholder: "Enter a query to test reranking...",
        runTest: "Run Test",
        testHistory: "Test History",
      },
      chunks: {
        title: "Chunks",
        subtitle: "Visualize and manage document chunks",
        project: "RAG Project",
        selectProject: "Select project",
        chunkType: "Chunk Type",
        selectType: "Select type",
        searchChunks: "Search Chunks",
        searchPlaceholder: "Search by content or tags...",
        totalChunks: "Total Chunks",
        avgSize: "Average Size",
        autoTags: "Auto Tags",
        viewDetails: "View Details",
        filters: "Filters",
        manualTags: "Manual Tags",
        newTag: "New tag...",
        addTag: "Add Tag",
        editTags: "Edit Tags",
        removeTag: "Remove Tag",
        source: "Source",
        relevance: "Relevance",
        characters: "characters",
        createdAt: "Created",
        saveChanges: "Save Changes",
        cancel: "Cancel",
      },
      search: {
        title: "Semantic Search",
        subtitle: "Advanced search in RAG documents",
        project: "RAG Project",
        selectProject: "Select project",
        searchQuery: "Search Query",
        queryPlaceholder: "Enter your search query...",
        searchType: "Search Type",
        selectType: "Select type",
        executeSearch: "Execute Search",
        searchHistory: "Search History",
        results: "Results",
        relevance: "Relevance",
        source: "Source",
      },
      analytics: {
        title: "Analytics",
        subtitle: "RAG project metrics and performance",
        project: "RAG Project",
        selectProject: "Select project",
        timeRange: "Time Range",
        selectRange: "Select range",
        refresh: "Refresh",
        totalSearches: "Total Searches",
        avgResponseTime: "Average Response Time",
        successRate: "Success Rate",
        userSatisfaction: "User Satisfaction",
        topQueries: "Top Queries",
        performanceMetrics: "Performance Metrics",
        usageStats: "Usage Statistics",
      },
    },

    // Technology Management
    technology: {
      create: {
        title: "Crear Nueva Tecnología",
        content:
          "Esta página te permite registrar una nueva tecnología en el catálogo del sistema. Puedes especificar información básica como nombre, categoría, tipo, versiones, URLs de documentación y repositorio, así como configurar requisitos de infraestructura y opciones de integración con RAG, entrenamiento y evaluación automática.",
      },
              help: {
          create: {
            title: "Crear Tecnología",
            content:
              "Registra nuevas tecnologías en el catálogo para que estén disponibles en el sistema. Incluye información detallada sobre versiones, documentación y requisitos técnicos.",
          },
        },
        catalog: {
          title: "Catálogo de Tecnologías",
          content:
            "Explora y gestiona todas las tecnologías disponibles en el sistema. Puedes buscar, filtrar, descargar y agregar tecnologías a tus stacks. Utiliza los filtros para encontrar tecnologías específicas por categoría, popularidad o nombre.",
        },
    },

    // Core Module
    core: {
      departments: {
        title: "Gestión de Departamentos",
        description: "Gestiona departamentos, equipos y recursos organizacionales",
        register: "Registrar Departamento",
        list: "Lista de Departamentos",
        new: "Nuevo Departamento",
        edit: "Editar Departamento",
        detailDescription: "Gestión y configuración de Department",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Nombre",
        code: "Código",
        managerid: "ID Manager",
        parentid: "ID Departamento Padre",
        isactive: "Activo",
        description: "Descripción",
      },
      users: {
        title: "Gestión de Usuarios",
        description: "Gestiona usuarios del sistema",
        register: "Registrar Usuario",
        list: "Lista de Usuarios",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "Resumen general del sistema administrativo",
        activeItems: "Items Activos",
        deployedItems: "Items Desplegados",
        trainingItems: "Items en Entrenamiento",
        offlineItems: "Items Offline",
      },
    },
  },
  en: {
    // ... existing translations ...
    // Core Module
    core: {
      departments: {
        title: "Department Management",
        description: "Manage departments, teams and organizational resources",
        register: "Register Department",
        list: "Department List",
        new: "New Department",
        edit: "Edit Department",
        detailDescription: "Department management and configuration",
        createdAt: "Created At",
        updatedAt: "Updated At",
        corname: "Name",
        code: "Code",
        managerid: "Manager ID",
        parentid: "Parent Department ID",
        isactive: "Active",
        description: "Description",
      },
      users: {
        title: "User Management",
        description: "Manage system users",
        register: "Register User",
        list: "User List",
      },
      dashboard: {
        title: "Admin Dashboard Summary",
        description: "General administrative system summary",
        activeItems: "Active Items",
        deployedItems: "Deployed Items",
        trainingItems: "Training Items",
        offlineItems: "Offline Items",
      },
    },
  },
};

// Hook para usar traducciones
export function useTranslation() {
  const [mounted, setMounted] = useState(false);
  const [language, setLanguageState] = useState<Language>("es");

  // Solo ejecutar en el cliente después de la hidratación
  useEffect(() => {
    setMounted(true);
    const savedLanguage =
      (localStorage.getItem("codeflowx-language") as Language) || "es";
    setLanguageState(savedLanguage);
  }, []);

  const getLanguage = (): Language => {
    if (typeof window !== "undefined") {
      return (localStorage.getItem("codeflowx-language") as Language) || "es";
    }
    return "es";
  };

  const setLanguage = (language: Language) => {
    if (typeof window !== "undefined") {
      localStorage.setItem("codeflowx-language", language);
      setLanguageState(language);
      // Recargar la página para aplicar el idioma
      window.location.reload();
    }
  };

  const t = (
    key: string,
    fallback?: string,
    params?: Record<string, string>
  ): string => {
    // Durante SSR o antes de la hidratación, siempre usar español
    if (!mounted) {
      const keys = key.split(".");
      let translation = translations["es"];
      for (const k of keys) {
        translation = translation?.[k];
        if (!translation) break;
      }
      // Asegurar que devolvemos una cadena
      return typeof translation === "string" ? translation : fallback || key;
    }

    const currentLanguage = getLanguage();
    const keys = key.split(".");

    // Buscar en el idioma actual
    let translation = translations[currentLanguage];
    for (const k of keys) {
      translation = translation?.[k];
      if (!translation) break;
    }

    // Si no se encuentra, buscar en español como fallback
    let fallbackTranslation = translations["es"];
    for (const k of keys) {
      fallbackTranslation = fallbackTranslation?.[k];
      if (!fallbackTranslation) break;
    }

    // Asegurar que devolvemos una cadena
    let finalTranslation =
      translation || fallbackTranslation || fallback || key;

    // Si finalTranslation no es una cadena, usar el fallback o la clave
    if (typeof finalTranslation !== "string") {
      finalTranslation = fallback || key;
    }

    // Reemplazar parámetros si existen
    if (params) {
      return finalTranslation.replace(/\{(\w+)\}/g, (match, param) => {
        return params[param] || match;
      });
    }

    return finalTranslation;
  };

  return {
    language: mounted ? language : "es",
    setLanguage,
    t,
    mounted,
    availableLanguages: ["es", "en"],
  };
}

```

Verificando errores de lint en los archivos creados:


Sin errores de linting. Resumen de lo realizado:

## Resumen de la migración

Migración de las pantallas de Infrastructure según los prompts. Completado:

### Páginas creadas

1. Dashboard:
   - `resource-utilization-dashboard/page.tsx` — Dashboard de utilización de recursos con métricas, gráficos y tabla de recursos

2. Details:
   - `cloud-resource-detail/page.tsx` — Detalle de recurso cloud con información, especificaciones y configuración de escalado

3. Overviews:
   - `cloud-resource-overview/page.tsx` — Vista general de recursos cloud con búsqueda y filtros

### Configuración actualizada

1. Menú (`app/config/modules.ts`):
   - Añadidas 24 nuevas entradas al menú de Infrastructure
   - Incluye todas las rutas de dashboard, details y overviews
   - Roles actualizados: `["admin", "devops", "architect", "it", "oem"]`

2. Traducciones (`app/config/i18n.ts`):
   - Añadidas traducciones en español e inglés para todas las nuevas páginas
   - Incluye claves para: dashboard, detalles, overviews, métricas, costos, etc.

### Estilo "Wow Factor"

- Gradientes sutiles en backgrounds
- Partículas flotantes animadas
- Glassmorphism en cards y banners
- Gradientes en títulos
- Animaciones suaves
- Cards con efectos hover
- Iconos de Lucide React

### Próximos pasos

Quedan 27 páginas por crear siguiendo el mismo patrón:
- 11 detalles más (cloud-credential, cloud-provider, cloud-region, gpu-instance, etc.)
- 16 overviews más (capacity-forecast, cloud-credential-overview, etc.)

Todas las rutas ya están en el menú y las traducciones están listas. Las páginas restantes pueden crearse siguiendo el patrón de las páginas de ejemplo creadas.

¿Quieres que continúe creando más páginas ahora o prefieres revisar primero las creadas?

---

**User**

crealas todas y luego las revisamos

---

**Cursor**

Creando todas las páginas restantes. Empezando por los detalles y luego los overviews.


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Edit,
  Key,
  Lock,
  Shield,
  User,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface CloudCredentialDetail {
  id: number;
  cloudProviderId: number;
  credentialName: string;
  credentialType: "API_KEY" | "OAUTH" | "SERVICE_ACCOUNT" | "IAM_ROLE";
  accessKey?: string;
  secretKey?: string;
  token?: string;
  isEncrypted: boolean;
  expiresAt?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

const mockCredential: CloudCredentialDetail = {
  id: 1,
  cloudProviderId: 1,
  credentialName: "AWS Production Credentials",
  credentialType: "API_KEY",
  isEncrypted: true,
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function CloudCredentialDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const credential = mockCredential;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/credentials">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Key className="w-8 h-8 text-primary" />
                {credential.credentialName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={credential.isActive ? "default" : "secondary"}>
              {credential.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <User className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.credentialType", "Credential Type")}
                    </label>
                    <p className="text-foreground font-medium">{credential.credentialType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{credential.cloudProvider}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.encrypted", "Encrypted")}
                    </label>
                    <p className="text-foreground font-medium">
                      {credential.isEncrypted ? t("common.yes", "Yes") : t("common.no", "No")}
                    </p>
                  </div>
                  {credential.expiresAt && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.expiresAt", "Expires At")}
                      </label>
                      <p className="text-foreground font-medium">
                        {new Date(credential.expiresAt).toLocaleString()}
                      </p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Shield className="w-5 h-5 text-primary" />
                  {t("infrastructure.security", "Security")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="flex items-center gap-2">
                  <Lock className="w-4 h-4 text-muted-foreground" />
                  <span className="text-sm text-foreground">
                    {credential.isEncrypted ? "Encrypted" : "Not Encrypted"}
                  </span>
                </div>
              </CardContent>
            </Card>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Key className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(credential.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(credential.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Cloud,
  DollarSign,
  Edit,
  Server,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface CloudProviderDetail {
  id: number;
  providerName: string;
  providerType: "AWS" | "GCP" | "AZURE" | "OVH" | "RUNPOD" | "VAST_AI";
  region?: string;
  accountId?: string;
  isActive: boolean;
  costPerMonth?: number;
  createdAt: string;
  updatedAt: string;
}

const mockProvider: CloudProviderDetail = {
  id: 1,
  providerName: "AWS Production",
  providerType: "AWS",
  region: "us-east-1",
  accountId: "123456789012",
  isActive: true,
  costPerMonth: 2847.32,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
};

export default function CloudProviderDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const provider = mockProvider;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/providers">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Cloud className="w-8 h-8 text-primary" />
                {provider.providerName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={provider.isActive ? "default" : "secondary"}>
              {provider.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Cloud className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.providerType", "Provider Type")}
                    </label>
                    <p className="text-foreground font-medium">{provider.providerType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.region", "Region")}
                    </label>
                    <p className="text-foreground font-medium">{provider.region || "N/A"}</p>
                  </div>
                  {provider.accountId && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.accountId", "Account ID")}
                      </label>
                      <p className="text-foreground font-medium">{provider.accountId}</p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            {provider.costPerMonth && (
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <DollarSign className="w-5 h-5 text-primary" />
                    {t("infrastructure.cost", "Cost")}
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerMonth", "Cost per Month")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${provider.costPerMonth.toFixed(2)}
                    </p>
                  </div>
                </CardContent>
              </Card>
            )}

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Server className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(provider.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(provider.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Edit,
  Globe,
  MapPin,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface CloudRegionDetail {
  id: number;
  cloudProviderId: number;
  regionName: string;
  regionCode: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

const mockRegion: CloudRegionDetail = {
  id: 1,
  cloudProviderId: 1,
  regionName: "US East (N. Virginia)",
  regionCode: "us-east-1",
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function CloudRegionDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const region = mockRegion;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/cloud-region-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Globe className="w-8 h-8 text-primary" />
                {region.regionName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={region.isActive ? "default" : "secondary"}>
              {region.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <MapPin className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.regionName", "Region Name")}
                    </label>
                    <p className="text-foreground font-medium">{region.regionName}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.regionCode", "Region Code")}
                    </label>
                    <p className="text-foreground font-medium">{region.regionCode}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{region.cloudProvider}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Globe className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(region.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(region.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Cpu,
  DollarSign,
  Edit,
  HardDrive,
  MemoryStick,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface GpuInstanceDetail {
  id: number;
  instanceName: string;
  gpuType: string;
  gpuCount: number;
  cloudProviderId: number;
  region?: string;
  status: "RUNNING" | "STOPPED" | "PENDING" | "ERROR";
  costPerHour?: number;
  costPerMonth?: number;
  memoryGB: number;
  storageGB: number;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

const mockGpuInstance: GpuInstanceDetail = {
  id: 1,
  instanceName: "Training GPU Cluster",
  gpuType: "A100",
  gpuCount: 4,
  cloudProviderId: 1,
  region: "us-east-1",
  status: "RUNNING",
  costPerHour: 12.5,
  costPerMonth: 9000,
  memoryGB: 512,
  storageGB: 2000,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function GpuInstanceDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const instance = mockGpuInstance;

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      RUNNING: "bg-green-500/20 text-green-500 border-green-500/50",
      STOPPED: "bg-gray-500/20 text-gray-500 border-gray-500/50",
      PENDING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      ERROR: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/gpu-instances">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Cpu className="w-8 h-8 text-primary" />
                {instance.instanceName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge className={getStatusBadge(instance.status)}>
              {instance.status}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Cpu className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.gpuType", "GPU Type")}
                    </label>
                    <p className="text-foreground font-medium">{instance.gpuType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.gpuCount", "GPU Count")}
                    </label>
                    <p className="text-foreground font-medium">{instance.gpuCount}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{instance.cloudProvider}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.region", "Region")}
                    </label>
                    <p className="text-foreground font-medium">{instance.region || "N/A"}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <MemoryStick className="w-5 h-5 text-primary" />
                  {t("infrastructure.specifications", "Specifications")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.memory", "Memory")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{instance.memoryGB} GB</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.storage", "Storage")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{instance.storageGB} GB</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            {instance.costPerHour && (
              <Card className="backdrop-blur-md bg-background/60 border-border/50">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <DollarSign className="w-5 h-5 text-primary" />
                    {t("infrastructure.cost", "Cost")}
                  </CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                  {instance.costPerHour && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.costPerHour", "Cost per Hour")}
                      </label>
                      <p className="text-2xl font-bold text-foreground">
                        ${instance.costPerHour.toFixed(2)}
                      </p>
                    </div>
                  )}
                  {instance.costPerMonth && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.costPerMonth", "Cost per Month")}
                      </label>
                      <p className="text-2xl font-bold text-foreground">
                        ${instance.costPerMonth.toFixed(2)}
                      </p>
                    </div>
                  )}
                </CardContent>
              </Card>
            )}

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <HardDrive className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(instance.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(instance.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Edit,
  Server,
  Users,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface KubernetesClusterDetail {
  id: number;
  clusterName: string;
  cloudProviderId: number;
  region?: string;
  status: "RUNNING" | "STOPPED" | "PENDING" | "ERROR";
  nodeCount: number;
  podCount: number;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
}

const mockCluster: KubernetesClusterDetail = {
  id: 1,
  clusterName: "Production Cluster",
  cloudProviderId: 1,
  region: "us-east-1",
  status: "RUNNING",
  nodeCount: 5,
  podCount: 24,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  cloudProvider: "AWS",
};

export default function KubernetesClusterDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const cluster = mockCluster;

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      RUNNING: "bg-green-500/20 text-green-500 border-green-500/50",
      STOPPED: "bg-gray-500/20 text-gray-500 border-gray-500/50",
      PENDING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      ERROR: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/kubernetes">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Server className="w-8 h-8 text-primary" />
                {cluster.clusterName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge className={getStatusBadge(cluster.status)}>
              {cluster.status}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Server className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cloudProvider", "Cloud Provider")}
                    </label>
                    <p className="text-foreground font-medium">{cluster.cloudProvider}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.region", "Region")}
                    </label>
                    <p className="text-foreground font-medium">{cluster.region || "N/A"}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.nodes", "Nodes")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{cluster.nodeCount}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.pods", "Pods")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{cluster.podCount}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Users className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cluster.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cluster.updatedAt).toLocaleString()}
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

```

Continuando con las páginas de detalles restantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Cpu,
  Edit,
  HardDrive,
  MemoryStick,
  Server,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface KubernetesNodeDetail {
  id: number;
  nodeName: string;
  kubernetesClusterId: number;
  status: "READY" | "NOT_READY" | "UNKNOWN";
  cpuCores: number;
  memoryGB: number;
  storageGB: number;
  podCount: number;
  createdAt: string;
  updatedAt: string;
  clusterName: string;
}

const mockNode: KubernetesNodeDetail = {
  id: 1,
  nodeName: "node-001",
  kubernetesClusterId: 1,
  status: "READY",
  cpuCores: 8,
  memoryGB: 32,
  storageGB: 500,
  podCount: 5,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  clusterName: "Production Cluster",
};

export default function KubernetesNodeDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const node = mockNode;

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      READY: "bg-green-500/20 text-green-500 border-green-500/50",
      NOT_READY: "bg-red-500/20 text-red-500 border-red-500/50",
      UNKNOWN: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/kubernetes-node-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Server className="w-8 h-8 text-primary" />
                {node.nodeName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge className={getStatusBadge(node.status)}>
              {node.status}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Server className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.clusterName", "Cluster Name")}
                    </label>
                    <p className="text-foreground font-medium">{node.clusterName}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.pods", "Pods")}
                    </label>
                    <p className="text-foreground font-medium">{node.podCount}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Cpu className="w-5 h-5 text-primary" />
                  {t("infrastructure.specifications", "Specifications")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.cpu", "CPU")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{node.cpuCores} cores</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.memory", "Memory")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{node.memoryGB} GB</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.storage", "Storage")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">{node.storageGB} GB</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <HardDrive className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(node.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(node.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  DollarSign,
  Edit,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface InfrastructureCostDetail {
  id: number;
  resourceId: number;
  resourceType: string;
  costPerHour: number;
  costPerDay: number;
  costPerMonth: number;
  period: string;
  createdAt: string;
  updatedAt: string;
  resourceName: string;
}

const mockCost: InfrastructureCostDetail = {
  id: 1,
  resourceId: 1,
  resourceType: "LOAD_BALANCER",
  costPerHour: 0.0225,
  costPerDay: 0.54,
  costPerMonth: 16.2,
  period: "2024-01",
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  resourceName: "Production Load Balancer",
};

export default function InfrastructureCostDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const cost = mockCost;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/infrastructure-cost-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <DollarSign className="w-8 h-8 text-primary" />
                {cost.resourceName} - {t("infrastructure.cost", "Cost")}
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
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="w-5 h-5 text-primary" />
                  {t("infrastructure.costDetails", "Cost Details")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerHour", "Cost per Hour")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerHour.toFixed(4)}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerDay", "Cost per Day")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerDay.toFixed(2)}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerMonth", "Cost per Month")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerMonth.toFixed(2)}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-primary" />
                  {t("infrastructure.resourceInformation", "Resource Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{cost.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.period", "Period")}
                    </label>
                    <p className="text-foreground font-medium">{cost.period}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cost.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cost.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Activity,
  Edit,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
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

export default function InfrastructureMetricDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const metric = mockMetric;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/infrastructure-metric-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
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
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
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
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Edit,
  FileText,
  Settings,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface InfrastructureTemplateDetail {
  id: number;
  templateName: string;
  templateType: string;
  templateContent: string;
  description?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

const mockTemplate: InfrastructureTemplateDetail = {
  id: 1,
  templateName: "Kubernetes Cluster Template",
  templateType: "KUBERNETES",
  templateContent: JSON.stringify({
    apiVersion: "v1",
    kind: "Deployment",
    metadata: { name: "example" },
    spec: { replicas: 3 },
  }, null, 2),
  description: "Standard Kubernetes cluster deployment template",
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
};

export default function InfrastructureTemplateDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const template = mockTemplate;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/infrastructure-template-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <FileText className="w-8 h-8 text-primary" />
                {template.templateName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={template.isActive ? "default" : "secondary"}>
              {template.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.templateType", "Template Type")}
                    </label>
                    <p className="text-foreground font-medium">{template.templateType}</p>
                  </div>
                  {template.description && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.description", "Description")}
                      </label>
                      <p className="text-foreground font-medium">{template.description}</p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="w-5 h-5 text-primary" />
                  {t("infrastructure.templateContent", "Template Content")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <pre className="text-sm bg-muted/50 p-4 rounded-lg overflow-x-auto">
                  {template.templateContent}
                </pre>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(template.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(template.updatedAt).toLocaleString()}
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  FileText,
  User,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface InfrastructureAuditDetail {
  id: number;
  resourceId: number;
  resourceType: string;
  action: string;
  userId: number;
  userName: string;
  timestamp: string;
  details: string;
  createdAt: string;
  resourceName: string;
}

const mockAudit: InfrastructureAuditDetail = {
  id: 1,
  resourceId: 1,
  resourceType: "LOAD_BALANCER",
  action: "UPDATE",
  userId: 1,
  userName: "admin@example.com",
  timestamp: "2024-01-15T10:00:00Z",
  details: "Updated load balancer configuration",
  createdAt: "2024-01-15T10:00:00Z",
  resourceName: "Production Load Balancer",
};

export default function InfrastructureAuditDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const audit = mockAudit;

  const getActionBadge = (action: string) => {
    const colors: Record<string, string> = {
      CREATE: "bg-green-500/20 text-green-500 border-green-500/50",
      UPDATE: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      DELETE: "bg-red-500/20 text-red-500 border-red-500/50",
      VIEW: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[action] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/infrastructure-audit-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <FileText className="w-8 h-8 text-primary" />
                {t("infrastructure.auditRecord", "Audit Record")} #{audit.id}
              </h1>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.auditDetails", "Audit Details")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.action", "Action")}
                    </label>
                    <Badge className={getActionBadge(audit.action)}>
                      {audit.action}
                    </Badge>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{audit.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceName", "Resource Name")}
                    </label>
                    <p className="text-foreground font-medium">{audit.resourceName}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.timestamp", "Timestamp")}
                    </label>
                    <p className="text-foreground font-medium">
                      {new Date(audit.timestamp).toLocaleString()}
                    </p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.details", "Details")}
                    </label>
                    <p className="text-foreground font-medium">{audit.details}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <User className="w-5 h-5 text-primary" />
                  {t("infrastructure.userInformation", "User Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.userName", "User Name")}
                  </label>
                  <p className="text-sm text-foreground">{audit.userName}</p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.userId", "User ID")}
                  </label>
                  <p className="text-sm text-foreground">{audit.userId}</p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  ArrowLeft,
  Edit,
  Gauge,
  Settings,
} from "lucide-react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

interface ResourceQuotaDetail {
  id: number;
  resourceType: string;
  quotaName: string;
  limit: number;
  used: number;
  unit: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

const mockQuota: ResourceQuotaDetail = {
  id: 1,
  resourceType: "CPU",
  quotaName: "CPU Quota",
  limit: 100,
  used: 45,
  unit: "cores",
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
};

export default function ResourceQuotaDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const quota = mockQuota;

  const usagePercentage = (quota.used / quota.limit) * 100;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/infrastructure/resource-quota-overview">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </Link>
            <div>
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent flex items-center gap-3">
                <Gauge className="w-8 h-8 text-primary" />
                {quota.quotaName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={quota.isActive ? "default" : "secondary"}>
              {quota.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Gauge className="w-5 h-5 text-primary" />
                  {t("infrastructure.quotaDetails", "Quota Details")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{quota.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.unit", "Unit")}
                    </label>
                    <p className="text-foreground font-medium">{quota.unit}</p>
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="text-muted-foreground">{t("infrastructure.used", "Used")}</span>
                    <span className="text-foreground font-medium">
                      {quota.used} / {quota.limit} {quota.unit}
                    </span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-4">
                    <div
                      className={`h-4 rounded-full transition-all duration-500 ${
                        usagePercentage >= 80
                          ? "bg-red-500"
                          : usagePercentage >= 60
                          ? "bg-yellow-500"
                          : "bg-green-500"
                      }`}
                      style={{ width: `${usagePercentage}%` }}
                    />
                  </div>
                  <div className="text-sm text-muted-foreground text-right">
                    {usagePercentage.toFixed(1)}% {t("infrastructure.used", "used")}
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="backdrop-blur-md bg-background/60 border-border/50">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(quota.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(quota.updatedAt).toLocaleString()}
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

```

Detalles completados. Creando los overviews restantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Cloud,
  Edit,
  Eye,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface CloudProvider {
  id: number;
  providerName: string;
  providerType: string;
  region?: string;
  isActive: boolean;
  costPerMonth?: number;
}

const mockProviders: CloudProvider[] = [
  {
    id: 1,
    providerName: "AWS Production",
    providerType: "AWS",
    region: "us-east-1",
    isActive: true,
    costPerMonth: 2847.32,
  },
  {
    id: 2,
    providerName: "Google Cloud",
    providerType: "GCP",
    region: "europe-west1",
    isActive: true,
    costPerMonth: 1456.78,
  },
  {
    id: 3,
    providerName: "Azure",
    providerType: "AZURE",
    region: "West Europe",
    isActive: true,
    costPerMonth: 987.45,
  },
];

export default function CloudProviderOverviewPage() {
  const { t } = useTranslation();
  const [providers, setProviders] = useState<CloudProvider[]>(mockProviders);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredProviders = providers.filter((provider) =>
    provider.providerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    provider.providerType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Cloud className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.cloudProviderOverview", "Cloud Provider Overview")}
            </h1>
          </div>
          <Link href="/infrastructure/providers/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Provider")}
            </Button>
          </Link>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search providers...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProviders.map((provider) => (
            <Card
              key={provider.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{provider.providerName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{provider.providerType}</Badge>
                  <Badge variant={provider.isActive ? "default" : "secondary"}>
                    {provider.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  {provider.region && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.region", "Region")}</span>
                      <span className="text-foreground font-medium">{provider.region}</span>
                    </div>
                  )}
                  {provider.costPerMonth && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                      <span className="text-foreground font-medium">${provider.costPerMonth.toFixed(2)}</span>
                    </div>
                  )}
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/cloud-provider-detail?id=${provider.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredProviders.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Cloud className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noProvidersFound", "No providers found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Key,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface CloudCredential {
  id: number;
  credentialName: string;
  credentialType: string;
  cloudProvider: string;
  isActive: boolean;
}

const mockCredentials: CloudCredential[] = [
  {
    id: 1,
    credentialName: "AWS Production Credentials",
    credentialType: "API_KEY",
    cloudProvider: "AWS",
    isActive: true,
  },
  {
    id: 2,
    credentialName: "GCP Service Account",
    credentialType: "SERVICE_ACCOUNT",
    cloudProvider: "GCP",
    isActive: true,
  },
];

export default function CloudCredentialOverviewPage() {
  const { t } = useTranslation();
  const [credentials, setCredentials] = useState<CloudCredential[]>(mockCredentials);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredCredentials = credentials.filter((credential) =>
    credential.credentialName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    credential.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Key className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.cloudCredentialOverview", "Cloud Credential Overview")}
            </h1>
          </div>
          <Link href="/infrastructure/credentials/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Credential")}
            </Button>
          </Link>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search credentials...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCredentials.map((credential) => (
            <Card
              key={credential.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{credential.credentialName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{credential.credentialType}</Badge>
                  <Badge variant={credential.isActive ? "default" : "secondary"}>
                    {credential.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{credential.cloudProvider}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/cloud-credential-detail?id=${credential.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredCredentials.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Key className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noCredentialsFound", "No credentials found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Globe,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface CloudRegion {
  id: number;
  regionName: string;
  regionCode: string;
  cloudProvider: string;
  isActive: boolean;
}

const mockRegions: CloudRegion[] = [
  {
    id: 1,
    regionName: "US East (N. Virginia)",
    regionCode: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
  },
  {
    id: 2,
    regionName: "Europe West 1",
    regionCode: "europe-west1",
    cloudProvider: "GCP",
    isActive: true,
  },
  {
    id: 3,
    regionName: "West Europe",
    regionCode: "westeurope",
    cloudProvider: "AZURE",
    isActive: true,
  },
];

export default function CloudRegionOverviewPage() {
  const { t } = useTranslation();
  const [regions, setRegions] = useState<CloudRegion[]>(mockRegions);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredRegions = regions.filter((region) =>
    region.regionName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    region.regionCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
    region.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Globe className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.cloudRegionOverview", "Cloud Region Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Region")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search regions...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredRegions.map((region) => (
            <Card
              key={region.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{region.regionName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{region.regionCode}</Badge>
                  <Badge variant={region.isActive ? "default" : "secondary"}>
                    {region.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{region.cloudProvider}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/cloud-region-detail?id=${region.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredRegions.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Globe className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noRegionsFound", "No regions found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Continuando con los overviews restantes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Cpu,
  Edit,
  Eye,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface GpuInstance {
  id: number;
  instanceName: string;
  gpuType: string;
  gpuCount: number;
  cloudProvider: string;
  status: string;
  costPerMonth?: number;
}

const mockGpuInstances: GpuInstance[] = [
  {
    id: 1,
    instanceName: "Training GPU Cluster",
    gpuType: "A100",
    gpuCount: 4,
    cloudProvider: "AWS",
    status: "RUNNING",
    costPerMonth: 9000,
  },
  {
    id: 2,
    instanceName: "Inference GPU",
    gpuType: "T4",
    gpuCount: 2,
    cloudProvider: "GCP",
    status: "RUNNING",
    costPerMonth: 1200,
  },
];

export default function GpuInstanceOverviewPage() {
  const { t } = useTranslation();
  const [instances, setInstances] = useState<GpuInstance[]>(mockGpuInstances);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredInstances = instances.filter((instance) =>
    instance.instanceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    instance.gpuType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      RUNNING: "bg-green-500/20 text-green-500 border-green-500/50",
      STOPPED: "bg-gray-500/20 text-gray-500 border-gray-500/50",
      PENDING: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Cpu className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.gpuInstanceOverview", "GPU Instance Overview")}
            </h1>
          </div>
          <Link href="/infrastructure/gpu-instances/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Instance")}
            </Button>
          </Link>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search instances...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredInstances.map((instance) => (
            <Card
              key={instance.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{instance.instanceName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{instance.gpuType} x{instance.gpuCount}</Badge>
                  <Badge className={getStatusBadge(instance.status)}>
                    {instance.status}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{instance.cloudProvider}</span>
                  </div>
                  {instance.costPerMonth && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                      <span className="text-foreground font-medium">${instance.costPerMonth.toFixed(2)}</span>
                    </div>
                  )}
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/gpu-instance-detail?id=${instance.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredInstances.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Cpu className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noInstancesFound", "No instances found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface KubernetesCluster {
  id: number;
  clusterName: string;
  cloudProvider: string;
  status: string;
  nodeCount: number;
  podCount: number;
}

const mockClusters: KubernetesCluster[] = [
  {
    id: 1,
    clusterName: "Production Cluster",
    cloudProvider: "AWS",
    status: "RUNNING",
    nodeCount: 5,
    podCount: 24,
  },
  {
    id: 2,
    clusterName: "Staging Cluster",
    cloudProvider: "GCP",
    status: "RUNNING",
    nodeCount: 3,
    podCount: 12,
  },
];

export default function KubernetesClusterOverviewPage() {
  const { t } = useTranslation();
  const [clusters, setClusters] = useState<KubernetesCluster[]>(mockClusters);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredClusters = clusters.filter((cluster) =>
    cluster.clusterName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cluster.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      RUNNING: "bg-green-500/20 text-green-500 border-green-500/50",
      STOPPED: "bg-gray-500/20 text-gray-500 border-gray-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.kubernetesClusterOverview", "Kubernetes Cluster Overview")}
            </h1>
          </div>
          <Link href="/infrastructure/kubernetes/new">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Cluster")}
            </Button>
          </Link>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search clusters...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredClusters.map((cluster) => (
            <Card
              key={cluster.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{cluster.clusterName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{cluster.cloudProvider}</Badge>
                  <Badge className={getStatusBadge(cluster.status)}>
                    {cluster.status}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.nodes", "Nodes")}</span>
                    <span className="text-foreground font-medium">{cluster.nodeCount}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.pods", "Pods")}</span>
                    <span className="text-foreground font-medium">{cluster.podCount}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/kubernetes-cluster-detail?id=${cluster.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredClusters.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Server className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noClustersFound", "No clusters found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface KubernetesNode {
  id: number;
  nodeName: string;
  clusterName: string;
  status: string;
  cpuCores: number;
  memoryGB: number;
  podCount: number;
}

const mockNodes: KubernetesNode[] = [
  {
    id: 1,
    nodeName: "node-001",
    clusterName: "Production Cluster",
    status: "READY",
    cpuCores: 8,
    memoryGB: 32,
    podCount: 5,
  },
  {
    id: 2,
    nodeName: "node-002",
    clusterName: "Production Cluster",
    status: "READY",
    cpuCores: 8,
    memoryGB: 32,
    podCount: 4,
  },
];

export default function KubernetesNodeOverviewPage() {
  const { t } = useTranslation();
  const [nodes, setNodes] = useState<KubernetesNode[]>(mockNodes);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredNodes = nodes.filter((node) =>
    node.nodeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    node.clusterName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      READY: "bg-green-500/20 text-green-500 border-green-500/50",
      NOT_READY: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.kubernetesNodeOverview", "Kubernetes Node Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Node")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search nodes...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredNodes.map((node) => (
            <Card
              key={node.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{node.nodeName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{node.clusterName}</Badge>
                  <Badge className={getStatusBadge(node.status)}>
                    {node.status}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cpu", "CPU")}</span>
                    <span className="text-foreground font-medium">{node.cpuCores} cores</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.memory", "Memory")}</span>
                    <span className="text-foreground font-medium">{node.memoryGB} GB</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.pods", "Pods")}</span>
                    <span className="text-foreground font-medium">{node.podCount}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/kubernetes-node-detail?id=${node.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredNodes.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Server className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noNodesFound", "No nodes found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Creando los overviews restantes en lotes:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  FileText,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface InfrastructureTemplate {
  id: number;
  templateName: string;
  templateType: string;
  description?: string;
  isActive: boolean;
}

const mockTemplates: InfrastructureTemplate[] = [
  {
    id: 1,
    templateName: "Kubernetes Cluster Template",
    templateType: "KUBERNETES",
    description: "Standard Kubernetes cluster deployment template",
    isActive: true,
  },
  {
    id: 2,
    templateName: "GPU Instance Template",
    templateType: "GPU",
    description: "Template for GPU instance deployment",
    isActive: true,
  },
];

export default function InfrastructureTemplateOverviewPage() {
  const { t } = useTranslation();
  const [templates, setTemplates] = useState<InfrastructureTemplate[]>(mockTemplates);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredTemplates = templates.filter((template) =>
    template.templateName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    template.templateType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <FileText className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.infrastructureTemplateOverview", "Infrastructure Template Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Template")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search templates...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredTemplates.map((template) => (
            <Card
              key={template.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{template.templateName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{template.templateType}</Badge>
                  <Badge variant={template.isActive ? "default" : "secondary"}>
                    {template.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {template.description && (
                  <p className="text-sm text-muted-foreground">{template.description}</p>
                )}
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/infrastructure-template-detail?id=${template.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredTemplates.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <FileText className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noTemplatesFound", "No templates found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Eye,
  Search,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
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

export default function InfrastructureMetricOverviewPage() {
  const { t } = useTranslation();
  const [metrics, setMetrics] = useState<InfrastructureMetric[]>(mockMetrics);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredMetrics = metrics.filter((metric) =>
    metric.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    metric.metricType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Activity className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.infrastructureMetricOverview", "Infrastructure Metric Overview")}
            </h1>
          </div>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
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
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
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
                <Link href={`/infrastructure/infrastructure-metric-detail?id=${metric.id}`}>
                  <Button variant="outline" size="sm" className="w-full">
                    <Eye className="w-4 h-4 mr-2" />
                    {t("common.details", "Details")}
                  </Button>
                </Link>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredMetrics.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
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
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <BarChart3 className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.infrastructureMetricsSummaryOverview", "Infrastructure Metrics Summary")}
          </h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  DollarSign,
  Eye,
  Search,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface InfrastructureCost {
  id: number;
  resourceName: string;
  resourceType: string;
  costPerMonth: number;
  period: string;
}

const mockCosts: InfrastructureCost[] = [
  {
    id: 1,
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    costPerMonth: 16.2,
    period: "2024-01",
  },
  {
    id: 2,
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    costPerMonth: 234.5,
    period: "2024-01",
  },
];

export default function InfrastructureCostOverviewPage() {
  const { t } = useTranslation();
  const [costs, setCosts] = useState<InfrastructureCost[]>(mockCosts);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredCosts = costs.filter((cost) =>
    cost.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cost.resourceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalCost = costs.reduce((sum, cost) => sum + cost.costPerMonth, 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <DollarSign className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.infrastructureCostOverview", "Infrastructure Cost Overview")}
            </h1>
          </div>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="flex items-center justify-between mb-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search costs...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex items-center gap-4 pt-4 border-t border-border">
              <div>
                <div className="text-sm text-muted-foreground">{t("infrastructure.totalCost", "Total Cost")}</div>
                <div className="text-3xl font-bold text-foreground flex items-center gap-2">
                  <TrendingUp className="w-6 h-6 text-primary" />
                  ${totalCost.toFixed(2)}/mo
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCosts.map((cost) => (
            <Card
              key={cost.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{cost.resourceName}</CardTitle>
                <Badge variant="outline" className="mt-2">{cost.resourceType}</Badge>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                    <span className="text-2xl font-bold text-foreground">
                      ${cost.costPerMonth.toFixed(2)}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-sm text-muted-foreground">
                    <span>{t("infrastructure.period", "Period")}</span>
                    <span>{cost.period}</span>
                  </div>
                </div>
                <Link href={`/infrastructure/infrastructure-cost-detail?id=${cost.id}`}>
                  <Button variant="outline" size="sm" className="w-full">
                    <Eye className="w-4 h-4 mr-2" />
                    {t("common.details", "Details")}
                  </Button>
                </Link>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredCosts.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <DollarSign className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noCostsFound", "No costs found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Creando los últimos overviews:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  DollarSign,
  TrendingDown,
  TrendingUp,
} from "lucide-react";

interface CostAnalysis {
  totalCost: number;
  costByProvider: { provider: string; cost: number }[];
  costByResourceType: { type: string; cost: number }[];
  trend: "up" | "down";
  percentageChange: number;
}

const mockAnalysis: CostAnalysis = {
  totalCost: 5289.45,
  costByProvider: [
    { provider: "AWS", cost: 2847.32 },
    { provider: "GCP", cost: 1456.78 },
    { provider: "AZURE", cost: 987.45 },
  ],
  costByResourceType: [
    { type: "DATABASE", cost: 234.5 },
    { type: "LOAD_BALANCER", cost: 16.2 },
    { type: "STORAGE", cost: 45.8 },
  ],
  trend: "down",
  percentageChange: 12.5,
};

export default function InfrastructureCostAnalysisOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <DollarSign className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.infrastructureCostAnalysisOverview", "Infrastructure Cost Analysis")}
          </h1>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <DollarSign className="w-5 h-5 text-primary" />
              {t("infrastructure.totalCost", "Total Cost")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-4xl font-bold text-foreground">
                ${mockAnalysis.totalCost.toFixed(2)}
              </div>
              <div className={`flex items-center gap-2 ${mockAnalysis.trend === "down" ? "text-green-500" : "text-red-500"}`}>
                {mockAnalysis.trend === "down" ? (
                  <TrendingDown className="w-5 h-5" />
                ) : (
                  <TrendingUp className="w-5 h-5" />
                )}
                <span className="text-lg font-medium">
                  {mockAnalysis.percentageChange}%
                </span>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>{t("infrastructure.costByProvider", "Cost by Provider")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {mockAnalysis.costByProvider.map((item, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-foreground font-medium">{item.provider}</span>
                    <span className="text-foreground font-bold">${item.cost.toFixed(2)}</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>{t("infrastructure.costByResourceType", "Cost by Resource Type")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {mockAnalysis.costByResourceType.map((item, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-foreground font-medium">{item.type}</span>
                    <span className="text-foreground font-bold">${item.cost.toFixed(2)}</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Eye,
  FileText,
  Search,
  User,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface InfrastructureAudit {
  id: number;
  resourceName: string;
  resourceType: string;
  action: string;
  userName: string;
  timestamp: string;
}

const mockAudits: InfrastructureAudit[] = [
  {
    id: 1,
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    action: "UPDATE",
    userName: "admin@example.com",
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    id: 2,
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    action: "CREATE",
    userName: "admin@example.com",
    timestamp: "2024-01-14T15:30:00Z",
  },
];

export default function InfrastructureAuditOverviewPage() {
  const { t } = useTranslation();
  const [audits, setAudits] = useState<InfrastructureAudit[]>(mockAudits);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredAudits = audits.filter((audit) =>
    audit.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    audit.action.toLowerCase().includes(searchTerm.toLowerCase()) ||
    audit.userName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getActionBadge = (action: string) => {
    const colors: Record<string, string> = {
      CREATE: "bg-green-500/20 text-green-500 border-green-500/50",
      UPDATE: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      DELETE: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[action] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <FileText className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.infrastructureAuditOverview", "Infrastructure Audit Overview")}
          </h1>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search audit logs...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="space-y-4">
          {filteredAudits.map((audit) => (
            <Card
              key={audit.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardContent className="pt-6">
                <div className="flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="text-lg font-semibold text-foreground">{audit.resourceName}</h3>
                      <Badge className={getActionBadge(audit.action)}>
                        {audit.action}
                      </Badge>
                      <Badge variant="outline">{audit.resourceType}</Badge>
                    </div>
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <div className="flex items-center gap-1">
                        <User className="w-4 h-4" />
                        {audit.userName}
                      </div>
                      <div>{new Date(audit.timestamp).toLocaleString()}</div>
                    </div>
                  </div>
                  <Link href={`/infrastructure/infrastructure-audit-detail?id=${audit.id}`}>
                    <Button variant="outline" size="sm">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredAudits.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <FileText className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noAuditsFound", "No audit logs found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
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
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <Heart className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.infrastructureHealthMatrixOverview", "Infrastructure Health Matrix")}
          </h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockHealthStatuses.map((health, index) => (
            <Card
              key={index}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
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
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <Server className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.infrastructureOverviewOverview", "Infrastructure Overview")}
          </h1>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
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

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import {
  TrendingUp,
} from "lucide-react";

interface CapacityForecast {
  currentUsage: number;
  forecastedUsage: number;
  growthRate: number;
  timeHorizon: string;
}

const mockForecast: CapacityForecast = {
  currentUsage: 75,
  forecastedUsage: 92,
  growthRate: 12.5,
  timeHorizon: "3 months",
};

export default function CapacityForecastOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center gap-3">
          <TrendingUp className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t("infrastructure.capacityForecastOverview", "Capacity Forecast")}
          </h1>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>{t("infrastructure.currentUsage", "Current Usage")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold text-foreground mb-2">
                {mockForecast.currentUsage}%
              </div>
              <div className="w-full bg-muted rounded-full h-4">
                <div
                  className="h-4 rounded-full bg-blue-500"
                  style={{ width: `${mockForecast.currentUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>{t("infrastructure.forecastedUsage", "Forecasted Usage")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold text-foreground mb-2">
                {mockForecast.forecastedUsage}%
              </div>
              <div className="w-full bg-muted rounded-full h-4">
                <div
                  className="h-4 rounded-full bg-yellow-500"
                  style={{ width: `${mockForecast.forecastedUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="w-5 h-5 text-primary" />
              {t("infrastructure.forecastDetails", "Forecast Details")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <div className="text-sm text-muted-foreground">
                  {t("infrastructure.growthRate", "Growth Rate")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {mockForecast.growthRate}%
                </div>
              </div>
              <div>
                <div className="text-sm text-muted-foreground">
                  {t("infrastructure.timeHorizon", "Time Horizon")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {mockForecast.timeHorizon}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

```

```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Gauge,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface ResourceQuota {
  id: number;
  quotaName: string;
  resourceType: string;
  limit: number;
  used: number;
  unit: string;
  isActive: boolean;
}

const mockQuotas: ResourceQuota[] = [
  {
    id: 1,
    quotaName: "CPU Quota",
    resourceType: "CPU",
    limit: 100,
    used: 45,
    unit: "cores",
    isActive: true,
  },
  {
    id: 2,
    quotaName: "Memory Quota",
    resourceType: "MEMORY",
    limit: 512,
    used: 256,
    unit: "GB",
    isActive: true,
  },
];

export default function ResourceQuotaOverviewPage() {
  const { t } = useTranslation();
  const [quotas, setQuotas] = useState<ResourceQuota[]>(mockQuotas);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredQuotas = quotas.filter((quota) =>
    quota.quotaName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    quota.resourceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Gauge className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.resourceQuotaOverview", "Resource Quota Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Quota")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search quotas...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredQuotas.map((quota) => {
            const usagePercentage = (quota.used / quota.limit) * 100;
            return (
              <Card
                key={quota.id}
                className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
              >
                <CardHeader>
                  <CardTitle className="text-lg">{quota.quotaName}</CardTitle>
                  <div className="flex items-center gap-2 mt-2">
                    <Badge variant="outline">{quota.resourceType}</Badge>
                    <Badge variant={quota.isActive ? "default" : "secondary"}>
                      {quota.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="text-muted-foreground">{t("infrastructure.used", "Used")}</span>
                      <span className="text-foreground font-medium">
                        {quota.used} / {quota.limit} {quota.unit}
                      </span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${
                          usagePercentage >= 80
                            ? "bg-red-500"
                            : usagePercentage >= 60
                            ? "bg-yellow-500"
                            : "bg-green-500"
                        }`}
                        style={{ width: `${usagePercentage}%` }}
                      />
                    </div>
                    <div className="text-sm text-muted-foreground text-right">
                      {usagePercentage.toFixed(1)}%
                    </div>
                  </div>
                  <div className="flex items-center gap-2 pt-2 border-t border-border">
                    <Link href={`/infrastructure/resource-quota-detail?id=${quota.id}`} className="flex-1">
                      <Button variant="outline" size="sm" className="w-full">
                        <Eye className="w-4 h-4 mr-2" />
                        {t("common.details", "Details")}
                      </Button>
                    </Link>
                    <Button variant="outline" size="sm">
                      <Edit className="w-4 h-4" />
                    </Button>
                    <Button variant="outline" size="sm">
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>

        {filteredQuotas.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Gauge className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noQuotasFound", "No quotas found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}

```

Corrigiendo el error de linting:


```typescriptreact
"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface KubernetesNode {
  id: number;
  nodeName: string;
  clusterName: string;
  status: string;
  cpuCores: number;
  memoryGB: number;
  podCount: number;
}

const mockNodes: KubernetesNode[] = [
  {
    id: 1,
    nodeName: "node-001",
    clusterName: "Production Cluster",
    status: "READY",
    cpuCores: 8,
    memoryGB: 32,
    podCount: 5,
  },
  {
    id: 2,
    nodeName: "node-002",
    clusterName: "Production Cluster",
    status: "READY",
    cpuCores: 8,
    memoryGB: 32,
    podCount: 4,
  },
];

export default function KubernetesNodeOverviewPage() {
  const { t } = useTranslation();
  const [nodes] = useState<KubernetesNode[]>(mockNodes);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredNodes = nodes.filter((node) =>
    node.nodeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    node.clusterName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      READY: "bg-green-500/20 text-green-500 border-green-500/50",
      NOT_READY: "bg-red-500/20 text-red-500 border-red-500/50",
    };
    return colors[status] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("infrastructure.kubernetesNodeOverview", "Kubernetes Node Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Node")}
          </Button>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search nodes...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredNodes.map((node) => (
            <Card
              key={node.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{node.nodeName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{node.clusterName}</Badge>
                  <Badge className={getStatusBadge(node.status)}>
                    {node.status}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cpu", "CPU")}</span>
                    <span className="text-foreground font-medium">{node.cpuCores} cores</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.memory", "Memory")}</span>
                    <span className="text-foreground font-medium">{node.memoryGB} GB</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.pods", "Pods")}</span>
                    <span className="text-foreground font-medium">{node.podCount}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Link href={`/infrastructure/kubernetes-node-detail?id=${node.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredNodes.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Server className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noNodesFound", "No nodes found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


```

## Migración completada

### Resumen de páginas creadas

**Total: 30 páginas**

#### 1 Dashboard (1)
- `resource-utilization-dashboard/page.tsx` — Dashboard de utilización de recursos

#### 2 Details (12)
- `cloud-credential-detail/page.tsx`
- `cloud-provider-detail/page.tsx`
- `cloud-region-detail/page.tsx`
- `cloud-resource-detail/page.tsx`
- `gpu-instance-detail/page.tsx`
- `infrastructure-audit-detail/page.tsx`
- `infrastructure-cost-detail/page.tsx`
- `infrastructure-metric-detail/page.tsx`
- `infrastructure-template-detail/page.tsx`
- `kubernetes-cluster-detail/page.tsx`
- `kubernetes-node-detail/page.tsx`
- `resource-quota-detail/page.tsx`

#### 3 Overviews (17)
- `capacity-forecast-overview/page.tsx`
- `cloud-credential-overview/page.tsx`
- `cloud-provider-overview/page.tsx`
- `cloud-region-overview/page.tsx`
- `cloud-resource-overview/page.tsx`
- `gpu-instance-overview/page.tsx`
- `infrastructure-audit-overview/page.tsx`
- `infrastructure-cost-analysis-overview/page.tsx`
- `infrastructure-cost-overview/page.tsx`
- `infrastructure-health-matrix-overview/page.tsx`
- `infrastructure-metric-overview/page.tsx`
- `infrastructure-metrics-summary-overview/page.tsx`
- `infrastructure-overview-overview/page.tsx`
- `infrastructure-template-overview/page.tsx`
- `kubernetes-cluster-overview/page.tsx`
- `kubernetes-node-overview/page.tsx`
- `resource-quota-overview/page.tsx`

### Características implementadas

- Estilo "Wow Factor": gradientes, partículas animadas, glassmorphism
- Responsive: diseño adaptable a móvil, tablet y desktop
- Búsqueda y filtros: en todas las páginas de overview
- Mock data: datos de ejemplo para todas las entidades
- Navegación: enlaces entre detalles y overviews
- Traducciones: español e inglés
- Menú actualizado: todas las rutas añadidas al menú de Infrastructure
- Linter: sin errores (1 warning corregido)

### Archivos modificados

1. `app/config/modules.ts` — Menú actualizado con 24 nuevas entradas
2. `app/config/i18n.ts` — Traducciones añadidas en español e inglés

Todas las páginas están listas para revisión. Siguen el mismo patrón de diseño y están integradas en el sistema de menú y traducciones.

