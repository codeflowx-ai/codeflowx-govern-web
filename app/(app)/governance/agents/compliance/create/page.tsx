"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, Save, Shield } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface ComplianceFormData {
  agentUuid?: string;
  complianceType: string;
  complianceStatus: string;
  complianceScore: string;
  riskLevel: string;
  assessmentFrequency: string;
  assessorId: string;
  assessorName: string;
  assessmentMethod: string;
  notes: string;
}

interface Agent {
  id: number;
  uuid: string;
  name: string;
  status: string;
}

interface User {
  id: string;
  username: string;
  email?: string;
  name?: string;
}

export default function ComplianceCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<ComplianceFormData>({
    agentUuid: "",
    complianceType: "REGULATORY",
    complianceStatus: "DRAFT", // Por defecto borrador
    complianceScore: "",
    riskLevel: "MEDIUM",
    assessmentFrequency: "MONTHLY",
    assessorId: "",
    assessorName: "",
    assessmentMethod: "MANUAL",
    notes: "",
  });

  useEffect(() => {
    loadActiveAgents();
    loadUsers();
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

  const loadUsers = async () => {
    setLoadingUsers(true);
    try {
      const response = await fetch("/api/bpmn/users");
      if (response.ok) {
        const data = await response.json();
        setUsers(data.users || data || []);
      } else {
        // Mock de usuarios
        setUsers([
          { id: "1", username: "admin", email: "admin@codeflowx.com", name: "Administrador" },
          { id: "2", username: "compliance", email: "compliance@codeflowx.com", name: "María González" },
          { id: "3", username: "auditor", email: "auditor@codeflowx.com", name: "Juan Pérez" },
          { id: "4", username: "assessor", email: "assessor@codeflowx.com", name: "Ana Martínez" },
        ]);
      }
    } catch (error) {
      console.error("Error loading users:", error);
      // Mock de usuarios en caso de error
      setUsers([
        { id: "1", username: "admin", email: "admin@codeflowx.com", name: "Administrador" },
        { id: "2", username: "compliance", email: "compliance@codeflowx.com", name: "María González" },
        { id: "3", username: "auditor", email: "auditor@codeflowx.com", name: "Juan Pérez" },
        { id: "4", username: "assessor", email: "assessor@codeflowx.com", name: "Ana Martínez" },
      ]);
    } finally {
      setLoadingUsers(false);
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

  const handleSaveDraft = async (e?: React.FormEvent | React.MouseEvent) => {
    if (e) {
      e.preventDefault();
    }
    setIsSubmitting(true);

    try {
      const createdBy = await getCurrentUser();

      const submissionData = {
        ...formData,
        complianceStatus: "DRAFT", // Forzar estado borrador
        createdBy: createdBy,
      };

      const response = await fetch("/api/governance/agents/compliance", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.id) {
          router.push(`/governance/agents/compliance/${data.id}`);
        } else {
          router.push("/governance/agents/compliance/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.compliance.create.error", "Error al guardar el borrador: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al guardar borrador:", error);
      alert(t("agents.compliance.create.error", "Error al guardar el borrador"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSubmitForApproval = async (e?: React.FormEvent | React.MouseEvent) => {
    if (e) {
      e.preventDefault();
    }
    setIsSubmitting(true);

    try {
      const createdBy = await getCurrentUser();

      const submissionData = {
        ...formData,
        complianceStatus: "PENDING", // Forzar estado pendiente
        createdBy: createdBy,
      };

      const response = await fetch("/api/governance/agents/compliance", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();

        // Lanzar proceso BPMN después de crear el cumplimiento
        if (data.id) {
          try {
            const bpmnResponse = await fetch(`/api/governance/agents/compliance/${data.id}/start-process`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processDefinitionKey: "compliance-assessment-process",
                businessKey: `compliance-${data.id}`,
                variables: {
                  complianceId: data.id,
                  agentUuid: formData.agentUuid,
                  complianceType: formData.complianceType,
                  riskLevel: formData.riskLevel,
                },
              }),
            });

            if (!bpmnResponse.ok) {
              console.warn("No se pudo iniciar el proceso BPMN, pero el cumplimiento se creó correctamente");
            }
          } catch (bpmnError) {
            console.error("Error al iniciar proceso BPMN:", bpmnError);
            // No bloqueamos la creación si falla el BPMN
          }

          router.push(`/governance/agents/compliance/${data.id}`);
        } else {
          router.push("/governance/agents/compliance/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.compliance.create.error", "Error al enviar para aprobación: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al enviar para aprobación:", error);
      alert(t("agents.compliance.create.error", "Error al enviar para aprobación"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.push("/governance/agents/compliance/overview");
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
              {t("agents.compliance.create.title", "Crear Nuevo Cumplimiento")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.compliance.create.subtitle", "Complete el formulario para crear un nuevo registro de cumplimiento")}
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
            type="button"
            onClick={handleSaveDraft}
            variant="outline"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.compliance.create.saveDraft", "Guardar como Borrador")}
          </Button>
          <Button
            type="button"
            onClick={handleSubmitForApproval}
            variant="primary"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.compliance.create.submitForApproval", "Enviar para Evaluación")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSubmitForApproval}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("agents.compliance.create.formTitle", "Información del Cumplimiento")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            {/* Grid compacto de campos */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {/* Agent UUID */}
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.compliance.create.agentUuid", "Agente")} *
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
                      : t("agents.compliance.create.agentUuidPlaceholder", "Seleccione un agente activo")}
                  </option>
                  {agents.map((agent) => (
                    <option key={agent.id} value={agent.uuid || agent.id.toString()}>
                      {agent.name} ({agent.uuid || `ID: ${agent.id}`})
                    </option>
                  ))}
                </select>
              </div>

              {/* Compliance Type */}
              <div className="space-y-1">
                <Label htmlFor="complianceType" className="text-sm">
                  {t("agents.compliance.create.complianceType", "Tipo de Cumplimiento")} *
                </Label>
                <select
                  id="complianceType"
                  value={formData.complianceType}
                  onChange={(e) => setFormData({ ...formData, complianceType: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="REGULATORY">{t("agents.compliance.create.typeRegulatory", "Regulatorio")}</option>
                  <option value="INDUSTRY_STANDARD">{t("agents.compliance.create.typeIndustryStandard", "Estándar de la Industria")}</option>
                  <option value="INTERNAL_POLICY">{t("agents.compliance.create.typeInternalPolicy", "Política Interna")}</option>
                  <option value="DATA_PROTECTION">{t("agents.compliance.create.typeDataProtection", "Protección de Datos")}</option>
                </select>
              </div>

              {/* Compliance Status - Bloqueado, se establece automáticamente según la acción */}
              <div className="space-y-1">
                <Label htmlFor="complianceStatus" className="text-sm">
                  {t("agents.compliance.create.complianceStatus", "Estado de Cumplimiento")} *
                </Label>
                <select
                  id="complianceStatus"
                  value={formData.complianceStatus}
                  disabled
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-muted text-muted-foreground cursor-not-allowed"
                >
                  <option value="DRAFT">{t("agents.compliance.create.statusDraft", "Borrador")}</option>
                  <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                </select>
                <p className="text-xs text-muted-foreground">
                  {t("agents.compliance.create.statusInfo", "El estado se establecerá automáticamente: 'Borrador' al guardar como borrador, 'Pendiente' al enviar para evaluación")}
                </p>
              </div>

              {/* Risk Level */}
              <div className="space-y-1">
                <Label htmlFor="riskLevel" className="text-sm">
                  {t("agents.compliance.create.riskLevel", "Nivel de Riesgo")}
                </Label>
                <select
                  id="riskLevel"
                  value={formData.riskLevel}
                  onChange={(e) => setFormData({ ...formData, riskLevel: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="LOW">{t("common.low", "Bajo")}</option>
                  <option value="MEDIUM">{t("common.medium", "Medio")}</option>
                  <option value="HIGH">{t("common.high", "Alto")}</option>
                  <option value="CRITICAL">{t("agents.compliance.create.riskCritical", "Crítico")}</option>
                </select>
              </div>

              {/* Compliance Score */}
              <div className="space-y-1">
                <Label htmlFor="complianceScore" className="text-sm">
                  {t("agents.compliance.create.complianceScore", "Puntuación de Cumplimiento")}
                </Label>
                <Input
                  id="complianceScore"
                  type="number"
                  min="0"
                  max="100"
                  value={formData.complianceScore}
                  onChange={(e) => setFormData({ ...formData, complianceScore: e.target.value })}
                  placeholder={t("agents.compliance.create.complianceScorePlaceholder", "0-100")}
                  className="text-sm"
                />
              </div>

              {/* Assessment Frequency */}
              <div className="space-y-1">
                <Label htmlFor="assessmentFrequency" className="text-sm">
                  {t("agents.compliance.create.assessmentFrequency", "Frecuencia de Evaluación")}
                </Label>
                <select
                  id="assessmentFrequency"
                  value={formData.assessmentFrequency}
                  onChange={(e) => setFormData({ ...formData, assessmentFrequency: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="DAILY">{t("agents.compliance.create.frequencyDaily", "Diario")}</option>
                  <option value="WEEKLY">{t("agents.compliance.create.frequencyWeekly", "Semanal")}</option>
                  <option value="MONTHLY">{t("agents.compliance.create.frequencyMonthly", "Mensual")}</option>
                  <option value="QUARTERLY">{t("agents.compliance.create.frequencyQuarterly", "Trimestral")}</option>
                  <option value="YEARLY">{t("agents.compliance.create.frequencyYearly", "Anual")}</option>
                </select>
              </div>

              {/* Assessor ID */}
              <div className="space-y-1">
                <Label htmlFor="assessorId" className="text-sm">
                  {t("agents.compliance.create.assessorId", "ID del Evaluador")}
                </Label>
                <Input
                  id="assessorId"
                  value={formData.assessorId}
                  onChange={(e) => setFormData({ ...formData, assessorId: e.target.value })}
                  placeholder={t("agents.compliance.create.assessorIdPlaceholder", "Ingrese el ID del evaluador")}
                  className="text-sm"
                />
              </div>

              {/* Assessor Name */}
              <div className="space-y-1">
                <Label htmlFor="assessorName" className="text-sm">
                  {t("agents.compliance.create.assessorName", "Evaluador")}
                </Label>
                <select
                  id="assessorName"
                  value={formData.assessorName}
                  onChange={(e) => {
                    const selectedUser = users.find((u) => u.username === e.target.value || u.email === e.target.value);
                    setFormData({
                      ...formData,
                      assessorName: e.target.value,
                      assessorId: selectedUser?.id || e.target.value,
                    });
                  }}
                  disabled={loadingUsers}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary disabled:bg-muted disabled:text-muted-foreground"
                >
                  <option value="">
                    {loadingUsers
                      ? t("common.loading", "Cargando usuarios...")
                      : t("agents.compliance.create.assessorNamePlaceholder", "Seleccione un evaluador")}
                  </option>
                  {users.map((user) => (
                    <option key={user.id} value={user.username || user.email || user.id}>
                      {user.name || user.username} {user.email && `(${user.email})`}
                    </option>
                  ))}
                </select>
              </div>

              {/* Assessment Method */}
              <div className="space-y-1">
                <Label htmlFor="assessmentMethod" className="text-sm">
                  {t("agents.compliance.create.assessmentMethod", "Método de Evaluación")}
                </Label>
                <select
                  id="assessmentMethod"
                  value={formData.assessmentMethod}
                  onChange={(e) => setFormData({ ...formData, assessmentMethod: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="MANUAL">{t("agents.compliance.create.methodManual", "Manual")}</option>
                  <option value="AUTOMATIC">{t("agents.compliance.create.methodAutomatic", "Automático")}</option>
                  <option value="HYBRID">{t("agents.compliance.create.methodHybrid", "Híbrido (Manual + Automático)")}</option>
                  <option value="CONTINUOUS">{t("agents.compliance.create.methodContinuous", "Continuo")}</option>
                </select>
              </div>
            </div>

            {/* Notes */}
            <div className="space-y-1">
              <Label htmlFor="notes" className="text-sm">
                {t("agents.compliance.create.notes", "Notas")}
              </Label>
              <Textarea
                id="notes"
                value={formData.notes}
                onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                placeholder={t("agents.compliance.create.notesPlaceholder", "Ingrese notas adicionales sobre el cumplimiento")}
                rows={4}
                className="text-sm"
              />
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
