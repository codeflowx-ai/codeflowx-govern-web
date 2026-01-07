"use client";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Textarea } from "@/components/ui/textarea";
import {
    Activity,
    BarChart3,
    Database,
    Edit,
    Eye,
    FileText,
    HardDrive,
    Loader,
    Network,
    Plus,
    Server,
    Settings,
    Shield,
    Trash2,
    Zap,
} from "lucide-react";
import { useState } from "react";

// Interfaces basadas en el documento
interface CloudResource {
  id: number;
  cloudProviderId: number;
  resourceType:
    | "LOAD_BALANCER"
    | "STORAGE"
    | "GATEWAY"
    | "ORCHESTRATOR"
    | "DATABASE"
    | "CACHE"
    | "MESSAGE_QUEUE"
    | "MONITORING"
    | "LOGGING"
    | "SECURITY";
  resourceName: string;
  resourceId?: string;
  description?: string;
  region?: string;
  zone?: string;
  specifications: string; // JSON con especificaciones
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

interface ResourceMetrics {
  id: number;
  resourceId: number;
  cpuUsage: number;
  memoryUsage: number;
  storageUsage: number;
  networkIn: number;
  networkOut: number;
  timestamp: string;
}

// Mock data
const mockResources: CloudResource[] = [
  {
    id: 1,
    cloudProviderId: 1,
    resourceType: "LOAD_BALANCER",
    resourceName: "Production Load Balancer",
    resourceId: "lb-prod-001",
    description: "Load balancer for production services",
    region: "us-east-1",
    zone: "us-east-1a",
    specifications:
      '{"type": "Application Load Balancer", "scheme": "internet-facing", "ip_address_type": "ipv4"}',
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
  },
  {
    id: 2,
    cloudProviderId: 1,
    resourceType: "STORAGE",
    resourceName: "Model Storage S3",
    resourceId: "s3-models-001",
    description: "S3 bucket for AI model storage",
    region: "us-east-1",
    zone: "us-east-1a",
    specifications:
      '{"type": "S3 Standard", "versioning": true, "encryption": "AES256", "lifecycle": "IA"}',
    costPerHour: 0.000023,
    costPerMonth: 0.017,
    isAutoScaling: false,
    minInstances: 1,
    maxInstances: 1,
    currentInstances: 1,
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "AWS",
  },
  {
    id: 3,
    cloudProviderId: 1,
    resourceType: "DATABASE",
    resourceName: "PostgreSQL RDS",
    resourceId: "rds-postgres-001",
    description: "PostgreSQL database for application data",
    region: "us-east-1",
    zone: "us-east-1a",
    specifications:
      '{"engine": "postgres", "version": "14.7", "instance_class": "db.t3.micro", "storage": "20GB"}',
    costPerHour: 0.017,
    costPerMonth: 12.24,
    isAutoScaling: false,
    minInstances: 1,
    maxInstances: 1,
    currentInstances: 1,
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "AWS",
  },
  {
    id: 4,
    cloudProviderId: 1,
    resourceType: "CACHE",
    resourceName: "Redis ElastiCache",
    resourceId: "redis-cache-001",
    description: "Redis cache for session management",
    region: "us-east-1",
    zone: "us-east-1a",
    specifications:
      '{"engine": "redis", "version": "7.0", "node_type": "cache.t3.micro", "num_cache_nodes": 1}',
    costPerHour: 0.017,
    costPerMonth: 12.24,
    isAutoScaling: false,
    minInstances: 1,
    maxInstances: 1,
    currentInstances: 1,
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "AWS",
  },
  {
    id: 5,
    cloudProviderId: 1,
    resourceType: "MONITORING",
    resourceName: "CloudWatch Monitoring",
    resourceId: "cw-monitoring-001",
    description: "CloudWatch monitoring and alerting",
    region: "us-east-1",
    zone: "us-east-1a",
    specifications:
      '{"metrics": ["CPU", "Memory", "Network"], "alerts": true, "logs": true, "dashboards": true}',
    costPerHour: 0.0003,
    costPerMonth: 0.216,
    isAutoScaling: false,
    minInstances: 1,
    maxInstances: 1,
    currentInstances: 1,
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "AWS",
  },
];

const mockResourceMetrics: ResourceMetrics[] = [
  {
    id: 1,
    resourceId: 1,
    cpuUsage: 45,
    memoryUsage: 62,
    storageUsage: 28,
    networkIn: 1250,
    networkOut: 890,
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    id: 2,
    resourceId: 2,
    cpuUsage: 0,
    memoryUsage: 0,
    storageUsage: 75,
    networkIn: 450,
    networkOut: 120,
    timestamp: "2024-01-15T10:00:00Z",
  },
];

export default function CloudResourcesPage() {
  const [resources, setResources] = useState<CloudResource[]>(mockResources);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showMetricsModal, setShowMetricsModal] = useState(false);
  const [selectedResource, setSelectedResource] =
    useState<CloudResource | null>(null);
  const [filterType, setFilterType] = useState<string>("all");
  const [filterProvider, setFilterProvider] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");

  const handleAddResource = () => {
    // Implementar lógica de creación
    setShowAddModal(false);
  };

  const handleEditResource = () => {
    // Implementar lógica de edición
    setShowEditModal(false);
  };

  const handleDeleteResource = (id: number) => {
    setResources((prev) => prev.filter((r) => r.id !== id));
  };

  const getResourceTypeIcon = (type: string) => {
    const iconMap = {
      LOAD_BALANCER: Loader,
      STORAGE: HardDrive,
      GATEWAY: Network,
      ORCHESTRATOR: Settings,
      DATABASE: Database,
      CACHE: Zap,
      MESSAGE_QUEUE: Activity,
      MONITORING: BarChart3,
      LOGGING: FileText,
      SECURITY: Shield,
    };

    const Icon = iconMap[type as keyof typeof iconMap] || Server;
    return <Icon className="w-5 h-5" />;
  };

  const getResourceTypeBadge = (type: string) => {
    const typeConfig = {
      LOAD_BALANCER: { variant: "primary", color: "bg-blue-100 text-blue-800" },
      STORAGE: { variant: "primary", color: "bg-green-100 text-green-800" },
      GATEWAY: { variant: "primary", color: "bg-purple-100 text-purple-800" },
      ORCHESTRATOR: {
        variant: "primary",
        color: "bg-orange-100 text-orange-800",
      },
      DATABASE: { variant: "primary", color: "bg-red-100 text-red-800" },
      CACHE: { variant: "primary", color: "bg-yellow-100 text-yellow-800" },
      MESSAGE_QUEUE: {
        variant: "primary",
        color: "bg-indigo-100 text-indigo-800",
      },
      MONITORING: { variant: "primary", color: "bg-pink-100 text-pink-800" },
      LOGGING: { variant: "primary", color: "bg-gray-100 text-gray-800" },
      SECURITY: { variant: "primary", color: "bg-red-100 text-red-800" },
    };

    const config =
      typeConfig[type as keyof typeof typeConfig] || typeConfig.STORAGE;
    return (
      <Badge variant={config.variant as any} className={config.color}>

        {type.replace("_", " ")}
      </Badge>
    );
  };

  const getStatusBadge = (isActive: boolean) => {
    return (
      <Badge variant={isActive ? "primary" : "secondary"}>
        {isActive ? "Active" : "Inactive"}
      </Badge>
    );
  };

  const filteredResources = resources.filter((resource) => {
    if (filterType !== "all" && resource.resourceType !== filterType)
      return false;
    if (filterProvider !== "all" && resource.cloudProvider !== filterProvider)
      return false;
    if (
      filterStatus !== "all" &&
      resource.isActive !== (filterStatus === "active")
    )
      return false;
    return true;
  });

  const getTotalCosts = () => {
    const totalHourly = resources.reduce(
      (sum, r) => sum + (r.costPerHour || 0),
      0
    );
    const totalMonthly = resources.reduce(
      (sum, r) => sum + (r.costPerMonth || 0),
      0
    );
    return { hourly: totalHourly, monthly: totalMonthly };
  };

  const totalCosts = getTotalCosts();

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header - Título y subtítulo alineados a la izquierda */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                Cloud Resources
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              Gestión de recursos cloud: balanceadores, almacenamiento, bases de datos y más
            </p>
          </div>
          <Button onClick={() => setShowAddModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            New Resource
          </Button>
        </div>

      {/* Cost Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Resources
            </CardTitle>
            <Server className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{resources.length}</div>
            <p className="text-xs text-muted-foreground">
              Active: {resources.filter((r) => r.isActive).length}
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Hourly Cost</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${totalCosts.hourly.toFixed(4)}
            </div>
            <p className="text-xs text-muted-foreground">Per hour</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Monthly Cost</CardTitle>
            <BarChart3 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${totalCosts.monthly.toFixed(2)}
            </div>
            <p className="text-xs text-muted-foreground">Estimated monthly</p>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <div className="flex gap-4">
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium">Type:</label>
          <select
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
            className="border rounded px-3 py-1"
          >
            <option value="all">All Types</option>
            <option value="LOAD_BALANCER">Load Balancer</option>
            <option value="STORAGE">Storage</option>
            <option value="GATEWAY">Gateway</option>
            <option value="ORCHESTRATOR">Orchestrator</option>
            <option value="DATABASE">Database</option>
            <option value="CACHE">Cache</option>
            <option value="MESSAGE_QUEUE">Message Queue</option>
            <option value="MONITORING">Monitoring</option>
            <option value="LOGGING">Logging</option>
            <option value="SECURITY">Security</option>
          </select>
        </div>
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium">Provider:</label>
          <select
            value={filterProvider}
            onChange={(e) => setFilterProvider(e.target.value)}
            className="border rounded px-3 py-1"
          >
            <option value="all">All Providers</option>
            <option value="AWS">AWS</option>
            <option value="Azure">Azure</option>
            <option value="Google Cloud">Google Cloud</option>
            <option value="OVH">OVH</option>
          </select>
        </div>
        <div className="flex items-center gap-2">
          <label className="text-sm font-medium">Status:</label>
          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            className="border rounded px-3 py-1"
          >
            <option value="all">All Status</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
          </select>
        </div>
      </div>

      {/* Resources Table */}
      <div className="bg-white rounded-lg shadow">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Resource
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Type
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Provider
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Region
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cost/Hour
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Instances
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredResources.map((resource) => (
                <tr key={resource.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10">
                        <div className="h-10 w-10 rounded-full bg-gray-100 flex items-center justify-center">
                          {getResourceTypeIcon(resource.resourceType)}
                        </div>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {resource.resourceName}
                        </div>
                        <div className="text-sm text-gray-500">
                          {resource.description}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getResourceTypeBadge(resource.resourceType)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {resource.cloudProvider}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {resource.region}
                    {resource.zone && (
                      <span className="text-gray-500 ml-1">
                        ({resource.zone})
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getStatusBadge(resource.isActive)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${resource.costPerHour?.toFixed(4) || "N/A"}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {resource.currentInstances}
                    {resource.isAutoScaling && (
                      <span className="text-gray-500 ml-1">
                        ({resource.minInstances}-{resource.maxInstances})
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div className="flex gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedResource(resource);
                          setShowDetailsModal(true);
                        }}
                      >
                        <Eye className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedResource(resource);
                          setShowMetricsModal(true);
                        }}
                      >
                        <BarChart3 className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedResource(resource);
                          setShowEditModal(true);
                        }}
                      >
                        <Edit className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="danger"
                        onClick={() => handleDeleteResource(resource.id)}
                      >
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Resource Modal */}
      <SimpleModal
        isOpen={showAddModal}
        onClose={() => setShowAddModal(false)}
        title="New Cloud Resource"
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Resource Name
            </label>
            <Input placeholder="Enter resource name" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Resource Type
            </label>
            <select className="w-full border rounded px-3 py-2">
              <option value="LOAD_BALANCER">Load Balancer</option>
              <option value="STORAGE">Storage</option>
              <option value="GATEWAY">Gateway</option>
              <option value="ORCHESTRATOR">Orchestrator</option>
              <option value="DATABASE">Database</option>
              <option value="CACHE">Cache</option>
              <option value="MESSAGE_QUEUE">Message Queue</option>
              <option value="MONITORING">Monitoring</option>
              <option value="LOGGING">Logging</option>
              <option value="SECURITY">Security</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Description
            </label>
            <Textarea placeholder="Enter resource description" rows={3} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Region
              </label>
              <Input placeholder="e.g., us-east-1" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Zone
              </label>
              <Input placeholder="e.g., us-east-1a" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Specifications (JSON)
            </label>
            <Textarea placeholder="Enter resource specifications" rows={4} />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Cost per Hour
              </label>
              <Input type="number" step="0.0001" placeholder="0.00" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Cost per Month
              </label>
              <Input type="number" step="0.01" placeholder="0.00" />
            </div>
          </div>
          <div className="flex items-center gap-2">
            <input type="checkbox" id="autoScaling" />
            <label
              htmlFor="autoScaling"
              className="text-sm font-medium text-gray-700"
            >
              Enable Auto-scaling
            </label>
          </div>
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setShowAddModal(false)}>
              Cancel
            </Button>
            <Button onClick={handleAddResource}>Create Resource</Button>
          </div>
        </div>
      </SimpleModal>

      {/* Resource Details Modal */}
      <SimpleModal
        isOpen={showDetailsModal}
        onClose={() => setShowDetailsModal(false)}
        title={`Resource Details: ${selectedResource?.resourceName}`}
        maxWidth="max-w-2xl"
      >
        {selectedResource && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  ID
                </label>
                <p className="text-sm text-gray-900">{selectedResource.id}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Type
                </label>
                <p className="text-sm text-gray-900">
                  {getResourceTypeBadge(selectedResource.resourceType)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Provider
                </label>
                <p className="text-sm text-gray-900">
                  {selectedResource.cloudProvider}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Status
                </label>
                <p className="text-sm text-gray-900">
                  {getStatusBadge(selectedResource.isActive)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Region
                </label>
                <p className="text-sm text-gray-900">
                  {selectedResource.region}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Zone
                </label>
                <p className="text-sm text-gray-900">
                  {selectedResource.zone || "N/A"}
                </p>
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Description
              </label>
              <p className="text-sm text-gray-900">
                {selectedResource.description || "No description"}
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Specifications
              </label>
              <pre className="text-sm bg-gray-100 p-2 rounded">
                {selectedResource.specifications}
              </pre>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Cost per Hour
                </label>
                <p className="text-sm text-gray-900">
                  ${selectedResource.costPerHour?.toFixed(4) || "N/A"}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Cost per Month
                </label>
                <p className="text-sm text-gray-900">
                  ${selectedResource.costPerMonth?.toFixed(2) || "N/A"}
                </p>
              </div>
            </div>
          </div>
        )}
      </SimpleModal>

      {/* Resource Metrics Modal */}
      <SimpleModal
        isOpen={showMetricsModal}
        onClose={() => setShowMetricsModal(false)}
        title={`Resource Metrics: ${selectedResource?.resourceName}`}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Card>
              <CardHeader>
                <CardTitle className="text-sm">CPU Usage</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold text-blue-600">45%</div>
              </CardContent>
            </Card>
            <Card>
              <CardHeader>
                <CardTitle className="text-sm">Memory Usage</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold text-green-600">62%</div>
              </CardContent>
            </Card>
            <Card>
              <CardHeader>
                <CardTitle className="text-sm">Storage Usage</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold text-orange-600">28%</div>
              </CardContent>
            </Card>
            <Card>
              <CardHeader>
                <CardTitle className="text-sm">Network I/O</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-sm">
                  <div>In: 1.25 MB/s</div>
                  <div>Out: 890 KB/s</div>
                </div>
              </CardContent>
            </Card>
          </div>
          <div>
            <h3 className="text-lg font-medium mb-2">Recent Metrics</h3>
            <div className="space-y-2 max-h-64 overflow-y-auto">
              {mockResourceMetrics.map((metric) => (
                <div
                  key={metric.id}
                  className="flex justify-between items-center p-2 bg-gray-50 rounded"
                >
                  <span className="text-sm text-gray-600">
                    {new Date(metric.timestamp).toLocaleString()}
                  </span>
                  <div className="flex gap-4 text-sm">
                    <span>CPU: {metric.cpuUsage}%</span>
                    <span>Memory: {metric.memoryUsage}%</span>
                    <span>Storage: {metric.storageUsage}%</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </SimpleModal>
      </div>
    </div>
  );
}


