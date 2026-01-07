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
import {
  ArrowLeft,
  Brain,
  Database,
  Edit,
  Eye,
  FileText,
  Plus,
  Save,
  Settings,
  Tag,
  Trash2,
  TrendingUp
} from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface ModelReference {
  idxmodel: number; // IDXMODEL (PK de MODMODELS)
  modelId: string; // UUID del modelo (MODMODELID)
  modelName: string; // MODNAME
  displayName: string; // MODDISPLAYNAME
  type: "EMBEDDING" | "RERANKER" | "LLM"; // MODTYPE
}

interface RAGProject {
  id: number;
  projectId: string; // UUID generado automáticamente (ID del RagSystem en ejecución)
  name: string;
  displayName: string;
  description: string;
  version: string; // Versión semántica automática
  status: string;
  owner: string;
  createdAt: string;
  updatedAt: string;
  // Vinculación con Project de cumplimiento
  complianceProjectId?: number; // IDXPROJECT (FK a PRJPROJECTS donde PRJISTYPE = "RAG")
  complianceProjectUuid?: string; // UUID del Project de cumplimiento
  complianceProjectName?: string; // Nombre del Project de cumplimiento
  chunks?: number;
  embeddings?: number;
  searches?: number;
  models?: {
    embedding?: ModelReference;
    reranker?: ModelReference;
    llm?: ModelReference;
  };
  performance?: {
    avgResponseTime: number;
    successRate: number;
    totalCost: number;
  };
  versions?: RAGProjectVersion[];
  dataSources?: RAGProjectDataSource[];
}

interface RAGProjectVersion {
  id: number;
  version: string;
  description?: string;
  status: string;
  createdAt: string;
  createdBy: string;
}

interface DataSourceReference {
  idxdatasource: number; // IDXDATASOURCE (PK de DATASOURCES)
  datasourceId: string; // UUID de la fuente de datos
  name: string; // Nombre de la fuente de datos
  displayName: string; // Nombre para mostrar
  type: "DATABASE" | "FILE" | "API" | "WEB" | "UPLOAD_DOCUMENTS" | "OTHER"; // Tipo de fuente
}

interface RAGProjectDataSource {
  id: number;
  idxdatasource: number; // FK a DATASOURCES.IDXDATASOURCE
  datasourceId: string; // UUID de la fuente de datos
  name: string;
  displayName: string;
  type: string;
  status: string;
  linkedAt: string; // Fecha de vinculación al proyecto RAG
  linkedBy: string; // Usuario que vinculó la fuente
}

export default function RAGProjectDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const id = params.id as string;
  const isNew = id === "new";

  const [loading, setLoading] = useState(!isNew);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState(isNew);
  const [project, setProject] = useState<RAGProject>({
    id: 0,
    projectId: "", // UUID generado automáticamente
    name: "",
    displayName: "",
    description: "",
    version: "1.0.0", // Versión inicial automática
    status: "ACTIVE",
    owner: "",
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    versions: [],
    dataSources: [],
  });

  const [newVersion, setNewVersion] = useState({
    version: "",
    description: "",
    status: "",
  });
  const [showVersionForm, setShowVersionForm] = useState(false);
  const [editingVersion, setEditingVersion] = useState<RAGProjectVersion | null>(null);
  const [selectedDataSourceId, setSelectedDataSourceId] = useState<string>("");
  const [showDataSourceSelector, setShowDataSourceSelector] = useState(false);
  const [availableDataSources, setAvailableDataSources] = useState<DataSourceReference[]>([]);
  const [availableModels, setAvailableModels] = useState<ModelReference[]>([]);

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

  useEffect(() => {
    if (!isNew) {
      loadProject();
    } else {
      // Generar UUID automáticamente para nuevo proyecto
      const uuid = generateUUID();
      setProject((prev) => ({ ...prev, projectId: uuid, version: "1.0.0" }));
    }
    loadAvailableModels();
    loadAvailableDataSources();
  }, [id]);

  const loadAvailableDataSources = async () => {
    try {
      // Llamada API para obtener fuentes de datos disponibles del registro
      // GET /api/data-sources/registry
      const response = await fetch("/api/data-sources/registry");
      if (response.ok) {
        const data = await response.json();
        // Mapear fuentes de datos del registro a DataSourceReference
        const dataSources: DataSourceReference[] = data.items?.map((ds: any) => ({
          idxdatasource: ds.id || ds.idxdatasource,
          datasourceId: ds.datasourceId || ds.iduuid || ds.id,
          name: ds.name || ds.dsname,
          displayName: ds.displayName || ds.dsdisplayname || ds.name || ds.dsname,
          type: ds.type || ds.dstype,
        })) || [];
        setAvailableDataSources(dataSources);
      } else {
        // Fallback a mock data si falla la API
        const mockDataSources: DataSourceReference[] = [
          {
            idxdatasource: 1,
            datasourceId: "550e8400-e29b-41d4-a716-446655440010",
            name: "PostgreSQL Production",
            displayName: "PostgreSQL Production",
            type: "DATABASE",
          },
          {
            idxdatasource: 2,
            datasourceId: "550e8400-e29b-41d4-a716-446655440011",
            name: "Documentación API",
            displayName: "Documentación API",
            type: "API",
          },
          {
            idxdatasource: 3,
            datasourceId: "550e8400-e29b-41d4-a716-446655440012",
            name: "Knowledge Base Docs",
            displayName: "Knowledge Base Docs",
            type: "UPLOAD_DOCUMENTS",
          },
        ];
        setAvailableDataSources(mockDataSources);
      }
    } catch (error) {
      console.error("Error loading data sources:", error);
      setAvailableDataSources([]);
    }
  };

  const loadAvailableModels = async () => {
    try {
      // Llamada API para obtener modelos del registro de modelos
      // GET /api/models/registry?type=EMBEDDING|RERANKER|LLM
      const response = await fetch("/api/models/registry");
      if (response.ok) {
        const data = await response.json();
        // Mapear modelos del registro a ModelReference
        const models: ModelReference[] = data.items?.map((model: any) => ({
          idxmodel: model.id, // IDXMODEL
          modelId: model.modelId || model.iduuid, // UUID del modelo
          modelName: model.modelName || model.modname, // MODNAME
          displayName: model.displayName || model.moddisplayname || model.modelName || model.modname,
          type: model.type || model.modtype, // MODTYPE debe ser EMBEDDING, RERANKER o LLM
        })) || [];
        setAvailableModels(models);
      } else {
        // Fallback a mock data si falla la API
        const mockModels: ModelReference[] = [
          {
            idxmodel: 1,
            modelId: "550e8400-e29b-41d4-a716-446655440000",
            modelName: "text-embedding-ada-002",
            displayName: "text-embedding-ada-002",
            type: "EMBEDDING",
          },
          {
            idxmodel: 2,
            modelId: "550e8400-e29b-41d4-a716-446655440001",
            modelName: "cohere-rerank-multilingual-v2.0",
            displayName: "cohere-rerank-multilingual-v2.0",
            type: "RERANKER",
          },
          {
            idxmodel: 3,
            modelId: "550e8400-e29b-41d4-a716-446655440002",
            modelName: "gpt-4-turbo",
            displayName: "GPT-4 Turbo",
            type: "LLM",
          },
          {
            idxmodel: 4,
            modelId: "550e8400-e29b-41d4-a716-446655440003",
            modelName: "text-embedding-3-large",
            displayName: "text-embedding-3-large",
            type: "EMBEDDING",
          },
          {
            idxmodel: 5,
            modelId: "550e8400-e29b-41d4-a716-446655440004",
            modelName: "claude-3-opus",
            displayName: "Claude 3 Opus",
            type: "LLM",
          },
        ];
        setAvailableModels(mockModels);
      }
    } catch (error) {
      console.error("Error loading models:", error);
      // Fallback a mock data en caso de error
      setAvailableModels([]);
    }
  };

  const loadProject = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamada API
      const mockProject: RAGProject = {
        id: parseInt(id),
        projectId: "550e8400-e29b-41d4-a716-446655440000", // UUID del RagSystem en ejecución
        name: "DemoRAGProject",
        displayName: "Demo RAG Project",
        description: "Proyecto RAG de demostración para testing",
        version: "1.2.3",
        status: "ACTIVE",
        owner: "Admin User",
        createdAt: "2024-01-01T00:00:00Z",
        updatedAt: "2024-01-15T10:30:00Z",
        // Vinculación con Project de cumplimiento
        complianceProjectId: 1001, // IDXPROJECT en PRJPROJECTS
        complianceProjectUuid: "660e8400-e29b-41d4-a716-446655440001",
        complianceProjectName: "DemoRAGProject - Compliance",
        chunks: 2500,
        embeddings: 2500,
        searches: 45,
        models: {
          embedding: {
            idxmodel: 1,
            modelId: "550e8400-e29b-41d4-a716-446655440000",
            modelName: "text-embedding-ada-002",
            displayName: "text-embedding-ada-002",
            type: "EMBEDDING",
          },
          reranker: {
            idxmodel: 2,
            modelId: "550e8400-e29b-41d4-a716-446655440001",
            modelName: "cohere-rerank-multilingual-v2.0",
            displayName: "cohere-rerank-multilingual-v2.0",
            type: "RERANKER",
          },
          llm: {
            idxmodel: 3,
            modelId: "550e8400-e29b-41d4-a716-446655440002",
            modelName: "gpt-4-turbo",
            displayName: "GPT-4 Turbo",
            type: "LLM",
          },
        },
        performance: {
          avgResponseTime: 1.2,
          successRate: 94.5,
          totalCost: 12.5,
        },
        versions: [
          {
            id: 1,
            version: "1.0.0",
            description: "Versión inicial",
            status: "ACTIVE",
            createdAt: "2024-01-01T00:00:00Z",
            createdBy: "admin",
          },
          {
            id: 2,
            version: "1.1.0",
            description: "Mejoras de rendimiento",
            status: "ACTIVE",
            createdAt: "2024-01-10T00:00:00Z",
            createdBy: "admin",
          },
          {
            id: 3,
            version: "1.2.3",
            description: "Bug fixes y optimizaciones",
            status: "ACTIVE",
            createdAt: "2024-01-15T00:00:00Z",
            createdBy: "admin",
          },
        ],
        dataSources: [
          {
            id: 1,
            idxdatasource: 1,
            datasourceId: "550e8400-e29b-41d4-a716-446655440010",
            name: "PostgreSQL Production",
            displayName: "PostgreSQL Production",
            type: "DATABASE",
            status: "ACTIVE",
            linkedAt: "2024-01-01T00:00:00Z",
            linkedBy: "admin",
          },
          {
            id: 2,
            idxdatasource: 2,
            datasourceId: "550e8400-e29b-41d4-a716-446655440011",
            name: "Documentación API",
            displayName: "Documentación API",
            type: "API",
            status: "ACTIVE",
            linkedAt: "2024-01-05T00:00:00Z",
            linkedBy: "admin",
          },
        ],
      };
      setProject(mockProject);
    } catch (error) {
      console.error("Error loading project:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      // TODO: Implementar llamada API
      if (isNew) {
        // Crear nuevo proyecto
        router.push("/rag/projects");
      } else {
        // Actualizar proyecto existente
        setEditing(false);
      }
    } catch (error) {
      console.error("Error saving project:", error);
    } finally {
      setSaving(false);
    }
  };

  const calculateNextVersion = (
    currentVersion: string,
    versionType: "major" | "minor" | "patch" = "patch"
  ): string => {
    const parts = currentVersion.split(".").map(Number);
    if (parts.length !== 3) return "1.0.0";

    if (versionType === "major") {
      return `${parts[0] + 1}.0.0`;
    } else if (versionType === "minor") {
      return `${parts[0]}.${parts[1] + 1}.0`;
    } else {
      return `${parts[0]}.${parts[1]}.${parts[2] + 1}`;
    }
  };

  const handleCreateVersion = async () => {
    if (!newVersion.status) {
      alert(t("rag.projects.detail.versions.validationError", "Por favor, complete todos los campos obligatorios"));
      return;
    }

    try {
      // TODO: Implementar llamada API
      if (editingVersion) {
        // Actualizar versión existente
        const updatedVersion: RAGProjectVersion = {
          ...editingVersion,
          description: newVersion.description,
          status: newVersion.status,
        };

        setProject({
          ...project,
          versions:
            project.versions?.map((v) => (v.id === editingVersion.id ? updatedVersion : v)) || [],
        });
        setEditingVersion(null);
      } else {
        // Crear nueva versión - calcular automáticamente
        const latestVersion =
          project.versions && project.versions.length > 0
            ? project.versions[project.versions.length - 1].version
            : project.version || "1.0.0";
        const nextVersion = calculateNextVersion(latestVersion, "patch");

        const newVer: RAGProjectVersion = {
          id: (project.versions?.length || 0) + 1,
          version: nextVersion,
          description: newVersion.description,
          status: newVersion.status,
          createdAt: new Date().toISOString(),
          createdBy: "current_user", // TODO: Obtener del contexto
        };

        setProject({
          ...project,
          versions: [...(project.versions || []), newVer],
          version: nextVersion, // Actualizar versión actual del proyecto
        });
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

  const handleAddDataSource = async () => {
    if (!selectedDataSourceId) {
      alert(t("rag.projects.detail.dataSources.validationError", "Por favor, seleccione una fuente de datos"));
      return;
    }

    try {
      // TODO: Implementar llamada API
      const selectedDataSource = availableDataSources.find(
        (ds) => ds.idxdatasource.toString() === selectedDataSourceId
      );

      if (!selectedDataSource) return;

      // Verificar si ya está vinculada
      if (project.dataSources?.some((ds) => ds.idxdatasource === selectedDataSource.idxdatasource)) {
        alert(t("rag.projects.detail.dataSources.alreadyLinked", "Esta fuente de datos ya está vinculada al proyecto"));
        return;
      }

      // Vincular fuente de datos existente al proyecto RAG
      const newLinkedDS: RAGProjectDataSource = {
        id: (project.dataSources?.length || 0) + 1,
        idxdatasource: selectedDataSource.idxdatasource,
        datasourceId: selectedDataSource.datasourceId,
        name: selectedDataSource.name,
        displayName: selectedDataSource.displayName,
        type: selectedDataSource.type,
        status: "ACTIVE",
        linkedAt: new Date().toISOString(),
        linkedBy: "current_user", // TODO: Obtener del contexto
      };

      setProject({
        ...project,
        dataSources: [...(project.dataSources || []), newLinkedDS],
      });

      setSelectedDataSourceId("");
      setShowDataSourceSelector(false);
    } catch (error) {
      console.error("Error linking data source:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    if (status === "ACTIVE") {
      return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">ACTIVE</Badge>;
    } else if (status === "INACTIVE") {
      return <Badge className="bg-gray-500/20 text-gray-400 border-gray-500/50">INACTIVE</Badge>;
    } else if (status === "PAUSED") {
      return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">PAUSED</Badge>;
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
      {/* Header - Título y Subtítulo */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <Settings className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {isNew
              ? t("rag.projects.detail.createTitle", "Nuevo Proyecto RAG")
              : project.displayName || project.name}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9">
          {isNew
            ? t("rag.projects.detail.createSubtitle", "Crear un nuevo proyecto RAG")
            : t("rag.projects.detail.subtitle", "Detalles y gestión del proyecto RAG")}
        </p>
      </div>

      {/* Botones de Acción - Volver a la izquierda, Acciones a la derecha */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => {
            router.push("/rag/projects");
          }}
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          {!isNew && !editing && (
            <Button variant="outline" onClick={() => setEditing(true)}>
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
                    router.push("/rag/projects");
                  } else {
                    setEditing(false);
                    loadProject();
                  }
                }}
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button onClick={handleSave} disabled={saving} className="bg-primary hover:bg-primary/90">
                <Save className="w-4 h-4 mr-2" />
                {saving ? t("common.saving", "Guardando...") : t("common.save", "Guardar")}
              </Button>
            </>
          )}
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="general" className="space-y-6">
        <TabsList className="flex space-x-1 p-1 overflow-x-auto">
          <TabsTrigger value="general" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.general", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="versions" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.versions", "Versiones")}
          </TabsTrigger>
          <TabsTrigger value="models" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.models", "Modelos")}
          </TabsTrigger>
          <TabsTrigger value="dataSources" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.dataSources", "Fuentes de Datos")}
          </TabsTrigger>
          <TabsTrigger value="chunks" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.chunks", "Chunks")}
          </TabsTrigger>
          <TabsTrigger value="performance" className="flex-1 min-w-fit">
            {t("rag.projects.detail.tabs.performance", "Rendimiento")}
          </TabsTrigger>
        </TabsList>

        {/* Tab: Información General */}
        <TabsContent value="general">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle>{t("rag.projects.detail.general.title", "Información del Proyecto RAG")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.name", "Nombre del Proyecto")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={project.name}
                      onChange={(e) => setProject({ ...project, name: e.target.value })}
                      placeholder={t("rag.projects.detail.general.namePlaceholder", "Ingrese el nombre del proyecto")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{project.name}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.projectId", "ID del Proyecto (UUID)")}
                  </label>
                  <p className="text-sm text-foreground font-mono text-muted-foreground">
                    {project.projectId || t("rag.projects.detail.general.uuidAutoGenerated", "Se generará automáticamente")}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {t("rag.projects.detail.general.uuidInfo", "UUID generado automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.displayName", "Nombre de Visualización")} *
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={project.displayName}
                      onChange={(e) => setProject({ ...project, displayName: e.target.value })}
                      placeholder={t("rag.projects.detail.general.displayNamePlaceholder", "Ingrese el nombre de visualización")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{project.displayName}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.version", "Versión")}
                  </label>
                  <p className="text-sm text-foreground font-mono">{project.version || "1.0.0"}</p>
                  <p className="text-xs text-muted-foreground">
                    {t("rag.projects.detail.general.versionAutoGenerated", "Versión semántica generada automáticamente")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.owner", "Propietario")}
                  </label>
                  {editing || isNew ? (
                    <Input
                      value={project.owner}
                      onChange={(e) => setProject({ ...project, owner: e.target.value })}
                      placeholder={t("rag.projects.detail.general.ownerPlaceholder", "Ingrese el propietario")}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{project.owner || "-"}</p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.complianceProject", "Proyecto de Cumplimiento")}
                  </label>
                  {project.complianceProjectName ? (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">{project.complianceProjectName}</p>
                      {project.complianceProjectId && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            router.push(`/governance/projects/list?projectId=${project.complianceProjectId}`);
                          }}
                          title={t("rag.projects.detail.general.viewComplianceProject", "Ver proyecto de cumplimiento")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  ) : (
                    <p className="text-sm text-muted-foreground">
                      {t("rag.projects.detail.general.noComplianceProject", "No vinculado a proyecto de cumplimiento")}
                    </p>
                  )}
                  <p className="text-xs text-muted-foreground">
                    {t("rag.projects.detail.general.complianceProjectInfo", "Vinculado a Project en PRJPROJECTS con PRJISTYPE = 'RAG'")}
                  </p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.status", "Estado")} *
                  </label>
                  {editing || isNew ? (
                    <Select value={project.status} onValueChange={(value) => setProject({ ...project, status: value })}>
                      <SelectTrigger>
                        <SelectValue placeholder={t("rag.projects.detail.general.statusPlaceholder", "Seleccione el estado")} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                        <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                        <SelectItem value="PAUSED">PAUSED</SelectItem>
                      </SelectContent>
                    </Select>
                  ) : (
                    <div>{getStatusBadge(project.status)}</div>
                  )}
                </div>
                <div className="space-y-2 md:col-span-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.general.description", "Descripción")}
                  </label>
                  {editing || isNew ? (
                    <Textarea
                      value={project.description}
                      onChange={(e) => setProject({ ...project, description: e.target.value })}
                      placeholder={t("rag.projects.detail.general.descriptionPlaceholder", "Ingrese una descripción del proyecto")}
                      rows={3}
                    />
                  ) : (
                    <p className="text-sm text-foreground">{project.description || "-"}</p>
                  )}
                </div>
              </div>
            </CardBody>
          </Card>
          </div>
        </TabsContent>

        {/* Tab: Versiones */}
        <TabsContent value="versions">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Tag className="w-5 h-5" />
                {t("rag.projects.detail.tabs.versions", "Versiones")}
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
                {t("rag.projects.detail.versions.addButton", "Agregar Versión")}
              </Button>
            </CardHeader>
            <CardBody>
              {showVersionForm && (
                <Card className="border-2 mb-6">
                  <CardHeader>
                    <CardTitle className="text-lg">
                      {editingVersion
                        ? t("rag.projects.detail.versions.editTitle", "Editar Versión")
                        : t("rag.projects.detail.versions.createTitle", "Agregar Versión")}
                    </CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("rag.projects.detail.general.version", "Versión")}
                        </label>
                        <p className="text-sm text-foreground font-mono bg-muted p-2 rounded">
                          {editingVersion
                            ? editingVersion.version
                            : project.versions && project.versions.length > 0
                              ? calculateNextVersion(project.versions[project.versions.length - 1].version)
                              : calculateNextVersion(project.version || "1.0.0")}
                        </p>
                        <p className="text-xs text-muted-foreground">
                          {t("rag.projects.detail.versions.autoGenerated", "Generada automáticamente")}
                        </p>
                      </div>
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("rag.projects.detail.general.status", "Estado")} *
                        </label>
                        <Select
                          value={newVersion.status}
                          onValueChange={(value) => setNewVersion({ ...newVersion, status: value })}
                        >
                          <SelectTrigger>
                            <SelectValue placeholder={t("rag.projects.detail.general.statusPlaceholder", "Seleccione el estado")} />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                            <SelectItem value="INACTIVE">INACTIVE</SelectItem>
                            <SelectItem value="PAUSED">PAUSED</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                      <div className="space-y-2 md:col-span-2">
                        <label className="text-sm font-medium">
                          {t("rag.projects.detail.general.description", "Descripción")}
                        </label>
                        <Textarea
                          placeholder={t("rag.projects.detail.general.descriptionPlaceholder", "Ingrese una descripción")}
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
                          : t("rag.projects.detail.versions.addButton", "Agregar Versión")}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              )}

              {project.versions && project.versions.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("rag.projects.detail.general.version", "Versión")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.general.description", "Descripción")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.general.status", "Estado")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.versions.createdAt", "Creado")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.versions.createdBy", "Creado Por")}</th>
                        <th className="text-center p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {project.versions.map((version) => (
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
                                    setProject({
                                      ...project,
                                      versions: project.versions?.filter((v) => v.id !== version.id) || [],
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
                  {t("rag.projects.detail.versions.noVersions", "No hay versiones registradas")}
                </div>
              )}
            </CardBody>
          </Card>
          </div>
        </TabsContent>

        {/* Tab: Modelos */}
        <TabsContent value="models">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Brain className="w-5 h-5" />
                {t("rag.projects.detail.tabs.models", "Modelos")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.models.embedding", "Modelo de Embedding")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={project.models?.embedding?.idxmodel?.toString() || ""}
                      onValueChange={(value) => {
                        const selectedModel = availableModels.find(
                          (m) => m.idxmodel.toString() === value && m.type === "EMBEDDING"
                        );
                        setProject({
                          ...project,
                          models: {
                            ...project.models,
                            embedding: selectedModel,
                          },
                        });
                      }}
                    >
                      <SelectTrigger>
                        <SelectValue
                          placeholder={t("rag.projects.detail.models.selectEmbedding", "Seleccione un modelo de embedding")}
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="">
                          {t("rag.projects.detail.models.none", "Sin modelo")}
                        </SelectItem>
                        {availableModels
                          .filter((m) => m.type === "EMBEDDING")
                          .map((model) => (
                            <SelectItem key={model.idxmodel} value={model.idxmodel.toString()}>
                              {model.displayName}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">
                        {project.models?.embedding?.displayName || "-"}
                      </p>
                      {project.models?.embedding && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            router.push(`/models/registry/${project.models?.embedding?.idxmodel}`);
                          }}
                          title={t("rag.projects.detail.models.viewModel", "Ver modelo en registro")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  )}
                  {project.models?.embedding && (
                    <p className="text-xs text-muted-foreground font-mono">
                      {project.models.embedding.modelId.substring(0, 8)}...
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.models.reranker", "Modelo de Reranker")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={project.models?.reranker?.idxmodel?.toString() || ""}
                      onValueChange={(value) => {
                        const selectedModel = availableModels.find(
                          (m) => m.idxmodel.toString() === value && m.type === "RERANKER"
                        );
                        setProject({
                          ...project,
                          models: {
                            ...project.models,
                            reranker: selectedModel,
                          },
                        });
                      }}
                    >
                      <SelectTrigger>
                        <SelectValue
                          placeholder={t("rag.projects.detail.models.selectReranker", "Seleccione un modelo de reranker")}
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="">
                          {t("rag.projects.detail.models.none", "Sin modelo")}
                        </SelectItem>
                        {availableModels
                          .filter((m) => m.type === "RERANKER")
                          .map((model) => (
                            <SelectItem key={model.idxmodel} value={model.idxmodel.toString()}>
                              {model.displayName}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">
                        {project.models?.reranker?.displayName || "-"}
                      </p>
                      {project.models?.reranker && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            router.push(`/models/registry/${project.models?.reranker?.idxmodel}`);
                          }}
                          title={t("rag.projects.detail.models.viewModel", "Ver modelo en registro")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  )}
                  {project.models?.reranker && (
                    <p className="text-xs text-muted-foreground font-mono">
                      {project.models.reranker.modelId.substring(0, 8)}...
                    </p>
                  )}
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.models.llm", "Modelo LLM")}
                  </label>
                  {editing || isNew ? (
                    <Select
                      value={project.models?.llm?.idxmodel?.toString() || ""}
                      onValueChange={(value) => {
                        const selectedModel = availableModels.find(
                          (m) => m.idxmodel.toString() === value && m.type === "LLM"
                        );
                        setProject({
                          ...project,
                          models: {
                            ...project.models,
                            llm: selectedModel,
                          },
                        });
                      }}
                    >
                      <SelectTrigger>
                        <SelectValue
                          placeholder={t("rag.projects.detail.models.selectLLM", "Seleccione un modelo LLM")}
                        />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="">
                          {t("rag.projects.detail.models.none", "Sin modelo")}
                        </SelectItem>
                        {availableModels
                          .filter((m) => m.type === "LLM")
                          .map((model) => (
                            <SelectItem key={model.idxmodel} value={model.idxmodel.toString()}>
                              {model.displayName}
                            </SelectItem>
                          ))}
                      </SelectContent>
                    </Select>
                  ) : (
                    <div className="flex items-center gap-2">
                      <p className="text-sm text-foreground">
                        {project.models?.llm?.displayName || "-"}
                      </p>
                      {project.models?.llm && (
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            router.push(`/models/registry/${project.models?.llm?.idxmodel}`);
                          }}
                          title={t("rag.projects.detail.models.viewModel", "Ver modelo en registro")}
                        >
                          <Eye className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                  )}
                  {project.models?.llm && (
                    <p className="text-xs text-muted-foreground font-mono">
                      {project.models.llm.modelId.substring(0, 8)}...
                    </p>
                  )}
                </div>
              </div>
              <div className="mt-4 pt-4 border-t border-border">
                <Button
                  variant="outline"
                  onClick={() => router.push("/models/registry")}
                  className="w-full"
                >
                  <Brain className="w-4 h-4 mr-2" />
                  {t("rag.projects.detail.models.viewAllModels", "Ver todos los modelos en el registro")}
                </Button>
              </div>
            </CardBody>
          </Card>
          </div>
        </TabsContent>

        {/* Tab: Fuentes de Datos */}
        <TabsContent value="dataSources">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <Database className="w-5 h-5" />
                {t("rag.projects.detail.tabs.dataSources", "Fuentes de Datos")}
              </CardTitle>
              <Button
                onClick={() => {
                  setSelectedDataSourceId("");
                  setShowDataSourceSelector(!showDataSourceSelector);
                }}
                className="bg-primary hover:bg-primary/90"
                size="sm"
              >
                <Plus className="w-4 h-4 mr-2" />
                {t("rag.projects.detail.dataSources.addButton", "Añadir Fuente de Datos")}
              </Button>
            </CardHeader>
            <CardBody>
              {showDataSourceSelector && (
                <Card className="border-2 mb-6">
                  <CardHeader>
                    <CardTitle className="text-lg">
                      {t("rag.projects.detail.dataSources.selectTitle", "Seleccionar Fuente de Datos")}
                    </CardTitle>
                  </CardHeader>
                  <CardBody>
                    <div className="space-y-4">
                      <div className="space-y-2">
                        <label className="text-sm font-medium">
                          {t("rag.projects.detail.dataSources.selectDataSource", "Seleccionar Fuente de Datos")} *
                        </label>
                        <Select
                          value={selectedDataSourceId}
                          onValueChange={(value) => setSelectedDataSourceId(value)}
                        >
                          <SelectTrigger>
                            <SelectValue
                              placeholder={t("rag.projects.detail.dataSources.selectPlaceholder", "Seleccione una fuente de datos del registro")}
                            />
                          </SelectTrigger>
                          <SelectContent>
                            {availableDataSources
                              .filter((ds) => !project.dataSources?.some((linkedDS) => linkedDS.idxdatasource === ds.idxdatasource))
                              .map((dataSource) => (
                                <SelectItem key={dataSource.idxdatasource} value={dataSource.idxdatasource.toString()}>
                                  <div className="flex items-center gap-2">
                                    <span>{dataSource.displayName}</span>
                                    <Badge variant="outline" className="text-xs">
                                      {dataSource.type}
                                    </Badge>
                                  </div>
                                </SelectItem>
                              ))}
                          </SelectContent>
                        </Select>
                        <p className="text-xs text-muted-foreground">
                          {t("rag.projects.detail.dataSources.selectInfo", "Seleccione una fuente de datos existente del registro para vincularla a este proyecto RAG")}
                        </p>
                      </div>
                    </div>
                    <div className="flex justify-end gap-2 pt-4">
                      <Button
                        variant="outline"
                        onClick={() => {
                          setShowDataSourceSelector(false);
                          setSelectedDataSourceId("");
                        }}
                      >
                        {t("common.cancel", "Cancelar")}
                      </Button>
                      <Button onClick={handleAddDataSource} className="bg-primary hover:bg-primary/90">
                        {t("rag.projects.detail.dataSources.addButton", "Añadir Fuente de Datos")}
                      </Button>
                    </div>
                  </CardBody>
                </Card>
              )}

              {project.dataSources && project.dataSources.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="border-b">
                        <th className="text-left p-2">{t("rag.projects.detail.dataSources.name", "Nombre")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.dataSources.type", "Tipo")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.general.status", "Estado")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.dataSources.linkedAt", "Vinculado")}</th>
                        <th className="text-left p-2">{t("rag.projects.detail.dataSources.linkedBy", "Vinculado Por")}</th>
                        <th className="text-center p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {project.dataSources.map((dataSource) => (
                        <tr key={dataSource.id} className="border-b hover:bg-muted/50">
                          <td className="p-2">
                            <div className="flex items-center gap-2">
                              <span className="font-medium">{dataSource.displayName}</span>
                              <Button
                                variant="ghost"
                                size="sm"
                                onClick={() => {
                                  // TODO: Navegar a la fuente de datos en el registro
                                  router.push(`/data-sources/${dataSource.idxdatasource}`);
                                }}
                                title={t("rag.projects.detail.dataSources.viewDataSource", "Ver fuente de datos en registro")}
                              >
                                <Eye className="w-4 h-4" />
                              </Button>
                            </div>
                          </td>
                          <td className="p-2">
                            <Badge variant="outline">{dataSource.type}</Badge>
                          </td>
                          <td className="p-2">{getStatusBadge(dataSource.status)}</td>
                          <td className="p-2">{new Date(dataSource.linkedAt).toLocaleString()}</td>
                          <td className="p-2">{dataSource.linkedBy}</td>
                          <td className="p-2">
                            <div className="flex items-center justify-center gap-2">
                              <Button
                                variant="ghost"
                                size="sm"
                                className="text-red-400 hover:text-red-300"
                                title={t("rag.projects.detail.dataSources.unlink", "Desvincular")}
                                onClick={() => {
                                  if (confirm(t("rag.projects.detail.dataSources.confirmUnlink", "¿Está seguro de desvincular esta fuente de datos del proyecto?"))) {
                                    setProject({
                                      ...project,
                                      dataSources: project.dataSources?.filter((ds) => ds.id !== dataSource.id) || [],
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
                  {t("rag.projects.detail.dataSources.noData", "No hay fuentes de datos vinculadas")}
                  <p className="text-sm mt-2">
                    {t("rag.projects.detail.dataSources.addInfo", "Haz clic en 'Añadir Fuente de Datos' para vincular fuentes de datos existentes del registro")}
                  </p>
                </div>
              )}
              {availableDataSources.length > 0 && (
                <div className="mt-4 pt-4 border-t border-border">
                  <Button
                    variant="outline"
                    onClick={() => {
                      // TODO: Navegar al registro de fuentes de datos
                      router.push("/data-sources");
                    }}
                    className="w-full"
                  >
                    <Database className="w-4 h-4 mr-2" />
                    {t("rag.projects.detail.dataSources.viewAllDataSources", "Ver todas las fuentes de datos en el registro")}
                  </Button>
                </div>
              )}
            </CardBody>
          </Card>
          </div>
        </TabsContent>

        {/* Tab: Chunks */}
        <TabsContent value="chunks">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileText className="w-5 h-5" />
                {t("rag.projects.detail.tabs.chunks", "Chunks")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.chunks.total", "Total Chunks")}
                  </label>
                  <p className="text-2xl font-bold text-foreground">{project.chunks?.toLocaleString() || 0}</p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.chunks.embeddings", "Embeddings")}
                  </label>
                  <p className="text-2xl font-bold text-foreground">{project.embeddings?.toLocaleString() || 0}</p>
                </div>
                <div className="space-y-2">
                  <label className="text-sm font-medium">
                    {t("rag.projects.detail.chunks.searches", "Búsquedas")}
                  </label>
                  <p className="text-2xl font-bold text-foreground">{project.searches?.toLocaleString() || 0}</p>
                </div>
              </div>
            </CardBody>
          </Card>
          </div>
        </TabsContent>

        {/* Tab: Rendimiento */}
        <TabsContent value="performance">
          <div className="space-y-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <TrendingUp className="w-5 h-5" />
                {t("rag.projects.detail.tabs.performance", "Rendimiento")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {project.performance ? (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">
                      {t("rag.projects.detail.performance.avgResponseTime", "Tiempo Respuesta Promedio")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      {project.performance.avgResponseTime.toFixed(2)}s
                    </p>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">
                      {t("rag.projects.detail.performance.successRate", "Tasa de Éxito")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      {project.performance.successRate.toFixed(1)}%
                    </p>
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">
                      {t("rag.projects.detail.performance.totalCost", "Costo Total")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${project.performance.totalCost.toFixed(2)}
                    </p>
                  </div>
                </div>
              ) : (
                <div className="text-center py-8 text-muted-foreground">
                  {t("rag.projects.detail.performance.noData", "No hay datos de rendimiento disponibles")}
                </div>
              )}
            </CardBody>
          </Card>
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
}
