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

export default function KubernetesNodeOverviewPage() {   const { t } = useTranslation();
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
              {t("infrastructure.kubernetesNodeOverview", "Kubernetes Node Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Node")}
          </Button>
        </div>

        <Card className="border-border">
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
              className="border-border hover:border-primary/50 transition-all duration-300"
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
                  <Button
                    variant="outline"
                    size="sm"
                    className="flex-1"
                    onClick={() => window.location.href = `/infrastructure/kubernetes-node-detail?id=${node.id}`}
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

        {filteredNodes.length === 0 && (
          <Card className="border-border">
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


