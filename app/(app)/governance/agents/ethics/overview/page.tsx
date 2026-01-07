"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Eye, Plus, Scale, Search, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface Ethics {
  id: number;
  agentUuid?: string;
  agentName?: string;
  assessmentType: string;
  assessmentStatus: string;
  overallScore?: number;
  riskLevel?: string;
  assessmentDate?: string;
}

export default function EthicsOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [ethics, setEthics] = useState<Ethics[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [totalItems, setTotalItems] = useState(0);
  const [activeItems, setActiveItems] = useState(0);
  const [pendingItems, setPendingItems] = useState(0);
  const [selectedEthics, setSelectedEthics] = useState<Ethics | null>(null);

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

      // Cargar evaluaciones éticas
      let ethicsList: Ethics[] = [];
      try {
        const response = await fetch("/api/agents/ethics/list");
        if (response.ok) {
          const data = await response.json();
          ethicsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar evaluaciones éticas desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      // TODO: Remover cuando la API esté completamente implementada
      if (true || ethicsList.length === 0) {
        ethicsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            assessmentType: "FAIRNESS",
            assessmentStatus: "PASSED",
            overallScore: 92,
            riskLevel: "LOW",
            assessmentDate: "2024-01-15T10:00:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            assessmentType: "TRANSPARENCY",
            assessmentStatus: "PENDING",
            overallScore: 85,
            riskLevel: "MEDIUM",
            assessmentDate: "2024-01-16T14:30:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            assessmentType: "BIAS",
            assessmentStatus: "FAILED",
            overallScore: 65,
            riskLevel: "HIGH",
            assessmentDate: "2024-01-20T09:15:00",
          },
          {
            id: 4,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            assessmentType: "ACCOUNTABILITY",
            assessmentStatus: "PASSED",
            overallScore: 88,
            riskLevel: "LOW",
            assessmentDate: "2024-01-18T11:00:00",
          },
        ];
      }

      // Hacer match de agentes con evaluaciones éticas
      const ethicsWithAgents = ethicsList.map((ethicsItem: Ethics) => {
        if (ethicsItem.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === ethicsItem.agentUuid || a.id.toString() === ethicsItem.agentUuid);
          if (agent) {
            return { ...ethicsItem, agentName: agent.name };
          }
        }
        return ethicsItem;
      });

      setEthics(ethicsWithAgents);
      setTotalItems(ethicsWithAgents.length);
      setActiveItems(ethicsWithAgents.filter((e: Ethics) => e.assessmentStatus === "PASSED").length);
      setPendingItems(ethicsWithAgents.filter((e: Ethics) => e.assessmentStatus === "PENDING" || e.assessmentStatus === "DRAFT").length);
    } catch (error) {
      console.error("Error loading ethics:", error);
      // Mock completo en caso de error
      const mockEthics = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          assessmentType: "FAIRNESS",
          assessmentStatus: "PASSED",
          overallScore: 92,
          riskLevel: "LOW",
          assessmentDate: "2024-01-15T10:00:00",
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          assessmentType: "TRANSPARENCY",
          assessmentStatus: "PENDING",
          overallScore: 85,
          riskLevel: "MEDIUM",
          assessmentDate: "2024-01-16T14:30:00",
        },
        {
          id: 3,
          agentUuid: "550e8400-e29b-41d4-a716-446655440002",
          agentName: "Customer Service Agent",
          assessmentType: "BIAS",
          assessmentStatus: "FAILED",
          overallScore: 65,
          riskLevel: "HIGH",
          assessmentDate: "2024-01-20T09:15:00",
        },
        {
          id: 4,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          assessmentType: "ACCOUNTABILITY",
          assessmentStatus: "PASSED",
          overallScore: 88,
          riskLevel: "LOW",
          assessmentDate: "2024-01-18T11:00:00",
        },
      ];
      setEthics(mockEthics);
      setTotalItems(4);
      setActiveItems(2);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredEthics = ethics.filter((item) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (item.assessmentType && item.assessmentType.toLowerCase().includes(searchLower)) ||
      (item.agentName && item.agentName.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Scale className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <Scale className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.ethics.title", "Ética")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.ethics.subtitle", "Gestión de evaluaciones éticas de agentes")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            router.push("/governance/agents/ethics/create");
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.ethics.register", "Registrar Evaluación Ética")}
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
          <CardTitle className="text-base font-medium">{t("agents.ethics.list", "Listado de Ética")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredEthics.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.ethics.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.ethics.assessmentType", "Tipo de Evaluación")}</th>
                    <th className="text-left p-2">{t("agents.ethics.assessmentStatus", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.ethics.overallScore", "Puntuación General")}</th>
                    <th className="text-left p-2">{t("agents.ethics.riskLevel", "Nivel de Riesgo")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredEthics.map((item) => (
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
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {item.assessmentType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            item.assessmentStatus === "PASSED"
                              ? "bg-green-500/20 text-green-500"
                              : item.assessmentStatus === "PENDING" || item.assessmentStatus === "DRAFT"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {item.assessmentStatus}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-bold">{item.overallScore || "-"}%</span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            item.riskLevel === "LOW"
                              ? "bg-green-500/20 text-green-500"
                              : item.riskLevel === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : item.riskLevel === "HIGH"
                              ? "bg-orange-500/20 text-orange-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {item.riskLevel || "-"}
                        </span>
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => {
                              router.push(`/governance/agents/ethics/${item.id}`);
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
        isOpen={selectedEthics !== null}
        onClose={() => setSelectedEthics(null)}
        title={selectedEthics ? `${t("agents.ethics.detail.title", "Detalles de Evaluación Ética")} #${selectedEthics.id}` : ""}
        maxWidth="max-w-2xl"
      >
        {selectedEthics && (
          <div className="space-y-4">
            <Card className="border-2">
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.ethics.agent", "Agente")}
                    </label>
                    <div className="flex flex-col">
                      <p className="text-base font-semibold">{selectedEthics.agentName || ""}</p>
                      {selectedEthics.agentUuid && (
                        <p className="text-xs text-muted-foreground">{selectedEthics.agentUuid}</p>
                      )}
                    </div>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.ethics.assessmentType", "Tipo de Evaluación")}
                    </label>
                    <p className="text-base font-semibold">{selectedEthics.assessmentType}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.ethics.assessmentStatus", "Estado")}
                    </label>
                    <p className="text-base">
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          selectedEthics.assessmentStatus === "PASSED"
                            ? "bg-green-500/20 text-green-500"
                            : selectedEthics.assessmentStatus === "PENDING" || selectedEthics.assessmentStatus === "DRAFT"
                            ? "bg-yellow-500/20 text-yellow-500"
                            : "bg-red-500/20 text-red-500"
                        }`}
                      >
                        {selectedEthics.assessmentStatus}
                      </span>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("agents.ethics.overallScore", "Puntuación General")}
                    </label>
                    <p className="text-base font-bold">{selectedEthics.overallScore || "-"}%</p>
                  </div>
                  {selectedEthics.riskLevel && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.ethics.riskLevel", "Nivel de Riesgo")}
                      </label>
                      <p className="text-base">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            selectedEthics.riskLevel === "LOW"
                              ? "bg-green-500/20 text-green-500"
                              : selectedEthics.riskLevel === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : selectedEthics.riskLevel === "HIGH"
                              ? "bg-orange-500/20 text-orange-500"
                              : "bg-red-500/20 text-red-500"
                          }`}
                        >
                          {selectedEthics.riskLevel}
                        </span>
                      </p>
                    </div>
                  )}
                  {selectedEthics.assessmentDate && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("agents.ethics.assessmentDate", "Fecha de Evaluación")}
                      </label>
                      <p className="text-base">
                        {new Date(selectedEthics.assessmentDate).toLocaleString("es-ES", {
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
