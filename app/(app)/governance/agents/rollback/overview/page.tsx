"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { RotateCcw, Search, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface Rollback {
  id: number;
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
  createdAt?: string;
}

export default function RollbackOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [rollbacks, setRollbacks] = useState<Rollback[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [pendingApprovalItems, setPendingApprovalItems] = useState(0);
  const [approvedItems, setApprovedItems] = useState(0);
  const [completedItems, setCompletedItems] = useState(0);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      // Cargar agentes primero
      const agentsResponse = await fetch("/api/governance/agents/registry");
      let agentsList: any[] = [];
      if (agentsResponse.ok) {
        const agentsData = await agentsResponse.json();
        agentsList = agentsData.items || agentsData || [];
      } else {
        agentsList = [
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ];
      }

      // Cargar reversiones
      let rollbacksList: Rollback[] = [];
      try {
        const response = await fetch("/api/governance/agents/rollback");
        if (response.ok) {
          const data = await response.json();
          rollbacksList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar reversiones desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || rollbacksList.length === 0) {
        rollbacksList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            rollbackType: "VERSION",
            rollbackStatus: "PENDING_APPROVAL",
            fromVersion: "v2.1.0",
            toVersion: "v2.0.5",
            rollbackReason: "Degradación de métricas de precisión detectada automáticamente. La precisión cayó del 95% al 87% en las últimas 24 horas.",
            triggerEvent: "METRIC_DEGRADATION",
            triggerThreshold: 90,
            triggerValue: 87,
            createdAt: "2024-01-15T10:30:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            rollbackType: "CONFIG",
            rollbackStatus: "APPROVED",
            fromVersion: "v1.5.2",
            toVersion: "v1.5.1",
            rollbackReason: "Aumento de falsos positivos después de cambio de configuración. Se requiere revertir a configuración anterior.",
            triggerEvent: "FALSE_POSITIVE_INCREASE",
            triggerThreshold: 5,
            triggerValue: 8,
            rollbackApprovedBy: "admin@codeflowx.com",
            rollbackApprovedAt: "2024-01-15T11:00:00",
            rollbackStartedAt: "2024-01-15T11:05:00",
            rollbackCompletedAt: "2024-01-15T11:10:00",
            createdAt: "2024-01-15T10:45:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            rollbackType: "MODEL",
            rollbackStatus: "PENDING_APPROVAL",
            fromVersion: "v3.2.0",
            toVersion: "v3.1.5",
            rollbackReason: "Baja confianza en decisiones detectada. El modelo actual muestra confianza promedio del 65%, por debajo del umbral del 70%.",
            triggerEvent: "LOW_CONFIDENCE",
            triggerThreshold: 70,
            triggerValue: 65,
            createdAt: "2024-01-15T14:20:00",
          },
        ];
      }

      // Hacer match de agentes con reversiones
      const rollbacksWithAgents = rollbacksList.map((rollback: Rollback) => {
        if (rollback.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === rollback.agentUuid || a.id.toString() === rollback.agentUuid);
          if (agent) {
            return { ...rollback, agentName: agent.name };
          }
        }
        return rollback;
      });

      setRollbacks(rollbacksWithAgents);
      setTotalItems(rollbacksWithAgents.length);
      setPendingApprovalItems(rollbacksWithAgents.filter((r: Rollback) => r.rollbackStatus === "PENDING_APPROVAL").length);
      setApprovedItems(rollbacksWithAgents.filter((r: Rollback) => r.rollbackStatus === "APPROVED" || r.rollbackStatus === "IN_PROGRESS").length);
      setCompletedItems(rollbacksWithAgents.filter((r: Rollback) => r.rollbackStatus === "COMPLETED").length);
    } catch (error) {
      console.error("Error loading rollbacks:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredRollbacks = rollbacks.filter((item) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (item.rollbackType && item.rollbackType.toLowerCase().includes(searchLower)) ||
      (item.agentName && item.agentName.toLowerCase().includes(searchLower)) ||
      (item.fromVersion && item.fromVersion.toLowerCase().includes(searchLower)) ||
      (item.toVersion && item.toVersion.toLowerCase().includes(searchLower)) ||
      (item.rollbackReason && item.rollbackReason.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <RotateCcw className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <RotateCcw className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.rollback.title", "Reversión")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.rollback.subtitle", "Gestión de reversiones")}
          </p>
        </div>
        {/* Las reversiones se generan automáticamente - No se crean manualmente */}
      </div>

      {/* Estadísticas - Enfoque HITL */}
      <div className="grid grid-cols-4 gap-4">
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
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.rollback.pendingApproval", "Pendientes Aprobación")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingApprovalItems}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.rollback.hitlPending", "Pendientes HITL")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-blue-500 hover:shadow-lg hover:border-blue-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.rollback.approved", "Aprobadas")}
                </p>
                <h2 className="text-2xl font-bold text-blue-600 mb-1">
                  {approvedItems}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.rollback.inProgress", "En Progreso")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.rollback.completed", "Completadas")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {completedItems}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.rollback.successfullyRolledBack", "Revertidas Exitosamente")}
                </p>
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
          <CardTitle className="text-base font-medium">{t("agents.rollback.list", "Listado de Reversiones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredRollbacks.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.rollback.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.rollback.rollbackType", "Tipo")}</th>
                    <th className="text-left p-2">{t("agents.rollback.fromVersion", "Desde Versión")}</th>
                    <th className="text-left p-2">{t("agents.rollback.toVersion", "Hacia Versión")}</th>
                    <th className="text-left p-2">{t("agents.rollback.triggerEvent", "Evento Disparador")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.rollback.createdAt", "Fecha")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredRollbacks.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.agentName || item.agentUuid || "-"}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {item.rollbackType}
                        </span>
                      </td>
                      <td className="p-2">{item.fromVersion}</td>
                      <td className="p-2">{item.toVersion}</td>
                      <td className="p-2">
                        {item.triggerEvent ? (
                          <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                            {item.triggerEvent}
                            {item.triggerValue !== undefined && item.triggerThreshold !== undefined && (
                              <span className="ml-1">
                                ({item.triggerValue}/{item.triggerThreshold})
                              </span>
                            )}
                          </span>
                        ) : (
                          "-"
                        )}
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            item.rollbackStatus === "COMPLETED"
                              ? "bg-green-500/20 text-green-500"
                              : item.rollbackStatus === "PENDING_APPROVAL"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : item.rollbackStatus === "APPROVED" || item.rollbackStatus === "IN_PROGRESS"
                              ? "bg-blue-500/20 text-blue-500"
                              : "bg-gray-500/20 text-gray-500"
                          }`}
                        >
                          {item.rollbackStatus}
                        </span>
                      </td>
                      <td className="p-2">
                        {item.createdAt
                          ? new Date(item.createdAt).toLocaleDateString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                            })
                          : "-"}
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => router.push(`/governance/agents/rollback/${item.id}`)}
                            title={t("agents.rollback.review", "Revisar Reversión")}
                          >
                            <Eye className="w-4 h-4" />
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
    </div>
  );
}
