"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import HelpTooltip from "@/components/ui/help-tooltip";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Edit, Hash, Plus, Save, Search, Settings, Tag, X } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

// Mock data para proyectos RAG
const mockProjects = [
  {
    id: 1,
    name: "DemoRAGProject",
    description: "Proyecto RAG de demostración",
    status: "active",
    totalChunks: 156,
  },
  {
    id: 2,
    name: "KnowledgeBase",
    description: "Base de conocimiento corporativa",
    status: "active",
    totalChunks: 234,
  },
];

// Mock data para chunks
const mockChunks = [
  {
    id: 1,
    projectId: 1,
    content:
      "El sistema de autenticación utiliza JWT tokens para validar la identidad de los usuarios. Los tokens se generan con una clave secreta y tienen una duración limitada para mayor seguridad.",
    size: 156,
    type: "text",
    autoTags: ["autenticación", "JWT", "seguridad", "tokens"],
    manualTags: ["documentación", "API"],
    createdAt: "2024-01-20T10:30:00Z",
    source: "auth-guide.pdf",
    relevance: 0.92,
  },
  {
    id: 2,
    projectId: 1,
    content:
      "La configuración de la base de datos requiere establecer conexiones seguras mediante SSL y configurar pools de conexión para optimizar el rendimiento.",
    size: 134,
    type: "text",
    autoTags: ["base de datos", "SSL", "conexiones", "rendimiento"],
    manualTags: ["configuración"],
    createdAt: "2024-01-19T15:45:00Z",
    source: "db-config.md",
    relevance: 0.87,
  },
  {
    id: 3,
    projectId: 1,
    content:
      "Los microservicios se comunican a través de APIs REST y utilizan circuit breakers para manejar fallos de manera resiliente.",
    size: 98,
    type: "text",
    autoTags: ["microservicios", "APIs", "REST", "circuit breakers"],
    manualTags: ["arquitectura", "resiliencia"],
    createdAt: "2024-01-18T09:20:00Z",
    source: "microservices-arch.docx",
    relevance: 0.89,
  },
];

export default function ChunksPage() {
  const { t } = useTranslation();
  const router = useRouter();

  const [selectedProject, setSelectedProject] = useState(mockProjects[0]);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedType, setSelectedType] = useState("all");
  const [editingChunk, setEditingChunk] = useState<number | null>(null);
  const [newTag, setNewTag] = useState("");
  const [chunks, setChunks] = useState(mockChunks);

  const handleAddTag = (chunkId: number) => {
    if (!newTag.trim()) return;

    setChunks((prev) =>
      prev.map((chunk) =>
        chunk.id === chunkId
          ? { ...chunk, manualTags: [...chunk.manualTags, newTag.trim()] }
          : chunk
      )
    );
    setNewTag("");
  };

  const handleRemoveTag = (chunkId: number, tag: string, isAuto: boolean) => {
    setChunks((prev) =>
      prev.map((chunk) =>
        chunk.id === chunkId
          ? {
              ...chunk,
              [isAuto ? "autoTags" : "manualTags"]: chunk[
                isAuto ? "autoTags" : "manualTags"
              ].filter((t) => t !== tag),
            }
          : chunk
      )
    );
  };

  const handleEditTag = (
    chunkId: number,
    oldTag: string,
    newTagValue: string,
    isAuto: boolean
  ) => {
    if (!newTagValue.trim()) return;

    setChunks((prev) =>
      prev.map((chunk) =>
        chunk.id === chunkId
          ? {
              ...chunk,
              [isAuto ? "autoTags" : "manualTags"]: chunk[
                isAuto ? "autoTags" : "manualTags"
              ].map((t) => (t === oldTag ? newTagValue.trim() : t)),
            }
          : chunk
      )
    );
  };

  const getChunkTypeColor = (type: string) => {
    switch (type) {
      case "text":
        return "bg-blue-100 text-blue-800 dark:bg-blue-500/20 dark:text-blue-300";
      case "code":
        return "bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300";
      case "table":
        return "bg-purple-100 text-purple-800 dark:bg-purple-500/20 dark:text-purple-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-500/20 dark:text-gray-300";
    }
  };

  const filteredChunks = chunks.filter((chunk) => {
    const matchesProject = chunk.projectId === selectedProject.id;
    const matchesType = selectedType === "all" || chunk.type === selectedType;
    const matchesSearch =
      searchQuery === "" ||
      chunk.content.toLowerCase().includes(searchQuery.toLowerCase()) ||
      [...chunk.autoTags, ...chunk.manualTags].some((tag) =>
        tag.toLowerCase().includes(searchQuery.toLowerCase())
      );

    return matchesProject && matchesType && matchesSearch;
  });

  return (
    <div className="min-h-screen bg-background transition-all duration-500 relative overflow-hidden">

      {/* Partículas flotantes de fondo */}
      <div className="absolute inset-0 pointer-events-none">
        <div
          className="absolute top-20 left-10 w-2 h-2 bg-blue-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "0s" }}
        />
        <div
          className="absolute top-40 right-20 w-1 h-1 bg-green-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "1s" }}
        />
        <div
          className="absolute top-60 left-1/4 w-1.5 h-1.5 bg-purple-400/20 rounded-full animate-pulse"
          style={{ animationDelay: "2s" }}
        />
      </div>

      <div className="p-6 space-y-6 relative z-10">
        {/* Development Banner */}
        <div className="backdrop-blur-md bg-white/10 dark:bg-black/10 rounded-xl border border-white/20 dark:border-black/20 shadow-xl">
          <DevelopmentBanner
            type="operational"
            customText="✅ Versión 1.0.0 - Operativa"
            showEarlyAdopterButton={false}
            className="justify-start"
          />
        </div>

        {/* Header */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-3">
              <div>
                <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                  <Settings className="w-8 h-8 text-blue-500" />
                  {t("rag.chunks.title", "Chunks")}
                </h1>
                <p className="text-muted-foreground text-lg">
                  {t(
                    "rag.chunks.subtitle",
                    "Visualiza y gestiona los chunks de documentos"
                  )}
                </p>
              </div>
              <HelpTooltip helpKey="rag.help.chunks" />
            </div>
          </div>

          {/* Botones de acción en el header */}
          <div className="flex gap-3">
            <Button
              type="button"
              variant="outline"
              onClick={() => (window.location.href = "/rag")}
              className="transition-all duration-300 hover:scale-105"
            >
              {t("rag.chunks.cancel", "Cancelar")}
            </Button>
            <Button
              type="button"
              className="transition-all duration-300 hover:scale-105"
            >
              <Save className="w-4 h-4 mr-2" />
              {t("rag.chunks.saveChanges", "Guardar Cambios")}
            </Button>
          </div>
        </div>

        <div className="space-y-6">
          {/* Filtros */}
          <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
            <div className="flex items-center space-x-3 mb-6">
              <Settings className="h-5 w-5 text-blue-600" />
              <h3 className="text-lg font-semibold text-foreground">
                {t("rag.chunks.filters", "Filtros")}
              </h3>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="space-y-2">
                <Label htmlFor="project">
                  {t("rag.chunks.project", "Proyecto RAG")}
                </Label>
                <Select
                  value={selectedProject.id.toString()}
                  onValueChange={(value) => {
                    const project = mockProjects.find(
                      (p) => p.id.toString() === value
                    );
                    if (project) setSelectedProject(project);
                  }}
                >
                  <SelectTrigger className="bg-background/50">
                    <SelectValue
                      placeholder={t("rag.chunks.selectProject", "Seleccionar proyecto")}
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {mockProjects.map((project) => (
                      <SelectItem
                        key={project.id}
                        value={project.id.toString()}
                      >
                        {project.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="type">
                  {t("rag.chunks.chunkType", "Tipo de Chunk")}
                </Label>
                <Select value={selectedType} onValueChange={setSelectedType}>
                  <SelectTrigger className="bg-background/50">
                    <SelectValue
                      placeholder={t(
                        "rag.chunks.selectType",
                        "Seleccionar tipo"
                      )}
                    />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">Todos</SelectItem>
                    <SelectItem value="text">Texto</SelectItem>
                    <SelectItem value="code">Código</SelectItem>
                    <SelectItem value="table">Tabla</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="search">
                  {t("rag.chunks.searchChunks", "Buscar Chunks")}
                </Label>
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    id="search"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder={t(
                      "rag.chunks.searchPlaceholder",
                      "Buscar por contenido o tags..."
                    )}
                    className="pl-10 bg-background/50"
                  />
                </div>
              </div>
            </div>
          </Card>

          {/* Estadísticas */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    {t("rag.chunks.totalChunks", "Total Chunks")}
                  </p>
                  <p className="text-2xl font-bold text-foreground">
                    {filteredChunks.length}
                  </p>
                </div>
                <div className="p-3 bg-blue-100 dark:bg-blue-500/20 rounded-full">
                  <Hash className="h-6 w-6 text-blue-600 dark:text-blue-400" />
                </div>
              </div>
            </Card>
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    {t("rag.chunks.avgSize", "Tamaño Promedio")}
                  </p>
                  <p className="text-2xl font-bold text-foreground">
                    {Math.round(
                      filteredChunks.reduce(
                        (acc, chunk) => acc + chunk.size,
                        0
                      ) / filteredChunks.length || 0
                    )}{" "}
                    chars
                  </p>
                </div>
                <div className="p-3 bg-green-100 dark:bg-green-500/20 rounded-full">
                  <Settings className="h-6 w-6 text-green-600 dark:text-green-400" />
                </div>
              </div>
            </Card>
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    {t("rag.chunks.autoTags", "Auto Tags")}
                  </p>
                  <p className="text-2xl font-bold text-foreground">
                    {filteredChunks.reduce(
                      (acc, chunk) => acc + chunk.autoTags.length,
                      0
                    )}
                  </p>
                </div>
                <div className="p-3 bg-purple-100 dark:bg-purple-500/20 rounded-full">
                  <Tag className="h-6 w-6 text-purple-600 dark:text-purple-400" />
                </div>
              </div>
            </Card>
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">
                    Tags Manuales
                  </p>
                  <p className="text-2xl font-bold text-foreground">
                    {filteredChunks.reduce(
                      (acc, chunk) => acc + chunk.manualTags.length,
                      0
                    )}
                  </p>
                </div>
                <div className="p-3 bg-orange-100 dark:bg-orange-500/20 rounded-full">
                  <Edit className="h-6 w-6 text-orange-600 dark:text-orange-400" />
                </div>
              </div>
            </Card>
          </div>

          {/* Lista de Chunks */}
          <div className="space-y-4">
            {filteredChunks.map((chunk) => (
              <Card
                key={chunk.id}
                className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg"
              >
                <div className="flex items-start justify-between mb-4">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <Badge
                        variant="outline"
                        className={getChunkTypeColor(chunk.type)}
                      >
                        {chunk.type}
                      </Badge>
                      <span className="text-sm text-muted-foreground">
                        {chunk.size} {t("rag.chunks.characters", "caracteres")}
                      </span>
                      <span className="text-sm text-muted-foreground">
                        {t("rag.chunks.relevance", "Relevancia")}:{" "}
                        {(chunk.relevance * 100).toFixed(1)}%
                      </span>
                    </div>
                    <p className="text-foreground mb-3 line-clamp-3">
                      {chunk.content}
                    </p>
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                      <span>
                        {t("rag.chunks.source", "Fuente")}: {chunk.source}
                      </span>
                      <span>•</span>
                      <span>
                        {t("rag.chunks.createdAt", "Creado")}:{" "}
                        {new Date(chunk.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() =>
                      setEditingChunk(
                        editingChunk === chunk.id ? null : chunk.id
                      )
                    }
                  >
                    {editingChunk === chunk.id ? (
                      <X className="h-4 w-4" />
                    ) : (
                      <Edit className="h-4 w-4" />
                    )}
                  </Button>
                </div>

                {/* Tags */}
                <div className="space-y-4">
                  {/* Auto Tags */}
                  <div>
                    <h4 className="text-sm font-medium text-foreground mb-2 flex items-center gap-2">
                      <Tag className="h-4 w-4" />
                      {t("rag.chunks.autoTags", "Auto Tags")}
                    </h4>
                    <div className="flex flex-wrap gap-2">
                      {chunk.autoTags.map((tag, index) => (
                        <div key={index} className="flex items-center gap-1">
                          <Badge
                            variant="outline"
                            className="bg-blue-100 text-blue-800 dark:bg-blue-500/20 dark:text-blue-300"
                          >
                            {editingChunk === chunk.id ? (
                              <div className="flex items-center gap-1">
                                <Input
                                  defaultValue={tag}
                                  className="h-5 w-20 text-xs"
                                  onBlur={(e) =>
                                    handleEditTag(
                                      chunk.id,
                                      tag,
                                      e.target.value,
                                      true
                                    )
                                  }
                                />
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-4 w-4 p-0"
                                  onClick={() =>
                                    handleRemoveTag(chunk.id, tag, true)
                                  }
                                >
                                  <X className="h-3 w-3" />
                                </Button>
                              </div>
                            ) : (
                              tag
                            )}
                          </Badge>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Manual Tags */}
                  <div>
                    <h4 className="text-sm font-medium text-foreground mb-2 flex items-center gap-2">
                      <Edit className="h-4 w-4" />
                      {t("rag.chunks.manualTags", "Tags Manuales")}
                    </h4>
                    <div className="flex flex-wrap gap-2 mb-2">
                      {chunk.manualTags.map((tag, index) => (
                        <div key={index} className="flex items-center gap-1">
                          <Badge
                            variant="outline"
                            className="bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300"
                          >
                            {editingChunk === chunk.id ? (
                              <div className="flex items-center gap-1">
                                <Input
                                  defaultValue={tag}
                                  className="h-5 w-20 text-xs"
                                  onBlur={(e) =>
                                    handleEditTag(
                                      chunk.id,
                                      tag,
                                      e.target.value,
                                      false
                                    )
                                  }
                                />
                                <Button
                                  variant="ghost"
                                  size="sm"
                                  className="h-4 w-4 p-0"
                                  onClick={() =>
                                    handleRemoveTag(chunk.id, tag, false)
                                  }
                                >
                                  <X className="h-3 w-3" />
                                </Button>
                              </div>
                            ) : (
                              tag
                            )}
                          </Badge>
                        </div>
                      ))}
                    </div>
                    {editingChunk === chunk.id && (
                      <div className="flex items-center gap-2">
                        <Input
                          value={newTag}
                          onChange={(e) => setNewTag(e.target.value)}
                          placeholder={t("rag.chunks.newTag", "Nuevo tag...")}
                          className="h-8 text-sm"
                          onKeyPress={(e) =>
                            e.key === "Enter" && handleAddTag(chunk.id)
                          }
                        />
                        <Button
                          size="sm"
                          onClick={() => handleAddTag(chunk.id)}
                          disabled={!newTag.trim()}
                        >
                          <Plus className="h-4 w-4" />
                        </Button>
                      </div>
                    )}
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
