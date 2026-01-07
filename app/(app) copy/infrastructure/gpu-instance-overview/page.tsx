"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Cpu,
  Edit,
  Eye,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
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

export default function GpuInstanceOverviewPage() {   const { t } = useTranslation();
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
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Cpu className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.gpuInstanceOverview", "GPU Instance Overview")}
            </h1>
          </div>
          <span onClick={() => window.location.href = "/infrastructure/gpu-instances/new"}>
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Instance")}
            </Button>
          </span>
        </div>

        <Card className="border-border">
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
              className="border-border hover:border-primary/50 transition-all duration-300"
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
                  <span onClick={() => window.location.href = `/infrastructure/gpu-instance-detail?id=${instance.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </span>
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
          <Card className="border-border">
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


