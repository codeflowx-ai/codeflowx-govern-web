"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Server, Save } from "lucide-react";
import { useState, useEffect } from "react";
import { useParams } from "next/navigation";

interface Agent {
  id: number;
  uuid?: string;
  name: string;
  description: string;
  status: string;
  version: string;
  domain: string;
}

interface DeploymentFormData {
  agentUuid?: string;
  deploymentType: string;
  status: string;
  environment: string;
  version: string;
  configuration?: string;
  notes?: string;
  agentUrl?: string;
}

export default function DeploymentEditPage() {
  const { t, mounted } = useTranslation();
  const params = useParams();
  const deploymentId = params.id as string;
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [deployment, setDeployment] = useState<{ id?: string; agentUuid?: string } | null>(null);
  const [formData, setFormData] = useState<DeploymentFormData>({
    agentUuid: "",
    deploymentType: "PRODUCTION",
    status: "PENDING",
    environment: "prod",
    version: "1.0.0",
    configuration: "",
    notes: "",
    agentUrl: "",
  });

  useEffect(() => {
    loadAgents();
    loadDeployment();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [deploymentId]);

  const loadAgents = async () => {
    setLoadingAgents(true);
    try {
      const response = await fetch("/api/agents/registry/list");
      const data = await response.json();
      setAgents(data.items || []);
    } catch (error) {
      console.error("Error loading agents:", error);
      // Mock data para desarrollo
      setAgents([
        {
          id: 1,
          uuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          name: "Credit Scoring Agent",
          description: "Agent for credit risk assessment",
          status: "ACTIVE",
          version: "v2.1.0",
          domain: "Finance",
        },
        {
          id: 2,
          uuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
          name: "Document OCR Agent",
          description: "Optical character recognition agent",
          status: "ACTIVE",
          version: "v1.9.5",
          domain: "Document Processing",
        },
        {
          id: 3,
          uuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
          name: "Fraud Detection Agent",
          description: "Real-time fraud detection agent",
          status: "PENDING",
          version: "v2.0.3",
          domain: "Security",
        },
        {
          id: 4,
          uuid: "d4e5f6a7-b8c9-4012-d345-678901234567",
          name: "Invoice Processing Agent",
          description: "Automated invoice processing and validation",
          status: "ACTIVE",
          version: "v1.8.2",
          domain: "Finance",
        },
        {
          id: 5,
          uuid: "e5f6a7b8-c9d0-4123-e456-789012345678",
          name: "Customer Onboarding Agent",
          description: "Streamlined customer onboarding process",
          status: "ACTIVE",
          version: "v2.2.1",
          domain: "Customer Service",
        },
      ]);
    } finally {
      setLoadingAgents(false);
    }
  };


  const loadDeployment = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/deployment/${deploymentId}`);
      if (response.ok) {
        const data = await response.json();
        setDeployment(data);
        setFormData({
          agentUuid: data.agentUuid || "",
          deploymentType: data.deploymentType || "PRODUCTION",
          status: data.status || "PENDING",
          environment: data.environment || "prod",
          version: data.version || "1.0.0",
          configuration: data.configuration || "",
          notes: data.notes || "",
          agentUrl: data.agentUrl || "",
        });
      } else {
        // Mock data para desarrollo
        const mockDeployment = {
          id: deploymentId,
          agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          deploymentType: "PRODUCTION",
          status: "ACTIVE",
          environment: "prod",
          version: "v2.1.0",
          configuration: JSON.stringify({ replicas: 3, resources: { cpu: "2", memory: "4Gi" } }, null, 2),
          notes: "Deployment inicial en producción",
          agentUrl: "https://agents.example.com/credit-scoring/v2.1.0",
        };
        setDeployment(mockDeployment);
        setFormData({
          agentUuid: mockDeployment.agentUuid,
          deploymentType: mockDeployment.deploymentType,
          status: mockDeployment.status,
          environment: mockDeployment.environment,
          version: mockDeployment.version,
          configuration: mockDeployment.configuration,
          notes: mockDeployment.notes,
          agentUrl: mockDeployment.agentUrl,
        });
      }
    } catch (error) {
      console.error("Error loading deployment:", error);
      // Mock data para desarrollo
      const mockDeployment = {
        id: deploymentId,
        agentUuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
        deploymentType: "PRODUCTION",
        status: "ACTIVE",
        environment: "prod",
        version: "v2.1.0",
        configuration: JSON.stringify({ replicas: 3, resources: { cpu: "2", memory: "4Gi" } }, null, 2),
        notes: "Deployment inicial en producción",
      };
      setDeployment(mockDeployment);
      setFormData({
        agentUuid: mockDeployment.agentUuid,
        deploymentType: mockDeployment.deploymentType,
        status: mockDeployment.status,
        environment: mockDeployment.environment,
        version: mockDeployment.version,
        configuration: mockDeployment.configuration,
        notes: mockDeployment.notes,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const response = await fetch(`/api/governance/agents/deployment/${deploymentId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        // Redirigir al detalle después de actualizar
        window.location.href = `/governance/agents/deployment/${deploymentId}`;
      } else {
        const error = await response.json();
        alert(t("agents.deployment.edit.error", "Error al actualizar el deployment: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al actualizar deployment:", error);
      alert(t("agents.deployment.edit.error", "Error al actualizar el deployment"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    window.location.href = `/governance/agents/deployment/${deploymentId}`;
  };

  if (!mounted || loading || loadingAgents) {
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
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Button
            variant="ghost"
            size="sm"
            className="h-8 w-8 p-0"
            onClick={handleCancel}
            title={t("common.back", "Volver")}
          >
            <ArrowLeft className="h-4 w-4" />
          </Button>
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("agents.deployment.edit.title", "Editar Deployment")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("agents.deployment.edit.subtitle", "Modifique la información del deployment")}
            </p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <Button
            type="button"
            variant="outline"
            onClick={handleCancel}
            disabled={isSubmitting}
          >
            {t("common.cancel", "Cancelar")}
          </Button>
          <Button
            type="submit"
            form="deployment-edit-form"
            className="bg-primary hover:bg-primary/90"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.deployment.edit.saveButton", "Guardar Cambios")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form id="deployment-edit-form" onSubmit={handleSubmit}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* UUID - Solo lectura */}
            <div className="space-y-2">
              <Label htmlFor="uuid">
                {t("agents.deployment.edit.uuid", "UUID")}
              </Label>
              <Input
                id="uuid"
                value={deployment?.id || deploymentId}
                disabled
                className="bg-muted text-muted-foreground"
              />
              <p className="text-xs text-muted-foreground">
                {t("agents.deployment.edit.uuidInfo", "UUID no se puede modificar")}
              </p>
            </div>

            {/* Agent Selection */}
            <div className="space-y-2">
              <Label htmlFor="agentUuid">
                {t("agents.deployment.edit.agent", "Agente")} *
              </Label>
              <Select
                value={formData.agentUuid || ""}
                onValueChange={(value) => {
                  setFormData({ ...formData, agentUuid: value });
                }}
                disabled={loadingAgents}
              >
                <SelectTrigger>
                  <SelectValue
                    placeholder={
                      loadingAgents
                        ? t("common.loading", "Cargando agentes...")
                        : t("agents.deployment.edit.agentPlaceholder", "Seleccione un agente")
                    }
                  />
                </SelectTrigger>
                <SelectContent>
                  {agents.map((agent) => (
                    <SelectItem key={agent.uuid} value={agent.uuid || ""}>
                      <div className="flex flex-col">
                        <span className="font-medium">{agent.name}</span>
                        <span className="text-xs text-muted-foreground font-mono">
                          {agent.uuid}
                        </span>
                      </div>
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              {formData.agentUuid && (
                <p className="text-xs text-muted-foreground">
                  {t("agents.deployment.edit.agentSelected", "Agente seleccionado")}
                </p>
              )}
            </div>

            {/* Deployment Type */}
            <div className="space-y-2">
              <Label htmlFor="deploymentType">
                {t("agents.deployment.edit.deploymentType", "Tipo de Deployment")} *
              </Label>
              <Select
                value={formData.deploymentType}
                onValueChange={(value) => setFormData({ ...formData, deploymentType: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t("agents.deployment.edit.deploymentTypePlaceholder", "Seleccione el tipo")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PRODUCTION">
                    {t("agents.deployment.edit.typeProduction", "Producción")}
                  </SelectItem>
                  <SelectItem value="STAGING">
                    {t("agents.deployment.edit.typeStaging", "Staging")}
                  </SelectItem>
                  <SelectItem value="DEVELOPMENT">
                    {t("agents.deployment.edit.typeDevelopment", "Desarrollo")}
                  </SelectItem>
                  <SelectItem value="TESTING">
                    {t("agents.deployment.edit.typeTesting", "Testing")}
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Environment */}
            <div className="space-y-2">
              <Label htmlFor="environment">
                {t("agents.deployment.edit.environment", "Ambiente")} *
              </Label>
              <Select
                value={formData.environment}
                onValueChange={(value) => setFormData({ ...formData, environment: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t("agents.deployment.edit.environmentPlaceholder", "Seleccione el ambiente")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="prod">PROD</SelectItem>
                  <SelectItem value="staging">STAGING</SelectItem>
                  <SelectItem value="dev">DEV</SelectItem>
                  <SelectItem value="test">TEST</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Version */}
            <div className="space-y-2">
              <Label htmlFor="version">
                {t("agents.deployment.edit.version", "Versión")} *
              </Label>
              <Input
                id="version"
                value={formData.version}
                onChange={(e) => setFormData({ ...formData, version: e.target.value })}
                placeholder={t("agents.deployment.edit.versionPlaceholder", "Ingrese la versión (ej: 1.0.0)")}
                required
              />
            </div>

            {/* Status */}
            <div className="space-y-2">
              <Label htmlFor="status">
                {t("agents.deployment.edit.status", "Estado")} *
              </Label>
              <Select
                value={formData.status}
                onValueChange={(value) => setFormData({ ...formData, status: value })}
              >
                <SelectTrigger>
                  <SelectValue placeholder={t("agents.deployment.edit.statusPlaceholder", "Seleccione el estado")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="PENDING">
                    {t("agents.deployment.edit.statusPending", "Pendiente")}
                  </SelectItem>
                  <SelectItem value="ACTIVE">
                    {t("agents.deployment.edit.statusActive", "Activo")}
                  </SelectItem>
                  <SelectItem value="DEPLOYED">
                    {t("agents.deployment.edit.statusDeployed", "Desplegado")}
                  </SelectItem>
                  <SelectItem value="FAILED">
                    {t("agents.deployment.edit.statusFailed", "Fallido")}
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Agent URL - Para agentes conversacionales */}
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="agentUrl">
                {t("agents.deployment.edit.agentUrl", "Agent URL")}
              </Label>
              <Input
                id="agentUrl"
                type="url"
                value={formData.agentUrl}
                onChange={(e) => setFormData({ ...formData, agentUrl: e.target.value })}
                placeholder={t("agents.deployment.edit.agentUrlPlaceholder", "URL del agente conversacional (opcional)")}
              />
              <p className="text-xs text-muted-foreground">
                {t("agents.deployment.edit.agentUrlInfo", "URL del agente si es conversacional")}
              </p>
            </div>

            {/* Configuration - Ocupa 2 columnas */}
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="configuration">
                {t("agents.deployment.edit.configuration", "Configuración (JSON)")}
              </Label>
              <Textarea
                id="configuration"
                value={formData.configuration}
                onChange={(e) => setFormData({ ...formData, configuration: e.target.value })}
                placeholder={t("agents.deployment.edit.configurationPlaceholder", "Ingrese la configuración en formato JSON (opcional)")}
                rows={6}
              />
            </div>

            {/* Notes - Ocupa 2 columnas */}
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="notes">
                {t("agents.deployment.edit.notes", "Notas")}
              </Label>
              <Textarea
                id="notes"
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                placeholder={t("agents.deployment.edit.notesPlaceholder", "Ingrese notas adicionales (opcional)")}
                rows={3}
              />
            </div>
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
