"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
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
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Server className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.kubernetesClusterOverview", "Kubernetes Cluster Overview")}
            </h1>
          </div>
          <span onClick={() => window.location.href = "/infrastructure/kubernetes/new"}>
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Cluster")}
            </Button>
          </span>
        </div>

        <Card className="border-border">
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
              className="border-border hover:border-primary/50 transition-all duration-300"
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
                  <Button
                    variant="outline"
                    size="sm"
                    className="flex-1"
                    onClick={() => window.location.href = `/infrastructure/kubernetes-cluster-detail?id=${cluster.id}`}
                  >
                    <Eye className="w-4 h-4 mr-2" />
                    {t("common.details", "Details")}
                  </Button>
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
          <Card className="border-border">
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


