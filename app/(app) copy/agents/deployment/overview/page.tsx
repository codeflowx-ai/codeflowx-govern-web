"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Eye, Plus, Search, Server, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";

interface Deployment {
  id: string; // UUID
  uuid?: string;
  agentUuid?: string;
  agentName?: string;
  deploymentType: string;
  status: string;
  environment: string;
  deployedAt: string;
}

export default function DeploymentOverviewPage() {
  const { t, mounted } = useTranslation();
  const [deployments, setDeployments] = useState<Deployment[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/deployment/list");
      const data = await response.json();
      setDeployments(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading deployments:", error);
      setDeployments([
        {
          id: "d1e2f3a4-b5c6-4789-d012-345678901234",
          uuid: "d1e2f3a4-b5c6-4789-d012-345678901234",
          agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          agentName: "Credit Scoring Agent",
          deploymentType: "PRODUCTION",
          status: "ACTIVE",
          environment: "prod",
          deployedAt: "2024-01-15"
        },
        {
          id: "e2f3a4b5-c6d7-4890-e123-456789012345",
          uuid: "e2f3a4b5-c6d7-4890-e123-456789012345",
          agentUuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
          agentName: "Document OCR Agent",
          deploymentType: "STAGING",
          status: "PENDING",
          environment: "staging",
          deployedAt: "2024-01-16"
        },
        {
          id: "f3a4b5c6-d7e8-4901-f234-567890123456",
          uuid: "f3a4b5c6-d7e8-4901-f234-567890123456",
          agentUuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
          agentName: "Fraud Detection Agent",
          deploymentType: "DEVELOPMENT",
          status: "ACTIVE",
          environment: "dev",
          deployedAt: "2024-01-17"
        },
      ]);
      setTotalItems(3);
      setActiveItems(2);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };
  const filteredDeployments = deployments.filter((deployment) =>
    deployment.deploymentType.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (deployment.agentName && deployment.agentName.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (deployment.agentUuid && deployment.agentUuid.toLowerCase().includes(searchTerm.toLowerCase())) ||
    (deployment.uuid && deployment.uuid.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Server className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Server className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.deployment.title", "Deployment")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.deployment.subtitle", "Gestión de Deployment")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            window.location.href = "/governance/agents/deployment/create";
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.deployment.register", "Registrar Deployment")}
        </Button>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-3 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardBody className="p-4">
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t("common.search", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
              />
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.deployment.list", "Listado de Deployments")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredDeployments.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("agents.deployment.uuid", "UUID")}</th>
                    <th className="text-left p-2">{t("agents.deployment.detail.agentName", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.deployment.deploymentType", "Deployment Type")}</th>
                    <th className="text-left p-2">{t("common.status", "Status")}</th>
                    <th className="text-left p-2">{t("agents.deployment.environment", "Environment")}</th>
                    <th className="text-left p-2">{t("agents.deployment.deployedAt", "Deployed At")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDeployments.map((deployment) => (
                    <tr
                      key={deployment.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">
                        <span className="text-xs font-mono text-muted-foreground">
                          {deployment.uuid || deployment.id}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-medium">{deployment.agentName || "N/A"}</span>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {deployment.deploymentType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            deployment.status === "ACTIVE"
                              ? "bg-green-500/20 text-green-500"
                              : "bg-yellow-500/20 text-yellow-500"
                          }`}
                        >
                          {deployment.status}
                        </span>
                      </td>
                      <td className="p-2">{deployment.environment}</td>
                      <td className="p-2">{deployment.deployedAt}</td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            title={t("common.view", "Ver")}
                            onClick={() => {
                              window.location.href = `/governance/agents/deployment/${deployment.id}`;
                            }}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0 text-destructive"
                            onClick={() => {
                              if (confirm(t("common.confirmDelete", "¿Está seguro de eliminar este deployment?"))) {
                                console.log("Eliminar deployment:", deployment.id);
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
          )}
        </CardBody>
      </Card>
    </div>
  );
}


