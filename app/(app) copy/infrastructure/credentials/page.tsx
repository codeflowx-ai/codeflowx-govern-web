"use client";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Textarea } from "@/components/ui/textarea";
import {
    Activity,
    AlertTriangle,
    CheckCircle,
    Copy,
    Edit,
    Eye,
    EyeOff,
    Key,
    Lock,
    Plus,
    RotateCcw,
    Server,
    Shield,
    Trash2,
} from "lucide-react";
import { useState } from "react";

// Interfaces basadas en el documento
interface ProviderCredential {
  id: number;
  credentialName: string;
  credentialType:
    | "API_KEY_SECRET"
    | "OAUTH_TOKEN"
    | "SERVICE_ACCOUNT"
    | "KUBECONFIG"
    | "USERNAME_PASSWORD"
    | "CERTIFICATE"
    | "SSH_KEY";
  accessKey?: string;
  secretKey?: string;
  apiToken?: string;
  kubeconfig?: string;
  region: string;
  projectId?: string;
  subscriptionId?: string;
  tenantId?: string;
  isDefault: boolean;
  isActive: boolean;
  expiresAt?: string;
  lastUsed?: string;
  usageCount: number;
  createdAt: string;
  updatedAt: string;
  cloudProvider: string;
  cloudProviderId: number;
}

interface CredentialAudit {
  id: number;
  credentialId: number;
  action:
    | "CREATED"
    | "UPDATED"
    | "DELETED"
    | "ROTATED"
    | "ACCESSED"
    | "FAILED_ACCESS";
  userId: number;
  userName: string;
  timestamp: string;
  ipAddress: string;
  userAgent: string;
  details?: string;
}

// Mock data
const mockCredentials: ProviderCredential[] = [
  {
    id: 1,
    credentialName: "AWS Production Access",
    credentialType: "API_KEY_SECRET",
    accessKey: "AKIAIOSFODNN7EXAMPLE",
    secretKey: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",
    region: "us-east-1",
    isDefault: true,
    isActive: true,
    usageCount: 1250,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "AWS",
    cloudProviderId: 1,
  },
  {
    id: 2,
    credentialName: "Azure Kubernetes Cluster",
    credentialType: "KUBECONFIG",
    kubeconfig:
      "apiVersion: v1\nkind: Config\nclusters:\n- name: azure-cluster\n  cluster:\n    server: https://azure-cluster.azmk8s.io:443",
    region: "East US",
    subscriptionId: "12345678-1234-1234-1234-123456789012",
    tenantId: "87654321-4321-4321-4321-210987654321",
    isDefault: false,
    isActive: true,
    usageCount: 89,
    createdAt: "2024-01-05T00:00:00Z",
    updatedAt: "2024-01-14T00:00:00Z",
    cloudProvider: "Azure",
    cloudProviderId: 2,
  },
  {
    id: 3,
    credentialName: "Google Cloud Service Account",
    credentialType: "SERVICE_ACCOUNT",
    apiToken: "ya29.a0AfH6SMC...",
    region: "us-central1",
    projectId: "my-project-123456",
    isDefault: false,
    isActive: true,
    expiresAt: "2024-12-31T23:59:59Z",
    usageCount: 456,
    createdAt: "2024-01-10T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "Google Cloud",
    cloudProviderId: 3,
  },
  {
    id: 4,
    credentialName: "OVH SSH Access",
    credentialType: "SSH_KEY",
    region: "GRA",
    isDefault: false,
    isActive: true,
    usageCount: 23,
    createdAt: "2024-01-12T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "OVH",
    cloudProviderId: 4,
  },
  {
    id: 5,
    credentialName: "RunPod API Token",
    credentialType: "API_KEY_SECRET",
    apiToken: "rp_1234567890abcdef...",
    region: "US East",
    isDefault: false,
    isActive: true,
    usageCount: 67,
    createdAt: "2024-01-13T00:00:00Z",
    updatedAt: "2024-01-15T00:00:00Z",
    cloudProvider: "RunPod",
    cloudProviderId: 5,
  },
];

const mockCredentialAudits: CredentialAudit[] = [
  {
    id: 1,
    credentialId: 1,
    action: "ACCESSED",
    userId: 1,
    userName: "admin@codeflowx.com",
    timestamp: "2024-01-15T10:00:00Z",
    ipAddress: "192.168.1.100",
    userAgent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
    details: "Kubernetes cluster deployment",
  },
  {
    id: 2,
    credentialId: 1,
    action: "ROTATED",
    userId: 1,
    userName: "admin@codeflowx.com",
    timestamp: "2024-01-14T15:30:00Z",
    ipAddress: "192.168.1.100",
    userAgent: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
    details: "Scheduled credential rotation",
  },
  {
    id: 3,
    credentialId: 2,
    action: "ACCESSED",
    userId: 2,
    userName: "dev@codeflowx.com",
    timestamp: "2024-01-15T09:15:00Z",
    ipAddress: "192.168.1.101",
    userAgent:
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36",
    details: "Application deployment",
  },
];

export default function ProviderCredentialsPage() {
  const [credentials, setCredentials] =
    useState<ProviderCredential[]>(mockCredentials);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showAuditModal, setShowAuditModal] = useState(false);
  const [showRotateModal, setShowRotateModal] = useState(false);
  const [selectedCredential, setSelectedCredential] =
    useState<ProviderCredential | null>(null);
  const [filterType, setFilterType] = useState<string>("all");
  const [filterProvider, setFilterProvider] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [showSecrets, setShowSecrets] = useState<{ [key: number]: boolean }>(
    {}
  );

  const handleAddCredential = () => {
    // Implementar lógica de creación
    setShowAddModal(false);
  };

  const handleEditCredential = () => {
    // Implementar lógica de edición
    setShowEditModal(false);
  };

  const handleDeleteCredential = (id: number) => {
    setCredentials((prev) => prev.filter((c) => c.id !== id));
  };

  const handleRotateCredential = (id: number) => {
    // Implementar lógica de rotación
    setShowRotateModal(false);
  };

  const toggleSecretVisibility = (id: number) => {
    setShowSecrets((prev) => ({
      ...prev,
      [id]: !prev[id],
    }));
  };

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
  };

  const getCredentialTypeIcon = (type: string) => {
    const iconMap = {
      API_KEY_SECRET: Key,
      OAUTH_TOKEN: Lock,
      SERVICE_ACCOUNT: Server,
      KUBECONFIG: Server,
      USERNAME_PASSWORD: Key,
      CERTIFICATE: Shield,
      SSH_KEY: Key,
    };

    const Icon = iconMap[type as keyof typeof iconMap] || Key;
    return <Icon className="w-5 h-5" />;
  };

  const getCredentialTypeBadge = (type: string) => {
    const typeConfig = {
      API_KEY_SECRET: {
        variant: "primary",
        color: "bg-blue-100 text-blue-800",
      },
      OAUTH_TOKEN: { variant: "primary", color: "bg-green-100 text-green-800" },
      SERVICE_ACCOUNT: {
        variant: "primary",
        color: "bg-purple-100 text-purple-800",
      },
      KUBECONFIG: {
        variant: "primary",
        color: "bg-orange-100 text-orange-800",
      },
      USERNAME_PASSWORD: {
        variant: "primary",
        color: "bg-red-100 text-red-800",
      },
      CERTIFICATE: {
        variant: "primary",
        color: "bg-yellow-100 text-yellow-800",
      },
      SSH_KEY: { variant: "primary", color: "bg-indigo-100 text-indigo-800" },
    };

    const config =
      typeConfig[type as keyof typeof typeConfig] || typeConfig.API_KEY_SECRET;
    return (
      <Badge variant={config.variant as any} className={config.color}>

        {type.replace("_", " ")}
      </Badge>
    );
  };

  const getStatusBadge = (isActive: boolean, isDefault: boolean) => {
    if (isDefault) {
      return (
        <Badge variant="primary" className="bg-purple-100 text-purple-800">
          Default
        </Badge>
      );
    }
    return (
      <Badge variant={isActive ? "primary" : "secondary"}>
        {isActive ? "Active" : "Inactive"}
      </Badge>
    );
  };

  const getExpiryStatus = (expiresAt?: string) => {
    if (!expiresAt) return null;

    const expiryDate = new Date(expiresAt);
    const now = new Date();
    const daysUntilExpiry = Math.ceil(
      (expiryDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24)
    );

    if (daysUntilExpiry < 0) {
      return <Badge variant="danger">Expired</Badge>;
    } else if (daysUntilExpiry <= 7) {
      return <Badge variant="danger">Expires Soon</Badge>;
    } else if (daysUntilExpiry <= 30) {
      return (
        <Badge variant="secondary">Expires in {daysUntilExpiry} days</Badge>
      );
    }

    return null;
  };

  const filteredCredentials = credentials.filter((credential) => {
    if (filterType !== "all" && credential.credentialType !== filterType)
      return false;
    if (filterProvider !== "all" && credential.cloudProvider !== filterProvider)
      return false;
    if (
      filterStatus !== "all" &&
      credential.isActive !== (filterStatus === "active")
    )
      return false;
    return true;
  });

  const getTotalCredentials = () => {
    return {
      total: credentials.length,
      active: credentials.filter((c) => c.isActive).length,
      default: credentials.filter((c) => c.isDefault).length,
      expired: credentials.filter(
        (c) => c.expiresAt && new Date(c.expiresAt) < new Date()
      ).length,
    };
  };

  const totalStats = getTotalCredentials();

  return (
    <div className="w-full p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center space-x-4">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Provider Credentials
            </h1>
            <p className="text-gray-600 mt-2">
              Gestión segura de credenciales y tokens de proveedores cloud
            </p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button onClick={() => setShowAddModal(true)}>
            <Plus className="w-4 h-4 mr-2" />
            New Credential
          </Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Total Credentials
            </CardTitle>
            <Key className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{totalStats.total}</div>
            <p className="text-xs text-muted-foreground">
              Across all providers
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Active</CardTitle>
            <CheckCircle className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {totalStats.active}
            </div>
            <p className="text-xs text-muted-foreground">Currently active</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Default</CardTitle>
            <Shield className="h-4 w-4 text-purple-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-purple-600">
              {totalStats.default}
            </div>
            <p className="text-xs text-muted-foreground">Default credentials</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Expired</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">
              {totalStats.expired}
            </div>
            <p className="text-xs text-muted-foreground">Need rotation</p>
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
            <option value="API_KEY_SECRET">API Key/Secret</option>
            <option value="OAUTH_TOKEN">OAuth Token</option>
            <option value="SERVICE_ACCOUNT">Service Account</option>
            <option value="KUBECONFIG">Kubeconfig</option>
            <option value="USERNAME_PASSWORD">Username/Password</option>
            <option value="CERTIFICATE">Certificate</option>
            <option value="SSH_KEY">SSH Key</option>
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
            <option value="RunPod">RunPod</option>
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

      {/* Credentials Table */}
      <div className="bg-white rounded-lg shadow">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Credential
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
                  Usage
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Expiry
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredCredentials.map((credential) => (
                <tr key={credential.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10">
                        <div className="h-10 w-10 rounded-full bg-gray-100 flex items-center justify-center">
                          {getCredentialTypeIcon(credential.credentialType)}
                        </div>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {credential.credentialName}
                        </div>
                        <div className="text-sm text-gray-500">
                          ID: {credential.id}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getCredentialTypeBadge(credential.credentialType)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {credential.cloudProvider}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {credential.region}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getStatusBadge(credential.isActive, credential.isDefault)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {credential.usageCount} times
                    {credential.lastUsed && (
                      <div className="text-xs text-gray-500">
                        Last:{" "}
                        {new Date(credential.lastUsed).toLocaleDateString()}
                      </div>
                    )}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    {getExpiryStatus(credential.expiresAt)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div className="flex gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedCredential(credential);
                          setShowDetailsModal(true);
                        }}
                      >
                        <Eye className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedCredential(credential);
                          setShowAuditModal(true);
                        }}
                      >
                        <Activity className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedCredential(credential);
                          setShowRotateModal(true);
                        }}
                      >
                        <RotateCcw className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => {
                          setSelectedCredential(credential);
                          setShowEditModal(true);
                        }}
                      >
                        <Edit className="w-4 h-4" />
                      </Button>
                      <Button
                        size="sm"
                        variant="danger"
                        onClick={() => handleDeleteCredential(credential.id)}
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

      {/* Add Credential Modal */}
      <SimpleModal
        isOpen={showAddModal}
        onClose={() => setShowAddModal(false)}
        title="New Provider Credential"
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Credential Name
            </label>
            <Input placeholder="Enter credential name" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Credential Type
            </label>
            <select className="w-full border rounded px-3 py-2">
              <option value="API_KEY_SECRET">API Key/Secret</option>
              <option value="OAUTH_TOKEN">OAuth Token</option>
              <option value="SERVICE_ACCOUNT">Service Account</option>
              <option value="KUBECONFIG">Kubeconfig</option>
              <option value="USERNAME_PASSWORD">Username/Password</option>
              <option value="CERTIFICATE">Certificate</option>
              <option value="SSH_KEY">SSH Key</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Cloud Provider
            </label>
            <select className="w-full border rounded px-3 py-2">
              <option value="AWS">AWS</option>
              <option value="Azure">Azure</option>
              <option value="Google Cloud">Google Cloud</option>
              <option value="OVH">OVH</option>
              <option value="RunPod">RunPod</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Region
            </label>
            <Input placeholder="e.g., us-east-1" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Access Key/Token
            </label>
            <Input placeholder="Enter access key or token" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Secret Key (if applicable)
            </label>
            <Input type="password" placeholder="Enter secret key" />
          </div>
          <div className="flex items-center gap-2">
            <input type="checkbox" id="isDefault" />
            <label
              htmlFor="isDefault"
              className="text-sm font-medium text-gray-700"
            >
              Set as default credential
            </label>
          </div>
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setShowAddModal(false)}>
              Cancel
            </Button>
            <Button onClick={handleAddCredential}>Create Credential</Button>
          </div>
        </div>
      </SimpleModal>

      {/* Credential Details Modal */}
      <SimpleModal
        isOpen={showDetailsModal}
        onClose={() => setShowDetailsModal(false)}
        title={`Credential Details: ${selectedCredential?.credentialName}`}
        maxWidth="max-w-2xl"
      >
        {selectedCredential && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  ID
                </label>
                <p className="text-sm text-gray-900">{selectedCredential.id}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Type
                </label>
                <p className="text-sm text-gray-900">
                  {getCredentialTypeBadge(selectedCredential.credentialType)}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Provider
                </label>
                <p className="text-sm text-gray-900">
                  {selectedCredential.cloudProvider}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Region
                </label>
                <p className="text-sm text-gray-900">
                  {selectedCredential.region}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Status
                </label>
                <p className="text-sm text-gray-900">
                  {getStatusBadge(
                    selectedCredential.isActive,
                    selectedCredential.isDefault
                  )}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Usage Count
                </label>
                <p className="text-sm text-gray-900">
                  {selectedCredential.usageCount} times
                </p>
              </div>
            </div>

            {selectedCredential.accessKey && (
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Access Key
                </label>
                <div className="flex gap-2">
                  <Input
                    value={selectedCredential.accessKey}
                    readOnly
                    className="font-mono text-sm"
                  />
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() =>
                      copyToClipboard(selectedCredential.accessKey!)
                    }
                  >
                    <Copy className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            )}

            {selectedCredential.secretKey && (
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Secret Key
                </label>
                <div className="flex gap-2">
                  <Input
                    type={
                      showSecrets[selectedCredential.id] ? "text" : "password"
                    }
                    value={selectedCredential.secretKey}
                    readOnly
                    className="font-mono text-sm"
                  />
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() =>
                      toggleSecretVisibility(selectedCredential.id)
                    }
                  >
                    {showSecrets[selectedCredential.id] ? (
                      <EyeOff className="w-4 h-4" />
                    ) : (
                      <Eye className="w-4 h-4" />
                    )}
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() =>
                      copyToClipboard(selectedCredential.secretKey!)
                    }
                  >
                    <Copy className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            )}

            {selectedCredential.apiToken && (
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  API Token
                </label>
                <div className="flex gap-2">
                  <Input
                    type={
                      showSecrets[selectedCredential.id] ? "text" : "password"
                    }
                    value={selectedCredential.apiToken}
                    readOnly
                    className="font-mono text-sm"
                  />
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() =>
                      toggleSecretVisibility(selectedCredential.id)
                    }
                  >
                    {showSecrets[selectedCredential.id] ? (
                      <EyeOff className="w-4 h-4" />
                    ) : (
                      <Eye className="w-4 h-4" />
                    )}
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() =>
                      copyToClipboard(selectedCredential.apiToken!)
                    }
                  >
                    <Copy className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            )}

            {selectedCredential.kubeconfig && (
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Kubeconfig
                </label>
                <Textarea
                  value={selectedCredential.kubeconfig}
                  readOnly
                  rows={6}
                  className="font-mono text-sm"
                />
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Created
                </label>
                <p className="text-sm text-gray-900">
                  {new Date(selectedCredential.createdAt).toLocaleString()}
                </p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Last Updated
                </label>
                <p className="text-sm text-gray-900">
                  {new Date(selectedCredential.updatedAt).toLocaleString()}
                </p>
              </div>
            </div>
          </div>
        )}
      </SimpleModal>

      {/* Credential Audit Modal */}
      <SimpleModal
        isOpen={showAuditModal}
        onClose={() => setShowAuditModal(false)}
        title={`Credential Audit: ${selectedCredential?.credentialName}`}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Action
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    User
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Timestamp
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    IP Address
                  </th>
                  <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">
                    Details
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-200">
                {mockCredentialAudits.map((audit) => (
                  <tr key={audit.id}>
                    <td className="px-4 py-2 text-sm">
                      <Badge
                        variant={
                          audit.action === "FAILED_ACCESS"
                            ? "danger"
                            : "primary"
                        }
                      >
                        {audit.action.replace("_", " ")}
                      </Badge>
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {audit.userName}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {new Date(audit.timestamp).toLocaleString()}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {audit.ipAddress}
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-900">
                      {audit.details || "N/A"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </SimpleModal>

      {/* Rotate Credential Modal */}
      <SimpleModal
        isOpen={showRotateModal}
        onClose={() => setShowRotateModal(false)}
        title={`Rotate Credential: ${selectedCredential?.credentialName}`}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="bg-yellow-50 border border-yellow-200 rounded-md p-4">
            <div className="flex">
              <AlertTriangle className="h-5 w-5 text-yellow-400" />
              <div className="ml-3">
                <h3 className="text-sm font-medium text-yellow-800">
                  Credential Rotation
                </h3>
                <div className="mt-2 text-sm text-yellow-700">
                  <p>
                    This will generate new credentials and invalidate the old
                    ones.
                  </p>
                  <p className="mt-1">
                    Make sure to update any systems using these credentials.
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              New Access Key/Token
            </label>
            <Input placeholder="Enter new access key or token" />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              New Secret Key (if applicable)
            </label>
            <Input type="password" placeholder="Enter new secret key" />
          </div>

          <div className="flex items-center gap-2">
            <input type="checkbox" id="invalidateOld" defaultChecked />
            <label
              htmlFor="invalidateOld"
              className="text-sm font-medium text-gray-700"
            >
              Invalidate old credentials immediately
            </label>
          </div>

          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={() => setShowRotateModal(false)}>
              Cancel
            </Button>
            <Button
              onClick={() =>
                handleRotateCredential(selectedCredential?.id || 0)
              }
            >
              Rotate Credential
            </Button>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}
