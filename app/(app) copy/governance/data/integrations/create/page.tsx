"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Switch } from "@/components/ui/switch";
import { ArrowLeft, Save, Loader2, PlusCircle } from "lucide-react";
import { AuthenticationType, DatabaseType, DatabaseIntegration } from "../../types/integrations";

export default function CreateIntegrationPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [saving, setSaving] = useState(false);

  // Función auxiliar para obtener auth por defecto (debe estar antes del useState)
  const getDefaultAuthForProvider = (provider: DatabaseType): AuthenticationType => {
    if (["HUGGINGFACE", "KAGGLE", "API"].includes(provider)) return "API_KEY";
    if (["S3", "REDSHIFT"].includes(provider)) return "AWS_IAM";
    if (["AZURE_BLOB", "ADLS_GEN2", "AZURE_ML"].includes(provider)) return "AZURE_AD";
    if (["GCS", "BIGQUERY", "VERTEX_AI"].includes(provider)) return "GCP_SERVICE_ACCOUNT";
    if (["DATABRICKS", "DATABRICKS_ML", "MLFLOW"].includes(provider)) return "API_KEY";
    if (["SAGEMAKER"].includes(provider)) return "AWS_IAM";
    return "USERNAME_PASSWORD";
  };

  const [formData, setFormData] = useState<Partial<DatabaseIntegration>>({
    dtgname: "",
    dtgdescription: "",
    dtgdatabasetype: "POSTGRESQL",
    dtghost: "",
    dtgport: 5432,
    dtgdatabase: "",
    dtgauthenticationtype: getDefaultAuthForProvider("POSTGRESQL"),
    dtgusername: "",
    dtgpassword: "",
    dtgsslrequired: false,
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 1,
  });

  type IntegrationCategory =
    | "RELATIONAL"
    | "NOSQL"
    | "DATA_LAKE"
    | "DATA_WAREHOUSE"
    | "ML_PLATFORM"
    | "EXTERNAL";

  const providers: Record<IntegrationCategory, { value: DatabaseType; label: string }[]> = {
    RELATIONAL: [
      { value: "POSTGRESQL", label: "PostgreSQL" },
      { value: "MYSQL", label: "MySQL" },
      { value: "MARIADB", label: "MariaDB" },
      { value: "SQLSERVER", label: "SQL Server" },
      { value: "ORACLE", label: "Oracle" },
      { value: "DB2", label: "DB2" },
      { value: "SQLITE", label: "SQLite" },
    ],
    NOSQL: [
      { value: "MONGODB", label: "MongoDB" },
      { value: "CASSANDRA", label: "Cassandra" },
      { value: "REDIS", label: "Redis" },
      { value: "ELASTICSEARCH", label: "Elasticsearch" },
      { value: "NEO4J", label: "Neo4j" },
      { value: "DYNAMODB", label: "DynamoDB" },
    ],
    DATA_LAKE: [
      { value: "S3", label: "Amazon S3" },
      { value: "AZURE_BLOB", label: "Azure Blob Storage" },
      { value: "GCS", label: "Google Cloud Storage" },
      { value: "ADLS_GEN2", label: "Azure Data Lake Gen2" },
      { value: "HDFS", label: "HDFS" },
      { value: "MINIO", label: "MinIO" },
      { value: "IBM_COS", label: "IBM COS" },
      { value: "OCI_OBJECT_STORAGE", label: "OCI Object Storage" },
    ],
    DATA_WAREHOUSE: [
      { value: "SNOWFLAKE", label: "Snowflake" },
      { value: "DATABRICKS", label: "Databricks (SQL)" },
      { value: "BIGQUERY", label: "BigQuery" },
      { value: "REDSHIFT", label: "Redshift" },
    ],
    ML_PLATFORM: [
      { value: "SAGEMAKER", label: "SageMaker" },
      { value: "VERTEX_AI", label: "Vertex AI" },
      { value: "AZURE_ML", label: "Azure ML" },
      { value: "MLFLOW", label: "MLflow" },
      { value: "DATABRICKS_ML", label: "Databricks ML/Unity Catalog" },
      { value: "KUBEFLOW", label: "Kubeflow" },
      { value: "SELDON", label: "Seldon" },
      { value: "WAND_B", label: "Weights & Biases" },
    ],
    EXTERNAL: [
      { value: "HUGGINGFACE", label: "HuggingFace" },
      { value: "KAGGLE", label: "Kaggle" },
      { value: "API", label: "API REST" },
    ],
  };

  const providerToCategory: Record<DatabaseType, IntegrationCategory> = Object.entries(providers).reduce(
    (acc, [category, list]) => {
      list.forEach((p) => {
        acc[p.value] = category as IntegrationCategory;
      });
      return acc;
    },
    {} as Record<DatabaseType, IntegrationCategory>
  );


  const currentType = formData.dtgdatabasetype || "POSTGRESQL";
  const currentCategory = providerToCategory[currentType] || "RELATIONAL";

  // Actualizar tipo de auth cuando cambia el proveedor
  const handleProviderChange = (provider: DatabaseType) => {
    const defaultAuth = getDefaultAuthForProvider(provider);
    setFormData({
      ...formData,
      dtgdatabasetype: provider,
      dtgauthenticationtype: defaultAuth,
    });
  };

  const isRelational =
    ["POSTGRESQL", "MYSQL", "MARIADB", "SQLSERVER", "ORACLE", "DB2", "SQLITE"].includes(currentType);
  const isNoSQL = ["MONGODB", "CASSANDRA", "REDIS", "ELASTICSEARCH", "NEO4J", "DYNAMODB"].includes(currentType);
  const isDataLake = ["S3", "AZURE_BLOB", "GCS", "ADLS_GEN2", "HDFS", "MINIO", "IBM_COS", "OCI_OBJECT_STORAGE"].includes(currentType);
  const isWarehouse = ["SNOWFLAKE", "DATABRICKS", "BIGQUERY", "REDSHIFT"].includes(currentType);
  const isMLPlatform = ["SAGEMAKER", "VERTEX_AI", "AZURE_ML", "MLFLOW", "DATABRICKS_ML", "KUBEFLOW", "SELDON", "WAND_B"].includes(currentType);
  const isExternal = ["HUGGINGFACE", "KAGGLE", "API"].includes(currentType);

  // Determinar si mostrar selector de tipo de autenticación
  const showAuthTypeSelector = !isExternal; // Externos solo usan API_KEY

  // Determinar qué campos de autenticación mostrar
  const authType = formData.dtgauthenticationtype || getDefaultAuthForProvider(currentType);
  const showUsernamePassword = authType === "USERNAME_PASSWORD";
  const showApiKey = authType === "API_KEY";
  const showAwsIam = authType === "AWS_IAM";
  const showAzureAd = authType === "AZURE_AD";
  const showGcpServiceAccount = authType === "GCP_SERVICE_ACCOUNT";
  const showOAuth2 = authType === "OAUTH2";

  const handleCategoryChange = (cat: IntegrationCategory) => {
    const firstProvider = providers[cat][0]?.value || "POSTGRESQL";
    setFormData((prev) => ({
      ...prev,
      dtgdatabasetype: firstProvider,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setSaving(true);
      const response = await fetch("/api/v1/governance/data/integrations", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const data = await response.json();
        const newId = data.idxintegration || data.id || null;
        router.push(
          newId
            ? `/governance/data/integrations/${newId}/explore`
            : "/governance/data/integrations"
        );
      } else {
        alert("Error al crear la integración");
      }
    } catch (error) {
      console.error("Error creando integración:", error);
      alert("Error al crear la integración");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="w-full py-6 space-y-6 px-6">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">{t("governance.data.integrations.create")}</h1>
        <p className="text-muted-foreground mt-2">
          {t("governance.data.integrations.createDescription")}
        </p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button variant="ghost" onClick={() => router.push("/governance/data/integrations")}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button type="submit" form="integration-form" disabled={saving}>
            {saving ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Guardando...
              </>
            ) : (
              <>
                <Save className="mr-2 h-4 w-4" />
                Guardar
              </>
            )}
          </Button>
        </div>
      </div>

      <form id="integration-form" onSubmit={handleSubmit}>
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <PlusCircle className="h-5 w-5" />
              <span>{t("governance.data.integrations.create")}</span>
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.name")}</Label>
                <Input
                  value={formData.dtgname}
                  onChange={(e) => setFormData({ ...formData, dtgname: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.category", "Tipo de integración")}</Label>
                <select
                  className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
                  value={currentCategory}
                  onChange={(e) => handleCategoryChange(e.target.value as IntegrationCategory)}
                >
                  <option value="RELATIONAL">Base de datos relacional</option>
                  <option value="NOSQL">Base de datos NoSQL</option>
                  <option value="DATA_LAKE">Data Lake / Objetos</option>
                  <option value="DATA_WAREHOUSE">Data Warehouse / Analytics</option>
                  <option value="ML_PLATFORM">Plataforma ML</option>
                  <option value="EXTERNAL">Proveedor externo / Marketplace</option>
                </select>
              </div>
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.databaseType")}</Label>
                <select
                  className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
                  value={formData.dtgdatabasetype}
                  onChange={(e) => handleProviderChange(e.target.value as DatabaseType)}
                >
                  {providers[currentCategory].map((p) => (
                    <option key={p.value} value={p.value}>
                      {p.label}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="space-y-2">
              <Label>{t("governance.data.integrations.form.description")}</Label>
              <Textarea
                value={formData.dtgdescription}
                onChange={(e) => setFormData({ ...formData, dtgdescription: e.target.value })}
              />
            </div>

            {(isRelational || isNoSQL || isWarehouse) && (
              <div className="grid grid-cols-3 gap-4">
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.host")}</Label>
                  <Input
                    value={formData.dtghost}
                    onChange={(e) => setFormData({ ...formData, dtghost: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.port")}</Label>
                  <Input
                    type="number"
                    value={formData.dtgport}
                    onChange={(e) =>
                      setFormData({ ...formData, dtgport: parseInt(e.target.value) })
                    }
                  />
                </div>
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.database")}</Label>
                  <Input
                    value={formData.dtgdatabase}
                    onChange={(e) => setFormData({ ...formData, dtgdatabase: e.target.value })}
                  />
                </div>
              </div>
            )}

            {isDataLake && (
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.host")}</Label>
                  <Input
                    value={formData.dtghost}
                    onChange={(e) => setFormData({ ...formData, dtghost: e.target.value })}
                    placeholder="s3.amazonaws.com / account.blob.core.windows.net"
                  />
                </div>
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.database")}</Label>
                  <Input
                    value={formData.dtgdatabase}
                    onChange={(e) => setFormData({ ...formData, dtgdatabase: e.target.value })}
                    placeholder="bucket / contenedor"
                  />
                </div>
              </div>
            )}

            {isMLPlatform && (
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.host")}</Label>
                <Input
                  value={formData.dtghost}
                  onChange={(e) => setFormData({ ...formData, dtghost: e.target.value })}
                  placeholder="mlflow.example.com / *.databricks.com"
                />
              </div>
            )}

            {/* Selector de tipo de autenticación (solo si no es externo) */}
            {showAuthTypeSelector && (
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.authenticationType")}</Label>
                <select
                  className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
                  value={authType}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      dtgauthenticationtype: e.target.value as AuthenticationType,
                    })
                  }
                >
                  {isRelational || isNoSQL || currentType === "SNOWFLAKE" ? (
                    <>
                      <option value="USERNAME_PASSWORD">
                        {t("governance.data.integrations.auth.usernamePassword")}
                      </option>
                      <option value="NONE">{t("governance.data.integrations.auth.none")}</option>
                    </>
                  ) : isDataLake && currentType === "S3" ? (
                    <option value="AWS_IAM">{t("governance.data.integrations.auth.awsIam")}</option>
                  ) : isDataLake && (currentType === "AZURE_BLOB" || currentType === "ADLS_GEN2") ? (
                    <option value="AZURE_AD">{t("governance.data.integrations.auth.azureAd")}</option>
                  ) : isDataLake && currentType === "GCS" ? (
                    <>
                      <option value="GCP_SERVICE_ACCOUNT">
                        {t("governance.data.integrations.auth.gcpServiceAccount")}
                      </option>
                      <option value="API_KEY">{t("governance.data.integrations.auth.apiKey")}</option>
                    </>
                  ) : isWarehouse && currentType === "DATABRICKS" ? (
                    <>
                      <option value="API_KEY">{t("governance.data.integrations.auth.apiKey")}</option>
                      <option value="USERNAME_PASSWORD">
                        {t("governance.data.integrations.auth.usernamePassword")}
                      </option>
                    </>
                  ) : isWarehouse && currentType === "BIGQUERY" ? (
                    <option value="GCP_SERVICE_ACCOUNT">
                      {t("governance.data.integrations.auth.gcpServiceAccount")}
                    </option>
                  ) : isMLPlatform && currentType === "SAGEMAKER" ? (
                    <option value="AWS_IAM">{t("governance.data.integrations.auth.awsIam")}</option>
                  ) : isMLPlatform && currentType === "VERTEX_AI" ? (
                    <option value="GCP_SERVICE_ACCOUNT">
                      {t("governance.data.integrations.auth.gcpServiceAccount")}
                    </option>
                  ) : isMLPlatform && currentType === "AZURE_ML" ? (
                    <option value="AZURE_AD">{t("governance.data.integrations.auth.azureAd")}</option>
                  ) : isMLPlatform && (currentType === "MLFLOW" || currentType === "DATABRICKS_ML") ? (
                    <>
                      <option value="API_KEY">{t("governance.data.integrations.auth.apiKey")}</option>
                      <option value="USERNAME_PASSWORD">
                        {t("governance.data.integrations.auth.usernamePassword")}
                      </option>
                    </>
                  ) : null}
                </select>
              </div>
            )}

            {/* Campos de autenticación según el tipo */}
            {showUsernamePassword && (
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.username")}</Label>
                  <Input
                    value={formData.dtgusername}
                    onChange={(e) => setFormData({ ...formData, dtgusername: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <Label>{t("governance.data.integrations.form.password")}</Label>
                  <Input
                    type="password"
                    value={formData.dtgpassword}
                    onChange={(e) => setFormData({ ...formData, dtgpassword: e.target.value })}
                  />
                </div>
              </div>
            )}

            {showApiKey && (
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.apiKey", "API Key / Token")}</Label>
                <Input
                  type="password"
                  value={formData.dtgpassword}
                  onChange={(e) => setFormData({ ...formData, dtgpassword: e.target.value })}
                  placeholder="Token / PAT / API Key"
                />
              </div>
            )}

            {showAwsIam && (
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Access Key ID</Label>
                  <Input
                    value={formData.dtgusername}
                    onChange={(e) => setFormData({ ...formData, dtgusername: e.target.value })}
                    placeholder="AKIA..."
                  />
                </div>
                <div className="space-y-2">
                  <Label>Secret Access Key</Label>
                  <Input
                    type="password"
                    value={formData.dtgpassword}
                    onChange={(e) => setFormData({ ...formData, dtgpassword: e.target.value })}
                    placeholder="Secret key"
                  />
                </div>
              </div>
            )}

            {showAzureAd && (
              <div className="grid grid-cols-3 gap-4">
                <div className="space-y-2">
                  <Label>Tenant ID</Label>
                  <Input
                    value={formData.dtgtenantid}
                    onChange={(e) => setFormData({ ...formData, dtgtenantid: e.target.value })}
                    placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
                  />
                </div>
                <div className="space-y-2">
                  <Label>Client ID</Label>
                  <Input
                    value={formData.dtgclientid}
                    onChange={(e) => setFormData({ ...formData, dtgclientid: e.target.value })}
                    placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
                  />
                </div>
                <div className="space-y-2">
                  <Label>Client Secret</Label>
                  <Input
                    type="password"
                    value={formData.dtgclientsecret}
                    onChange={(e) => setFormData({ ...formData, dtgclientsecret: e.target.value })}
                    placeholder="Client secret"
                  />
                </div>
              </div>
            )}

            {showGcpServiceAccount && (
              <div className="space-y-2">
                <Label>Service Account JSON</Label>
                <Textarea
                  value={formData.dtgpassword}
                  onChange={(e) => setFormData({ ...formData, dtgpassword: e.target.value })}
                  placeholder='{"type": "service_account", "project_id": "...", ...}'
                  rows={6}
                />
              </div>
            )}

            {showOAuth2 && (
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Client ID</Label>
                  <Input
                    value={formData.dtgclientid}
                    onChange={(e) => setFormData({ ...formData, dtgclientid: e.target.value })}
                  />
                </div>
                <div className="space-y-2">
                  <Label>Client Secret</Label>
                  <Input
                    type="password"
                    value={formData.dtgclientsecret}
                    onChange={(e) => setFormData({ ...formData, dtgclientsecret: e.target.value })}
                  />
                </div>
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              {(isRelational || isNoSQL || isWarehouse) && (
                <div className="flex items-center space-x-2">
                  <Switch
                    checked={formData.dtgsslrequired || false}
                    onChange={(checked) =>
                      setFormData({ ...formData, dtgsslrequired: checked })
                    }
                  />
                  <Label>{t("governance.data.integrations.form.sslRequired")}</Label>
                </div>
              )}
              <div className="flex items-center space-x-2">
                <Switch
                  checked={formData.dtgenabled || false}
                  onChange={(checked) =>
                    setFormData({ ...formData, dtgenabled: checked })
                  }
                />
                <Label>{t("governance.data.integrations.form.enabled")}</Label>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <Switch
                checked={formData.dtgsyncenabled || false}
                onChange={(checked) =>
                  setFormData({ ...formData, dtgsyncenabled: checked })
                }
              />
              <Label>{t("governance.data.integrations.form.syncEnabled")}</Label>
            </div>

            {formData.dtgsyncenabled && (
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.syncFrequency")}</Label>
                <Input
                  type="number"
                  value={formData.dtgsyncfrequencyhours}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      dtgsyncfrequencyhours: parseInt(e.target.value),
                    })
                  }
                />
              </div>
            )}

            <div className="flex justify-end space-x-2">
              <Button
                type="button"
                variant="outline"
                onClick={() => router.push("/governance/data/integrations")}
              >
                {t("common.cancel")}
              </Button>
              <Button type="submit" disabled={saving}>
                {saving ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    {t("common.saving", "Guardando...")}
                  </>
                ) : (
                  <>
                    <Save className="mr-2 h-4 w-4" />
                    {t("common.save")}
                  </>
                )}
              </Button>
            </div>
          </CardContent>
        </Card>
      </form>
    </div>
  );
}
