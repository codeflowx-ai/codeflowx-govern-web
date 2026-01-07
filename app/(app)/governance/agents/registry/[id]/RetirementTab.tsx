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
  Archive,
  AlertTriangle,
  Calendar,
  FileText,
  CheckCircle,
  Clock,
  XCircle,
} from "lucide-react";
import { useEffect, useState } from "react";

interface RetirementStatus {
  status: string;
  retirementDate?: string;
  retirementReason?: string;
  retirementType?: string;
  approvedBy?: string;
  approvedAt?: string;
  migrationPlan?: string;
  dependencies: {
    projects: number;
    deployments: number;
    integrations: number;
  };
}

interface RetirementTabProps {
  agentId: string | number;
  agentUuid?: string;
  agentName?: string;
  agentStatus?: string;
}

export default function RetirementTab({
  agentId,
  agentUuid,
  agentName,
  agentStatus,
}: RetirementTabProps) {
  const { t, mounted } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [retirement, setRetirement] = useState<RetirementStatus | null>(null);
  const [showRetirementForm, setShowRetirementForm] = useState(false);
  const [formData, setFormData] = useState({
    retirementType: "",
    retirementDate: "",
    retirementReason: "",
    migrationPlan: "",
  });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    loadRetirementData();
  }, [agentId, agentUuid]);

  const loadRetirementData = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamada API real
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Mock data - agente no retirado
      setRetirement({
        status: "ACTIVE",
        dependencies: {
          projects: 3,
          deployments: 2,
          integrations: 5,
        },
      });
    } catch (error) {
      console.error("Error loading retirement data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitRetirement = async () => {
    if (!formData.retirementType || !formData.retirementDate || !formData.retirementReason) {
      alert("Por favor, complete todos los campos obligatorios");
      return;
    }

    try {
      setSaving(true);
      // TODO: Lanzar proceso BPMN de retiro
      await new Promise((resolve) => setTimeout(resolve, 500));

      setRetirement({
        status: "PENDING_APPROVAL",
        retirementDate: formData.retirementDate,
        retirementReason: formData.retirementReason,
        retirementType: formData.retirementType,
        migrationPlan: formData.migrationPlan,
        dependencies: retirement?.dependencies || {
          projects: 0,
          deployments: 0,
          integrations: 0,
        },
      });

      setShowRetirementForm(false);
      setFormData({
        retirementType: "",
        retirementDate: "",
        retirementReason: "",
        migrationPlan: "",
      });
      alert("Solicitud de retiro enviada. Requiere aprobación.");
    } catch (error) {
      console.error("Error submitting retirement:", error);
      alert("Error al enviar la solicitud de retiro");
    } finally {
      setSaving(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: "bg-green-500/20 text-green-500",
      PENDING_APPROVAL: "bg-yellow-500/20 text-yellow-400",
      APPROVED: "bg-blue-500/20 text-blue-500",
      RETIRED: "bg-gray-500/20 text-gray-400",
      CANCELLED: "bg-red-500/20 text-red-400",
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
          <Archive className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  const hasDependencies =
    retirement &&
    (retirement.dependencies.projects > 0 ||
      retirement.dependencies.deployments > 0 ||
      retirement.dependencies.integrations > 0);

  return (
    <div className="space-y-6">
      {/* Estado de Retiro */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Archive className="h-5 w-5" />
              Estado de Retiro
            </CardTitle>
            {retirement && getStatusBadge(retirement.status)}
          </div>
        </CardHeader>
        <CardBody>
          {retirement?.status === "ACTIVE" ? (
            <div className="space-y-4">
              <p className="text-sm text-muted-foreground">
                Este agente está activo y no tiene solicitudes de retiro pendientes.
              </p>
              {hasDependencies && (
                <div className="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                  <div className="flex items-start gap-2">
                    <AlertTriangle className="h-5 w-5 text-yellow-600 mt-0.5" />
                    <div className="flex-1">
                      <p className="font-medium text-yellow-900 mb-2">
                        Dependencias Activas
                      </p>
                      <div className="space-y-1 text-sm text-yellow-800">
                        <p>• {retirement.dependencies.projects} proyecto(s) asociado(s)</p>
                        <p>• {retirement.dependencies.deployments} despliegue(s) activo(s)</p>
                        <p>• {retirement.dependencies.integrations} integración(es)</p>
                      </div>
                      <p className="text-sm text-yellow-700 mt-2">
                        Revise y migre estas dependencias antes de retirar el agente.
                      </p>
                    </div>
                  </div>
                </div>
              )}
              <Button
                onClick={() => setShowRetirementForm(true)}
                variant="outline"
                className="w-full"
              >
                <Archive className="h-4 w-4 mr-2" />
                Iniciar Proceso de Retiro
              </Button>
            </div>
          ) : retirement?.status === "PENDING_APPROVAL" ? (
            <div className="space-y-4">
              <div className="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                <div className="flex items-start gap-2">
                  <Clock className="h-5 w-5 text-yellow-600 mt-0.5" />
                  <div className="flex-1">
                    <p className="font-medium text-yellow-900 mb-2">
                      Solicitud de Retiro Pendiente
                    </p>
                    <div className="space-y-2 text-sm text-yellow-800">
                      {retirement.retirementType && (
                        <p>
                          <span className="font-medium">Tipo:</span> {retirement.retirementType}
                        </p>
                      )}
                      {retirement.retirementDate && (
                        <p>
                          <span className="font-medium">Fecha propuesta:</span>{" "}
                          {new Date(retirement.retirementDate).toLocaleDateString()}
                        </p>
                      )}
                      {retirement.retirementReason && (
                        <p>
                          <span className="font-medium">Razón:</span> {retirement.retirementReason}
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ) : retirement?.status === "RETIRED" ? (
            <div className="space-y-4">
              <div className="p-4 bg-gray-50 border border-gray-200 rounded-lg">
                <div className="flex items-start gap-2">
                  <CheckCircle className="h-5 w-5 text-gray-600 mt-0.5" />
                  <div className="flex-1">
                    <p className="font-medium text-gray-900 mb-2">Agente Retirado</p>
                    <div className="space-y-2 text-sm text-gray-700">
                      {retirement.retirementDate && (
                        <p>
                          <span className="font-medium">Fecha de retiro:</span>{" "}
                          {new Date(retirement.retirementDate).toLocaleDateString()}
                        </p>
                      )}
                      {retirement.approvedBy && (
                        <p>
                          <span className="font-medium">Aprobado por:</span> {retirement.approvedBy}
                        </p>
                      )}
                      {retirement.approvedAt && (
                        <p>
                          <span className="font-medium">Aprobado el:</span>{" "}
                          {new Date(retirement.approvedAt).toLocaleString()}
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ) : null}
        </CardBody>
      </Card>

      {/* Formulario de Retiro */}
      {showRetirementForm && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileText className="h-5 w-5" />
              Solicitud de Retiro
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div>
                <Label>
                  Tipo de Retiro <span className="text-red-500">*</span>
                </Label>
                <Select
                  value={formData.retirementType}
                  onValueChange={(value) =>
                    setFormData({ ...formData, retirementType: value })
                  }
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Seleccione el tipo de retiro" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="PLANNED">Retiro Planificado</SelectItem>
                    <SelectItem value="IMMEDIATE">Retiro Inmediato</SelectItem>
                    <SelectItem value="GRACEFUL">Retiro Gradual</SelectItem>
                    <SelectItem value="REPLACEMENT">Reemplazo por Nuevo Agente</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div>
                <Label>
                  Fecha de Retiro Propuesta <span className="text-red-500">*</span>
                </Label>
                <Input
                  type="date"
                  value={formData.retirementDate}
                  onChange={(e) =>
                    setFormData({ ...formData, retirementDate: e.target.value })
                  }
                  min={new Date().toISOString().split("T")[0]}
                />
              </div>

              <div>
                <Label>
                  Razón del Retiro <span className="text-red-500">*</span>
                </Label>
                <Textarea
                  value={formData.retirementReason}
                  onChange={(e) =>
                    setFormData({ ...formData, retirementReason: e.target.value })
                  }
                  rows={4}
                  placeholder="Describa la razón del retiro del agente..."
                />
              </div>

              <div>
                <Label>Plan de Migración</Label>
                <Textarea
                  value={formData.migrationPlan}
                  onChange={(e) =>
                    setFormData({ ...formData, migrationPlan: e.target.value })
                  }
                  rows={4}
                  placeholder="Describa el plan de migración de dependencias (proyectos, despliegues, integraciones)..."
                />
              </div>

              {hasDependencies && (
                <div className="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
                  <div className="flex items-start gap-2">
                    <AlertTriangle className="h-5 w-5 text-yellow-600 mt-0.5" />
                    <div className="flex-1">
                      <p className="font-medium text-yellow-900 mb-1">
                        Atención: Dependencias Activas
                      </p>
                      <p className="text-sm text-yellow-800">
                        Este agente tiene dependencias activas. Asegúrese de incluir un plan de migración detallado.
                      </p>
                    </div>
                  </div>
                </div>
              )}

              <div className="flex gap-2">
                <Button onClick={handleSubmitRetirement} disabled={saving}>
                  {saving ? "Enviando..." : "Enviar Solicitud"}
                </Button>
                <Button
                  variant="outline"
                  onClick={() => {
                    setShowRetirementForm(false);
                    setFormData({
                      retirementType: "",
                      retirementDate: "",
                      retirementReason: "",
                      migrationPlan: "",
                    });
                  }}
                >
                  Cancelar
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Criterios de Retiro */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <AlertTriangle className="h-5 w-5" />
            Criterios de Retiro
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-2 text-sm">
            <p className="font-medium">Un agente puede ser retirado cuando:</p>
            <ul className="list-disc list-inside space-y-1 text-muted-foreground ml-4">
              <li>Ha sido reemplazado por una versión mejorada o un nuevo agente</li>
              <li>Ya no cumple con los requisitos de cumplimiento normativo</li>
              <li>Presenta problemas de seguridad o rendimiento críticos</li>
              <li>Ha alcanzado el final de su ciclo de vida planificado</li>
              <li>Los costos de mantenimiento superan los beneficios</li>
            </ul>
            <p className="font-medium mt-4">Antes de retirar un agente:</p>
            <ul className="list-disc list-inside space-y-1 text-muted-foreground ml-4">
              <li>Migre todas las dependencias (proyectos, despliegues, integraciones)</li>
              <li>Notifique a todos los stakeholders</li>
              <li>Documente el proceso de retiro</li>
              <li>Obtenga la aprobación requerida</li>
            </ul>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
