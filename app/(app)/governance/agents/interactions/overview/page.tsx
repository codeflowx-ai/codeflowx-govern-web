"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { MessageSquare, Search, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface AgentInteraction {
  id: number;
  agentUuid?: string;
  agentName?: string;
  userId: string;
  sessionId?: string;
  input?: string;
  output?: string;
  durationMs?: number;
  tokensUsed?: number;
  cost?: number;
  createdAt: string;
}

export default function InteractionsOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [interactions, setInteractions] = useState<AgentInteraction[]>([]);
  const [totalItems, setTotalItems] = useState(0);
  const [totalCost, setTotalCost] = useState(0);
  const [totalTokens, setTotalTokens] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);

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

      // Cargar interacciones
      let interactionsList: AgentInteraction[] = [];
      try {
        const response = await fetch("/api/governance/agents/interactions");
        if (response.ok) {
          const data = await response.json();
          interactionsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar interacciones desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || interactionsList.length === 0) {
        interactionsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            userId: "user123",
            sessionId: "session-abc-123",
            input: JSON.stringify({ creditAmount: 50000, income: 75000, creditScore: 750 }),
            output: JSON.stringify({ approved: true, interestRate: 3.5, term: 60 }),
            durationMs: 1250,
            tokensUsed: 450,
            cost: 0.0025,
            createdAt: "2024-01-15T10:30:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            userId: "user456",
            sessionId: "session-def-456",
            input: JSON.stringify({ transactionId: "txn-789", amount: 15000, merchant: "Online Store" }),
            output: JSON.stringify({ fraudRisk: "LOW", confidence: 0.92, action: "APPROVE" }),
            durationMs: 890,
            tokensUsed: 320,
            cost: 0.0018,
            createdAt: "2024-01-15T11:15:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            userId: "user789",
            sessionId: "session-ghi-789",
            input: "¿Cuál es el estado de mi pedido #12345?",
            output: "Su pedido #12345 está en tránsito y llegará el 20 de enero. Puede rastrearlo con el código ABC123.",
            durationMs: 2100,
            tokensUsed: 680,
            cost: 0.0034,
            createdAt: "2024-01-15T12:00:00",
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            userId: "user123",
            sessionId: "session-abc-123",
            input: JSON.stringify({ creditAmount: 100000, income: 50000, creditScore: 650 }),
            output: JSON.stringify({ approved: false, reason: "Credit score below threshold", recommendation: "Improve credit score" }),
            durationMs: 980,
            tokensUsed: 380,
            cost: 0.0021,
            createdAt: "2024-01-15T14:30:00",
          },
        ];
      }

      // Hacer match de agentes con interacciones
      const interactionsWithAgents = interactionsList.map((interaction: AgentInteraction) => {
        if (interaction.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === interaction.agentUuid || a.id.toString() === interaction.agentUuid);
          if (agent) {
            return { ...interaction, agentName: agent.name };
          }
        }
        return interaction;
      });

      setInteractions(interactionsWithAgents);
      setTotalItems(interactionsWithAgents.length);
      setTotalCost(interactionsWithAgents.reduce((sum, i) => sum + (i.cost || 0), 0));
      setTotalTokens(interactionsWithAgents.reduce((sum, i) => sum + (i.tokensUsed || 0), 0));
    } catch (error) {
      console.error("Error loading interactions:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredInteractions = interactions.filter((interaction) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (interaction.agentName && interaction.agentName.toLowerCase().includes(searchLower)) ||
      (interaction.userId && interaction.userId.toLowerCase().includes(searchLower)) ||
      (interaction.sessionId && interaction.sessionId.toLowerCase().includes(searchLower)) ||
      (interaction.input && interaction.input.toLowerCase().includes(searchLower)) ||
      (interaction.output && interaction.output.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <MessageSquare className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <MessageSquare className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.interactions.title", "Interacciones Usuario-Agente")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.interactions.subtitle", "Registro de telemetría de interacciones entre usuarios y agentes")}
          </p>
        </div>
        {/* Las interacciones se registran automáticamente por telemetría - No se crean manualmente */}
      </div>

      {/* Estadísticas */}
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
                <p className="text-xs text-muted-foreground">
                  {t("agents.interactions.totalInteractions", "Interacciones")}
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
                  {t("agents.interactions.totalCost", "Costo Total")}
                </p>
                <h2 className="text-2xl font-bold text-blue-600 mb-1">
                  ${totalCost.toFixed(4)}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.interactions.cumulativeCost", "Costo acumulado")}
                </p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-purple-500 hover:shadow-lg hover:border-purple-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("agents.interactions.totalTokens", "Tokens Totales")}
                </p>
                <h2 className="text-2xl font-bold text-purple-600 mb-1">
                  {totalTokens.toLocaleString()}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.interactions.cumulativeTokens", "Tokens acumulados")}
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
                  {t("agents.interactions.avgDuration", "Duración Promedio")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {interactions.length > 0
                    ? `${(interactions.reduce((sum, i) => sum + (i.durationMs || 0), 0) / interactions.length).toFixed(0)}ms`
                    : "0ms"}
                </h2>
                <p className="text-xs text-muted-foreground">
                  {t("agents.interactions.avgResponseTime", "Tiempo de respuesta")}
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
                placeholder={t("agents.interactions.searchPlaceholder", "Buscar por agente, usuario, sesión, input o output...")}
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
          <CardTitle className="text-base font-medium">{t("agents.interactions.list", "Listado de Interacciones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredInteractions.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.interactions.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.interactions.userId", "Usuario")}</th>
                    <th className="text-left p-2">{t("agents.interactions.sessionId", "Sesión")}</th>
                    <th className="text-left p-2">{t("agents.interactions.duration", "Duración")}</th>
                    <th className="text-left p-2">{t("agents.interactions.tokens", "Tokens")}</th>
                    <th className="text-left p-2">{t("agents.interactions.cost", "Costo")}</th>
                    <th className="text-left p-2">{t("agents.interactions.createdAt", "Fecha")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredInteractions.map((interaction) => (
                    <tr
                      key={interaction.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{interaction.id}</td>
                      <td className="p-2">{interaction.agentName || interaction.agentUuid || "-"}</td>
                      <td className="p-2 font-mono text-xs">{interaction.userId}</td>
                      <td className="p-2 font-mono text-xs">{interaction.sessionId || "-"}</td>
                      <td className="p-2">
                        {interaction.durationMs !== undefined && interaction.durationMs !== null
                          ? `${interaction.durationMs}ms`
                          : "-"}
                      </td>
                      <td className="p-2">
                        {interaction.tokensUsed !== undefined && interaction.tokensUsed !== null
                          ? interaction.tokensUsed.toLocaleString()
                          : "-"}
                      </td>
                      <td className="p-2">
                        {interaction.cost !== undefined && interaction.cost !== null
                          ? `$${interaction.cost.toFixed(4)}`
                          : "-"}
                      </td>
                      <td className="p-2">
                        {interaction.createdAt
                          ? new Date(interaction.createdAt).toLocaleString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                              hour: "2-digit",
                              minute: "2-digit",
                            })
                          : "-"}
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => router.push(`/governance/agents/interactions/${interaction.id}`)}
                            title={t("agents.interactions.view", "Ver Interacción")}
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
