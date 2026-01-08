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

interface BiasDetectionFormData {
  agentUuid?: string;
  biasType: string;
  biasCategory?: string;
  severity: string;
  confidenceScore?: string;
  biasScore?: string;
  description?: string;
  detectionMethod?: string;
  detectionAlgorithm?: string;
  status: string;
  priority?: string;
  detectedAt: string;
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

export default function BiasDetectionDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const detectionId = params?.id as string;
  const isEditMode = searchParams?.get("edit") === "true";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [formData, setFormData] = useState<BiasDetectionFormData>({
    agentUuid: "",
    biasType: "GENDER",
    severity: "MEDIUM",
    status: "ACTIVE",
    priority: "MEDIUM",
    detectedAt: new Date().toISOString().split("T")[0],
  });

  useEffect(() => {
    if (detectionId) {
      loadDetectionData();
    }
    loadActiveAgents();
  }, [detectionId]);

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

  const loadDetectionData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/bias-detection/${detectionId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || "",
          biasType: data.biasType || "GENDER",
          biasCategory: data.biasCategory || "",
          severity: data.severity || "MEDIUM",
          confidenceScore: data.confidenceScore?.toString() || "",
          biasScore: data.biasScore?.toString() || "",
          description: data.description || "",
          detectionMethod: data.detectionMethod || "",
          detectionAlgorithm: data.detectionAlgorithm || "",
          status: data.status || "ACTIVE",
          priority: data.priority || "MEDIUM",
          detectedAt: data.detectedAt ? new Date(data.detectedAt).toISOString().split("T")[0] : new Date().toISOString().split("T")[0],
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
      }
    } catch (error) {
      console.error("Error loading detection:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const response = await fetch(`/api/governance/agents/bias-detection/${detectionId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...formData, updatedBy: currentUser }),
      });
      if (response.ok) {
        router.push(`/governance/agents/bias-detection/${detectionId}`);
      }
    } catch (error) {
      console.error("Error updating detection:", error);
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

  return (
    <div className="space-y-4 p-6">
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold">{isEditMode ? t("agents.biasDetection.edit.title", "Editar Detección") : t("agents.biasDetection.detail.title", "Detalles de Detección")}</h1>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => router.push(isEditMode ? `/governance/agents/bias-detection/${detectionId}` : "/governance/agents/bias-detection/overview")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {isEditMode ? t("common.cancel", "Cancelar") : t("common.back", "Volver")}
          </Button>
          {!isEditMode && (
            <Button variant="outline" size="sm" onClick={() => router.push(`/governance/agents/bias-detection/${detectionId}?edit=true`)}>
              <Edit className="h-4 w-4 mr-2" />
              {t("common.edit", "Editar")}
            </Button>
          )}
          {isEditMode && (
            <Button type="button" onClick={handleSubmit} variant="primary" disabled={isSubmitting}>
              <Save className="h-4 w-4 mr-2" />
              {t("common.save", "Guardar")}
            </Button>
          )}
        </div>
      </div>

      <form onSubmit={handleSubmit}>
        <Card>
          <CardHeader>
            <CardTitle>{isEditMode ? t("agents.biasDetection.edit.formTitle", "Editar Detección") : t("agents.biasDetection.detail.formTitle", "Información de Detección")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.biasDetection.create.agentUuid", "Agente")}</Label>
                {isEditMode ? (
                  <select value={formData.agentUuid} onChange={(e) => setFormData({ ...formData, agentUuid: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    {agents.map((agent) => (
                      <option key={agent.id} value={agent.uuid}>{agent.name}</option>
                    ))}
                  </select>
                ) : (
                  <p className="text-base">{agents.find((a) => a.uuid === formData.agentUuid)?.name || formData.agentUuid || "-"}</p>
                )}
              </div>
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.biasDetection.create.biasType", "Tipo de Sesgo")}</Label>
                {isEditMode ? (
                  <select value={formData.biasType} onChange={(e) => setFormData({ ...formData, biasType: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    <option value="GENDER">Género</option>
                    <option value="RACIAL">Racial</option>
                    <option value="AGE">Edad</option>
                    <option value="OTHER">Otro</option>
                  </select>
                ) : (
                  <p className="text-base">{formData.biasType}</p>
                )}
              </div>
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.biasDetection.create.severity", "Severidad")}</Label>
                {isEditMode ? (
                  <select value={formData.severity} onChange={(e) => setFormData({ ...formData, severity: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    <option value="LOW">Bajo</option>
                    <option value="MEDIUM">Medio</option>
                    <option value="HIGH">Alto</option>
                    <option value="CRITICAL">Crítico</option>
                  </select>
                ) : (
                  <p className="text-base">{formData.severity}</p>
                )}
              </div>
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.biasDetection.create.status", "Estado")}</Label>
                {isEditMode ? (
                  <select value={formData.status} onChange={(e) => setFormData({ ...formData, status: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    <option value="ACTIVE">Activo</option>
                    <option value="PENDING">Pendiente</option>
                    <option value="RESOLVED">Resuelto</option>
                  </select>
                ) : (
                  <p className="text-base">{formData.status}</p>
                )}
              </div>
            </div>
            {formData.description && (
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.biasDetection.create.description", "Descripción")}</Label>
                {isEditMode ? (
                  <Textarea value={formData.description} onChange={(e) => setFormData({ ...formData, description: e.target.value })} rows={4} />
                ) : (
                  <p className="text-base">{formData.description}</p>
                )}
              </div>
            )}
            {formData.createdBy && (
              <div className="pt-4 border-t space-y-3">
                <h3 className="text-sm font-semibold">{t("agents.biasDetection.audit.title", "Auditoría")}</h3>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.biasDetection.audit.createdBy", "Creado por")}</Label>
                    <p className="text-sm">{formData.createdBy}</p>
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.biasDetection.audit.createdAt", "Fecha de creación")}</Label>
                    <p className="text-sm">{formData.createdAt ? new Date(formData.createdAt).toLocaleString("es-ES") : "-"}</p>
                  </div>
                </div>
              </div>
            )}
          </CardBody>
        </Card>
      </form>
    </div>
  );
}
