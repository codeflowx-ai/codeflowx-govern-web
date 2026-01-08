"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  ArrowLeft,
  Package,
  Edit,
  Save,
  Tag,
  Activity,
  BarChart3,
  Shield,
  Server,
  Eye,
  Plus,
  Trash2,
  TrendingUp,
  FolderOpen,
} from "lucide-react";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { useState, useEffect } from "react";
import { useParams, useSearchParams, useRouter } from "next/navigation";
import ReviewRulesTab from "./ReviewRulesTab";
import ComplianceTab from "./ComplianceTab";
import CertificationTab from "./CertificationTab";
import RetirementTab from "./RetirementTab";
import ConfigurationTab from "./ConfigurationTab";

interface Agent {
  id: number;
  uuid?: string;
  name: string;
  description: string;
  status: string;
  version: string;
  domain: string;
  createdAt: string;
  lastModified: string;
  type?: string;
  capabilities?: string;
  configuration?: string;
  metadata?: string;
}

interface AgentVersion {
  id: number;
  version: string;
  description?: string;
  status: string;
  createdAt: string;
  createdBy: string;
}

interface AgentDeployment {
  id: number;
  name: string;
  status: string;
  environment: string;
  deployedAt: string;
  endpoint?: string;
}

interface AgentMetrics {
  totalInteractions: number;
  successRate: number;
  averageResponseTime: number;
  uptime: number;
  trends?: Array<{
    date: string;
    interactions: number;
    successRate: number;
    responseTime: number;
  }>;
}

interface AgentProject {
  id: number;
  projectId: number;
  projectName: string;
  usageType: string; // PRIMARY, SECONDARY, REFERENCE, TESTING
  role?: string;
  lastUsedAt?: string;
  createdAt: string;
}

export default function AgentDetailPage() {
  const { t, mounted } = useTranslation();
  const params = useParams();
  const searchParams = useSearchParams();
  const router = useRouter();
  const agentId = params.id as string;
  const isNew = agentId === "new";
  const isEditMode = searchParams.get('edit') === 'true';

  const [agent, setAgent] = useState<Agent | null>(null);
  const [loading, setLoading] = useState(!isNew);
  const [editing, setEditing] = useState(isNew || isEditMode);
  const [saving, setSaving] = useState(false);
  const [versions, setVersions] = useState<AgentVersion[]>([]);
  const [deployments, setDeployments] = useState<AgentDeployment[]>([]);
  const [metrics, setMetrics] = useState<AgentMetrics | null>(null);
  const [projects, setProjects] = useState<AgentProject[]>([]);
  const [selectedProject, setSelectedProject] = useState<AgentProject | null>(null);
  const [newVersion, setNewVersion] = useState({
    version: "",
    description: "",
    status: "",
  });
  const [showVersionForm, setShowVersionForm] = useState(false);
  const [editingVersion, setEditingVersion] = useState<AgentVersion | null>(null);

  // Función helper para generar UUID v4
  const generateUUID = (): string => {
    // Intentar usar crypto.randomUUID si está disponible
    if (typeof crypto !== 'undefined' && crypto.randomUUID) {
      return crypto.randomUUID();
    }
    // Fallback: generar UUID v4 manualmente
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
      const r = (Math.random() * 16) | 0;
      const v = c === 'x' ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    });
  };

  // Actualizar estado de edición cuando cambien los query params
  useEffect(() => {
    setEditing(isNew || isEditMode);
  }, [isNew, isEditMode]);

  useEffect(() => {
    if (!isNew) {
      loadAgent();
      loadVersions();
      loadDeployments();
      loadMetrics();
      loadProjects();
    } else {
      // Generar UUID automáticamente para nuevo agente
      const uuid = generateUUID();
      setAgent({
        id: 0,
        uuid: uuid,
        name: "",
        description: "",
        status: "DRAFT",
        version: "1.0.0",
        domain: "",
        createdAt: new Date().toISOString().split("T")[0],
        lastModified: new Date().toISOString().split("T")[0],
      });
    }
  }, [agentId]);

  const loadAgent = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/registry/${agentId}`);
      if (response.ok) {
        const data = await response.json();
        setAgent(data);
      } else {
        // Mock data para desarrollo
        setAgent({
          id: parseInt(agentId),
          uuid: `agent-${agentId}-${Date.now()}`,
          name: "Credit Scoring Agent",
          description: "Agent for credit risk assessment",
          status: "ACTIVE",
          version: "2.1.0",
          domain: "Finance",
          createdAt: "2024-01-10",
          lastModified: "2024-01-15",
        });
      }
    } catch (error) {
      console.error("Error loading agent:", error);
      // Mock data para desarrollo
      setAgent({
        id: parseInt(agentId),
        uuid: `agent-${agentId}-${Date.now()}`,
        name: "Credit Scoring Agent",
        description: "Agent for credit risk assessment",
        status: "ACTIVE",
        version: "2.1.0",
        domain: "Finance",
        createdAt: "2024-01-10",
        lastModified: "2024-01-15",
      });
    } finally {
      setLoading(false);
    }
  };

  const loadVersions = async () => {
    try {
      // TODO: Implementar llamada API
      const mockVersions: AgentVersion[] = [
        {
          id: 1,
          version: "1.0.0",
          description: "Versión inicial",
          status: "ACTIVE",
          createdAt: "2024-01-10T00:00:00",
          createdBy: "admin",
        },
        {
          id: 2,
          version: "2.0.0",
          description: "Mejoras de rendimiento",
          status: "ACTIVE",
          createdAt: "2024-01-12T00:00:00",
          createdBy: "admin",
        },
        {
          id: 3,
          version: "2.1.0",
          description: "Nuevas funcionalidades",
          status: "ACTIVE",
          createdAt: "2024-01-15T00:00:00",
          createdBy: "admin",
        },
      ];
      setVersions(mockVersions);
    } catch (error) {
      console.error("Error loading versions:", error);
    }
  };

  const loadDeployments = async () => {
    try {
      // TODO: Implementar llamada API
      const mockDeployments: AgentDeployment[] = [
        {
          id: 1,
          name: "production-deployment",
          status: "ACTIVE",
          environment: "production",
          deployedAt: "2024-01-15T10:00:00",
          endpoint: "https://agents.example.com/credit-scoring/v2.1.0",
        },
      ];
      setDeployments(mockDeployments);
    } catch (error) {
      console.error("Error loading deployments:", error);
    }
  };

  const loadMetrics = async () => {
    try {
      // TODO: Implementar llamada API
      const mockMetrics: AgentMetrics = {
        totalInteractions: 125000,
        successRate: 98.5,
        averageResponseTime: 320,
        uptime: 99.9,
        trends: [
          { date: "2024-01-01", interactions: 8500, successRate: 97.2, responseTime: 380 },
          { date: "2024-01-05", interactions: 10200, successRate: 98.0, responseTime: 350 },
          { date: "2024-01-10", interactions: 11500, successRate: 98.3, responseTime: 330 },
          { date: "2024-01-15", interactions: 12500, successRate: 98.5, responseTime: 320 },
          { date: "2024-01-20", interactions: 13200, successRate: 98.6, responseTime: 315 },
        ],
      };
      setMetrics(mockMetrics);
    } catch (error) {
      console.error("Error loading metrics:", error);
    }
  };

  const loadProjects = async () => {
    try {
      const response = await fetch(`/api/v1/governance/agents/registry/${agentId}/projects`);
      if (response.ok) {
        const data: AgentProject[] = await response.json();
        // Mapear datos del backend al formato del frontend
        const mappedProjects: AgentProject[] = data.map((project) => ({
          id: project.id,
          projectId: project.projectId,
          projectName: project.projectName,
          usageType: project.usageType,
          role: project.role,
          lastUsedAt: project.lastUsedAt ? new Date(project.lastUsedAt).toISOString() : undefined,
          createdAt: project.createdAt ? new Date(project.createdAt).toISOString() : new Date().toISOString(),
        }));
        setProjects(mappedProjects);
      } else if (response.status === 404) {
        // Agente no encontrado o sin proyectos
        setProjects([]);
      } else {
        console.error("Error loading projects:", response.statusText);
        setProjects([]);
      }
    } catch (error) {
      console.error("Error loading projects:", error);
      setProjects([]);
    }
  };

  const calculateNextVersion = (currentVersion: string, versionType: 'major' | 'minor' | 'patch' = 'patch'): string => {
    // Remover 'v' si existe
    const cleanVersion = currentVersion.replace(/^v/, '');
    const parts = cleanVersion.split('.').map(Number);
    if (parts.length !== 3) return "1.0.0";

    if (versionType === 'major') {
      return `${parts[0] + 1}.0.0`;
    } else if (versionType === 'minor') {
      return `${parts[0]}.${parts[1] + 1}.0`;
    } else {
      return `${parts[0]}.${parts[1]}.${parts[2] + 1}`;
    }
  };

  const handleCreateVersion = async () => {
    if (!newVersion.status) {
      alert(t("agents.registry.detail.versions.validationError", "Por favor, complete todos los campos obligatorios"));
      return;
    }

    try {
      // TODO: Implementar llamada API
      const latestVersion = versions && versions.length > 0
        ? versions[versions.length - 1].version
        : agent?.version || "1.0.0";
      const nextVersion = calculateNextVersion(latestVersion, 'patch');

      const newVer: AgentVersion = {
        id: (versions?.length || 0) + 1,
        version: nextVersion,
        description: newVersion.description,
        status: newVersion.status,
        createdAt: new Date().toISOString(),
        createdBy: "current_user", // TODO: Obtener del contexto
      };

      setVersions([...(versions || []), newVer]);
      if (agent) {
        setAgent({ ...agent, version: nextVersion });
      }

      setNewVersion({
        version: "",
        description: "",
        status: "",
      });
      setShowVersionForm(false);
    } catch (error) {
      console.error("Error saving version:", error);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);

      if (isNew) {
        // Crear nuevo agente
        const response = await fetch("/api/governance/agents/registry", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: agent?.name,
            description: agent?.description,
            domain: agent?.domain,
            version: agent?.version || "1.0.0",
            status: agent?.status || "DRAFT",
          }),
        });

        if (response.ok) {
          const data = await response.json();
          // Redirigir al detalle del agente creado (sin modo edición)
          window.location.href = `/governance/agents/registry/${data.id}`;
        } else {
          const error = await response.json();
          alert(t("agents.registry.create.error", "Error al crear el agente: ") + (error.message || "Error desconocido"));
        }
      } else {
        // Actualizar agente existente
        const response = await fetch(`/api/governance/agents/registry/${agentId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: agent?.name,
            description: agent?.description,
            domain: agent?.domain,
            version: agent?.version,
            status: agent?.status,
          }),
        });

        if (response.ok) {
          // Redirigir al detalle sin modo edición
          window.location.href = `/governance/agents/registry/${agentId}`;
        } else {
          const error = await response.json();
          alert(t("agents.registry.edit.error", "Error al actualizar el agente: ") + (error.message || "Error desconocido"));
        }
      }
    } catch (error) {
      console.error("Error saving agent:", error);
      alert(t("agents.registry.create.error", "Error al guardar el agente"));
    } finally {
      setSaving(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusColors: Record<string, string> = {
      ACTIVE: "bg-green-500/20 text-green-500",
      PENDING: "bg-yellow-500/20 text-yellow-400",
      DRAFT: "bg-gray-500/20 text-gray-400",
      INACTIVE: "bg-red-500/20 text-red-400",
    };
    return (
      <Badge className={statusColors[status] || statusColors.DRAFT}>
        {status}
      </Badge>
    );
  };

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Package className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  if (!agent && !isNew) {
    return (
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Package className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("agents.registry.detail.title", "Detalle del Agente")}
              </h1>
            </div>
          </div>
          <Button
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
            onClick={() => {
              window.location.href = "/governance/agents/registry";
            }}
          >
            <ArrowLeft className="h-4 w-4" />
            {t("common.back", "Volver")}
          </Button>
        </div>
        <Card>
          <CardBody>
            <p className="text-center text-muted-foreground py-8">
              {t("agents.registry.detail.notFound", "Agente no encontrado")}
            </p>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-4 p-6">
      {/* Header - Título y Subtítulo */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Package className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {isNew
              ? t("agents.registry.detail.createTitle", "Nuevo Agente")
              : agent?.name || t("agents.registry.detail.title", "Detalle del Agente")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {isNew
            ? t("agents.registry.detail.createSubtitle", "Crear un nuevo agente")
            : t("agents.registry.detail.subtitle", "Información detallada del agente")}
        </p>
      </div>

      {/* Botones de Acción - Volver a la izquierda, Acciones a la derecha */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => { window.location.href = "/governance/agents/registry"; }}
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          {!isNew && !editing && (
            <Button
              variant="outline"
              onClick={() => {
                router.push(`/governance/agents/registry/${agentId}?edit=true`);
              }}
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
                    router.push("/governance/agents/registry");
                  } else {
                    // Cancelar edición: volver al detalle sin modo edición
                    router.push(`/governance/agents/registry/${agentId}`);
                  }
                }}
                disabled={saving}
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
        <TabsList className="flex space-x-1 p-1">
          <TabsTrigger value="general" className="flex-1">
            {t("agents.registry.detail.tabs.general", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="versions" className="flex-1">
            {t("agents.registry.detail.tabs.versions", "Versiones")}
          </TabsTrigger>
          <TabsTrigger value="monitoring" className="flex-1">
            {t("agents.registry.detail.tabs.monitoring", "Monitoreo")}
          </TabsTrigger>
          <TabsTrigger value="deployments" className="flex-1">
            {t("agents.registry.detail.tabs.deployments", "Despliegues")}
          </TabsTrigger>
          <TabsTrigger value="projects" className="flex-1">
            {t("agents.registry.detail.tabs.projects", "Proyectos")}
          </TabsTrigger>
          <TabsTrigger value="compliance" className="flex-1">
            {t("agents.registry.detail.tabs.compliance", "Cumplimiento")}
          </TabsTrigger>
          <TabsTrigger value="review-rules" className="flex-1">
            {t("agents.registry.detail.tabs.reviewRules", "Reglas de Revisión")}
          </TabsTrigger>
          <TabsTrigger value="certification" className="flex-1">
            {t("agents.registry.detail.tabs.certification", "Certificación")}
          </TabsTrigger>
          <TabsTrigger value="retirement" className="flex-1">
            {t("agents.registry.detail.tabs.retirement", "Retiro")}
          </TabsTrigger>
          <TabsTrigger value="configuration" className="flex-1">
            {t("agents.registry.detail.tabs.configuration", "Configuración")}
          </TabsTrigger>
        </TabsList>

        {/* Tab: Información General */}
        <TabsContent value="general" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("agents.registry.detail.general.title", "Información del Agente")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.name", "Nombre")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={agent?.name || ""}
                      onChange={(e) => setAgent(agent ? { ...agent, name: e.target.value } : null)}
                      placeholder={t("agents.registry.create.namePlaceholder", "Ingrese el nombre del agente")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{agent?.name}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.detail.uuid", "UUID")}
                  </label>
                  <p className="text-sm text-foreground font-mono text-muted-foreground">
                    {agent?.uuid || t("agents.registry.detail.uuidAutoGenerated", "Se generará automáticamente")}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {t("agents.registry.detail.general.uuidInfo", "UUID generado automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.version", "Versión")}
                  </label>
                  <p className="text-sm text-foreground font-mono">
                    {agent?.version || "1.0.0"}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {t("agents.registry.detail.general.versionAutoGenerated", "Versión semántica generada automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.domain", "Dominio")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={agent?.domain || ""}
                      onChange={(e) => setAgent(agent ? { ...agent, domain: e.target.value } : null)}
                      placeholder={t("agents.registry.create.domainPlaceholder", "Ingrese el dominio del agente")}
                    />
                  ) : (
                    <Badge variant="outline" className="w-fit">
                      {agent?.domain}
                    </Badge>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("common.status", "Estado")} *
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={agent?.status || ""}
                      onValueChange={(value) => setAgent(agent ? { ...agent, status: value } : null)}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder={t("agents.registry.create.statusPlaceholder", "Seleccione el estado")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="DRAFT">{t("agents.registry.create.statusDraft", "Borrador")}</SelectItem>
                        <SelectItem value="PENDING">{t("agents.registry.create.statusPending", "Pendiente")}</SelectItem>
                        <SelectItem value="ACTIVE">{t("agents.registry.create.statusActive", "Activo")}</SelectItem>
                        <SelectItem value="INACTIVE">{t("agents.registry.create.statusInactive", "Inactivo")}</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <div>{getStatusBadge(agent?.status || "DRAFT")}</div>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.createdAt", "Creado")}
                  </label>
                  <p className="text-sm text-foreground">{agent?.createdAt || "-"}</p>
                </div>
                <div className="space-y-2 md:col-span-2">
                  <label className="text-sm font-medium">
                    {t("agents.registry.description", "Descripción")}
                  </label>
                  {editing || isNew ? (
                    <Textarea
                      value={agent?.description || ""}
                      onChange={(e) => setAgent(agent ? { ...agent, description: e.target.value } : null)}
                      placeholder={t("agents.registry.create.descriptionPlaceholder", "Ingrese una descripción del agente")}
                      rows={3}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{agent?.description || "-"}</p>
                  )}
                </div>
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Versiones */}
        <TabsContent value="versions" className="space-y-6">
          <Card className="border-2">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Tag className="w-5 h-5" />
                {t("agents.registry.detail.tabs.versions", "Versiones")}
              </CardTitle>
              <Button
                onClick={() => {
                  setEditingVersion(null);
                  setNewVersion({
                    version: "",
                    description: "",
                    status: "",
                  });
                  setShowVersionForm(!showVersionForm);
                }}
                className="bg-primary hover:bg-primary/90"
                size="sm"
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("agents.registry.detail.versions.addButton", "Agregar Versión")}
              </Button>
            </CardHeader>
            <CardBody>
              {showVersionForm && (
                <Card className="border-2 mb-6">
                  <CardHeader>
                    <CardTitle className="text-lg">
                      {editingVersion
                        ? t("agents.registry.detail.versions.editTitle", "Editar Versión")
                        : t("agents.registry.detail.versions.createTitle", "Agregar Versión")}
                    </CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("agents.registry.version", "Versión")}
                        </label>
                        <p className="text-sm text-foreground font-mono bg-muted p-2 rounded">
                          {editingVersion
                            ? editingVersion.version
                            : versions && versions.length > 0
                              ? calculateNextVersion(versions[versions.length - 1].version)
                              : calculateNextVersion(agent?.version || "1.0.0")}
                        </p>
                        <p className="text-xs text-muted-foreground">
                          {t("agents.registry.detail.versions.autoGenerated", "Generada automáticamente")}
                        </p>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("common.status", "Estado")} *
                        </label>
                        <Select value={newVersion.status} onValueChange={(value) => setNewVersion({ ...newVersion, status: value })}>
                          <SelectTrigger>
                            <SelectValue placeholder={t("agents.registry.create.statusPlaceholder", "Seleccione el estado")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="ACTIVE">{t("agents.registry.create.statusActive", "Activo")}</SelectItem>
                            <SelectItem value="INACTIVE">{t("agents.registry.create.statusInactive", "Inactivo")}</SelectItem>
                            <SelectItem value="PENDING">{t("agents.registry.create.statusPending", "Pendiente")}</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2 md:col-span-2">
                        <label className="text-sm font-medium">
                          {t("agents.registry.description", "Descripción")}
                        </label>
                        <Textarea
                          placeholder={t("agents.registry.create.descriptionPlaceholder", "Ingrese una descripción")}
                          value={newVersion.description}
                          onChange={(e) => setNewVersion({ ...newVersion, description: e.target.value })}
                          rows={3}
                        />
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4">
                      <Button
                        variant="outline"
                        onClick={() => {
                          setShowVersionForm(false);
                          setEditingVersion(null);
                          setNewVersion({
                            version: "",
                            description: "",
                            status: "",
                          });
                        }}
                      >
                        {t("common.cancel", "Cancelar")}
                      </Button>
                      <Button onClick={handleCreateVersion} className="bg-primary hover:bg-primary/90">
                        {editingVersion
                          ? t("common.save", "Guardar")
                          : t("agents.registry.detail.versions.addButton", "Agregar Versión")}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              )}

              {versions && versions.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("agents.registry.version", "Versión")}</th>
                        <th className="text-left p-2">{t("agents.registry.description", "Descripción")}</th>
                        <th className="text-left p-2">{t("common.status", "Estado")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.versions.createdAt", "Creado")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.versions.createdBy", "Creado Por")}</th>
                        <th className="text-center p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {versions.map((version) => (
                        <tr key={version.id} className="border-b hover:bg-muted/50">
                          <td className="p-2 font-mono">{version.version}</td>
                          <td className="p-2">{version.description || <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">{getStatusBadge(version.status)}</td>
                          <td className="p-2">{new Date(version.createdAt).toLocaleString()}</td>
                          <td className="p-2">{version.createdBy}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.edit", "Editar")}
                                onClick={() => {
                                  setEditingVersion(version);
                                  setNewVersion({
                                    version: version.version,
                                    description: version.description || "",
                                    status: version.status,
                                  });
                                  setShowVersionForm(true);
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
                                  if (confirm(t("common.confirmDelete", "¿Está seguro de eliminar esta versión?"))) {
                                    setVersions(versions.filter(v => v.id !== version.id));
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
                  {t("agents.registry.detail.versions.noVersions", "No hay versiones registradas")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Monitoreo */}
        <TabsContent value="monitoring" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.registry.detail.monitoring.totalInteractions", "Total Interacciones")}
                </p>
                <h2 className="text-2xl font-bold">{metrics?.totalInteractions.toLocaleString() || 0}</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.registry.detail.monitoring.successRate", "Tasa de Éxito")}
                </p>
                <h2 className="text-2xl font-bold">{metrics?.successRate.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.registry.detail.monitoring.avgResponseTime", "Tiempo Respuesta Promedio")}
                </p>
                <h2 className="text-2xl font-bold">{metrics?.averageResponseTime || 0}ms</h2>
              </CardBody>
            </Card>
            <Card className="border-2">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.registry.detail.monitoring.uptime", "Uptime")}
                </p>
                <h2 className="text-2xl font-bold">{metrics?.uptime.toFixed(1) || 0}%</h2>
              </CardBody>
            </Card>
          </div>

          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5" />
                {t("agents.registry.detail.monitoring.trends", "Tendencias")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {metrics?.trends && metrics.trends.length > 0 ? (
                <div className="h-64 border rounded-lg flex items-center justify-center">
                  <p className="text-muted-foreground">
                    {t("agents.registry.detail.monitoring.chartPlaceholder", "Gráfico de tendencias (implementar con Chart.js)")}
                  </p>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("agents.registry.detail.monitoring.noData", "No hay datos de monitoreo disponibles")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Despliegues */}
        <TabsContent value="deployments" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="w-5 h-5" />
                {t("agents.registry.detail.tabs.deployments", "Despliegues")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {deployments && deployments.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("agents.registry.detail.deployments.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("common.status", "Estado")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.deployments.environment", "Entorno")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.deployments.deployedAt", "Desplegado")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.deployments.endpoint", "Endpoint")}</th>
                        <th className="text-center p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {deployments.map((deployment) => (
                        <tr key={deployment.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">{deployment.name}</td>
                          <td className="p-2">{getStatusBadge(deployment.status)}</td>
                          <td className="p-2">{deployment.environment}</td>
                          <td className="p-2">{new Date(deployment.deployedAt).toLocaleString()}</td>
                          <td className="p-2">
                            {deployment.endpoint ? (
                              <a href={deployment.endpoint} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline font-mono text-xs">
                                {deployment.endpoint}
                              </a>
                            ) : (
                              <span className="text-muted-foreground">-</span>
                            )}
                          </td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                title={t("common.view", "Ver")}
                                onClick={() => {
                                  window.location.href = `/governance/agents/deployment/${deployment.id}`;
                                }}
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
                  {t("agents.registry.detail.deployments.noDeployments", "No hay despliegues registrados")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Proyectos */}
        <TabsContent value="projects" className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FolderOpen className="w-5 h-5" />
                {t("agents.registry.detail.tabs.projects", "Proyectos Asociados")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {projects && projects.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("agents.registry.detail.projects.name", "Nombre del Proyecto")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.projects.usageType", "Tipo de Uso")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.projects.role", "Rol")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.projects.lastUsed", "Último Uso")}</th>
                        <th className="text-left p-2">{t("agents.registry.detail.projects.createdAt", "Asociado Desde")}</th>
                        <th className="text-center p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {projects.map((project) => (
                        <tr key={project.id} className="border-b hover:bg-muted/50">
                          <td className="p-2 font-medium">{project.projectName}</td>
                          <td className="p-2">
                            <Badge
                              variant="outline"
                              className={
                                project.usageType === "PRIMARY"
                                  ? "bg-blue-500/20 text-blue-500 border-blue-500/50"
                                  : project.usageType === "SECONDARY"
                                  ? "bg-purple-500/20 text-purple-500 border-purple-500/50"
                                  : project.usageType === "TESTING"
                                  ? "bg-yellow-500/20 text-yellow-500 border-yellow-500/50"
                                  : "bg-gray-500/20 text-gray-500 border-gray-500/50"
                              }
                            >
                              {project.usageType}
                            </Badge>
                          </td>
                          <td className="p-2">{project.role || <span className="text-muted-foreground">-</span>}</td>
                          <td className="p-2">
                            {project.lastUsedAt
                              ? new Date(project.lastUsedAt).toLocaleString()
                              : <span className="text-muted-foreground">-</span>}
                          </td>
                          <td className="p-2">
                            {new Date(project.createdAt).toLocaleDateString()}
                          </td>
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
                  {t("agents.registry.detail.projects.noProjects", "No hay proyectos asociados")}
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        {/* Tab: Cumplimiento */}
        <TabsContent value="compliance" className="space-y-6">
          <ComplianceTab
            agentId={agentId}
            agentUuid={agent?.uuid}
            agentName={agent?.name}
          />
        </TabsContent>

        {/* Tab: Reglas de Revisión */}
        <TabsContent value="review-rules" className="space-y-6">
          <ReviewRulesTab agentId={agentId} agentUuid={agent?.uuid} />
        </TabsContent>

        {/* Tab: Certificación */}
        <TabsContent value="certification" className="space-y-6">
          <CertificationTab
            agentId={agentId}
            agentUuid={agent?.uuid}
            agentName={agent?.name}
            agentStatus={agent?.status}
          />
        </TabsContent>

        {/* Tab: Retiro */}
        <TabsContent value="retirement" className="space-y-6">
          <RetirementTab
            agentId={agentId}
            agentUuid={agent?.uuid}
            agentName={agent?.name}
            agentStatus={agent?.status}
          />
        </TabsContent>

        {/* Tab: Configuración */}
        <TabsContent value="configuration" className="space-y-6">
          <ConfigurationTab agentId={agentId} agentUuid={agent?.uuid} />
        </TabsContent>
      </Tabs>

      {/* Modal de Información del Proyecto */}
      <SimpleModal
        isOpen={selectedProject !== null}
        onClose={() => setSelectedProject(null)}
        title={selectedProject ? `${t("agents.registry.detail.projects.title", "Información del Proyecto")} - ${selectedProject.projectName}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedProject && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.registry.detail.projects.name", "Nombre del Proyecto")}
                    </label>
                    <p className="text-base font-semibold">{selectedProject.projectName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.registry.detail.projects.usageType", "Tipo de Uso")}
                    </label>
                    <div className="mt-1">
                      <Badge
                        variant="outline"
                        className={
                          selectedProject.usageType === "PRIMARY"
                            ? "bg-blue-500/20 text-blue-500 border-blue-500/50"
                            : selectedProject.usageType === "SECONDARY"
                            ? "bg-purple-500/20 text-purple-500 border-purple-500/50"
                            : selectedProject.usageType === "TESTING"
                            ? "bg-yellow-500/20 text-yellow-500 border-yellow-500/50"
                            : "bg-gray-500/20 text-gray-500 border-gray-500/50"
                        }
                      >
                        {selectedProject.usageType}
                      </Badge>
                    </div>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.registry.detail.projects.role", "Rol")}
                    </label>
                    <p className="text-base">{selectedProject.role || <span className="text-muted-foreground">-</span>}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.registry.detail.projects.lastUsed", "Último Uso")}
                    </label>
                    <p className="text-base">
                      {selectedProject.lastUsedAt
                        ? new Date(selectedProject.lastUsedAt).toLocaleString()
                        : <span className="text-muted-foreground">-</span>}
                    </p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.registry.detail.projects.createdAt", "Asociado Desde")}
                    </label>
                    <p className="text-base">
                      {new Date(selectedProject.createdAt).toLocaleString()}
                    </p>
                  </div>
                </div>
                <div className="pt-4 border-t">
                  <p className="text-sm text-muted-foreground">
                    {t("agents.registry.detail.projects.description", "Este proyecto utiliza este agente para sus operaciones.")}
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
