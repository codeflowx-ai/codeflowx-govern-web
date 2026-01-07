"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { CheckCircle2, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface AgentApproval {
  id: number;
  agentUuid?: string;
  agentName?: string;
  approvalType: string;
  approvalStatus: string;
  requestReason: string;
  requestDetails: string;
  approvalCriteria: string;
}

interface Agent {
  id: number;
  uuid: string;
  name: string;
  status: string;
}

export default function AgentApprovalOverviewPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [approvals, setApprovals] = useState<AgentApproval[]>([]);
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);
  const [selectedApproval, setSelectedApproval] = useState<AgentApproval | null>(null);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      // Cargar agentes primero
      const agentsResponse = await fetch("/api/governance/agents/registry");
      let agentsList: Agent[] = [];
      if (agentsResponse.ok) {
        const agentsData = await agentsResponse.json();
        agentsList = agentsData.items || agentsData || [];
      } else {
        // Mock de agentes
        agentsList = [
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ];
      }
      setAgents(agentsList);

      // Cargar aprobaciones
      let approvalsList: AgentApproval[] = [];
      try {
        const response = await fetch("/api/agents/approval/list");
        if (response.ok) {
          const data = await response.json();
          approvalsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar aprobaciones desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      // TODO: Remover cuando la API esté completamente implementada
      if (true || approvalsList.length === 0) {
        approvalsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            approvalType: "DEPLOYMENT",
            approvalStatus: "APPROVED",
            requestReason: "Despliegue del agente de scoring crediticio a producción para procesar solicitudes de crédito",
            requestDetails: "El agente ha completado todas las pruebas en ambiente de staging y cumple con los criterios de calidad establecidos. Se requiere aprobación para desplegar en producción.",
            approvalCriteria: "Cumplimiento de métricas de precisión >95%, pruebas de carga exitosas, documentación completa, revisión de seguridad aprobada"
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            approvalType: "VERSION_UPDATE",
            approvalStatus: "PENDING",
            requestReason: "Actualización a versión 2.1.0 del agente de detección de fraude con mejoras en algoritmos de ML",
            requestDetails: "La nueva versión incluye mejoras en la detección de patrones fraudulentos y reducción de falsos positivos. Requiere aprobación para actualizar en producción.",
            approvalCriteria: "Validación de mejoras en métricas de detección, pruebas de regresión completas, análisis de impacto en sistemas existentes"
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            approvalType: "CONFIGURATION_CHANGE",
            approvalStatus: "PENDING",
            requestReason: "Modificación de parámetros de configuración del agente de servicio al cliente para mejorar tiempos de respuesta",
            requestDetails: "Ajuste de umbrales de timeout y límites de concurrencia para optimizar el rendimiento del agente en horas pico.",
            approvalCriteria: "Análisis de impacto en rendimiento, pruebas de carga con nueva configuración, validación de SLA"
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            approvalType: "PRODUCTION_ACCESS",
            approvalStatus: "REJECTED",
            requestReason: "Solicitud de acceso a producción para el agente de scoring crediticio con permisos ampliados",
            requestDetails: "Se requiere acceso a datos sensibles de clientes y capacidad de modificar decisiones crediticias. La solicitud no cumple con los requisitos de seguridad establecidos.",
            approvalCriteria: "Certificación de seguridad, auditoría de acceso, validación de permisos mínimos necesarios, aprobación del comité de seguridad"
          },
        ];
      }

      // Hacer match de agentes con aprobaciones
      const approvalsWithAgents = approvalsList.map((approval: AgentApproval) => {
        if (approval.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === approval.agentUuid || a.id.toString() === approval.agentUuid);
          if (agent) {
            return { ...approval, agentName: agent.name };
          }
        }
        return approval;
      });

      setApprovals(approvalsWithAgents);
      setTotalItems(approvalsWithAgents.length);
      setActiveItems(approvalsWithAgents.filter((a: AgentApproval) => a.approvalStatus === "ACTIVE" || a.approvalStatus === "APPROVED").length);
      setPendingItems(approvalsWithAgents.filter((a: AgentApproval) => a.approvalStatus === "PENDING").length);
    } catch (error) {
      console.error("Error loading approvals:", error);
      // Mock completo
      const mockAgents = [
        { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
        { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
        { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
      ];
      setAgents(mockAgents);
      const mockApprovals = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          approvalType: "DEPLOYMENT",
          approvalStatus: "APPROVED",
          requestReason: "Despliegue del agente de scoring crediticio a producción para procesar solicitudes de crédito",
          requestDetails: "El agente ha completado todas las pruebas en ambiente de staging y cumple con los criterios de calidad establecidos. Se requiere aprobación para desplegar en producción.",
          approvalCriteria: "Cumplimiento de métricas de precisión >95%, pruebas de carga exitosas, documentación completa, revisión de seguridad aprobada"
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          approvalType: "VERSION_UPDATE",
          approvalStatus: "PENDING",
          requestReason: "Actualización a versión 2.1.0 del agente de detección de fraude con mejoras en algoritmos de ML",
          requestDetails: "La nueva versión incluye mejoras en la detección de patrones fraudulentos y reducción de falsos positivos. Requiere aprobación para actualizar en producción.",
          approvalCriteria: "Validación de mejoras en métricas de detección, pruebas de regresión completas, análisis de impacto en sistemas existentes"
        },
        {
          id: 3,
          agentUuid: "550e8400-e29b-41d4-a716-446655440002",
          agentName: "Customer Service Agent",
          approvalType: "CONFIGURATION_CHANGE",
          approvalStatus: "PENDING",
          requestReason: "Modificación de parámetros de configuración del agente de servicio al cliente para mejorar tiempos de respuesta",
          requestDetails: "Ajuste de umbrales de timeout y límites de concurrencia para optimizar el rendimiento del agente en horas pico.",
          approvalCriteria: "Análisis de impacto en rendimiento, pruebas de carga con nueva configuración, validación de SLA"
        },
        {
          id: 4,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          approvalType: "PRODUCTION_ACCESS",
          approvalStatus: "REJECTED",
          requestReason: "Solicitud de acceso a producción para el agente de scoring crediticio con permisos ampliados",
          requestDetails: "Se requiere acceso a datos sensibles de clientes y capacidad de modificar decisiones crediticias. La solicitud no cumple con los requisitos de seguridad establecidos.",
          approvalCriteria: "Certificación de seguridad, auditoría de acceso, validación de permisos mínimos necesarios, aprobación del comité de seguridad"
        },
      ];
      setApprovals(mockApprovals);
      setTotalItems(4);
      setActiveItems(1);
      setPendingItems(2);
    } finally {
      setLoading(false);
    }
  };

  const filteredApprovals = approvals.filter((approval) =>
    approval.requestReason.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (approval.agentName && approval.agentName.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <CheckCircle2 className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.approval.title", "Aprobación de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.approval.subtitle", "Gestión de aprobaciones de agentes")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            window.location.href = "/governance/agents/approval/create";
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.approval.register", "Registrar Aprobación")}
        </Button>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-3 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardBody className="p-4">
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t("common.search", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
              />
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.approval.list", "Listado de Aprobaciones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredApprovals.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.approval.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.approval.approvalType", "Tipo de Aprobación")}</th>
                    <th className="text-left p-2">{t("agents.approval.approvalStatus", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.approval.requestReason", "Razón de Solicitud")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredApprovals.map((approval) => (
                    <tr
                      key={approval.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{approval.id}</td>
                      <td className="p-2">
                        <div className="flex flex-col">
                          <span className="font-medium text-sm">{approval.agentName || ""}</span>
                          {approval.agentUuid && (
                            <span className="text-xs text-muted-foreground">{approval.agentUuid}</span>
                          )}
                        </div>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {approval.approvalType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            approval.approvalStatus === "ACTIVE" || approval.approvalStatus === "APPROVED"
                              ? "bg-green-500/20 text-green-500"
                              : approval.approvalStatus === "PENDING"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {approval.approvalStatus}
                        </span>
                      </td>
                      <td className="p-2">{approval.requestReason}</td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => setSelectedApproval(approval)}
                            title={t("common.view", "Ver")}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0 text-destructive"
                            title={t("common.delete", "Eliminar")}
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardBody>
      </Card>

      {/* Modal de Detalles */}
      <SimpleModal
        isOpen={selectedApproval !== null}
        onClose={() => setSelectedApproval(null)}
        title={selectedApproval ? `${t("agents.approval.detail.title", "Detalles de Aprobación")} #${selectedApproval.id}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedApproval && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.agent", "Agente")}
                    </label>
                    <div className="flex flex-col">
                      <p className="text-base font-semibold">{selectedApproval.agentName || ""}</p>
                      {selectedApproval.agentUuid && (
                        <p className="text-xs text-muted-foreground">{selectedApproval.agentUuid}</p>
                      )}
                    </div>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.approvalType", "Tipo de Aprobación")}
                    </label>
                    <p className="text-base font-semibold">{selectedApproval.approvalType}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.approvalStatus", "Estado")}
                    </label>
                    <p className="text-base">
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          selectedApproval.approvalStatus === "ACTIVE" || selectedApproval.approvalStatus === "APPROVED"
                            ? "bg-green-500/20 text-green-500"
                            : selectedApproval.approvalStatus === "PENDING"
                            ? "bg-yellow-500/20 text-yellow-500"
                            : "bg-red-500/20 text-red-500"
                        }`}
                      >
                        {selectedApproval.approvalStatus}
                      </span>
                    </p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.requestReason", "Razón de Solicitud")}
                    </label>
                    <p className="text-base">{selectedApproval.requestReason}</p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.requestDetails", "Detalles de Solicitud")}
                    </label>
                    <p className="text-base">{selectedApproval.requestDetails}</p>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.approval.approvalCriteria", "Criterios de Aprobación")}
                    </label>
                    <p className="text-base">{selectedApproval.approvalCriteria}</p>
                  </div>
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
