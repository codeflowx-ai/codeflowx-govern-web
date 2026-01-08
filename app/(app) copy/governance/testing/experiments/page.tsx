"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { FlaskConical, Plus, Search, Eye, Archive } from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface Experiment {
  id: number;
  name: string;
  description: string;
  entityType: "MODEL" | "PROMPT" | "AGENT" | "RAG" | "DATASET";
  entityId: number;
  entityName?: string;
  status: "ACTIVE" | "ARCHIVED" | "DELETED";
  runCount: number;
  createdAt: string;
  updatedAt: string;
  tags?: Record<string, string>;
}

export default function ExperimentsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [experiments, setExperiments] = useState<Experiment[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [entityTypeFilter, setEntityTypeFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newExperiment, setNewExperiment] = useState({
    name: "",
    description: "",
    entityType: "MODEL" as "MODEL" | "PROMPT" | "AGENT" | "RAG" | "DATASET",
    entityId: "",
  });
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    archived: 0,
    byEntityType: {} as Record<string, number>,
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // Mock data
      const mockData: Experiment[] = [
        {
          id: 1,
          name: "Bias Analysis Experiments for Model #1",
          description: "Experimentos de análisis de sesgo para GPT-4 Fine-tuned Classification",
          entityType: "MODEL",
          entityId: 1,
          entityName: "GPT-4 Fine-tuned Classification",
          status: "ACTIVE",
          runCount: 5,
          createdAt: "2024-01-15T10:30:00",
          updatedAt: "2024-01-20T14:20:00",
          tags: { version: "1.0.0", team: "data-science" },
        },
        {
          id: 2,
          name: "Explainability Experiments for Model #2",
          description: "Análisis de explicabilidad para Claude Vision OCR",
          entityType: "MODEL",
          entityId: 2,
          entityName: "Claude Vision OCR",
          status: "ACTIVE",
          runCount: 3,
          createdAt: "2024-01-14T09:15:00",
          updatedAt: "2024-01-18T11:45:00",
          tags: { method: "SHAP", version: "2.1.0" },
        },
        {
          id: 3,
          name: "Performance Tests for Model #3",
          description: "Pruebas de rendimiento para Resume Screening Model",
          entityType: "MODEL",
          entityId: 3,
          entityName: "Resume Screening Model",
          status: "ACTIVE",
          runCount: 8,
          createdAt: "2024-01-13T08:00:00",
          updatedAt: "2024-01-19T16:30:00",
          tags: { environment: "production", version: "1.5.0" },
        },
        {
          id: 4,
          name: "Prompt Testing for Customer Service",
          description: "Testing de prompts para servicio al cliente",
          entityType: "PROMPT",
          entityId: 10,
          entityName: "Customer Service Prompt v2",
          status: "ACTIVE",
          runCount: 12,
          createdAt: "2024-01-12T10:00:00",
          updatedAt: "2024-01-21T09:20:00",
          tags: { category: "customer-service", version: "2.0.0" },
        },
        {
          id: 5,
          name: "RAG Accuracy Tests",
          description: "Pruebas de precisión para sistema RAG de documentación técnica",
          entityType: "RAG",
          entityId: 5,
          entityName: "Technical Documentation RAG",
          status: "ACTIVE",
          runCount: 6,
          createdAt: "2024-01-11T14:30:00",
          updatedAt: "2024-01-17T10:15:00",
          tags: { domain: "technical", version: "1.3.0" },
        },
        {
          id: 6,
          name: "Agent Behavior Tests",
          description: "Pruebas de comportamiento para agente de soporte",
          entityType: "AGENT",
          entityId: 7,
          entityName: "Support Agent v1",
          status: "ARCHIVED",
          runCount: 4,
          createdAt: "2024-01-10T11:00:00",
          updatedAt: "2024-01-16T15:45:00",
          tags: { archived: "true" },
        },
        {
          id: 7,
          name: "Dataset Quality Checks",
          description: "Validación de calidad para dataset de entrenamiento",
          entityType: "DATASET",
          entityId: 3,
          entityName: "Training Dataset v3",
          status: "ACTIVE",
          runCount: 2,
          createdAt: "2024-01-09T09:00:00",
          updatedAt: "2024-01-15T12:00:00",
          tags: { datasetType: "training", version: "3.0.0" },
        },
      ];

      setExperiments(mockData);

      // Calcular estadísticas
      const total = mockData.length;
      const active = mockData.filter((e) => e.status === "ACTIVE").length;
      const archived = mockData.filter((e) => e.status === "ARCHIVED").length;
      const byEntityType: Record<string, number> = {};
      mockData.forEach((e) => {
        byEntityType[e.entityType] = (byEntityType[e.entityType] || 0) + 1;
      });

      setStats({
        total,
        active,
        archived,
        byEntityType,
      });
    } catch (error) {
      console.error("Error loading experiments:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredExperiments = experiments.filter((exp) => {
    const matchesSearch =
      exp.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      exp.description?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      exp.entityName?.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesEntityType = entityTypeFilter === "ALL" || exp.entityType === entityTypeFilter;
    const matchesStatus = statusFilter === "ALL" || exp.status === statusFilter;
    return matchesSearch && matchesEntityType && matchesStatus;
  });

  const getEntityTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      MODEL: "bg-blue-500/20 text-blue-400 border-blue-500/50",
      PROMPT: "bg-purple-500/20 text-purple-400 border-purple-500/50",
      AGENT: "bg-green-500/20 text-green-400 border-green-500/50",
      RAG: "bg-orange-500/20 text-orange-400 border-orange-500/50",
      DATASET: "bg-pink-500/20 text-pink-400 border-pink-500/50",
    };
    return (
      <Badge className={colors[type] || "bg-gray-500/20 text-gray-400 border-gray-500/50"}>
        {type}
      </Badge>
    );
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">ACTIVE</Badge>;
      case "ARCHIVED":
        return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">ARCHIVED</Badge>;
      case "DELETED":
        return <Badge className="bg-red-500/20 text-red-400 border-red-500/50">DELETED</Badge>;
      default:
        return <Badge>{status}</Badge>;
    }
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <FlaskConical className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.experiments.title", "Experimentos")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t(
              "governance.testing.experiments.subtitle",
              "Sistema de experimentos estilo MLflow para testing y validación"
            )}
          </p>
        </div>
        <Button
          onClick={() => setShowCreateModal(true)}
          className="bg-primary hover:bg-primary/90"
        >
          <Plus className="h-4 w-4 mr-2" />
          {t("governance.testing.experiments.newExperiment", "Nuevo Experimento")}
        </Button>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.testing.experiments.metrics.total", "Total")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.testing.experiments.metrics.active", "Activos")}
            </p>
            <h2 className="text-2xl font-bold text-green-400">{stats.active}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.testing.experiments.metrics.archived", "Archivados")}
            </p>
            <h2 className="text-2xl font-bold text-yellow-400">{stats.archived}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.testing.experiments.metrics.models", "Modelos")}
            </p>
            <h2 className="text-2xl font-bold">{stats.byEntityType.MODEL || 0}</h2>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.experiments.filters.title", "Filtros")}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.testing.experiments.filters.search", "Búsqueda")}
              </label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
                <Input
                  placeholder={t("governance.testing.experiments.filters.searchPlaceholder", "Buscar...")}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.testing.experiments.filters.entityType", "Tipo de Entidad")}
              </label>
              <Select value={entityTypeFilter} onValueChange={setEntityTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.testing.experiments.filters.all", "TODOS")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.testing.experiments.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="MODEL">MODEL</SelectItem>
                  <SelectItem value="PROMPT">PROMPT</SelectItem>
                  <SelectItem value="AGENT">AGENT</SelectItem>
                  <SelectItem value="RAG">RAG</SelectItem>
                  <SelectItem value="DATASET">DATASET</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-medium">
                {t("governance.testing.experiments.filters.status", "Estado")}
              </label>
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.testing.experiments.filters.all", "TODOS")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">{t("governance.testing.experiments.filters.all", "TODOS")}</SelectItem>
                  <SelectItem value="ACTIVE">ACTIVE</SelectItem>
                  <SelectItem value="ARCHIVED">ARCHIVED</SelectItem>
                  <SelectItem value="DELETED">DELETED</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.testing.experiments.table.title", "Listado de Experimentos")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredExperiments.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.testing.experiments.table.noResults", "No hay experimentos disponibles")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.testing.experiments.table.name", "Nombre")}</th>
                    <th className="text-left p-2">{t("governance.testing.experiments.table.entityType", "Tipo")}</th>
                    <th className="text-left p-2">{t("governance.testing.experiments.table.entity", "Entidad")}</th>
                    <th className="text-left p-2">{t("governance.testing.experiments.table.runs", "Runs")}</th>
                    <th className="text-left p-2">{t("governance.testing.experiments.table.status", "Estado")}</th>
                    <th className="text-left p-2">{t("governance.testing.experiments.table.updated", "Actualizado")}</th>
                    <th className="text-center p-2">{t("governance.testing.experiments.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredExperiments.map((exp) => (
                    <tr key={exp.id} className="border-b hover:bg-muted/50">
                      <td className="p-2">
                        <div>
                          <div className="font-medium">{exp.name}</div>
                          {exp.description && (
                            <div className="text-xs text-muted-foreground mt-1">{exp.description}</div>
                          )}
                        </div>
                      </td>
                      <td className="p-2">{getEntityTypeBadge(exp.entityType)}</td>
                      <td className="p-2">
                        {exp.entityName ? (
                          <span className="text-sm">{exp.entityName}</span>
                        ) : (
                          <span className="text-xs text-muted-foreground">ID: {exp.entityId}</span>
                        )}
                      </td>
                      <td className="p-2">
                        <Badge variant="outline">{exp.runCount}</Badge>
                      </td>
                      <td className="p-2">{getStatusBadge(exp.status)}</td>
                      <td className="p-2 text-xs text-muted-foreground">
                        {new Date(exp.updatedAt).toLocaleDateString()}
                      </td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            title={t("common.view", "Ver")}
                            onClick={() => router.push(`/governance/testing/experiments/${exp.id}`)}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          {exp.status === "ACTIVE" && (
                            <Button
                              variant="ghost"
                              size="sm"
                              className="text-yellow-400 hover:text-yellow-300"
                              title={t("governance.testing.experiments.archive", "Archivar")}
                              onClick={() => {
                                // TODO: Implementar archivado
                                console.log("Archive experiment:", exp.id);
                              }}
                            >
                              <Archive className="w-4 h-4" />
                            </Button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Modal de Crear Experimento */}
      <SimpleModal
        isOpen={showCreateModal}
        onClose={() => {
          setShowCreateModal(false);
          setNewExperiment({
            name: "",
            description: "",
            entityType: "MODEL",
            entityId: "",
          });
        }}
        title={t("governance.testing.experiments.createModal.title", "Crear Nuevo Experimento")}
        maxWidth="max-w-2xl"
      >
        <div className="space-y-4">
          <div className="space-y-2">
            <label className="text-sm font-medium text-foreground">
              {t("governance.testing.experiments.createModal.name", "Nombre del Experimento")} *
            </label>
            <Input
              placeholder={t("governance.testing.experiments.createModal.namePlaceholder", "Ej: Bias Analysis Experiments for Model #1")}
              value={newExperiment.name}
              onChange={(e) => setNewExperiment({ ...newExperiment, name: e.target.value })}
              required
            />
          </div>

          <div className="space-y-2">
            <label className="text-sm font-medium text-foreground">
              {t("governance.testing.experiments.createModal.description", "Descripción")}
            </label>
            <Textarea
              placeholder={t("governance.testing.experiments.createModal.descriptionPlaceholder", "Descripción del experimento...")}
              value={newExperiment.description}
              onChange={(e) => setNewExperiment({ ...newExperiment, description: e.target.value })}
              rows={3}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <label className="text-sm font-medium text-foreground">
                {t("governance.testing.experiments.createModal.entityType", "Tipo de Entidad")} *
              </label>
              <Select
                value={newExperiment.entityType}
                onValueChange={(value) => setNewExperiment({ ...newExperiment, entityType: value as any })}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="MODEL">MODEL</SelectItem>
                  <SelectItem value="PROMPT">PROMPT</SelectItem>
                  <SelectItem value="AGENT">AGENT</SelectItem>
                  <SelectItem value="RAG">RAG</SelectItem>
                  <SelectItem value="DATASET">DATASET</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-foreground">
                {t("governance.testing.experiments.createModal.entityId", "ID de la Entidad")}
              </label>
              <Input
                type="number"
                placeholder={t("governance.testing.experiments.createModal.entityIdPlaceholder", "Opcional")}
                value={newExperiment.entityId}
                onChange={(e) => setNewExperiment({ ...newExperiment, entityId: e.target.value })}
              />
            </div>
          </div>

          <div className="flex justify-end gap-2 pt-4 border-t">
            <Button
              variant="outline"
              onClick={() => {
                setShowCreateModal(false);
                setNewExperiment({
                  name: "",
                  description: "",
                  entityType: "MODEL",
                  entityId: "",
                });
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={() => {
                if (!newExperiment.name.trim()) {
                  alert(t("governance.testing.experiments.createModal.nameRequired", "El nombre es requerido"));
                  return;
                }

                // Mock: Crear experimento
                const newExp: Experiment = {
                  id: experiments.length + 1,
                  name: newExperiment.name,
                  description: newExperiment.description,
                  entityType: newExperiment.entityType,
                  entityId: newExperiment.entityId ? parseInt(newExperiment.entityId) : undefined as any,
                  status: "ACTIVE",
                  runCount: 0,
                  createdAt: new Date().toISOString(),
                  updatedAt: new Date().toISOString(),
                };

                setExperiments([...experiments, newExp]);
                setStats({
                  ...stats,
                  total: stats.total + 1,
                  active: stats.active + 1,
                  byEntityType: {
                    ...stats.byEntityType,
                    [newExperiment.entityType]: (stats.byEntityType[newExperiment.entityType] || 0) + 1,
                  },
                });

                setShowCreateModal(false);
                setNewExperiment({
                  name: "",
                  description: "",
                  entityType: "MODEL",
                  entityId: "",
                });

                alert(t("governance.testing.experiments.createModal.success", "Experimento creado exitosamente"));
              }}
              className="bg-primary hover:bg-primary/90"
            >
              <Plus className="h-4 w-4 mr-2" />
              {t("governance.testing.experiments.createModal.create", "Crear Experimento")}
            </Button>
          </div>
        </div>
      </SimpleModal>
    </div>
  );
}


