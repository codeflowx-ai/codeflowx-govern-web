"use client";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Textarea } from "@/components/ui/textarea";
import {
  Activity,
  CheckCircle,
  Clock,
  DollarSign,
  FileText,
  Pause,
  Play,
  Plus,
  RotateCcw,
  Scale,
  Square,
  XCircle,
} from "lucide-react";
import { useState } from "react";

// Interfaces basadas en el documento
interface InfrastructureDeployment {
  id: number;
  deploymentName: string;
  deploymentType:
    | "MODEL_SERVING"
    | "MODEL_TRAINING"
    | "INFRASTRUCTURE_SCALING"
    | "BACKUP_RESTORE"
    | "DISASTER_RECOVERY"
    | "MIGRATION";
  projectId?: number;
  modelId?: number;
  kubernetesClusterId?: number;
  gpuInstanceId?: number;
  deploymentConfig: string;
  resourceRequirements: string;
  status:
    | "PENDING"
    | "IN_PROGRESS"
    | "COMPLETED"
    | "FAILED"
    | "CANCELLED"
    | "ROLLING_BACK"
    | "ROLLED_BACK";
  startedAt?: string;
  completedAt?: string;
  estimatedCostPerHour?: number;
  actualCostPerHour?: number;
  totalCost?: number;
  deploymentDurationHours?: number;
  createdAt: string;
  updatedAt: string;
}

interface DeploymentCost {
  id: number;
  deploymentId: number;
  costType:
    | "COMPUTE"
    | "STORAGE"
    | "NETWORK"
    | "LICENSING"
    | "SUPPORT"
    | "DATA_TRANSFER"
    | "API_CALLS"
    | "MONITORING"
    | "BACKUP"
    | "SNAPSHOT";
  resourceType: string;
  resourceName: string;
  costPerHour: number;
  hoursUsed: number;
  totalCost: number;
  currency: string;
  billingPeriodStart?: string;
  billingPeriodEnd?: string;
  isBilled: boolean;
  billingDate?: string;
}

interface DeploymentLog {
  id: number;
  deploymentId: number;
  logLevel: "INFO" | "WARNING" | "ERROR" | "DEBUG";
  message: string;
  timestamp: string;
  source: string;
}

// Mock data
const mockDeployments: InfrastructureDeployment[] = [
  {
    id: 1,
    deploymentName: "GPT-4 Model Serving",
    deploymentType: "MODEL_SERVING",
    projectId: 1,
    modelId: 101,
    kubernetesClusterId: 1,
    deploymentConfig:
      '{"replicas": 3, "resources": {"cpu": "4", "memory": "16Gi"}}',
    resourceRequirements:
      '{"gpu": "A100", "memory": "32Gi", "storage": "100Gi"}',
    status: "IN_PROGRESS",
    startedAt: "2024-01-15T10:00:00Z",
    estimatedCostPerHour: 2.5,
    actualCostPerHour: 2.45,
    totalCost: 58.8,
    deploymentDurationHours: 24,
    createdAt: "2024-01-15T09:30:00Z",
    updatedAt: "2024-01-15T10:00:00Z",
  },
  {
    id: 2,
    deploymentName: "BERT Training Job",
    deploymentType: "MODEL_TRAINING",
    projectId: 2,
    modelId: 102,
    gpuInstanceId: 1,
    deploymentConfig:
      '{"epochs": 100, "batch_size": 32, "learning_rate": 0.001}',
    resourceRequirements:
      '{"gpu": "V100", "memory": "64Gi", "storage": "500Gi"}',
    status: "COMPLETED",
    startedAt: "2024-01-10T08:00:00Z",
    completedAt: "2024-01-12T16:00:00Z",
    estimatedCostPerHour: 4.2,
    actualCostPerHour: 4.15,
    totalCost: 200.0,
    deploymentDurationHours: 48,
    createdAt: "2024-01-10T07:30:00Z",
    updatedAt: "2024-01-12T16:00:00Z",
  },
  {
    id: 3,
    deploymentName: "Auto-scaling Infrastructure",
    deploymentType: "INFRASTRUCTURE_SCALING",
    kubernetesClusterId: 2,
    deploymentConfig: '{"min_nodes": 3, "max_nodes": 10, "target_cpu": 70}',
    resourceRequirements: '{"cpu": "8", "memory": "32Gi", "storage": "200Gi"}',
    status: "COMPLETED",
    startedAt: "2024-01-14T14:00:00Z",
    completedAt: "2024-01-14T15:30:00Z",
    estimatedCostPerHour: 1.8,
    actualCostPerHour: 1.75,
    totalCost: 2.63,
    deploymentDurationHours: 1.5,
    createdAt: "2024-01-14T13:30:00Z",
    updatedAt: "2024-01-14T15:30:00Z",
  },
];

const mockDeploymentCosts: DeploymentCost[] = [
  {
    id: 1,
    deploymentId: 1,
    costType: "COMPUTE",
    resourceType: "GPU",
    resourceName: "A100 Instance",
    costPerHour: 2.5,
    hoursUsed: 24,
    totalCost: 60.0,
    currency: "USD",
    isBilled: false,
  },
  {
    id: 2,
    deploymentId: 1,
    costType: "STORAGE",
    resourceType: "Persistent Volume",
    resourceName: "Model Storage",
    costPerHour: 0.1,
    hoursUsed: 24,
    totalCost: 2.4,
    currency: "USD",
    isBilled: false,
  },
];

const mockDeploymentLogs: DeploymentLog[] = [
  {
    id: 1,
    deploymentId: 1,
    logLevel: "INFO",
    message: "Deployment started successfully",
    timestamp: "2024-01-15T10:00:00Z",
    source: "Kubernetes",
  },
  {
    id: 2,
    deploymentId: 1,
    logLevel: "INFO",
    message: "Pod scheduled on node worker-1",
    timestamp: "2024-01-15T10:01:00Z",
    source: "Scheduler",
  },
  {
    id: 3,
    deploymentId: 1,
    logLevel: "WARNING",
    message: "High memory usage detected",
    timestamp: "2024-01-15T10:05:00Z",
    source: "Monitoring",
  },
];

export default function InfrastructureDeploymentsPage() {
  const [deployments, setDeployments] =
    useState<InfrastructureDeployment[]>(mockDeployments);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showLogsModal, setShowLogsModal] = useState(false);
  const [showCostsModal, setShowCostsModal] = useState(false);
  const [selectedDeployment, setSelectedDeployment] =
    useState<InfrastructureDeployment | null>(null);
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [filterType, setFilterType] = useState<string>("all");

  const handleAddDeployment = () => {
    // Implementar lógica de creación
    setShowAddModal(false);
  };

  const handleStartDeployment = (id: number) => {
    setDeployments((prev) =>
      prev.map((d) =>
        d.id === id
          ? { ...d, status: "IN_PROGRESS", startedAt: new Date().toISOString() }
          : d
      )
    );
  };

  const handleStopDeployment = (id: number) => {
    setDeployments((prev) =>
      prev.map((d) =>
        d.id === id
          ? { ...d, status: "CANCELLED", completedAt: new Date().toISOString() }
          : d
      )
    );
  };

  const handleScaleDeployment = (id: number) => {
    // Implementar lógica de escalado
  };

  const getStatusBadge = (status: string) => {
    const statusConfig = {
      PENDING: { variant: "secondary", icon: Clock },
      IN_PROGRESS: { variant: "primary", icon: Activity },
      COMPLETED: { variant: "primary", icon: CheckCircle },
      FAILED: { variant: "danger", icon: XCircle },
      CANCELLED: { variant: "secondary", icon: Pause },
      ROLLING_BACK: { variant: "danger", icon: RotateCcw },
      ROLLED_BACK: { variant: "secondary", icon: RotateCcw },
    };

    const config =
      statusConfig[status as keyof typeof statusConfig] || statusConfig.PENDING;
    const Icon = config.icon;

    return (
      <Badge
        variant={config.variant as any}
        className="flex items-center gap-1"
      >
        <Icon className="w-3 h-3" />
        {status.replace("_", " ")}
      </Badge>
    );
  };

  const getTypeBadge = (type: string) => {
    const typeConfig = {
      MODEL_SERVING: { variant: "primary", color: "bg-blue-100 text-blue-800" },
      MODEL_TRAINING: {
        variant: "primary",
        color: "bg-green-100 text-green-800",
      },
      INFRASTRUCTURE_SCALING: {
        variant: "primary",
        color: "bg-purple-100 text-purple-800",
      },
      BACKUP_RESTORE: {
        variant: "primary",
        color: "bg-orange-100 text-orange-800",
      },
      DISASTER_RECOVERY: {
        variant: "primary",
        color: "bg-red-100 text-red-800",
      },
      MIGRATION: { variant: "primary", color: "bg-yellow-100 text-yellow-800" },
    };

    const config =
      typeConfig[type as keyof typeof typeConfig] || typeConfig.MODEL_SERVING;
    return (
      <Badge variant={config.variant as any} className={config.color}>
        {type.replace("_", " ")}
      </Badge>
    );
  };

  const filteredDeployments = deployments.filter((deployment) => {
    if (filterStatus !== "all" && deployment.status !== filterStatus)
      return false;
    if (filterType !== "all" && deployment.deploymentType !== filterType)
      return false;
    return true;
  });

  return (
    <div className="w-full p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center space-x-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Infrastructure Deployments
            </h1>
            <p className="text-gray-600 mt-2">
              Gestión y monitoreo de despliegues de infraestructura
            </p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button onClick={() => setShowAddModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            New Deployment
          </Button>
        </div>
      </div>

      {/* Filters */}
      <div className="flex gap-4">
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium">Status:</label>
          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            className="border rounded px-3 py-1"
          >
            <option value="all">All</option>
            <option value="PENDING">Pending</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
            <option value="FAILED">Failed</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </div>
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium">Type:</label>
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
            className="border rounded px-3 py-1"
          >
            <option value="all">All</option>
            <option value="MODEL_SERVING">Model Serving</option>
            <option value="MODEL_TRAINING">Model Training</option>
            <option value="INFRASTRUCTURE_SCALING">
              Infrastructure Scaling
            </option>
            <option value="BACKUP_RESTORE">Backup/Restore</option>
            <option value="DISASTER_RECOVERY">Disaster Recovery</option>
            <option value="MIGRATION">Migration</option>
          </select>
        </div>
      </div>

      {/* Deployments Table */}
      <div className="bg-white rounded-lg shadow">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Name
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Duration
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cost/Hour
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Cost
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredDeployments.map((deployment) => (
                <tr key={deployment.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div>
                      <div className="text-sm font-medium text-gray-900">
                        {deployment.deploymentName}
                      </div>
                      <div className="text-sm text-gray-500">
                        ID: {deployment.id}
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getTypeBadge(deployment.deploymentType)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getStatusBadge(deployment.status)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {deployment.deploymentDurationHours
                      ? `${deployment.deploymentDurationHours}h`
                      : "N/A"}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${deployment.estimatedCostPerHour?.toFixed(2) || "N/A"}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${deployment.totalCost?.toFixed(2) || "N/A"}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div className="flex gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedDeployment(deployment);
                          setShowDetailsModal(true);
                        }}
                      >
                        <FileText className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedDeployment(deployment);
                          setShowLogsModal(true);
                        }}
                      >
                        <Activity className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedDeployment(deployment);
                          setShowCostsModal(true);
                        }}
                      >
                        <DollarSign className="w-4 h-4" />
                      </Button>
                      {deployment.status === "PENDING" && (
                        <Button
                          size="sm"
                          onClick={() => handleStartDeployment(deployment.id)}
                        >
                          <Play className="w-4 h-4" />
                        </Button>
                      )}
                      {deployment.status === "IN_PROGRESS" && (
                        <Button
                          size="sm"
                          variant="danger"
                          onClick={() => handleStopDeployment(deployment.id)}
                        >
                          <Square className="w-4 h-4" />
                        </Button>
                      )}
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => handleScaleDeployment(deployment.id)}
                      >
                        <Scale className="w-4 h-4" />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Deployment Modal */}
      <SimpleModal
        isOpen={showAddModal}
        onClose={() => setShowAddModal(false)}
        title="New Infrastructure Deployment"
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Deployment Name
            </label>
            <Input placeholder="Enter deployment name" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Deployment Type
            </label>
            <select className="w-full border rounded px-3 py-2">
              <option value="MODEL_SERVING">Model Serving</option>
              <option value="MODEL_TRAINING">Model Training</option>
              <option value="INFRASTRUCTURE_SCALING">
                Infrastructure Scaling
              </option>
              <option value="BACKUP_RESTORE">Backup/Restore</option>
              <option value="DISASTER_RECOVERY">Disaster Recovery</option>
              <option value="MIGRATION">Migration</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Deployment Config (JSON)
            </label>
            <Textarea placeholder="Enter deployment configuration" rows={4} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Resource Requirements (JSON)
            </label>
            <Textarea placeholder="Enter resource requirements" rows={4} />
          </div>
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setShowAddModal(false)}>
              Cancel
            </Button>
            <Button onClick={handleAddDeployment}>Create Deployment</Button>
          </div>
        </div>
      </SimpleModal>

      {/* Deployment Details Modal */}
      <SimpleModal
        isOpen={showDetailsModal}
        onClose={() => setShowDetailsModal(false)}
        title={`Deployment Details: ${selectedDeployment?.deploymentName}`}
        maxWidth="max-w-2xl"
      >
        {selectedDeployment && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  ID
                </label>
                <p className="text-sm text-gray-900">{selectedDeployment.id}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Status
                </label>
                <p className="text-sm text-gray-900">
                  {getStatusBadge(selectedDeployment.status)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Type
                </label>
                <p className="text-sm text-gray-900">
                  {getTypeBadge(selectedDeployment.deploymentType)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Created
                </label>
                <p className="text-sm text-gray-900">
                  {new Date(selectedDeployment.createdAt).toLocaleString()}
                </p>
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Deployment Config
              </label>
              <pre className="text-sm bg-gray-100 p-2 rounded">
                {selectedDeployment.deploymentConfig}
              </pre>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Resource Requirements
              </label>
              <pre className="text-sm bg-gray-100 p-2 rounded">
                {selectedDeployment.resourceRequirements}
              </pre>
            </div>
          </div>
        )}
      </SimpleModal>

      {/* Deployment Logs Modal */}
      <SimpleModal
        isOpen={showLogsModal}
        onClose={() => setShowLogsModal(false)}
        title={`Deployment Logs: ${selectedDeployment?.deploymentName}`}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-2 max-h-96 overflow-y-auto">
          {mockDeploymentLogs.map((log) => (
            <div
              key={log.id}
              className="flex items-start gap-2 p-2 bg-gray-50 rounded"
            >
              <Badge
                variant={
                  log.logLevel === "ERROR"
                    ? "danger"
                    : log.logLevel === "WARNING"
                    ? "warning"
                    : "primary"
                }
              >
                {log.logLevel}
              </Badge>
              <div className="flex-1">
                <p className="text-sm text-gray-900">{log.message}</p>
                <p className="text-xs text-gray-500">
                  {log.source} - {new Date(log.timestamp).toLocaleString()}
                </p>
              </div>
            </div>
          ))}
        </div>
      </SimpleModal>

      {/* Deployment Costs Modal */}
      <SimpleModal
        isOpen={showCostsModal}
        onClose={() => setShowCostsModal(false)}
        title={`Deployment Costs: ${selectedDeployment?.deploymentName}`}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Type
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Resource
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Cost/Hour
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Hours
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Total
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {mockDeploymentCosts.map((cost) => (
                  <tr key={cost.id}>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {cost.costType}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {cost.resourceName}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      ${cost.costPerHour}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {cost.hoursUsed}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      ${cost.totalCost}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <div className="text-right">
            <p className="text-lg font-semibold">
              Total: $
              {mockDeploymentCosts
                .reduce((sum, cost) => sum + cost.totalCost, 0)
                .toFixed(2)}
            </p>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}
