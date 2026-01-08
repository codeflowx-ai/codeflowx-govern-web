"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Eye, Plus, Search, Shield, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface Compliance {
  id: number;
  agentUuid?: string;
  agentName?: string;
  complianceType: string;
  complianceStatus: string;
  complianceScore?: number;
  riskLevel?: string;
  lastAssessmentAt?: string;
  nextAssessmentAt?: string;
}

export default function AgentComplianceOverviewPage() {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [compliances, setCompliances] = useState<Compliance[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);
  const [selectedCompliance, setSelectedCompliance] = useState<Compliance | null>(null);

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

      // Cargar cumplimientos
      let compliancesList: Compliance[] = [];
      try {
        const response = await fetch("/api/agents/compliance/list");
        if (response.ok) {
          const data = await response.json();
          compliancesList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar cumplimientos desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      // TODO: Remover cuando la API esté completamente implementada
      if (true || compliancesList.length === 0) {
        compliancesList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            complianceType: "REGULATORY",
            complianceStatus: "COMPLIANT",
            complianceScore: 95,
            riskLevel: "LOW",
            lastAssessmentAt: "2024-01-15T10:00:00",
            nextAssessmentAt: "2024-02-15T10:00:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            complianceType: "DATA_PROTECTION",
            complianceStatus: "PENDING",
            complianceScore: 78,
            riskLevel: "MEDIUM",
            lastAssessmentAt: "2024-01-10T14:30:00",
            nextAssessmentAt: "2024-02-10T14:30:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            complianceType: "INDUSTRY_STANDARD",
            complianceStatus: "UNDER_REVIEW",
            complianceScore: 85,
            riskLevel: "LOW",
            lastAssessmentAt: "2024-01-20T09:15:00",
            nextAssessmentAt: "2024-02-20T09:15:00",
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            complianceType: "INTERNAL_POLICY",
            complianceStatus: "NON_COMPLIANT",
            complianceScore: 45,
            riskLevel: "HIGH",
            lastAssessmentAt: "2024-01-18T11:00:00",
            nextAssessmentAt: "2024-02-18T11:00:00",
          },
        ];
      }

      // Hacer match de agentes con cumplimientos
      const compliancesWithAgents = compliancesList.map((compliance: Compliance) => {
        if (compliance.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === compliance.agentUuid || a.id.toString() === compliance.agentUuid);
          if (agent) {
            return { ...compliance, agentName: agent.name };
          }
        }
        return compliance;
      });

      setCompliances(compliancesWithAgents);
      setTotalItems(compliancesWithAgents.length);
      setActiveItems(compliancesWithAgents.filter((c: Compliance) => c.complianceStatus === "COMPLIANT").length);
      setPendingItems(compliancesWithAgents.filter((c: Compliance) => c.complianceStatus === "PENDING" || c.complianceStatus === "UNDER_REVIEW").length);
    } catch (error) {
      console.error("Error loading compliances:", error);
      // Mock completo en caso de error
      const mockCompliances = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          complianceType: "REGULATORY",
          complianceStatus: "COMPLIANT",
          complianceScore: 95,
          riskLevel: "LOW",
          lastAssessmentAt: "2024-01-15T10:00:00",
          nextAssessmentAt: "2024-02-15T10:00:00",
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          complianceType: "DATA_PROTECTION",
          complianceStatus: "PENDING",
          complianceScore: 78,
          riskLevel: "MEDIUM",
          lastAssessmentAt: "2024-01-10T14:30:00",
          nextAssessmentAt: "2024-02-10T14:30:00",
        },
        {
          id: 3,
          agentUuid: "550e8400-e29b-41d4-a716-446655440002",
          agentName: "Customer Service Agent",
          complianceType: "INDUSTRY_STANDARD",
          complianceStatus: "UNDER_REVIEW",
          complianceScore: 85,
          riskLevel: "LOW",
          lastAssessmentAt: "2024-01-20T09:15:00",
          nextAssessmentAt: "2024-02-20T09:15:00",
        },
        {
          id: 4,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          complianceType: "INTERNAL_POLICY",
          complianceStatus: "NON_COMPLIANT",
          complianceScore: 45,
          riskLevel: "HIGH",
          lastAssessmentAt: "2024-01-18T11:00:00",
          nextAssessmentAt: "2024-02-18T11:00:00",
        },
      ];
      setCompliances(mockCompliances);
      setTotalItems(4);
      setActiveItems(1);
      setPendingItems(2);
    } finally {
      setLoading(false);
    }
  };

  const filteredCompliances = compliances.filter((compliance) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (compliance.complianceType && compliance.complianceType.toLowerCase().includes(searchLower)) ||
      (compliance.agentName && compliance.agentName.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Shield className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.compliance.title", "Cumplimiento de Agentes")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.compliance.subtitle", "Gestión de cumplimiento normativo y regulatorio de agentes")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            router.push("/governance/agents/compliance/create");
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.compliance.register", "Registrar Cumplimiento")}
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
          <CardTitle className="text-base font-medium">{t("agents.compliance.list", "Listado de Cumplimientos")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredCompliances.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.compliance.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.compliance.complianceType", "Tipo de Cumplimiento")}</th>
                    <th className="text-left p-2">{t("agents.compliance.complianceStatus", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.compliance.complianceScore", "Puntuación")}</th>
                    <th className="text-left p-2">{t("agents.compliance.riskLevel", "Nivel de Riesgo")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredCompliances.map((compliance) => (
                    <tr
                      key={compliance.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{compliance.id}</td>
                      <td className="p-2">
                        <div className="flex flex-col">
                          <span className="font-medium text-sm">{compliance.agentName || ""}</span>
                          {compliance.agentUuid && (
                            <span className="text-xs text-muted-foreground">{compliance.agentUuid.substring(0, 20)}...</span>
                          )}
                        </div>
                      </td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {compliance.complianceType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            compliance.complianceStatus === "COMPLIANT"
                              ? "bg-green-500/20 text-green-500"
                              : compliance.complianceStatus === "PENDING" || compliance.complianceStatus === "UNDER_REVIEW"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {compliance.complianceStatus}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-bold">{compliance.complianceScore || "-"}%</span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            compliance.riskLevel === "LOW"
                              ? "bg-green-500/20 text-green-500"
                              : compliance.riskLevel === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : compliance.riskLevel === "HIGH"
                              ? "bg-orange-500/20 text-orange-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {compliance.riskLevel || "-"}
                        </span>
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => {
                              router.push(`/governance/agents/compliance/${compliance.id}`);
                            }}
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
        isOpen={selectedCompliance !== null}
        onClose={() => setSelectedCompliance(null)}
        title={selectedCompliance ? `${t("agents.compliance.detail.title", "Detalles de Cumplimiento")} #${selectedCompliance.id}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedCompliance && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.compliance.agent", "Agente")}
                    </label>
                    <div className="flex flex-col">
                      <p className="text-base font-semibold">{selectedCompliance.agentName || ""}</p>
                      {selectedCompliance.agentUuid && (
                        <p className="text-xs text-muted-foreground">{selectedCompliance.agentUuid}</p>
                      )}
                    </div>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.compliance.complianceType", "Tipo de Cumplimiento")}
                    </label>
                    <p className="text-base font-semibold">{selectedCompliance.complianceType}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.compliance.complianceStatus", "Estado")}
                    </label>
                    <p className="text-base">
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          selectedCompliance.complianceStatus === "COMPLIANT"
                            ? "bg-green-500/20 text-green-500"
                            : selectedCompliance.complianceStatus === "PENDING" || selectedCompliance.complianceStatus === "UNDER_REVIEW"
                            ? "bg-yellow-500/20 text-yellow-500"
                            : "bg-red-500/20 text-red-500"
                        }`}
                      >
                        {selectedCompliance.complianceStatus}
                      </span>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.compliance.complianceScore", "Puntuación")}
                    </label>
                    <p className="text-base font-bold">{selectedCompliance.complianceScore || "-"}%</p>
                  </div>
                  {selectedCompliance.riskLevel && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.compliance.riskLevel", "Nivel de Riesgo")}
                      </label>
                      <p className="text-base">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            selectedCompliance.riskLevel === "LOW"
                              ? "bg-green-500/20 text-green-500"
                              : selectedCompliance.riskLevel === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : selectedCompliance.riskLevel === "HIGH"
                              ? "bg-orange-500/20 text-orange-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {selectedCompliance.riskLevel}
                        </span>
                      </p>
                    </div>
                  )}
                  {selectedCompliance.lastAssessmentAt && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.compliance.lastAssessmentAt", "Última Evaluación")}
                      </label>
                      <p className="text-base">
                        {new Date(selectedCompliance.lastAssessmentAt).toLocaleString("es-ES", {
                          year: "numeric",
                          month: "2-digit",
                          day: "2-digit",
                          hour: "2-digit",
                          minute: "2-digit",
                        })}
                      </p>
                    </div>
                  )}
                </div>
              </CardBody>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}
