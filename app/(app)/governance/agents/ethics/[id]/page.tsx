"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Scale, Save, Edit } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams, useSearchParams } from "next/navigation";

interface EthicsFormData {
  agentUuid?: string;
  assessmentType: string;
  assessmentStatus: string;
  overallScore: string;
  riskLevel: string;
  humanReviewerId: string;
  humanReviewerName: string;
  assessmentDate: string;
  algorithmUsed: string;
  datasetUsed: string;
  humanReviewNotes: string;
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

export default function EthicsDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const ethicsId = params?.id as string;
  const isEditMode = searchParams?.get("edit") === "true";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<EthicsFormData>({
    agentUuid: "",
    assessmentType: "FAIRNESS",
    assessmentStatus: "PENDING",
    overallScore: "",
    riskLevel: "MEDIUM",
    humanReviewerId: "",
    humanReviewerName: "",
    assessmentDate: new Date().toISOString().split("T")[0],
    algorithmUsed: "",
    datasetUsed: "",
    humanReviewNotes: "",
  });

  useEffect(() => {
    if (ethicsId) {
      loadEthicsData();
    }
    loadActiveAgents();
    loadUsers();
  }, [ethicsId]);

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

  const loadEthicsData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/ethics/${ethicsId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || data.agent?.uuid || "",
          assessmentType: data.assessmentType || "FAIRNESS",
          assessmentStatus: data.assessmentStatus || "PENDING",
          overallScore: data.overallScore?.toString() || "",
          riskLevel: data.riskLevel || "MEDIUM",
          humanReviewerId: data.humanReviewerId || "",
          humanReviewerName: data.humanReviewerName || "",
          assessmentDate: data.assessmentDate ? new Date(data.assessmentDate).toISOString().split("T")[0] : new Date().toISOString().split("T")[0],
          algorithmUsed: data.algorithmUsed || "",
          datasetUsed: data.datasetUsed || "",
          humanReviewNotes: data.humanReviewNotes || "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
      } else {
        // Mock data para desarrollo
        const mockData = {
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          assessmentType: "FAIRNESS",
          assessmentStatus: "PASSED",
          overallScore: "92",
          riskLevel: "LOW",
          humanReviewerId: "2",
          humanReviewerName: "compliance",
          assessmentDate: "2024-01-15",
          algorithmUsed: "NEURAL_NETWORK",
          datasetUsed: "credit-scoring-dataset-v1.0",
          humanReviewNotes: "La evaluación ética muestra que el agente cumple con los estándares de equidad establecidos. No se detectaron sesgos significativos en las decisiones crediticias.",
          createdBy: "ethics@codeflowx.com",
          createdAt: "2024-01-15T10:00:00",
          updatedBy: "auditor@codeflowx.com",
          updatedAt: "2024-01-20T14:30:00",
        };
        setFormData({
          agentUuid: mockData.agentUuid,
          assessmentType: mockData.assessmentType,
          assessmentStatus: mockData.assessmentStatus,
          overallScore: mockData.overallScore,
          riskLevel: mockData.riskLevel,
          humanReviewerId: mockData.humanReviewerId,
          humanReviewerName: mockData.humanReviewerName,
          assessmentDate: mockData.assessmentDate,
          algorithmUsed: mockData.algorithmUsed,
          datasetUsed: mockData.datasetUsed,
          humanReviewNotes: mockData.humanReviewNotes,
          createdBy: mockData.createdBy,
          createdAt: mockData.createdAt,
          updatedBy: mockData.updatedBy,
          updatedAt: mockData.updatedAt,
        });
      }
    } catch (error) {
      console.error("Error loading ethics:", error);
      // Mock data en caso de error
      const mockData = {
        agentUuid: "550e8400-e29b-41d4-a716-446655440000",
        assessmentType: "FAIRNESS",
        assessmentStatus: "PASSED",
        overallScore: "92",
        riskLevel: "LOW",
        humanReviewerId: "2",
        humanReviewerName: "compliance",
        assessmentDate: "2024-01-15",
        algorithmUsed: "NEURAL_NETWORK",
        datasetUsed: "credit-scoring-dataset-v1.0",
        humanReviewNotes: "La evaluación ética muestra que el agente cumple con los estándares de equidad establecidos.",
        createdBy: "ethics@codeflowx.com",
        createdAt: "2024-01-15T10:00:00",
        updatedBy: "auditor@codeflowx.com",
        updatedAt: "2024-01-20T14:30:00",
      };
      setFormData({
        agentUuid: mockData.agentUuid,
        assessmentType: mockData.assessmentType,
        assessmentStatus: mockData.assessmentStatus,
        overallScore: mockData.overallScore,
        riskLevel: mockData.riskLevel,
        humanReviewerId: mockData.humanReviewerId,
        humanReviewerName: mockData.humanReviewerName,
        assessmentDate: mockData.assessmentDate,
        algorithmUsed: mockData.algorithmUsed,
        datasetUsed: mockData.datasetUsed,
        humanReviewNotes: mockData.humanReviewNotes,
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
        assessmentDate: formData.assessmentDate ? new Date(formData.assessmentDate).toISOString() : new Date().toISOString(),
      };

      const response = await fetch(`/api/governance/agents/ethics/${ethicsId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        router.push(`/governance/agents/ethics/${ethicsId}`);
      } else {
        const error = await response.json();
        alert(t("agents.ethics.edit.error", "Error al actualizar la evaluación ética: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al actualizar evaluación ética:", error);
      alert(t("agents.ethics.edit.error", "Error al actualizar la evaluación ética"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    if (isEditMode) {
      router.push(`/governance/agents/ethics/${ethicsId}`);
    } else {
      router.push("/governance/agents/ethics/overview");
    }
  };

  const handleEdit = () => {
    router.push(`/governance/agents/ethics/${ethicsId}?edit=true`);
  };

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Scale className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <Scale className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {isEditMode
                ? t("agents.ethics.edit.title", "Editar Evaluación Ética")
                : t("agents.ethics.detail.title", "Detalles de Evaluación Ética")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {isEditMode
              ? t("agents.ethics.edit.subtitle", "Modifique los campos necesarios para actualizar la evaluación ética")
              : t("agents.ethics.detail.subtitle", "Información detallada de la evaluación ética del agente")}
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
                : t("agents.ethics.edit.saveButton", "Guardar Cambios")}
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
                ? t("agents.ethics.edit.formTitle", "Editar Información de la Evaluación Ética")
                : t("agents.ethics.detail.formTitle", "Información de la Evaluación Ética")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            {/* Grid compacto de campos */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {/* Agent UUID */}
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.ethics.create.agentUuid", "Agente")} *
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
                        : t("agents.ethics.create.agentUuidPlaceholder", "Seleccione un agente activo")}
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

              {/* Assessment Type */}
              <div className="space-y-1">
                <Label htmlFor="assessmentType" className="text-sm">
                  {t("agents.ethics.create.assessmentType", "Tipo de Evaluación")} *
                </Label>
                {isEditMode ? (
                  <select
                    id="assessmentType"
                    value={formData.assessmentType}
                    onChange={(e) => setFormData({ ...formData, assessmentType: e.target.value })}
                    required
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="FAIRNESS">{t("agents.ethics.create.typeFairness", "Equidad")}</option>
                    <option value="TRANSPARENCY">{t("agents.ethics.create.typeTransparency", "Transparencia")}</option>
                    <option value="BIAS">{t("agents.ethics.create.typeBias", "Sesgo")}</option>
                    <option value="ACCOUNTABILITY">{t("agents.ethics.create.typeAccountability", "Responsabilidad")}</option>
                    <option value="PRIVACY">{t("agents.ethics.create.typePrivacy", "Privacidad")}</option>
                  </select>
                ) : (
                  <p className="text-base font-semibold">{formData.assessmentType}</p>
                )}
              </div>

              {/* Assessment Status */}
              <div className="space-y-1">
                <Label htmlFor="assessmentStatus" className="text-sm">
                  {t("agents.ethics.create.assessmentStatus", "Estado de Evaluación")} *
                </Label>
                {isEditMode ? (
                  <select
                    id="assessmentStatus"
                    value={formData.assessmentStatus}
                    onChange={(e) => setFormData({ ...formData, assessmentStatus: e.target.value })}
                    required
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="DRAFT">{t("agents.ethics.create.statusDraft", "Borrador")}</option>
                    <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                    <option value="PASSED">{t("agents.ethics.create.statusPassed", "Aprobado")}</option>
                    <option value="FAILED">{t("agents.ethics.create.statusFailed", "Fallido")}</option>
                  </select>
                ) : (
                  <p className="text-base">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        formData.assessmentStatus === "PASSED"
                          ? "bg-green-500/20 text-green-500"
                          : formData.assessmentStatus === "PENDING" || formData.assessmentStatus === "DRAFT"
                          ? "bg-yellow-500/20 text-yellow-500"
                          : "bg-red-500/20 text-red-500"
                      }`}
                    >
                      {formData.assessmentStatus}
                    </span>
                  </p>
                )}
              </div>

              {/* Risk Level */}
              <div className="space-y-1">
                <Label htmlFor="riskLevel" className="text-sm">
                  {t("agents.ethics.create.riskLevel", "Nivel de Riesgo")}
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
                    <option value="CRITICAL">{t("agents.ethics.create.riskCritical", "Crítico")}</option>
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

              {/* Overall Score */}
              <div className="space-y-1">
                <Label htmlFor="overallScore" className="text-sm">
                  {t("agents.ethics.create.overallScore", "Puntuación General")}
                </Label>
                {isEditMode ? (
                  <Input
                    id="overallScore"
                    type="number"
                    min="0"
                    max="100"
                    value={formData.overallScore}
                    onChange={(e) => setFormData({ ...formData, overallScore: e.target.value })}
                    placeholder={t("agents.ethics.create.overallScorePlaceholder", "0-100")}
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base font-bold">{formData.overallScore || "-"}%</p>
                )}
              </div>

              {/* Assessment Date */}
              <div className="space-y-1">
                <Label htmlFor="assessmentDate" className="text-sm">
                  {t("agents.ethics.create.assessmentDate", "Fecha de Evaluación")} *
                </Label>
                {isEditMode ? (
                  <Input
                    id="assessmentDate"
                    type="date"
                    value={formData.assessmentDate}
                    onChange={(e) => setFormData({ ...formData, assessmentDate: e.target.value })}
                    required
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">
                    {formData.assessmentDate
                      ? new Date(formData.assessmentDate).toLocaleDateString("es-ES", {
                          year: "numeric",
                          month: "2-digit",
                          day: "2-digit",
                        })
                      : "-"}
                  </p>
                )}
              </div>

              {/* Human Reviewer */}
              <div className="space-y-1">
                <Label htmlFor="humanReviewerName" className="text-sm">
                  {t("agents.ethics.create.humanReviewer", "Revisor Humano")}
                </Label>
                {isEditMode ? (
                  <select
                    id="humanReviewerName"
                    value={formData.humanReviewerName}
                    onChange={(e) => {
                      const selectedUser = users.find((u) => u.username === e.target.value || u.email === e.target.value);
                      setFormData({
                        ...formData,
                        humanReviewerName: e.target.value,
                        humanReviewerId: selectedUser?.id || e.target.value,
                      });
                    }}
                    disabled={loadingUsers}
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary disabled:bg-muted disabled:text-muted-foreground"
                  >
                    <option value="">
                      {loadingUsers
                        ? t("common.loading", "Cargando usuarios...")
                        : t("agents.ethics.create.humanReviewerPlaceholder", "Seleccione un revisor")}
                    </option>
                    {users.map((user) => (
                      <option key={user.id} value={user.username || user.email || user.id}>
                        {user.name || user.username} {user.email && `(${user.email})`}
                      </option>
                    ))}
                  </select>
                ) : (
                  <p className="text-base">
                    {users.find((u) => u.username === formData.humanReviewerName || u.email === formData.humanReviewerName)?.name || formData.humanReviewerName || "-"}
                  </p>
                )}
              </div>

              {/* Algorithm Used */}
              <div className="space-y-1">
                <Label htmlFor="algorithmUsed" className="text-sm">
                  {t("agents.ethics.create.algorithmUsed", "Algoritmo Utilizado")}
                </Label>
                {isEditMode ? (
                  <select
                    id="algorithmUsed"
                    value={formData.algorithmUsed}
                    onChange={(e) => setFormData({ ...formData, algorithmUsed: e.target.value })}
                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="">{t("agents.ethics.create.algorithmUsedPlaceholder", "Seleccione un algoritmo")}</option>
                    <option value="NEURAL_NETWORK">{t("agents.ethics.create.algorithmNeuralNetwork", "Red Neuronal (Neural Network)")}</option>
                    <option value="TRANSFORMER">{t("agents.ethics.create.algorithmTransformer", "Transformer")}</option>
                    <option value="DECISION_TREE">{t("agents.ethics.create.algorithmDecisionTree", "Árbol de Decisión (Decision Tree)")}</option>
                    <option value="RANDOM_FOREST">{t("agents.ethics.create.algorithmRandomForest", "Bosque Aleatorio (Random Forest)")}</option>
                    <option value="SVM">{t("agents.ethics.create.algorithmSVM", "Máquina de Vectores de Soporte (SVM)")}</option>
                    <option value="LINEAR_REGRESSION">{t("agents.ethics.create.algorithmLinearRegression", "Regresión Lineal")}</option>
                    <option value="LOGISTIC_REGRESSION">{t("agents.ethics.create.algorithmLogisticRegression", "Regresión Logística")}</option>
                    <option value="GRADIENT_BOOSTING">{t("agents.ethics.create.algorithmGradientBoosting", "Gradient Boosting")}</option>
                    <option value="K_MEANS">{t("agents.ethics.create.algorithmKMeans", "K-Means (Clustering)")}</option>
                    <option value="NAIVE_BAYES">{t("agents.ethics.create.algorithmNaiveBayes", "Naive Bayes")}</option>
                    <option value="OTHER">{t("agents.ethics.create.algorithmOther", "Otro")}</option>
                  </select>
                ) : (
                  <p className="text-base">
                    {formData.algorithmUsed === "NEURAL_NETWORK"
                      ? t("agents.ethics.create.algorithmNeuralNetwork", "Red Neuronal (Neural Network)")
                      : formData.algorithmUsed === "TRANSFORMER"
                      ? t("agents.ethics.create.algorithmTransformer", "Transformer")
                      : formData.algorithmUsed === "DECISION_TREE"
                      ? t("agents.ethics.create.algorithmDecisionTree", "Árbol de Decisión (Decision Tree)")
                      : formData.algorithmUsed === "RANDOM_FOREST"
                      ? t("agents.ethics.create.algorithmRandomForest", "Bosque Aleatorio (Random Forest)")
                      : formData.algorithmUsed === "SVM"
                      ? t("agents.ethics.create.algorithmSVM", "Máquina de Vectores de Soporte (SVM)")
                      : formData.algorithmUsed === "LINEAR_REGRESSION"
                      ? t("agents.ethics.create.algorithmLinearRegression", "Regresión Lineal")
                      : formData.algorithmUsed === "LOGISTIC_REGRESSION"
                      ? t("agents.ethics.create.algorithmLogisticRegression", "Regresión Logística")
                      : formData.algorithmUsed === "GRADIENT_BOOSTING"
                      ? t("agents.ethics.create.algorithmGradientBoosting", "Gradient Boosting")
                      : formData.algorithmUsed === "K_MEANS"
                      ? t("agents.ethics.create.algorithmKMeans", "K-Means (Clustering)")
                      : formData.algorithmUsed === "NAIVE_BAYES"
                      ? t("agents.ethics.create.algorithmNaiveBayes", "Naive Bayes")
                      : formData.algorithmUsed || "-"}
                  </p>
                )}
              </div>

              {/* Dataset Used */}
              <div className="space-y-1">
                <Label htmlFor="datasetUsed" className="text-sm">
                  {t("agents.ethics.create.datasetUsed", "Dataset de Entrenamiento del Agente")}
                </Label>
                {isEditMode ? (
                  <Input
                    id="datasetUsed"
                    value={formData.datasetUsed}
                    onChange={(e) => setFormData({ ...formData, datasetUsed: e.target.value })}
                    placeholder={t("agents.ethics.create.datasetUsedPlaceholder", "Ej: credit-scoring-dataset-v1.0")}
                    className="text-sm"
                  />
                ) : (
                  <p className="text-base">{formData.datasetUsed || "-"}</p>
                )}
              </div>
            </div>

            {/* Human Review Notes */}
            <div className="space-y-1">
              <Label htmlFor="humanReviewNotes" className="text-sm">
                {t("agents.ethics.create.humanReviewNotes", "Notas de Revisión Humana")}
              </Label>
              {isEditMode ? (
                <Textarea
                  id="humanReviewNotes"
                  value={formData.humanReviewNotes}
                  onChange={(e) => setFormData({ ...formData, humanReviewNotes: e.target.value })}
                  placeholder={t("agents.ethics.create.humanReviewNotesPlaceholder", "Ingrese notas sobre la revisión humana")}
                  rows={4}
                  className="text-sm"
                />
              ) : (
                <p className="text-base whitespace-pre-wrap">{formData.humanReviewNotes || "-"}</p>
              )}
            </div>

            {/* Sección de Auditoría */}
            <div className="pt-4 border-t space-y-3">
              <h3 className="text-sm font-semibold text-foreground">
                {t("agents.ethics.audit.title", "Registro de Auditoría")}
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {/* Creado por */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.ethics.audit.createdBy", "Creado por")}
                  </Label>
                  <p className="text-sm font-medium">{formData.createdBy || "-"}</p>
                </div>
                {/* Creado en */}
                <div className="space-y-1">
                  <Label className="text-xs text-muted-foreground">
                    {t("agents.ethics.audit.createdAt", "Fecha de creación")}
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
                        {t("agents.ethics.audit.updatedBy", "Última modificación por")}
                      </Label>
                      <p className="text-sm font-medium">{formData.updatedBy}</p>
                    </div>
                    {/* Actualizado en */}
                    <div className="space-y-1">
                      <Label className="text-xs text-muted-foreground">
                        {t("agents.ethics.audit.updatedAt", "Fecha de última modificación")}
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

