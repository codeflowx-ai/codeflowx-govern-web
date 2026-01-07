"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Shield,
  CheckCircle2,
  Clock,
  XCircle,
  ExternalLink,
  AlertTriangle,
} from "lucide-react";
import Link from "next/link";

interface ComplianceStatus {
  projectId: number;
  projectName: string;
  classification: "compliant" | "pending" | "non-compliant";
  fria: "completed" | "in-progress" | "not-started";
  euRegistration: "registered" | "pending" | "not-required";
  postMarketMonitoring: "active" | "inactive";
  conformity: "declared" | "pending" | "not-declared";
  hitl: "supervised" | "not-supervised";
  qms: "certified" | "in-progress" | "not-certified";
  lastUpdated: string;
}

const mockComplianceStatus: ComplianceStatus[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    classification: "compliant",
    fria: "completed",
    euRegistration: "registered",
    postMarketMonitoring: "active",
    conformity: "declared",
    hitl: "supervised",
    qms: "certified",
    lastUpdated: "2024-11-20T10:30:00Z",
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    classification: "pending",
    fria: "in-progress",
    euRegistration: "pending",
    postMarketMonitoring: "active",
    conformity: "pending",
    hitl: "supervised",
    qms: "in-progress",
    lastUpdated: "2024-11-19T15:20:00Z",
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    classification: "non-compliant",
    fria: "not-started",
    euRegistration: "not-required",
    postMarketMonitoring: "inactive",
    conformity: "not-declared",
    hitl: "not-supervised",
    qms: "not-certified",
    lastUpdated: "2024-11-18T09:15:00Z",
  },
];

const getStatusIcon = (status: string) => {
  switch (status) {
    case "compliant":
    case "completed":
    case "registered":
    case "active":
    case "declared":
    case "supervised":
    case "certified":
      return CheckCircle2;
    case "pending":
    case "in-progress":
      return Clock;
    case "non-compliant":
    case "not-started":
    case "not-required":
    case "inactive":
    case "not-declared":
    case "not-supervised":
    case "not-certified":
      return XCircle;
    default:
      return AlertTriangle;
  }
};

const getStatusColor = (status: string) => {
  if (
    status === "compliant" ||
    status === "completed" ||
    status === "registered" ||
    status === "active" ||
    status === "declared" ||
    status === "supervised" ||
    status === "certified"
  ) {
    return "primary";
  }
  if (status === "pending" || status === "in-progress") {
    return "secondary";
  }
  return "danger";
};

export default function ComplianceStatusPage() {
  const { t } = useTranslation();

  const compliantProjects = mockComplianceStatus.filter((p) => p.classification === "compliant").length;
  const pendingProjects = mockComplianceStatus.filter((p) => p.classification === "pending").length;
  const nonCompliantProjects = mockComplianceStatus.filter((p) => p.classification === "non-compliant").length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Shield className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.complianceStatus.title", "Estado de Cumplimiento por Proyecto")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.complianceStatus.compliant", "Conformes")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-500">{compliantProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.complianceStatus.projects", "proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.complianceStatus.pending", "Pendientes")}
              </CardTitle>
              <Clock className="h-4 w-4 text-yellow-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-yellow-500">{pendingProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.complianceStatus.projects", "proyectos")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-red-500/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.complianceStatus.nonCompliant", "No Conformes")}
              </CardTitle>
              <XCircle className="h-4 w-4 text-red-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-500">{nonCompliantProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.complianceStatus.projects", "proyectos")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockComplianceStatus.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{project.projectName}</CardTitle>
                    <Badge variant={getStatusColor(project.classification) as any} className="mb-3">
                      {t(`projects.complianceStatus.classification.${project.classification}`, project.classification)}
                    </Badge>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.fria);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">{t("projects.complianceStatus.fria", "FRIA")}</p>
                    </div>
                    <Badge variant={getStatusColor(project.fria) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.friaStatus.${project.fria}`, project.fria)}
                    </Badge>
                    <Link href={`/governance/compliance/fria/projects?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>

                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.euRegistration);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">
                        {t("projects.complianceStatus.euRegistration", "EU Registration")}
                      </p>
                    </div>
                    <Badge variant={getStatusColor(project.euRegistration) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.euStatus.${project.euRegistration}`, project.euRegistration)}
                    </Badge>
                    <Link href={`/governance/compliance/eu-registration?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>

                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.postMarketMonitoring);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">
                        {t("projects.complianceStatus.pmm", "PMM")}
                      </p>
                    </div>
                    <Badge variant={getStatusColor(project.postMarketMonitoring) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.pmmStatus.${project.postMarketMonitoring}`, project.postMarketMonitoring)}
                    </Badge>
                    <Link href={`/governance/compliance/post-market-monitoring?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>

                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.conformity);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">
                        {t("projects.complianceStatus.conformity", "Conformity")}
                      </p>
                    </div>
                    <Badge variant={getStatusColor(project.conformity) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.conformityStatus.${project.conformity}`, project.conformity)}
                    </Badge>
                    <Link href={`/governance/compliance/conformity-declaration/projects?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>
                </div>

                <div className="grid grid-cols-2 md:grid-cols-3 gap-4 pt-2">
                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.hitl);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">{t("projects.complianceStatus.hitl", "HITL")}</p>
                    </div>
                    <Badge variant={getStatusColor(project.hitl) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.hitlStatus.${project.hitl}`, project.hitl)}
                    </Badge>
                    <Link href={`/governance/compliance/hitl-supervision?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>

                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.qms);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">{t("projects.complianceStatus.qms", "QMS")}</p>
                    </div>
                    <Badge variant={getStatusColor(project.qms) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.qmsStatus.${project.qms}`, project.qms)}
                    </Badge>
                    <Link href={`/governance/compliance/qms/projects?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>

                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      {(() => {
                        const Icon = getStatusIcon(project.classification);
                        return <Icon className="w-4 h-4 text-muted-foreground" />;
                      })()}
                      <p className="text-sm text-muted-foreground">
                        {t("projects.complianceStatus.classification", "Classification")}
                      </p>
                    </div>
                    <Badge variant={getStatusColor(project.classification) as any} className="w-full justify-center">
                      {t(`projects.complianceStatus.classificationStatus.${project.classification}`, project.classification)}
                    </Badge>
                    <Link href={`/governance/compliance/classification/projects?projectId=${project.projectId}`}>
                      <p className="text-xs text-primary hover:underline flex items-center gap-1">
                        {t("common.viewDetails", "Ver detalles")}
                        <ExternalLink className="w-3 h-3" />
                      </p>
                    </Link>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}


