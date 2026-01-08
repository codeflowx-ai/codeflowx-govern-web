"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Shield, Save, Edit } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams, useSearchParams } from "next/navigation";

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
  createdBy?: string;
  createdAt?: string;
  updatedBy?: string;
  updatedAt?: string;
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

export default function ComplianceDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const complianceId = params?.id as string;
  const isEditMode = searchParams?.get("edit") === "true";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<ComplianceFormData>({
    agentUuid: "",
    complianceType: "REGULATORY",
    complianceStatus: "PENDING",
    complianceScore: "",
    riskLevel: "MEDIUM",
    assessmentFrequency: "MONTHLY",
    assessorId: "",
    assessorName: "",
    assessmentMethod: "MANUAL",
    notes: "",
  });

  useEffect(() => {
    if (complianceId) {
      loadComplianceData();
    }
    loadActiveAgents();
    loadUsers();
  }, [complianceId]);

  useEffect(() => {
    if (typeof window !== "undefined") {
      const editParam = new URLSearchParams(window.location.search).get("edit");
      if (editParam === "true" && !isEditMode) {
        // Force re-render when edit mode changes
        window.location.reload();
      }
    }
  }, [isEditMode]);

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

  const loadComplianceData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/compliance/${complianceId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || data.agent?.uuid || "",
          complianceType: data.complianceType || "REGULATORY",
          complianceStatus: data.complianceStatus || "PENDING",
          complianceScore: data.complianceScore?.toString() || "",
          riskLevel: data.riskLevel || "MEDIUM",
          assessmentFrequency: data.assessmentFrequency || "MONTHLY",
          assessorId: data.assessorId || "",
          assessorName: data.assessorName || "",
          assessmentMethod: data.assessmentMethod || "",
          notes: data.notes || "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
      } else {
        // Mock data para desarrollo
        const mockData = {
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          complianceType: "REGULATORY",
          complianceStatus: "COMPLIANT",
          complianceScore: "95",
          riskLevel: "LOW",
          assessmentFrequency: "MONTHLY",
          assessorId: "2",
          assessorName: "compliance",
          assessmentMethod: "HYBRID",
          notes: "El agente cumple con todos los requisitos regulatorios establecidos. Se recomienda realizar una evaluación trimestral para mantener el cumplimiento.",
          createdBy: "compliance@codeflowx.com",
          createdAt: "2024-01-15T10:00:00",
          updatedBy: "auditor@codeflowx.com",
          updatedAt: "2024-01-20T14:30:00",
        };
        setFormData({
          agentUuid: mockData.agentUuid,
          complianceType: mockData.complianceType,
          complianceStatus: mockData.complianceStatus,
          complianceScore: mockData.complianceScore,
          riskLevel: mockData.riskLevel,
          assessmentFrequency: mockData.assessmentFrequency,
          assessorId: mockData.assessorId,
          assessorName: mockData.assessorName,
          assessmentMethod: mockData.assessmentMethod,
          notes: mockData.notes,
          createdBy: mockData.createdBy,
          createdAt: mockData.createdAt,
          updatedBy: mockData.updatedBy,
          updatedAt: mockData.updatedAt,
        });
      }
    } catch (error) {
      console.error("Error loading compliance:", error);
      // Mock data en caso de error
      const mockData = {
        agentUuid: "550e8400-e29b-41d4-a716-446655440000",
        complianceType: "REGULATORY",
        complianceStatus: "COMPLIANT",
        complianceScore: "95",
        riskLevel: "LOW",
        assessmentFrequency: "MONTHLY",
        assessorId: "2",
        assessorName: "compliance",
        assessmentMethod: "HYBRID",
        notes: "El agente cumple con todos los requisitos regulatorios establecidos.",
        createdBy: "compliance@codeflowx.com",
        createdAt: "2024-01-15T10:00:00",
        updatedBy: "auditor@codeflowx.com",
        updatedAt: "2024-01-20T14:30:00",
      };
      setFormData({
        agentUuid: mockData.agentUuid,
        complianceType: mockData.complianceType,
        complianceStatus: mockData.complianceStatus,
        complianceScore: mockData.complianceScore,
        riskLevel: mockData.riskLevel,
        assessmentFrequency: mockData.assessmentFrequency,
        assessorId: mockData.assessorId,
        assessorName: mockData.assessorName,
        assessmentMethod: mockData.assessmentMethod,
        notes: mockData.notes,
        createdBy: mockData.createdBy,
        createdAt: mockData.createdAt,
        updatedBy: mockData.updatedBy,
        updatedAt: mockData.updatedAt,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e?: React.FormEvent | React.MouseEvent) => {
    if (e) {
      e.preventDefault();
    }
    setIsSubmitting(true);

    try {
      // Obtener usuario actual para actualizar updatedBy
      const currentUser = await getCurrentUser();

      const submissionData = {
        ...formData,
        updatedBy: currentUser,
        // Mantener createdBy original (no debe cambiar)
        createdBy: formData.createdBy,
      };

      const response = await fetch(`/api/governance/agents/compliance/${complianceId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        router.push(`/governance/agents/compliance/${complianceId}`);
      } else {
        const error = await response.json();
        alert(t("agents.compliance.edit.error", "Error al actualizar el cumplimiento: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al actualizar cumplimiento:", error);
      alert(t("agents.compliance.edit.error", "Error al actualizar el cumplimiento"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    if (isEditMode) {
      router.push(`/governance/agents/compliance/${complianceId}`);
    } else {
      router.push("/governance/agents/compliance/overview");
    }
  };

  const handleEdit = () => {
    router.push(`/governance/agents/compliance/${complianceId}?edit=true`);
  };

  if (!mounted || loading) {
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
              {isEditMode
                ? t("agents.compliance.edit.title", "Editar Cumplimiento")
                : t("agents.compliance.detail.title", "Detalles de Cumplimiento")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {isEditMode
              ? t("agents.compliance.edit.subtitle", "Modifique los campos necesarios para actualizar el cumplimiento")
              : t("agents.compliance.detail.subtitle", "Información detallada del registro de cumplimiento")}
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
            {isEditMode ? t("common.cancel", "Cancelar") : t("common.back", "Volver")}
          </Button>
          {!isEditMode && (
            <Button
              variant="outline"
              size="sm"
              onClick={handleEdit}
            >
              <Edit className="h-4 w-4 mr-2" />
              {t("common.edit", "Editar")}
            </Button>
          )}
          {isEditMode && (
            <Button
              type="button"
              onClick={handleSubmit}
              variant="primary"
              disabled={isSubmitting}
            >
              <Save className="h-4 w-4 mr-2" />
              {isSubmitting
                ? t("common.saving", "Guardando...")
                : t("agents.compliance.edit.saveButton", "Guardar Cambios")}
            </Button>
          )}
        </div>
      </div>

      {/* Formulario o Vista */}
      <form onSubmit={handleSubmit}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {isEditMode
                ? t("agents.compliance.edit.formTitle", "Editar Información del Cumplimiento")
                : t("agents.compliance.detail.formTitle", "Información del Cumplimiento")}
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
                {isEditMode ? (
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
                ) : (
                  <p className="text-base font-semibold">
                    {agents.find((a) => a.uuid === formData.agentUuid)?.name || formData.agentUuid || "-"}
                  </p>
                )}
              </div>

              {/* Compliance Type */}
              <div className="space-y-1">
                <Label htmlFor="complianceType" className="text-sm">
                  {t("agents.compliance.create.complianceType", "Tipo de Cumplimiento")} *
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base font-semibold">{formData.complianceType}</p>
                )}
              </div>

              {/* Compliance Status */}
              <div className="space-y-1">
                <Label htmlFor="complianceStatus" className="text-sm">
                  {t("agents.compliance.create.complianceStatus", "Estado de Cumplimiento")} *
                </Label>
                {isEditMode ? (
                  <select
                    id="complianceStatus"
                    value={formData.complianceStatus}
                    onChange={(e) => setFormData({ ...formData, complianceStatus: e.target.value })}
                    required
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                    <option value="COMPLIANT">{t("agents.compliance.create.statusCompliant", "Cumpliente")}</option>
                    <option value="NON_COMPLIANT">{t("agents.compliance.create.statusNonCompliant", "No Cumpliente")}</option>
                    <option value="UNDER_REVIEW">{t("agents.compliance.create.statusUnderReview", "En Revisión")}</option>
                    <option value="DRAFT">{t("agents.compliance.create.statusDraft", "Borrador")}</option>
                  </select>
                ) : (
                  <p className="text-base">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        formData.complianceStatus === "COMPLIANT"
                          ? "bg-green-500/20 text-green-500"
                          : formData.complianceStatus === "PENDING" || formData.complianceStatus === "UNDER_REVIEW"
                          ? "bg-yellow-500/20 text-yellow-500"
                          : formData.complianceStatus === "DRAFT"
                          ? "bg-gray-500/20 text-gray-500"
                          : "bg-red-500/20 text-red-500"
                      }`}
                    >
                      {formData.complianceStatus}
                    </span>
                  </p>
                )}
              </div>

              {/* Risk Level */}
              <div className="space-y-1">
                <Label htmlFor="riskLevel" className="text-sm">
                  {t("agents.compliance.create.riskLevel", "Nivel de Riesgo")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        formData.riskLevel === "LOW"
                          ? "bg-green-500/20 text-green-500"
                          : formData.riskLevel === "MEDIUM"
                          ? "bg-yellow-500/20 text-yellow-500"
                          : formData.riskLevel === "HIGH"
                          ? "bg-orange-500/20 text-orange-500"
                          : "bg-red-500/20 text-red-500"
                      }`}
                    >
                      {formData.riskLevel || "-"}
                    </span>
                  </p>
                )}
              </div>

              {/* Compliance Score */}
              <div className="space-y-1">
                <Label htmlFor="complianceScore" className="text-sm">
                  {t("agents.compliance.create.complianceScore", "Puntuación de Cumplimiento")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base font-bold">{formData.complianceScore || "-"}%</p>
                )}
              </div>

              {/* Assessment Frequency */}
              <div className="space-y-1">
                <Label htmlFor="assessmentFrequency" className="text-sm">
                  {t("agents.compliance.create.assessmentFrequency", "Frecuencia de Evaluación")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">{formData.assessmentFrequency || "-"}</p>
                )}
              </div>

              {/* Assessor ID */}
              <div className="space-y-1">
                <Label htmlFor="assessorId" className="text-sm">
                  {t("agents.compliance.create.assessorId", "ID del Evaluador")}
                </Label>
                {isEditMode ? (
                  <Input
                    id="assessorId"
                    value={formData.assessorId}
                    onChange={(e) => setFormData({ ...formData, assessorId: e.target.value })}
                    placeholder={t("agents.compliance.create.assessorIdPlaceholder", "Ingrese el ID del evaluador")}
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">{formData.assessorId || "-"}</p>
                )}
              </div>

              {/* Assessor Name */}
              <div className="space-y-1">
                <Label htmlFor="assessorName" className="text-sm">
                  {t("agents.compliance.create.assessorName", "Evaluador")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">
                    {users.find((u) => u.username === formData.assessorName || u.email === formData.assessorName)?.name || formData.assessorName || "-"}
                  </p>
                )}
              </div>

              {/* Assessment Method */}
              <div className="space-y-1">
                <Label htmlFor="assessmentMethod" className="text-sm">
                  {t("agents.compliance.create.assessmentMethod", "Método de Evaluación")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">
                    {formData.assessmentMethod === "MANUAL"
                      ? t("agents.compliance.create.methodManual", "Manual")
                      : formData.assessmentMethod === "AUTOMATIC"
                      ? t("agents.compliance.create.methodAutomatic", "Automático")
                      : formData.assessmentMethod === "HYBRID"
                      ? t("agents.compliance.create.methodHybrid", "Híbrido (Manual + Automático)")
                      : formData.assessmentMethod === "CONTINUOUS"
                      ? t("agents.compliance.create.methodContinuous", "Continuo")
                      : formData.assessmentMethod || "-"}
                  </p>
                )}
              </div>
            </div>

            {/* Notes */}
            <div className="space-y-1">
              <Label htmlFor="notes" className="text-sm">
                {t("agents.compliance.create.notes", "Notas")}
              </Label>
              {isEditMode ? (
                <Textarea
                  id="notes"
                  value={formData.notes}
                  onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                  placeholder={t("agents.compliance.create.notesPlaceholder", "Ingrese notas adicionales sobre el cumplimiento")}
                  rows={4}
                  className="text-sm"
                />
              ) : (
                <p className="text-base whitespace-pre-wrap">{formData.notes || "-"}</p>
              )}
            </div>

            {/* Sección de Auditoría */}
            <div className="pt-4 border-t space-y-3">
              <h3 className="text-sm font-semibold text-foreground">
                {t("agents.compliance.audit.title", "Registro de Auditoría")}
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {/* Creado por */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.compliance.audit.createdBy", "Creado por")}
                  </Label>
                  <p className="text-sm font-medium">{formData.createdBy || "-"}</p>
                </div>
                {/* Creado en */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.compliance.audit.createdAt", "Fecha de creación")}
                  </Label>
                  <p className="text-sm">
                    {formData.createdAt
                      ? new Date(formData.createdAt).toLocaleString("es-ES", {
                          year: "numeric",
                          month: "2-digit",
                          day: "2-digit",
                          hour: "2-digit",
                          minute: "2-digit",
                        })
                      : "-"}
                  </p>
                </div>
                {/* Actualizado por */}
                {formData.updatedBy && (
                  <>
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.compliance.audit.updatedBy", "Última modificación por")}
                      </Label>
                      <p className="text-sm font-medium">{formData.updatedBy}</p>
                    </div>
                    {/* Actualizado en */}
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.compliance.audit.updatedAt", "Fecha de última modificación")}
                      </Label>
                      <p className="text-sm">
                        {formData.updatedAt
                          ? new Date(formData.updatedAt).toLocaleString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                              hour: "2-digit",
                              minute: "2-digit",
                            })
                          : "-"}
                      </p>
                    </div>
                  </>
                )}
              </div>
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
