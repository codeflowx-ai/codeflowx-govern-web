"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { MessageSquare, Plus, Search, Trash2, Eye, Users, MessageCircle } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";

interface Interaction {
  id: number;
  interactionType: string;
  status: string;
  participants: number;
  timestamp: string;
}

interface Collaboration {
  id: number;
  collaborationType: string;
  status: string;
  agents: number;
  createdAt: string;
}

interface Communication {
  id: number;
  communicationType: string;
  status: string;
  messages: number;
  lastMessage: string;
}

export default function InteractionsOverviewPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { t, mounted } = useTranslation();
  const [activeTab, setActiveTab] = useState(searchParams.get("tab") || "overview");

  // Overview data
  const [interactions, setInteractions] = useState<Interaction[]>([]);
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);

  // Collaboration data
  const [collaborations, setCollaborations] = useState<Collaboration[]>([]);
  const [collabTotal, setCollabTotal] = useState(0);
  const [collabActive, setCollabActive] = useState(0);
  const [collabPending, setCollabPending] = useState(0);
  const [collabSearchTerm, setCollabSearchTerm] = useState("");

  // Communication data
  const [communications, setCommunications] = useState<Communication[]>([]);
  const [commTotal, setCommTotal] = useState(0);
  const [commActive, setCommActive] = useState(0);
  const [commPending, setCommPending] = useState(0);
  const [commSearchTerm, setCommSearchTerm] = useState("");

  useEffect(() => {
    loadOverviewData();
    loadCollaborationData();
    loadCommunicationData();
  }, []);

  const loadOverviewData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/interactions/list");
      const data = await response.json();
      setInteractions(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading interactions:", error);
      setInteractions([
        { id: 1, interactionType: "COLLABORATION", status: "ACTIVE", participants: 3, timestamp: "2024-01-15 10:30" },
        { id: 2, interactionType: "COMMUNICATION", status: "PENDING", participants: 2, timestamp: "2024-01-15 11:00" },
      ]);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const loadCollaborationData = async () => {
    try {
      const response = await fetch("/api/agents/interactions/collaboration/list");
      const data = await response.json();
      setCollaborations(data.items || []);
      setCollabTotal(data.total || 0);
      setCollabActive(data.active || 0);
      setCollabPending(data.pending || 0);
    } catch (error) {
      console.error("Error loading collaborations:", error);
      setCollaborations([
        { id: 1, collaborationType: "TEAM", status: "ACTIVE", agents: 4, createdAt: "2024-01-15" },
        { id: 2, collaborationType: "PAIR", status: "PENDING", agents: 2, createdAt: "2024-01-16" },
      ]);
      setCollabTotal(2);
      setCollabActive(1);
      setCollabPending(1);
    }
  };

  const loadCommunicationData = async () => {
    try {
      const response = await fetch("/api/agents/interactions/communication/list");
      const data = await response.json();
      setCommunications(data.items || []);
      setCommTotal(data.total || 0);
      setCommActive(data.active || 0);
      setCommPending(data.pending || 0);
    } catch (error) {
      console.error("Error loading communications:", error);
      setCommunications([
        { id: 1, communicationType: "CHAT", status: "ACTIVE", messages: 45, lastMessage: "2024-01-15 10:30" },
        { id: 2, communicationType: "EMAIL", status: "PENDING", messages: 12, lastMessage: "2024-01-15 11:00" },
      ]);
      setCommTotal(2);
      setCommActive(1);
      setCommPending(1);
    }
  };

  const filteredInteractions = interactions.filter((item) =>
    item.interactionType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredCollaborations = collaborations.filter((item) =>
    item.collaborationType.toLowerCase().includes(collabSearchTerm.toLowerCase())
  );

  const filteredCommunications = communications.filter((item) =>
    item.communicationType.toLowerCase().includes(commSearchTerm.toLowerCase())
  );

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

  const renderTableRow = (status: string) => (
    <span
      className={`px-2 py-1 rounded text-xs ${
        status === "ACTIVE"
          ? "bg-green-500/20 text-green-500"
          : "bg-yellow-500/20 text-yellow-500"
      }`}
    >
      {status}
    </span>
  );

  return (
    <div className="space-y-4 p-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <MessageSquare className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.interactions.title", "Interacciones")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.interactions.subtitle", "Gestión de interacciones, colaboración y comunicación")}
          </p>
        </div>
        <Button variant="outline" size="sm" className="flex items-center gap-2" onClick={() => router.push("/governance/agents/interactions/create")}>
          <Plus className="h-4 w-4" />
          {t("agents.interactions.register", "Registrar Interacción")}
        </Button>
      </div>

      {/* Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} defaultValue={activeTab} className="space-y-6">
        <TabsList className="flex space-x-1 p-1">
          <TabsTrigger value="overview" className="flex-1">
            {t("agents.interactions.tabs.overview", "Resumen")}
          </TabsTrigger>
          <TabsTrigger value="collaboration" className="flex-1">
            {t("agents.interactions.tabs.collaboration", "Colaboración")}
          </TabsTrigger>
          <TabsTrigger value="communication" className="flex-1">
            {t("agents.interactions.tabs.communication", "Comunicación")}
          </TabsTrigger>
        </TabsList>

        {/* Tab: Overview */}
        <TabsContent value="overview" className="space-y-6">
          {/* Estadísticas */}
          <div className="grid grid-cols-3 gap-4">
            <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalItems}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
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
                    placeholder={t("common.search", "Buscar...")}
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
                        <th className="text-left p-2">{t("agents.interactions.interactionType", "Tipo")}</th>
                        <th className="text-left p-2">{t("common.status", "Estado")}</th>
                        <th className="text-left p-2">{t("agents.interactions.participants", "Participantes")}</th>
                        <th className="text-left p-2">{t("agents.interactions.timestamp", "Fecha")}</th>
                        <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {filteredInteractions.map((item) => (
                        <tr key={item.id} className="border-b border-border hover:bg-card/50 transition-colors">
                          <td className="p-2">{item.id}</td>
                          <td className="p-2">
                            <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                              {item.interactionType}
                            </span>
                          </td>
                          <td className="p-2">{renderTableRow(item.status)}</td>
                          <td className="p-2">{item.participants}</td>
                          <td className="p-2">{item.timestamp}</td>
                          <td className="p-2">
                            <div className="flex gap-2">
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                <Eye className="w-4 h-4" />
                              </Button>
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                                <Trash2 className="w-4 h-4" />
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
        </TabsContent>

        {/* Tab: Collaboration */}
        <TabsContent value="collaboration" className="space-y-6">
          {/* Estadísticas */}
          <div className="grid grid-cols-3 gap-4">
            <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {collabTotal}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {collabActive}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {collabPending}
                </h2>
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
                    placeholder={t("common.search", "Buscar...")}
                    value={collabSearchTerm}
                    onChange={(e) => setCollabSearchTerm(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                  />
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Tabla */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("agents.collaboration.list", "Listado de Colaboraciones")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              {filteredCollaborations.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="border-b border-border">
                        <th className="text-left p-2">{t("common.id", "ID")}</th>
                        <th className="text-left p-2">{t("agents.collaboration.collaborationType", "Tipo")}</th>
                        <th className="text-left p-2">{t("common.status", "Estado")}</th>
                        <th className="text-left p-2">{t("agents.collaboration.agents", "Agentes")}</th>
                        <th className="text-left p-2">{t("agents.collaboration.createdAt", "Creado")}</th>
                        <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {filteredCollaborations.map((item) => (
                        <tr key={item.id} className="border-b border-border hover:bg-card/50 transition-colors">
                          <td className="p-2">{item.id}</td>
                          <td className="p-2">
                            <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                              {item.collaborationType}
                            </span>
                          </td>
                          <td className="p-2">{renderTableRow(item.status)}</td>
                          <td className="p-2">{item.agents}</td>
                          <td className="p-2">{item.createdAt}</td>
                          <td className="p-2">
                            <div className="flex gap-2">
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                <Eye className="w-4 h-4" />
                              </Button>
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                                <Trash2 className="w-4 h-4" />
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
        </TabsContent>

        {/* Tab: Communication */}
        <TabsContent value="communication" className="space-y-6">
          {/* Estadísticas */}
          <div className="grid grid-cols-3 gap-4">
            <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.total", "Total")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {commTotal}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {commActive}
                </h2>
              </CardBody>
            </Card>
            <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
              <CardBody className="p-4">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {commPending}
                </h2>
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
                    placeholder={t("common.search", "Buscar...")}
                    value={commSearchTerm}
                    onChange={(e) => setCommSearchTerm(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
                  />
                </div>
              </div>
            </CardBody>
          </Card>

          {/* Tabla */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader className="pb-3">
              <CardTitle className="text-base font-medium">{t("agents.communication.list", "Listado de Comunicaciones")}</CardTitle>
            </CardHeader>
            <CardBody className="pt-0">
              {filteredCommunications.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="border-b border-border">
                        <th className="text-left p-2">{t("common.id", "ID")}</th>
                        <th className="text-left p-2">{t("agents.communication.communicationType", "Tipo")}</th>
                        <th className="text-left p-2">{t("common.status", "Estado")}</th>
                        <th className="text-left p-2">{t("agents.communication.messages", "Mensajes")}</th>
                        <th className="text-left p-2">{t("agents.communication.lastMessage", "Último Mensaje")}</th>
                        <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                      </tr>
                    </thead>
                    <tbody>
                      {filteredCommunications.map((item) => (
                        <tr key={item.id} className="border-b border-border hover:bg-card/50 transition-colors">
                          <td className="p-2">{item.id}</td>
                          <td className="p-2">
                            <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                              {item.communicationType}
                            </span>
                          </td>
                          <td className="p-2">{renderTableRow(item.status)}</td>
                          <td className="p-2">{item.messages}</td>
                          <td className="p-2">{item.lastMessage}</td>
                          <td className="p-2">
                            <div className="flex gap-2">
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                <Eye className="w-4 h-4" />
                              </Button>
                              <Button variant="ghost" size="sm" className="h-8 w-8 p-0 text-destructive">
                                <Trash2 className="w-4 h-4" />
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
        </TabsContent>
      </Tabs>
    </div>
  );
}
