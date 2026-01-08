"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
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
import { useRouter } from "next/navigation";
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

export default function CloudResourceDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";

  const resource = mockResourceDetail;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/resources"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <Server className="w-8 h-8 text-primary" />
                {resource.resourceName}
              </h1>
              <p className="text-muted-foreground mt-1">{resource.description}</p>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge
              variant={resource.isActive ? "primary" : "secondary"}
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
            <Card className="border-border">
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
            <Card className="border-border">
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
              <Card className="border-border">
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
            <Card className="border-border">
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
            <Card className="border-border">
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


