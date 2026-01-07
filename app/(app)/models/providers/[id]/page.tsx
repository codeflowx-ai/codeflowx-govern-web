"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { SimpleModal } from "@/components/ui/SimpleModal";
import {
  ArrowLeft,
  BarChart3,
  DollarSign,
  Edit,
  Eye,
  Key,
  Plus,
  Save,
  Server,
  Trash2,
  TrendingUp,
} from "lucide-react";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface Provider {
  id: number;
  name: string;
  displayName: string;
  description: string;
  providerType: string;
  status: string;
  authType: string;
  baseUrl: string;
  credentials?: ProviderCredential[];
  associatedModels?: AssociatedModel[];
  associatedProjects?: AssociatedProject[];
  costs?: CostData;
  kpis?: KPIData;
}

interface ProviderCredential {
  id: number;
  name: string;
  type: string;
  environment: string;
  status: string;
  projectId?: number;
  projectName?: string;
  expiresAt?: string;
  lastUsedAt?: string;
  usageCount: number;
  rotationEnabled?: boolean;
  rotationDays?: number;
  dailyQuotaTokens?: number;
  monthlyBudget?: number;
}

interface AssociatedModel {
  id: number;
  name: string;
  version: string;
  status: string;
  lastUsedAt?: string;
}

interface AssociatedProject {
  id: number;
  name: string;
  credentialCount: number;
  lastUsedAt?: string;
}

interface CostData {
  totalMonthly: number;
  totalDaily: number;
  byCredential: Array<{
    credentialId: number;
    credentialName: string;
    monthly: number;
    daily: number;
  }>;
  trends: Array<{
    date: string;
    cost: number;
  }>;
}

interface KPIData {
  totalRequests: number;
  successRate: number;
  averageLatency: number;
  uptime: number;
  activeCredentials: number;
  expiredCredentials: number;
}

interface Project {
  id: number;
  name: string;
}

export default function ProviderDetailPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const id = params.id as string;
  const isNew = id === "new";

  // Verificar si hay parámetro edit en la URL
  const [isEditMode, setIsEditMode] = useState(false);
  useEffect(() => {
    if (typeof window !== 'undefined') {
      const searchParams = new URLSearchParams(window.location.search);
      setIsEditMode(searchParams.get('edit') === 'true');
    }
  }, []);

  const [loading, setLoading] = useState(!isNew);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState(isNew || isEditMode);
  const [provider, setProvider] = useState<Provider>({
    id: 0,
    name: "",
    displayName: "",
    description: "",
    providerType: "",
    status: "",
    authType: "",
    baseUrl: "",
    credentials: [],
    associatedModels: [],
    associatedProjects: [],
    costs: {
      totalMonthly: 0,
      totalDaily: 0,
      byCredential: [],
      trends: [],
    },
    kpis: {
      totalRequests: 0,
      successRate: 0,
      averageLatency: 0,
      uptime: 0,
      activeCredentials: 0,
      expiredCredentials: 0,
    },
  });
  const [projects, setProjects] = useState<Project[]>([]);
  const [newCredential, setNewCredential] = useState({
    name: "",
    type: "",
    value: "",
    environment: "",
    projectId: "",
    expiresAt: "",
    rotationEnabled: false,
    rotationDays: "",
    dailyQuotaTokens: "",
    monthlyBudget: "",
  });
  const [showCredentialForm, setShowCredentialForm] = useState(false);
  const [editingCredential, setEditingCredential] = useState<ProviderCredential | null>(null);
  const [selectedModel, setSelectedModel] = useState<AssociatedModel | null>(null);
  const [selectedProject, setSelectedProject] = useState<AssociatedProject | null>(null);

  useEffect(() => {
    if (!isNew) {
      loadProvider();
    }
    loadProjects();
  }, [id]);

  const loadProjects = async () => {
    try {
      // TODO: Implementar llamada API
      const mockProjects: Project[] = [
        { id: 1, name: "Proyecto Demo" },
        { id: 2, name: "Proyecto RAG" },
        { id: 3, name: "Proyecto Compliance" },
      ];
      setProjects(mockProjects);
    } catch (error) {
      console.error("Error loading projects:", error);
    }
  };

  const loadProvider = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamada API
      const mockProvider: Provider = {
        id: parseInt(id),
        name: "openai",
        displayName: "OpenAI",
        description: "OpenAI API provider",
        providerType: "EXTERNAL",
        status: "ACTIVE",
        authType: "API_KEY",
        baseUrl: "https://api.openai.com",
        credentials: [
          {
            id: 1,
            name: "OpenAI Production Key",
            type: "API_KEY",
            environment: "PRODUCTION",
            status: "ACTIVE",
            projectId: 1,
            projectName: "Proyecto Demo",
            expiresAt: "2025-12-31",
            lastUsedAt: "2024-01-15T10:30:00",
            usageCount: 1250,
            rotationEnabled: true,
            rotationDays: 90,
            dailyQuotaTokens: 1000000,
            monthlyBudget: 5000,
          },
          {
            id: 2,
            name: "OpenAI Dev Key",
            type: "API_KEY",
            environment: "DEVELOPMENT",
            status: "ACTIVE",
            usageCount: 45,
          },
        ],
        associatedModels: [
          { id: 1, name: "GPT-4", version: "1.0.0", status: "ACTIVE", lastUsedAt: "2024-01-15T10:30:00" },
          { id: 2, name: "GPT-3.5", version: "2.0.0", status: "ACTIVE", lastUsedAt: "2024-01-14T15:20:00" },
        ],
        associatedProjects: [
          { id: 1, name: "Proyecto Demo", credentialCount: 2, lastUsedAt: "2024-01-15T10:30:00" },
          { id: 2, name: "Proyecto RAG", credentialCount: 1, lastUsedAt: "2024-01-14T15:20:00" },
        ],
        costs: {
          totalMonthly: 4500,
          totalDaily: 150,
          byCredential: [
            { credentialId: 1, credentialName: "OpenAI Production Key", monthly: 4000, daily: 133 },
            { credentialId: 2, credentialName: "OpenAI Dev Key", monthly: 500, daily: 17 },
          ],
          trends: [
            { date: "2024-01-01", cost: 4200 },
            { date: "2024-01-08", cost: 4400 },
            { date: "2024-01-15", cost: 4500 },
          ],
        },
        kpis: {
          totalRequests: 125000,
          successRate: 99.5,
          averageLatency: 450,
          uptime: 99.9,
          activeCredentials: 2,
          expiredCredentials: 0,
        },
      };
      setProvider(mockProvider);
    } catch (error) {
      console.error("Error loading provider:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      // TODO: Implementar llamada API
      if (isNew) {
        // Crear nuevo proveedor
        window.location.href = "/models/providers";
      } else {
        // Actualizar proveedor existente
        setEditing(false);
      }
    } catch (error) {
      console.error("Error saving provider:", error);
    } finally {
      setSaving(false);
    }
  };

  const handleCreateCredential = async () => {
    if (!newCredential.name || !newCredential.type || (!newCredential.value && !editingCredential) || !newCredential.environment) {
      alert(t("governance.models.providers.modal.credential.create.validationError", "Por favor, complete todos los campos obligatorios"));
      return;
    }

    try {
      // TODO: Implementar llamada API
      if (editingCredential) {
        // Actualizar credencial existente
        const updatedCred: ProviderCredential = {
          ...editingCredential,
          name: newCredential.name,
          type: newCredential.type,
          environment: newCredential.environment,
          projectId: newCredential.projectId ? parseInt(newCredential.projectId) : undefined,
          projectName: newCredential.projectId ? projects.find(p => p.id === parseInt(newCredential.projectId))?.name : undefined,
          expiresAt: newCredential.expiresAt || undefined,
          rotationEnabled: newCredential.rotationEnabled,
          rotationDays: newCredential.rotationDays ? parseInt(newCredential.rotationDays) : undefined,
          dailyQuotaTokens: newCredential.dailyQuotaTokens ? parseInt(newCredential.dailyQuotaTokens) : undefined,
          monthlyBudget: newCredential.monthlyBudget ? parseFloat(newCredential.monthlyBudget) : undefined,
        };

        setProvider({
          ...provider,
          credentials: provider.credentials?.map(c => c.id === editingCredential.id ? updatedCred : c) || [],
        });
        setEditingCredential(null);
      } else {
        // Crear nueva credencial
        const newCred: ProviderCredential = {
          id: (provider.credentials?.length || 0) + 1,
          name: newCredential.name,
          type: newCredential.type,
          environment: newCredential.environment,
          status: "ACTIVE",
          projectId: newCredential.projectId ? parseInt(newCredential.projectId) : undefined,
          projectName: newCredential.projectId ? projects.find(p => p.id === parseInt(newCredential.projectId))?.name : undefined,
          expiresAt: newCredential.expiresAt || undefined,
          usageCount: 0,
          rotationEnabled: newCredential.rotationEnabled,
          rotationDays: newCredential.rotationDays ? parseInt(newCredential.rotationDays) : undefined,
          dailyQuotaTokens: newCredential.dailyQuotaTokens ? parseInt(newCredential.dailyQuotaTokens) : undefined,
          monthlyBudget: newCredential.monthlyBudget ? parseFloat(newCredential.monthlyBudget) : undefined,
        };

        setProvider({
          ...provider,
          credentials: [...(provider.credentials || []), newCred],
        });
      }

      setNewCredential({
        name: "",
        type: "",
        value: "",
        environment: "",
        projectId: "",
        expiresAt: "",
        rotationEnabled: false,
        rotationDays: "",
        dailyQuotaTokens: "",
        monthlyBudget: "",
      });
      setShowCredentialForm(false);
    } catch (error) {
      console.error("Error saving credential:", error);
    }
  };

  const getProviderTypeBadge = (type: string) => {
    if (type === "EXTERNAL") {
      return <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50">EXTERNAL</Badge>;
    } else if (type === "INTERNAL") {
      return <Badge className="bg-purple-500/20 text-purple-400 border-purple-500/50">INTERNAL</Badge>;
    }
    return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">{type}</Badge>;
  };

  const getStatusBadge = (status: string) => {
    if (status === "ACTIVE") {
      return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">ACTIVE</Badge>;
    } else if (status === "INACTIVE") {
      return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">INACTIVE</Badge>;
    } else if (status === "PENDING") {
      return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">PENDING</Badge>;
    }
    return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">{status}</Badge>;
  };

  if (loading) {
    return (
      <div className="space-y-4 p-6">
        <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
      </div>
    );
  }

  return (
    <div className="space-y-4 p-6">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Server className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {isNew
              ? t("governance.models.providers.detail.createTitle", "Nuevo Proveedor")
              : provider.displayName || provider.name}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9 mb-2">
          {isNew
            ? t("governance.models.providers.detail.createSubtitle", "Crear un nuevo proveedor de modelos")
            : t("governance.models.providers.detail.subtitle", "Detalles y gestión del proveedor")}
        </p>
      </div>

      {/* Action Buttons */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => { window.location.href = "/models/providers"; }}
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex items-center gap-2">
          {!isNew && !editing && (
            <Button
              variant="outline"
              onClick={() => setEditing(true)}
            >
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Editar")}
            </Button>
          )}
          {(editing || isNew) && (
            <>
              <Button
                variant="outline"
                onClick={() => {
                  if (isNew) {
                    window.location.href = "/models/providers";
                  } else {
                    setEditing(false);
                    loadProvider();
                  }
                }}
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={handleSave}
                disabled={saving}
                className="bg-primary hover:bg-primary/90"
              >
                <Save className="w-4 h-4 mr-2" />
                {saving ? t("common.saving", "Guardando...") : t("common.save", "Guardar")}
              </Button>
            </>
          )}
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="general" className="space-y-6">
        <TabsList className="w-full">
          <TabsTrigger value="general" className="flex-1">
            {t("governance.models.providers.detail.tabs.general", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="credentials" className="flex-1">
            {t("governance.models.providers.detail.tabs.credentials", "Credenciales")}
          </TabsTrigger>
          <TabsTrigger value="models" className="flex-1">
            {t("governance.models.providers.detail.tabs.models", "Modelos Asociados")}
          </TabsTrigger>
          <TabsTrigger value="projects" className="flex-1">
            {t("governance.models.providers.detail.tabs.projects", "Proyectos Asociados")}
          </TabsTrigger>
          <TabsTrigger value="costs" className="flex-1">
            {t("governance.models.providers.detail.tabs.costs", "Costes y KPIs")}
          </TabsTrigger>
        </TabsList>

        {/* Tab: Información General */}
        <TabsContent value="general" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("governance.models.providers.detail.general.title", "Información del Proveedor")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.name", "Nombre")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={provider.name}
                      onChange={(e) => setProvider({ ...provider, name: e.target.value })}
                      placeholder={t("governance.models.providers.modal.create.namePlaceholder", "Ingrese el nombre del proveedor")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{provider.name}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.displayName", "Nombre de Visualización")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={provider.displayName}
                      onChange={(e) => setProvider({ ...provider, displayName: e.target.value })}
                      placeholder={t("governance.models.providers.modal.create.displayNamePlaceholder", "Ingrese el nombre de visualización")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{provider.displayName}</p>
                  )}
                </div>
                <div className="space-y-2 md:col-span-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.description", "Descripción")}
                  </label>
                  {editing || isNew ? (
                    <Textarea
                      value={provider.description}
                      onChange={(e) => setProvider({ ...provider, description: e.target.value })}
                      placeholder={t("governance.models.providers.modal.create.descriptionPlaceholder", "Ingrese una descripción del proveedor")}
                      rows={3}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{provider.description || "-"}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.providerType", "Tipo de Proveedor")} *
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={provider.providerType}
                      onValueChange={(value) => setProvider({ ...provider, providerType: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.providers.modal.create.providerTypePlaceholder", "Seleccione el tipo de proveedor")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="EXTERNAL">EXTERNAL</SelectItem>
                        <SelectItem value="INTERNAL">INTERNAL</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <div>{getProviderTypeBadge(provider.providerType)}</div>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.status", "Estado")} *
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={provider.status}
                      onValueChange={(value) => setProvider({ ...provider, status: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.providers.modal.create.statusPlaceholder", "Seleccione el estado")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                        <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                        <SelectItem value="PENDING">PENDING</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <div>{getStatusBadge(provider.status)}</div>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.authType", "Tipo de Autenticación")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={provider.authType}
                      onValueChange={(value) => setProvider({ ...provider, authType: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("governance.models.providers.modal.create.authTypePlaceholder", "Seleccione el tipo de autenticación")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="API_KEY">API_KEY</SelectItem>
                        <SelectItem value="OAUTH">OAUTH</SelectItem>
                        <SelectItem value="BEARER_TOKEN">BEARER_TOKEN</SelectItem>
                        <SelectItem value="BASIC_AUTH">BASIC_AUTH</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <p className="text-sm text-foreground">{provider.authType || "-"}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("governance.models.providers.modal.create.baseUrl", "URL Base")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={provider.baseUrl}
                      onChange={(e) => setProvider({ ...provider, baseUrl: e.target.value })}
                      placeholder={t("governance.models.providers.modal.create.baseUrlPlaceholder", "Ingrese la URL base (ej: https://api.example.com)")}
                    />
                  ) : (
                    <p className="text-sm text-foreground font-mono">{provider.baseUrl}</p>
                  )}
                </div>
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Credenciales */}
        <TabsContent value="credentials" className="space-y-6">
          <Card className="border-2">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Key className="w-5 h-5" />
                {t("governance.models.providers.credentials.title", "Credenciales")}
              </CardTitle>
              <Button
                onClick={() => {
                  setEditingCredential(null);
                  setNewCredential({
                    name: "",
                    type: "",
                    value: "",
                    environment: "",
                    projectId: "",
                    expiresAt: "",
                    rotationEnabled: false,
                    rotationDays: "",
                    dailyQuotaTokens: "",
                    monthlyBudget: "",
                  });
                  setShowCredentialForm(!showCredentialForm);
                }}
                className="bg-primary hover:bg-primary/90"
                size="sm"
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("governance.models.providers.credentials.addButton", "Agregar Credencial")}
              </Button>
            </CardHeader>
            <CardBody>
              {showCredentialForm && (
                <Card className="border-2 mb-6">
                  <CardHeader>
                    <CardTitle className="text-lg">
                      {editingCredential
                        ? t("governance.models.providers.modal.credential.edit.title", "Editar Credencial")
                        : t("governance.models.providers.modal.credential.create.title", "Agregar Credencial")}
                    </CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.name", "Nombre de la Credencial")} *
                        </label>
                        <Input
                          placeholder={t("governance.models.providers.modal.credential.create.namePlaceholder", "Ingrese un nombre para la credencial")}
                          value={newCredential.name}
                          onChange={(e) => setNewCredential({ ...newCredential, name: e.target.value })}
                        />
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.type", "Tipo de Credencial")} *
                        </label>
                        <Select value={newCredential.type} onValueChange={(value) => setNewCredential({ ...newCredential, type: value })}>
                          <SelectTrigger>
                            <SelectValue placeholder={t("governance.models.providers.modal.credential.create.typePlaceholder", "Seleccione el tipo")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="API_KEY">API_KEY</SelectItem>
                            <SelectItem value="ACCESS_TOKEN">ACCESS_TOKEN</SelectItem>
                            <SelectItem value="SECRET_KEY">SECRET_KEY</SelectItem>
                            <SelectItem value="BEARER_TOKEN">BEARER_TOKEN</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.value", "Valor (API Key, Token, etc.)")} *
                        </label>
                        <Input
                          type="password"
                          placeholder={t("governance.models.providers.modal.credential.create.valuePlaceholder", "Ingrese el valor de la credencial")}
                          value={newCredential.value}
                          onChange={(e) => setNewCredential({ ...newCredential, value: e.target.value })}
                        />
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.environment", "Ambiente")} *
                        </label>
                        <Select value={newCredential.environment} onValueChange={(value) => setNewCredential({ ...newCredential, environment: value })}>
                          <SelectTrigger>
                            <SelectValue placeholder={t("governance.models.providers.modal.credential.create.environmentPlaceholder", "Seleccione el ambiente")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="PRODUCTION">PRODUCTION</SelectItem>
                            <SelectItem value="DEVELOPMENT">DEVELOPMENT</SelectItem>
                            <SelectItem value="STAGING">STAGING</SelectItem>
                            <SelectItem value="TESTING">TESTING</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.project", "Proyecto (Opcional)")}
                        </label>
                        <Select value={newCredential.projectId} onValueChange={(value) => setNewCredential({ ...newCredential, projectId: value })}>
                          <SelectTrigger>
                            <SelectValue placeholder={t("governance.models.providers.modal.credential.create.projectPlaceholder", "Seleccione un proyecto o deje vacío para global")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="">{t("governance.models.providers.modal.credential.create.projectGlobal", "Global (Sin proyecto)")}</SelectItem>
                            {projects.map((project) => (
                              <SelectItem key={project.id} value={project.id.toString()}>
                                {project.name}
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("governance.models.providers.modal.credential.create.expiresAt", "Fecha de Expiración (Opcional)")}
                        </label>
                        <Input
                          type="date"
                          placeholder={t("governance.models.providers.modal.credential.create.expiresAtPlaceholder", "Seleccione fecha de expiración")}
                          value={newCredential.expiresAt}
                          onChange={(e) => setNewCredential({ ...newCredential, expiresAt: e.target.value })}
                        />
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4">
                      <Button
                        variant="outline"
                        onClick={() => {
                          setShowCredentialForm(false);
                          setEditingCredential(null);
                          setNewCredential({
                            name: "",
                            type: "",
                            value: "",
                            environment: "",
                            projectId: "",
                            expiresAt: "",
                            rotationEnabled: false,
                            rotationDays: "",
                            dailyQuotaTokens: "",
                            monthlyBudget: "",
                          });
                        }}
                      >
                        {t("common.cancel", "Cancelar")}
                      </Button>
                      <Button onClick={handleCreateCredential} className="bg-primary hover:bg-primary/90">
                        {editingCredential
                          ? t("common.save", "Guardar")
                          : t("governance.models.providers.modal.credential.create.createButton", "Agregar Credencial")}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              )}

              {provider.credentials && provider.credentials.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.type", "Tipo")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.environment", "Ambiente")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.status", "Estado")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.project", "Proyecto")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.expiresAt", "Expira")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.lastUsed", "Último Uso")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.credentials.table.usageCount", "Usos")}</th>
                        <th className="text-center p-2">{t("governance.models.providers.credentials.table.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {provider.credentials.map((cred) => (
                        <tr key={cred.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{cred.name}</td>
                          <td className="p-2">{cred.type}</td>
                          <td className="p-2">{cred.environment}</td>
                          <td className="p-2">{getStatusBadge(cred.status)}</td>
                          <td className="p-2">{cred.projectName || <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.expiresAt ? new Date(cred.expiresAt).toLocaleDateString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.lastUsedAt ? new Date(cred.lastUsedAt).toLocaleString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{cred.usageCount}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.edit", "Editar")}
                                onClick={() => {
                                  setEditingCredential(cred);
                                  setNewCredential({
                                    name: cred.name,
                                    type: cred.type,
                                    value: "", // No mostrar el valor por seguridad
                                    environment: cred.environment,
                                    projectId: cred.projectId?.toString() || "",
                                    expiresAt: cred.expiresAt || "",
                                    rotationEnabled: cred.rotationEnabled || false,
                                    rotationDays: cred.rotationDays?.toString() || "",
                                    dailyQuotaTokens: cred.dailyQuotaTokens?.toString() || "",
                                    monthlyBudget: cred.monthlyBudget?.toString() || "",
                                  });
                                  setShowCredentialForm(true);
                                }}
                              >
                                <Edit className="w-4 h-4" />
                              </Button>
                              <Button
                                variant="ghost"
                                size="sm"
                                className="text-red-400 hover:text-red-300"
                                title={t("common.delete", "Eliminar")}
                                onClick={() => {
                                  if (confirm(t("common.confirmDelete", "¿Está seguro de eliminar esta credencial?"))) {
                                    setProvider({
                                      ...provider,
                                      credentials: provider.credentials?.filter(c => c.id !== cred.id) || [],
                                    });
                                  }
                                }}
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
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.models.providers.credentials.noCredentials", "No hay credenciales registradas")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Modelos Asociados */}
        <TabsContent value="models" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="w-5 h-5" />
                {t("governance.models.providers.detail.tabs.models", "Modelos Asociados")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {provider.associatedModels && provider.associatedModels.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.providers.detail.models.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.detail.models.version", "Versión")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.detail.models.status", "Estado")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.detail.models.lastUsed", "Último Uso")}</th>
                        <th className="text-center p-2">{t("governance.models.providers.detail.models.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {provider.associatedModels.map((model) => (
                        <tr key={model.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{model.name}</td>
                          <td className="p-2">{model.version}</td>
                          <td className="p-2">{getStatusBadge(model.status)}</td>
                          <td className="p-2">{model.lastUsedAt ? new Date(model.lastUsedAt).toLocaleString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.view", "Ver Modelo")}
                                onClick={() => setSelectedModel(model)}
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.models.providers.detail.models.noModels", "No hay modelos asociados")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Proyectos Asociados */}
        <TabsContent value="projects" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="w-5 h-5" />
                {t("governance.models.providers.detail.tabs.projects", "Proyectos Asociados")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {provider.associatedProjects && provider.associatedProjects.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("governance.models.providers.detail.projects.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.detail.projects.credentialCount", "Credenciales")}</th>
                        <th className="text-left p-2">{t("governance.models.providers.detail.projects.lastUsed", "Último Uso")}</th>
                        <th className="text-center p-2">{t("governance.models.providers.detail.projects.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {provider.associatedProjects.map((project) => (
                        <tr key={project.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{project.name}</td>
                          <td className="p-2">{project.credentialCount}</td>
                          <td className="p-2">{project.lastUsedAt ? new Date(project.lastUsedAt).toLocaleString() : <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.view", "Ver Proyecto")}
                                onClick={() => setSelectedProject(project)}
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("governance.models.providers.detail.projects.noProjects", "No hay proyectos asociados")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Costes y KPIs */}
        <TabsContent value="costs" className="space-y-6">
          {/* KPIs */}
          <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-4">
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.totalRequests", "Total Requests")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.totalRequests.toLocaleString() || 0}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.successRate", "Tasa de Éxito")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.successRate.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.averageLatency", "Latencia Promedio")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.averageLatency || 0}ms</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.uptime", "Uptime")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.uptime.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.activeCredentials", "Credenciales Activas")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.activeCredentials || 0}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("governance.models.providers.detail.kpis.expiredCredentials", "Credenciales Expiradas")}
                </p>
                <h2 className="text-2xl font-bold">{provider.kpis?.expiredCredentials || 0}</h2>
              </CardBody>
            </Card>
          </div>

          {/* Costes */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card className="border-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="w-5 h-5" />
                  {t("governance.models.providers.detail.costs.title", "Costes")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-4">
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("governance.models.providers.detail.costs.totalMonthly", "Total Mensual")}
                    </p>
                    <h3 className="text-3xl font-bold">${provider.costs?.totalMonthly.toLocaleString() || 0}</h3>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      {t("governance.models.providers.detail.costs.totalDaily", "Total Diario")}
                    </p>
                    <h3 className="text-2xl font-bold">${provider.costs?.totalDaily.toLocaleString() || 0}</h3>
                  </div>
                  {provider.costs?.byCredential && provider.costs.byCredential.length > 0 && (
                    <div>
                      <p className="text-sm font-medium mb-2">
                        {t("governance.models.providers.detail.costs.byCredential", "Por Credencial")}
                      </p>
                      <div className="space-y-2">
                        {provider.costs.byCredential.map((cost) => (
                          <div key={cost.credentialId} className="flex justify-between items-center p-2 border rounded">
                            <span className="text-sm">{cost.credentialName}</span>
                            <span className="text-sm font-semibold">${cost.monthly.toLocaleString()}/mes</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </CardBody>
            </Card>

            <Card className="border-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="w-5 h-5" />
                  {t("governance.models.providers.detail.costs.trends", "Tendencias")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="h-64 border rounded-lg flex items-center justify-center">
                  <p className="text-muted-foreground">
                    {t("governance.models.providers.detail.costs.trendsChart", "Gráfico de tendencias de costes")}
                  </p>
                </div>
              </CardBody>
            </Card>
          </div>
        </TabsContent>
      </Tabs>

      {/* Modal de Información del Modelo */}
      <SimpleModal
        isOpen={selectedModel !== null}
        onClose={() => setSelectedModel(null)}
        title={selectedModel ? `${t("governance.models.providers.detail.models.title", "Información del Modelo")} - ${selectedModel.name}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedModel && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.models.name", "Nombre")}
                    </label>
                    <p className="text-base font-semibold">{selectedModel.name}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.models.version", "Versión")}
                    </label>
                    <p className="text-base font-semibold">{selectedModel.version}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.models.status", "Estado")}
                    </label>
                    <div className="mt-1">{getStatusBadge(selectedModel.status)}</div>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.models.lastUsed", "Último Uso")}
                    </label>
                    <p className="text-base">
                      {selectedModel.lastUsedAt
                        ? new Date(selectedModel.lastUsedAt).toLocaleString()
                        : <span className="text-muted-foreground">-</span>}
                    </p>
                  </div>
                </div>
                <div className="pt-4 border-t">
                  <p className="text-sm text-muted-foreground">
                    {t("governance.models.providers.detail.models.description", "Este modelo está asociado al proveedor y puede ser utilizado a través de las credenciales configuradas.")}
                  </p>
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>

      {/* Modal de Información del Proyecto */}
      <SimpleModal
        isOpen={selectedProject !== null}
        onClose={() => setSelectedProject(null)}
        title={selectedProject ? `${t("governance.models.providers.detail.projects.title", "Información del Proyecto")} - ${selectedProject.name}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedProject && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.projects.name", "Nombre")}
                    </label>
                    <p className="text-base font-semibold">{selectedProject.name}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.projects.credentialCount", "Número de Credenciales")}
                    </label>
                    <p className="text-base font-semibold">{selectedProject.credentialCount}</p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.providers.detail.projects.lastUsed", "Último Uso")}
                    </label>
                    <p className="text-base">
                      {selectedProject.lastUsedAt
                        ? new Date(selectedProject.lastUsedAt).toLocaleString()
                        : <span className="text-muted-foreground">-</span>}
                    </p>
                  </div>
                </div>
                <div className="pt-4 border-t">
                  <p className="text-sm text-muted-foreground">
                    {t("governance.models.providers.detail.projects.description", "Este proyecto utiliza credenciales de este proveedor para acceder a los servicios.")}
                  </p>
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
