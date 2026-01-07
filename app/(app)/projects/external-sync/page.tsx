"use client";

import { useState } from "react";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  RefreshCw,
  CheckCircle2,
  XCircle,
  Clock,
  ExternalLink,
  Settings,
  ArrowRight,
} from "lucide-react";
import Link from "next/link";

interface ExternalSync {
  projectId: number;
  projectName: string;
  jiraIssues: {
    synced: number;
    pending: number;
    lastSync: string;
    status: "success" | "failed" | "pending";
  };
  servicenowTickets: {
    synced: number;
    pending: number;
    lastSync: string;
    status: "success" | "failed" | "pending";
  };
}

const mockExternalSync: ExternalSync[] = [
  {
    projectId: 1,
    projectName: "Proyecto Alpha",
    jiraIssues: {
      synced: 45,
      pending: 3,
      lastSync: "2024-11-20T10:30:00Z",
      status: "success",
    },
    servicenowTickets: {
      synced: 23,
      pending: 1,
      lastSync: "2024-11-20T09:15:00Z",
      status: "success",
    },
  },
  {
    projectId: 2,
    projectName: "Proyecto Beta",
    jiraIssues: {
      synced: 67,
      pending: 5,
      lastSync: "2024-11-20T11:00:00Z",
      status: "success",
    },
    servicenowTickets: {
      synced: 34,
      pending: 2,
      lastSync: "2024-11-20T10:45:00Z",
      status: "pending",
    },
  },
  {
    projectId: 3,
    projectName: "Proyecto Gamma",
    jiraIssues: {
      synced: 30,
      pending: 0,
      lastSync: "2024-11-19T15:20:00Z",
      status: "success",
    },
    servicenowTickets: {
      synced: 15,
      pending: 0,
      lastSync: "2024-11-19T14:30:00Z",
      status: "success",
    },
  },
];

const getStatusIcon = (status: string) => {
  switch (status) {
    case "success":
      return CheckCircle2;
    case "failed":
      return XCircle;
    case "pending":
      return RefreshCw;
    default:
      return Clock;
  }
};

const getStatusColor = (status: string) => {
  switch (status) {
    case "success":
      return "primary";
    case "failed":
      return "danger";
    case "pending":
      return "secondary";
    default:
      return "outline";
  }
};

export default function ExternalSyncPage() {
  const { t } = useTranslation();
  const [isConfigDialogOpen, setIsConfigDialogOpen] = useState(false);
  const [isMappingDialogOpen, setIsMappingDialogOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState<ExternalSync | null>(null);

  const totalJiraIssues = mockExternalSync.reduce((sum, p) => sum + p.jiraIssues.synced, 0);
  const totalServiceNowTickets = mockExternalSync.reduce((sum, p) => sum + p.servicenowTickets.synced, 0);
  const totalPending = mockExternalSync.reduce(
    (sum, p) => sum + p.jiraIssues.pending + p.servicenowTickets.pending,
    0
  );

  const handleSyncNow = (project: ExternalSync) => {
    // Aquí se implementaría la lógica de sincronización
    console.log("Sincronizando proyecto:", project.projectName);
    alert(t("projects.externalSync.syncStarted", `Iniciando sincronización para ${project.projectName}...`));
  };

  const handleConfigureMapping = (project: ExternalSync) => {
    setSelectedProject(project);
    setIsMappingDialogOpen(true);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <RefreshCw className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.externalSync.title", "Sincronización con Herramientas Externas")}
            </h1>
          </div>
          <Button onClick={() => setIsConfigDialogOpen(true)}>
            <Settings className="w-4 h-4 mr-2" />
            {t("projects.externalSync.configure", "Configurar")}
          </Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.externalSync.jiraIssues", "Issues de Jira")}
              </CardTitle>
              <ExternalLink className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalJiraIssues}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.externalSync.synced", "sincronizados")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.externalSync.servicenowTickets", "Tickets de ServiceNow")}
              </CardTitle>
              <ExternalLink className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalServiceNowTickets}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.externalSync.synced", "sincronizados")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.externalSync.pending", "Pendientes")}
              </CardTitle>
              <Clock className="h-4 w-4 text-yellow-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-yellow-500">{totalPending}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.externalSync.syncPending", "sincronización pendiente")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockExternalSync.map((project) => (
            <Card
              key={project.projectId}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{project.projectName}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-3 p-4 rounded-lg bg-muted/50">
                    <div className="flex items-center justify-between">
                      <h3 className="font-medium">Jira Issues</h3>
                      {(() => {
                        const Icon = getStatusIcon(project.jiraIssues.status);
                        return <Icon className={`w-4 h-4 ${project.jiraIssues.status === "success" ? "text-green-500" : project.jiraIssues.status === "failed" ? "text-red-500" : "text-yellow-500"}`} />;
                      })()}
                    </div>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.synced", "Sincronizados")}
                        </span>
                        <span className="font-medium">{project.jiraIssues.synced}</span>
                      </div>
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.pending", "Pendientes")}
                        </span>
                        <span className="font-medium text-yellow-500">{project.jiraIssues.pending}</span>
                      </div>
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.lastSync", "Última Sincronización")}
                        </span>
                        <span className="font-medium text-xs">
                          {new Date(project.jiraIssues.lastSync).toLocaleString()}
                        </span>
                      </div>
                    </div>
                    <Badge variant={getStatusColor(project.jiraIssues.status) as any} className="w-full justify-center">
                      {t(`projects.externalSync.status.${project.jiraIssues.status}`, project.jiraIssues.status)}
                    </Badge>
                  </div>

                  <div className="space-y-3 p-4 rounded-lg bg-muted/50">
                    <div className="flex items-center justify-between">
                      <h3 className="font-medium">ServiceNow Tickets</h3>
                      {(() => {
                        const Icon = getStatusIcon(project.servicenowTickets.status);
                        return <Icon className={`w-4 h-4 ${project.servicenowTickets.status === "success" ? "text-green-500" : project.servicenowTickets.status === "failed" ? "text-red-500" : "text-yellow-500"}`} />;
                      })()}
                    </div>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.synced", "Sincronizados")}
                        </span>
                        <span className="font-medium">{project.servicenowTickets.synced}</span>
                      </div>
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.pending", "Pendientes")}
                        </span>
                        <span className="font-medium text-yellow-500">{project.servicenowTickets.pending}</span>
                      </div>
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">
                          {t("projects.externalSync.lastSync", "Última Sincronización")}
                        </span>
                        <span className="font-medium text-xs">
                          {new Date(project.servicenowTickets.lastSync).toLocaleString()}
                        </span>
                      </div>
                    </div>
                    <Badge variant={getStatusColor(project.servicenowTickets.status) as any} className="w-full justify-center">
                      {t(`projects.externalSync.status.${project.servicenowTickets.status}`, project.servicenowTickets.status)}
                    </Badge>
                  </div>
                </div>

                <div className="flex gap-2 pt-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleSyncNow(project)}
                  >
                    <RefreshCw className="w-4 h-4 mr-2" />
                    {t("projects.externalSync.syncNow", "Sincronizar Ahora")}
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => handleConfigureMapping(project)}
                  >
                    <Settings className="w-4 h-4 mr-2" />
                    {t("projects.externalSync.configureMapping", "Configurar Mapeo")}
                  </Button>
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
