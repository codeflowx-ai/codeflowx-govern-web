"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Bot,
  Shield,
  AlertTriangle,
  CheckCircle,
  Clock,
  TrendingUp,
  Activity,
  FileCheck,
  BarChart3,
  Users,
  Eye,
  ArrowRight,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface GovernanceMetrics {
  totalAgents: number;
  activeAgents: number;
  pendingApprovals: number;
  criticalAlerts: number;
  complianceScore: number;
  certifiedAgents: number;
  agentsRequiringReview: number;
  totalInteractions: number;
  avgResponseTime: number;
}

interface AgentHealth {
  id: number;
  uuid: string;
  name: string;
  status: string;
  complianceStatus: string;
  certificationStatus: string;
  lastAudit: string;
  alerts: number;
  interactions24h: number;
  healthScore: number;
}

interface RecentActivity {
  id: number;
  type: string;
  agentName: string;
  description: string;
  timestamp: string;
  status: string;
}

export default function AgentsGovernanceDashboard() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [metrics, setMetrics] = useState<GovernanceMetrics | null>(null);
  const [agentHealth, setAgentHealth] = useState<AgentHealth[]>([]);
  const [recentActivity, setRecentActivity] = useState<RecentActivity[]>([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamadas API reales
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Mock data
      setMetrics({
        totalAgents: 24,
        activeAgents: 18,
        pendingApprovals: 5,
        criticalAlerts: 3,
        complianceScore: 92,
        certifiedAgents: 15,
        agentsRequiringReview: 7,
        totalInteractions: 125000,
        avgResponseTime: 320,
      });

      setAgentHealth([
        {
          id: 1,
          uuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          name: "Credit Scoring Agent",
          status: "ACTIVE",
          complianceStatus: "COMPLIANT",
          certificationStatus: "CERTIFIED",
          lastAudit: "2024-01-15",
          alerts: 0,
          interactions24h: 1250,
          healthScore: 98,
        },
        {
          id: 2,
          uuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
          name: "Document OCR Agent",
          status: "ACTIVE",
          complianceStatus: "PENDING",
          certificationStatus: "PENDING",
          lastAudit: "2024-01-10",
          alerts: 2,
          interactions24h: 890,
          healthScore: 85,
        },
        {
          id: 3,
          uuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
          name: "Fraud Detection Agent",
          status: "PENDING",
          complianceStatus: "NON_COMPLIANT",
          certificationStatus: "NOT_CERTIFIED",
          lastAudit: "2024-01-08",
          alerts: 5,
          interactions24h: 0,
          healthScore: 45,
        },
      ]);

      setRecentActivity([
        {
          id: 1,
          type: "APPROVAL",
          agentName: "Credit Scoring Agent",
          description: "Aprobación de despliegue a producción",
          timestamp: "2024-01-20T10:30:00",
          status: "PENDING",
        },
        {
          id: 2,
          type: "ALERT",
          agentName: "Fraud Detection Agent",
          description: "Alerta crítica: Tasa de error > 5%",
          timestamp: "2024-01-20T09:15:00",
          status: "OPEN",
        },
        {
          id: 3,
          type: "CERTIFICATION",
          agentName: "Document OCR Agent",
          description: "Certificación completada",
          timestamp: "2024-01-20T08:00:00",
          status: "COMPLETED",
        },
        {
          id: 4,
          type: "COMPLIANCE",
          agentName: "Invoice Processing Agent",
          description: "Evaluación de cumplimiento AI Act",
          timestamp: "2024-01-19T16:45:00",
          status: "IN_PROGRESS",
        },
      ]);
    } catch (error) {
      console.error("Error loading dashboard data:", error);
    } finally {
      setLoading(false);
    }
  };

  const getHealthScoreColor = (score: number) => {
    if (score >= 90) return "text-green-600";
    if (score >= 70) return "text-yellow-600";
    return "text-red-600";
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: "bg-green-500/20 text-green-500",
      PENDING: "bg-yellow-500/20 text-yellow-400",
      INACTIVE: "bg-red-500/20 text-red-400",
      COMPLIANT: "bg-green-500/20 text-green-500",
      NON_COMPLIANT: "bg-red-500/20 text-red-400",
      CERTIFIED: "bg-blue-500/20 text-blue-500",
      NOT_CERTIFIED: "bg-gray-500/20 text-gray-400",
    };
    return (
      <Badge className={colors[status] || "bg-gray-500/20 text-gray-400"}>
        {status}
      </Badge>
    );
  };

  const getActivityIcon = (type: string) => {
    switch (type) {
      case "APPROVAL":
        return <FileCheck className="h-4 w-4" />;
      case "ALERT":
        return <AlertTriangle className="h-4 w-4 text-red-500" />;
      case "CERTIFICATION":
        return <Shield className="h-4 w-4 text-blue-500" />;
      case "COMPLIANCE":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      default:
        return <Activity className="h-4 w-4" />;
    }
  };

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Bot className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6 p-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
            <Shield className="w-8 h-8 text-primary" />
            Dashboard de Gobierno de Agentes
          </h1>
          <p className="text-muted-foreground mt-1">
            Vista consolidada de salud, cumplimiento y gobernanza de agentes
          </p>
        </div>
        <Button
          variant="outline"
          onClick={() => router.push("/governance/agents/registry")}
        >
          <Eye className="h-4 w-4 mr-2" />
          Ver Todos los Agentes
        </Button>
      </div>

      {/* KPIs Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Total Agentes
                </p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {metrics?.totalAgents || 0}
                </p>
                <div className="flex items-center gap-1 mt-2 text-sm">
                  <TrendingUp className="h-3 w-3 text-green-500" />
                  <span className="text-green-600">
                    {metrics?.activeAgents || 0} activos
                  </span>
                </div>
              </div>
              <Bot className="h-8 w-8 text-primary opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Puntuación de Cumplimiento
                </p>
                <p className={`text-2xl font-bold mt-1 ${getHealthScoreColor(metrics?.complianceScore || 0)}`}>
                  {metrics?.complianceScore || 0}%
                </p>
                <div className="flex items-center gap-1 mt-2 text-sm">
                  <CheckCircle className="h-3 w-3 text-green-500" />
                  <span className="text-green-600">
                    {metrics?.certifiedAgents || 0} certificados
                  </span>
                </div>
              </div>
              <Shield className="h-8 w-8 text-green-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Aprobaciones Pendientes
                </p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {metrics?.pendingApprovals || 0}
                </p>
                <div className="flex items-center gap-1 mt-2 text-sm">
                  <Clock className="h-3 w-3 text-yellow-500" />
                  <span className="text-yellow-600">Requieren atención</span>
                </div>
              </div>
              <FileCheck className="h-8 w-8 text-yellow-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Alertas Críticas
                </p>
                <p className="text-2xl font-bold text-red-500 mt-1">
                  {metrics?.criticalAlerts || 0}
                </p>
                <div className="flex items-center gap-1 mt-2 text-sm">
                  <AlertTriangle className="h-3 w-3 text-red-500" />
                  <span className="text-red-600">Acción inmediata</span>
                </div>
              </div>
              <AlertTriangle className="h-8 w-8 text-red-500 opacity-50" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Salud de Agentes y Actividad Reciente */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Salud de Agentes */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="h-5 w-5" />
              Salud de Agentes
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {agentHealth.map((agent) => (
                <div
                  key={agent.id}
                  className="border rounded-lg p-4 hover:bg-muted/50 transition-colors cursor-pointer"
                  onClick={() => router.push(`/governance/agents/registry/${agent.id}`)}
                >
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center gap-2">
                      <Bot className="h-4 w-4 text-primary" />
                      <span className="font-semibold">{agent.name}</span>
                    </div>
                    <span className={`text-sm font-bold ${getHealthScoreColor(agent.healthScore)}`}>
                      {agent.healthScore}%
                    </span>
                  </div>
                  <div className="flex items-center gap-2 flex-wrap mt-2">
                    {getStatusBadge(agent.status)}
                    {getStatusBadge(agent.complianceStatus)}
                    {getStatusBadge(agent.certificationStatus)}
                    {agent.alerts > 0 && (
                      <Badge className="bg-red-500/20 text-red-500">
                        {agent.alerts} alertas
                      </Badge>
                    )}
                  </div>
                  <div className="flex items-center justify-between mt-3 text-sm text-muted-foreground">
                    <span>{agent.interactions24h} interacciones (24h)</span>
                    <span>Última auditoría: {new Date(agent.lastAudit).toLocaleDateString()}</span>
                  </div>
                </div>
              ))}
            </div>
            <Button
              variant="outline"
              className="w-full mt-4"
              onClick={() => router.push("/governance/agents/registry")}
            >
              Ver Todos los Agentes
              <ArrowRight className="h-4 w-4 ml-2" />
            </Button>
          </CardBody>
        </Card>

        {/* Actividad Reciente */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Clock className="h-5 w-5" />
              Actividad Reciente
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {recentActivity.map((activity) => (
                <div
                  key={activity.id}
                  className="border rounded-lg p-4 hover:bg-muted/50 transition-colors"
                >
                  <div className="flex items-start gap-3">
                    <div className="mt-1">{getActivityIcon(activity.type)}</div>
                    <div className="flex-1">
                      <div className="flex items-center justify-between mb-1">
                        <span className="font-semibold text-sm">{activity.agentName}</span>
                        {getStatusBadge(activity.status)}
                      </div>
                      <p className="text-sm text-muted-foreground mb-2">
                        {activity.description}
                      </p>
                      <span className="text-xs text-muted-foreground">
                        {new Date(activity.timestamp).toLocaleString()}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <Button
              variant="outline"
              className="w-full mt-4"
              onClick={() => router.push("/governance/agents/approval/overview")}
            >
              Ver Todas las Aprobaciones
              <ArrowRight className="h-4 w-4 ml-2" />
            </Button>
          </CardBody>
        </Card>
      </div>

      {/* Métricas Adicionales */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Requieren Revisión HITL
                </p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {metrics?.agentsRequiringReview || 0}
                </p>
              </div>
              <Users className="h-8 w-8 text-yellow-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Total Interacciones
                </p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {(metrics?.totalInteractions || 0).toLocaleString()}
                </p>
              </div>
              <BarChart3 className="h-8 w-8 text-blue-500 opacity-50" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  Tiempo Respuesta Promedio
                </p>
                <p className="text-2xl font-bold text-foreground mt-1">
                  {metrics?.avgResponseTime || 0}ms
                </p>
              </div>
              <Activity className="h-8 w-8 text-green-500 opacity-50" />
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
