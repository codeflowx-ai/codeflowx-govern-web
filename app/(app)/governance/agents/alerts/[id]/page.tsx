"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, AlertTriangle, CheckCircle, XCircle, Clock, User, FileText } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { Badge } from "@/components/ui/badge";

interface AlertFormData {
  agentUuid?: string;
  agentName?: string;
  alertType: string;
  alertCategory?: string;
  severity: string;
  status: string;
  priority?: string;
  impactLevel?: string;
  urgencyLevel?: string;
  title: string;
  description?: string;
  message?: string;
  triggerCondition?: string;
  triggerValue?: number;
  thresholdValue?: number;
  triggeredAt?: string;
  acknowledgedAt?: string;
  acknowledgedBy?: string;
  resolvedAt?: string;
  resolvedBy?: string;
  resolutionNotes?: string;
  escalatedAt?: string;
  escalatedTo?: string;
  escalationReason?: string;
  suppressedUntil?: string;
  suppressionReason?: string;
  createdBy?: string;
  createdAt?: string;
  updatedBy?: string;
  updatedAt?: string;
}

export default function AlertDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const alertId = params?.id as string;

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [resolutionNotes, setResolutionNotes] = useState("");
  const [formData, setFormData] = useState<AlertFormData>({
    alertType: "PERFORMANCE_DEGRADATION",
    severity: "MEDIUM",
    status: "TRIGGERED",
    title: "",
  });

  useEffect(() => {
    if (alertId) {
      loadAlertData();
    }
  }, [alertId]);

  const loadAlertData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/alerts/${alertId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || "",
          agentName: data.agentName || "",
          alertType: data.alertType || "PERFORMANCE_DEGRADATION",
          alertCategory: data.alertCategory || "",
          severity: data.severity || "MEDIUM",
          status: data.status || "TRIGGERED",
          priority: data.priority || "",
          impactLevel: data.impactLevel || "",
          urgencyLevel: data.urgencyLevel || "",
          title: data.title || "",
          description: data.description || "",
          message: data.message || "",
          triggerCondition: data.triggerCondition || "",
          triggerValue: data.triggerValue,
          thresholdValue: data.thresholdValue,
          triggeredAt: data.triggeredAt || "",
          acknowledgedAt: data.acknowledgedAt || "",
          acknowledgedBy: data.acknowledgedBy || "",
          resolvedAt: data.resolvedAt || "",
          resolvedBy: data.resolvedBy || "",
          resolutionNotes: data.resolutionNotes || "",
          escalatedAt: data.escalatedAt || "",
          escalatedTo: data.escalatedTo || "",
          escalationReason: data.escalationReason || "",
          suppressedUntil: data.suppressedUntil || "",
          suppressionReason: data.suppressionReason || "",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
        setResolutionNotes(data.resolutionNotes || "");
      } else {
        // Mock data para desarrollo
        const mockData = {
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          alertType: "PERFORMANCE_DEGRADATION",
          alertCategory: "PERFORMANCE",
          severity: "HIGH",
          status: "TRIGGERED",
          priority: "HIGH",
          impactLevel: "HIGH",
          urgencyLevel: "HIGH",
          title: "Degradación de Precisión Detectada",
          description: "La precisión del agente ha caído del 95% al 87% en las últimas 24 horas. Esto puede indicar un problema con el modelo o cambios en los datos de entrada.",
          message: "Se recomienda revisar el modelo y considerar un rollback a una versión anterior si el problema persiste.",
          triggerCondition: "accuracy < 90",
          triggerValue: 87,
          thresholdValue: 90,
          triggeredAt: "2024-01-15T10:30:00",
          createdBy: "system",
          createdAt: "2024-01-15T10:30:00",
        };
        setFormData(mockData);
      }
    } catch (error) {
      console.error("Error loading alert:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAcknowledge = async () => {
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const response = await fetch(`/api/governance/agents/alerts/${alertId}/acknowledge`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          acknowledgedBy: currentUser,
        }),
      });
      if (response.ok) {
        router.push("/governance/agents/alerts/overview");
      }
    } catch (error) {
      console.error("Error acknowledging alert:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleResolve = async () => {
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const response = await fetch(`/api/governance/agents/alerts/${alertId}/resolve`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          resolvedBy: currentUser,
          resolutionNotes: resolutionNotes,
        }),
      });
      if (response.ok) {
        router.push("/governance/agents/alerts/overview");
      }
    } catch (error) {
      console.error("Error resolving alert:", error);
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

  const canAcknowledge = formData.status === "TRIGGERED";
  const canResolve = formData.status === "TRIGGERED" || formData.status === "ACKNOWLEDGED";
  const severityColor =
    formData.severity === "CRITICAL"
      ? "bg-red-500/20 text-red-500 border-red-500/50"
      : formData.severity === "HIGH"
      ? "bg-orange-500/20 text-orange-500 border-orange-500/50"
      : formData.severity === "MEDIUM"
      ? "bg-yellow-500/20 text-yellow-500 border-yellow-500/50"
      : "bg-gray-500/20 text-gray-500 border-gray-500/50";

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <AlertTriangle className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold">{t("agents.alerts.detail.title", "Detalles de Alerta")}</h1>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => router.push("/governance/agents/alerts/overview")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
        </div>
      </div>

      {/* Información Principal de la Alerta */}
      <Card>
        <CardHeader>
          <CardTitle>{t("agents.alerts.detail.alertInfo", "Información de la Alerta")}</CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.agent", "Agente")}</Label>
              <p className="text-base">{formData.agentName || formData.agentUuid || "-"}</p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.title", "Título")}</Label>
              <p className="text-base font-semibold">{formData.title}</p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.alertType", "Tipo de Alerta")}</Label>
              <p className="text-base">
                <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                  {formData.alertType}
                </span>
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.alertCategory", "Categoría")}</Label>
              <p className="text-base">
                {formData.alertCategory ? (
                  <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                    {formData.alertCategory}
                  </span>
                ) : (
                  "-"
                )}
              </p>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.severity", "Severidad")}</Label>
              <Badge className={severityColor}>{formData.severity}</Badge>
            </div>
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.status", "Estado")}</Label>
              <p className="text-base">
                <span
                  className={`px-2 py-1 rounded text-xs ${
                    formData.status === "RESOLVED"
                      ? "bg-green-500/20 text-green-500"
                      : formData.status === "ACKNOWLEDGED"
                      ? "bg-blue-500/20 text-blue-500"
                      : formData.status === "TRIGGERED"
                      ? "bg-red-500/20 text-red-500"
                      : "bg-yellow-500/20 text-yellow-500"
                  }`}
                >
                  {formData.status}
                </span>
              </p>
            </div>
            {formData.priority && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold">{t("agents.alerts.detail.priority", "Prioridad")}</Label>
                <p className="text-base">{formData.priority}</p>
              </div>
            )}
            {formData.impactLevel && (
              <div className="space-y-1">
                <Label className="text-sm font-semibold">{t("agents.alerts.detail.impactLevel", "Nivel de Impacto")}</Label>
                <p className="text-base">{formData.impactLevel}</p>
              </div>
            )}
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.triggeredAt", "Fecha de Disparo")}</Label>
              <p className="text-base">
                {formData.triggeredAt
                  ? new Date(formData.triggeredAt).toLocaleString("es-ES", {
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

          {formData.description && (
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.description", "Descripción")}</Label>
              <p className="text-base whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.description}</p>
            </div>
          )}

          {formData.message && (
            <div className="space-y-1">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.message", "Mensaje")}</Label>
              <p className="text-base whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.message}</p>
            </div>
          )}

          {/* Condiciones de Disparo */}
          {(formData.triggerCondition || formData.triggerValue !== undefined || formData.thresholdValue !== undefined) && (
            <div className="space-y-2 pt-4 border-t">
              <Label className="text-sm font-semibold">{t("agents.alerts.detail.triggerConditions", "Condiciones de Disparo")}</Label>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                {formData.triggerCondition && (
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.triggerCondition", "Condición")}</Label>
                    <p className="text-sm font-mono bg-muted/50 p-2 rounded">{formData.triggerCondition}</p>
                  </div>
                )}
                {formData.triggerValue !== undefined && (
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.triggerValue", "Valor Disparado")}</Label>
                    <p className="text-sm font-semibold">{formData.triggerValue}</p>
                  </div>
                )}
                {formData.thresholdValue !== undefined && (
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.thresholdValue", "Umbral")}</Label>
                    <p className="text-sm font-semibold">{formData.thresholdValue}</p>
                  </div>
                )}
              </div>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Acciones */}
      {(canAcknowledge || canResolve) && (
        <Card className="border-primary">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="w-5 h-5 text-primary" />
              {t("agents.alerts.detail.actions", "Acciones")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            {canAcknowledge && (
              <div className="space-y-2">
                <Button
                  type="button"
                  onClick={handleAcknowledge}
                  variant="primary"
                  disabled={isSubmitting}
                  className="w-full"
                >
                  <CheckCircle className="h-4 w-4 mr-2" />
                  {t("agents.alerts.detail.acknowledge", "Reconocer Alerta")}
                </Button>
              </div>
            )}

            {canResolve && (
              <div className="space-y-2">
                <Label className="text-sm font-semibold">
                  {t("agents.alerts.detail.resolutionNotes", "Notas de Resolución")} *
                </Label>
                <Textarea
                  value={resolutionNotes}
                  onChange={(e) => setResolutionNotes(e.target.value)}
                  placeholder={t("agents.alerts.detail.resolutionNotesPlaceholder", "Ingrese las notas sobre cómo se resolvió esta alerta...")}
                  rows={4}
                  className="text-sm"
                  required
                />
                <Button
                  type="button"
                  onClick={handleResolve}
                  variant="primary"
                  disabled={isSubmitting || !resolutionNotes.trim()}
                  className="w-full"
                >
                  <CheckCircle className="h-4 w-4 mr-2" />
                  {t("agents.alerts.detail.resolve", "Resolver Alerta")}
                </Button>
              </div>
            )}
          </CardBody>
        </Card>
      )}

      {/* Información de Reconocimiento */}
      {formData.acknowledgedBy && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <User className="w-5 h-5" />
              {t("agents.alerts.detail.acknowledgment", "Reconocimiento")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.acknowledgedBy", "Reconocido por")}</Label>
                <p className="text-sm font-medium">{formData.acknowledgedBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.acknowledgedAt", "Fecha de Reconocimiento")}</Label>
                <p className="text-sm">
                  {formData.acknowledgedAt
                    ? new Date(formData.acknowledgedAt).toLocaleString("es-ES", {
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

      {/* Información de Resolución */}
      {formData.resolvedBy && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <CheckCircle className="w-5 h-5" />
              {t("agents.alerts.detail.resolution", "Resolución")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.resolvedBy", "Resuelto por")}</Label>
                <p className="text-sm font-medium">{formData.resolvedBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.resolvedAt", "Fecha de Resolución")}</Label>
                <p className="text-sm">
                  {formData.resolvedAt
                    ? new Date(formData.resolvedAt).toLocaleString("es-ES", {
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
            {formData.resolutionNotes && (
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.detail.resolutionNotes", "Notas de Resolución")}</Label>
                <p className="text-sm whitespace-pre-wrap bg-muted/50 p-3 rounded-lg">{formData.resolutionNotes}</p>
              </div>
            )}
          </CardBody>
        </Card>
      )}

      {/* Auditoría */}
      {formData.createdBy && (
        <Card>
          <CardHeader>
            <CardTitle className="text-sm">{t("agents.alerts.audit.title", "Registro de Auditoría")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.audit.createdBy", "Creado por")}</Label>
                <p className="text-sm">{formData.createdBy}</p>
              </div>
              <div>
                <Label className="text-xs text-muted-foreground">{t("agents.alerts.audit.createdAt", "Fecha de creación")}</Label>
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
