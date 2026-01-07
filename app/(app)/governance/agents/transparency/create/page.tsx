"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, Save, Eye } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface TransparencyFormData {
  agentUuid?: string;
  transparencyType: string;
  transparencyScore?: string;
  explainabilityScore?: string;
  interpretabilityScore?: string;
  auditabilityScore?: string;
  algorithmDisclosure?: string;
  dataDisclosure?: string;
  decisionExplainability?: string;
  explanationMethod?: string;
  auditDate?: string;
  auditor?: string;
  certificationStatus?: string;
  complianceLevel?: string;
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

export default function TransparencyCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<TransparencyFormData>({
    agentUuid: "",
    transparencyType: "DECISION",
    transparencyScore: "",
    explainabilityScore: "",
    interpretabilityScore: "",
    auditabilityScore: "",
    algorithmDisclosure: "PARTIAL",
    dataDisclosure: "PARTIAL",
    decisionExplainability: "MEDIUM",
    explanationMethod: "",
    auditDate: new Date().toISOString().split("T")[0],
    auditor: "",
    certificationStatus: "PENDING",
    complianceLevel: "MEDIUM",
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
        ]);
      }
    } catch (error) {
      console.error("Error loading agents:", error);
      setAgents([
        { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
        { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
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
        setUsers([
          { id: "1", username: "admin", email: "admin@codeflowx.com", name: "Administrador" },
          { id: "2", username: "auditor", email: "auditor@codeflowx.com", name: "Juan Pérez" },
        ]);
      }
    } catch (error) {
      console.error("Error loading users:", error);
      setUsers([
        { id: "1", username: "admin", email: "admin@codeflowx.com", name: "Administrador" },
        { id: "2", username: "auditor", email: "auditor@codeflowx.com", name: "Juan Pérez" },
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

  const handleSubmit = async (e: React.FormEvent, action: "draft" | "submit" = "draft") => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      const currentUser = await getCurrentUser();
      const submissionData = {
        ...formData,
        createdBy: currentUser,
        certificationStatus: action === "draft" ? "DRAFT" : "PENDING",
        auditDate: formData.auditDate ? new Date(formData.auditDate).toISOString() : new Date().toISOString(),
        transparencyScore: formData.transparencyScore ? parseFloat(formData.transparencyScore) : null,
        explainabilityScore: formData.explainabilityScore ? parseFloat(formData.explainabilityScore) : null,
        interpretabilityScore: formData.interpretabilityScore ? parseFloat(formData.interpretabilityScore) : null,
        auditabilityScore: formData.auditabilityScore ? parseFloat(formData.auditabilityScore) : null,
      };

      const response = await fetch("/api/governance/agents/transparency", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();
        if (action === "submit") {
          try {
            await fetch("/api/bpmn/process/start", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processKey: "transparency-review-process",
                variables: {
                  transparencyId: data.id || data.idxagenttransparency,
                  agentUuid: formData.agentUuid,
                  transparencyType: formData.transparencyType,
                },
              }),
            });
          } catch (bpmnError) {
            console.warn("Error al lanzar proceso BPMN:", bpmnError);
          }
        }
        router.push("/governance/agents/transparency/overview");
      } else {
        const error = await response.json();
        alert(t("agents.transparency.create.error", "Error al crear la transparencia: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al crear transparencia:", error);
      alert(t("agents.transparency.create.error", "Error al crear la transparencia"));
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!mounted) {
    return null;
  }

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Eye className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.transparency.create.title", "Registrar Transparencia")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.transparency.create.subtitle", "Complete el formulario para registrar una nueva transparencia")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => router.push("/governance/agents/transparency/overview")}
            disabled={isSubmitting}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.cancel", "Cancelar")}
          </Button>
          <Button
            type="button"
            onClick={(e) => handleSubmit(e, "draft")}
            variant="outline"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {t("agents.transparency.create.saveDraft", "Guardar Borrador")}
          </Button>
          <Button
            type="button"
            onClick={(e) => handleSubmit(e, "submit")}
            variant="primary"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {t("agents.transparency.create.submitForReview", "Enviar para Revisión")}
          </Button>
        </div>
      </div>

      <form onSubmit={(e) => handleSubmit(e, "draft")}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("agents.transparency.create.formTitle", "Información de Transparencia")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.transparency.create.agentUuid", "Agente")} *
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
                      : t("agents.transparency.create.agentUuidPlaceholder", "Seleccione un agente activo")}
                  </option>
                  {agents.map((agent) => (
                    <option key={agent.id} value={agent.uuid || agent.id.toString()}>
                      {agent.name} ({agent.uuid || `ID: ${agent.id}`})
                    </option>
                  ))}
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="transparencyType" className="text-sm">
                  {t("agents.transparency.create.transparencyType", "Tipo de Transparencia")} *
                </Label>
                <select
                  id="transparencyType"
                  value={formData.transparencyType}
                  onChange={(e) => setFormData({ ...formData, transparencyType: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="DECISION">{t("agents.transparency.create.typeDecision", "Decisión")}</option>
                  <option value="PROCESS">{t("agents.transparency.create.typeProcess", "Proceso")}</option>
                  <option value="ALGORITHM">{t("agents.transparency.create.typeAlgorithm", "Algoritmo")}</option>
                  <option value="DATA">{t("agents.transparency.create.typeData", "Datos")}</option>
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="transparencyScore" className="text-sm">
                  {t("agents.transparency.create.transparencyScore", "Puntuación de Transparencia")}
                </Label>
                <Input
                  id="transparencyScore"
                  type="number"
                  min="0"
                  max="100"
                  value={formData.transparencyScore}
                  onChange={(e) => setFormData({ ...formData, transparencyScore: e.target.value })}
                  placeholder={t("agents.transparency.create.transparencyScorePlaceholder", "0-100")}
                  className="text-sm"
                />
              </div>

              <div className="space-y-1">
                <Label htmlFor="explainabilityScore" className="text-sm">
                  {t("agents.transparency.create.explainabilityScore", "Puntuación de Explicabilidad")}
                </Label>
                <Input
                  id="explainabilityScore"
                  type="number"
                  min="0"
                  max="100"
                  value={formData.explainabilityScore}
                  onChange={(e) => setFormData({ ...formData, explainabilityScore: e.target.value })}
                  placeholder={t("agents.transparency.create.explainabilityScorePlaceholder", "0-100")}
                  className="text-sm"
                />
              </div>

              <div className="space-y-1">
                <Label htmlFor="algorithmDisclosure" className="text-sm">
                  {t("agents.transparency.create.algorithmDisclosure", "Divulgación de Algoritmo")}
                </Label>
                <select
                  id="algorithmDisclosure"
                  value={formData.algorithmDisclosure}
                  onChange={(e) => setFormData({ ...formData, algorithmDisclosure: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="FULL">{t("agents.transparency.create.disclosureFull", "Completa")}</option>
                  <option value="PARTIAL">{t("agents.transparency.create.disclosurePartial", "Parcial")}</option>
                  <option value="MINIMAL">{t("agents.transparency.create.disclosureMinimal", "Mínima")}</option>
                  <option value="NONE">{t("agents.transparency.create.disclosureNone", "Ninguna")}</option>
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="dataDisclosure" className="text-sm">
                  {t("agents.transparency.create.dataDisclosure", "Divulgación de Datos")}
                </Label>
                <select
                  id="dataDisclosure"
                  value={formData.dataDisclosure}
                  onChange={(e) => setFormData({ ...formData, dataDisclosure: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="FULL">{t("agents.transparency.create.disclosureFull", "Completa")}</option>
                  <option value="PARTIAL">{t("agents.transparency.create.disclosurePartial", "Parcial")}</option>
                  <option value="MINIMAL">{t("agents.transparency.create.disclosureMinimal", "Mínima")}</option>
                  <option value="NONE">{t("agents.transparency.create.disclosureNone", "Ninguna")}</option>
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="explanationMethod" className="text-sm">
                  {t("agents.transparency.create.explanationMethod", "Método de Explicación")}
                </Label>
                <select
                  id="explanationMethod"
                  value={formData.explanationMethod}
                  onChange={(e) => setFormData({ ...formData, explanationMethod: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="">{t("agents.transparency.create.explanationMethodPlaceholder", "Seleccione un método")}</option>
                  <option value="SHAP">{t("agents.transparency.create.methodSHAP", "SHAP (SHapley Additive exPlanations)")}</option>
                  <option value="LIME">{t("agents.transparency.create.methodLIME", "LIME (Local Interpretable Model-agnostic Explanations)")}</option>
                  <option value="INTEGRATED_GRADIENTS">{t("agents.transparency.create.methodIntegratedGradients", "Integrated Gradients")}</option>
                  <option value="GRADIENT_BASED">{t("agents.transparency.create.methodGradientBased", "Gradient-Based Methods")}</option>
                  <option value="ATTENTION_MECHANISMS">{t("agents.transparency.create.methodAttention", "Attention Mechanisms")}</option>
                  <option value="FEATURE_IMPORTANCE">{t("agents.transparency.create.methodFeatureImportance", "Feature Importance")}</option>
                  <option value="PARTIAL_DEPENDENCE">{t("agents.transparency.create.methodPartialDependence", "Partial Dependence Plots")}</option>
                  <option value="SALIENCY_MAPS">{t("agents.transparency.create.methodSaliencyMaps", "Saliency Maps")}</option>
                  <option value="COUNTERFACTUAL">{t("agents.transparency.create.methodCounterfactual", "Counterfactual Explanations")}</option>
                  <option value="ANCHOR">{t("agents.transparency.create.methodAnchor", "Anchor Explanations")}</option>
                  <option value="RUL_BASED">{t("agents.transparency.create.methodRuleBased", "Rule-Based Explanations")}</option>
                  <option value="EXAMPLE_BASED">{t("agents.transparency.create.methodExampleBased", "Example-Based Explanations")}</option>
                  <option value="OTHER">{t("agents.transparency.create.methodOther", "Otro")}</option>
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="auditDate" className="text-sm">
                  {t("agents.transparency.create.auditDate", "Fecha de Auditoría")}
                </Label>
                <Input
                  id="auditDate"
                  type="date"
                  value={formData.auditDate}
                  onChange={(e) => setFormData({ ...formData, auditDate: e.target.value })}
                  className="text-sm"
                />
              </div>

              <div className="space-y-1">
                <Label htmlFor="auditor" className="text-sm">
                  {t("agents.transparency.create.auditor", "Auditor")}
                </Label>
                <select
                  id="auditor"
                  value={formData.auditor}
                  onChange={(e) => setFormData({ ...formData, auditor: e.target.value })}
                  disabled={loadingUsers}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary disabled:bg-muted disabled:text-muted-foreground"
                >
                  <option value="">
                    {loadingUsers
                      ? t("common.loading", "Cargando usuarios...")
                      : t("agents.transparency.create.auditorPlaceholder", "Seleccione un auditor")}
                  </option>
                  {users.map((user) => (
                    <option key={user.id} value={user.username || user.email || user.id}>
                      {user.name || user.username} {user.email && `(${user.email})`}
                    </option>
                  ))}
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="certificationStatus" className="text-sm">
                  {t("agents.transparency.create.certificationStatus", "Estado de Certificación")}
                </Label>
                <select
                  id="certificationStatus"
                  value={formData.certificationStatus}
                  onChange={(e) => setFormData({ ...formData, certificationStatus: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="DRAFT">{t("agents.transparency.create.statusDraft", "Borrador")}</option>
                  <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                  <option value="CERTIFIED">{t("agents.transparency.create.statusCertified", "Certificado")}</option>
                  <option value="REJECTED">{t("agents.transparency.create.statusRejected", "Rechazado")}</option>
                </select>
              </div>

              <div className="space-y-1">
                <Label htmlFor="complianceLevel" className="text-sm">
                  {t("agents.transparency.create.complianceLevel", "Nivel de Cumplimiento")}
                </Label>
                <select
                  id="complianceLevel"
                  value={formData.complianceLevel}
                  onChange={(e) => setFormData({ ...formData, complianceLevel: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="LOW">{t("common.low", "Bajo")}</option>
                  <option value="MEDIUM">{t("common.medium", "Medio")}</option>
                  <option value="HIGH">{t("common.high", "Alto")}</option>
                </select>
              </div>
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
