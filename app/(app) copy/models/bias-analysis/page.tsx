"use client";
import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { SimpleModal } from "@/components/ui/SimpleModal";
import { AlertTriangle, Eye, Plus, Search, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";

interface BiasAnalysis {
  id: number;
  modelId: number;
  modelName: string;
  modelType: string;
  biasScore: number; // 0-100, donde 100 = sin bias
  detectedBias: boolean;
  biasLevel: "NONE" | "LOW" | "MEDIUM" | "HIGH";
  affectedGroups: string[]; // Grupos afectados por el sesgo (ej: "Género", "Edad", "Raza")
  analysisDate: string;
  analyst: string;
  analysisMethod: string; // Método de análisis utilizado
  recommendations?: string; // Recomendaciones para mitigar el sesgo
  status: "PENDING" | "REVIEWED" | "RESOLVED";
}

export default function BiasAnalysisPage() {
  const { t } = useTranslation();
  const [analyses, setAnalyses] = useState<BiasAnalysis[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [modelTypeFilter, setModelTypeFilter] = useState<string>("ALL");
  const [biasLevelFilter, setBiasLevelFilter] = useState<string>("ALL");
  const [selectedAnalysis, setSelectedAnalysis] = useState<BiasAnalysis | null>(null);
  const [stats, setStats] = useState({
    total: 0,
    withBias: 0,
    withoutBias: 0,
    averageScore: 0,
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const searchParams = new URLSearchParams();
      if (searchQuery) searchParams.append("search", searchQuery);
      if (modelTypeFilter !== "ALL") searchParams.append("type", modelTypeFilter);
      if (biasLevelFilter !== "ALL") searchParams.append("biasLevel", biasLevelFilter);

      const response = await fetch(
        `/api/v1/bff/compliance/models/bias-analysis${searchParams.toString() ? `?${searchParams.toString()}` : ""}`
      );

      if (response.ok) {
        const data: BiasAnalysis[] = await response.json();
        setAnalyses(data);

        // Calcular estadísticas
        const total = data.length;
        const withBias = data.filter((a) => a.detectedBias).length;
        const withoutBias = total - withBias;
        const averageScore = data.reduce((sum, a) => sum + a.biasScore, 0) / total;

        setStats({
          total,
          withBias,
          withoutBias,
          averageScore: Math.round(averageScore * 10) / 10,
        });
        return;
      }

      // Fallback a mock data si la API falla
      const mockData: BiasAnalysis[] = [
        {
          id: 1,
          modelId: 1,
          modelName: "GPT-4 Fine-tuned Classification",
          modelType: "TEXT",
          biasScore: 45,
          detectedBias: true,
          biasLevel: "HIGH",
          affectedGroups: ["Género", "Edad"],
          analysisDate: "2024-01-15T10:30:00",
          analyst: "analyst@example.com",
          analysisMethod: "Statistical Parity",
          recommendations: "Ajustar dataset de entrenamiento para incluir más representación balanceada",
          status: "REVIEWED",
        },
        {
          id: 2,
          modelId: 2,
          modelName: "Claude Vision OCR",
          modelType: "MULTIMODAL",
          biasScore: 85,
          detectedBias: false,
          biasLevel: "NONE",
          affectedGroups: [],
          analysisDate: "2024-01-14T14:20:00",
          analyst: "analyst@example.com",
          analysisMethod: "Equalized Odds",
          status: "RESOLVED",
        },
        {
          id: 3,
          modelId: 3,
          modelName: "Resume Screening Model",
          modelType: "TEXT",
          biasScore: 60,
          detectedBias: true,
          biasLevel: "MEDIUM",
          affectedGroups: ["Género", "Etnia"],
          analysisDate: "2024-01-13T09:15:00",
          analyst: "analyst@example.com",
          analysisMethod: "Demographic Parity",
          recommendations: "Implementar técnicas de debiasing y re-entrenar modelo",
          status: "PENDING",
        },
        {
          id: 4,
          modelId: 4,
          modelName: "Credit Scoring Model",
          modelType: "TEXT",
          biasScore: 35,
          detectedBias: true,
          biasLevel: "HIGH",
          affectedGroups: ["Etnia", "Ubicación geográfica"],
          analysisDate: "2024-01-12T16:45:00",
          analyst: "analyst@example.com",
          analysisMethod: "Calibrated Fairness",
          recommendations: "Revisar variables de entrada y eliminar proxies de características protegidas",
          status: "REVIEWED",
        },
        {
          id: 5,
          modelId: 5,
          modelName: "Image Classification Model",
          modelType: "IMAGE",
          biasScore: 92,
          detectedBias: false,
          biasLevel: "NONE",
          affectedGroups: [],
          analysisDate: "2024-01-11T11:30:00",
          analyst: "analyst@example.com",
          analysisMethod: "Fairness Through Awareness",
          status: "RESOLVED",
        },
      ];

      setAnalyses(mockData);

      // Calcular estadísticas
      const total = mockData.length;
      const withBias = mockData.filter((a) => a.detectedBias).length;
      const withoutBias = total - withBias;
      const averageScore = mockData.reduce((sum, a) => sum + a.biasScore, 0) / total;

      setStats({
        total,
        withBias,
        withoutBias,
        averageScore: Math.round(averageScore * 10) / 10,
      });
    } catch (error) {
      console.error("Error loading bias analyses:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredAnalyses = analyses.filter((analysis) => {
    const matchesSearch =
      analysis.modelName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      analysis.modelType.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesModelType = modelTypeFilter === "ALL" || analysis.modelType === modelTypeFilter;
    const matchesBiasLevel = biasLevelFilter === "ALL" || analysis.biasLevel === biasLevelFilter;
    return matchesSearch && matchesModelType && matchesBiasLevel;
  });

  const getBiasLevelBadge = (level: string) => {
    switch (level) {
      case "HIGH":
        return <Badge className="bg-red-500/20 text-red-400 border-red-500/50">HIGH</Badge>;
      case "MEDIUM":
        return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">MEDIUM</Badge>;
      case "LOW":
        return <Badge className="bg-orange-500/20 text-orange-400 border-orange-500/50">LOW</Badge>;
      case "NONE":
        return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">NONE</Badge>;
      default:
        return <Badge>{level}</Badge>;
    }
  };

  const getBiasScoreColor = (score: number) => {
    if (score >= 80) return "text-green-400";
    if (score >= 60) return "text-yellow-400";
    if (score >= 40) return "text-orange-400";
    return "text-red-400";
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "RESOLVED":
        return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">RESOLVED</Badge>;
      case "REVIEWED":
        return <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50">REVIEWED</Badge>;
      case "PENDING":
        return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">PENDING</Badge>;
      default:
        return <Badge>{status}</Badge>;
    }
  };

  return (
    <div className="space-y-4">
      {/* Header */}
      <div>
        <div className="flex items-center gap-3 mb-2">
          <AlertTriangle className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.biasAnalysis.title", "Análisis de Sesgo")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9 mb-2">
          {t("governance.models.biasAnalysis.subtitle", "Monitoreo y análisis de sesgos en modelos de IA")}
        </p>
      </div>

      {/* Action Buttons */}
      <div className="flex items-center justify-end">
        <Button
          onClick={() => {
            // Navegar a la página de testing del modelo seleccionado (si hay uno)
            // Por ahora, mostrar modal de selección de modelo
            alert("Selecciona un modelo desde el registro para ejecutar análisis");
          }}
        >
          <Plus className="w-4 h-4 mr-2" />
          {t("governance.models.biasAnalysis.newAnalysis", "Nuevo Análisis")}
        </Button>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.biasAnalysis.metrics.total", "Total de Análisis")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.biasAnalysis.metrics.withBias", "Con Sesgo Detectado")}
            </p>
            <h2 className="text-2xl font-bold text-red-400">{stats.withBias}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.biasAnalysis.metrics.withoutBias", "Sin Sesgo")}
            </p>
            <h2 className="text-2xl font-bold text-green-400">{stats.withoutBias}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.biasAnalysis.metrics.averageScore", "Score Promedio")}
            </p>
            <h2 className={`text-2xl font-bold ${getBiasScoreColor(stats.averageScore)}`}>
              {stats.averageScore}/100
            </h2>
          </CardBody>
        </Card>
      </div>

      {/* Filtros */}
      <Card className="border-2">
        <CardContent className="p-4">
          <div className="flex gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground w-4 h-4" />
              <Input
                placeholder={t("governance.models.biasAnalysis.filters.search", "Buscar por nombre de modelo...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={modelTypeFilter} onValueChange={setModelTypeFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.biasAnalysis.filters.modelType", "Tipo de Modelo")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.biasAnalysis.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="TEXT">TEXT</SelectItem>
                <SelectItem value="IMAGE">IMAGE</SelectItem>
                <SelectItem value="MULTIMODAL">MULTIMODAL</SelectItem>
                <SelectItem value="AUDIO">AUDIO</SelectItem>
              </SelectContent>
            </Select>
            <Select value={biasLevelFilter} onValueChange={setBiasLevelFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.biasAnalysis.filters.biasLevel", "Nivel de Sesgo")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.biasAnalysis.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="NONE">NONE</SelectItem>
                <SelectItem value="LOW">LOW</SelectItem>
                <SelectItem value="MEDIUM">MEDIUM</SelectItem>
                <SelectItem value="HIGH">HIGH</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.biasAnalysis.table.title", "Análisis de Sesgo")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredAnalyses.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.biasAnalysis.table.noResults", "No hay análisis disponibles")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.modelName", "Modelo")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.modelType", "Tipo")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.biasScore", "Score")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.biasLevel", "Nivel")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.affectedGroups", "Grupos Afectados")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.analysisDate", "Fecha")}</th>
                    <th className="text-left p-2">{t("governance.models.biasAnalysis.table.status", "Estado")}</th>
                    <th className="text-center p-2">{t("governance.models.biasAnalysis.table.actions", "Acciones")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredAnalyses.map((analysis) => (
                    <tr
                      key={analysis.id}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2 font-medium">{analysis.modelName}</td>
                      <td className="p-2">
                        <Badge variant="outline">{analysis.modelType}</Badge>
                      </td>
                      <td className="p-2">
                        <span className={`font-bold ${getBiasScoreColor(analysis.biasScore)}`}>
                          {analysis.biasScore}/100
                        </span>
                      </td>
                      <td className="p-2">{getBiasLevelBadge(analysis.biasLevel)}</td>
                      <td className="p-2">
                        {analysis.affectedGroups.length > 0 ? (
                          <div className="flex flex-wrap gap-1">
                            {analysis.affectedGroups.map((group, idx) => (
                              <Badge key={idx} variant="outline" className="text-xs">
                                {group}
                              </Badge>
                            ))}
                          </div>
                        ) : (
                          <span className="text-muted-foreground text-xs">-</span>
                        )}
                      </td>
                      <td className="p-2 text-xs text-muted-foreground">
                        {new Date(analysis.analysisDate).toLocaleDateString()}
                      </td>
                      <td className="p-2">{getStatusBadge(analysis.status)}</td>
                      <td className="p-2">
                        <div className="flex items-center justify-center gap-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            title={t("common.view", "Ver")}
                            onClick={() => setSelectedAnalysis(analysis)}
                          >
                            <Eye className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="text-red-400 hover:text-red-300"
                            title={t("common.delete", "Eliminar")}
                            onClick={() => {
                              // TODO: Implementar eliminación
                              console.log("Delete analysis:", analysis.id);
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
        </CardContent>
      </Card>

      {/* Modal de Detalles */}
      <SimpleModal
        isOpen={selectedAnalysis !== null}
        onClose={() => setSelectedAnalysis(null)}
        title={selectedAnalysis ? `${t("governance.models.biasAnalysis.detail.title", "Detalles del Análisis")} - ${selectedAnalysis.modelName}` : ""}
        maxWidth="max-w-5xl"
      >
        {selectedAnalysis && (
          <div className="space-y-6">
            {/* Información General */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.biasAnalysis.detail.general", "Información General")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.modelName", "Modelo")}
                    </label>
                    <p className="text-base font-semibold">{selectedAnalysis.modelName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.modelType", "Tipo")}
                    </label>
                    <p className="text-base">
                      <Badge variant="outline">{selectedAnalysis.modelType}</Badge>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.biasScore", "Score de Sesgo")}
                    </label>
                    <p className={`text-base font-bold ${getBiasScoreColor(selectedAnalysis.biasScore)}`}>
                      {selectedAnalysis.biasScore}/100
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.biasLevel", "Nivel de Sesgo")}
                    </label>
                    <p className="text-base">{getBiasLevelBadge(selectedAnalysis.biasLevel)}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.detectedBias", "Sesgo Detectado")}
                    </label>
                    <p className="text-base">
                      {selectedAnalysis.detectedBias ? (
                        <Badge className="bg-red-500/20 text-red-400 border-red-500/50">
                          {t("common.yes", "Sí")}
                        </Badge>
                      ) : (
                        <Badge className="bg-green-500/20 text-green-400 border-green-500/50">
                          {t("common.no", "No")}
                        </Badge>
                      )}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.biasAnalysis.detail.status", "Estado")}
                    </label>
                    <p className="text-base">{getStatusBadge(selectedAnalysis.status)}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Grupos Afectados */}
            {selectedAnalysis.affectedGroups.length > 0 && (
              <Card className="border-2">
                <CardHeader>
                  <CardTitle>{t("governance.models.biasAnalysis.detail.affectedGroups", "Grupos Afectados")}</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="flex flex-wrap gap-2">
                    {selectedAnalysis.affectedGroups.map((group, idx) => (
                      <Badge key={idx} variant="outline" className="text-sm">
                        {group}
                      </Badge>
                    ))}
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Método de Análisis */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.biasAnalysis.detail.analysisMethod", "Método de Análisis")}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-base">{selectedAnalysis.analysisMethod}</p>
              </CardContent>
            </Card>

            {/* Recomendaciones */}
            {selectedAnalysis.recommendations && (
              <Card className="border-2">
                <CardHeader>
                  <CardTitle>{t("governance.models.biasAnalysis.detail.recommendations", "Recomendaciones")}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-base text-muted-foreground">{selectedAnalysis.recommendations}</p>
                </CardContent>
              </Card>
            )}

            {/* Información Adicional */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.biasAnalysis.detail.additionalInfo", "Información Adicional")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-2">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.biasAnalysis.detail.analysisDate", "Fecha de Análisis")}
                  </label>
                  <p className="text-base">
                    {new Date(selectedAnalysis.analysisDate).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.biasAnalysis.detail.analyst", "Analista")}
                  </label>
                  <p className="text-base">{selectedAnalysis.analyst}</p>
                </div>
              </CardContent>
            </Card>
          </div>
        )}
      </SimpleModal>
    </div>
  );
}


