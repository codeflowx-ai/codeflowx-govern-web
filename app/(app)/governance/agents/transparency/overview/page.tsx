"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Eye, Plus, Search, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";

interface Transparency {
  id: number;
  agentUuid?: string;
  agentName?: string;
  transparencyType: string;
  transparencyScore?: number;
  explainabilityScore?: number;
  interpretabilityScore?: number;
  auditabilityScore?: number;
  algorithmDisclosure?: string;
  dataDisclosure?: string;
  decisionExplainability?: string;
  explanationMethod?: string;
  auditDate?: string;
  auditor?: string;
  certificationStatus?: string;
  complianceLevel?: string;
  status?: string;
}

export default function TransparencyOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [transparencies, setTransparencies] = useState<Transparency[]>([]);
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

      // Cargar transparencias
      let transparenciesList: Transparency[] = [];
      try {
        const response = await fetch("/api/governance/agents/transparency");
        if (response.ok) {
          const data = await response.json();
          transparenciesList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar transparencias desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || transparenciesList.length === 0) {
        transparenciesList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            transparencyType: "DECISION",
            transparencyScore: 88,
            explainabilityScore: 85,
            interpretabilityScore: 82,
            auditabilityScore: 90,
            algorithmDisclosure: "FULL",
            dataDisclosure: "PARTIAL",
            decisionExplainability: "HIGH",
            explanationMethod: "SHAP",
            auditDate: "2024-01-15T10:00:00",
            auditor: "auditor@codeflowx.com",
            certificationStatus: "CERTIFIED",
            complianceLevel: "HIGH",
            status: "ACTIVE",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            transparencyType: "PROCESS",
            transparencyScore: 75,
            explainabilityScore: 70,
            interpretabilityScore: 68,
            auditabilityScore: 80,
            algorithmDisclosure: "PARTIAL",
            dataDisclosure: "MINIMAL",
            decisionExplainability: "MEDIUM",
            explanationMethod: "LIME",
            auditDate: "2024-01-16T14:30:00",
            auditor: "compliance@codeflowx.com",
            certificationStatus: "PENDING",
            complianceLevel: "MEDIUM",
            status: "PENDING",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            transparencyType: "ALGORITHM",
            transparencyScore: 92,
            explainabilityScore: 90,
            interpretabilityScore: 88,
            auditabilityScore: 95,
            algorithmDisclosure: "FULL",
            dataDisclosure: "FULL",
            decisionExplainability: "HIGH",
            explanationMethod: "Integrated Gradients",
            auditDate: "2024-01-20T09:15:00",
            auditor: "auditor@codeflowx.com",
            certificationStatus: "CERTIFIED",
            complianceLevel: "HIGH",
            status: "ACTIVE",
          },
        ];
      }

      // Hacer match de agentes con transparencias
      const transparenciesWithAgents = transparenciesList.map((transparency: Transparency) => {
        if (transparency.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === transparency.agentUuid || a.id.toString() === transparency.agentUuid);
          if (agent) {
            return { ...transparency, agentName: agent.name };
          }
        }
        return transparency;
      });

      setTransparencies(transparenciesWithAgents);
      setTotalItems(transparenciesWithAgents.length);
      setActiveItems(transparenciesWithAgents.filter((t: Transparency) => t.status === "ACTIVE" || t.certificationStatus === "CERTIFIED").length);
      setPendingItems(transparenciesWithAgents.filter((t: Transparency) => t.status === "PENDING" || t.certificationStatus === "PENDING").length);
    } catch (error) {
      console.error("Error loading transparencies:", error);
      // Mock completo en caso de error
      const mockTransparencies = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          transparencyType: "DECISION",
          transparencyScore: 88,
          status: "ACTIVE",
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          transparencyType: "PROCESS",
          transparencyScore: 75,
          status: "PENDING",
        },
      ];
      setTransparencies(mockTransparencies);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };

  const filteredTransparencies = transparencies.filter((item) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (item.transparencyType && item.transparencyType.toLowerCase().includes(searchLower)) ||
      (item.agentName && item.agentName.toLowerCase().includes(searchLower)) ||
      (item.auditor && item.auditor.toLowerCase().includes(searchLower)) ||
      (item.certificationStatus && item.certificationStatus.toLowerCase().includes(searchLower))
    );
  });

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Eye className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
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
            <Eye className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("agents.transparency.title", "Transparencia")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.transparency.subtitle", "Gestión de transparencia")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            router.push("/governance/agents/transparency/create");
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.transparency.register", "Registrar Transparencia")}
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
          <CardTitle className="text-base font-medium">{t("agents.transparency.list", "Listado de Transparencias")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredTransparencies.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.transparency.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.transparency.transparencyType", "Tipo de Transparencia")}</th>
                    <th className="text-left p-2">{t("agents.transparency.transparencyScore", "Puntuación de Transparencia")}</th>
                    <th className="text-left p-2">{t("agents.transparency.explainabilityScore", "Explicabilidad")}</th>
                    <th className="text-left p-2">{t("agents.transparency.certificationStatus", "Estado de Certificación")}</th>
                    <th className="text-left p-2">{t("agents.transparency.auditDate", "Fecha de Auditoría")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredTransparencies.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{item.id}</td>
                      <td className="p-2">{item.agentName || item.agentUuid || "-"}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {item.transparencyType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span className="font-bold">
                          {item.transparencyScore !== undefined && item.transparencyScore !== null
                            ? `${item.transparencyScore}%`
                            : "-"}
                        </span>
                      </td>
                      <td className="p-2">
                        {item.explainabilityScore !== undefined && item.explainabilityScore !== null
                          ? `${item.explainabilityScore}%`
                          : "-"}
                      </td>
                      <td className="p-2">
                        {item.certificationStatus && (
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              item.certificationStatus === "CERTIFIED"
                                ? "bg-green-500/20 text-green-500"
                                : item.certificationStatus === "PENDING"
                                ? "bg-yellow-500/20 text-yellow-500"
                                : "bg-gray-500/20 text-gray-500"
                            }`}
                          >
                            {item.certificationStatus}
                          </span>
                        )}
                      </td>
                      <td className="p-2">
                        {item.auditDate
                          ? new Date(item.auditDate).toLocaleDateString("es-ES", {
                              year: "numeric",
                              month: "2-digit",
                              day: "2-digit",
                            })
                          : "-"}
                      </td>
                      <td className="p-2">
                        <div className="flex gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-8 w-8 p-0"
                            onClick={() => {
                              router.push(`/governance/agents/transparency/${item.id}`);
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

    </div>
  );
}
