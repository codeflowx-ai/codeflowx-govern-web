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
import { Eye, Lightbulb, Search, Trash2, Plus } from "lucide-react";
import { useEffect, useState } from "react";

interface ExplainabilityAnalysis {
  id: number;
  modelId: number;
  modelName: string;
  modelType: string;
  explainabilityScore: number; // 0-100
  isExplainable: boolean;
  explainabilityMethod: string; // Método de explicabilidad utilizado (ej: "SHAP", "LIME", "Integrated Gradients")
  featureImportance?: Record<string, number>; // Importancia de características
  analysisDate: string;
  analyst: string;
  explanationQuality: "EXCELLENT" | "GOOD" | "FAIR" | "POOR";
  limitations?: string; // Limitaciones de la explicabilidad
  recommendations?: string; // Recomendaciones para mejorar
  status: "PENDING" | "REVIEWED" | "APPROVED";
}

export default function ExplainabilityPage() {
  const { t } = useTranslation();
  const [analyses, setAnalyses] = useState<ExplainabilityAnalysis[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [modelTypeFilter, setModelTypeFilter] = useState<string>("ALL");
  const [scoreFilter, setScoreFilter] = useState<string>("ALL");
  const [selectedAnalysis, setSelectedAnalysis] = useState<ExplainabilityAnalysis | null>(null);
  const [stats, setStats] = useState({
    total: 0,
    explainable: 0,
    notExplainable: 0,
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
      if (scoreFilter !== "ALL") searchParams.append("score", scoreFilter);

      const response = await fetch(
        `/api/v1/bff/compliance/models/explainability${searchParams.toString() ? `?${searchParams.toString()}` : ""}`
      );

      if (response.ok) {
        const data: ExplainabilityAnalysis[] = await response.json();
        setAnalyses(data);

        // Calcular estadísticas
        const total = data.length;
        const explainable = data.filter((a) => a.isExplainable).length;
        const notExplainable = total - explainable;
        const averageScore = data.reduce((sum, a) => sum + a.explainabilityScore, 0) / total;

        setStats({
          total,
          explainable,
          notExplainable,
          averageScore: Math.round(averageScore * 10) / 10,
        });
        return;
      }

      // Fallback a mock data si la API falla
      const mockData: ExplainabilityAnalysis[] = [
        {
          id: 1,
          modelId: 1,
          modelName: "GPT-4 Fine-tuned Classification",
          modelType: "TEXT",
          explainabilityScore: 75,
          isExplainable: true,
          explainabilityMethod: "SHAP",
          featureImportance: {
            "feature_1": 0.25,
            "feature_2": 0.18,
            "feature_3": 0.15,
          },
          analysisDate: "2024-01-15T10:30:00",
          analyst: "analyst@example.com",
          explanationQuality: "GOOD",
          limitations: "Algunas características tienen baja importancia pero son críticas en casos edge",
          recommendations: "Mejorar documentación de características y agregar ejemplos de explicación",
          status: "REVIEWED",
        },
        {
          id: 2,
          modelId: 2,
          modelName: "Neural Network Credit Scoring",
          modelType: "TEXT",
          explainabilityScore: 45,
          isExplainable: false,
          explainabilityMethod: "LIME",
          analysisDate: "2024-01-14T14:20:00",
          analyst: "analyst@example.com",
          explanationQuality: "POOR",
          limitations: "Modelo de caja negra con baja interpretabilidad",
          recommendations: "Considerar modelos más interpretables o usar técnicas de post-hoc explicabilidad avanzadas",
          status: "PENDING",
        },
        {
          id: 3,
          modelId: 3,
          modelName: "Decision Tree Classifier",
          modelType: "TEXT",
          explainabilityScore: 95,
          isExplainable: true,
          explainabilityMethod: "Tree-based",
          featureImportance: {
            "age": 0.35,
            "income": 0.28,
            "education": 0.20,
          },
          analysisDate: "2024-01-13T09:15:00",
          analyst: "analyst@example.com",
          explanationQuality: "EXCELLENT",
          recommendations: "Modelo altamente explicable, mantener estructura",
          status: "APPROVED",
        },
        {
          id: 4,
          modelId: 4,
          modelName: "ResNet Image Classification",
          modelType: "IMAGE",
          explainabilityScore: 60,
          isExplainable: true,
          explainabilityMethod: "Integrated Gradients",
          analysisDate: "2024-01-12T16:45:00",
          analyst: "analyst@example.com",
          explanationQuality: "FAIR",
          limitations: "Explicaciones a nivel de píxel pueden ser difíciles de interpretar",
          recommendations: "Agregar visualizaciones de heatmaps y documentación de interpretación",
          status: "REVIEWED",
        },
        {
          id: 5,
          modelId: 5,
          modelName: "BERT Sentiment Analysis",
          modelType: "TEXT",
          explainabilityScore: 70,
          isExplainable: true,
          explainabilityMethod: "Attention Weights",
          featureImportance: {
            "tokens_positive": 0.22,
            "tokens_negative": 0.19,
            "context": 0.16,
          },
          analysisDate: "2024-01-11T11:30:00",
          analyst: "analyst@example.com",
          explanationQuality: "GOOD",
          recommendations: "Mejorar visualización de attention weights para usuarios finales",
          status: "APPROVED",
        },
      ];

      setAnalyses(mockData);

      // Calcular estadísticas
      const total = mockData.length;
      const explainable = mockData.filter((a) => a.isExplainable).length;
      const notExplainable = total - explainable;
      const averageScore = mockData.reduce((sum, a) => sum + a.explainabilityScore, 0) / total;

      setStats({
        total,
        explainable,
        notExplainable,
        averageScore: Math.round(averageScore * 10) / 10,
      });
    } catch (error) {
      console.error("Error loading explainability analyses:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredAnalyses = analyses.filter((analysis) => {
    const matchesSearch =
      analysis.modelName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      analysis.modelType.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesModelType = modelTypeFilter === "ALL" || analysis.modelType === modelTypeFilter;
    const matchesScore =
      scoreFilter === "ALL" ||
      (scoreFilter === "HIGH" && analysis.explainabilityScore >= 80) ||
      (scoreFilter === "MEDIUM" && analysis.explainabilityScore >= 60 && analysis.explainabilityScore < 80) ||
      (scoreFilter === "LOW" && analysis.explainabilityScore < 60);
    return matchesSearch && matchesModelType && matchesScore;
  });

  const getScoreColor = (score: number) => {
    if (score >= 80) return "text-green-400";
    if (score >= 60) return "text-yellow-400";
    if (score >= 40) return "text-orange-400";
    return "text-red-400";
  };

  const getQualityBadge = (quality: string) => {
    switch (quality) {
      case "EXCELLENT":
        return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">EXCELLENT</Badge>;
      case "GOOD":
        return <Badge className="bg-blue-500/20 text-blue-400 border-blue-500/50">GOOD</Badge>;
      case "FAIR":
        return <Badge className="bg-yellow-500/20 text-yellow-400 border-yellow-500/50">FAIR</Badge>;
      case "POOR":
        return <Badge className="bg-red-500/20 text-red-400 border-red-500/50">POOR</Badge>;
      default:
        return <Badge>{quality}</Badge>;
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "APPROVED":
        return <Badge className="bg-green-500/20 text-green-400 border-green-500/50">APPROVED</Badge>;
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
          <Lightbulb className="w-6 h-6 text-primary" />
          <h1 className="text-2xl font-bold text-foreground">
            {t("governance.models.explainability.title", "Explicabilidad")}
          </h1>
        </div>
        <p className="text-sm text-muted-foreground ml-9 mb-2">
          {t("governance.models.explainability.subtitle", "Análisis de explicabilidad de modelos de IA")}
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
          {t("governance.models.explainability.newAnalysis", "Nuevo Análisis")}
        </Button>
      </div>

      {/* Métricas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.explainability.metrics.total", "Total de Análisis")}
            </p>
            <h2 className="text-2xl font-bold">{stats.total}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.explainability.metrics.explainable", "Modelos Explicables")}
            </p>
            <h2 className="text-2xl font-bold text-green-400">{stats.explainable}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.explainability.metrics.notExplainable", "No Explicables")}
            </p>
            <h2 className="text-2xl font-bold text-red-400">{stats.notExplainable}</h2>
          </CardBody>
        </Card>
        <Card className="border-2">
          <CardBody className="p-4">
            <p className="text-xs font-medium text-muted-foreground mb-1">
              {t("governance.models.explainability.metrics.averageScore", "Score Promedio")}
            </p>
            <h2 className={`text-2xl font-bold ${getScoreColor(stats.averageScore)}`}>
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
                placeholder={t("governance.models.explainability.filters.search", "Buscar por nombre de modelo...")}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
            <Select value={modelTypeFilter} onValueChange={setModelTypeFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.explainability.filters.modelType", "Tipo de Modelo")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.explainability.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="TEXT">TEXT</SelectItem>
                <SelectItem value="IMAGE">IMAGE</SelectItem>
                <SelectItem value="MULTIMODAL">MULTIMODAL</SelectItem>
                <SelectItem value="AUDIO">AUDIO</SelectItem>
              </SelectContent>
            </Select>
            <Select value={scoreFilter} onValueChange={setScoreFilter}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder={t("governance.models.explainability.filters.score", "Score de Explicabilidad")} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("governance.models.explainability.filters.all", "TODOS")}</SelectItem>
                <SelectItem value="HIGH">≥80 (Alto)</SelectItem>
                <SelectItem value="MEDIUM">60-79 (Medio)</SelectItem>
                <SelectItem value="LOW">&lt;60 (Bajo)</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Tabla */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>{t("governance.models.explainability.table.title", "Análisis de Explicabilidad")}</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">{t("common.loading", "Cargando...")}</div>
          ) : filteredAnalyses.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.models.explainability.table.noResults", "No hay análisis disponibles")}
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.models.explainability.table.modelName", "Modelo")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.modelType", "Tipo")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.score", "Score")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.method", "Método")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.quality", "Calidad")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.analysisDate", "Fecha")}</th>
                    <th className="text-left p-2">{t("governance.models.explainability.table.status", "Estado")}</th>
                    <th className="text-center p-2">{t("governance.models.explainability.table.actions", "Acciones")}</th>
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
                        <span className={`font-bold ${getScoreColor(analysis.explainabilityScore)}`}>
                          {analysis.explainabilityScore}/100
                        </span>
                        {analysis.isExplainable && (
                          <Badge className="ml-2 bg-green-500/20 text-green-400 border-green-500/50 text-xs">
                            EXPLICABLE
                          </Badge>
                        )}
                      </td>
                      <td className="p-2">
                        <Badge variant="outline">{analysis.explainabilityMethod}</Badge>
                      </td>
                      <td className="p-2">{getQualityBadge(analysis.explanationQuality)}</td>
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
        title={selectedAnalysis ? `${t("governance.models.explainability.detail.title", "Detalles del Análisis")} - ${selectedAnalysis.modelName}` : ""}
        maxWidth="max-w-5xl"
      >
        {selectedAnalysis && (
          <div className="space-y-6">
            {/* Información General */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.explainability.detail.general", "Información General")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.modelName", "Modelo")}
                    </label>
                    <p className="text-base font-semibold">{selectedAnalysis.modelName}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.modelType", "Tipo")}
                    </label>
                    <p className="text-base">
                      <Badge variant="outline">{selectedAnalysis.modelType}</Badge>
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.score", "Score de Explicabilidad")}
                    </label>
                    <p className={`text-base font-bold ${getScoreColor(selectedAnalysis.explainabilityScore)}`}>
                      {selectedAnalysis.explainabilityScore}/100
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.isExplainable", "Es Explicable")}
                    </label>
                    <p className="text-base">
                      {selectedAnalysis.isExplainable ? (
                        <Badge className="bg-green-500/20 text-green-400 border-green-500/50">
                          {t("common.yes", "Sí")}
                        </Badge>
                      ) : (
                        <Badge className="bg-red-500/20 text-red-400 border-red-500/50">
                          {t("common.no", "No")}
                        </Badge>
                      )}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.quality", "Calidad de Explicación")}
                    </label>
                    <p className="text-base">{getQualityBadge(selectedAnalysis.explanationQuality)}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.models.explainability.detail.status", "Estado")}
                    </label>
                    <p className="text-base">{getStatusBadge(selectedAnalysis.status)}</p>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Método de Explicabilidad */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.explainability.detail.method", "Método de Explicabilidad")}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-base">
                  <Badge variant="outline" className="text-sm">{selectedAnalysis.explainabilityMethod}</Badge>
                </p>
              </CardContent>
            </Card>

            {/* Importancia de Características */}
            {selectedAnalysis.featureImportance && Object.keys(selectedAnalysis.featureImportance).length > 0 && (
              <Card className="border-2">
                <CardHeader>
                  <CardTitle>{t("governance.models.explainability.detail.featureImportance", "Importancia de Características")}</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-2">
                    {Object.entries(selectedAnalysis.featureImportance)
                      .sort(([, a], [, b]) => (b as number) - (a as number))
                      .map(([feature, importance]) => (
                        <div key={feature} className="flex items-center justify-between">
                          <span className="text-sm font-medium">{feature}</span>
                          <div className="flex items-center gap-2">
                            <div className="w-32 bg-muted rounded-full h-2">
                              <div
                                className="bg-primary h-2 rounded-full"
                                style={{ width: `${(importance as number) * 100}%` }}
                              />
                            </div>
                            <span className="text-sm text-muted-foreground w-12 text-right">
                              {((importance as number) * 100).toFixed(1)}%
                            </span>
                          </div>
                        </div>
                      ))}
                  </div>
                </CardContent>
              </Card>
            )}

            {/* Limitaciones */}
            {selectedAnalysis.limitations && (
              <Card className="border-2">
                <CardHeader>
                  <CardTitle>{t("governance.models.explainability.detail.limitations", "Limitaciones")}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-base text-muted-foreground">{selectedAnalysis.limitations}</p>
                </CardContent>
              </Card>
            )}

            {/* Recomendaciones */}
            {selectedAnalysis.recommendations && (
              <Card className="border-2">
                <CardHeader>
                  <CardTitle>{t("governance.models.explainability.detail.recommendations", "Recomendaciones")}</CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-base text-muted-foreground">{selectedAnalysis.recommendations}</p>
                </CardContent>
              </Card>
            )}

            {/* Información Adicional */}
            <Card className="border-2">
              <CardHeader>
                <CardTitle>{t("governance.models.explainability.detail.additionalInfo", "Información Adicional")}</CardTitle>
              </CardHeader>
              <CardContent className="space-y-2">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.explainability.detail.analysisDate", "Fecha de Análisis")}
                  </label>
                  <p className="text-base">
                    {new Date(selectedAnalysis.analysisDate).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.models.explainability.detail.analyst", "Analista")}
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


