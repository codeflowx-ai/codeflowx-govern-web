"use client";

import { useTranslation } from "@/app/config/i18n";
import { useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Shield,
  CheckCircle,
  AlertTriangle,
  Clock,
  Database,
  TrendingUp,
  FileText,
  Eye,
  ArrowRight,
  Search,
  Filter,
  RefreshCw,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface RAGComplianceDashboardData {
  totalProjects: number;
  pendingClassification: number;
  highRiskProjects: number;
  friaCompleted: number;
  euRegistered: number;
  averageComplianceScore: number;
  complianceStatus: string;
  projectsByStatus: {
    pending: number;
    inProgress: number;
    compliant: number;
    nonCompliant: number;
  };
}

const mockDashboardData: RAGComplianceDashboardData = {
  totalProjects: 24,
  pendingClassification: 8,
  highRiskProjects: 12,
  friaCompleted: 10,
  euRegistered: 15,
  averageComplianceScore: 78.5,
  complianceStatus: "Cumplimiento Parcial",
  projectsByStatus: {
    pending: 8,
    inProgress: 6,
    compliant: 7,
    nonCompliant: 3,
  },
};

const mockRecentActivity = [
  {
    id: 1,
    projectName: "Ecommerce Knowledge Base",
    action: "FRIA completado",
    date: "2025-01-15T10:30:00Z",
    status: "success",
  },
  {
    id: 2,
    projectName: "Legal Document Assistant",
    action: "Clasificado como alto riesgo",
    date: "2025-01-15T09:15:00Z",
    status: "warning",
  },
  {
    id: 3,
    projectName: "DemoRAGProject",
    action: "Registrado en EU",
    date: "2025-01-15T08:45:00Z",
    status: "success",
  },
  {
    id: 4,
    projectName: "Technical Support RAG",
    action: "Clasificación pendiente",
    date: "2025-01-15T07:20:00Z",
    status: "pending",
  },
];

export default function RAGComplianceDashboardPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case "success":
        return "success";
      case "warning":
        return "warning";
      case "pending":
        return "outline";
      default:
        return "outline";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "success":
        return "text-green-600";
      case "warning":
        return "text-yellow-600";
      case "pending":
        return "text-gray-600";
      default:
        return "text-gray-600";
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t(
                "governance.rag.compliance.dashboard.title",
                "Dashboard de Cumplimiento RAG"
              )}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t(
              "governance.rag.compliance.dashboard.subtitle",
              "Vista consolidada de cumplimiento normativo de proyectos RAG"
            )}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            onClick={() => router.push("/governance/rag/registry")}
          >
            <Eye className="h-4 w-4 mr-2" />
            {t("governance.rag.compliance.dashboard.viewRegistry", "Ver Registro")}
          </Button>
        </div>
      </div>

      {/* KPIs Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.kpis.totalProjects",
                    "Total Proyectos"
                  )}
                </p>
                <p className="text-3xl font-bold text-foreground mt-2">
                  {mockDashboardData.totalProjects}
                </p>
              </div>
              <div className="p-3 bg-blue-100 dark:bg-blue-500/20 rounded-full">
                <Database className="h-6 w-6 text-blue-600 dark:text-blue-400" />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.kpis.pendingClassification",
                    "Pendientes Clasificación"
                  )}
                </p>
                <p className="text-3xl font-bold text-yellow-600 mt-2">
                  {mockDashboardData.pendingClassification}
                </p>
              </div>
              <div className="p-3 bg-yellow-100 dark:bg-yellow-500/20 rounded-full">
                <Clock className="h-6 w-6 text-yellow-600 dark:text-yellow-400" />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.kpis.highRisk",
                    "Alto Riesgo"
                  )}
                </p>
                <p className="text-3xl font-bold text-red-600 mt-2">
                  {mockDashboardData.highRiskProjects}
                </p>
              </div>
              <div className="p-3 bg-red-100 dark:bg-red-500/20 rounded-full">
                <AlertTriangle className="h-6 w-6 text-red-600 dark:text-red-400" />
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.kpis.complianceScore",
                    "Score Cumplimiento"
                  )}
                </p>
                <p className="text-3xl font-bold text-green-600 mt-2">
                  {mockDashboardData.averageComplianceScore}%
                </p>
              </div>
              <div className="p-3 bg-green-100 dark:bg-green-500/20 rounded-full">
                <CheckCircle className="h-6 w-6 text-green-600 dark:text-green-400" />
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Métricas de Cumplimiento */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <FileText className="h-5 w-5 text-primary" />
              {t(
                "governance.rag.compliance.dashboard.fria.title",
                "FRIA Completados"
              )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.fria.completed",
                    "Completados"
                  )}
                </span>
                <span className="text-2xl font-bold text-foreground">
                  {mockDashboardData.friaCompleted}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.fria.total",
                    "Total Proyectos"
                  )}
                </span>
                <span className="text-lg font-semibold text-muted-foreground">
                  {mockDashboardData.totalProjects}
                </span>
              </div>
              <div className="mt-4">
                <Button
                  variant="outline"
                  size="sm"
                  className="w-full"
                  onClick={() => router.push("/governance/rag/fria")}
                >
                  {t(
                    "governance.rag.compliance.dashboard.fria.viewAll",
                    "Ver Todos"
                  )}
                  <ArrowRight className="h-4 w-4 ml-2" />
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <Database className="h-5 w-5 text-primary" />
              {t(
                "governance.rag.compliance.dashboard.euRegistration.title",
                "Registro EU"
              )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.euRegistration.registered",
                    "Registrados"
                  )}
                </span>
                <span className="text-2xl font-bold text-foreground">
                  {mockDashboardData.euRegistered}
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.euRegistration.total",
                    "Total Proyectos"
                  )}
                </span>
                <span className="text-lg font-semibold text-muted-foreground">
                  {mockDashboardData.totalProjects}
                </span>
              </div>
              <div className="mt-4">
                <Button
                  variant="outline"
                  size="sm"
                  className="w-full"
                  onClick={() => router.push("/governance/compliance/eu-registration")}
                >
                  {t(
                    "governance.rag.compliance.dashboard.euRegistration.viewAll",
                    "Ver Todos"
                  )}
                  <ArrowRight className="h-4 w-4 ml-2" />
                </Button>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-base font-medium">
              <TrendingUp className="h-5 w-5 text-primary" />
              {t(
                "governance.rag.compliance.dashboard.status.title",
                "Estado General"
              )}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.status.pending",
                    "Pendientes"
                  )}
                </span>
                <Badge variant="outline" className="bg-yellow-50 text-yellow-700">
                  {mockDashboardData.projectsByStatus.pending}
                </Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.status.inProgress",
                    "En Progreso"
                  )}
                </span>
                <Badge variant="outline" className="bg-blue-50 text-blue-700">
                  {mockDashboardData.projectsByStatus.inProgress}
                </Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.status.compliant",
                    "Cumplimiento"
                  )}
                </span>
                <Badge variant="success" className="bg-green-50 text-green-700">
                  {mockDashboardData.projectsByStatus.compliant}
                </Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">
                  {t(
                    "governance.rag.compliance.dashboard.status.nonCompliant",
                    "No Cumplimiento"
                  )}
                </span>
                <Badge variant="danger">
                  {mockDashboardData.projectsByStatus.nonCompliant}
                </Badge>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Próximas Acciones Requeridas */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-base font-medium">
            <AlertTriangle className="h-5 w-5 text-primary" />
            {t(
              "governance.rag.compliance.dashboard.actionsRequired.title",
              "Próximas Acciones Requeridas"
            )}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between p-4 bg-yellow-50 dark:bg-yellow-500/10 rounded-lg border border-yellow-200 dark:border-yellow-500/20">
              <div className="flex items-center gap-3">
                <Clock className="h-5 w-5 text-yellow-600" />
                <div>
                  <p className="font-medium text-foreground">
                    {t(
                      "governance.rag.compliance.dashboard.actionsRequired.classificationPending",
                      "Proyectos pendientes de clasificación"
                    )}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    {mockDashboardData.pendingClassification}{" "}
                    {t("governance.rag.compliance.dashboard.actionsRequired.projects", "proyectos")}
                  </p>
                </div>
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => router.push("/governance/rag/classification")}
              >
                {t("governance.rag.compliance.dashboard.actionsRequired.view", "Ver")}
                <ArrowRight className="h-4 w-4 ml-2" />
              </Button>
            </div>

            <div className="flex items-center justify-between p-4 bg-blue-50 dark:bg-blue-500/10 rounded-lg border border-blue-200 dark:border-blue-500/20">
              <div className="flex items-center gap-3">
                <FileText className="h-5 w-5 text-blue-600" />
                <div>
                  <p className="font-medium text-foreground">
                    {t(
                      "governance.rag.compliance.dashboard.actionsRequired.friaPending",
                      "FRIAs pendientes de completar"
                    )}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    {mockDashboardData.highRiskProjects - mockDashboardData.friaCompleted}{" "}
                    {t("governance.rag.compliance.dashboard.actionsRequired.frias", "FRIAs")}
                  </p>
                </div>
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => router.push("/governance/rag/fria")}
              >
                {t("governance.rag.compliance.dashboard.actionsRequired.view", "Ver")}
                <ArrowRight className="h-4 w-4 ml-2" />
              </Button>
            </div>

            <div className="flex items-center justify-between p-4 bg-purple-50 dark:bg-purple-500/10 rounded-lg border border-purple-200 dark:border-purple-500/20">
              <div className="flex items-center gap-3">
                <Database className="h-5 w-5 text-purple-600" />
                <div>
                  <p className="font-medium text-foreground">
                    {t(
                      "governance.rag.compliance.dashboard.actionsRequired.euRegistrationPending",
                      "Registros EU pendientes"
                    )}
                  </p>
                  <p className="text-sm text-muted-foreground">
                    {mockDashboardData.totalProjects - mockDashboardData.euRegistered}{" "}
                    {t("governance.rag.compliance.dashboard.actionsRequired.projects", "proyectos")}
                  </p>
                </div>
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => router.push("/governance/compliance/eu-registration")}
              >
                {t("governance.rag.compliance.dashboard.actionsRequired.view", "Ver")}
                <ArrowRight className="h-4 w-4 ml-2" />
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Actividad Reciente */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-base font-medium">
            <RefreshCw className="h-5 w-5 text-primary" />
            {t(
              "governance.rag.compliance.dashboard.recentActivity.title",
              "Actividad Reciente"
            )}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-3">
            {mockRecentActivity.map((activity) => (
              <div
                key={activity.id}
                className="flex items-center justify-between p-3 bg-background/50 rounded-lg border border-border hover:bg-background/70 transition-colors"
              >
                <div className="flex items-center gap-3">
                  <Badge variant={getStatusBadgeVariant(activity.status)}>
                    {activity.action}
                  </Badge>
                  <span className="font-medium text-foreground">
                    {activity.projectName}
                  </span>
                </div>
                <div className="flex items-center gap-3">
                  <span className={`text-sm ${getStatusColor(activity.status)}`}>
                    {formatDate(activity.date)}
                  </span>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() =>
                      router.push(`/governance/rag/registry?project=${activity.projectName}`)
                    }
                  >
                    <Eye className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
