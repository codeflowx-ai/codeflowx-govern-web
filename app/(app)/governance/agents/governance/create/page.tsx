"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Shield, Save } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

interface GovernanceFormData {
  agentUuid?: string;
  policyType: string;
  policyName: string;
  description: string;
  policyContent: string;
  enforcementLevel: string;
  status: string;
  priority: string;
  scope: string;
  effectiveFrom: string;
  effectiveUntil: string;
}

interface Agent {
  id: number;
  uuid: string;
  name: string;
  status: string;
}

export default function GovernanceCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [formData, setFormData] = useState<GovernanceFormData>({
    agentUuid: "",
    policyType: "SECURITY",
    policyName: "",
    description: "",
    policyContent: "",
    enforcementLevel: "MANDATORY",
    status: "DRAFT", // Por defecto borrador
    priority: "MEDIUM",
    scope: "",
    effectiveFrom: new Date().toISOString().split("T")[0],
    effectiveUntil: "",
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
        const activeAgents = (data.items || data || []).filter(
          (agent: Agent) => agent.status === "ACTIVE"
        );
        setAgents(activeAgents);
      } else {
        setAgents([
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ]);
      }
    } catch (error) {
      console.error("Error loading agents:", error);
      setAgents([
        { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
        { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
        { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
      ]);
    } finally {
      setLoadingAgents(false);
    }
  };

  const getCurrentUser = async (): Promise<string> => {
    try {
      const userResponse = await fetch("/api/auth/me");
      if (userResponse.ok) {
        const userData = await userResponse.json();
        return userData.username || userData.email || userData.id || "system";
      }
    } catch (userError) {
      console.warn("No se pudo obtener el usuario actual:", userError);
    }
    // Intentar obtener de localStorage o sessionStorage
    if (typeof window !== "undefined") {
      const storedUserName = localStorage.getItem("currentUserName");
      const storedUser = localStorage.getItem("user") || sessionStorage.getItem("user");
      if (storedUserName) {
        return storedUserName;
      } else if (storedUser) {
        try {
          const user = JSON.parse(storedUser);
          return user.username || user.email || user.id || "system";
        } catch (e) {
          return storedUser;
        }
      }
    }
    return "system";
  };

  const handleSaveDraft = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const createdBy = await getCurrentUser();

      const submissionData = {
        ...formData,
        status: "DRAFT", // Forzar estado borrador
        createdBy: createdBy,
      };

      const response = await fetch("/api/governance/agents/governance", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.id) {
          router.push(`/governance/agents/governance/${data.id}`);
        } else {
          router.push("/governance/agents/governance/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.governance.create.error", "Error al guardar el borrador: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al guardar borrador:", error);
      alert(t("agents.governance.create.error", "Error al guardar el borrador"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSubmitForApproval = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const createdBy = await getCurrentUser();

      const submissionData = {
        ...formData,
        status: "PENDING", // Forzar estado pendiente
        createdBy: createdBy,
      };

      const response = await fetch("/api/governance/agents/governance", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();

        // Lanzar proceso BPMN después de crear la política
        if (data.id) {
          try {
            const bpmnResponse = await fetch(`/api/governance/agents/governance/${data.id}/start-process`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processDefinitionKey: "governance-policy-approval-process",
                businessKey: `governance-${data.id}`,
                variables: {
                  governanceId: data.id,
                  agentUuid: formData.agentUuid,
                  policyType: formData.policyType,
                  policyName: formData.policyName,
                },
              }),
            });

            if (!bpmnResponse.ok) {
              console.warn("No se pudo iniciar el proceso BPMN, pero la política se creó correctamente");
            }
          } catch (bpmnError) {
            console.error("Error al iniciar proceso BPMN:", bpmnError);
            // No bloqueamos la creación si falla el BPMN
          }

          router.push(`/governance/agents/governance/${data.id}`);
        } else {
          router.push("/governance/agents/governance/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.governance.create.error", "Error al enviar para aprobación: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al enviar para aprobación:", error);
      alert(t("agents.governance.create.error", "Error al enviar para aprobación"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.push("/governance/agents/governance/overview");
  };

  if (!mounted) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Shield className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.governance.create.title", "Crear Nueva Política de Gobierno")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.governance.create.subtitle", "Complete el formulario para crear una nueva política de gobierno")}
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
            onClick={handleSaveDraft}
            variant="outline"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.governance.create.saveDraft", "Guardar como Borrador")}
          </Button>
          <Button
            onClick={handleSubmitForApproval}
            variant="primary"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.governance.create.submitForApproval", "Enviar para Aprobación")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSubmitForApproval}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("agents.governance.create.formTitle", "Información de la Política de Gobierno")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            {/* Grid compacto de campos */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {/* Agent UUID */}
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.governance.create.agentUuid", "Agente")} *
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
                      : t("agents.governance.create.agentUuidPlaceholder", "Seleccione un agente activo")}
                  </option>
                  {agents.map((agent) => (
                    <option key={agent.id} value={agent.uuid || agent.id.toString()}>
                      {agent.name} ({agent.uuid || `ID: ${agent.id}`})
                    </option>
                  ))}
                </select>
              </div>

              {/* Policy Type */}
              <div className="space-y-1">
                <Label htmlFor="policyType" className="text-sm">
                  {t("agents.governance.create.policyType", "Tipo de Política")} *
                </Label>
                <select
                  id="policyType"
                  value={formData.policyType}
                  onChange={(e) => setFormData({ ...formData, policyType: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="SECURITY">{t("agents.governance.create.typeSecurity", "Seguridad")}</option>
                  <option value="DATA_PRIVACY">{t("agents.governance.create.typeDataPrivacy", "Privacidad de Datos")}</option>
                  <option value="ETHICAL">{t("agents.governance.create.typeEthical", "Ético")}</option>
                  <option value="OPERATIONAL">{t("agents.governance.create.typeOperational", "Operacional")}</option>
                </select>
              </div>

              {/* Policy Name */}
              <div className="space-y-1">
                <Label htmlFor="policyName" className="text-sm">
                  {t("agents.governance.create.policyName", "Nombre de la Política")} *
                </Label>
                <Input
                  id="policyName"
                  value={formData.policyName}
                  onChange={(e) => setFormData({ ...formData, policyName: e.target.value })}
                  placeholder={t("agents.governance.create.policyNamePlaceholder", "Ingrese el nombre de la política")}
                  required
                  className="text-sm"
                />
              </div>

              {/* Enforcement Level */}
              <div className="space-y-1">
                <Label htmlFor="enforcementLevel" className="text-sm">
                  {t("agents.governance.create.enforcementLevel", "Nivel de Cumplimiento")} *
                </Label>
                <select
                  id="enforcementLevel"
                  value={formData.enforcementLevel}
                  onChange={(e) => setFormData({ ...formData, enforcementLevel: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="MANDATORY">{t("agents.governance.create.enforcementMandatory", "Obligatorio")}</option>
                  <option value="RECOMMENDED">{t("agents.governance.create.enforcementRecommended", "Recomendado")}</option>
                  <option value="OPTIONAL">{t("agents.governance.create.enforcementOptional", "Opcional")}</option>
                </select>
              </div>

              {/* Status - Bloqueado, se establece automáticamente según la acción */}
              <div className="space-y-1">
                <Label htmlFor="status" className="text-sm">
                  {t("agents.governance.create.status", "Estado")} *
                </Label>
                <select
                  id="status"
                  value={formData.status}
                  disabled
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-muted text-muted-foreground cursor-not-allowed"
                >
                  <option value="DRAFT">{t("agents.governance.create.statusDraft", "Borrador")}</option>
                  <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                </select>
                <p className="text-xs text-muted-foreground">
                  {t("agents.governance.create.statusInfo", "El estado se establecerá automáticamente: 'Borrador' al guardar como borrador, 'Pendiente' al enviar para aprobación")}
                </p>
              </div>

              {/* Priority */}
              <div className="space-y-1">
                <Label htmlFor="priority" className="text-sm">
                  {t("agents.governance.create.priority", "Prioridad")}
                </Label>
                <select
                  id="priority"
                  value={formData.priority}
                  onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="HIGH">{t("common.high", "Alta")}</option>
                  <option value="MEDIUM">{t("common.medium", "Media")}</option>
                  <option value="LOW">{t("common.low", "Baja")}</option>
                </select>
              </div>

              {/* Scope */}
              <div className="space-y-1">
                <Label htmlFor="scope" className="text-sm">
                  {t("agents.governance.create.scope", "Alcance")}
                </Label>
                <Input
                  id="scope"
                  value={formData.scope}
                  onChange={(e) => setFormData({ ...formData, scope: e.target.value })}
                  placeholder={t("agents.governance.create.scopePlaceholder", "Ingrese el alcance de la política")}
                  className="text-sm"
                />
              </div>

              {/* Effective From */}
              <div className="space-y-1">
                <Label htmlFor="effectiveFrom" className="text-sm">
                  {t("agents.governance.create.effectiveFrom", "Vigente Desde")} *
                </Label>
                <Input
                  id="effectiveFrom"
                  type="date"
                  value={formData.effectiveFrom}
                  onChange={(e) => setFormData({ ...formData, effectiveFrom: e.target.value })}
                  required
                  className="text-sm"
                />
              </div>

              {/* Effective Until */}
              <div className="space-y-1">
                <Label htmlFor="effectiveUntil" className="text-sm">
                  {t("agents.governance.create.effectiveUntil", "Vigente Hasta")}
                </Label>
                <Input
                  id="effectiveUntil"
                  type="date"
                  value={formData.effectiveUntil}
                  onChange={(e) => setFormData({ ...formData, effectiveUntil: e.target.value })}
                  className="text-sm"
                />
              </div>
            </div>

            {/* Description */}
            <div className="space-y-1">
              <Label htmlFor="description" className="text-sm">
                {t("agents.governance.create.description", "Descripción")}
              </Label>
              <Textarea
                id="description"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                placeholder={t("agents.governance.create.descriptionPlaceholder", "Ingrese la descripción de la política")}
                rows={3}
                className="text-sm"
              />
            </div>

            {/* Policy Content */}
            <div className="space-y-1">
              <Label htmlFor="policyContent" className="text-sm">
                {t("agents.governance.create.policyContent", "Contenido de la Política")} *
              </Label>
              <Textarea
                id="policyContent"
                value={formData.policyContent}
                onChange={(e) => setFormData({ ...formData, policyContent: e.target.value })}
                placeholder={t("agents.governance.create.policyContentPlaceholder", "Ingrese el contenido completo de la política")}
                rows={5}
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
