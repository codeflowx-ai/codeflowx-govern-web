"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, Save, Scale } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

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


export default function EthicsCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<EthicsFormData>({
    agentUuid: "",
    assessmentType: "FAIRNESS",
    assessmentStatus: "DRAFT", // Por defecto borrador
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
        assessmentStatus: "DRAFT", // Forzar estado borrador
        createdBy: createdBy,
        assessmentDate: formData.assessmentDate ? new Date(formData.assessmentDate).toISOString() : new Date().toISOString(),
      };

      const response = await fetch("/api/governance/agents/ethics", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.id) {
          router.push(`/governance/agents/ethics/${data.id}`);
        } else {
          router.push("/governance/agents/ethics/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.ethics.create.error", "Error al guardar el borrador: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al guardar borrador:", error);
      alert(t("agents.ethics.create.error", "Error al guardar el borrador"));
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
        assessmentStatus: "PENDING", // Forzar estado pendiente
        createdBy: createdBy,
        assessmentDate: formData.assessmentDate ? new Date(formData.assessmentDate).toISOString() : new Date().toISOString(),
      };

      const response = await fetch("/api/governance/agents/ethics", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();

        // Lanzar proceso BPMN después de crear la evaluación ética
        if (data.id) {
          try {
            const bpmnResponse = await fetch(`/api/governance/agents/ethics/${data.id}/start-process`, {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processDefinitionKey: "ethics-assessment-process",
                businessKey: `ethics-${data.id}`,
                variables: {
                  ethicsAssessmentId: data.id,
                  agentUuid: formData.agentUuid,
                  assessmentType: formData.assessmentType,
                  riskLevel: formData.riskLevel,
                },
              }),
            });

            if (!bpmnResponse.ok) {
              console.warn("No se pudo iniciar el proceso BPMN, pero la evaluación ética se creó correctamente");
            }
          } catch (bpmnError) {
            console.error("Error al iniciar proceso BPMN:", bpmnError);
            // No bloqueamos la creación si falla el BPMN
          }

          router.push(`/governance/agents/ethics/${data.id}`);
        } else {
          router.push("/governance/agents/ethics/overview");
        }
      } else {
        const error = await response.json();
        alert(t("agents.ethics.create.error", "Error al enviar para aprobación: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al enviar para aprobación:", error);
      alert(t("agents.ethics.create.error", "Error al enviar para aprobación"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.push("/governance/agents/ethics/overview");
  };

  if (!mounted) {
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
              {t("agents.ethics.create.title", "Crear Nueva Evaluación Ética")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.ethics.create.subtitle", "Complete el formulario para crear una nueva evaluación ética")}
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
              : t("agents.ethics.create.saveDraft", "Guardar como Borrador")}
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
              : t("agents.ethics.create.submitForApproval", "Enviar para Evaluación")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={handleSubmitForApproval}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("agents.ethics.create.formTitle", "Información de la Evaluación Ética")}
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
              </div>

              {/* Assessment Type */}
              <div className="space-y-1">
                <Label htmlFor="assessmentType" className="text-sm">
                  {t("agents.ethics.create.assessmentType", "Tipo de Evaluación")} *
                </Label>
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
              </div>

              {/* Assessment Status - Bloqueado, se establece automáticamente */}
              <div className="space-y-1">
                <Label htmlFor="assessmentStatus" className="text-sm">
                  {t("agents.ethics.create.assessmentStatus", "Estado de Evaluación")} *
                </Label>
                <select
                  id="assessmentStatus"
                  value={formData.assessmentStatus}
                  disabled
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-muted text-muted-foreground cursor-not-allowed"
                >
                  <option value="DRAFT">{t("agents.ethics.create.statusDraft", "Borrador")}</option>
                  <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                </select>
                <p className="text-xs text-muted-foreground">
                  {t("agents.ethics.create.statusInfo", "El estado se establecerá automáticamente: 'Borrador' al guardar como borrador, 'Pendiente' al enviar para evaluación")}
                </p>
              </div>

              {/* Risk Level */}
              <div className="space-y-1">
                <Label htmlFor="riskLevel" className="text-sm">
                  {t("agents.ethics.create.riskLevel", "Nivel de Riesgo")}
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
                  <option value="CRITICAL">{t("agents.ethics.create.riskCritical", "Crítico")}</option>
                </select>
              </div>

              {/* Overall Score */}
              <div className="space-y-1">
                <Label htmlFor="overallScore" className="text-sm">
                  {t("agents.ethics.create.overallScore", "Puntuación General")}
                </Label>
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
              </div>

              {/* Assessment Date */}
              <div className="space-y-1">
                <Label htmlFor="assessmentDate" className="text-sm">
                  {t("agents.ethics.create.assessmentDate", "Fecha de Evaluación")} *
                </Label>
                <Input
                  id="assessmentDate"
                  type="date"
                  value={formData.assessmentDate}
                  onChange={(e) => setFormData({ ...formData, assessmentDate: e.target.value })}
                  required
                  className="text-sm"
                />
              </div>

              {/* Human Reviewer */}
              <div className="space-y-1">
                <Label htmlFor="humanReviewerName" className="text-sm">
                  {t("agents.ethics.create.humanReviewer", "Revisor Humano")}
                </Label>
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
              </div>

              {/* Algorithm Used */}
              <div className="space-y-1">
                <Label htmlFor="algorithmUsed" className="text-sm">
                  {t("agents.ethics.create.algorithmUsed", "Algoritmo Utilizado")}
                </Label>
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
              </div>

              {/* Dataset Used */}
              <div className="space-y-1">
                <Label htmlFor="datasetUsed" className="text-sm">
                  {t("agents.ethics.create.datasetUsed", "Dataset de Entrenamiento del Agente")}
                </Label>
                <Input
                  id="datasetUsed"
                  value={formData.datasetUsed}
                  onChange={(e) => setFormData({ ...formData, datasetUsed: e.target.value })}
                  placeholder={t("agents.ethics.create.datasetUsedPlaceholder", "Ej: credit-scoring-dataset-v1.0, fraud-detection-data-2024")}
                  className="text-sm"
                />
                <p className="text-xs text-muted-foreground">
                  {t("agents.ethics.create.datasetUsedHelp", "Nombre o identificador del dataset de entrenamiento utilizado por este agente. Permite rastrear la procedencia de los datos y evaluar posibles sesgos.")}
                </p>
              </div>
            </div>

            {/* Human Review Notes */}
            <div className="space-y-1">
              <Label htmlFor="humanReviewNotes" className="text-sm">
                {t("agents.ethics.create.humanReviewNotes", "Notas de Revisión Humana")}
              </Label>
              <Textarea
                id="humanReviewNotes"
                value={formData.humanReviewNotes}
                onChange={(e) => setFormData({ ...formData, humanReviewNotes: e.target.value })}
                placeholder={t("agents.ethics.create.humanReviewNotesPlaceholder", "Ingrese notas sobre la revisión humana")}
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
