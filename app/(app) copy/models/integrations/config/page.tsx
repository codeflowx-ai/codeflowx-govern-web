"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Textarea } from "@/components/ui/textarea";
import { Cloud, Save, ShieldCheck } from "lucide-react";
import { useState } from "react";

type ProviderKey =
  | "databricks"
  | "sagemaker"
  | "vertex"
  | "azureml"
  | "mlflow"
  | "huggingface"
  | "kubeflow"
  | "seldon"
  | "wandb";

type CapabilityKey = "catalog" | "quality" | "pii" | "crud" | "cleanup";

interface IntegrationConfig {
  enabled: boolean;
  endpoint?: string;
  region?: string;
  project?: string;
  workspace?: string;
  token?: string;
  clientId?: string;
  clientSecret?: string;
  extra?: string;
  capabilities: Record<CapabilityKey, boolean | "pending">;
}

const defaultConfig: Record<ProviderKey, IntegrationConfig> = {
  databricks: {
    enabled: true,
    endpoint: "https://dbc-example.cloud.databricks.com",
    token: "",
    workspace: "main",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
  },
  sagemaker: {
    enabled: true,
    region: "us-east-1",
    token: "",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
  },
  vertex: {
    enabled: true,
    project: "my-gcp-project",
    region: "us-central1",
    token: "",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
  },
  azureml: {
    enabled: true,
    workspace: "aml-workspace",
    token: "",
    clientId: "",
    clientSecret: "",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
  },
  mlflow: {
    enabled: true,
    endpoint: "https://mlflow.example.com",
    token: "",
    capabilities: { catalog: true, quality: "pending", pii: "pending", crud: "pending", cleanup: "pending" },
  },
  huggingface: {
    enabled: true,
    token: "",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
  },
  kubeflow: {
    enabled: false,
    endpoint: "https://kubeflow.example.com",
    token: "",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
  },
  seldon: {
    enabled: false,
    endpoint: "https://seldon.example.com",
    token: "",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
  },
  wandb: {
    enabled: false,
    token: "",
    capabilities: { catalog: true, quality: false, pii: false, crud: false, cleanup: false },
  },
};

const capabilityLabels: Record<CapabilityKey, string> = {
  catalog: "Catalogación",
  quality: "Evaluación Calidad",
  pii: "PII Detection",
  crud: "CRUD Queries",
  cleanup: "Limpieza",
};

export default function MlopsIntegrationsConfigPage() {
  const { t } = useTranslation();
  const [configs, setConfigs] = useState<Record<ProviderKey, IntegrationConfig>>(defaultConfig);
  const [message, setMessage] = useState<string>("");

  const handleSave = () => {
    // TODO: Persistir contra API real
    setMessage(t("governance.models.integrations.config.saved", "Configuración guardada (mock)."));
    setTimeout(() => setMessage(""), 2500);
  };

  const renderCommonFields = (key: ProviderKey, cfg: IntegrationConfig) => (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <Label>{t("common.enabled", "Habilitado")}</Label>
        <Switch
          checked={cfg.enabled}
          onChange={(checked) =>
            setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], enabled: checked } }))
          }
        />
      </div>
      {"endpoint" in cfg || key === "sagemaker" || key === "vertex" || key === "azureml" ? (
        <div className="space-y-1">
          <Label>{t("governance.models.integrations.config.endpoint", "Endpoint / Host")}</Label>
          <Input
            value={cfg.endpoint || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], endpoint: e.target.value } }))
            }
            placeholder="https://..."
          />
        </div>
      ) : null}
      {"region" in cfg ? (
        <div className="space-y-1">
          <Label>{t("governance.models.integrations.config.region", "Región")}</Label>
          <Input
            value={cfg.region || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], region: e.target.value } }))
            }
            placeholder="us-east-1 / us-central1"
          />
        </div>
      ) : null}
      {"project" in cfg ? (
        <div className="space-y-1">
          <Label>{t("governance.models.integrations.config.project", "Proyecto")}</Label>
          <Input
            value={cfg.project || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], project: e.target.value } }))
            }
            placeholder="gcp-project-id"
          />
        </div>
      ) : null}
      {"workspace" in cfg ? (
        <div className="space-y-1">
          <Label>{t("governance.models.integrations.config.workspace", "Workspace")}</Label>
          <Input
            value={cfg.workspace || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], workspace: e.target.value } }))
            }
            placeholder="aml-workspace / databricks-workspace"
          />
        </div>
      ) : null}
      <div className="space-y-1">
        <Label>{t("governance.models.integrations.config.token", "Token / API Key")}</Label>
        <Input
          type="password"
          value={cfg.token || ""}
          onChange={(e) =>
            setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], token: e.target.value } }))
          }
          placeholder="•••••••"
        />
      </div>
      {"clientId" in cfg ? (
        <div className="space-y-1">
          <Label>Client ID</Label>
          <Input
            value={cfg.clientId || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], clientId: e.target.value } }))
            }
            placeholder="client-id"
          />
        </div>
      ) : null}
      {"clientSecret" in cfg ? (
        <div className="space-y-1">
          <Label>Client Secret</Label>
          <Input
            type="password"
            value={cfg.clientSecret || ""}
            onChange={(e) =>
              setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], clientSecret: e.target.value } }))
            }
            placeholder="•••••••"
          />
        </div>
      ) : null}
      <div className="space-y-1">
        <Label>{t("governance.models.integrations.config.extra", "Notas / Config extra")}</Label>
        <Textarea
          value={cfg.extra || ""}
          onChange={(e) =>
            setConfigs((prev) => ({ ...prev, [key]: { ...prev[key], extra: e.target.value } }))
          }
          placeholder={t("governance.models.integrations.config.extraPlaceholder", "Ej: ruta a credenciales, bucket, etc.")}
          rows={3}
        />
      </div>
      <div className="space-y-2">
        <Label>{t("governance.models.integrations.config.capabilities", "Capacidades")}</Label>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-2 text-sm">
          {(Object.keys(cfg.capabilities) as CapabilityKey[]).map((cap) => (
            <div key={cap} className="flex items-center justify-between rounded-md border px-3 py-2">
              <span>{capabilityLabels[cap]}</span>
              <Switch
                checked={cfg.capabilities[cap] === true}
                onChange={(checked) =>
                  setConfigs((prev) => ({
                    ...prev,
                    [key]: {
                      ...prev[key],
                      capabilities: { ...prev[key].capabilities, [cap]: checked },
                    },
                  }))
                }
              />
            </div>
          ))}
        </div>
      </div>
    </div>
  );

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-3">
        <div className="p-2 rounded-lg bg-primary/10 text-primary">
          <Cloud className="w-6 h-6" />
        </div>
        <div>
          <h1 className="text-2xl font-bold">
            {t("governance.models.integrations.config.title", "Configuración de Integraciones MLOps")}
          </h1>
          <p className="text-sm text-muted-foreground">
            {t("governance.models.integrations.config.subtitle", "Gestiona credenciales y capacidades por plataforma")}
          </p>
        </div>
      </div>

      <Tabs defaultValue="databricks" className="space-y-4">
        <TabsList className="w-full">
          <TabsTrigger value="databricks" className="flex-1">Databricks</TabsTrigger>
          <TabsTrigger value="sagemaker" className="flex-1">SageMaker</TabsTrigger>
          <TabsTrigger value="vertex" className="flex-1">Vertex AI</TabsTrigger>
          <TabsTrigger value="azureml" className="flex-1">Azure ML</TabsTrigger>
          <TabsTrigger value="mlflow" className="flex-1">MLflow</TabsTrigger>
          <TabsTrigger value="huggingface" className="flex-1">Hugging Face</TabsTrigger>
          <TabsTrigger value="kubeflow" className="flex-1">Kubeflow</TabsTrigger>
          <TabsTrigger value="seldon" className="flex-1">Seldon</TabsTrigger>
          <TabsTrigger value="wandb" className="flex-1">W&B</TabsTrigger>
        </TabsList>

        {(Object.keys(configs) as ProviderKey[]).map((key) => (
          <TabsContent key={key} value={key}>
            <Card className="border-2">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <ShieldCheck className="w-5 h-5 text-primary" />
                  {configs[key].enabled ? t("common.enabled", "Habilitado") : t("common.disabled", "Deshabilitado")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                {renderCommonFields(key, configs[key])}
              </CardContent>
            </Card>
          </TabsContent>
        ))}
      </Tabs>

      <div className="flex justify-end">
        <Button onClick={handleSave}>
          <Save className="w-4 h-4 mr-2" />
          {t("common.save", "Guardar")}
        </Button>
      </div>

      {message && (
        <div className="text-sm text-green-500">
          {message}
        </div>
      )}
    </div>
  );
}
