"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { MessageSquare, ArrowLeft, Clock, DollarSign, Hash, User, Bot } from "lucide-react";
import { useParams, useRouter } from "next/navigation";

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

export default function InteractionDetailPage() {
  const params = useParams();
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [interaction, setInteraction] = useState<AgentInteraction | null>(null);
  const [loading, setLoading] = useState(true);

  const interactionId = params?.id as string;

  useEffect(() => {
    if (interactionId) {
      loadData();
    }
  }, [interactionId]);

  const loadData = async () => {
    setLoading(true);
    try {
      // Intentar cargar desde API
      try {
        const response = await fetch(`/api/governance/agents/interactions/${interactionId}`);
        if (response.ok) {
          const data = await response.json();
          setInteraction(data);
          setLoading(false);
          return;
        }
      } catch (apiError) {
        console.warn("Error al cargar interacción desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      const mockInteractions: AgentInteraction[] = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          userId: "user123",
          sessionId: "session-abc-123",
          input: JSON.stringify({ creditAmount: 50000, income: 75000, creditScore: 750 }, null, 2),
          output: JSON.stringify({ approved: true, interestRate: 3.5, term: 60 }, null, 2),
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
          input: JSON.stringify({ transactionId: "txn-789", amount: 15000, merchant: "Online Store" }, null, 2),
          output: JSON.stringify({ fraudRisk: "LOW", confidence: 0.92, action: "APPROVE" }, null, 2),
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
          input: JSON.stringify({ creditAmount: 100000, income: 50000, creditScore: 650 }, null, 2),
          output: JSON.stringify({ approved: false, reason: "Credit score below threshold", recommendation: "Improve credit score" }, null, 2),
          durationMs: 980,
          tokensUsed: 380,
          cost: 0.0021,
          createdAt: "2024-01-15T14:30:00",
        },
      ];

      const found = mockInteractions.find((i) => i.id.toString() === interactionId);
      setInteraction(found || null);
    } catch (error) {
      console.error("Error loading interaction:", error);
    } finally {
      setLoading(false);
    }
  };

  const isJson = (str: string | undefined): boolean => {
    if (!str) return false;
    try {
      JSON.parse(str);
      return true;
    } catch {
      return false;
    }
  };

  const formatJson = (str: string | undefined): string => {
    if (!str) return "";
    if (isJson(str)) {
      try {
        return JSON.stringify(JSON.parse(str), null, 2);
      } catch {
        return str;
      }
    }
    return str;
  };

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

  if (!interaction) {
    return (
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <MessageSquare className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("agents.interactions.detail.title", "Detalle de Interacción")}
              </h1>
            </div>
          </div>
          <Button
            variant="outline"
            size="sm"
            className="flex items-center gap-2"
            onClick={() => router.push("/governance/agents/interactions/overview")}
          >
            <ArrowLeft className="h-4 w-4" />
            {t("common.back", "Volver")}
          </Button>
        </div>
        <Card>
          <CardBody>
            <p className="text-center text-muted-foreground py-8">
              {t("agents.interactions.detail.notFound", "Interacción no encontrada")}
            </p>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6 p-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
            <MessageSquare className="w-8 h-8 text-primary" />
            Detalle de Interacción
          </h1>
          <p className="text-muted-foreground mt-1">
            ID: {interaction.id} - {interaction.agentName || interaction.agentUuid || "Agente desconocido"}
          </p>
        </div>
        <Button
          variant="outline"
          onClick={() => router.push("/governance/agents/interactions/overview")}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Volver
        </Button>
      </div>

      {/* Información General */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Bot className="h-5 w-5" />
            Información General
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="text-sm font-medium text-muted-foreground">Agente</label>
              <p className="text-sm font-semibold mt-1">
                {interaction.agentName || interaction.agentUuid || "-"}
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <User className="h-4 w-4" />
                Usuario
              </label>
              <p className="text-sm font-mono mt-1">{interaction.userId}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Hash className="h-4 w-4" />
                Sesión
              </label>
              <p className="text-sm font-mono mt-1">{interaction.sessionId || "-"}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <Clock className="h-4 w-4" />
                Fecha y Hora
              </label>
              <p className="text-sm mt-1">
                {interaction.createdAt
                  ? new Date(interaction.createdAt).toLocaleString("es-ES", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                      hour: "2-digit",
                      minute: "2-digit",
                      second: "2-digit",
                    })
                  : "-"}
              </p>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Duración</p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {interaction.durationMs !== undefined && interaction.durationMs !== null
                    ? `${interaction.durationMs}ms`
                    : "-"}
                </p>
              </div>
              <Clock className="h-8 w-8 text-blue-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Tokens Utilizados</p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {interaction.tokensUsed !== undefined && interaction.tokensUsed !== null
                    ? interaction.tokensUsed.toLocaleString()
                    : "-"}
                </p>
              </div>
              <Hash className="h-8 w-8 text-purple-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Costo</p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {interaction.cost !== undefined && interaction.cost !== null
                    ? `$${interaction.cost.toFixed(4)}`
                    : "-"}
                </p>
              </div>
              <DollarSign className="h-8 w-8 text-green-500 opacity-50" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Input */}
      <Card>
        <CardHeader>
          <CardTitle>Input</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="bg-muted rounded-lg p-4">
            <pre className="text-sm font-mono whitespace-pre-wrap break-words">
              {formatJson(interaction.input) || "-"}
            </pre>
          </div>
        </CardBody>
      </Card>

      {/* Output */}
      <Card>
        <CardHeader>
          <CardTitle>Output</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="bg-muted rounded-lg p-4">
            <pre className="text-sm font-mono whitespace-pre-wrap break-words">
              {formatJson(interaction.output) || "-"}
            </pre>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
