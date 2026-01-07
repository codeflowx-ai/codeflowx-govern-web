"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Award,
  CheckCircle,
  Clock,
  AlertTriangle,
  FileText,
  User,
  Calendar,
  Shield,
} from "lucide-react";
import { useEffect, useState } from "react";

interface CertificationStatus {
  status: string;
  certificateId?: string;
  certifiedAt?: string;
  expiryDate?: string;
  certifyingBody?: string;
  certifiedBy?: string;
  requirements: {
    classification: boolean;
    fria: boolean;
    euRegistration: boolean;
    conformityDeclaration: boolean;
    technicalDocs: boolean;
    qms: boolean;
  };
  notes?: string;
}

interface CertificationTabProps {
  agentId: string | number;
  agentUuid?: string;
  agentName?: string;
  agentStatus?: string;
}

export default function CertificationTab({
  agentId,
  agentUuid,
  agentName,
  agentStatus,
}: CertificationTabProps) {
  const { t, mounted } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [certification, setCertification] = useState<CertificationStatus | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    certifyingBody: "",
    expiryDate: "",
    notes: "",
  });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadCertificationData();
  }, [agentId, agentUuid]);

  const loadCertificationData = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamada API real
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Mock data
      setCertification({
        status: "CERTIFIED",
        certificateId: "CERT-2024-001234",
        certifiedAt: "2024-01-17T14:00:00",
        expiryDate: "2025-01-17T14:00:00",
        certifyingBody: "Internal Compliance Team",
        certifiedBy: "admin",
        requirements: {
          classification: true,
          fria: true,
          euRegistration: true,
          conformityDeclaration: true,
          technicalDocs: true,
          qms: true,
        },
        notes: "Certificado para uso en producción bajo supervisión HITL.",
      });

      setFormData({
        certifyingBody: "Internal Compliance Team",
        expiryDate: "2025-01-17",
        notes: "Certificado para uso en producción bajo supervisión HITL.",
      });
    } catch (error) {
      console.error("Error loading certification data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      // TODO: Implementar llamada API
      await new Promise((resolve) => setTimeout(resolve, 500));

      if (certification) {
        setCertification({
          ...certification,
          certifyingBody: formData.certifyingBody,
          expiryDate: formData.expiryDate ? `${formData.expiryDate}T00:00:00` : undefined,
          notes: formData.notes,
        });
      }
      setIsEditing(false);
    } catch (error) {
      console.error("Error saving certification:", error);
      alert("Error al guardar la certificación");
    } finally {
      setSaving(false);
    }
  };

  const handleStartCertification = async () => {
    try {
      setSaving(true);
      // TODO: Lanzar proceso BPMN de certificación
      await new Promise((resolve) => setTimeout(resolve, 500));
      alert("Proceso de certificación iniciado. Se requiere aprobación.");
    } catch (error) {
      console.error("Error starting certification:", error);
      alert("Error al iniciar el proceso de certificación");
    } finally {
      setSaving(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      CERTIFIED: "bg-green-500/20 text-green-500",
      PENDING: "bg-yellow-500/20 text-yellow-400",
      NOT_CERTIFIED: "bg-gray-500/20 text-gray-400",
      EXPIRED: "bg-red-500/20 text-red-400",
      REVOKED: "bg-red-500/20 text-red-400",
    };
    return (
      <Badge className={colors[status] || "bg-gray-500/20 text-gray-400"}>
        {status}
      </Badge>
    );
  };

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Award className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  const allRequirementsMet = certification?.requirements
    ? Object.values(certification.requirements).every((req) => req === true)
    : false;

  return (
    <div className="space-y-6">
      {/* Estado de Certificación */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Award className="h-5 w-5" />
              Estado de Certificación
            </CardTitle>
            {certification && getStatusBadge(certification.status)}
          </div>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            {certification?.certificateId && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">ID de Certificado:</span>
                <span className="text-sm font-mono">{certification.certificateId}</span>
              </div>
            )}
            {certification?.certifiedAt && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Certificado el:</span>
                <span className="text-sm">
                  {new Date(certification.certifiedAt).toLocaleString()}
                </span>
              </div>
            )}
            {certification?.certifiedBy && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Certificado por:</span>
                <span className="text-sm">{certification.certifiedBy}</span>
              </div>
            )}
            {certification?.expiryDate && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Válido hasta:</span>
                <span className="text-sm">
                  {new Date(certification.expiryDate).toLocaleString()}
                </span>
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Requisitos de Certificación */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <CheckCircle className="h-5 w-5" />
            Requisitos de Certificación
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-3">
            {certification?.requirements && (
              <>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Clasificación de Riesgo</span>
                  {certification.requirements.classification ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Evaluación FRIA</span>
                  {certification.requirements.fria ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Registro EU</span>
                  {certification.requirements.euRegistration ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Declaración de Conformidad</span>
                  {certification.requirements.conformityDeclaration ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Documentación Técnica</span>
                  {certification.requirements.technicalDocs ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">Sistema de Gestión de Calidad (QMS)</span>
                  {certification.requirements.qms ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-yellow-500" />
                  )}
                </div>
              </>
            )}
            {!allRequirementsMet && (
              <div className="mt-4 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
                <p className="text-sm text-yellow-800">
                  No todos los requisitos están cumplidos. Complete los requisitos pendientes antes de solicitar la certificación.
                </p>
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Información de Certificación */}
      {certification && (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2">
                <FileText className="h-5 w-5" />
                Información de Certificación
              </CardTitle>
              {!isEditing && (
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setIsEditing(true)}
                >
                  Editar
                </Button>
              )}
            </div>
          </CardHeader>
          <CardBody>
            {isEditing ? (
              <div className="space-y-4">
                <div>
                  <Label>Organismo Certificador</Label>
                  <Input
                    value={formData.certifyingBody}
                    onChange={(e) =>
                      setFormData({ ...formData, certifyingBody: e.target.value })
                    }
                    placeholder="Ej: Internal Compliance Team"
                  />
                </div>
                <div>
                  <Label>Fecha de Expiración</Label>
                  <Input
                    type="date"
                    value={formData.expiryDate}
                    onChange={(e) =>
                      setFormData({ ...formData, expiryDate: e.target.value })
                    }
                  />
                </div>
                <div>
                  <Label>Notas</Label>
                  <Textarea
                    value={formData.notes}
                    onChange={(e) =>
                      setFormData({ ...formData, notes: e.target.value })
                    }
                    rows={4}
                    placeholder="Notas adicionales sobre la certificación..."
                  />
                </div>
                <div className="flex gap-2">
                  <Button onClick={handleSave} disabled={saving}>
                    {saving ? "Guardando..." : "Guardar"}
                  </Button>
                  <Button
                    variant="outline"
                    onClick={() => {
                      setIsEditing(false);
                      loadCertificationData();
                    }}
                  >
                    Cancelar
                  </Button>
                </div>
              </div>
            ) : (
              <div className="space-y-4">
                {certification.certifyingBody && (
                  <div>
                    <span className="text-sm font-medium">Organismo Certificador:</span>
                    <p className="text-sm mt-1">{certification.certifyingBody}</p>
                  </div>
                )}
                {certification.notes && (
                  <div>
                    <span className="text-sm font-medium">Notas:</span>
                    <p className="text-sm mt-1">{certification.notes}</p>
                  </div>
                )}
              </div>
            )}
          </CardBody>
        </Card>
      )}

      {/* Acciones */}
      {certification?.status === "NOT_CERTIFIED" && allRequirementsMet && (
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="font-medium">Iniciar Proceso de Certificación</p>
                <p className="text-sm text-muted-foreground mt-1">
                  Todos los requisitos están cumplidos. Puede solicitar la certificación.
                </p>
              </div>
              <Button onClick={handleStartCertification} disabled={saving}>
                <Award className="h-4 w-4 mr-2" />
                Solicitar Certificación
              </Button>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}
