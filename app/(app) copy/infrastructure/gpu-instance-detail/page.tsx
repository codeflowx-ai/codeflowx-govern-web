"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Cpu,
  DollarSign,
  Edit,
  HardDrive,
  MemoryStick,
} from "lucide-react";
import { useRouter } from "next/navigation";
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

export default function GpuInstanceDetailPage() {   const { t } = useTranslation();
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
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/gpu-instances"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
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
            <Card className="border-border">
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

            <Card className="border-border">
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
              <Card className="border-border">
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

            <Card className="border-border">
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


