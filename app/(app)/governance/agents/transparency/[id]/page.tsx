"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { ArrowLeft, Eye, Save, Edit } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter, useParams, useSearchParams } from "next/navigation";

interface TransparencyFormData {
  agentUuid?: string;
  transparencyType: string;
  transparencyScore?: string;
  explainabilityScore?: string;
  algorithmDisclosure?: string;
  explanationMethod?: string;
  certificationStatus?: string;
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

export default function TransparencyDetailPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const searchParams = useSearchParams();
  const transparencyId = params?.id as string;
  const isEditMode = searchParams?.get("edit") === "true";

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [formData, setFormData] = useState<TransparencyFormData>({
    agentUuid: "",
    transparencyType: "DECISION",
    certificationStatus: "PENDING",
  });

  useEffect(() => {
    if (transparencyId) {
      loadTransparencyData();
    }
    loadActiveAgents();
  }, [transparencyId]);

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

  const loadTransparencyData = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/governance/agents/transparency/${transparencyId}`);
      if (response.ok) {
        const data = await response.json();
        setFormData({
          agentUuid: data.agentUuid || "",
          transparencyType: data.transparencyType || "DECISION",
          transparencyScore: data.transparencyScore?.toString() || "",
          explainabilityScore: data.explainabilityScore?.toString() || "",
          algorithmDisclosure: data.algorithmDisclosure || "",
          explanationMethod: data.explanationMethod || "",
          certificationStatus: data.certificationStatus || "PENDING",
          createdBy: data.createdBy || "",
          createdAt: data.createdAt || "",
          updatedBy: data.updatedBy || "",
          updatedAt: data.updatedAt || "",
        });
      }
    } catch (error) {
      console.error("Error loading transparency:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const currentUser = await getCurrentUser();
      const response = await fetch(`/api/governance/agents/transparency/${transparencyId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...formData, updatedBy: currentUser }),
      });
      if (response.ok) {
        router.push(`/governance/agents/transparency/${transparencyId}`);
      }
    } catch (error) {
      console.error("Error updating transparency:", error);
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
            <Eye className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold">{isEditMode ? t("agents.transparency.edit.title", "Editar Transparencia") : t("agents.transparency.detail.title", "Detalles de Transparencia")}</h1>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={() => router.push(isEditMode ? `/governance/agents/transparency/${transparencyId}` : "/governance/agents/transparency/overview")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {isEditMode ? t("common.cancel", "Cancelar") : t("common.back", "Volver")}
          </Button>
          {!isEditMode && (
            <Button variant="outline" size="sm" onClick={() => router.push(`/governance/agents/transparency/${transparencyId}?edit=true`)}>
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
            <CardTitle>{isEditMode ? t("agents.transparency.edit.formTitle", "Editar Transparencia") : t("agents.transparency.detail.formTitle", "Información de Transparencia")}</CardTitle>
          </CardHeader>
          <CardBody className="space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.transparency.create.agentUuid", "Agente")}</Label>
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
                <Label className="text-sm">{t("agents.transparency.create.transparencyType", "Tipo")}</Label>
                {isEditMode ? (
                  <select value={formData.transparencyType} onChange={(e) => setFormData({ ...formData, transparencyType: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    <option value="DECISION">Decisión</option>
                    <option value="PROCESS">Proceso</option>
                    <option value="ALGORITHM">Algoritmo</option>
                  </select>
                ) : (
                  <p className="text-base">{formData.transparencyType}</p>
                )}
              </div>
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.transparency.create.transparencyScore", "Puntuación")}</Label>
                {isEditMode ? (
                  <Input type="number" min="0" max="100" value={formData.transparencyScore} onChange={(e) => setFormData({ ...formData, transparencyScore: e.target.value })} />
                ) : (
                  <p className="text-base">{formData.transparencyScore || "-"}%</p>
                )}
              </div>
              <div className="space-y-1">
                <Label className="text-sm">{t("agents.transparency.create.certificationStatus", "Estado")}</Label>
                {isEditMode ? (
                  <select value={formData.certificationStatus} onChange={(e) => setFormData({ ...formData, certificationStatus: e.target.value })} className="w-full px-3 py-2 text-sm border rounded-lg">
                    <option value="PENDING">Pendiente</option>
                    <option value="CERTIFIED">Certificado</option>
                    <option value="REJECTED">Rechazado</option>
                  </select>
                ) : (
                  <p className="text-base">{formData.certificationStatus}</p>
                )}
              </div>
            </div>
            {formData.createdBy && (
              <div className="pt-4 border-t space-y-3">
                <h3 className="text-sm font-semibold">{t("agents.transparency.audit.title", "Auditoría")}</h3>
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.transparency.audit.createdBy", "Creado por")}</Label>
                    <p className="text-sm">{formData.createdBy}</p>
                  </div>
                  <div>
                    <Label className="text-xs text-muted-foreground">{t("agents.transparency.audit.createdAt", "Fecha")}</Label>
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
