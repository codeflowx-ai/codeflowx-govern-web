"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { Shield, Plus, Search, Trash2, Eye } from "lucide-react";
import { useRouter } from "next/navigation";

interface BiasDetection {
  id: number;
  agentUuid?: string;
  agentName?: string;
  biasType: string;
  biasCategory?: string;
  severity: string;
  confidenceScore?: number;
  biasScore?: number;
  description?: string;
  detectionMethod?: string;
  detectionAlgorithm?: string;
  status: string;
  priority?: string;
  detectedAt: string;
}

export default function BiasDetectionOverviewPage() {
  const router = useRouter();
  const { t, mounted } = useTranslation();
  const [detections, setDetections] = useState<BiasDetection[]>([]);
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

      // Cargar detecciones de sesgos
      let detectionsList: BiasDetection[] = [];
      try {
        const response = await fetch("/api/governance/agents/bias-detection");
        if (response.ok) {
          const data = await response.json();
          detectionsList = data.items || [];
        }
      } catch (apiError) {
        console.warn("Error al cargar detecciones desde API, usando datos mock:", apiError);
      }

      // Usar datos mock (temporalmente para desarrollo)
      if (true || detectionsList.length === 0) {
        detectionsList = [
          {
            id: 1,
            agentUuid: "550e8400-e29b-41d4-a716-446655440000",
            agentName: "Credit Scoring Agent",
            biasType: "GENDER",
            biasCategory: "Demographic",
            severity: "HIGH",
            confidenceScore: 0.92,
            biasScore: 0.75,
            description: "Se detectó un sesgo de género en las decisiones crediticias, favoreciendo a un género sobre otro",
            detectionMethod: "STATISTICAL",
            detectionAlgorithm: "Disparate Impact Analysis",
            status: "ACTIVE",
            priority: "HIGH",
            detectedAt: "2024-01-15T10:00:00",
          },
          {
            id: 2,
            agentUuid: "550e8400-e29b-41d4-a716-446655440001",
            agentName: "Fraud Detection Agent",
            biasType: "RACIAL",
            biasCategory: "Demographic",
            severity: "MEDIUM",
            confidenceScore: 0.78,
            biasScore: 0.55,
            description: "Sesgo racial detectado en la detección de fraude, con mayor tasa de falsos positivos en ciertos grupos",
            detectionMethod: "MACHINE_LEARNING",
            detectionAlgorithm: "Fairness Metrics",
            status: "PENDING",
            priority: "MEDIUM",
            detectedAt: "2024-01-16T14:30:00",
          },
          {
            id: 3,
            agentUuid: "550e8400-e29b-41d4-a716-446655440002",
            agentName: "Customer Service Agent",
            biasType: "AGE",
            biasCategory: "Demographic",
            severity: "LOW",
            confidenceScore: 0.65,
            biasScore: 0.35,
            description: "Ligero sesgo de edad en las respuestas del agente de servicio al cliente",
            detectionMethod: "MANUAL",
            detectionAlgorithm: "Human Review",
            status: "RESOLVED",
            priority: "LOW",
            detectedAt: "2024-01-20T09:15:00",
          },
        ];
      }

      // Hacer match de agentes con detecciones
      const detectionsWithAgents = detectionsList.map((detection: BiasDetection) => {
        if (detection.agentUuid) {
          const agent = agentsList.find((a) => a.uuid === detection.agentUuid || a.id.toString() === detection.agentUuid);
          if (agent) {
            return { ...detection, agentName: agent.name };
          }
        }
        return detection;
      });

      setDetections(detectionsWithAgents);
      setTotalItems(detectionsWithAgents.length);
      setActiveItems(detectionsWithAgents.filter((d: BiasDetection) => d.status === "ACTIVE").length);
      setPendingItems(detectionsWithAgents.filter((d: BiasDetection) => d.status === "PENDING").length);
    } catch (error) {
      console.error("Error loading detections:", error);
      // Mock completo en caso de error
      const mockDetections = [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          biasType: "GENDER",
          severity: "HIGH",
          status: "ACTIVE",
          description: "Gender bias detected",
          detectedAt: "2024-01-15T10:00:00",
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          biasType: "RACIAL",
          severity: "MEDIUM",
          status: "PENDING",
          description: "Racial bias detected",
          detectedAt: "2024-01-16T14:30:00",
        },
      ];
      setDetections(mockDetections);
      setTotalItems(2);
      setActiveItems(1);
      setPendingItems(1);
    } finally {
      setLoading(false);
    }
  };
  const filteredDetections = detections.filter((detection) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      (detection.biasType && detection.biasType.toLowerCase().includes(searchLower)) ||
      (detection.agentName && detection.agentName.toLowerCase().includes(searchLower)) ||
      (detection.description && detection.description.toLowerCase().includes(searchLower)) ||
      (detection.biasCategory && detection.biasCategory.toLowerCase().includes(searchLower))
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
              {t("agents.biasDetection.title", "Detección de Sesgo")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("agents.biasDetection.subtitle", "Gestión de detección de sesgos")}
          </p>
        </div>
        <Button
          variant="outline"
          size="sm"
          className="flex items-center gap-2"
          onClick={() => {
            router.push("/governance/agents/bias-detection/create");
          }}
        >
          <Plus className="h-4 w-4" />
          {t("agents.biasDetection.register", "Registrar Detección")}
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
          <CardTitle className="text-base font-medium">{t("agents.biasDetection.list", "Listado de Detecciones")}</CardTitle>
        </CardHeader>
        <CardBody className="pt-0">
          {filteredDetections.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("common.noResults", "No se encontraron resultados")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-2">{t("common.id", "ID")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.agent", "Agente")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.biasType", "Tipo de Sesgo")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.severity", "Severidad")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.biasScore", "Puntuación de Sesgo")}</th>
                    <th className="text-left p-2">{t("common.status", "Estado")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.priority", "Prioridad")}</th>
                    <th className="text-left p-2">{t("agents.biasDetection.detectedAt", "Detectado En")}</th>
                    <th className="text-left p-2">{t("common.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDetections.map((detection) => (
                    <tr
                      key={detection.id}
                      className="border-b border-border hover:bg-card/50 transition-colors"
                    >
                      <td className="p-2">{detection.id}</td>
                      <td className="p-2">{detection.agentName || detection.agentUuid || "-"}</td>
                      <td className="p-2">
                        <span className="px-2 py-1 rounded text-xs bg-primary/20 text-primary">
                          {detection.biasType}
                        </span>
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            detection.severity === "HIGH" || detection.severity === "CRITICAL"
                              ? "bg-red-500/20 text-red-500"
                              : detection.severity === "MEDIUM"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-green-500/20 text-green-500"
                          }`}
                        >
                          {detection.severity}
                        </span>
                      </td>
                      <td className="p-2">
                        {detection.biasScore !== undefined && detection.biasScore !== null
                          ? `${(detection.biasScore * 100).toFixed(1)}%`
                          : "-"}
                      </td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded text-xs ${
                            detection.status === "ACTIVE" || detection.status === "RESOLVED"
                              ? "bg-green-500/20 text-green-500"
                              : detection.status === "PENDING"
                              ? "bg-yellow-500/20 text-yellow-500"
                              : "bg-gray-500/20 text-gray-500"
                          }`}
                        >
                          {detection.status}
                        </span>
                      </td>
                      <td className="p-2">
                        {detection.priority && (
                          <span
                            className={`px-2 py-1 rounded text-xs ${
                              detection.priority === "HIGH" || detection.priority === "CRITICAL"
                                ? "bg-red-500/20 text-red-500"
                                : detection.priority === "MEDIUM"
                                ? "bg-yellow-500/20 text-yellow-500"
                                : "bg-green-500/20 text-green-500"
                            }`}
                          >
                            {detection.priority}
                          </span>
                        )}
                      </td>
                      <td className="p-2">
                        {detection.detectedAt
                          ? new Date(detection.detectedAt).toLocaleDateString("es-ES", {
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
                              router.push(`/governance/agents/bias-detection/${detection.id}`);
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
