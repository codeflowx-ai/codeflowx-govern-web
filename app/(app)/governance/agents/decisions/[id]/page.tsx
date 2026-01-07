"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, GanttChartSquare, CheckCircle, XCircle, AlertTriangle, FileText } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";

interface DecisionFormData {
  agentUuid?: string;
  agentName?: string;
  decisionType: string;
  decisionReason?: string;
  inputData?: string;
  decisionData?: string;
  confidenceScore?: number;
  status: string;
  reviewedBy?: string;
  reviewedAt?: string;
  reviewComments?: string;
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

export default function DecisionDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const decisionId = params?.id as string;

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [reviewComments, setReviewComments] = useState("");
  const [formData, setFormData] = useState<DecisionFormData>({
    decisionType: "APPROVE",
    status: "PENDING",
  });
  const [showReviewSection, setShowReviewSection] = useState(false);

  useEffect(() => {
    if (decisionId) {
      loadDecisionData();
    }
    loadActiveAgents();
  }, [decisionId]);

  const loadActiveAgents = async () => {
    try {
      const response = await fetch("/api/governance/agents/registry");
      if (response.ok) {
        const data = await response.json();
        setAgents((data.items || data || []).filter((a: Agent) => a.status === "ACTIVE"));
      }
    } catch (error) {
      console.error("Error loading agents:", error);
    }
  };

  const loadDecisionData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/decisions/${decisionId}`);
      if (response.ok) {
        const data = await response.json();
        const agent = agents.find((a) => a.uuid === data.agentUuid);
        setFormData({
          agentUuid: data.agentUuid || "",
          agentName: agent?.name || "",
          decisionType: data.decisionType || "APPROVE",
          decisionReason: data.decisionReason || "",
          inputData: data.inputData || "",
          decisionData: data.decisionData || "",
          confidenceScore: data.confidenceScore,
          status: data.status || "PENDING",
          reviewedBy: data.reviewedBy || "",
          reviewedAt: data.reviewedAt || "",
          reviewComments: data.reviewComments || "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
        setReviewComments(data.reviewComments || "");
        setShowReviewSection(!data.reviewedBy); // Mostrar sección de revisión si no ha sido revisada
      } else {
        // Mock data para desarrollo
        const mockData = {
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          decisionType: "APPROVE",
          decisionReason: "La solicitud de crédito cumple con todos los criterios establecidos. Historial crediticio positivo, ingresos estables y relación deuda-ingresos dentro de los límites permitidos.",
          inputData: JSON.stringify({ applicantId: "12345", creditAmount: 50000, income: 75000, creditScore: 750 }, null, 2),
          decisionData: JSON.stringify({ approved: true, interestRate: 3.5, term: 60 }, null, 2),
          confidenceScore: 95,
          status: "PENDING",
          reviewedBy: "",
          reviewedAt: "",
          reviewComments: "",
          createdBy: "system",
          createdAt: "2024-01-15T10:00:00",
        };
        setFormData(mockData);
        setShowReviewSection(true);
      }
    } catch (error) {
      console.error("Error loading decision:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleReview = async (action: "APPROVE" | "REJECT" | "ESCALATE") => {
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const reviewData = {
        reviewedBy: currentUser,
        reviewedAt: new Date().toISOString(),
        reviewComments: reviewComments,
        status: action === "APPROVE" ? "REVIEWED" : action === "REJECT" ? "REJECTED" : "ESCALATED",
        updatedBy: currentUser,
      };

      const response = await fetch(`/api/governance/agents/decisions/${decisionId}/review`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(reviewData),
      });

      if (response.ok) {
        // Si se aprueba o rechaza, puede lanzar un proceso BPMN para cumplimiento
        if (action === "APPROVE" || action === "REJECT") {
          try {
            await fetch("/api/bpmn/process/start", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                processKey: "decision-compliance-review",
                variables: {
                  decisionId: decisionId,
                  agentUuid: formData.agentUuid,
                  decisionType: formData.decisionType,
                  reviewAction: action,
                  reviewer: currentUser,
                },
              }),
            });
          } catch (bpmnError) {
            console.warn("Error al lanzar proceso BPMN de cumplimiento:", bpmnError);
          }
        }
        router.push("/governance/agents/decisions/overview");
      } else {
        const error = await response.json();
        alert(t("agents.decisions.review.error", "Error al revisar la decisión: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error reviewing decision:", error);
      alert(t("agents.decisions.review.error", "Error al revisar la decisión"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const getCurrentUser = async (): Promise<string> => {
    try {
      const userResponse = await fetch("/api/auth/me");
      if (userResponse.ok) {
        const userData = await userResponse.json();
        return userData.username || userData.email || "system";
      }
    } catch (error) {
      console.warn("No se pudo obtener el usuario actual:", error);
    }
    return "system";
  };

  if (!mounted || loading) {
    return <div className="flex items-center justify-center h-64"><p>{t("common.loading", "Cargando...")}</p></div>;
  }

  const requiresReview = !formData.reviewedBy || formData.status === "PENDING";
  const isLowConfidence = formData.confidenceScore !== undefined && formData.confidenceScore < 70;

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <GanttChartSquare className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold">{t("agents.decisions.detail.title", "Revisión de Decisión del Agente")}</h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.decisions.detail.subtitle", "Revisión HITL (Human-In-The-Loop) para cumplimiento de la Ley IA Act")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => router.push("/governance/agents/decisions/overview")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
        </div>
      </div>

      {/* Alerta si requiere revisión urgente */}
      {requiresReview && isLowConfidence && (
        <Card className="border-yellow-500 bg-yellow-500/10">
          <CardBody className="p-4">
            <div className="flex items-center gap-3">
              <AlertTriangle className="w-5 h-5 text-yellow-500" />
              <div>
                <p className="font-semibold text-yellow-500">
                  {t("agents.decisions.alert.requiresUrgentReview", "Revisión Urgente Requerida")}
                </p>
                <p className="text-sm text-muted-foreground">
                  {t("agents.decisions.alert.lowConfidence", "Esta decisión tiene baja confianza y requiere revisión humana según los criterios de la Ley IA Act")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Información de la Decisión del Agente */}
      <Card>
        <CardHeader>
          <CardTitle>{t("agents.decisions.detail.decisionInfo", "Información de la Decisión")}</CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.agent", "Agente")}</Label>
              <p className="text-base">{formData.agentName || formData.agentUuid || "-"}</p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.decisionType", "Tipo de Decisión")}</Label>
              <p className="text-base">
                <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                  {formData.decisionType}
                </span>
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.confidenceScore", "Confianza del Agente")}</Label>
              <div className="flex items-center gap-2">
                <p className={`text-lg font-bold ${isLowConfidence ? "text-red-500" : formData.confidenceScore && formData.confidenceScore >= 90 ? "text-green-500" : "text-yellow-500"}`}>
                  {formData.confidenceScore !== undefined && formData.confidenceScore !== null ? `${formData.confidenceScore}%` : "-"}
                </p>
                {isLowConfidence && (
                  <AlertTriangle className="w-4 h-4 text-red-500" />
                )}
              </div>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.status", "Estado")}</Label>
              <p className="text-base">
                <span
                  className={`px-2 py-1 rounded text-xs ${
                    formData.status === "EXECUTED" || formData.status === "REVIEWED"
                      ? "bg-green-500/20 text-green-500"
                      : formData.status === "PENDING"
                      ? "bg-yellow-500/20 text-yellow-500"
                      : "bg-gray-500/20 text-gray-500"
                  }`}
                >
                  {formData.status}
                </span>
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.createdAt", "Fecha de Decisión")}</Label>
              <p className="text-base">
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
            {formData.reviewedBy && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold">{t("agents.decisions.detail.reviewedBy", "Revisado por")}</Label>
                <p className="text-base">{formData.reviewedBy}</p>
                {formData.reviewedAt && (
                  <p className="text-xs text-muted-foreground">
                    {new Date(formData.reviewedAt).toLocaleString("es-ES")}
                  </p>
                )}
              </div>
            )}
          </div>

          {formData.decisionReason && (
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.decisions.detail.decisionReason", "Razón de la Decisión")}</Label>
              <p className="text-base whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.decisionReason}</p>
            </div>
          )}

          {/* Datos de Entrada y Salida */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {formData.inputData && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold flex items-center gap-2">
                  <FileText className="w-4 h-4" />
                  {t("agents.decisions.detail.inputData", "Datos de Entrada")}
                </Label>
                <pre className="text-xs bg-muted/50 p-3 rounded-lg overflow-auto max-h-40">
                  {typeof formData.inputData === "string" ? formData.inputData : JSON.stringify(formData.inputData, null, 2)}
                </pre>
              </div>
            )}
            {formData.decisionData && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold flex items-center gap-2">
                  <FileText className="w-4 h-4" />
                  {t("agents.decisions.detail.decisionData", "Datos de Decisión")}
                </Label>
                <pre className="text-xs bg-muted/50 p-3 rounded-lg overflow-auto max-h-40">
                  {typeof formData.decisionData === "string" ? formData.decisionData : JSON.stringify(formData.decisionData, null, 2)}
                </pre>
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Sección de Revisión HITL */}
      {requiresReview && (
        <Card className="border-primary">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="w-5 h-5 text-primary" />
              {t("agents.decisions.review.title", "Revisión Humana (HITL)")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="bg-blue-50 dark:bg-blue-950/20 p-4 rounded-lg border border-blue-200 dark:border-blue-800">
              <p className="text-sm text-blue-900 dark:text-blue-100">
                <strong>{t("agents.decisions.review.iaActNotice", "Cumplimiento Ley IA Act:")}</strong>{" "}
                {t("agents.decisions.review.iaActDescription", "Esta decisión requiere revisión humana según los requisitos de transparencia y supervisión de la Ley IA Act. Por favor, revise la decisión del agente y proporcione su evaluación.")}
              </p>
            </div>

            <div className="space-y-2">
              <Label className="text-sm font-semibold">
                {t("agents.decisions.review.comments", "Comentarios de Revisión")} *
              </Label>
              <Textarea
                value={reviewComments}
                onChange={(e) => setReviewComments(e.target.value)}
                placeholder={t("agents.decisions.review.commentsPlaceholder", "Ingrese sus comentarios sobre esta decisión. Explique si está de acuerdo o no con la decisión del agente y por qué.")}
                rows={6}
                className="text-sm"
                required
              />
            </div>

            <div className="flex gap-3 pt-4 border-t">
              <Button
                type="button"
                onClick={() => handleReview("APPROVE")}
                variant="primary"
                disabled={isSubmitting || !reviewComments.trim()}
                className="flex-1"
              >
                <CheckCircle className="h-4 w-4 mr-2" />
                {t("agents.decisions.review.approve", "Aprobar Decisión")}
              </Button>
              <Button
                type="button"
                onClick={() => handleReview("REJECT")}
                variant="danger"
                disabled={isSubmitting || !reviewComments.trim()}
                className="flex-1"
              >
                <XCircle className="h-4 w-4 mr-2" />
                {t("agents.decisions.review.reject", "Rechazar Decisión")}
              </Button>
              <Button
                type="button"
                onClick={() => handleReview("ESCALATE")}
                variant="outline"
                disabled={isSubmitting || !reviewComments.trim()}
                className="flex-1"
              >
                <AlertTriangle className="h-4 w-4 mr-2" />
                {t("agents.decisions.review.escalate", "Escalar")}
              </Button>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Información de Revisión Existente */}
      {formData.reviewedBy && (
        <Card>
          <CardHeader>
            <CardTitle>{t("agents.decisions.review.existingReview", "Revisión Realizada")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.decisions.review.reviewedBy", "Revisado por")}</Label>
                <p className="text-sm font-medium">{formData.reviewedBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.decisions.review.reviewedAt", "Fecha de Revisión")}</Label>
                <p className="text-sm">
                  {formData.reviewedAt
                    ? new Date(formData.reviewedAt).toLocaleString("es-ES", {
                        year: "numeric",
                        month: "2-digit",
                        day: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                      })
                    : "-"}
                </p>
              </div>
            </div>
            {formData.reviewComments && (
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.decisions.review.comments", "Comentarios")}</Label>
                <p className="text-sm whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.reviewComments}</p>
              </div>
            )}
          </CardBody>
        </Card>
      )}

      {/* Auditoría */}
      {formData.createdBy && (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm">{t("agents.decisions.audit.title", "Registro de Auditoría")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.decisions.audit.createdBy", "Creado por")}</Label>
                <p className="text-sm">{formData.createdBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.decisions.audit.createdAt", "Fecha de creación")}</Label>
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
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}
