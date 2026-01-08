"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  CheckCircle2,
  Clock,
  Cloud,
  Database,
  Plus,
  RefreshCw,
  Server,
  Settings,
  TestTube,
  XCircle,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { DatabaseIntegration, DatabaseType } from "../types/integrations";

type CategoryCode =
  | "ALL"
  | "RELATIONAL"
  | "NOSQL"
  | "DATA_LAKE"
  | "DATA_WAREHOUSE"
  | "ML_PLATFORM"
  | "EXTERNAL";

export default function DataGovernanceIntegrationsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [integrations, setIntegrations] = useState<DatabaseIntegration[]>([]);
  const [loading, setLoading] = useState(true);
  const [testingIntegration, setTestingIntegration] = useState<number | null>(null);
  const [categoryFilter, setCategoryFilter] = useState<CategoryCode>("ALL");
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    loadIntegrations();
  }, []);

  const loadIntegrations = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/v1/governance/data/integrations");
      if (response.ok) {
        const data = await response.json();
        setIntegrations(data);
      }
    } catch (error) {
      console.error("Error cargando integraciones:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleTest = async (integration: DatabaseIntegration) => {
    setTestingIntegration(integration.idxintegration);
    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integration.idxintegration}/test`,
        { method: "POST" }
      );
      if (response.ok) {
        loadIntegrations();
      }
    } catch (error) {
      console.error("Error probando integración:", error);
    } finally {
      setTestingIntegration(null);
    }
  };

  const handleSync = async (integration: DatabaseIntegration) => {
    try {
      const response = await fetch(
        `/api/v1/governance/data/integrations/${integration.idxintegration}/sync`,
        { method: "POST" }
      );
      if (response.ok) {
        loadIntegrations();
      }
    } catch (error) {
      console.error("Error sincronizando:", error);
    }
  };

  const getDatabaseIcon = (type: DatabaseType) => {
    if (["POSTGRESQL", "MYSQL", "MARIADB", "SQLSERVER", "ORACLE", "DB2", "SQLITE"].includes(type)) {
      return Database;
    }
    if (["MONGODB", "CASSANDRA", "REDIS", "ELASTICSEARCH", "NEO4J"].includes(type)) {
      return Server;
    }
    return Cloud;
  };

  const getCategoryCode = (type: DatabaseType): CategoryCode => {
    if (["POSTGRESQL", "MYSQL", "MARIADB", "SQLSERVER", "ORACLE", "DB2", "SQLITE"].includes(type)) {
      return "RELATIONAL";
    }
    if (["MONGODB", "CASSANDRA", "REDIS", "ELASTICSEARCH", "NEO4J"].includes(type)) {
      return "NOSQL";
    }
    if (["S3", "AZURE_BLOB", "GCS", "ADLS_GEN2", "HDFS", "MINIO", "IBM_COS", "OCI_OBJECT_STORAGE"].includes(type)) {
      return "DATA_LAKE";
    }
    if (["DATABRICKS", "SNOWFLAKE", "BIGQUERY", "REDSHIFT"].includes(type)) {
      return "DATA_WAREHOUSE";
    }
    if (["SAGEMAKER", "VERTEX_AI", "AZURE_ML", "MLFLOW", "KUBEFLOW", "SELDON", "WAND_B", "DATABRICKS_ML"].includes(type)) {
      return "ML_PLATFORM";
    }
    if (["HUGGINGFACE", "KAGGLE", "API"].includes(type)) {
      return "EXTERNAL";
    }
    return "EXTERNAL";
  };

  const getCategoryLabel = (type: DatabaseType) => {
    const code = getCategoryCode(type);
    switch (code) {
      case "RELATIONAL":
        return t("governance.data.integrations.categories.relational", "Relacional");
      case "NOSQL":
        return t("governance.data.integrations.categories.nosql", "NoSQL");
      case "DATA_LAKE":
        return t("governance.data.integrations.categories.dataLake", "Data Lake");
      case "DATA_WAREHOUSE":
        return t("governance.data.integrations.categories.dataWarehouse", "Data Warehouse");
      case "ML_PLATFORM":
        return t("governance.data.integrations.categories.mlPlatform", "Plataforma ML");
      case "EXTERNAL":
      default:
        return t("governance.data.integrations.categories.external", "Externo");
    }
  };

  const filteredIntegrations = integrations.filter((item) => {
    if (categoryFilter === "ALL") return true;
    return getCategoryCode(item.dtgdatabasetype) === categoryFilter;
  });

  const searchedIntegrations = filteredIntegrations.filter((item) =>
    item.dtgname.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (item.dtgdescription || "").toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusIcon = (status?: string) => {
    switch (status) {
      case "SUCCESS":
        return CheckCircle2;
      case "FAILED":
        return XCircle;
      case "SYNCING":
        return RefreshCw;
      default:
        return Clock;
    }
  };

  const getStatusColor = (status?: string): "success" | "danger" | "secondary" | "outline" => {
    switch (status) {
      case "SUCCESS":
        return "success";
      case "FAILED":
        return "danger";
      case "SYNCING":
        return "secondary";
      default:
        return "outline";
    }
  };

  return (
    <div className="w-full py-6 space-y-6 px-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">{t("governance.data.integrations.title")}</h1>
          <p className="text-muted-foreground mt-2">{t("governance.data.integrations.description")}</p>
        </div>
        <div className="flex items-center gap-2 flex-wrap">
          <div className="flex items-center gap-2">
            <select
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value as CategoryCode)}
              className="h-10 rounded-md border border-input bg-background px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2"
            >
              <option value="ALL">{t("governance.data.integrations.filters.all", "Todos")}</option>
              <option value="RELATIONAL">{t("governance.data.integrations.categories.relational", "Relacional")}</option>
              <option value="NOSQL">{t("governance.data.integrations.categories.nosql", "NoSQL")}</option>
              <option value="DATA_LAKE">{t("governance.data.integrations.categories.dataLake", "Data Lake")}</option>
              <option value="DATA_WAREHOUSE">{t("governance.data.integrations.categories.dataWarehouse", "Data Warehouse")}</option>
              <option value="ML_PLATFORM">{t("governance.data.integrations.categories.mlPlatform", "Plataforma ML")}</option>
              <option value="EXTERNAL">{t("governance.data.integrations.categories.external", "Externo")}</option>
            </select>
            <Input
              className="w-64"
              placeholder={t("common.search", "Buscar...")}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <Button onClick={() => router.push("/governance/data/integrations/create")}>
            <Plus className="mr-2 h-4 w-4" />
            {t("governance.data.integrations.create")}
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>{t("governance.data.integrations.list")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading")}</div>
          ) : searchedIntegrations.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.data.integrations.empty")}
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>{t("governance.data.integrations.columns.name")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.type")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.category", "Categoría")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.host")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.status")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.syncStatus")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.origins")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.datasets")}</TableHead>
                  <TableHead>{t("governance.data.integrations.columns.actions")}</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {searchedIntegrations.map((integration) => {
                  const Icon = getDatabaseIcon(integration.dtgdatabasetype);
                  const StatusIcon = getStatusIcon(integration.dtgsyncstatus);
                  return (
                    <TableRow key={integration.idxintegration}>
                      <TableCell>
                        <div className="flex items-center space-x-2">
                          <Icon className="h-4 w-4" />
                          <span className="font-medium">{integration.dtgname}</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{integration.dtgdatabasetype}</Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant="secondary">{getCategoryLabel(integration.dtgdatabasetype)}</Badge>
                      </TableCell>
                      <TableCell>{integration.dtghost || "-"}</TableCell>
                      <TableCell>
                        <Badge variant={integration.dtgenabled ? "success" : "secondary"}>
                          {integration.dtgenabled
                            ? t("governance.data.integrations.status.enabled")
                            : t("governance.data.integrations.status.disabled")}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {integration.dtgsyncstatus && (
                          <Badge variant={getStatusColor(integration.dtgsyncstatus)}>
                            <StatusIcon className="mr-1 h-3 w-3" />
                            {t(`governance.data.integrations.syncStatus.${integration.dtgsyncstatus.toLowerCase()}`)}
                          </Badge>
                        )}
                      </TableCell>
                      <TableCell>{integration.originsCount || 0}</TableCell>
                      <TableCell>{integration.datasetsCount || 0}</TableCell>
                      <TableCell>
                        <div className="flex items-center space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.preventDefault();
                              e.stopPropagation();
                              handleTest(integration);
                            }}
                            disabled={testingIntegration === integration.idxintegration}
                            title="Probar conexión"
                          >
                            <TestTube className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.preventDefault();
                              e.stopPropagation();
                              handleSync(integration);
                            }}
                            title="Sincronizar"
                          >
                            <RefreshCw className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.preventDefault();
                              e.stopPropagation();
                              router.push(`/governance/data/integrations/${integration.idxintegration}/explore`);
                            }}
                            title="Explorar esquema"
                          >
                            <Database className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={(e) => {
                              e.preventDefault();
                              e.stopPropagation();
                              router.push(`/governance/data/integrations/${integration.idxintegration}/edit`);
                            }}
                            title="Editar integración"
                          >
                            <Settings className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
