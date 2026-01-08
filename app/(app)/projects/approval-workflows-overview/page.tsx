"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  CheckCircle2,
  Clock,
  Brain,
  Bot,
  Shield,
  ExternalLink,
  ArrowRight,
} from "lucide-react";
import Link from "next/link";

interface ApprovalWorkflow {
  projectId: number;
  projectName: string;
  modelApprovalsPending: number;
  agentApprovalsPending: number;
  complianceApprovalsPending: number;
  bpmnTasksPending: number;
  totalPending: number;
  lastUpdated: string;
}

const mockApprovalWorkflows: ApprovalWorkflow[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    modelApprovalsPending: 2,
    agentApprovalsPending: 1,
    complianceApprovalsPending: 0,
    bpmnTasksPending: 3,
    totalPending: 6,
    lastUpdated: "2024-11-20T10:30:00Z",
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    modelApprovalsPending: 5,
    agentApprovalsPending: 3,
    complianceApprovalsPending: 2,
    bpmnTasksPending: 4,
    totalPending: 14,
    lastUpdated: "2024-11-19T15:20:00Z",
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    modelApprovalsPending: 1,
    agentApprovalsPending: 0,
    complianceApprovalsPending: 1,
    bpmnTasksPending: 2,
    totalPending: 4,
    lastUpdated: "2024-11-18T09:15:00Z",
  },
];

export default function ApprovalWorkflowsOverviewPage() {
  const { t } = useTranslation();

  const totalPending = mockApprovalWorkflows.reduce((sum, p) => sum + p.totalPending, 0);
  const totalModelApprovals = mockApprovalWorkflows.reduce((sum, p) => sum + p.modelApprovalsPending, 0);
  const totalAgentApprovals = mockApprovalWorkflows.reduce((sum, p) => sum + p.agentApprovalsPending, 0);
  const totalComplianceApprovals = mockApprovalWorkflows.reduce((sum, p) => sum + p.complianceApprovalsPending, 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <CheckCircle2 className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.approvalWorkflows.title", "Vista General de Workflows de Aprobación")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.approvalWorkflows.totalPending", "Total Pendientes")}
              </CardTitle>
              <Clock className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalPending}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.approvalWorkflows.approvals", "aprobaciones")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.approvalWorkflows.modelApprovals", "Aprobaciones de Modelos")}
              </CardTitle>
              <Brain className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalModelApprovals}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.approvalWorkflows.pending", "pendientes")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.approvalWorkflows.agentApprovals", "Aprobaciones de Agentes")}
              </CardTitle>
              <Bot className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalAgentApprovals}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.approvalWorkflows.pending", "pendientes")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.approvalWorkflows.complianceApprovals", "Aprobaciones de Compliance")}
              </CardTitle>
              <Shield className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalComplianceApprovals}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.approvalWorkflows.pending", "pendientes")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockApprovalWorkflows.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{project.projectName}</CardTitle>
                    <Badge variant={project.totalPending > 0 ? "secondary" : "primary"} className="mb-2">
                      {project.totalPending} {t("projects.approvalWorkflows.pending", "pendientes")}
                    </Badge>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="flex items-center gap-2">
                    <Brain className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.approvalWorkflows.models", "Modelos")}
                      </p>
                      <p className="text-lg font-bold">{project.modelApprovalsPending}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Bot className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.approvalWorkflows.agents", "Agentes")}
                      </p>
                      <p className="text-lg font-bold">{project.agentApprovalsPending}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Shield className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.approvalWorkflows.compliance", "Compliance")}
                      </p>
                      <p className="text-lg font-bold">{project.complianceApprovalsPending}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <CheckCircle2 className="w-5 h-5 text-muted-foreground" />
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.approvalWorkflows.bpmnTasks", "Tareas BPMN")}
                      </p>
                      <p className="text-lg font-bold">{project.bpmnTasksPending}</p>
                    </div>
                  </div>
                </div>

                <div className="flex gap-2 pt-2">
                  <Link href={`/bpmn/task-inbox?projectId=${project.projectId}&type=model-approval`}>
                    <Button variant="outline" size="sm">
                      {t("projects.approvalWorkflows.viewModelApprovals", "Aprobaciones Modelos")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/bpmn/task-inbox?projectId=${project.projectId}&type=agent-approval`}>
                    <Button variant="outline" size="sm">
                      {t("projects.approvalWorkflows.viewAgentApprovals", "Aprobaciones Agentes")}
                      <ExternalLink className="w-4 h-4 ml-2" />
                    </Button>
                  </Link>
                  <Link href={`/bpmn/task-inbox?projectId=${project.projectId}`}>
                    <Button variant="outline" size="sm">
                      {t("projects.approvalWorkflows.viewAllTasks", "Ver Todas las Tareas")}
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
