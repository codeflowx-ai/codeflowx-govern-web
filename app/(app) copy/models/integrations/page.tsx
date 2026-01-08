"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Cloud, Filter, RefreshCw, Search } from "lucide-react";
import { useEffect, useMemo, useState } from "react";

type CapabilityKey = "catalog" | "quality" | "pii" | "crud" | "cleanup";
type Status = "COMPLETO" | "BASICO" | "EN_PROGRESO";

interface MlopsIntegration {
  id: string;
  module: string;
  platform: string;
  status: Status;
  capabilities: Record<CapabilityKey, boolean | "pending">;
  sdk: string;
  nextSteps: string[];
}

const mockIntegrations: MlopsIntegration[] = [
  {
    id: "databricks",
    module: "codeflowx.govern.integrations.databricks",
    platform: "Databricks",
    status: "BASICO",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
    sdk: "com.databricks:databricks-sdk-java",
    nextSteps: ["Implementar evaluación de calidad", "Agregar PII detection", "Queries y limpieza"],
  },
  {
    id: "sagemaker",
    module: "codeflowx.govern.integrations.sagemaker",
    platform: "AWS SageMaker",
    status: "BASICO",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
    sdk: "software.amazon.awssdk (AWS SDK v2)",
    nextSteps: ["Implementar evaluación de calidad", "PII detection", "CRUD y limpieza"],
  },
  {
    id: "vertex",
    module: "codeflowx.govern.integrations.vertexai",
    platform: "Google Vertex AI",
    status: "BASICO",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
    sdk: "com.google.cloud (GCP SDK)",
    nextSteps: ["Implementar calidad", "PII detection", "CRUD y limpieza"],
  },
  {
    id: "azureml",
    module: "codeflowx.govern.integrations.azure-ml",
    platform: "Azure ML",
    status: "BASICO",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
    sdk: "com.azure (Azure SDK)",
    nextSteps: ["Implementar calidad", "PII detection", "CRUD y limpieza"],
  },
  {
    id: "mlflow",
    module: "codeflowx.govern.integrations.mlflow",
    platform: "MLflow",
    status: "BASICO",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
    sdk: "MLflow Client",
    nextSteps: ["Evaluación de calidad", "PII detection", "CRUD y limpieza"],
  },
  {
    id: "huggingface",
    module: "codeflowx.govern.integrations.huggingface",
    platform: "Hugging Face",
    status: "BASICO",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
    sdk: "Hugging Face Hub",
    nextSteps: ["Evaluación de calidad", "PII detection", "CRUD y limpieza"],
  },
  {
    id: "kubeflow",
    module: "codeflowx.govern.integrations.kubeflow",
    platform: "Kubeflow Pipelines",
    status: "BASICO",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
    sdk: "Kubeflow SDK",
    nextSteps: ["Quality/PII", "CRUD pipelines", "Limpieza y evidencias"],
  },
  {
    id: "seldon",
    module: "codeflowx.govern.integrations.seldon",
    platform: "Seldon Core",
    status: "BASICO",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
    sdk: "Seldon SDK",
    nextSteps: ["Calidad endpoints", "PII detection", "CRUD/Limpieza"],
  },
  {
    id: "wandb",
    module: "codeflowx.govern.integrations.wandb",
    platform: "Weights & Biases",
    status: "BASICO",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
    sdk: "W&B SDK",
    nextSteps: ["Calidad y PII en runs", "CRUD/Limpieza de runs"],
  },
];

const capabilityLabels: Record<CapabilityKey, string> = {
  catalog: "Catalogación",
  quality: "Evaluación Calidad",
  pii: "PII Detection",
  crud: "CRUD Queries",
  cleanup: "Limpieza",
};

export default function MlopsIntegrationsPage() {
  const { t } = useTranslation();
  const [search, setSearch] = useState("");
  const [platformFilter, setPlatformFilter] = useState<string>("ALL");
  const [statusFilter, setStatusFilter] = useState<string>("ALL");
  const [capabilityFilter, setCapabilityFilter] = useState<CapabilityKey | "ALL">("ALL");
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<MlopsIntegration[]>([]);

  useEffect(() => {
    // Simula carga
    setLoading(true);
    const timer = setTimeout(() => {
      setData(mockIntegrations);
      setLoading(false);
    }, 200);
    return () => clearTimeout(timer);
  }, []);

  const filtered = useMemo(() => {
    return data.filter((item) => {
      const matchesSearch =
        item.module.toLowerCase().includes(search.toLowerCase()) ||
        item.platform.toLowerCase().includes(search.toLowerCase());
      const matchesPlatform = platformFilter === "ALL" || item.platform === platformFilter;
      const matchesStatus = statusFilter === "ALL" || item.status === statusFilter;
      const matchesCapability =
        capabilityFilter === "ALL" ||
        item.capabilities[capabilityFilter] === true ||
        item.capabilities[capabilityFilter] === "pending";
      return matchesSearch && matchesPlatform && matchesStatus && matchesCapability;
    });
  }, [data, search, platformFilter, statusFilter, capabilityFilter]);

  const metrics = useMemo(() => {
    const total = data.length;
    const completos = data.filter((i) => i.status === "COMPLETO").length;
    const basicos = data.filter((i) => i.status === "BASICO").length;
    const enProgreso = data.filter((i) => i.status === "EN_PROGRESO").length;
    return { total, completos, basicos, enProgreso };
  }, [data]);

  const statusBadge = (status: Status) => {
    const map: Record<Status, string> = {
      COMPLETO: "bg-green-500/20 text-green-400 border-green-500/50",
      BASICO: "bg-blue-500/20 text-blue-400 border-blue-500/50",
      EN_PROGRESO: "bg-yellow-500/20 text-yellow-400 border-yellow-500/50",
    };
    return <Badge className={map[status]}>{status}</Badge>;
  };

  const capabilityBadge = (value: boolean | "pending") => {
    if (value === true) return <Badge className="bg-green-500/20 text-green-400 border-green-500/50 text-xs">✅</Badge>;
    if (value === "pending") return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50 text-xs">⏳</Badge>;
    return <Badge className="bg-red-500/20 text-red-400 border-red-500/50 text-xs">❌</Badge>;
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-3">
        <div className="p-2 rounded-lg bg-primary/10 text-primary">
          <Cloud className="w-6 h-6" />
        </div>
        <div>
          <h1 className="text-2xl font-bold">
            {t("governance.models.integrations.title", "Integraciones MLOps")}
          </h1>
          <p className="text-sm text-muted-foreground">
            {t(
              "governance.models.integrations.subtitle",
              "Estado de integraciones con plataformas MLOps (registry/endpoints/pipelines)"
            )}
          </p>
        </div>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">{t("common.total", "Total")}</p>
            <h2 className="text-2xl font-bold">{metrics.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">{t("common.completed", "Completos")}</p>
            <h2 className="text-2xl font-bold text-green-400">{metrics.completos}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">{t("common.basic", "Básicos")}</p>
            <h2 className="text-2xl font-bold text-blue-400">{metrics.basicos}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">{t("common.inProgress", "En progreso")}</p>
            <h2 className="text-2xl font-bold text-yellow-400">{metrics.enProgreso}</h2>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="border-2">
        <CardContent className="p-4 space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder={t("common.search", "Buscar")}
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={platformFilter} onValueChange={setPlatformFilter}>
              <SelectTrigger>
                <SelectValue placeholder="Plataforma" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">Todas</SelectItem>
                <SelectItem value="Databricks">Databricks</SelectItem>
                <SelectItem value="AWS SageMaker">AWS SageMaker</SelectItem>
                <SelectItem value="Google Vertex AI">Google Vertex AI</SelectItem>
                <SelectItem value="Azure ML">Azure ML</SelectItem>
                <SelectItem value="MLflow">MLflow</SelectItem>
                <SelectItem value="Hugging Face">Hugging Face</SelectItem>
                <SelectItem value="Kubeflow Pipelines">Kubeflow</SelectItem>
                <SelectItem value="Seldon Core">Seldon</SelectItem>
                <SelectItem value="Weights & Biases">Weights & Biases</SelectItem>
              </SelectContent>
            </Select>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger>
                <SelectValue placeholder="Estado" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">Todos</SelectItem>
                <SelectItem value="COMPLETO">Completo</SelectItem>
                <SelectItem value="BASICO">Básico</SelectItem>
                <SelectItem value="EN_PROGRESO">En progreso</SelectItem>
              </SelectContent>
            </Select>
            <Select value={capabilityFilter} onValueChange={(v) => setCapabilityFilter(v as CapabilityKey | "ALL")}>
              <SelectTrigger>
                <SelectValue placeholder="Capacidad" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">Todas</SelectItem>
                <SelectItem value="catalog">Catalogación</SelectItem>
                <SelectItem value="quality">Evaluación Calidad</SelectItem>
                <SelectItem value="pii">PII Detection</SelectItem>
                <SelectItem value="crud">CRUD Queries</SelectItem>
                <SelectItem value="cleanup">Limpieza</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={() => setData(mockIntegrations)}>
              <RefreshCw className="w-4 h-4 mr-2" />
              {t("common.reset", "Reset")}
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.integrations.table.title", "Integraciones MLOps")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filtered.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.integrations.table.noResults", "No hay integraciones disponibles")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">Módulo</th>
                    <th className="text-left p-2">Plataforma</th>
                    <th className="text-left p-2">Estado</th>
                    <th className="text-left p-2">Capacidades</th>
                    <th className="text-left p-2">SDK</th>
                    <th className="text-left p-2">Próximos pasos</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map((item) => (
                    <tr key={item.id} className="border-b hover:bg-muted/50">
                      <td className="p-2">
                        <div className="font-semibold">{item.platform}</div>
                        <div className="text-xs text-muted-foreground">{item.module}</div>
                      </td>
                      <td className="p-2">
                        <Badge variant="outline">{item.platform}</Badge>
                      </td>
                      <td className="p-2">{statusBadge(item.status)}</td>
                      <td className="p-2">
                        <div className="flex flex-wrap gap-2 text-xs">
                          {Object.entries(item.capabilities).map(([key, value]) => (
                            <span key={key} className="flex items-center gap-1">
                              <span className="text-muted-foreground">{capabilityLabels[key as CapabilityKey]}:</span>
                              {capabilityBadge(value)}
                            </span>
                          ))}
                        </div>
                      </td>
                      <td className="p-2 text-xs">{item.sdk}</td>
                      <td className="p-2 text-xs">
                        <ul className="list-disc list-inside space-y-1">
                          {item.nextSteps.map((s, idx) => (
                            <li key={idx}>{s}</li>
                          ))}
                        </ul>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}


