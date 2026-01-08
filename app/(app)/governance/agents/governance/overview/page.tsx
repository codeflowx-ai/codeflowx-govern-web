"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { ShieldCheck, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface Governance {
  id: number;
  agentUuid?: string;
  agentName?: string;
  policyType: string;
  policyName: string;
  status: string;
  enforcementLevel: string;
  priority?: string;
  effectiveFrom: string;
  effectiveUntil?: string;
}

export default function AgentGovernanceOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [governances, setGovernances] = useState<Governance[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);
  const [selectedGovernance, setSelectedGovernance] = useState<Governance | null>(null);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      // Cargar agentes primero
      const agentsResponse = await fetch("/api/governance/agents/registry");
      let agentsList: any[] = [];
      if (agentsResponse.ok) {
        const agentsData = await agentsResponse.json();
        agentsList = agentsData.items || agentsData || [];
      } else {
        agentsList = [
          { id: 1, uuid: "550e8400-e29b-41d4-a716-446655440000", name: "Credit Scoring Agent", status: "ACTIVE" },
          { id: 2, uuid: "550e8400-e29b-41d4-a716-446655440001", name: "Fraud Detection Agent", status: "ACTIVE" },
          { id: 3, uuid: "550e8400-e29b-41d4-a716-446655440002", name: "Customer Service Agent", status: "ACTIVE" },
        ];
      }

      // Cargar políticas de gobierno
      let governancesList: Governance[] = [];
      try {
        const response = await fetch("/api/agents/governance/list");
        if (response.ok) {
          const data = await response.json();
          governancesList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar políticas desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      // TODO: Remover cuando la API esté completamente implementada
      if (true || governancesList.length === 0) {
        governancesList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            policyType: "SECURITY",
            policyName: "Política de Seguridad de Datos Sensibles",
            status: "ACTIVE",
            enforcementLevel: "MANDATORY",
            priority: "HIGH",
            effectiveFrom: "2024-01-15",
            effectiveUntil: "2025-01-15"
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            policyType: "DATA_PRIVACY",
            policyName: "Política de Privacidad de Información Personal",
            status: "ACTIVE",
            enforcementLevel: "MANDATORY",
            priority: "HIGH",
            effectiveFrom: "2024-01-10",
            effectiveUntil: undefined
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            policyType: "ETHICAL",
            policyName: "Política Ética de Decisiones Crediticias",
            status: "DRAFT",
            enforcementLevel: "RECOMMENDED",
            priority: "MEDIUM",
            effectiveFrom: "2024-02-01",
            effectiveUntil: undefined
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            policyType: "OPERATIONAL",
            policyName: "Política Operacional de Tiempos de Respuesta",
            status: "ACTIVE",
            enforcementLevel: "RECOMMENDED",
            priority: "MEDIUM",
            effectiveFrom: "2024-01-20",
            effectiveUntil: undefined
          },
        ];
      }

      // Hacer match de agentes con políticas
      const governancesWithAgents = governancesList.map((governance: Governance) => {
        if (governance.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === governance.agentUuid || a.id.toString() === governance.agentUuid);
          if (agent) {
            return { ...governance, agentName: agent.name };
          }
        }
        return governance;
      });

      setGovernances(governancesWithAgents);
      setTotalItems(governancesWithAgents.length);
      setActiveItems(governancesWithAgents.filter((g: Governance) => g.status === "ACTIVE").length);
      setPendingItems(governancesWithAgents.filter((g: Governance) => g.status === "DRAFT").length);
    } catch (error) {
      console.error("Error loading governances:", error);
      // Mock completo en caso de error
      const mockGovernances = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          policyType: "SECURITY",
          policyName: "Política de Seguridad de Datos Sensibles",
          status: "ACTIVE",
          enforcementLevel: "MANDATORY",
          priority: "HIGH",
          effectiveFrom: "2024-01-15",
          effectiveUntil: "2025-01-15"
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          policyType: "DATA_PRIVACY",
          policyName: "Política de Privacidad de Información Personal",
          status: "ACTIVE",
          enforcementLevel: "MANDATORY",
          priority: "HIGH",
          effectiveFrom: "2024-01-10",
          effectiveUntil: undefined
        },
        {
          id: 3,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          policyType: "ETHICAL",
          policyName: "Política Ética de Decisiones Crediticias",
          status: "DRAFT",
          enforcementLevel: "RECOMMENDED",
          priority: "MEDIUM",
          effectiveFrom: "2024-02-01",
          effectiveUntil: undefined
        },
        {
          id: 4,
          agentUuid: "550e8400-e29b-41d4-a716-446655440002",
          agentName: "Customer Service Agent",
          policyType: "OPERATIONAL",
          policyName: "Política Operacional de Tiempos de Respuesta",
          status: "ACTIVE",
          enforcementLevel: "RECOMMENDED",
          priority: "MEDIUM",
          effectiveFrom: "2024-01-20",
          effectiveUntil: undefined
        },
      ];
      setGovernances(mockGovernances);
      setTotalItems(4);
      setActiveItems(3);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredGovernances = governances.filter((item) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (item.policyName && item.policyName.toLowerCase().includes(searchLower)) ||
      (item.policyType && item.policyType.toLowerCase().includes(searchLower)) ||
      (item.agentName && item.agentName.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <ShieldCheck className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <ShieldCheck className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.governance.title", "Políticas de Gobierno de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.governance.subtitle", "Gestión de políticas de gobierno para agentes")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            router.push("/governance/agents/governance/create");
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.governance.register", "Registrar Política de Gobierno")}
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
          <CardTitle className="text-base font-medium">{t("agents.governance.list", "Listado de Políticas de Gobierno")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredGovernances.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.governance.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.governance.policyName", "Nombre de Política")}</th>
                    <th className="text-left p-2">{t("agents.governance.policyType", "Tipo de Política")}</th>
                    <th className="text-left p-2">{t("agents.governance.enforcementLevel", "Nivel de Cumplimiento")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredGovernances.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">
                        <div className="flex flex-col">
                          <span className="font-medium text-sm">{item.agentName || ""}</span>
                          {item.agentUuid && (
                            <span className="text-xs text-muted-foreground">{item.agentUuid.substring(0, 20)}...</span>
                          )}
                        </div>
                      </td>
                      <td className="p-2">
                        <span className="font-medium text-sm">{item.policyName}</span>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {item.policyType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                          {item.enforcementLevel}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            item.status === "ACTIVE"
                              ? "bg-green-500/20 text-green-500"
                              : item.status === "DRAFT"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-gray-500/20 text-gray-500"
                          }`}
                        >
                          {item.status}
                        </span>
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => setSelectedGovernance(item)}
                            title={t("common.view", "Ver")}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0 text-destructive"
                            title={t("common.delete", "Eliminar")}
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

      {/* Modal de Detalles */}
      <SimpleModal
        isOpen={selectedGovernance !== null}
        onClose={() => setSelectedGovernance(null)}
        title={selectedGovernance ? `${t("agents.governance.detail.title", "Detalles de Política de Gobierno")} #${selectedGovernance.id}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedGovernance && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.governance.agent", "Agente")}
                    </label>
                    <div className="flex flex-col">
                      <p className="text-base font-semibold">{selectedGovernance.agentName || ""}</p>
                      {selectedGovernance.agentUuid && (
                        <p className="text-xs text-muted-foreground">{selectedGovernance.agentUuid}</p>
                      )}
                    </div>
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.governance.policyName", "Nombre de Política")}
                    </label>
                    <p className="text-base font-semibold">{selectedGovernance.policyName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.governance.policyType", "Tipo de Política")}
                    </label>
                    <p className="text-base font-semibold">{selectedGovernance.policyType}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.governance.enforcementLevel", "Nivel de Cumplimiento")}
                    </label>
                    <p className="text-base">
                      <span className="px-2 py-1 rounded text-xs bg-blue-500/20 text-blue-500">
                        {selectedGovernance.enforcementLevel}
                      </span>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("common.status", "Estado")}
                    </label>
                    <p className="text-base">
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          selectedGovernance.status === "ACTIVE"
                            ? "bg-green-500/20 text-green-500"
                            : selectedGovernance.status === "DRAFT"
                            ? "bg-yellow-500/20 text-yellow-500"
                            : "bg-gray-500/20 text-gray-500"
                        }`}
                      >
                        {selectedGovernance.status}
                      </span>
                    </p>
                  </div>
                  {selectedGovernance.priority && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.governance.priority", "Prioridad")}
                      </label>
                      <p className="text-base">{selectedGovernance.priority}</p>
                    </div>
                  )}
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.governance.effectiveFrom", "Vigente Desde")}
                    </label>
                    <p className="text-base">{selectedGovernance.effectiveFrom}</p>
                  </div>
                  {selectedGovernance.effectiveUntil && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.governance.effectiveUntil", "Vigente Hasta")}
                      </label>
                      <p className="text-base">{selectedGovernance.effectiveUntil}</p>
                    </div>
                  )}
                </div>
                <div className="flex justify-end gap-2 pt-4 border-t">
                  <Button
                    variant="outline"
                    onClick={() => {
                      router.push(`/governance/agents/governance/${selectedGovernance.id}?edit=true`);
                    }}
                  >
                    {t("common.edit", "Editar")}
                  </Button>
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
