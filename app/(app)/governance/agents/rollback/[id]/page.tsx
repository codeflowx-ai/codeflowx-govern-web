"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, RotateCcw, CheckCircle, XCircle, AlertTriangle, FileText } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";

interface RollbackFormData {
  agentUuid?: string;
  agentName?: string;
  rollbackType: string;
  rollbackStatus: string;
  fromVersion: string;
  toVersion: string;
  rollbackReason?: string;
  triggerEvent?: string;
  triggerThreshold?: number;
  triggerValue?: number;
  rollbackApprovedBy?: string;
  rollbackApprovedAt?: string;
  rollbackStartedAt?: string;
  rollbackCompletedAt?: string;
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

export default function RollbackDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const rollbackId = params?.id as string;

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [approvalComments, setApprovalComments] = useState("");
  const [formData, setFormData] = useState<RollbackFormData>({
    rollbackType: "VERSION",
    rollbackStatus: "PENDING_APPROVAL",
    fromVersion: "",
    toVersion: "",
  });

  useEffect(() => {
    if (rollbackId) {
      loadRollbackData();
    }
    loadActiveAgents();
  }, [rollbackId]);

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

  const loadRollbackData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/rollback/${rollbackId}`);
      if (response.ok) {
        const data = await response.json();
        const agent = agents.find((a) => a.uuid === data.agentUuid);
        setFormData({
          agentUuid: data.agentUuid || "",
          agentName: agent?.name || "",
          rollbackType: data.rollbackType || "VERSION",
          rollbackStatus: data.rollbackStatus || "PENDING_APPROVAL",
          fromVersion: data.fromVersion || "",
          toVersion: data.toVersion || "",
          rollbackReason: data.rollbackReason || "",
          triggerEvent: data.triggerEvent || "",
          triggerThreshold: data.triggerThreshold,
          triggerValue: data.triggerValue,
          rollbackApprovedBy: data.rollbackApprovedBy || "",
          rollbackApprovedAt: data.rollbackApprovedAt || "",
          rollbackStartedAt: data.rollbackStartedAt || "",
          rollbackCompletedAt: data.rollbackCompletedAt || "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
      } else {
        // Mock data para desarrollo
        const mockData = {
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          rollbackType: "VERSION",
          rollbackStatus: "PENDING_APPROVAL",
          fromVersion: "v2.1.0",
          toVersion: "v2.0.5",
          rollbackReason: "Degradación de métricas de precisión detectada automáticamente. La precisión cayó del 95% al 87% en las últimas 24 horas. Se recomienda revertir a la versión anterior que mantenía una precisión estable del 94%.",
          triggerEvent: "METRIC_DEGRADATION",
          triggerThreshold: 90,
          triggerValue: 87,
          createdBy: "system",
          createdAt: "2024-01-15T10:30:00",
        };
        setFormData(mockData);
      }
    } catch (error) {
      console.error("Error loading rollback:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleApproval = async (action: "APPROVE" | "REJECT" | "ESCALATE") => {
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const approvalData = {
        rollbackApprovedBy: action === "APPROVE" ? currentUser : undefined,
        rollbackApprovedAt: action === "APPROVE" ? new Date().toISOString() : undefined,
        rollbackStatus: action === "APPROVE" ? "APPROVED" : action === "REJECT" ? "REJECTED" : "ESCALATED",
        approvalComments: approvalComments,
        updatedBy: currentUser,
      };

      const response = await fetch(`/api/governance/agents/rollback/${rollbackId}/approve`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(approvalData),
      });

      if (response.ok) {
        // Si se aprueba, puede lanzar un proceso BPMN para ejecutar la reversión
        if (action === "APPROVE") {
          try {
            await fetch("/api/bpmn/process/start", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({
                processKey: "agent-rollback-execution",
                variables: {
                  rollbackId: rollbackId,
                  agentUuid: formData.agentUuid,
                  rollbackType: formData.rollbackType,
                  fromVersion: formData.fromVersion,
                  toVersion: formData.toVersion,
                  approvedBy: currentUser,
                },
              }),
            });
          } catch (bpmnError) {
            console.warn("Error al lanzar proceso BPMN de ejecución:", bpmnError);
          }
        }
        router.push("/governance/agents/rollback/overview");
      } else {
        const error = await response.json();
        alert(t("agents.rollback.approval.error", "Error al procesar la aprobación: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error processing approval:", error);
      alert(t("agents.rollback.approval.error", "Error al procesar la aprobación"));
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

  const requiresApproval = formData.rollbackStatus === "PENDING_APPROVAL";

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <RotateCcw className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold">{t("agents.rollback.detail.title", "Aprobación de Reversión Automática")}</h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.rollback.detail.subtitle", "Revisión HITL (Human-In-The-Loop) para aprobación de reversiones automáticas")}
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => router.push("/governance/agents/rollback/overview")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
        </div>
      </div>

      {/* Alerta si requiere aprobación urgente */}
      {requiresApproval && (
        <Card className="border-yellow-500 bg-yellow-500/10">
          <CardBody className="p-4">
            <div className="flex items-center gap-3">
              <AlertTriangle className="w-5 h-5 text-yellow-500" />
              <div>
                <p className="font-semibold text-yellow-500">
                  {t("agents.rollback.alert.requiresApproval", "Aprobación Requerida")}
                </p>
                <p className="text-sm text-muted-foreground">
                  {t("agents.rollback.alert.automaticRollback", "Esta reversión fue generada automáticamente y requiere aprobación humana antes de ejecutarse.")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Información de la Reversión */}
      <Card>
        <CardHeader>
          <CardTitle>{t("agents.rollback.detail.rollbackInfo", "Información de la Reversión")}</CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.agent", "Agente")}</Label>
              <p className="text-base">{formData.agentName || formData.agentUuid || "-"}</p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.rollbackType", "Tipo de Reversión")}</Label>
              <p className="text-base">
                <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                  {formData.rollbackType}
                </span>
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.fromVersion", "Desde Versión")}</Label>
              <p className="text-base font-mono">{formData.fromVersion || "-"}</p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.toVersion", "Hacia Versión")}</Label>
              <p className="text-base font-mono">{formData.toVersion || "-"}</p>
            </div>
            {formData.triggerEvent && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold">{t("agents.rollback.detail.triggerEvent", "Evento Disparador")}</Label>
                <div className="flex items-center gap-2">
                  <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                    {formData.triggerEvent}
                  </span>
                  {formData.triggerValue !== undefined && formData.triggerThreshold !== undefined && (
                    <span className="text-sm text-muted-foreground">
                      ({formData.triggerValue} / {formData.triggerThreshold})
                    </span>
                  )}
                </div>
              </div>
            )}
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.status", "Estado")}</Label>
              <p className="text-base">
                <span
                  className={`px-2 py-1 rounded text-xs ${
                    formData.rollbackStatus === "COMPLETED"
                      ? "bg-green-500/20 text-green-500"
                      : formData.rollbackStatus === "PENDING_APPROVAL"
                      ? "bg-yellow-500/20 text-yellow-500"
                      : formData.rollbackStatus === "APPROVED" || formData.rollbackStatus === "IN_PROGRESS"
                      ? "bg-blue-500/20 text-blue-500"
                      : "bg-gray-500/20 text-gray-500"
                  }`}
                >
                  {formData.rollbackStatus}
                </span>
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.createdAt", "Fecha de Solicitud")}</Label>
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
            {formData.rollbackApprovedBy && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold">{t("agents.rollback.detail.approvedBy", "Aprobado por")}</Label>
                <p className="text-base">{formData.rollbackApprovedBy}</p>
                {formData.rollbackApprovedAt && (
                  <p className="text-xs text-muted-foreground">
                    {new Date(formData.rollbackApprovedAt).toLocaleString("es-ES")}
                  </p>
                )}
              </div>
            )}
          </div>

          {formData.rollbackReason && (
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.rollback.detail.rollbackReason", "Razón de la Reversión")}</Label>
              <p className="text-base whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.rollbackReason}</p>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Sección de Aprobación HITL */}
      {requiresApproval && (
        <Card className="border-primary">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="w-5 h-5 text-primary" />
              {t("agents.rollback.approval.title", "Aprobación Humana (HITL)")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="bg-blue-50 dark:bg-blue-950/20 p-4 rounded-lg border border-blue-200 dark:border-blue-800">
              <p className="text-sm text-blue-900 dark:text-blue-100">
                <strong>{t("agents.rollback.approval.notice", "Importante:")}</strong>{" "}
                {t("agents.rollback.approval.description", "Esta reversión fue generada automáticamente por el sistema debido a la detección de problemas. Por favor, revise la información y apruebe o rechace la reversión.")}
              </p>
            </div>

            <div className="space-y-2">
              <Label className="text-sm font-semibold">
                {t("agents.rollback.approval.comments", "Comentarios de Aprobación")} *
              </Label>
              <Textarea
                value={approvalComments}
                onChange={(e) => setApprovalComments(e.target.value)}
                placeholder={t("agents.rollback.approval.commentsPlaceholder", "Ingrese sus comentarios sobre esta reversión. Explique si está de acuerdo o no con la reversión propuesta y por qué.")}
                rows={6}
                className="text-sm"
                required
              />
            </div>

            <div className="flex gap-3 pt-4 border-t">
              <Button
                type="button"
                onClick={() => handleApproval("APPROVE")}
                variant="primary"
                disabled={isSubmitting || !approvalComments.trim()}
                className="flex-1"
              >
                <CheckCircle className="h-4 w-4 mr-2" />
                {t("agents.rollback.approval.approve", "Aprobar Reversión")}
              </Button>
              <Button
                type="button"
                onClick={() => handleApproval("REJECT")}
                variant="danger"
                disabled={isSubmitting || !approvalComments.trim()}
                className="flex-1"
              >
                <XCircle className="h-4 w-4 mr-2" />
                {t("agents.rollback.approval.reject", "Rechazar Reversión")}
              </Button>
              <Button
                type="button"
                onClick={() => handleApproval("ESCALATE")}
                variant="outline"
                disabled={isSubmitting || !approvalComments.trim()}
                className="flex-1"
              >
                <AlertTriangle className="h-4 w-4 mr-2" />
                {t("agents.rollback.approval.escalate", "Escalar")}
              </Button>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Información de Aprobación Existente */}
      {formData.rollbackApprovedBy && (
        <Card>
          <CardHeader>
            <CardTitle>{t("agents.rollback.approval.existingApproval", "Aprobación Realizada")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.rollback.approval.approvedBy", "Aprobado por")}</Label>
                <p className="text-sm font-medium">{formData.rollbackApprovedBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.rollback.approval.approvedAt", "Fecha de Aprobación")}</Label>
                <p className="text-sm">
                  {formData.rollbackApprovedAt
                    ? new Date(formData.rollbackApprovedAt).toLocaleString("es-ES", {
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

      {/* Auditoría */}
      {formData.createdBy && (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm">{t("agents.rollback.audit.title", "Registro de Auditoría")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.rollback.audit.createdBy", "Creado por")}</Label>
                <p className="text-sm">{formData.createdBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.rollback.audit.createdAt", "Fecha de creación")}</Label>
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
