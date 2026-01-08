"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ArrowLeft, CheckCircle, Database, Globe, RefreshCw, Settings, XCircle } from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { DataGovernanceOrigin } from "../../types/data-governance";

export default function DataOriginDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const originId = params.id as string;
  const [origin, setOrigin] = useState<DataGovernanceOrigin | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (originId) {
      loadOrigin();
    }
  }, [originId]);

  const loadOrigin = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/origins/${originId}`);
      if (response.ok) {
        const data = await response.json();
        setOrigin(data);
      }
    } catch (error) {
      console.error("Error loading origin:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSync = async () => {
    try {
      const response = await fetch(`/api/v1/governance/data/origins/${originId}/sync`, {
        method: "POST",
      });
      if (response.ok) {
        loadOrigin();
      }
    } catch (error) {
      console.error("Error syncing origin:", error);
    }
  };

  const handleTestConnection = async () => {
    try {
      const response = await fetch(`/api/v1/governance/data/origins/${originId}/test`, {
        method: "POST",
      });
      if (response.ok) {
        const result = await response.json();
        alert(result.success ? "Conexión exitosa" : `Error: ${result.error}`);
      }
    } catch (error) {
      console.error("Error testing connection:", error);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: "bg-green-100 text-green-800",
      INACTIVE: "bg-gray-100 text-gray-800",
      DEPRECATED: "bg-yellow-100 text-yellow-800",
      ERROR: "bg-red-100 text-red-800",
    };
    return colors[status] || "bg-gray-100 text-gray-800";
  };

  const getTypeIcon = (type: string) => {
    if (type === "EXTERNAL") {
      return <Globe className="h-4 w-4" />;
    }
    return <Database className="h-4 w-4" />;
  };

  if (loading) {
    return <div className="w-full p-6">Cargando...</div>;
  }

  if (!origin) {
    return <div className="w-full p-6">Origen no encontrado</div>;
  }

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center gap-4 mb-4">
        <Button
          variant="ghost"
          onClick={() => router.push("/governance/data/origins")}
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("governance.data.origins.detail.actions.back", "Volver")}
        </Button>
      </div>

      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">{origin.dtgorname}</h1>
          <p className="text-muted-foreground mt-1">{origin.dtgordescription}</p>
        </div>
        <div className="flex gap-2">
          <Button onClick={handleTestConnection} variant="outline">
            <Settings className="mr-2 h-4 w-4" />
            {t("governance.data.origins.detail.actions.testConnection", "Probar Conexión")}
          </Button>
          <Button onClick={handleSync} variant="outline">
            <RefreshCw className="mr-2 h-4 w-4" />
            {t("governance.data.origins.detail.actions.sync", "Sincronizar")}
          </Button>
          <Button onClick={() => router.push(`/governance/data/origins/${originId}/edit`)}>
            <Settings className="mr-2 h-4 w-4" />
            {t("governance.data.origins.detail.actions.edit", "Editar")}
          </Button>
        </div>
      </div>

      <Tabs defaultValue="overview">
        <TabsList>
          <TabsTrigger value="overview">
            {t("governance.data.origins.detail.tabs.overview", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="connection">
            {t("governance.data.origins.detail.tabs.connection", "Conexión")}
          </TabsTrigger>
          <TabsTrigger value="sync">
            {t("governance.data.origins.detail.tabs.sync", "Sincronización")}
          </TabsTrigger>
          <TabsTrigger value="datasets">
            {t("governance.data.origins.detail.tabs.datasets", "Datasets")}
          </TabsTrigger>
        </TabsList>

        <TabsContent value="overview">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.origins.detail.tabs.overview", "Información General")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.info.name", "Nombre")}
                  </label>
                  <p className="text-sm">{origin.dtgorname}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.info.type", "Tipo")}
                  </label>
                  <p className="text-sm">
                    <Badge variant="outline" className="flex items-center gap-1 w-fit">
                      {getTypeIcon(origin.dtgortype)}
                      {origin.dtgortype}
                    </Badge>
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.info.category", "Categoría")}
                  </label>
                  <p className="text-sm">
                    <Badge variant="outline">{origin.dtgorcategory}</Badge>
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.info.status", "Estado")}
                  </label>
                  <p className="text-sm">
                    <Badge className={getStatusBadge(origin.dtgorstatus)}>
                      {origin.dtgorstatus}
                    </Badge>
                  </p>
                </div>
                {origin.dtgorurl && (
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.url", "URL")}
                    </label>
                    <p className="text-sm">
                      <a
                        href={origin.dtgorurl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-blue-600 hover:underline"
                      >
                        {origin.dtgorurl}
                      </a>
                    </p>
                  </div>
                )}
                {origin.dtgorhost && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.host", "Host")}
                    </label>
                    <p className="text-sm">{origin.dtgorhost}</p>
                  </div>
                )}
                {origin.dtgorport && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.port", "Puerto")}
                    </label>
                    <p className="text-sm">{origin.dtgorport}</p>
                  </div>
                )}
                {origin.dtgordatabase && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.database", "Base de Datos")}
                    </label>
                    <p className="text-sm">{origin.dtgordatabase}</p>
                  </div>
                )}
                {origin.dtgorprovider && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.provider", "Proveedor")}
                    </label>
                    <p className="text-sm">{origin.dtgorprovider}</p>
                  </div>
                )}
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.info.enabled", "Habilitado")}
                  </label>
                  <p className="text-sm">
                    {origin.dtgorenabled ? (
                      <Badge className="bg-green-100 text-green-800">
                        <CheckCircle className="mr-1 h-3 w-3" />
                        Sí
                      </Badge>
                    ) : (
                      <Badge variant="outline">
                        <XCircle className="mr-1 h-3 w-3" />
                        No
                      </Badge>
                    )}
                  </p>
                </div>
                {origin.dtgordescription && (
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.info.description", "Descripción")}
                    </label>
                    <p className="text-sm">{origin.dtgordescription}</p>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="connection">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.origins.detail.tabs.connection", "Conexión")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.connection.connectionType", "Tipo de Conexión")}
                  </label>
                  <p className="text-sm">{origin.dtgorconnectiontype || "-"}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.connection.authMethod", "Método de Autenticación")}
                  </label>
                  <p className="text-sm">{origin.dtgorauthmethod || "-"}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.connection.config", "Configuración")}
                  </label>
                  <pre className="text-xs bg-muted p-3 rounded overflow-auto">
                    {origin.dtgorconnectionconfig
                      ? JSON.stringify(JSON.parse(origin.dtgorconnectionconfig), null, 2)
                      : "-"}
                  </pre>
                </div>
                {origin.dtgorauthconfig && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.connection.authConfig", "Configuración de Autenticación")}
                    </label>
                    <pre className="text-xs bg-muted p-3 rounded overflow-auto">
                      {JSON.stringify(JSON.parse(origin.dtgorauthconfig), null, 2)}
                    </pre>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="sync">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.origins.detail.tabs.sync", "Sincronización")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.method", "Método de Sincronización")}
                  </label>
                  <p className="text-sm">{origin.dtgorsyncmethod || "-"}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.frequency", "Frecuencia")}
                  </label>
                  <p className="text-sm">{origin.dtgorsyncfrequency || "-"}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.schedule", "Schedule (Cron)")}
                  </label>
                  <p className="text-sm font-mono text-xs">{origin.dtgorsyncschedule || "-"}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.lastSync", "Última Sincronización")}
                  </label>
                  <p className="text-sm">
                    {origin.dtgorlastsynced
                      ? new Date(origin.dtgorlastsynced).toLocaleString()
                      : "-"}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.lastStatus", "Último Estado")}
                  </label>
                  <p className="text-sm">
                    {origin.dtgorlastsyncstatus ? (
                      <Badge
                        className={
                          origin.dtgorlastsyncstatus === "SUCCESS"
                            ? "bg-green-100 text-green-800"
                            : origin.dtgorlastsyncstatus === "FAILED"
                            ? "bg-red-100 text-red-800"
                            : "bg-yellow-100 text-yellow-800"
                        }
                      >
                        {origin.dtgorlastsyncstatus}
                      </Badge>
                    ) : (
                      "-"
                    )}
                  </p>
                </div>
                {origin.dtgorlastsyncerror && (
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.origins.detail.sync.lastError", "Último Error")}
                    </label>
                    <p className="text-sm text-red-600">{origin.dtgorlastsyncerror}</p>
                  </div>
                )}
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.origins.detail.sync.nextSync", "Próxima Sincronización")}
                  </label>
                  <p className="text-sm">
                    {origin.dtgornextsync
                      ? new Date(origin.dtgornextsync).toLocaleString()
                      : "-"}
                  </p>
                </div>
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="datasets">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.origins.detail.tabs.datasets", "Datasets")}</CardTitle>
            </CardHeader>
            <CardBody>
              <p className="text-muted-foreground">
                {t("governance.data.origins.detail.datasets.description", "Lista de datasets asociados a este origen")}
              </p>
              <Button
                className="mt-4"
                onClick={() => router.push(`/governance/data/datasets/overview?origin=${originId}`)}
              >
                {t("governance.data.origins.detail.datasets.viewAll", "Ver todos los datasets")}
              </Button>
            </CardBody>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
