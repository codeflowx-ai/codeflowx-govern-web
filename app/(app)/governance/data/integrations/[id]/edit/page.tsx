"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
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
import { ArrowLeft, Save, Loader2 } from "lucide-react";
import { DatabaseIntegration, DatabaseType, AuthenticationType } from "../../../types/integrations";

export default function EditIntegrationPage() {
  const params = useParams();
  const router = useRouter();
  const { t } = useTranslation();
  const integrationId = parseInt(params.id as string);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [integration, setIntegration] = useState<DatabaseIntegration | null>(null);

  const [formData, setFormData] = useState<Partial<DatabaseIntegration>>({
    dtgname: "",
    dtgdescription: "",
    dtgdatabasetype: "POSTGRESQL",
    dtghost: "",
    dtgport: 5432,
    dtgdatabase: "",
    dtgauthenticationtype: "USERNAME_PASSWORD",
    dtgusername: "",
    dtgsslrequired: false,
    dtgenabled: true,
    dtgsyncenabled: true,
    dtgsyncfrequencyhours: 1,
  });

  useEffect(() => {
    loadIntegration();
  }, [integrationId]);

  const loadIntegration = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/integrations/${integrationId}`);
      if (response.ok) {
        const data = await response.json();
        setIntegration(data);
        setFormData({
          ...data,
          dtgpassword: "", // No mostrar contraseña
        });
      } else {
        router.push("/governance/data/integrations");
      }
    } catch (error) {
      console.error("Error cargando integración:", error);
      router.push("/governance/data/integrations");
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setSaving(true);
      const response = await fetch(`/api/v1/governance/data/integrations/${integrationId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        router.push(`/governance/data/integrations/${integrationId}/explore`);
      } else {
        alert("Error al actualizar la integración");
      }
    } catch (error) {
      console.error("Error actualizando integración:", error);
      alert("Error al actualizar la integración");
    } finally {
      setSaving(false);
    }
  };

  // Función auxiliar para obtener auth por defecto
  const getDefaultAuthForProvider = (provider: DatabaseType): AuthenticationType => {
    if (["HUGGINGFACE", "KAGGLE", "API"].includes(provider)) return "API_KEY";
    if (["S3", "REDSHIFT"].includes(provider)) return "AWS_IAM";
    if (["AZURE_BLOB", "ADLS_GEN2", "AZURE_ML"].includes(provider)) return "AZURE_AD";
    if (["GCS", "BIGQUERY", "VERTEX_AI"].includes(provider)) return "GCP_SERVICE_ACCOUNT";
    if (["DATABRICKS", "DATABRICKS_ML", "MLFLOW"].includes(provider)) return "API_KEY";
    if (["SAGEMAKER"].includes(provider)) return "AWS_IAM";
    return "USERNAME_PASSWORD";
  };

  const currentType = formData.dtgdatabasetype || "POSTGRESQL";
  const isRelational =
    ["POSTGRESQL", "MYSQL", "MARIADB", "SQLSERVER", "ORACLE", "DB2", "SQLITE"].includes(currentType);
  const isNoSQL = ["MONGODB", "CASSANDRA", "REDIS", "ELASTICSEARCH", "NEO4J", "DYNAMODB"].includes(currentType);
  const isDataLake = ["S3", "AZURE_BLOB", "GCS", "ADLS_GEN2", "HDFS", "MINIO", "IBM_COS", "OCI_OBJECT_STORAGE"].includes(currentType);
  const isWarehouse = ["SNOWFLAKE", "DATABRICKS", "BIGQUERY", "REDSHIFT"].includes(currentType);
  const isMLPlatform = ["SAGEMAKER", "VERTEX_AI", "AZURE_ML", "MLFLOW", "DATABRICKS_ML", "KUBEFLOW", "SELDON", "WAND_B"].includes(currentType);
  const isExternal = ["HUGGINGFACE", "KAGGLE", "API"].includes(currentType);

  // Determinar si mostrar selector de tipo de autenticación
  const showAuthTypeSelector = !isExternal;

  // Determinar qué campos de autenticación mostrar
  const authType = formData.dtgauthenticationtype || getDefaultAuthForProvider(currentType);
  const showUsernamePassword = authType === "USERNAME_PASSWORD";
  const showApiKey = authType === "API_KEY";
  const showAwsIam = authType === "AWS_IAM";
  const showAzureAd = authType === "AZURE_AD";
  const showGcpServiceAccount = authType === "GCP_SERVICE_ACCOUNT";
  const showOAuth2 = authType === "OAUTH2";

  const databaseTypes: { value: DatabaseType; label: string; category: string }[] = [
    { value: "POSTGRESQL", label: "PostgreSQL", category: "Relacional" },
    { value: "MYSQL", label: "MySQL", category: "Relacional" },
    { value: "MARIADB", label: "MariaDB", category: "Relacional" },
    { value: "SQLSERVER", label: "SQL Server", category: "Relacional" },
    { value: "ORACLE", label: "Oracle", category: "Relacional" },
    { value: "DB2", label: "DB2", category: "Relacional" },
    { value: "SQLITE", label: "SQLite", category: "Relacional" },
    { value: "MONGODB", label: "MongoDB", category: "NoSQL" },
    { value: "CASSANDRA", label: "Cassandra", category: "NoSQL" },
    { value: "REDIS", label: "Redis", category: "NoSQL" },
    { value: "ELASTICSEARCH", label: "Elasticsearch", category: "NoSQL" },
    { value: "NEO4J", label: "Neo4j", category: "NoSQL" },
    { value: "S3", label: "Amazon S3", category: "Data Lake" },
    { value: "AZURE_BLOB", label: "Azure Blob Storage", category: "Data Lake" },
    { value: "GCS", label: "Google Cloud Storage", category: "Data Lake" },
    { value: "DATABRICKS", label: "Databricks", category: "Data Warehouse" },
    { value: "SNOWFLAKE", label: "Snowflake", category: "Data Warehouse" },
    { value: "BIGQUERY", label: "BigQuery", category: "Data Warehouse" },
    { value: "HUGGINGFACE", label: "HuggingFace", category: "Externo" },
    { value: "KAGGLE", label: "Kaggle", category: "Externo" },
    { value: "API", label: "API REST", category: "Externo" },
  ];

  const groupedDatabaseTypes = databaseTypes.reduce((acc, type) => {
    if (!acc[type.category]) {
      acc[type.category] = [];
    }
    acc[type.category].push(type);
    return acc;
  }, {} as Record<string, typeof databaseTypes>);

  if (loading) {
    return (
      <div className="w-full py-6 px-6">
        <div className="text-center py-8">{t("common.loading")}</div>
      </div>
    );
  }

  if (!integration) {
    return (
      <div className="w-full py-6 px-6">
        <div className="text-center">Integración no encontrada</div>
      </div>
    );
  }

  return (
    <div className="w-full py-6 space-y-6 px-6">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">{t("governance.data.integrations.edit")}</h1>
        <p className="text-muted-foreground mt-2">{integration.dtgname}</p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button variant="ghost" onClick={() => router.push("/governance/data/integrations")}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("common.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button type="submit" form="integration-edit-form" disabled={saving}>
            {saving ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Guardando...
              </>
            ) : (
              <>
                <Save className="mr-2 h-4 w-4" />
                Guardar Cambios
              </>
            )}
          </Button>
        </div>
      </div>

      <form id="integration-edit-form" onSubmit={handleSubmit}>
        <Card>
          <CardHeader>
            <CardTitle>{t("governance.data.integrations.edit")}</CardTitle>
          </CardHeader>
          <CardContent className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.name")}</Label>
                <Input
                  value={formData.dtgname}
                  onChange={(e) => setFormData({ ...formData, dtgname: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label>{t("governance.data.integrations.form.databaseType")}</Label>
                <Select
                  value={formData.dtgdatabasetype}
                  onValueChange={(value) =>
                    setFormData({ ...formData, dtgdatabasetype: value as DatabaseType })
                  }
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {Object.entries(groupedDatabaseTypes).map(([category, types]) => (
                      <div key={category}>
                        <div className="px-2 py-1.5 text-xs font-semibold text-muted-foreground">
                          {category}
                        </div>
                        {types.map((type) => (
                          <SelectItem key={type.value} value={type.value}>
                            {type.label}
                          </SelectItem>
                        ))}
                      </div>
                    ))}
                  </SelectContent>
                </Select>
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
                    placeholder="Dejar vacío para mantener la actual"
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
                  placeholder="Dejar vacío para mantener la actual"
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
                    placeholder="Dejar vacío para mantener la actual"
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
                    placeholder="Dejar vacío para mantener la actual"
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
                    placeholder="Dejar vacío para mantener la actual"
                  />
                </div>
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              {(isRelational || isNoSQL || isWarehouse) && (
                <div className="flex items-center space-x-2">
                  <Switch
                    checked={formData.dtgsslrequired ?? false}
                    onChange={(checked) =>
                      setFormData({ ...formData, dtgsslrequired: checked })
                    }
                  />
                  <Label>{t("governance.data.integrations.form.sslRequired")}</Label>
                </div>
              )}
              <div className="flex items-center space-x-2">
                <Switch
                  checked={formData.dtgenabled ?? true}
                  onChange={(checked) =>
                    setFormData({ ...formData, dtgenabled: checked })
                  }
                />
                <Label>{t("governance.data.integrations.form.enabled")}</Label>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <Switch
                checked={formData.dtgsyncenabled ?? true}
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
