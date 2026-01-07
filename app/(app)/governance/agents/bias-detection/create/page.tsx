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

interface BiasDetectionFormData {
  agentUuid?: string;
  biasType: string;
  biasCategory?: string;
  severity: string;
  confidenceScore?: string;
  biasScore?: string;
  description?: string;
  affectedContent?: string;
  detectionMethod?: string;
  detectionAlgorithm?: string;
  status: string;
  priority?: string;
  detectedAt: string;
  mitigationStatus?: string;
  verificationStatus?: string;
  verifierId?: string;
  verifiedAt?: string;
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

export default function BiasDetectionCreatePage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loadingAgents, setLoadingAgents] = useState(true);
  const [users, setUsers] = useState<User[]>([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [formData, setFormData] = useState<BiasDetectionFormData>({
    agentUuid: "",
    biasType: "GENDER",
    biasCategory: "",
    severity: "MEDIUM",
    confidenceScore: "",
    biasScore: "",
    description: "",
    affectedContent: "",
    detectionMethod: "STATISTICAL",
    detectionAlgorithm: "",
    status: "DRAFT",
    priority: "MEDIUM",
    detectedAt: new Date().toISOString().split("T")[0],
    mitigationStatus: "",
    verificationStatus: "",
    verifierId: "",
    verifiedAt: "",
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
        setUsers([
          { id: "1", username: "admin", email: "admin@codeflowx.com", name: "Administrador" },
          { id: "2", username: "compliance", email: "compliance@codeflowx.com", name: "María González" },
          { id: "3", username: "auditor", email: "auditor@codeflowx.com", name: "Juan Pérez" },
          { id: "4", username: "assessor", email: "assessor@codeflowx.com", name: "Ana Martínez" },
        ]);
      }
    } catch (error) {
      console.error("Error loading users:", error);
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
        status: action === "draft" ? "DRAFT" : "PENDING",
        detectedAt: formData.detectedAt ? new Date(formData.detectedAt).toISOString() : new Date().toISOString(),
        confidenceScore: formData.confidenceScore ? parseFloat(formData.confidenceScore) : null,
        biasScore: formData.biasScore ? parseFloat(formData.biasScore) : null,
      };

      const response = await fetch("/api/governance/agents/bias-detection", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(submissionData),
      });

      if (response.ok) {
        const data = await response.json();
        // Si se envía para evaluación, lanzar proceso BPMN
        if (action === "submit") {
          try {
            await fetch("/api/bpmn/process/start", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                processKey: "bias-detection-review-process",
                variables: {
                  biasDetectionId: data.id || data.idxagentbiasdetection,
                  agentUuid: formData.agentUuid,
                  biasType: formData.biasType,
                  severity: formData.severity,
                },
              }),
            });
          } catch (bpmnError) {
            console.warn("Error al lanzar proceso BPMN:", bpmnError);
          }
        }
        router.push("/governance/agents/bias-detection/overview");
      } else {
        const error = await response.json();
        alert(t("agents.biasDetection.create.error", "Error al crear la detección de sesgo: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error al crear detección de sesgo:", error);
      alert(t("agents.biasDetection.create.error", "Error al crear la detección de sesgo"));
    } finally {
      setIsSubmitting(false);
    }
  };

  if (!mounted) {
    return null;
  }

  return (
    <div className="space-y-4 p-6">
      {/* Header con botones de acción en la parte superior derecha */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.biasDetection.create.title", "Registrar Detección de Sesgo")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.biasDetection.create.subtitle", "Complete el formulario para registrar una nueva detección de sesgo")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => router.push("/governance/agents/bias-detection/overview")}
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
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.biasDetection.create.saveDraft", "Guardar Borrador")}
          </Button>
          <Button
            type="button"
            onClick={(e) => handleSubmit(e, "submit")}
            variant="primary"
            disabled={isSubmitting}
          >
            <Save className="h-4 w-4 mr-2" />
            {isSubmitting
              ? t("common.saving", "Guardando...")
              : t("agents.biasDetection.create.submitForReview", "Enviar para Revisión")}
          </Button>
        </div>
      </div>

      {/* Formulario */}
      <form onSubmit={(e) => handleSubmit(e, "draft")}>
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("agents.biasDetection.create.formTitle", "Información de la Detección de Sesgo")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            {/* Grid compacto de campos */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {/* Agent UUID */}
              <div className="space-y-1">
                <Label htmlFor="agentUuid" className="text-sm">
                  {t("agents.biasDetection.create.agentUuid", "Agente")} *
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
                      : t("agents.biasDetection.create.agentUuidPlaceholder", "Seleccione un agente activo")}
                  </option>
                  {agents.map((agent) => (
                    <option key={agent.id} value={agent.uuid || agent.id.toString()}>
                      {agent.name} ({agent.uuid || `ID: ${agent.id}`})
                    </option>
                  ))}
                </select>
              </div>

              {/* Bias Type */}
              <div className="space-y-1">
                <Label htmlFor="biasType" className="text-sm">
                  {t("agents.biasDetection.create.biasType", "Tipo de Sesgo")} *
                </Label>
                <select
                  id="biasType"
                  value={formData.biasType}
                  onChange={(e) => setFormData({ ...formData, biasType: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="GENDER">{t("agents.biasDetection.create.typeGender", "Género")}</option>
                  <option value="RACIAL">{t("agents.biasDetection.create.typeRacial", "Racial")}</option>
                  <option value="AGE">{t("agents.biasDetection.create.typeAge", "Edad")}</option>
                  <option value="RELIGIOUS">{t("agents.biasDetection.create.typeReligious", "Religioso")}</option>
                  <option value="ECONOMIC">{t("agents.biasDetection.create.typeEconomic", "Económico")}</option>
                  <option value="GEOGRAPHIC">{t("agents.biasDetection.create.typeGeographic", "Geográfico")}</option>
                  <option value="EDUCATIONAL">{t("agents.biasDetection.create.typeEducational", "Educativo")}</option>
                  <option value="OTHER">{t("agents.biasDetection.create.typeOther", "Otro")}</option>
                </select>
              </div>

              {/* Bias Category */}
              <div className="space-y-1">
                <Label htmlFor="biasCategory" className="text-sm">
                  {t("agents.biasDetection.create.biasCategory", "Categoría de Sesgo")}
                </Label>
                <Input
                  id="biasCategory"
                  value={formData.biasCategory}
                  onChange={(e) => setFormData({ ...formData, biasCategory: e.target.value })}
                  placeholder={t("agents.biasDetection.create.biasCategoryPlaceholder", "Ej: Demographic, Algorithmic, Data")}
                  className="text-sm"
                />
              </div>

              {/* Severity */}
              <div className="space-y-1">
                <Label htmlFor="severity" className="text-sm">
                  {t("agents.biasDetection.create.severity", "Severidad")} *
                </Label>
                <select
                  id="severity"
                  value={formData.severity}
                  onChange={(e) => setFormData({ ...formData, severity: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="LOW">{t("common.low", "Bajo")}</option>
                  <option value="MEDIUM">{t("common.medium", "Medio")}</option>
                  <option value="HIGH">{t("common.high", "Alto")}</option>
                  <option value="CRITICAL">{t("agents.biasDetection.create.severityCritical", "Crítico")}</option>
                </select>
              </div>

              {/* Priority */}
              <div className="space-y-1">
                <Label htmlFor="priority" className="text-sm">
                  {t("agents.biasDetection.create.priority", "Prioridad")}
                </Label>
                <select
                  id="priority"
                  value={formData.priority}
                  onChange={(e) => setFormData({ ...formData, priority: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="LOW">{t("common.low", "Bajo")}</option>
                  <option value="MEDIUM">{t("common.medium", "Medio")}</option>
                  <option value="HIGH">{t("common.high", "Alto")}</option>
                  <option value="CRITICAL">{t("agents.biasDetection.create.priorityCritical", "Crítico")}</option>
                </select>
              </div>

              {/* Confidence Score */}
              <div className="space-y-1">
                <Label htmlFor="confidenceScore" className="text-sm">
                  {t("agents.biasDetection.create.confidenceScore", "Puntuación de Confianza")}
                </Label>
                <Input
                  id="confidenceScore"
                  type="number"
                  min="0"
                  max="1"
                  step="0.01"
                  value={formData.confidenceScore}
                  onChange={(e) => setFormData({ ...formData, confidenceScore: e.target.value })}
                  placeholder={t("agents.biasDetection.create.confidenceScorePlaceholder", "0.00 - 1.00")}
                  className="text-sm"
                />
              </div>

              {/* Bias Score */}
              <div className="space-y-1">
                <Label htmlFor="biasScore" className="text-sm">
                  {t("agents.biasDetection.create.biasScore", "Puntuación de Sesgo")}
                </Label>
                <Input
                  id="biasScore"
                  type="number"
                  min="0"
                  max="1"
                  step="0.01"
                  value={formData.biasScore}
                  onChange={(e) => setFormData({ ...formData, biasScore: e.target.value })}
                  placeholder={t("agents.biasDetection.create.biasScorePlaceholder", "0.00 - 1.00")}
                  className="text-sm"
                />
              </div>

              {/* Detection Method */}
              <div className="space-y-1">
                <Label htmlFor="detectionMethod" className="text-sm">
                  {t("agents.biasDetection.create.detectionMethod", "Método de Detección")}
                </Label>
                <select
                  id="detectionMethod"
                  value={formData.detectionMethod}
                  onChange={(e) => setFormData({ ...formData, detectionMethod: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="STATISTICAL">{t("agents.biasDetection.create.methodStatistical", "Estadístico")}</option>
                  <option value="MACHINE_LEARNING">{t("agents.biasDetection.create.methodMachineLearning", "Machine Learning")}</option>
                  <option value="MANUAL">{t("agents.biasDetection.create.methodManual", "Manual")}</option>
                  <option value="HYBRID">{t("agents.biasDetection.create.methodHybrid", "Híbrido")}</option>
                </select>
              </div>

              {/* Detection Algorithm */}
              <div className="space-y-1">
                <Label htmlFor="detectionAlgorithm" className="text-sm">
                  {t("agents.biasDetection.create.detectionAlgorithm", "Algoritmo de Detección")}
                </Label>
                <select
                  id="detectionAlgorithm"
                  value={formData.detectionAlgorithm}
                  onChange={(e) => setFormData({ ...formData, detectionAlgorithm: e.target.value })}
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="">{t("agents.biasDetection.create.detectionAlgorithmPlaceholder", "Seleccione un algoritmo")}</option>
                  <option value="DISPARATE_IMPACT_ANALYSIS">{t("agents.biasDetection.create.algorithmDisparateImpact", "Análisis de Impacto Dispar (Disparate Impact Analysis)")}</option>
                  <option value="FAIRNESS_METRICS">{t("agents.biasDetection.create.algorithmFairnessMetrics", "Métricas de Equidad (Fairness Metrics)")}</option>
                  <option value="DEMOGRAPHIC_PARITY">{t("agents.biasDetection.create.algorithmDemographicParity", "Paridad Demográfica (Demographic Parity)")}</option>
                  <option value="EQUALIZED_ODDS">{t("agents.biasDetection.create.algorithmEqualizedOdds", "Probabilidades Igualadas (Equalized Odds)")}</option>
                  <option value="CALIBRATION">{t("agents.biasDetection.create.algorithmCalibration", "Calibración (Calibration)")}</option>
                  <option value="INDIVIDUAL_FAIRNESS">{t("agents.biasDetection.create.algorithmIndividualFairness", "Equidad Individual (Individual Fairness)")}</option>
                  <option value="ADVERSARIAL_DEBIASING">{t("agents.biasDetection.create.algorithmAdversarialDebiasing", "Eliminación de Sesgo Adversarial (Adversarial Debiasing)")}</option>
                  <option value="REWEIGHTING">{t("agents.biasDetection.create.algorithmReweighting", "Reponderación (Reweighting)")}</option>
                  <option value="PREPROCESSING_FAIRNESS">{t("agents.biasDetection.create.algorithmPreprocessingFairness", "Equidad de Preprocesamiento (Preprocessing Fairness)")}</option>
                  <option value="POSTPROCESSING_FAIRNESS">{t("agents.biasDetection.create.algorithmPostprocessingFairness", "Equidad de Postprocesamiento (Postprocessing Fairness)")}</option>
                  <option value="SHAP_FAIRNESS">{t("agents.biasDetection.create.algorithmShapFairness", "SHAP para Equidad (SHAP Fairness)")}</option>
                  <option value="LIME_FAIRNESS">{t("agents.biasDetection.create.algorithmLimeFairness", "LIME para Equidad (LIME Fairness)")}</option>
                  <option value="OTHER">{t("agents.biasDetection.create.algorithmOther", "Otro")}</option>
                </select>
              </div>

              {/* Detected At */}
              <div className="space-y-1">
                <Label htmlFor="detectedAt" className="text-sm">
                  {t("agents.biasDetection.create.detectedAt", "Fecha de Detección")} *
                </Label>
                <Input
                  id="detectedAt"
                  type="date"
                  value={formData.detectedAt}
                  onChange={(e) => setFormData({ ...formData, detectedAt: e.target.value })}
                  required
                  className="text-sm"
                />
              </div>

              {/* Status */}
              <div className="space-y-1">
                <Label htmlFor="status" className="text-sm">
                  {t("agents.biasDetection.create.status", "Estado")} *
                </Label>
                <select
                  id="status"
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                  required
                  className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background focus:outline-none focus:ring-2 focus:ring-primary"
                >
                  <option value="DRAFT">{t("agents.biasDetection.create.statusDraft", "Borrador")}</option>
                  <option value="PENDING">{t("common.pending", "Pendiente")}</option>
                  <option value="ACTIVE">{t("common.active", "Activo")}</option>
                  <option value="RESOLVED">{t("agents.biasDetection.create.statusResolved", "Resuelto")}</option>
                </select>
              </div>
            </div>

            {/* Description */}
            <div className="space-y-1">
              <Label htmlFor="description" className="text-sm">
                {t("agents.biasDetection.create.description", "Descripción")}
              </Label>
              <Textarea
                id="description"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                placeholder={t("agents.biasDetection.create.descriptionPlaceholder", "Describa el sesgo detectado y su impacto")}
                rows={4}
                className="text-sm"
              />
            </div>

            {/* Affected Content */}
            <div className="space-y-1">
              <Label htmlFor="affectedContent" className="text-sm">
                {t("agents.biasDetection.create.affectedContent", "Contenido Afectado")}
              </Label>
              <Textarea
                id="affectedContent"
                value={formData.affectedContent}
                onChange={(e) => setFormData({ ...formData, affectedContent: e.target.value })}
                placeholder={t("agents.biasDetection.create.affectedContentPlaceholder", "Describa qué contenido o decisiones están afectadas por el sesgo")}
                rows={3}
                className="text-sm"
              />
            </div>
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
