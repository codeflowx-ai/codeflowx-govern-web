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
  Plug,
  CheckCircle2,
  XCircle,
  Clock,
  RefreshCw,
  Settings,
  ExternalLink,
} from "lucide-react";

interface Integration {
  id: number;
  name: string;
  type: "jira" | "servicenow" | "slack" | "github" | "gitlab";
  status: "connected" | "disconnected" | "error" | "syncing";
  lastSync: string;
  syncStatus: "success" | "failed" | "pending";
  projectsCount: number;
  issuesSynced: number;
}

const mockIntegrations: Integration[] = [
  {
    id: 1,
    name: "Jira Cloud",
    type: "jira",
    status: "connected",
    lastSync: "2024-11-20T10:30:00Z",
    syncStatus: "success",
    projectsCount: 5,
    issuesSynced: 142,
  },
  {
    id: 2,
    name: "ServiceNow",
    type: "servicenow",
    status: "connected",
    lastSync: "2024-11-20T09:15:00Z",
    syncStatus: "success",
    projectsCount: 3,
    issuesSynced: 87,
  },
  {
    id: 3,
    name: "GitHub",
    type: "github",
    status: "connected",
    lastSync: "2024-11-20T11:00:00Z",
    syncStatus: "success",
    projectsCount: 8,
    issuesSynced: 234,
  },
  {
    id: 4,
    name: "Slack",
    type: "slack",
    status: "disconnected",
    lastSync: "2024-11-19T15:20:00Z",
    syncStatus: "failed",
    projectsCount: 0,
    issuesSynced: 0,
  },
];

const getStatusIcon = (status: string) => {
  switch (status) {
    case "connected":
      return CheckCircle2;
    case "disconnected":
      return XCircle;
    case "error":
      return XCircle;
    case "syncing":
      return RefreshCw;
    default:
      return Clock;
  }
};

const getStatusColor = (status: string) => {
  switch (status) {
    case "connected":
      return "primary";
    case "disconnected":
      return "danger";
    case "error":
      return "danger";
    case "syncing":
      return "secondary";
    default:
      return "outline";
  }
};

const getSyncStatusColor = (status: string) => {
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

export default function IntegrationsDashboardPage() {
  const { t } = useTranslation();
  const [isConfigDialogOpen, setIsConfigDialogOpen] = useState(false);
  const [isDetailsDialogOpen, setIsDetailsDialogOpen] = useState(false);
  const [selectedIntegration, setSelectedIntegration] = useState<Integration | null>(null);

  const connectedIntegrations = mockIntegrations.filter((i) => i.status === "connected").length;
  const totalIssuesSynced = mockIntegrations.reduce((sum, i) => sum + i.issuesSynced, 0);
  const totalProjects = mockIntegrations.reduce((sum, i) => sum + i.projectsCount, 0);

  const handleConfigure = (integration?: Integration) => {
    setSelectedIntegration(integration || null);
    setIsConfigDialogOpen(true);
  };

  const handleViewDetails = (integration: Integration) => {
    setSelectedIntegration(integration);
    setIsDetailsDialogOpen(true);
  };

  const handleSyncNow = (integration: Integration) => {
    // Aquí se implementaría la lógica de sincronización
    console.log("Sincronizando:", integration.name);
    // Por ahora solo mostramos un mensaje
    alert(t("projects.integrations.syncStarted", `Iniciando sincronización de ${integration.name}...`));
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
            <Plug className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.integrations.title", "Dashboard de Integraciones")}
            </h1>
          </div>
          <Button onClick={() => handleConfigure()}>
            <Settings className="w-4 h-4 mr-2" />
            {t("projects.integrations.configure", "Configurar")}
          </Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.integrations.connected", "Conectadas")}
              </CardTitle>
              <CheckCircle2 className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-500">{connectedIntegrations}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.integrations.ofTotal", "de {total}", { total: String(mockIntegrations.length) })}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.integrations.issuesSynced", "Issues Sincronizados")}
              </CardTitle>
              <RefreshCw className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalIssuesSynced}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.integrations.total", "total")}
              </p>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.integrations.projectsConnected", "Proyectos Conectados")}
              </CardTitle>
              <Plug className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalProjects}</div>
              <p className="text-xs text-muted-foreground">
                {t("projects.integrations.projects", "proyectos")}
              </p>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockIntegrations.map((integration) => {
            const StatusIcon = getStatusIcon(integration.status);
            return (
              <Card
                key={integration.id}
                className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
              >
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg mb-2">{integration.name}</CardTitle>
                      <div className="flex items-center gap-2 flex-wrap">
                        <Badge variant={getStatusColor(integration.status) as any}>
                          <StatusIcon className="w-3 h-3 mr-1" />
                          {t(`projects.integrations.status.${integration.status}`, integration.status)}
                        </Badge>
                        <Badge variant={getSyncStatusColor(integration.syncStatus) as any}>
                          {t(`projects.integrations.syncStatus.${integration.syncStatus}`, integration.syncStatus)}
                        </Badge>
                        <Badge variant="outline">
                          {integration.projectsCount} {t("projects.integrations.projects", "proyectos")}
                        </Badge>
                      </div>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.integrations.lastSync", "Última Sincronización")}
                      </p>
                      <p className="text-sm font-medium">
                        {new Date(integration.lastSync).toLocaleString()}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.integrations.issuesSynced", "Issues Sincronizados")}
                      </p>
                      <p className="text-sm font-medium">{integration.issuesSynced}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">
                        {t("projects.integrations.projectsCount", "Proyectos")}
                      </p>
                      <p className="text-sm font-medium">{integration.projectsCount}</p>
                    </div>
                  </div>

                  <div className="flex gap-2 pt-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleSyncNow(integration)}
                    >
                      <RefreshCw className="w-4 h-4 mr-2" />
                      {t("projects.integrations.syncNow", "Sincronizar Ahora")}
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleConfigure(integration)}
                    >
                      <Settings className="w-4 h-4 mr-2" />
                      {t("projects.integrations.configure", "Configurar")}
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleViewDetails(integration)}
                    >
                      <ExternalLink className="w-4 h-4 mr-2" />
                      {t("projects.integrations.viewDetails", "Ver Detalles")}
                    </Button>
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>
      </div>

      <Dialog open={isConfigDialogOpen} onOpenChange={setIsConfigDialogOpen}>
        <DialogContent maxWidth="700px">
          <DialogHeader>
            <DialogTitle>
              {selectedIntegration
                ? t("projects.integrations.configureIntegration", `Configurar ${selectedIntegration.name}`)
                : t("projects.integrations.configureNew", "Configurar Nueva Integración")}
            </DialogTitle>
          </DialogHeader>
          <div className="space-y-4 py-4">
            {selectedIntegration ? (
              <>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.name", "Nombre")}
                  </label>
                  <input
                    type="text"
                    defaultValue={selectedIntegration.name}
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.type", "Tipo")}
                  </label>
                  <select
                    defaultValue={selectedIntegration.type}
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  >
                    <option value="jira">Jira</option>
                    <option value="servicenow">ServiceNow</option>
                    <option value="slack">Slack</option>
                    <option value="github">GitHub</option>
                    <option value="gitlab">GitLab</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.url", "URL Base")}
                  </label>
                  <input
                    type="url"
                    placeholder="https://example.atlassian.net"
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.apiKey", "API Key / Token")}
                  </label>
                  <input
                    type="password"
                    placeholder="••••••••••••"
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.syncFrequency", "Frecuencia de Sincronización")}
                  </label>
                  <select className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary">
                    <option value="realtime">{t("projects.integrations.realtime", "Tiempo Real")}</option>
                    <option value="5min">{t("projects.integrations.every5min", "Cada 5 minutos")}</option>
                    <option value="15min">{t("projects.integrations.every15min", "Cada 15 minutos")}</option>
                    <option value="1hour">{t("projects.integrations.everyHour", "Cada hora")}</option>
                    <option value="daily">{t("projects.integrations.daily", "Diario")}</option>
                  </select>
                </div>
              </>
            ) : (
              <>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.selectType", "Seleccionar Tipo de Integración")}
                  </label>
                  <select className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary">
                    <option value="">{t("projects.integrations.selectOption", "Seleccionar...")}</option>
                    <option value="jira">Jira</option>
                    <option value="servicenow">ServiceNow</option>
                    <option value="slack">Slack</option>
                    <option value="github">GitHub</option>
                    <option value="gitlab">GitLab</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.name", "Nombre")}
                  </label>
                  <input
                    type="text"
                    placeholder={t("projects.integrations.namePlaceholder", "Nombre de la integración")}
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.url", "URL Base")}
                  </label>
                  <input
                    type="url"
                    placeholder="https://example.atlassian.net"
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.apiKey", "API Key / Token")}
                  </label>
                  <input
                    type="password"
                    placeholder="••••••••••••"
                    className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-2">
                    {t("projects.integrations.syncFrequency", "Frecuencia de Sincronización")}
                  </label>
                  <select className="w-full px-3 py-2 bg-background border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary">
                    <option value="realtime">{t("projects.integrations.realtime", "Tiempo Real")}</option>
                    <option value="5min">{t("projects.integrations.every5min", "Cada 5 minutos")}</option>
                    <option value="15min">{t("projects.integrations.every15min", "Cada 15 minutos")}</option>
                    <option value="1hour">{t("projects.integrations.everyHour", "Cada hora")}</option>
                    <option value="daily">{t("projects.integrations.daily", "Diario")}</option>
                  </select>
                </div>
              </>
            )}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsConfigDialogOpen(false)}>
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button
              onClick={() => {
                // Aquí se implementaría la lógica de guardado
                setIsConfigDialogOpen(false);
                setSelectedIntegration(null);
              }}
            >
              {selectedIntegration
                ? t("common.save", "Guardar")
                : t("projects.integrations.connect", "Conectar")}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isDetailsDialogOpen} onOpenChange={setIsDetailsDialogOpen}>
        <DialogContent maxWidth="800px">
          <DialogHeader>
            <DialogTitle>
              {selectedIntegration
                ? t("projects.integrations.detailsOf", `Detalles de ${selectedIntegration.name}`)
                : t("projects.integrations.details", "Detalles de Integración")}
            </DialogTitle>
          </DialogHeader>
          {selectedIntegration && (
            <div className="space-y-4 py-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.name", "Nombre")}
                  </label>
                  <p className="text-foreground font-medium">{selectedIntegration.name}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.type", "Tipo")}
                  </label>
                  <p className="text-foreground font-medium capitalize">{selectedIntegration.type}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.status", "Estado")}
                  </label>
                  <Badge variant={getStatusColor(selectedIntegration.status) as any}>
                    {t(`projects.integrations.status.${selectedIntegration.status}`, selectedIntegration.status)}
                  </Badge>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.syncStatus", "Estado de Sincronización")}
                  </label>
                  <Badge variant={getSyncStatusColor(selectedIntegration.syncStatus) as any}>
                    {t(`projects.integrations.syncStatus.${selectedIntegration.syncStatus}`, selectedIntegration.syncStatus)}
                  </Badge>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.lastSync", "Última Sincronización")}
                  </label>
                  <p className="text-foreground">
                    {new Date(selectedIntegration.lastSync).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.projectsCount", "Proyectos")}
                  </label>
                  <p className="text-foreground font-medium">{selectedIntegration.projectsCount}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-muted-foreground mb-1">
                    {t("projects.integrations.issuesSynced", "Issues Sincronizados")}
                  </label>
                  <p className="text-foreground font-medium">{selectedIntegration.issuesSynced}</p>
                </div>
              </div>

              <div className="pt-4 border-t border-border">
                <h3 className="text-sm font-semibold mb-3">
                  {t("projects.integrations.activity", "Actividad Reciente")}
                </h3>
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm p-2 bg-background/50 rounded">
                    <span className="text-muted-foreground">
                      {t("projects.integrations.lastSyncActivity", "Última sincronización exitosa")}
                    </span>
                    <span className="text-foreground">
                      {new Date(selectedIntegration.lastSync).toLocaleString()}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-sm p-2 bg-background/50 rounded">
                    <span className="text-muted-foreground">
                      {t("projects.integrations.totalIssues", "Total de issues sincronizados")}
                    </span>
                    <span className="text-foreground font-medium">{selectedIntegration.issuesSynced}</span>
                  </div>
                  <div className="flex items-center justify-between text-sm p-2 bg-background/50 rounded">
                    <span className="text-muted-foreground">
                      {t("projects.integrations.projectsConnected", "Proyectos Conectados")}
                    </span>
                    <span className="text-foreground font-medium">{selectedIntegration.projectsCount}</span>
                  </div>
                </div>
              </div>
            </div>
          )}
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsDetailsDialogOpen(false)}>
              {t("common.close", "Cerrar")}
            </Button>
            <Button
              onClick={() => {
                if (selectedIntegration) {
                  setIsDetailsDialogOpen(false);
                  handleConfigure(selectedIntegration);
                }
              }}
            >
              <Settings className="w-4 h-4 mr-2" />
              {t("projects.integrations.configure", "Configurar")}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
