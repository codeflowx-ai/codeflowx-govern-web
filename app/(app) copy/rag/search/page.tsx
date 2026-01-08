"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import HelpTooltip from "@/components/ui/help-tooltip";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import {
  Brain,
  CheckCircle,
  Database,
  FileText,
  Filter,
  Globe,
  Play,
  RefreshCw,
  Search,
  Settings,
  Target,
  TrendingUp,
  Zap,
} from "lucide-react";
import { useState } from "react";

// Interfaces
interface RAGProject {
  id: string;
  name: string;
  description: string;
  status: "active" | "inactive" | "error";
  embeddingModel: string;
  llmModel: string;
  rerankerModel?: string;
  totalChunks: number;
  totalSearches: number;
  lastUsed: string;
}

interface SearchResult {
  id: string;
  chunkId: string;
  chunkText: string;
  chunkType: string;
  similarityScore: number;
  semanticScore: number;
  rerankScore?: number;
  source: {
    type: "document" | "webscraping" | "datasource";
    name: string;
    id: string;
  };
  metadata: {
    keywords: string[];
    entities: string[];
    autoTags: string[];
    customTags: string[];
  };
  position: {
    start: number;
    end: number;
  };
}

interface SearchQuery {
  id: string;
  query: string;
  searchType: "vector" | "textual" | "hybrid" | "filtered";
  projectId: string;
  results: SearchResult[];
  metrics: {
    totalResults: number;
    averageScore: number;
    processingTime: number;
    rerankTime?: number;
  };
  filters?: {
    chunkTypes?: string[];
    sources?: string[];
    tags?: string[];
    dateRange?: {
      start: string;
      end: string;
    };
  };
  createdAt: string;
}

// Mock data
const mockRAGProjects: RAGProject[] = [
  {
    id: "1",
    name: "Asistente Herbolario",
    description:
      "Chatbot especializado en productos herbales y medicina natural",
    status: "active",
    embeddingModel: "text-embedding-ada-002",
    llmModel: "gpt-4-turbo",
    rerankerModel: "cohere-rerank-multilingual-v2.0",
    totalChunks: 1250,
    totalSearches: 342,
    lastUsed: "2024-01-15T10:30:00Z",
  },
  {
    id: "2",
    name: "Chatbot Náutico",
    description:
      "Asistente para consultas sobre navegación y equipamiento náutico",
    status: "active",
    embeddingModel: "text-embedding-ada-002",
    llmModel: "claude-3-opus",
    rerankerModel: "cohere-rerank-multilingual-v2.0",
    totalChunks: 890,
    totalSearches: 156,
    lastUsed: "2024-01-15T09:45:00Z",
  },
];

const mockSearchHistory: SearchQuery[] = [
  {
    id: "1",
    query: "propiedades medicinales de la manzanilla",
    searchType: "hybrid",
    projectId: "1",
    results: [
      {
        id: "1",
        chunkId: "chunk_001",
        chunkText:
          "La manzanilla tiene propiedades antiinflamatorias y antiespasmódicas que la hacen ideal para problemas digestivos. Los compuestos activos principales son el camazuleno y el bisabolol.",
        chunkType: "MAIN_CONTENT",
        similarityScore: 0.92,
        semanticScore: 0.89,
        rerankScore: 0.95,
        source: {
          type: "document",
          name: "Manual de Fitoterapia - Capítulo 3",
          id: "doc_001",
        },
        metadata: {
          keywords: ["manzanilla", "antiinflamatorio", "digestivo"],
          entities: ["camazuleno", "bisabolol"],
          autoTags: ["medicinal", "digestivo"],
          customTags: ["importante"],
        },
        position: {
          start: 0,
          end: 200,
        },
      },
      {
        id: "2",
        chunkId: "chunk_002",
        chunkText:
          "Para el uso medicinal de la manzanilla, se recomienda preparar una infusión con 1-2 cucharaditas de flores secas por taza de agua caliente.",
        chunkType: "INSTRUCTIONS",
        similarityScore: 0.78,
        semanticScore: 0.75,
        rerankScore: 0.82,
        source: {
          type: "document",
          name: "Manual de Fitoterapia - Capítulo 3",
          id: "doc_001",
        },
        metadata: {
          keywords: ["infusión", "flores", "secas"],
          entities: ["manzanilla"],
          autoTags: ["preparación", "uso"],
          customTags: ["práctico"],
        },
        position: {
          start: 0,
          end: 150,
        },
      },
    ],
    metrics: {
      totalResults: 8,
      averageScore: 0.85,
      processingTime: 1200,
      rerankTime: 300,
    },
    filters: {
      chunkTypes: ["MAIN_CONTENT", "INSTRUCTIONS"],
      sources: ["document"],
    },
    createdAt: "2024-01-15T10:30:00Z",
  },
];

export default function RAGSearchPage() {
  const { t } = useTranslation();

  const [selectedProject, setSelectedProject] = useState<string>("1");
  const [searchQuery, setSearchQuery] = useState("");
  const [searchType, setSearchType] = useState<string>("hybrid");
  const [isSearching, setIsSearching] = useState(false);
  const [selectedSearch, setSelectedSearch] = useState<SearchQuery | null>(
    mockSearchHistory[0]
  );
  const [showFilters, setShowFilters] = useState(false);
  const [filters, setFilters] = useState({
    chunkTypes: [] as string[],
    sources: [] as string[],
    tags: [] as string[],
    minScore: 0.5,
  });

  const selectedRAGProject = mockRAGProjects.find(
    (p) => p.id === selectedProject
  );

  const handleSearch = async () => {
    if (!searchQuery.trim() || isSearching) return;

    setIsSearching(true);
    // Simulate API call
    setTimeout(() => {
      const newSearch: SearchQuery = {
        id: Date.now().toString(),
        query: searchQuery,
        searchType: searchType as any,
        projectId: selectedProject,
        results: mockSearchHistory[0].results, // Reuse mock data
        metrics: {
          totalResults: 6,
          averageScore: 0.82,
          processingTime: 950,
          rerankTime: 250,
        },
        filters: {
          chunkTypes: filters.chunkTypes,
          sources: filters.sources,
          tags: filters.tags,
        },
        createdAt: new Date().toISOString(),
      };
      setSelectedSearch(newSearch);
      setIsSearching(false);
    }, 2000);
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "active":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      case "inactive":
        return <Database className="h-4 w-4 text-gray-500" />;
      case "error":
        return <Database className="h-4 w-4 text-red-500" />;
      default:
        return <Database className="h-4 w-4 text-gray-500" />;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300";
      case "inactive":
        return "bg-gray-100 text-gray-800 dark:bg-gray-500/20 dark:text-gray-300";
      case "error":
        return "bg-red-100 text-red-800 dark:bg-red-500/20 dark:text-red-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-500/20 dark:text-gray-300";
    }
  };

  const getChunkTypeIcon = (type: string) => {
    switch (type) {
      case "INTRODUCTION":
        return <FileText className="h-4 w-4 text-blue-500" />;
      case "MAIN_CONTENT":
        return <Brain className="h-4 w-4 text-green-500" />;
      case "CONCLUSION":
        return <Target className="h-4 w-4 text-purple-500" />;
      case "INSTRUCTIONS":
        return <Settings className="h-4 w-4 text-orange-500" />;
      default:
        return <FileText className="h-4 w-4 text-gray-500" />;
    }
  };

  const getSourceIcon = (type: string) => {
    switch (type) {
      case "document":
        return <FileText className="h-4 w-4 text-blue-500" />;
      case "webscraping":
        return <Globe className="h-4 w-4 text-green-500" />;
      case "datasource":
        return <Database className="h-4 w-4 text-purple-500" />;
      default:
        return <FileText className="h-4 w-4 text-gray-500" />;
    }
  };

  const getSearchTypeColor = (type: string) => {
    switch (type) {
      case "vector":
        return "bg-blue-100 text-blue-800 dark:bg-blue-500/20 dark:text-blue-300";
      case "textual":
        return "bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300";
      case "hybrid":
        return "bg-purple-100 text-purple-800 dark:bg-purple-500/20 dark:text-purple-300";
      case "filtered":
        return "bg-orange-100 text-orange-800 dark:bg-orange-500/20 dark:text-orange-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-500/20 dark:text-gray-300";
    }
  };

  const getScoreColor = (score: number) => {
    if (score >= 0.8) return "text-green-600 dark:text-green-400";
    if (score >= 0.6) return "text-yellow-600 dark:text-yellow-400";
    return "text-red-600 dark:text-red-400";
  };

  return (
    <div className="space-y-4">
      {/* Header - Título alineado a la izquierda */}
      <div className="flex items-center justify-between">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <Search className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("rag.search.title", "Búsqueda Semántica")}
            </h1>
            <HelpTooltip helpKey="rag.help.search" />
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t(
              "rag.search.subtitle",
              "Búsqueda avanzada con embeddings y reranking"
            )}
          </p>
        </div>
        <Button
          onClick={() => setShowFilters(!showFilters)}
          variant="outline"
          className="border-orange-200 text-orange-700 hover:bg-orange-50 dark:border-orange-800 dark:text-orange-300 dark:hover:bg-orange-900/20"
        >
          <Filter className="h-4 w-4 mr-2" />
          {t("rag.search.filters", "Filtros")}
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-4">
        {/* Panel de Configuración */}
        <div className="lg:col-span-1">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-base font-medium">
                <Settings className="h-5 w-5 text-blue-600" />
                {t("rag.search.configuration", "Configuración")}
              </CardTitle>
            </CardHeader>
            <CardBody>

              {/* Selector de Proyecto */}
              <div className="space-y-2 mb-6">
                <Label className="text-sm font-medium text-foreground">
                  {t("rag.search.selectProject", "Proyecto RAG")}
                </Label>
                <Select
                  value={selectedProject}
                  onValueChange={setSelectedProject}
                >
                  <SelectTrigger className="bg-background/50 border-border">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {mockRAGProjects.map((project) => (
                      <SelectItem key={project.id} value={project.id}>
                        <div className="flex items-center space-x-2">
                          {getStatusIcon(project.status)}
                          <span>{project.name}</span>
                        </div>
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Información del Proyecto */}
              {selectedRAGProject && (
                <div className="space-y-4">
                  <div className="p-4 bg-muted/30 rounded-lg border border-border">
                    <div className="flex items-center justify-between mb-2">
                      <h4 className="font-semibold text-foreground">
                        {selectedRAGProject.name}
                      </h4>
                      <Badge
                        variant="outline"
                        className={getStatusColor(selectedRAGProject.status)}
                      >
                        {selectedRAGProject.status}
                      </Badge>
                    </div>
                    <p className="text-sm text-muted-foreground mb-3">
                      {selectedRAGProject.description}
                    </p>
                    <div className="grid grid-cols-2 gap-2 text-xs">
                      <div className="text-center p-2 bg-background/50 rounded">
                        <div className="font-semibold text-foreground">
                          {selectedRAGProject.totalChunks.toLocaleString()}
                        </div>
                        <div className="text-muted-foreground">
                          {t("rag.search.chunks", "Chunks")}
                        </div>
                      </div>
                      <div className="text-center p-2 bg-background/50 rounded">
                        <div className="font-semibold text-foreground">
                          {selectedRAGProject.totalSearches}
                        </div>
                        <div className="text-muted-foreground">
                          {t("rag.search.searches", "Búsquedas")}
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Modelos Asignados */}
                  <div className="space-y-3">
                    <h5 className="font-medium text-foreground">
                      {t("rag.search.assignedModels", "Modelos Asignados")}
                    </h5>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between p-2 bg-background/50 rounded border border-border">
                        <div className="flex items-center space-x-2">
                          <Database className="h-4 w-4 text-blue-500" />
                          <span className="text-sm text-foreground">
                            {t("rag.search.embedding", "Embedding")}
                          </span>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          {selectedRAGProject.embeddingModel}
                        </span>
                      </div>
                      {selectedRAGProject.rerankerModel && (
                        <div className="flex items-center justify-between p-2 bg-background/50 rounded border border-border">
                          <div className="flex items-center space-x-2">
                            <Zap className="h-4 w-4 text-purple-500" />
                            <span className="text-sm text-foreground">
                              {t("rag.search.reranker", "Reranker")}
                            </span>
                          </div>
                          <span className="text-xs text-muted-foreground">
                            {selectedRAGProject.rerankerModel}
                          </span>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              )}
            </CardBody>
          </Card>
        </div>

        {/* Área Principal */}
        <div className="lg:col-span-3">
          <div className="space-y-4">
            {/* Panel de Búsqueda */}
            <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-base font-medium">
                  <Search className="h-5 w-5 text-blue-600" />
                  {t("rag.search.semanticSearch", "Búsqueda Semántica")}
                </CardTitle>
              </CardHeader>
              <CardBody>

                <div className="space-y-4">
                  {/* Query Input */}
                  <div>
                    <Label className="text-sm font-medium text-foreground">
                      {t("rag.search.searchQuery", "Consulta de Búsqueda")}
                    </Label>
                    <Textarea
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                      placeholder={t(
                        "rag.search.queryPlaceholder",
                        "Escribe tu consulta para buscar en el conocimiento..."
                      )}
                      className="mt-2 bg-background/50 border-border focus:border-blue-500 focus:ring-blue-500"
                      rows={3}
                    />
                  </div>

                  {/* Search Type */}
                  <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
                    <div>
                      <Label className="text-sm font-medium text-foreground">
                        {t("rag.search.searchType", "Tipo de Búsqueda")}
                      </Label>
                      <Select value={searchType} onValueChange={setSearchType}>
                        <SelectTrigger className="bg-background/50 border-border">
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="vector">
                            {t("rag.help.searchTypes.vector", "Vectorial")}
                          </SelectItem>
                          <SelectItem value="textual">
                            {t("rag.help.searchTypes.textual", "Textual")}
                          </SelectItem>
                          <SelectItem value="hybrid">
                            {t("rag.help.searchTypes.hybrid", "Híbrida")}
                          </SelectItem>
                          <SelectItem value="filtered">
                            {t("rag.help.searchTypes.filtered", "Filtrada")}
                          </SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                  </div>

                  {/* Action Buttons */}
                  <div className="flex items-center space-x-4">
                    <Button
                      onClick={handleSearch}
                      disabled={!searchQuery.trim() || isSearching}
                      className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white"
                    >
                      {isSearching ? (
                        <>
                          <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                          {t("rag.search.searching", "Buscando...")}
                        </>
                      ) : (
                        <>
                          <Play className="h-4 w-4 mr-2" />
                          {t("rag.search.search", "Buscar")}
                        </>
                      )}
                    </Button>
                    <Button
                      variant="outline"
                      onClick={() => setSearchQuery("")}
                      disabled={isSearching}
                    >
                      <RefreshCw className="h-4 w-4 mr-2" />
                      {t("rag.search.clear", "Limpiar")}
                    </Button>
                  </div>
                </div>
              </CardBody>
            </Card>

            {/* Resultados de la Búsqueda */}
            {selectedSearch && (
              <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-lg transition-all">
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <CardTitle className="flex items-center gap-2 text-base font-medium">
                      <TrendingUp className="h-5 w-5 text-green-600" />
                      {t(
                        "rag.search.searchResults",
                        "Resultados de la Búsqueda"
                      )}
                    </CardTitle>
                    <div className="flex items-center space-x-2">
                      <Badge
                        variant="outline"
                        className={getSearchTypeColor(
                          selectedSearch.searchType
                        )}
                      >
                        {selectedSearch.searchType}
                      </Badge>
                      <Badge variant="outline" className="text-xs">
                        {new Date(selectedSearch.createdAt).toLocaleString()}
                      </Badge>
                    </div>
                  </div>
                </CardHeader>
                <CardBody>
                  {/* Métricas */}
                  <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
                    <div className="text-center p-4 bg-muted/30 rounded-lg border border-border">
                      <div className="text-2xl font-bold text-foreground">
                        {selectedSearch.metrics.totalResults}
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {t("rag.search.totalResults", "Resultados")}
                      </div>
                    </div>
                    <div className="text-center p-4 bg-muted/30 rounded-lg border border-border">
                      <div className="text-2xl font-bold text-blue-600">
                        {(selectedSearch.metrics.averageScore * 100).toFixed(1)}
                        %
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {t("rag.search.avgScore", "Score Promedio")}
                      </div>
                    </div>
                    <div className="text-center p-4 bg-muted/30 rounded-lg border border-border">
                      <div className="text-2xl font-bold text-green-600">
                        {selectedSearch.metrics.processingTime}ms
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {t("rag.search.processingTime", "Tiempo")}
                      </div>
                    </div>
                    {selectedSearch.metrics.rerankTime && (
                      <div className="text-center p-4 bg-muted/30 rounded-lg border border-border">
                        <div className="text-2xl font-bold text-purple-600">
                          {selectedSearch.metrics.rerankTime}ms
                        </div>
                        <div className="text-sm text-muted-foreground">
                          {t("rag.search.rerankTime", "Rerank")}
                        </div>
                      </div>
                    )}
                  </div>

                  {/* Resultados Detallados */}
                  <div className="space-y-4">
                    <h4 className="font-semibold text-foreground">
                      {t("rag.search.rankedResults", "Resultados Rankeados")}
                    </h4>
                    {selectedSearch.results.map((result, index) => (
                      <div
                        key={result.id}
                        className="p-4 bg-background/50 rounded-lg border border-border"
                      >
                        <div className="flex items-start justify-between mb-3">
                          <div className="flex items-center space-x-3">
                            <div className="flex items-center space-x-2">
                              <span className="text-lg font-bold text-foreground">
                                #{index + 1}
                              </span>
                              {getChunkTypeIcon(result.chunkType)}
                              <Badge variant="outline" className="text-xs">
                                {result.chunkType}
                              </Badge>
                            </div>
                          </div>
                          <div className="text-right text-sm">
                            <div
                              className={`font-semibold ${getScoreColor(
                                result.similarityScore
                              )}`}
                            >
                              {(result.similarityScore * 100).toFixed(1)}%
                            </div>
                            {result.rerankScore && (
                              <div
                                className={`text-xs ${getScoreColor(
                                  result.rerankScore
                                )}`}
                              >
                                Rerank: {(result.rerankScore * 100).toFixed(1)}%
                              </div>
                            )}
                          </div>
                        </div>

                        <p className="text-sm text-foreground mb-3 line-clamp-3">
                          {result.chunkText}
                        </p>

                        <div className="flex items-center justify-between">
                          <div className="flex items-center space-x-4 text-xs text-muted-foreground">
                            <div className="flex items-center space-x-1">
                              {getSourceIcon(result.source.type)}
                              <span>{result.source.name}</span>
                            </div>
                            <span>
                              {result.metadata.keywords.slice(0, 3).join(", ")}
                            </span>
                          </div>
                          <div className="flex items-center space-x-2">
                            {result.metadata.autoTags
                              .slice(0, 2)
                              .map((tag, tagIndex) => (
                                <Badge
                                  key={tagIndex}
                                  variant="outline"
                                  className="text-xs"
                                >
                                  {tag}
                                </Badge>
                              ))}
                            {result.metadata.customTags.length > 0 && (
                              <Badge
                                variant="outline"
                                className="text-xs bg-blue-100 text-blue-800"
                              >
                                +{result.metadata.customTags.length}
                              </Badge>
                            )}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardBody>
              </Card>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}


