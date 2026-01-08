"use client";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Activity,
  BarChart3,
  CheckCircle,
  Container,
  Cpu,
  Database,
  HardDrive,
  Network,
  Plus,
  Search,
  Server,
  Settings,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useState } from "react";

interface KubernetesCluster {
  id: number;
  name: string;
  version: string;
  nodes: number;
  pods: number;
  status: "healthy" | "warning" | "error";
  cpuUsage: number;
  memoryUsage: number;
  storageUsage: number;
  region: string;
  provider: string;
  createdAt: string;
  color: string;
}

export default function KubernetesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [clusters] = useState<KubernetesCluster[]>([
    {
      id: 1,
      name: "Production Cluster",
      version: "1.28.5",
      nodes: 5,
      pods: 124,
      status: "healthy",
      cpuUsage: 45,
      memoryUsage: 67,
      storageUsage: 34,
      region: "eu-west-1",
      provider: "AWS EKS",
      createdAt: "2024-01-15",
      color: "#10b981",
    },
    {
      id: 2,
      name: "Staging Cluster",
      version: "1.27.3",
      nodes: 3,
      pods: 78,
      status: "healthy",
      cpuUsage: 23,
      memoryUsage: 34,
      storageUsage: 21,
      region: "us-east-1",
      provider: "AWS EKS",
      createdAt: "2024-01-10",
      color: "#3b82f6",
    },
    {
      id: 3,
      name: "Development Cluster",
      version: "1.26.8",
      nodes: 2,
      pods: 45,
      status: "warning",
      cpuUsage: 78,
      memoryUsage: 89,
      storageUsage: 67,
      region: "eu-central-1",
      provider: "GKE",
      createdAt: "2024-01-05",
      color: "#f59e0b",
    },
    {
      id: 4,
      name: "Testing Cluster",
      version: "1.28.2",
      nodes: 1,
      pods: 12,
      status: "healthy",
      cpuUsage: 15,
      memoryUsage: 22,
      storageUsage: 18,
      region: "us-west-2",
      provider: "AKS",
      createdAt: "2024-01-20",
      color: "#8b5cf6",
    },
  ]);

  const systemStats = {
    totalClusters: clusters.length,
    healthyClusters: clusters.filter((c) => c.status === "healthy").length,
    totalNodes: clusters.reduce((sum, c) => sum + c.nodes, 0),
    totalPods: clusters.reduce((sum, c) => sum + c.pods, 0),
    avgCpuUsage: Math.round(
      clusters.reduce((sum, c) => sum + c.cpuUsage, 0) / clusters.length
    ),
    avgMemoryUsage: Math.round(
      clusters.reduce((sum, c) => sum + c.memoryUsage, 0) / clusters.length
    ),
  };

  const filteredClusters = clusters.filter((cluster) =>
    cluster.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cluster.provider.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cluster.region.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusColor = (status: string) => {
    switch (status) {
      case "healthy":
        return "bg-green-500/20 text-green-500 border-green-500/50";
      case "warning":
        return "bg-yellow-500/20 text-yellow-500 border-yellow-500/50";
      case "error":
        return "bg-red-500/20 text-red-500 border-red-500/50";
      default:
        return "bg-gray-500/20 text-gray-500 border-gray-500/50";
    }
  };

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
      {/* Partículas animadas */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Container className="w-8 h-8 text-primary" />
              <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
                Kubernetes Clusters
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-11">
              Gestiona y monitorea tus clusters de Kubernetes
            </p>
          </div>
          <div className="flex items-center gap-2">
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              New Cluster
            </Button>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {[
            {
              title: "Total Clusters",
              value: systemStats.totalClusters.toString(),
              change: `${systemStats.healthyClusters} healthy`,
              icon: Container,
              color: "#3b82f6",
              gradient: "from-blue-500 to-blue-600",
            },
            {
              title: "Total Nodes",
              value: systemStats.totalNodes.toString(),
              change: "Across all clusters",
              icon: Server,
              color: "#10b981",
              gradient: "from-green-500 to-green-600",
            },
            {
              title: "Total Pods",
              value: systemStats.totalPods.toString(),
              change: "Running workloads",
              icon: Database,
              color: "#8b5cf6",
              gradient: "from-purple-500 to-purple-600",
            },
            {
              title: "Avg CPU Usage",
              value: `${systemStats.avgCpuUsage}%`,
              change: `${systemStats.avgMemoryUsage}% memory`,
              icon: Cpu,
              color: "#f59e0b",
              gradient: "from-orange-500 to-orange-600",
            },
          ].map((stat, index) => (
            <div
              key={index}
              style={{
                animationDelay: `${index * 150}ms`,
                animation: "fadeInUp 0.8s ease-out forwards",
              }}
            >
              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300 group">
                <CardContent className="p-4">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <p className="text-xs font-medium mb-1 text-muted-foreground">
                        {stat.title}
                      </p>
                      <div className="flex items-baseline gap-1">
                        <p className="text-2xl font-bold text-foreground">
                          {stat.value}
                        </p>
                      </div>
                      <p
                        className="text-xs font-medium mt-1"
                        style={{ color: stat.color }}
                      >
                        {stat.change}
                      </p>
                    </div>
                    <div
                      className="p-2.5 rounded-xl ml-3 transition-all duration-500 group-hover:scale-110 group-hover:rotate-6 relative overflow-hidden"
                      style={{
                        background: `linear-gradient(135deg, ${stat.color}20, ${stat.color}10)`,
                        borderColor: stat.color,
                        borderWidth: "2px",
                        boxShadow: `0 0 25px ${stat.color}50, inset 0 0 20px ${stat.color}10`,
                      }}
                    >
                      <stat.icon
                        className="w-5 h-5 transition-all duration-500 group-hover:scale-110 relative z-10"
                        style={{ color: stat.color }}
                      />
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          ))}
        </div>

        {/* Search Bar */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder="Search clusters by name, provider, or region..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        {/* Clusters Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {filteredClusters.map((cluster, index) => (
            <div
              key={cluster.id}
              style={{
                animationDelay: `${index * 150}ms`,
                animation: "fadeInUp 0.8s ease-out forwards",
              }}
            >
              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:border-primary/50 transition-all duration-300 group overflow-hidden relative">
                {/* Efecto de brillo en hover */}
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

                {/* Efecto de borde brillante */}
                <div
                  className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
                  style={{
                    background: `linear-gradient(45deg, ${cluster.color}40, transparent, ${cluster.color}40)`,
                    backgroundSize: "200% 200%",
                    animation: "shimmer 2s ease-in-out infinite",
                  }}
                />

                <CardHeader className="pb-3 relative z-10">
                  <div className="flex items-start justify-between">
                    <div className="flex items-center gap-3">
                      <div
                        className="p-3 rounded-xl transition-all duration-500 group-hover:scale-110 group-hover:rotate-6 relative overflow-hidden"
                        style={{
                          background: `linear-gradient(135deg, ${cluster.color}20, ${cluster.color}10)`,
                          borderColor: cluster.color,
                          borderWidth: "2px",
                          boxShadow: `0 0 25px ${cluster.color}50, inset 0 0 20px ${cluster.color}10`,
                        }}
                      >
                        <Container
                          className="w-6 h-6 transition-all duration-500 group-hover:scale-110 relative z-10"
                          style={{ color: cluster.color }}
                        />
                      </div>
                      <div className="flex-1">
                        <CardTitle className="text-foreground text-lg group-hover:text-primary transition-colors duration-300">
                          {cluster.name}
                        </CardTitle>
                        <div className="flex items-center gap-2 mt-1">
                          <Badge className={getStatusColor(cluster.status)}>
                            {cluster.status}
                          </Badge>
                          <Badge variant="outline" className="text-xs">
                            v{cluster.version}
                          </Badge>
                        </div>
                      </div>
                    </div>
                  </div>
                  <CardDescription className="text-muted-foreground text-sm mt-2">
                    {cluster.provider} • {cluster.region}
                  </CardDescription>
                </CardHeader>

                <CardContent className="pt-0 relative z-10">
                  {/* Métricas principales */}
                  <div className="grid grid-cols-3 gap-3 mb-4">
                    <div className="p-2 rounded-lg bg-muted/50">
                      <p className="text-xs text-muted-foreground">Nodes</p>
                      <p className="text-sm font-medium text-foreground">
                        {cluster.nodes}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-muted/50">
                      <p className="text-xs text-muted-foreground">Pods</p>
                      <p className="text-sm font-medium text-foreground">
                        {cluster.pods}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-muted/50">
                      <p className="text-xs text-muted-foreground">Created</p>
                      <p className="text-sm font-medium text-foreground">
                        {new Date(cluster.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                  </div>

                  {/* Uso de recursos */}
                  <div className="space-y-3 mb-4">
                    <div className="space-y-1">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground flex items-center gap-1">
                          <Cpu className="w-3 h-3" />
                          CPU Usage
                        </span>
                        <span className={`font-medium ${getUsageColor(cluster.cpuUsage)}`}>
                          {cluster.cpuUsage}%
                        </span>
                      </div>
                      <div className="w-full bg-muted rounded-full h-2">
                        <div
                          className={`h-2 rounded-full transition-all duration-500 ${
                            cluster.cpuUsage >= 80
                              ? "bg-red-500"
                              : cluster.cpuUsage >= 60
                              ? "bg-yellow-500"
                              : "bg-green-500"
                          }`}
                          style={{ width: `${cluster.cpuUsage}%` }}
                        />
                      </div>
                    </div>

                    <div className="space-y-1">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground flex items-center gap-1">
                          <Database className="w-3 h-3" />
                          Memory Usage
                        </span>
                        <span className={`font-medium ${getUsageColor(cluster.memoryUsage)}`}>
                          {cluster.memoryUsage}%
                        </span>
                      </div>
                      <div className="w-full bg-muted rounded-full h-2">
                        <div
                          className={`h-2 rounded-full transition-all duration-500 ${
                            cluster.memoryUsage >= 80
                              ? "bg-red-500"
                              : cluster.memoryUsage >= 60
                              ? "bg-yellow-500"
                              : "bg-green-500"
                          }`}
                          style={{ width: `${cluster.memoryUsage}%` }}
                        />
                      </div>
                    </div>

                    <div className="space-y-1">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground flex items-center gap-1">
                          <HardDrive className="w-3 h-3" />
                          Storage Usage
                        </span>
                        <span className={`font-medium ${getUsageColor(cluster.storageUsage)}`}>
                          {cluster.storageUsage}%
                        </span>
                      </div>
                      <div className="w-full bg-muted rounded-full h-2">
                        <div
                          className={`h-2 rounded-full transition-all duration-500 ${
                            cluster.storageUsage >= 80
                              ? "bg-red-500"
                              : cluster.storageUsage >= 60
                              ? "bg-yellow-500"
                              : "bg-green-500"
                          }`}
                          style={{ width: `${cluster.storageUsage}%` }}
                        />
                      </div>
                    </div>
                  </div>

                  {/* Botones de acción */}
                  <div className="flex items-center justify-between pt-2 border-t border-border">
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="text-xs">
                        {cluster.provider}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-2">
                      <Link href={`/infrastructure/kubernetes-cluster-detail?id=${cluster.id}`}>
                        <Button variant="outline" size="sm">
                          <Activity className="w-4 h-4 mr-2" />
                          Details
                        </Button>
                      </Link>
                      <Button variant="outline" size="sm">
                        <Settings className="w-4 h-4" />
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          ))}
        </div>

        {/* Estado vacío */}
        {filteredClusters.length === 0 && (
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardContent className="py-12 text-center">
              <Container className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                No clusters found matching your search
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


