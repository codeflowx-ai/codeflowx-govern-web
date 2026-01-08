"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Cpu,
  Edit,
  HardDrive,
  MemoryStick,
  Server,
} from "lucide-react";
import { useRouter } from "next/navigation";
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

export default function KubernetesNodeDetailPage() {   const { t } = useTranslation();
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
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/kubernetes-node-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
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
            <Card className="border-border">
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

            <Card className="border-border">
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


