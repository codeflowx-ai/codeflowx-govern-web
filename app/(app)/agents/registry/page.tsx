"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Edit, Eye, Package, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";

interface Agent {
  id: number;
  uuid?: string;
  name: string;
  description: string;
  status: string;
  version: string;
  domain: string;
  createdAt: string;
  lastModified: string;
}

export default function AgentsRegistryPage() {
  const { t, mounted } = useTranslation();
  const [agents, setAgents] = useState<Agent[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/agents/registry/list");
      const data = await response.json();
      setAgents(data.items || []);
      setTotalItems(data.total || 0);
      setActiveItems(data.active || 0);
      setPendingItems(data.pending || 0);
    } catch (error) {
      console.error("Error loading agents:", error);
      // Mock data para desarrollo
      setAgents([
        {
          id: 1,
          uuid: "a1b2c3d4-e5f6-4789-a012-345678901234",
          name: "Credit Scoring Agent",
          description: "Agent for credit risk assessment",
          status: "ACTIVE",
          version: "v2.1.0",
          domain: "Finance",
          createdAt: "2024-01-10",
          lastModified: "2024-01-15",
        },
        {
          id: 2,
          uuid: "b2c3d4e5-f6a7-4890-b123-456789012345",
          name: "Document OCR Agent",
          description: "Optical character recognition agent",
          status: "ACTIVE",
          version: "v1.9.5",
          domain: "Document Processing",
          createdAt: "2024-01-08",
          lastModified: "2024-01-14",
        },
        {
          id: 3,
          uuid: "c3d4e5f6-a7b8-4901-c234-567890123456",
          name: "Fraud Detection Agent",
          description: "Real-time fraud detection agent",
          status: "PENDING",
          version: "v2.0.3",
          domain: "Security",
          createdAt: "2024-01-12",
          lastModified: "2024-01-16",
        },
        {
          id: 4,
          uuid: "d4e5f6a7-b8c9-4012-d345-678901234567",
          name: "Invoice Processing Agent",
          description: "Automated invoice processing and validation",
          status: "ACTIVE",
          version: "v1.8.2",
          domain: "Finance",
          createdAt: "2024-01-09",
          lastModified: "2024-01-13",
        },
        {
          id: 5,
          uuid: "e5f6a7b8-c9d0-4123-e456-789012345678",
          name: "Customer Onboarding Agent",
          description: "Streamlined customer onboarding process",
          status: "ACTIVE",
          version: "v2.2.1",
          domain: "Customer Service",
          createdAt: "2024-01-11",
          lastModified: "2024-01-17",
        },
        {
          id: 6,
          uuid: "f6a7b8c9-d0e1-4234-f567-890123456789",
          name: "Risk Assessment Agent",
          description: "Comprehensive risk analysis and assessment",
          status: "DEPLOYED",
          version: "v1.7.0",
          domain: "Finance",
          createdAt: "2024-01-07",
          lastModified: "2024-01-12",
        },
      ]);
      setTotalItems(6);
      setActiveItems(4);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredAgents = agents.filter(
    (agent) =>
      agent.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      agent.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      agent.domain.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (agent.uuid && agent.uuid.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Package className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Package className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.registry.title", "Registro de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.registry.subtitle", "Gestión y registro de agentes de IA")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            window.location.href = "/agents/registry/create";
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.registry.register", "Registrar Agente")}
        </Button>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-3 gap-4">
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
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.active", "Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeItems}
                </h2>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("common.pending", "Pendientes")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  {pendingItems}
                </h2>
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
          <CardTitle className="text-base font-medium">
            {t("agents.registry.list", "Listado de Agentes Registrados")}
          </CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredAgents.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("agents.registry.uuid", "UUID")}</th>
                    <th className="text-left p-2">{t("agents.registry.name", "Nombre")}</th>
                    <th className="text-left p-2">{t("agents.registry.description", "Descripción")}</th>
                    <th className="text-left p-2">{t("agents.registry.domain", "Dominio")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.registry.version", "Versión")}</th>
                    <th className="text-left p-2">{t("agents.registry.createdAt", "Creado")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredAgents.map((agent) => (
                    <tr
                      key={agent.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">
                        <span className="text-xs font-mono text-muted-foreground">
                          {agent.uuid || "N/A"}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-medium">{agent.name}</span>
                      </td>
                      <td className="p-2">
                        <span className="text-sm text-muted-foreground">{agent.description}</span>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {agent.domain}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            agent.status === "ACTIVE"
                              ? "bg-green-500/20 text-green-500"
                              : "bg-yellow-500/20 text-yellow-500"
                          }`}
                        >
                          {agent.status}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="text-sm">{agent.version}</span>
                      </td>
                      <td className="p-2">
                        <span className="text-sm text-muted-foreground">{agent.createdAt}</span>
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            title={t("common.view", "Ver")}
                            onClick={() => {
                              window.location.href = `/agents/registry/${agent.id}`;
                            }}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            title={t("common.edit", "Editar")}
                            onClick={() => {
                              window.location.href = `/agents/registry/${agent.id}/edit`;
                            }}
                          >
                            <Edit className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0 text-destructive"
                            title={t("common.delete", "Eliminar")}
                            onClick={() => {
                              if (confirm(t("common.confirmDelete", "¿Está seguro de eliminar este agente?"))) {
                                // TODO: Implementar eliminación
                                console.log("Eliminar agente:", agent.id);
                              }
                            }}
                          >
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
    </div>
  );
}
