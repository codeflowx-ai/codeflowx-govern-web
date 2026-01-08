"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Brain,
  Bot,
  Shield,
  CheckCircle2,
  AlertTriangle,
  ArrowRight,
  ExternalLink,
} from "lucide-react";
import Link from "next/link";

interface AIGovernanceMetrics {
  projectId: number;
  projectName: string;
  modelsCount: number;
  agentsCount: number;
  complianceStatus: "compliant" | "pending" | "non-compliant";
  approvalWorkflowsPending: number;
  riskLevel: "low" | "medium" | "high";
  lastUpdated: string;
}

const mockMetrics: AIGovernanceMetrics[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    modelsCount: 5,
    agentsCount: 3,
    complianceStatus: "compliant",
    approvalWorkflowsPending: 2,
    riskLevel: "low",
    lastUpdated: "2024-11-20T10:30:00Z",
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    modelsCount: 8,
    agentsCount: 5,
    complianceStatus: "pending",
    approvalWorkflowsPending: 5,
    riskLevel: "medium",
    lastUpdated: "2024-11-19T15:20:00Z",
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    modelsCount: 3,
    agentsCount: 2,
    complianceStatus: "non-compliant",
    approvalWorkflowsPending: 1,
    riskLevel: "high",
    lastUpdated: "2024-11-18T09:15:00Z",
  },
];

const getComplianceColor = (status: string) => {
  switch (status) {
    case "compliant":
      return "primary";
    case "pending":
      return "secondary";
    case "non-compliant":
      return "danger";
    default:
      return "outline";
  }
};

const getRiskColor = (risk: string) => {
  switch (risk) {
    case "low":
      return "primary";
    case "medium":
      return "secondary";
    case "high":
      return "danger";
    default:
      return "outline";
  }
};

export default function AIGovernanceOverviewPage() {
  const { t } = useTranslation();

  const totalModels = mockMetrics.reduce((sum, m) => sum + m.modelsCount, 0);
  const totalAgents = mockMetrics.reduce((sum, m) => sum + m.agentsCount, 0);
  const totalPending = mockMetrics.reduce((sum, m) => sum + m.approvalWorkflowsPending, 0);
  const compliantProjects = mockMetrics.filter((m) => m.complianceStatus === "compliant").length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Brain className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.aiGovernance.title", "Vista General de Gobierno de IA")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.aiGovernance.totalModels", "Total Modelos")}
              </CardTitle>
              <Brain className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalModels}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.aiGovernance.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.aiGovernance.totalAgents", "Total Agentes")}
              </CardTitle>
              <Bot className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalAgents}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.aiGovernance.acrossProjects", "en todos los proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.aiGovernance.compliantProjects", "Proyectos Conformes")}
              </CardTitle>
              <Shield className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{compliantProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.aiGovernance.ofTotal", "de {total}", { total: String(mockMetrics.length) })}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.aiGovernance.pendingApprovals", "Aprobaciones Pendientes")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalPending}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.aiGovernance.workflows", "workflows")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockMetrics.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{project.projectName}</CardTitle>
                    <div className="flex items-center gap-2 flex-wrap">
                      <Badge variant={getComplianceColor(project.complianceStatus) as any}>
                        {t(`projects.aiGovernance.compliance.${project.complianceStatus}`, project.complianceStatus)}
                      </Badge>
                      <Badge variant={getRiskColor(project.riskLevel) as any}>
                        {t(`projects.aiGovernance.risk.${project.riskLevel}`, project.riskLevel)}
                      </Badge>
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="flex items-center gap-2">
                    <Brain className="w-4 h-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">{t("projects.aiGovernance.models", "Modelos")}</p>
                      <p className="text-lg font-bold">{project.modelsCount}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Bot className="w-4 h-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">{t("projects.aiGovernance.agents", "Agentes")}</p>
                      <p className="text-lg font-bold">{project.agentsCount}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.aiGovernance.pending", "Pendientes")}
                      </p>
                      <p className="text-lg font-bold">{project.approvalWorkflowsPending}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <AlertTriangle className="w-4 h-4 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">{t("projects.aiGovernance.risk", "Riesgo")}</p>
                      <p className="text-lg font-bold capitalize">{project.riskLevel}</p>
                    </div>
                  </div>
                </div>

                <div className="flex gap-2 pt-2">
                  <Link href={`/governance/models?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.aiGovernance.viewModels", "Ver Modelos")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/governance/agents?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.aiGovernance.viewAgents", "Ver Agentes")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/bpmn/task-inbox?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.aiGovernance.viewWorkflows", "Ver Workflows")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/projects/${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("common.details", "Detalles")}
                      <ArrowRight className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
