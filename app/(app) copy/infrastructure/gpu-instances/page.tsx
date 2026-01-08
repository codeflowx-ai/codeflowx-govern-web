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
import { SimpleModal } from "@/components/ui/SimpleModal";
import {
  Activity,
  BarChart3,
  CheckCircle,
  Cpu,
  HardDrive,
  Monitor,
  Plus,
  RefreshCw,
  Server,
  Settings,
  Zap,
} from "lucide-react";
import { useState } from "react";

interface GpuInstance {
  id: string;
  name: string;
  status: "active" | "idle" | "maintenance" | "error";
  type: "training" | "inference" | "development" | "research";
  gpuModel: string;
  gpuCount: number;
  color: string;
  specs: {
    cpu: string;
    memory: string;
    storage: string;
    network: string;
  };
  metrics: {
    gpuUsage: string;
    cpuUsage: string;
    memoryUsage: string;
    storageUsage: string;
  };
  workloads: string[];
  lastActivity: string;
  uptime: string;
  cost: string;
  region: string;
  tags: string[];
  metadata: {
    created: string;
    owner: string;
    team: string;
    project: string;
  };
}

const gpuInstances: GpuInstance[] = [
  {
    id: "gpu-train-01",
    name: "Training Cluster A100",
    status: "active",
    type: "training",
    gpuModel: "NVIDIA A100",
    gpuCount: 8,
    color: "#10b981",
    specs: {
      cpu: "64 cores",
      memory: "512GB",
      storage: "4TB NVMe",
      network: "100Gbps",
    },
    metrics: {
      gpuUsage: "92%",
      cpuUsage: "78%",
      memoryUsage: "85%",
      storageUsage: "67%",
    },
    workloads: ["BERT Training", "GPT Fine-tuning", "Computer Vision"],
    lastActivity: "Active now",
    uptime: "99.8%",
    cost: "$4,200/month",
    region: "us-east-1",
    tags: ["training", "high-performance", "ml-ops"],
    metadata: {
      created: "2024-01-10",
      owner: "ML Team",
      team: "Machine Learning",
      project: "Large Language Models",
    },
  },
  {
    id: "gpu-infer-01",
    name: "Inference Server V100",
    status: "active",
    type: "inference",
    gpuModel: "NVIDIA V100",
    gpuCount: 4,
    color: "#3b82f6",
    specs: {
      cpu: "32 cores",
      memory: "256GB",
      storage: "2TB SSD",
      network: "25Gbps",
    },
    metrics: {
      gpuUsage: "67%",
      cpuUsage: "45%",
      memoryUsage: "52%",
      storageUsage: "38%",
    },
    workloads: ["Real-time Inference", "API Serving", "Batch Processing"],
    lastActivity: "2 hours ago",
    uptime: "99.5%",
    cost: "$2,800/month",
    region: "us-west-2",
    tags: ["inference", "production", "low-latency"],
    metadata: {
      created: "2024-02-01",
      owner: "DevOps Team",
      team: "Platform Engineering",
      project: "AI Services",
    },
  },
  {
    id: "gpu-dev-01",
    name: "Development RTX 4090",
    status: "idle",
    type: "development",
    gpuModel: "NVIDIA RTX 4090",
    gpuCount: 2,
    color: "#8b5cf6",
    specs: {
      cpu: "16 cores",
      memory: "128GB",
      storage: "1TB SSD",
      network: "10Gbps",
    },
    metrics: {
      gpuUsage: "12%",
      cpuUsage: "23%",
      memoryUsage: "31%",
      storageUsage: "45%",
    },
    workloads: ["Model Prototyping", "Testing", "Development"],
    lastActivity: "1 day ago",
    uptime: "94.2%",
    cost: "$1,500/month",
    region: "eu-west-1",
    tags: ["development", "prototyping", "testing"],
    metadata: {
      created: "2024-03-01",
      owner: "Research Team",
      team: "AI Research",
      project: "Model Development",
    },
  },
  {
    id: "gpu-research-01",
    name: "Research H100",
    status: "maintenance",
    type: "research",
    gpuModel: "NVIDIA H100",
    gpuCount: 16,
    color: "#f59e0b",
    specs: {
      cpu: "128 cores",
      memory: "1TB",
      storage: "8TB NVMe",
      network: "200Gbps",
    },
    metrics: {
      gpuUsage: "0%",
      cpuUsage: "5%",
      memoryUsage: "8%",
      storageUsage: "12%",
    },
    workloads: ["Advanced Research", "Large Scale Training", "HPC"],
    lastActivity: "3 days ago",
    uptime: "89.7%",
    cost: "$12,000/month",
    region: "us-central-1",
    tags: ["research", "cutting-edge", "high-performance"],
    metadata: {
      created: "2024-01-01",
      owner: "Research Team",
      team: "Advanced AI",
      project: "Next-Gen Models",
    },
  },
];

const systemStats = {
  totalInstances: gpuInstances.length,
  activeInstances: gpuInstances.filter(
    (instance) => instance.status === "active"
  ).length,
  idleInstances: gpuInstances.filter((instance) => instance.status === "idle")
    .length,
  totalGPUs: gpuInstances.reduce((sum, instance) => sum + instance.gpuCount, 0),
  totalCost: "$20,500/month",
  averageUtilization: "67.8%",
  totalWorkloads: "12",
  lastActivity: "Active now",
};

const quickActions = [
  {
    title: "Add GPU Instance",
    description: "Provision new GPU instance",
    icon: Plus,
    action: () => console.log("Add GPU instance"),
    color: "#10b981",
  },
  {
    title: "Scale Resources",
    description: "Adjust GPU capacity",
    icon: RefreshCw,
    action: () => console.log("Scale resources"),
    color: "#3b82f6",
  },
  {
    title: "Monitor Performance",
    description: "Check GPU utilization",
    icon: Monitor,
    action: () => console.log("Monitor performance"),
    color: "#8b5cf6",
  },
  {
    title: "Optimize Costs",
    description: "Reduce GPU expenses",
    icon: BarChart3,
    action: () => console.log("Optimize costs"),
    color: "#f59e0b",
  },
];

const getStatusColor = (status: string) => {
  switch (status) {
    case "active":
      return "bg-green-500";
    case "idle":
      return "bg-blue-500";
    case "maintenance":
      return "bg-yellow-500";
    case "error":
      return "bg-red-500";
    default:
      return "bg-gray-500";
  }
};

const getStatusText = (status: string) => {
  switch (status) {
    case "active":
      return "Active";
    case "idle":
      return "Idle";
    case "maintenance":
      return "Maintenance";
    case "error":
      return "Error";
    default:
      return "Unknown";
  }
};

const getTypeColor = (type: string) => {
  switch (type) {
    case "training":
      return "bg-red-500";
    case "inference":
      return "bg-blue-500";
    case "development":
      return "bg-purple-500";
    case "research":
      return "bg-orange-500";
    default:
      return "bg-gray-500";
  }
};

export default function GpuInstancesPage() {
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedInstance, setSelectedInstance] = useState<GpuInstance | null>(
    null
  );

  const handleEditInstance = (instance: GpuInstance) => {
    setSelectedInstance(instance);
    setIsEditModalOpen(true);
  };

  return (
    <div className="space-y-6 p-6 relative min-h-screen">

      {/* Partículas flotantes de fondo */}
      <div className="absolute inset-0 pointer-events-none">
        <div
          className="absolute top-20 left-10 w-2 h-2 bg-blue-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "0s" }}
        />
        <div
          className="absolute top-40 right-20 w-1 h-1 bg-green-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "1s" }}
        />
        <div
          className="absolute top-60 left-1/4 w-1.5 h-1.5 bg-purple-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "2s" }}
        />
        <div
          className="absolute top-80 right-1/3 w-1 h-1 bg-orange-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "3s" }}
        />
        <div
          className="absolute top-32 left-1/2 w-2 h-2 bg-blue-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "4s" }}
        />
        <div
          className="absolute top-96 left-1/3 w-1 h-1 bg-green-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "5s" }}
        />
      </div>

      {/* Header de la página */}
      <div className="relative">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">
              GPU Instances
            </h1>
            <p className="text-muted-foreground text-lg">
              Gestiona y optimiza tus instancias de GPU para machine learning
            </p>
          </div>

          {/* Estadísticas generales del sistema */}
          <div className="flex items-center gap-4 p-4 rounded-xl backdrop-blur-md bg-white/10 dark:bg-black/10 border border-white/20 dark:border-black/20 shadow-lg">
            <div className="text-center">
              <p className="text-xs text-muted-foreground">Total Cost</p>
              <p className="text-lg font-bold text-foreground">
                {systemStats.totalCost}
              </p>
            </div>
            <div className="h-6 w-px bg-border" />
            <div className="text-center">
              <p className="text-xs text-muted-foreground">Active Instances</p>
              <p className="text-lg font-bold text-foreground">
                {systemStats.activeInstances}
              </p>
            </div>
            <div className="h-6 w-px bg-border" />
            <div className="text-center">
              <p className="text-xs text-muted-foreground">Total GPUs</p>
              <p className="text-lg font-bold text-foreground">
                {systemStats.totalGPUs}
              </p>
            </div>
          </div>
        </div>

        {/* Botón para agregar instancia GPU */}
        <Button
          onClick={() => setIsAddModalOpen(true)}
          className="mb-6 bg-gradient-to-r from-purple-600 to-blue-600 hover:from-purple-700 hover:to-blue-700 text-white shadow-lg hover:shadow-xl transition-all duration-300 transform hover:scale-105"
        >
          <Plus className="w-4 h-4 mr-2" />
          Add GPU Instance
        </Button>
      </div>

      {/* Grid de estadísticas principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 relative z-10">
        {[
          {
            title: "Active Instances",
            value: systemStats.activeInstances.toString(),
            change: `${(
              (systemStats.activeInstances / systemStats.totalInstances) *
              100
            ).toFixed(1)}% utilization`,
            icon: CheckCircle,
            color: "#10b981",
          },
          {
            title: "Total GPUs",
            value: systemStats.totalGPUs.toString(),
            change: "Across all instances",
            icon: Zap,
            color: "#3b82f6",
          },
          {
            title: "Average Utilization",
            value: systemStats.averageUtilization,
            change: "GPU performance",
            icon: Activity,
            color: "#8b5cf6",
          },
          {
            title: "Total Workloads",
            value: systemStats.totalWorkloads,
            change: "Running jobs",
            icon: Server,
            color: "#f59e0b",
          },
        ].map((stat, index) => (
          <div
            key={stat.title}
            className="relative"
            style={{
              animationDelay: `${index * 150}ms`,
              animation: "fadeInUp 0.8s ease-out forwards",
            }}
          >
            <Card className="group transition-all duration-700 hover:scale-105 hover:shadow-2xl bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden relative hover:bg-card/90">
              {/* Efecto de brillo en hover */}
              <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

              {/* Efecto de borde brillante */}
              <div
                className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
                style={{
                  background: `linear-gradient(45deg, ${stat.color}40, transparent, ${stat.color}40)`,
                  backgroundSize: "200% 200%",
                  animation: "shimmer 2s ease-in-out infinite",
                }}
              />

              <CardContent className="p-4 relative z-10">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-muted-foreground">
                      {stat.title}
                    </p>
                    <p className="text-2xl font-bold text-foreground">
                      {stat.value}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      {stat.change}
                    </p>
                  </div>
                  <div className="relative">
                    {/* Efecto de partículas en el icono */}
                    <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-x-[-100%] group-hover:translate-x-[100%]" />

                    <stat.icon
                      className="w-8 h-8 transition-all duration-500 group-hover:scale-110 relative z-10"
                      style={{ color: stat.color }}
                    />
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        ))}
      </div>

      {/* Acciones rápidas */}
      <div className="relative">
        <h2 className="text-xl font-semibold text-foreground mb-4">
          Quick Actions
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {quickActions.map((action, index) => (
            <div
              key={action.title}
              className="relative"
              style={{
                animationDelay: `${index * 100}ms`,
                animation: "fadeInUp 0.6s ease-out forwards",
              }}
            >
              <div
                className="cursor-pointer"
                onClick={action.action}
              >
                <Card className="group transition-all duration-500 hover:scale-105 hover:shadow-xl bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden relative hover:bg-card/90">
                  {/* Efecto de brillo en hover */}
                  <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

                  <div className="flex flex-col items-center text-center space-y-2 relative z-10">
                    <div
                      className="w-12 h-12 rounded-full flex items-center justify-center transition-all duration-300 group-hover:scale-110"
                      style={{ backgroundColor: `${action.color}20` }}
                    >
                      {/* Efecto de partículas en el icono */}
                      <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-x-[-100%] group-hover:translate-x-[100%]" />

                      <action.icon
                        className="w-6 h-6 transition-all duration-300 group-hover:scale-110 relative z-10"
                        style={{ color: action.color }}
                      />
                    </div>
                    <div>
                      <h3 className="font-medium text-foreground">
                        {action.title}
                      </h3>
                      <p className="text-xs text-muted-foreground">
                        {action.description}
                      </p>
                    </div>
                  </div>
                </Card>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Grid de instancias GPU */}
      <div className="relative">
        <h2 className="text-xl font-semibold text-foreground mb-4">
          GPU Instances
        </h2>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {gpuInstances.map((instance, index) => (
            <div
              key={instance.id}
              className="relative"
              style={{
                animationDelay: `${index * 150}ms`,
                animation: "fadeInUp 0.8s ease-out forwards",
              }}
            >
              <Card className="group transition-all duration-700 hover:scale-105 hover:shadow-2xl bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden relative hover:bg-card/90">
                {/* Efecto de brillo en hover */}
                <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

                {/* Efecto de borde brillante */}
                <div
                  className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
                  style={{
                    background: `linear-gradient(45deg, ${instance.color}40, transparent, ${instance.color}40)`,
                    backgroundSize: "200% 200%",
                    animation: "shimmer 2s ease-in-out infinite",
                  }}
                />

                <CardHeader className="pb-3 relative z-10">
                  <div className="flex items-start justify-between">
                    <div className="flex items-center gap-3">
                      <div className="relative">
                        {/* Efecto de partículas en el icono */}
                        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 transform translate-x-[-100%] group-hover:translate-x-[100%]" />

                        <Zap
                          className="w-6 h-6 transition-all duration-500 group-hover:scale-110 relative z-10"
                          style={{ color: instance.color }}
                        />
                      </div>
                      <div>
                        <CardTitle className="text-lg font-semibold text-foreground">
                          {instance.name}
                        </CardTitle>
                        <div className="flex items-center gap-2 mt-1">
                          <div
                            className={`w-2 h-2 rounded-full ${getStatusColor(
                              instance.status
                            )}`}
                          />
                          <span className="text-xs text-muted-foreground capitalize">
                            {getStatusText(instance.status)}
                          </span>
                          <div
                            className={`w-2 h-2 rounded-full ${getTypeColor(
                              instance.type
                            )}`}
                          />
                          <span className="text-xs text-muted-foreground capitalize">
                            {instance.type}
                          </span>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="text-xs">
                        {instance.gpuModel}
                      </Badge>
                      <Badge variant="outline" className="text-xs">
                        {instance.gpuCount} GPUs
                      </Badge>
                    </div>
                  </div>
                  <CardDescription className="text-muted-foreground text-sm mt-2">
                    {instance.region} • {instance.uptime} uptime •{" "}
                    {instance.cost}
                  </CardDescription>
                </CardHeader>

                <CardContent className="relative z-10">
                  {/* Especificaciones de la instancia */}
                  <div className="grid grid-cols-2 gap-3 mb-4">
                    <div className="p-2 rounded-lg bg-card/50">
                      <p className="text-xs text-muted-foreground">CPU</p>
                      <p className="text-sm font-medium text-foreground">
                        {instance.specs.cpu}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-card/50">
                      <p className="text-xs text-muted-foreground">Memory</p>
                      <p className="text-sm font-medium text-foreground">
                        {instance.specs.memory}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-card/50">
                      <p className="text-xs text-muted-foreground">Storage</p>
                      <p className="text-sm font-medium text-foreground">
                        {instance.specs.storage}
                      </p>
                    </div>
                    <div className="p-2 rounded-lg bg-card/50">
                      <p className="text-xs text-muted-foreground">Network</p>
                      <p className="text-sm font-medium text-foreground">
                        {instance.specs.network}
                      </p>
                    </div>
                  </div>

                  {/* Métricas de rendimiento */}
                  <div className="space-y-2 mb-4">
                    <p className="text-xs font-medium text-muted-foreground">
                      Performance Metrics
                    </p>
                    <div className="grid grid-cols-2 gap-2">
                      <div className="flex items-center gap-2 text-xs">
                        <Zap className="w-3 h-3 text-green-500" />
                        <span className="text-muted-foreground">GPU:</span>
                        <span className="text-foreground">
                          {instance.metrics.gpuUsage}
                        </span>
                      </div>
                      <div className="flex items-center gap-2 text-xs">
                        <Cpu className="w-3 h-3 text-blue-500" />
                        <span className="text-muted-foreground">CPU:</span>
                        <span className="text-foreground">
                          {instance.metrics.cpuUsage}
                        </span>
                      </div>
                      <div className="flex items-center gap-2 text-xs">
                        <HardDrive className="w-3 h-3 text-purple-500" />
                        <span className="text-muted-foreground">RAM:</span>
                        <span className="text-foreground">
                          {instance.metrics.memoryUsage}
                        </span>
                      </div>
                      <div className="flex items-center gap-2 text-xs">
                        <HardDrive className="w-3 h-3 text-orange-500" />
                        <span className="text-muted-foreground">Storage:</span>
                        <span className="text-foreground">
                          {instance.metrics.storageUsage}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Workloads activos */}
                  <div className="space-y-2 mb-4">
                    <p className="text-xs font-medium text-muted-foreground">
                      Active Workloads
                    </p>
                    <div className="flex flex-wrap gap-1">
                      {instance.workloads.map((workload, idx) => (
                        <Badge
                          key={idx}
                          variant="secondary"
                          className="text-xs bg-card/70 hover:bg-card/90 transition-colors duration-300"
                        >
                          {workload}
                        </Badge>
                      ))}
                    </div>
                  </div>

                  {/* Tags de la instancia */}
                  <div className="space-y-2 mb-4">
                    <p className="text-xs font-medium text-muted-foreground">
                      Tags
                    </p>
                    <div className="flex flex-wrap gap-1">
                      {instance.tags.map((tag, idx) => (
                        <Badge
                          key={idx}
                          variant="outline"
                          className="text-xs border-border text-muted-foreground"
                        >
                          {tag}
                        </Badge>
                      ))}
                    </div>
                  </div>

                  {/* Información de la instancia */}
                  <div className="space-y-2 mb-4">
                    <p className="text-xs font-medium text-muted-foreground">
                      Instance Info
                    </p>
                    <div className="grid grid-cols-2 gap-2 text-xs">
                      <div className="flex items-center gap-2">
                        <span className="text-muted-foreground">Owner:</span>
                        <span className="text-foreground">
                          {instance.metadata.owner}
                        </span>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-muted-foreground">Team:</span>
                        <span className="text-foreground">
                          {instance.metadata.team}
                        </span>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-muted-foreground">Created:</span>
                        <span className="text-foreground">
                          {instance.metadata.created}
                        </span>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-muted-foreground">
                          Last Activity:
                        </span>
                        <span className="text-foreground">
                          {instance.lastActivity}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Acciones de la instancia */}
                  <div className="flex items-center justify-between pt-4 border-t border-border">
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="text-xs">
                        {instance.uptime}
                      </Badge>
                      <Badge variant="outline" className="text-xs">
                        {instance.cost}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => handleEditInstance(instance)}
                      >
                        <Settings className="w-3 h-3 mr-1" />
                        Configure
                      </Button>
                      <Button size="sm">
                        <Monitor className="w-3 h-3 mr-1" />
                        Monitor
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          ))}
        </div>
      </div>

      {/* Footer con estadísticas adicionales */}
      <div className="relative pt-8 border-t border-border">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 text-center">
          <div>
            <p className="text-sm text-muted-foreground">Idle Instances</p>
            <p className="text-lg font-semibold text-foreground">
              {systemStats.idleInstances}
            </p>
          </div>
          <div>
            <p className="text-sm text-muted-foreground">Total Workloads</p>
            <p className="text-lg font-semibold text-foreground">
              {systemStats.totalWorkloads}
            </p>
          </div>
          <div>
            <p className="text-sm text-muted-foreground">Average Utilization</p>
            <p className="text-lg font-semibold text-foreground">
              {systemStats.averageUtilization}
            </p>
          </div>
          <div>
            <p className="text-sm text-muted-foreground">Last Updated</p>
            <p className="text-lg font-semibold text-foreground">Just now</p>
          </div>
        </div>
      </div>

      {/* Modal para agregar instancia GPU */}
      <SimpleModal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Add GPU Instance"
        maxWidth="max-w-4xl"
      >
        <div className="space-y-4">
          <p className="text-muted-foreground">
            Configure a new GPU instance with your preferred specifications and
            GPU model.
          </p>
          {/* Aquí irían los campos del formulario */}
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setIsAddModalOpen(false)}>
              Cancel
            </Button>
            <Button onClick={() => setIsAddModalOpen(false)}>
              Create Instance
            </Button>
          </div>
        </div>
      </SimpleModal>

      {/* Modal para editar instancia GPU */}
      <SimpleModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        title={`Edit GPU Instance: ${selectedInstance?.name}`}
        maxWidth="max-w-4xl"
      >
        <div className="space-y-4">
          <p className="text-muted-foreground">
            Modify the configuration of your GPU instance.
          </p>
          {/* Aquí irían los campos del formulario */}
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setIsEditModalOpen(false)}>
              Cancel
            </Button>
            <Button onClick={() => setIsEditModalOpen(false)}>
              Save Changes
            </Button>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}
