"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { ArrowLeft, CheckCircle2, Save } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

interface ApprovalFormData {
  agentUuid?: string;
  approvalType: string;
  approvalStatus: string;
  requestReason: string;
  requestDetails: string;
  approvalCriteria: string;
}

interface Agent {
  id: number;
  uuid: string;
  name: string;
  status: string;
}

export default function ApprovalCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [formData, setFormData] = useState<ApprovalFormData>({
    agentUuid: "",
    approvalType: "DEPLOYMENT",
    approvalStatus: "PENDING",
    requestReason: "",
    requestDetails: "",
    approvalCriteria: "",
  });

  useEffect(() => {
    loadActiveAgents();
  }, []);

  const loadActiveAgents = async () => {
    setLoadingAgents(true);
    try {
      const response = await fetch("/api/governance/agents/registry");
      if (response.ok) {
        const data = await response.json();
        // Filtrar solo agentes activos
        const activeAgents = (data.items || data || []).filter(
          (agent: Agent) => agent.status === "ACTIVE"
        );
        setAgents(activeAgents);
      } else {
        // Mock data para desarrollo
        setAgents([
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ]);
      }
    } catch (error) {
      console.error("Error loading agents:", error);
      // Mock data para desarrollo
      setAgents([
        { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
        { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
        { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
      ]);
    } finally {
      setLoadingAgents(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      // Asegurar que el estado siempre sea PENDING
      const submissionData = {
        ...formData,
        approvalStatus: "PENDING",
      };

      const response = await fetch("/api/governance/agents/approval", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();

        // Lanzar proceso BPMN después de crear la aprobación
        if (data.id) {
          try {
            const bpmnResponse = await fetch(`/api/governance/agents/approval/${data.id}/start-process`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processDefinitionKey: "agent-approval-process",
                businessKey: `approval-${data.id}`,
                variables: {
                  approvalId: data.id,
                  agentUuid: formData.agentUuid,
                  approvalType: formData.approvalType,
                },
              }),
            });

            if (!bpmnResponse.ok) {
              console.warn("No se pudo iniciar el proceso BPMN, pero la aprobación se creó correctamente");
            }
          } catch (bpmnError) {
            console.error("Error al iniciar proceso BPMN:", bpmnError);
            // No bloqueamos la creación si falla el BPMN
          }

          // Redirigir al detalle de la aprobación creada
          router.push(`/governance/agents/approval/${data.id}`);
        } else {
          router.push("/governance/agents/approval/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.approval.create.error", "Error al crear la aprobación: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al crear aprobación:", error);
      alert(t("agents.approval.create.error", "Error al crear la aprobación"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.push("/governance/agents/approval/overview");
  };

  if (!mounted) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <CheckCircle2 className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4 p-6">
      {/* Header con botones de acción en la parte superior derecha */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.approval.create.title", "Crear Nueva Aprobación")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.approval.create.subtitle", "Complete el formulario para crear una nueva aprobación")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={handleCancel}
            disabled={isSubmitting}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.cancel", "Cancelar")}
          </Button>
          <Button
            onClick={handleSubmit}
            variant="primary"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.approval.create.requestButton", "Solicitar Aprobación")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSubmit}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("agents.approval.create.formTitle", "Información de la Aprobación")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            {/* Grid compacto de campos */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {/* Agent UUID */}
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.approval.create.agentUuid", "Agente")} *
                </Label>
                <select
                  id="agentUuid"
                  value={formData.agentUuid}
                  onChange={(e) => setFormData({ ...formData, agentUuid: e.target.value })}
                  required
                  disabled={loadingAgents}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary disabled:bg-muted disabled:text-muted-foreground"
                >
                  <option value="">
                    {loadingAgents
                      ? t("common.loading", "Cargando agentes...")
                      : t("agents.approval.create.agentUuidPlaceholder", "Seleccione un agente activo")}
                  </option>
                  {agents.map((agent) => (
                    <option key={agent.id} value={agent.uuid || agent.id.toString()}>
                      {agent.name} ({agent.uuid || `ID: ${agent.id}`})
                    </option>
                  ))}
                </select>
              </div>

              {/* Approval Type */}
              <div className="space-y-1">
                <Label htmlFor="approvalType" className="text-sm">
                  {t("agents.approval.create.approvalType", "Tipo de Aprobación")} *
                </Label>
                <select
                  id="approvalType"
                  value={formData.approvalType}
                  onChange={(e) => setFormData({ ...formData, approvalType: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="DEPLOYMENT">
                    {t("agents.approval.create.typeDeployment", "Despliegue")}
                  </option>
                  <option value="VERSION_UPDATE">
                    {t("agents.approval.create.typeVersionUpdate", "Actualización de Versión")}
                  </option>
                  <option value="CONFIGURATION_CHANGE">
                    {t("agents.approval.create.typeConfigurationChange", "Cambio de Configuración")}
                  </option>
                  <option value="PRODUCTION_ACCESS">
                    {t("agents.approval.create.typeProductionAccess", "Acceso a Producción")}
                  </option>
                </select>
              </div>

              {/* Approval Status - Bloqueado a PENDING */}
              <div className="space-y-1">
                <Label htmlFor="approvalStatus" className="text-sm">
                  {t("agents.approval.create.approvalStatus", "Estado de Aprobación")} *
                </Label>
                <select
                  id="approvalStatus"
                  value="PENDING"
                  disabled
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-muted text-muted-foreground cursor-not-allowed"
                >
                  <option value="PENDING">
                    {t("agents.approval.create.statusPending", "Pendiente")}
                  </option>
                </select>
                <p className="text-xs text-muted-foreground">
                  {t("agents.approval.create.statusLockedInfo", "El estado se establecerá automáticamente como Pendiente al iniciar el proceso de aprobación")}
                </p>
              </div>

              {/* Request Reason */}
              <div className="space-y-1">
                <Label htmlFor="requestReason" className="text-sm">
                  {t("agents.approval.create.requestReason", "Razón de Solicitud")} *
                </Label>
                <Input
                  id="requestReason"
                  value={formData.requestReason}
                  onChange={(e) => setFormData({ ...formData, requestReason: e.target.value })}
                  placeholder={t("agents.approval.create.requestReasonPlaceholder", "Ingrese la razón de la solicitud")}
                  required
                  className="text-sm"
                />
              </div>
            </div>

            {/* Request Details */}
            <div className="space-y-1">
              <Label htmlFor="requestDetails" className="text-sm">
                {t("agents.approval.create.requestDetails", "Detalles de Solicitud")} *
              </Label>
              <Textarea
                id="requestDetails"
                value={formData.requestDetails}
                onChange={(e) => setFormData({ ...formData, requestDetails: e.target.value })}
                placeholder={t("agents.approval.create.requestDetailsPlaceholder", "Ingrese los detalles de la solicitud")}
                rows={3}
                required
                className="text-sm"
              />
            </div>

            {/* Approval Criteria */}
            <div className="space-y-1">
              <Label htmlFor="approvalCriteria" className="text-sm">
                {t("agents.approval.create.approvalCriteria", "Criterios de Aprobación")} *
              </Label>
              <Textarea
                id="approvalCriteria"
                value={formData.approvalCriteria}
                onChange={(e) => setFormData({ ...formData, approvalCriteria: e.target.value })}
                placeholder={t("agents.approval.create.approvalCriteriaPlaceholder", "Ingrese los criterios de aprobación")}
                rows={3}
                required
                className="text-sm"
              />
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
