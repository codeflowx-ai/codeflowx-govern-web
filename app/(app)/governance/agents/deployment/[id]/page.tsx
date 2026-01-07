"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Server, Edit } from "lucide-react";
import { useState, useEffect } from "react";
import { useParams } from "next/navigation";

interface Deployment {
  id: string; // UUID
  agentId?: number;
  agentUuid?: string;
  agentName?: string;
  agentType?: string; // CONVERSATIONAL, etc.
  agentUrl?: string; // URL del agente conversacional
  deploymentType: string;
  status: string;
  environment: string;
  deployedAt: string;
  deployedBy?: string;
  version?: string;
  configuration?: string;
  notes?: string;
  createdAt?: string;
  lastModified?: string;
}

export default function DeploymentDetailPage() {
  const { t, mounted } = useTranslation();
  const params = useParams();
  const deploymentId = params.id as string;
  const [deployment, setDeployment] = useState<Deployment | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDeployment();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [deploymentId]);

  const loadDeployment = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/deployment/${deploymentId}`);
      if (response.ok) {
        const data = await response.json();
        setDeployment(data);
      } else {
        // Mock data para desarrollo
        setDeployment({
          id: deploymentId,
          agentId: 1,
          agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          agentName: "Credit Scoring Agent",
          agentType: "CONVERSATIONAL",
          agentUrl: "https://agents.example.com/credit-scoring/v2.1.0",
          deploymentType: "PRODUCTION",
          status: "ACTIVE",
          environment: "prod",
          deployedAt: "2024-01-15T10:30:00",
          deployedBy: "admin@example.com",
          version: "v2.1.0",
          configuration: JSON.stringify({ replicas: 3, resources: { cpu: "2", memory: "4Gi" } }, null, 2),
          notes: "Deployment inicial en producción",
          createdAt: "2024-01-15",
          lastModified: "2024-01-15",
        });
      }
    } catch (error) {
      console.error("Error loading deployment:", error);
      // Mock data para desarrollo
      setDeployment({
        id: deploymentId,
        agentId: 1,
        agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
        agentName: "Credit Scoring Agent",
        agentType: "CONVERSATIONAL",
        agentUrl: "https://agents.example.com/credit-scoring/v2.1.0",
        deploymentType: "PRODUCTION",
        status: "ACTIVE",
        environment: "prod",
        deployedAt: "2024-01-15T10:30:00",
        deployedBy: "admin@example.com",
        version: "v2.1.0",
        configuration: JSON.stringify({ replicas: 3, resources: { cpu: "2", memory: "4Gi" } }, null, 2),
        notes: "Deployment inicial en producción",
        createdAt: "2024-01-15",
        lastModified: "2024-01-15",
      });
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusColors: Record<string, string> = {
      ACTIVE: "bg-green-500/20 text-green-500",
      PENDING: "bg-yellow-500/20 text-yellow-500",
      DEPLOYED: "bg-blue-500/20 text-blue-500",
      FAILED: "bg-red-500/20 text-red-500",
      ROLLED_BACK: "bg-gray-500/20 text-gray-500",
    };
    return (
      <Badge className={statusColors[status] || statusColors.PENDING}>
        {status}
      </Badge>
    );
  };

  const getEnvironmentBadge = (environment: string) => {
    const envColors: Record<string, string> = {
      prod: "bg-red-500/20 text-red-500",
      staging: "bg-yellow-500/20 text-yellow-500",
      dev: "bg-blue-500/20 text-blue-500",
      test: "bg-purple-500/20 text-purple-500",
    };
    return (
      <Badge className={envColors[environment] || envColors.dev}>
        {environment.toUpperCase()}
      </Badge>
    );
  };

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

  if (!deployment) {
    return (
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("agents.deployment.detail.title", "Detalle del Deployment")}
              </h1>
            </div>
          </div>
          <Button
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
            onClick={() => {
              window.location.href = "/governance/agents/deployment/overview";
            }}
          >
            <ArrowLeft className="h-4 w-4" />
            {t("common.back", "Volver")}
          </Button>
        </div>
        <Card>
          <CardBody>
            <p className="text-center text-muted-foreground py-8">
              {t("agents.deployment.detail.notFound", "Deployment no encontrado")}
            </p>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Server className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.deployment.detail.title", "Detalle del Deployment")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.deployment.detail.subtitle", "Información detallada del deployment")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
            onClick={() => {
              window.location.href = `/governance/agents/deployment/${deploymentId}/edit`;
            }}
          >
            <Edit className="h-4 w-4" />
            {t("common.edit", "Editar")}
          </Button>
          <Button
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
            onClick={() => {
              window.location.href = "/governance/agents/deployment/overview";
            }}
          >
            <ArrowLeft className="h-4 w-4" />
            {t("common.back", "Volver")}
          </Button>
        </div>
      </div>

      {/* Información del Deployment */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <CardTitle>
            {t("agents.deployment.detail.infoTitle", "Información del Deployment")}
          </CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* UUID */}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.detail.uuid", "UUID")}
              </Label>
              <p className="text-base font-mono text-sm">{deployment.id}</p>
            </div>

            {/* Agent ID */}
            {deployment.agentId && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.agentId", "Agent ID")}
                </Label>
                <p className="text-base font-semibold">{deployment.agentId}</p>
              </div>
            )}

            {/* Agent Name */}
            {deployment.agentName && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.agentName", "Agent Name")}
                </Label>
                <p className="text-base">{deployment.agentName}</p>
              </div>
            )}

            {/* Agent UUID */}
            {deployment.agentUuid && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.agentUuid", "Agent UUID")}
                </Label>
                <p className="text-base font-mono text-sm">{deployment.agentUuid}</p>
              </div>
            )}

            {/* Agent URL (si es conversacional) */}
            {deployment.agentType === "CONVERSATIONAL" && deployment.agentUrl && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.agentUrl", "Agent URL")}
                </Label>
                <div className="flex items-center gap-2">
                  <a
                    href={deployment.agentUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-sm text-primary hover:underline font-mono"
                  >
                    {deployment.agentUrl}
                  </a>
                </div>
              </div>
            )}

            {/* Deployment Type */}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.deploymentType", "Deployment Type")}
              </Label>
              <Badge variant="outline" className="w-fit">
                {deployment.deploymentType}
              </Badge>
            </div>

            {/* Environment */}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.environment", "Environment")}
              </Label>
              <div>{getEnvironmentBadge(deployment.environment)}</div>
            </div>

            {/* Status */}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("common.status", "Estado")}
              </Label>
              <div>{getStatusBadge(deployment.status)}</div>
            </div>

            {/* Version */}
            {deployment.version && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.version", "Versión")}
                </Label>
                <p className="text-base">{deployment.version}</p>
              </div>
            )}

            {/* Deployed At */}
            <div className="space-y-2">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.deployedAt", "Deployed At")}
              </Label>
              <p className="text-base">
                {deployment.deployedAt ? new Date(deployment.deployedAt).toLocaleString() : "-"}
              </p>
            </div>

            {/* Deployed By */}
            {deployment.deployedBy && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("agents.deployment.detail.deployedBy", "Deployed By")}
                </Label>
                <p className="text-base">{deployment.deployedBy}</p>
              </div>
            )}

            {/* Created At */}
            {deployment.createdAt && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("common.createdAt", "Creado")}
                </Label>
                <p className="text-base">{deployment.createdAt}</p>
              </div>
            )}

            {/* Last Modified */}
            {deployment.lastModified && (
              <div className="space-y-2">
                <Label className="text-sm font-medium text-muted-foreground">
                  {t("common.lastModified", "Última Modificación")}
                </Label>
                <p className="text-base">{deployment.lastModified}</p>
              </div>
            )}
          </div>

          {/* Configuration */}
          {deployment.configuration && (
            <div className="space-y-2 pt-4 border-t">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.detail.configuration", "Configuración")}
              </Label>
              <pre className="text-xs font-mono bg-muted p-3 rounded-lg overflow-x-auto">
                {deployment.configuration}
              </pre>
            </div>
          )}

          {/* Notes */}
          {deployment.notes && (
            <div className="space-y-2 pt-4 border-t">
              <Label className="text-sm font-medium text-muted-foreground">
                {t("agents.deployment.detail.notes", "Notas")}
              </Label>
              <p className="text-base text-muted-foreground">{deployment.notes}</p>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}
