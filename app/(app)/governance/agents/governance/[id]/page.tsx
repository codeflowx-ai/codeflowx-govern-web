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
  createdBy?: string;
  createdAt?: string;
  updatedBy?: string;
  updatedAt?: string;
  approvedBy?: string;
  approvedAt?: string;
}

interface Agent {
  id: number;
  uuid: string;
  name: string;
  status: string;
}

export default function GovernanceDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const governanceId = params?.id as string;
  const isEditMode = searchParams?.get("edit") === "true";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [formData, setFormData] = useState<GovernanceFormData>({
    agentUuid: "",
    policyType: "SECURITY",
    policyName: "",
    description: "",
    policyContent: "",
    enforcementLevel: "MANDATORY",
    status: "ACTIVE",
    priority: "MEDIUM",
    scope: "",
    effectiveFrom: new Date().toISOString().split("T")[0],
    effectiveUntil: "",
  });

  useEffect(() => {
    if (governanceId) {
      loadGovernanceData();
    }
    loadActiveAgents();
  }, [governanceId]);

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
      if (storedUserName) {
        return storedUserName;
      }
      const storedUser = localStorage.getItem("user") || sessionStorage.getItem("user");
      if (storedUser) {
        try {
          const user = JSON.parse(storedUser);
          return user.username || user.email || user.id || storedUser;
        } catch (e) {
          return storedUser;
        }
      }
    }
    return "system";
  };

  const loadGovernanceData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/governance/${governanceId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || data.agent?.uuid || "",
          policyType: data.policyType || "SECURITY",
          policyName: data.policyName || "",
          description: data.description || "",
          policyContent: data.policyContent || "",
          enforcementLevel: data.enforcementLevel || "MANDATORY",
          status: data.status || "ACTIVE",
          priority: data.priority || "MEDIUM",
          scope: data.scope || "",
          effectiveFrom: data.effectiveFrom ? data.effectiveFrom.split("T")[0] : new Date().toISOString().split("T")[0],
          effectiveUntil: data.effectiveUntil ? data.effectiveUntil.split("T")[0] : "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
          approvedBy: data.approvedBy || "",
          approvedAt: data.approvedAt || "",
        });
      } else {
        // Mock data para desarrollo
        const mockData = {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          policyType: "SECURITY",
          policyName: "Política de Seguridad de Datos Sensibles",
          description: "Esta política establece los requisitos de seguridad para el manejo de datos sensibles en los agentes de IA.",
          policyContent: "1. Todos los datos sensibles deben ser encriptados en tránsito y en reposo.\n2. El acceso a datos sensibles requiere autenticación de dos factores.\n3. Se debe mantener un registro de auditoría de todos los accesos.\n4. Los datos deben ser eliminados según los plazos establecidos en la política de retención.",
          enforcementLevel: "MANDATORY",
          status: "ACTIVE",
          priority: "HIGH",
          scope: "Todos los agentes que procesan datos de clientes",
          effectiveFrom: "2024-01-15",
          effectiveUntil: "2025-01-15",
          createdBy: "admin@codeflowx.com",
          createdAt: "2024-01-15T10:30:00",
          updatedBy: "compliance@codeflowx.com",
          updatedAt: "2024-01-20T14:15:00",
          approvedBy: "governance@codeflowx.com",
          approvedAt: "2024-01-16T09:00:00",
        };
        setFormData({
          agentUuid: mockData.agentUuid,
          policyType: mockData.policyType,
          policyName: mockData.policyName,
          description: mockData.description,
          policyContent: mockData.policyContent,
          enforcementLevel: mockData.enforcementLevel,
          status: mockData.status,
          priority: mockData.priority,
          scope: mockData.scope,
          effectiveFrom: mockData.effectiveFrom,
          effectiveUntil: mockData.effectiveUntil,
          createdBy: mockData.createdBy,
          createdAt: mockData.createdAt,
          updatedBy: mockData.updatedBy,
          updatedAt: mockData.updatedAt,
          approvedBy: mockData.approvedBy,
          approvedAt: mockData.approvedAt,
        });
      }
    } catch (error) {
      console.error("Error loading governance:", error);
      // Mock data en caso de error
      const mockData = {
        agentUuid: "550e8400-e29b-41d4-a716-446655440000",
        policyType: "SECURITY",
        policyName: "Política de Seguridad de Datos Sensibles",
        description: "Esta política establece los requisitos de seguridad para el manejo de datos sensibles en los agentes de IA.",
        policyContent: "1. Todos los datos sensibles deben ser encriptados en tránsito y en reposo.\n2. El acceso a datos sensibles requiere autenticación de dos factores.\n3. Se debe mantener un registro de auditoría de todos los accesos.\n4. Los datos deben ser eliminados según los plazos establecidos en la política de retención.",
        enforcementLevel: "MANDATORY",
        status: "ACTIVE",
        priority: "HIGH",
        scope: "Todos los agentes que procesan datos de clientes",
        effectiveFrom: "2024-01-15",
        effectiveUntil: "2025-01-15",
        createdBy: "admin@codeflowx.com",
        createdAt: "2024-01-15T10:30:00",
        updatedBy: "compliance@codeflowx.com",
        updatedAt: "2024-01-20T14:15:00",
        approvedBy: "governance@codeflowx.com",
        approvedAt: "2024-01-16T09:00:00",
      };
      setFormData({
        agentUuid: mockData.agentUuid,
        policyType: mockData.policyType,
        policyName: mockData.policyName,
        description: mockData.description,
        policyContent: mockData.policyContent,
        enforcementLevel: mockData.enforcementLevel,
        status: mockData.status,
        priority: mockData.priority,
        scope: mockData.scope,
        effectiveFrom: mockData.effectiveFrom,
        effectiveUntil: mockData.effectiveUntil,
        createdBy: mockData.createdBy,
        createdAt: mockData.createdAt,
        updatedBy: mockData.updatedBy,
        updatedAt: mockData.updatedAt,
        approvedBy: mockData.approvedBy,
        approvedAt: mockData.approvedAt,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
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

      const response = await fetch(`/api/governance/agents/governance/${governanceId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        router.push(`/governance/agents/governance/${governanceId}`);
      } else {
        const error = await response.json();
        alert(t("agents.governance.edit.error", "Error al actualizar la política: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al actualizar política:", error);
      alert(t("agents.governance.edit.error", "Error al actualizar la política"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    if (isEditMode) {
      router.push(`/governance/agents/governance/${governanceId}`);
    } else {
      router.push("/governance/agents/governance/overview");
    }
  };

  const handleEdit = () => {
    router.push(`/governance/agents/governance/${governanceId}?edit=true`);
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
                ? t("agents.governance.edit.title", "Editar Política de Gobierno")
                : t("agents.governance.detail.title", "Detalles de Política de Gobierno")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {isEditMode
              ? t("agents.governance.edit.subtitle", "Modifique los campos necesarios para actualizar la política")
              : t("agents.governance.detail.subtitle", "Información detallada de la política de gobierno")}
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
              onClick={handleSubmit}
              variant="primary"
              disabled={isSubmitting}
            >
              <Save className="h-4 w-4 mr-2" />
              {isSubmitting
                ? t("common.saving", "Guardando...")
                : t("agents.governance.edit.saveButton", "Guardar Cambios")}
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
                ? t("agents.governance.edit.formTitle", "Editar Información de la Política")
                : t("agents.governance.detail.formTitle", "Información de la Política de Gobierno")}
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
                        : t("agents.governance.create.agentUuidPlaceholder", "Seleccione un agente activo")}
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

              {/* Policy Type */}
              <div className="space-y-1">
                <Label htmlFor="policyType" className="text-sm">
                  {t("agents.governance.create.policyType", "Tipo de Política")} *
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base font-semibold">{formData.policyType}</p>
                )}
              </div>

              {/* Policy Name */}
              <div className="space-y-1">
                <Label htmlFor="policyName" className="text-sm">
                  {t("agents.governance.create.policyName", "Nombre de la Política")} *
                </Label>
                {isEditMode ? (
                  <Input
                    id="policyName"
                    value={formData.policyName}
                    onChange={(e) => setFormData({ ...formData, policyName: e.target.value })}
                    placeholder={t("agents.governance.create.policyNamePlaceholder", "Ingrese el nombre de la política")}
                    required
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base font-semibold">{formData.policyName}</p>
                )}
              </div>

              {/* Enforcement Level */}
              <div className="space-y-1">
                <Label htmlFor="enforcementLevel" className="text-sm">
                  {t("agents.governance.create.enforcementLevel", "Nivel de Cumplimiento")} *
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">
                    <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                      {formData.enforcementLevel}
                    </span>
                  </p>
                )}
              </div>

              {/* Status */}
              <div className="space-y-1">
                <Label htmlFor="status" className="text-sm">
                  {t("agents.governance.create.status", "Estado")} *
                </Label>
                {isEditMode ? (
                  <select
                    id="status"
                    value={formData.status}
                    onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                    required
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="ACTIVE">{t("common.active", "Activo")}</option>
                    <option value="INACTIVE">{t("common.inactive", "Inactivo")}</option>
                    <option value="DRAFT">{t("agents.governance.create.statusDraft", "Borrador")}</option>
                  </select>
                ) : (
                  <p className="text-base">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        formData.status === "ACTIVE"
                          ? "bg-green-500/20 text-green-500"
                          : formData.status === "DRAFT"
                          ? "bg-yellow-500/20 text-yellow-500"
                          : "bg-gray-500/20 text-gray-500"
                      }`}
                    >
                      {formData.status}
                    </span>
                  </p>
                )}
              </div>

              {/* Priority */}
              <div className="space-y-1">
                <Label htmlFor="priority" className="text-sm">
                  {t("agents.governance.create.priority", "Prioridad")}
                </Label>
                {isEditMode ? (
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
                ) : (
                  <p className="text-base">{formData.priority || "-"}</p>
                )}
              </div>

              {/* Scope */}
              <div className="space-y-1">
                <Label htmlFor="scope" className="text-sm">
                  {t("agents.governance.create.scope", "Alcance")}
                </Label>
                {isEditMode ? (
                  <Input
                    id="scope"
                    value={formData.scope}
                    onChange={(e) => setFormData({ ...formData, scope: e.target.value })}
                    placeholder={t("agents.governance.create.scopePlaceholder", "Ingrese el alcance de la política")}
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">{formData.scope || "-"}</p>
                )}
              </div>

              {/* Effective From */}
              <div className="space-y-1">
                <Label htmlFor="effectiveFrom" className="text-sm">
                  {t("agents.governance.create.effectiveFrom", "Vigente Desde")} *
                </Label>
                {isEditMode ? (
                  <Input
                    id="effectiveFrom"
                    type="date"
                    value={formData.effectiveFrom}
                    onChange={(e) => setFormData({ ...formData, effectiveFrom: e.target.value })}
                    required
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">{formData.effectiveFrom || "-"}</p>
                )}
              </div>

              {/* Effective Until */}
              <div className="space-y-1">
                <Label htmlFor="effectiveUntil" className="text-sm">
                  {t("agents.governance.create.effectiveUntil", "Vigente Hasta")}
                </Label>
                {isEditMode ? (
                  <Input
                    id="effectiveUntil"
                    type="date"
                    value={formData.effectiveUntil}
                    onChange={(e) => setFormData({ ...formData, effectiveUntil: e.target.value })}
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">{formData.effectiveUntil || "-"}</p>
                )}
              </div>
            </div>

            {/* Description */}
            <div className="space-y-1">
              <Label htmlFor="description" className="text-sm">
                {t("agents.governance.create.description", "Descripción")}
              </Label>
              {isEditMode ? (
                <Textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  placeholder={t("agents.governance.create.descriptionPlaceholder", "Ingrese la descripción de la política")}
                  rows={3}
                  className="text-sm"
                />
              ) : (
                <p className="text-base whitespace-pre-wrap">{formData.description || "-"}</p>
              )}
            </div>

            {/* Policy Content */}
            <div className="space-y-1">
              <Label htmlFor="policyContent" className="text-sm">
                {t("agents.governance.create.policyContent", "Contenido de la Política")} *
              </Label>
              {isEditMode ? (
                <Textarea
                  id="policyContent"
                  value={formData.policyContent}
                  onChange={(e) => setFormData({ ...formData, policyContent: e.target.value })}
                  placeholder={t("agents.governance.create.policyContentPlaceholder", "Ingrese el contenido completo de la política")}
                  rows={5}
                  required
                  className="text-sm"
                />
              ) : (
                <p className="text-base whitespace-pre-wrap">{formData.policyContent || "-"}</p>
              )}
            </div>

            {/* Sección de Auditoría */}
            <div className="pt-4 border-t space-y-3">
              <h3 className="text-sm font-semibold text-foreground">
                {t("agents.governance.audit.title", "Registro de Auditoría")}
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {/* Creado por */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.governance.audit.createdBy", "Creado por")}
                  </Label>
                  <p className="text-sm font-medium">{formData.createdBy || "-"}</p>
                </div>
                {/* Creado en */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.governance.audit.createdAt", "Fecha de creación")}
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
                        {t("agents.governance.audit.updatedBy", "Última modificación por")}
                      </Label>
                      <p className="text-sm font-medium">{formData.updatedBy}</p>
                    </div>
                    {/* Actualizado en */}
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.governance.audit.updatedAt", "Fecha de última modificación")}
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
                {/* Aprobado por */}
                {formData.approvedBy && (
                  <>
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.governance.audit.approvedBy", "Aprobado por")}
                      </Label>
                      <p className="text-sm font-medium">{formData.approvedBy}</p>
                    </div>
                    {/* Aprobado en */}
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.governance.audit.approvedAt", "Fecha de aprobación")}
                      </Label>
                      <p className="text-sm">
                        {formData.approvedAt
                          ? new Date(formData.approvedAt).toLocaleString("es-ES", {
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
