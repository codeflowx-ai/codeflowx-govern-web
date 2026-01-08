"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Progress } from "@/components/ui/progress";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Activity,
  BarChart3,
  Brain,
  CheckCircle,
  Clock,
  DollarSign,
  Edit,
  Eye,
  Filter,
  Globe,
  Pause,
  Play,
  Plus,
  Search,
  Server,
  Settings,
  TestTube,
  XCircle,
  Zap,
} from "lucide-react";
import { useState } from "react";

// Mock data
const mockModels = [
  {
    id: 1,
    modelName: "text-embedding-ada-002",
    modelType: "EMBEDDING",
    modelProvider: "OpenAI",
    modelVersion: "2.0",
    modelEndpoint: "https://api.openai.com/v1/embeddings",
    isLocal: false,
    isActive: true,
    performanceMetrics: {
      accuracy: 0.95,
      speed: 0.8,
      reliability: 0.98,
    },
    costPerToken: 0.0001,
    maxTokens: 8191,
    contextWindow: 8191,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-15T10:30:00Z",
    usage: {
      totalTokens: 1250000,
      totalCost: 125.0,
      requests: 1500,
      avgResponseTime: 0.8,
    },
  },
  {
    id: 2,
    modelName: "cohere-rerank-multilingual-v2.0",
    modelType: "RERANKER",
    modelProvider: "Cohere",
    modelVersion: "2.0",
    modelEndpoint: "https://api.cohere.ai/v1/rerank",
    isLocal: false,
    isActive: true,
    performanceMetrics: {
      accuracy: 0.92,
      speed: 0.7,
      reliability: 0.96,
    },
    costPerToken: 0.001,
    maxTokens: 1000,
    contextWindow: 1000,
    createdAt: "2024-01-02T00:00:00Z",
    updatedAt: "2024-01-15T09:15:00Z",
    usage: {
      totalTokens: 85000,
      totalCost: 85.0,
      requests: 850,
      avgResponseTime: 1.2,
    },
  },
  {
    id: 3,
    modelName: "gpt-4-turbo",
    modelType: "LLM",
    modelProvider: "OpenAI",
    modelVersion: "4.0",
    modelEndpoint: "https://api.openai.com/v1/chat/completions",
    isLocal: false,
    isActive: true,
    performanceMetrics: {
      accuracy: 0.94,
      speed: 0.6,
      reliability: 0.97,
    },
    costPerToken: 0.01,
    maxTokens: 128000,
    contextWindow: 128000,
    createdAt: "2024-01-03T00:00:00Z",
    updatedAt: "2024-01-15T08:45:00Z",
    usage: {
      totalTokens: 75000,
      totalCost: 750.0,
      requests: 450,
      avgResponseTime: 2.5,
    },
  },
];

export default function RAGModelsPage() {
  const { t } = useTranslation();
  const [models, setModels] = useState(mockModels);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("all");
  const [providerFilter, setProviderFilter] = useState("all");

  const getModelTypeIcon = (type: string) => {
    switch (type) {
      case "EMBEDDING":
        return <Brain className="h-4 w-4 text-blue-500" />;
      case "RERANKER":
        return <BarChart3 className="h-4 w-4 text-purple-500" />;
      case "LLM":
        return <Zap className="h-4 w-4 text-green-500" />;
      default:
        return <Activity className="h-4 w-4 text-gray-500" />;
    }
  };

  const getModelTypeColor = (type: string) => {
    switch (type) {
      case "EMBEDDING":
        return "bg-blue-100 text-blue-800";
      case "RERANKER":
        return "bg-purple-100 text-purple-800";
      case "LLM":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const filteredModels = models.filter((model) => {
    const matchesSearch =
      model.modelName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      model.modelProvider.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "all" || model.modelType === typeFilter;
    const matchesProvider =
      providerFilter === "all" || model.modelProvider === providerFilter;
    return matchesSearch && matchesType && matchesProvider;
  });

  const totalModels = models.length;
  const activeModels = models.filter((m) => m.isActive).length;
  const localModels = models.filter((m) => m.isLocal).length;
  const totalCost = models.reduce((sum, m) => sum + m.usage.totalCost, 0);

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Settings className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("rag.models.title", "Modelos RAG")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("rag.models.subtitle", "Gestión de modelos de IA para proyectos RAG")}
          </p>
        </div>
        <Button
          onClick={() => (window.location.href = "/rag/models/create")}
          className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white shadow-lg"
        >
          <Plus className="h-4 w-4 mr-2" />
          {t("rag.models.newModel", "Nuevo Modelo")}
        </Button>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-4 gap-4">
        <Card className="border-primary hover:shadow-lg hover:border-primary/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.models.totalModels", "Total Modelos")}
                </p>
                <h2 className="text-2xl font-bold text-primary mb-1">
                  {totalModels}
                </h2>
              </div>
              <Brain className="w-8 h-8 text-primary flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-green-500 hover:shadow-lg hover:border-green-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.models.activeModels", "Modelos Activos")}
                </p>
                <h2 className="text-2xl font-bold text-green-600 mb-1">
                  {activeModels}
                </h2>
              </div>
              <CheckCircle className="w-8 h-8 text-green-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-purple-500 hover:shadow-lg hover:border-purple-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.models.localModels", "Modelos Locales")}
                </p>
                <h2 className="text-2xl font-bold text-purple-600 mb-1">
                  {localModels}
                </h2>
              </div>
              <Server className="w-8 h-8 text-purple-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>

        <Card className="border-yellow-500 hover:shadow-lg hover:border-yellow-500/50 transition-all">
          <CardBody className="p-4">
            <div className="flex items-center justify-between">
              <div className="flex-grow">
                <p className="text-xs font-medium text-muted-foreground mb-1">
                  {t("rag.models.totalCost", "Costo Total")}
                </p>
                <h2 className="text-2xl font-bold text-yellow-600 mb-1">
                  ${totalCost.toFixed(2)}
                </h2>
              </div>
              <DollarSign className="w-8 h-8 text-yellow-600 flex-shrink-0" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Filters and Search */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
        <CardBody className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("rag.models.searchPlaceholder", "Buscar modelos...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex gap-4">
              <Select value={typeFilter} onValueChange={setTypeFilter}>
                <SelectTrigger className="w-40">
                  <Filter className="h-4 w-4 mr-2" />
                  <SelectValue placeholder={t("rag.models.filterType", "Tipo")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">
                    {t("rag.models.allTypes", "Todos")}
                  </SelectItem>
                  <SelectItem value="EMBEDDING">
                    {t("rag.models.embedding", "Embedding")}
                  </SelectItem>
                  <SelectItem value="RERANKER">
                    {t("rag.models.reranker", "Reranker")}
                  </SelectItem>
                  <SelectItem value="LLM">{t("rag.models.llm", "LLM")}</SelectItem>
                </SelectContent>
              </Select>
              <Select value={providerFilter} onValueChange={setProviderFilter}>
                <SelectTrigger className="w-40">
                  <Filter className="h-4 w-4 mr-2" />
                  <SelectValue placeholder={t("rag.models.filterProvider", "Proveedor")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">
                    {t("rag.models.allProviders", "Todos")}
                  </SelectItem>
                  <SelectItem value="OpenAI">OpenAI</SelectItem>
                  <SelectItem value="Cohere">Cohere</SelectItem>
                  <SelectItem value="Anthropic">Anthropic</SelectItem>
                  <SelectItem value="Local">Local</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Models Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4">
        {filteredModels.map((model) => (
          <Card
            key={model.id}
            className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all"
          >
            <CardBody className="p-4">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="p-2 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg">
                    {getModelTypeIcon(model.modelType)}
                  </div>
                  <div>
                    <h3 className="font-semibold text-foreground">
                      {model.modelName}
                    </h3>
                    <div className="flex items-center space-x-2 mt-1">
                      <Badge
                        className={`text-xs ${getModelTypeColor(model.modelType)}`}
                      >
                        {model.modelType}
                      </Badge>
                    </div>
                  </div>
                </div>
              </div>

              <div className="space-y-3 mb-4">
                <div className="flex items-center space-x-2">
                  {model.isLocal ? (
                    <Server className="h-4 w-4 text-blue-500" />
                  ) : (
                    <Globe className="h-4 w-4 text-green-500" />
                  )}
                  <span className="text-sm text-muted-foreground">
                    {model.modelProvider} v{model.modelVersion}
                  </span>
                </div>

                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span className="text-muted-foreground">
                      {t("rag.models.costPerToken", "Costo/token")}
                    </span>
                    <p className="font-medium text-foreground">
                      ${model.costPerToken}
                    </p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">
                      {t("rag.models.maxTokens", "Max Tokens")}
                    </span>
                    <p className="font-medium text-foreground">
                      {model.maxTokens.toLocaleString()}
                    </p>
                  </div>
                </div>
              </div>

              {/* Performance Metrics */}
              <div className="mb-4">
                <h4 className="text-sm font-medium text-foreground mb-2">
                  {t("rag.models.performance", "Rendimiento")}
                </h4>
                <div className="space-y-2">
                  <div>
                    <div className="flex justify-between text-xs mb-1">
                      <span className="text-muted-foreground">
                        {t("rag.models.accuracy", "Precisión")}
                      </span>
                      <span className="font-medium text-foreground">
                        {Math.round(model.performanceMetrics.accuracy * 100)}%
                      </span>
                    </div>
                    <Progress
                      value={model.performanceMetrics.accuracy * 100}
                      className="h-1"
                    />
                  </div>
                </div>
              </div>

              {/* Usage Stats */}
              <div className="grid grid-cols-2 gap-4 mb-4 text-sm">
                <div className="text-center p-2 bg-muted/30 rounded">
                  <div className="font-semibold text-foreground">
                    {model.usage.requests}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {t("rag.models.requests", "Requests")}
                  </div>
                </div>
                <div className="text-center p-2 bg-muted/30 rounded">
                  <div className="font-semibold text-foreground">
                    ${model.usage.totalCost.toFixed(2)}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {t("rag.models.totalCost", "Costo Total")}
                  </div>
                </div>
              </div>

              {/* Footer */}
              <div className="flex items-center justify-between pt-4 border-t border-slate-200">
                <div className="flex items-center space-x-2 text-xs text-slate-500">
                  <Clock className="h-3 w-3" />
                  <span>
                    {new Date(model.updatedAt).toLocaleDateString("es-ES")}
                  </span>
                </div>
                <div className="flex items-center space-x-1">
                  <Button variant="ghost" size="sm">
                    <Eye className="h-3 w-3" />
                  </Button>
                  <Button variant="ghost" size="sm">
                    <Edit className="h-3 w-3" />
                  </Button>
                </div>
              </div>
            </CardBody>
          </Card>
        ))}
      </div>

      {filteredModels.length === 0 && (
        <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
          <CardBody className="p-12 text-center">
            <Brain className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">
              {t("rag.models.noModels", "No hay modelos")}
            </h3>
            <p className="text-muted-foreground mb-6">
              {t("rag.models.noModelsDescription", "Crea tu primer modelo RAG")}
            </p>
            <Button
              onClick={() => (window.location.href = "/rag/models/create")}
              className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white"
            >
              <Plus className="h-4 w-4 mr-2" />
              {t("rag.models.createFirst", "Crear Modelo")}
            </Button>
          </CardBody>
        </Card>
      )}
    </div>
  );
}
