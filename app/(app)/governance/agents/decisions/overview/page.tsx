"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { GanttChartSquare, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface Decision {
  id: number;
  agentUuid?: string;
  agentName?: string;
  decisionType: string;
  decisionReason?: string;
  confidenceScore?: number;
  status: string;
  reviewedBy?: string;
  reviewedAt?: string;
  createdAt?: string;
}

export default function DecisionsOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [decisions, setDecisions] = useState<Decision[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

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

      // Cargar decisiones
      let decisionsList: Decision[] = [];
      try {
        const response = await fetch("/api/governance/agents/decisions");
        if (response.ok) {
          const data = await response.json();
          decisionsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar decisiones desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || decisionsList.length === 0) {
        decisionsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            decisionType: "APPROVE",
            decisionReason: "La solicitud de crédito cumple con todos los criterios establecidos",
            confidenceScore: 95,
            status: "EXECUTED",
            reviewedBy: "auditor@codeflowx.com",
            reviewedAt: "2024-01-15T10:30:00",
            createdAt: "2024-01-15T10:00:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            decisionType: "REJECT",
            decisionReason: "Se detectaron patrones sospechosos en la transacción",
            confidenceScore: 78,
            status: "PENDING",
            reviewedBy: "",
            reviewedAt: "",
            createdAt: "2024-01-15T11:00:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            decisionType: "REVIEW",
            decisionReason: "Requiere revisión manual por complejidad del caso",
            confidenceScore: 65,
            status: "REVIEWED",
            reviewedBy: "compliance@codeflowx.com",
            reviewedAt: "2024-01-16T09:15:00",
            createdAt: "2024-01-15T14:20:00",
          },
        ];
      }

      // Hacer match de agentes con decisiones
      const decisionsWithAgents = decisionsList.map((decision: Decision) => {
        if (decision.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === decision.agentUuid || a.id.toString() === decision.agentUuid);
          if (agent) {
            return { ...decision, agentName: agent.name };
          }
        }
        return decision;
      });

      setDecisions(decisionsWithAgents);
      setTotalItems(decisionsWithAgents.length);
      setActiveItems(decisionsWithAgents.filter((d: Decision) => d.status === "EXECUTED" || d.status === "REVIEWED").length);
      setPendingItems(decisionsWithAgents.filter((d: Decision) => d.status === "PENDING").length);
    } catch (error) {
      console.error("Error loading decisions:", error);
      // Mock completo en caso de error
      const mockDecisions = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          decisionType: "APPROVE",
          confidenceScore: 95,
          status: "EXECUTED",
          createdAt: "2024-01-15T10:00:00",
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          decisionType: "REJECT",
          confidenceScore: 78,
          status: "PENDING",
          createdAt: "2024-01-15T11:00:00",
        },
      ];
      setDecisions(mockDecisions);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredDecisions = decisions.filter((decision) => {
    const searchLower = searchTerm.toLowerCase();

    // Filtros especiales HITL
    if (searchTerm === "REQUIRES_REVIEW") {
      return decision.status === "PENDING" || !decision.reviewedBy;
    }
    if (searchTerm === "LOW_CONFIDENCE") {
      return decision.confidenceScore !== undefined && decision.confidenceScore < 70;
    }
    if (searchTerm === "HIGH_RISK") {
      return (decision.confidenceScore !== undefined && decision.confidenceScore < 60) ||
             (decision.decisionType === "REJECT" && !decision.reviewedBy);
    }

    // Búsqueda normal
    return (
      (decision.decisionType && decision.decisionType.toLowerCase().includes(searchLower)) ||
      (decision.agentName && decision.agentName.toLowerCase().includes(searchLower)) ||
      (decision.decisionReason && decision.decisionReason.toLowerCase().includes(searchLower)) ||
      (decision.reviewedBy && decision.reviewedBy.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <GanttChartSquare className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <GanttChartSquare className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.decisions.title", "Decisiones")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.decisions.subtitle", "Gestión de decisiones")}
          </p>
        </div>
        {/* Las decisiones se registran automáticamente por telemetría - No se crean manualmente */}
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
                  {t("agents.decisions.requiresReview", "Requieren Revisión")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.decisions.hitlPending", "Pendientes HITL")}
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
                  {t("agents.decisions.reviewed", "Revisadas")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.decisions.humanValidated", "Validadas por Humanos")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-red-500 hover:shadow-lg hover:border-red-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.decisions.lowConfidence", "Baja Confianza")}
                </p>
                <h2 className="text-2xl font-bold text-red-600 mb-1">
                  {decisions.filter((d) => d.confidenceScore !== undefined && d.confidenceScore < 70).length}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.decisions.requiresAttention", "Requieren Atención")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filtros - Enfoque HITL y Cumplimiento */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardBody className="p-4">
          <div className="flex gap-2 flex-wrap">
            <div className="flex-1 relative min-w-[200px]">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t("common.search", "Buscar...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
              />
            </div>
            <Button
              variant={searchTerm.includes("REQUIRES_REVIEW") ? "primary" : "outline"}
              size="sm"
              onClick={() => setSearchTerm("REQUIRES_REVIEW")}
            >
              {t("agents.decisions.filterRequiresReview", "Requieren Revisión")}
            </Button>
            <Button
              variant={searchTerm.includes("LOW_CONFIDENCE") ? "primary" : "outline"}
              size="sm"
              onClick={() => setSearchTerm("LOW_CONFIDENCE")}
            >
              {t("agents.decisions.filterLowConfidence", "Baja Confianza")}
            </Button>
            <Button
              variant={searchTerm.includes("HIGH_RISK") ? "primary" : "outline"}
              size="sm"
              onClick={() => setSearchTerm("HIGH_RISK")}
            >
              {t("agents.decisions.filterHighRisk", "Alto Riesgo")}
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setSearchTerm("")}
            >
              {t("common.clear", "Limpiar")}
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Tabla */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader className="pb-3">
          <CardTitle className="text-base font-medium">{t("agents.decisions.list", "Listado de Decisiones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredDecisions.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.decisions.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.decisions.decisionType", "Tipo de Decisión")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.decisions.confidenceScore", "Confianza")}</th>
                    <th className="text-left p-2">{t("agents.decisions.reviewedBy", "Revisado por")}</th>
                    <th className="text-left p-2">{t("agents.decisions.createdAt", "Fecha de Creación")}</th>
                    <th className="text-left p-2">{t("agents.decisions.hitlStatus", "Estado HITL")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDecisions.map((decision) => (
                    <tr
                      key={decision.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{decision.id}</td>
                      <td className="p-2">{decision.agentName || decision.agentUuid || "-"}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {decision.decisionType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            decision.status === "EXECUTED" || decision.status === "REVIEWED"
                              ? "bg-green-500/20 text-green-500"
                              : decision.status === "PENDING"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-gray-500/20 text-gray-500"
                          }`}
                        >
                          {decision.status}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-bold">
                          {decision.confidenceScore !== undefined && decision.confidenceScore !== null
                            ? `${decision.confidenceScore}%`
                            : "-"}
                        </span>
                      </td>
                      <td className="p-2">{decision.reviewedBy || "-"}</td>
                      <td className="p-2">
                        {decision.createdAt
                          ? new Date(decision.createdAt).toLocaleDateString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                            })
                          : "-"}
                      </td>
                      <td className="p-2">
                        {!decision.reviewedBy ? (
                          <span className="px-2 py-1 rounded text-xs bg-yellow-500/20 text-yellow-500 font-semibold">
                            {t("agents.decisions.pendingReview", "Pendiente Revisión")}
                          </span>
                        ) : (
                          <span className="px-2 py-1 rounded text-xs bg-green-500/20 text-green-500">
                            {t("agents.decisions.reviewed", "Revisada")}
                          </span>
                        )}
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => router.push(`/governance/agents/decisions/${decision.id}`)}
                            title={t("agents.decisions.review", "Revisar Decisión")}
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
