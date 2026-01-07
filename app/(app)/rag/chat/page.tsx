"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Bot,
  Brain,
  CheckCircle,
  Clock,
  Database,
  FileText,
  Globe,
  Plus,
  Send,
  Settings,
  User,
  XCircle,
  Zap,
} from "lucide-react";
import { useEffect, useRef, useState } from "react";

// Interfaces
interface ChatMessage {
  id: string;
  type: "user" | "assistant" | "system";
  content: string;
  timestamp: string;
  metadata?: {
    sources?: Array<{
      id: string;
      title: string;
      url?: string;
      type: "document" | "web" | "database";
      relevance: number;
    }>;
    model?: string;
    tokens?: number;
    cost?: number;
    responseTime?: number;
  };
}

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
  {
    id: "3",
    name: "Soporte Técnico",
    description:
      "Sistema de soporte técnico con documentación y troubleshooting",
    status: "active",
    embeddingModel: "text-embedding-ada-002",
    llmModel: "gpt-4-turbo",
    totalChunks: 2100,
    totalSearches: 567,
    lastUsed: "2024-01-15T11:15:00Z",
  },
];

const mockMessages: ChatMessage[] = [
  {
    id: "1",
    type: "user",
    content: "¿Qué propiedades tiene la manzanilla para el sistema digestivo?",
    timestamp: "2024-01-15T10:00:00Z",
  },
  {
    id: "2",
    type: "assistant",
    content:
      "La manzanilla (Matricaria chamomilla) tiene múltiples propiedades beneficiosas para el sistema digestivo:\n\n**Propiedades principales:**\n- **Antiinflamatoria**: Reduce la inflamación del tracto digestivo\n- **Antiespasmódica**: Alivia los espasmos musculares del estómago e intestinos\n- **Carminativa**: Ayuda a expulsar gases intestinales\n- **Digestiva**: Estimula la producción de jugos gástricos\n\n**Beneficios específicos:**\n- Alivia la indigestión y la acidez estomacal\n- Reduce los síntomas del síndrome del intestino irritable\n- Calma las náuseas y los vómitos\n- Mejora la digestión después de comidas pesadas",
    timestamp: "2024-01-15T10:00:15Z",
    metadata: {
      sources: [
        {
          id: "1",
          title: "Manual de Fitoterapia - Capítulo 3: Plantas Digestivas",
          type: "document",
          relevance: 0.95,
        },
        {
          id: "2",
          title: "Estudios clínicos sobre manzanilla y digestión",
          type: "document",
          relevance: 0.88,
        },
      ],
      model: "gpt-4-turbo",
      tokens: 245,
      cost: 0.002,
      responseTime: 1200,
    },
  },
];

export default function RAGChatPage() {
  const { t } = useTranslation();
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const [selectedProject, setSelectedProject] = useState<string>("1");
  const [messages, setMessages] = useState<ChatMessage[]>(mockMessages);
  const [newMessage, setNewMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Auto-scroll to bottom
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const selectedRAGProject = mockRAGProjects.find(
    (p) => p.id === selectedProject
  );

  const handleSendMessage = async () => {
    if (!newMessage.trim() || isLoading) return;

    const userMessage: ChatMessage = {
      id: Date.now().toString(),
      type: "user",
      content: newMessage,
      timestamp: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setNewMessage("");
    setIsLoading(true);

    // Simulate API call
    setTimeout(() => {
      const assistantMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        type: "assistant",
        content:
          "Esta es una respuesta simulada del chatbot RAG. En una implementación real, aquí se realizaría la búsqueda semántica en los chunks del proyecto y se generaría una respuesta contextualizada usando el modelo LLM asignado.",
        timestamp: new Date().toISOString(),
        metadata: {
          sources: [
            {
              id: "1",
              title: "Documento de ejemplo",
              type: "document",
              relevance: 0.85,
            },
          ],
          model: selectedRAGProject?.llmModel || "gpt-4-turbo",
          tokens: 150,
          cost: 0.001,
          responseTime: 800,
        },
      };

      setMessages((prev) => [...prev, assistantMessage]);
      setIsLoading(false);
    }, 1500);
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "active":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      case "inactive":
        return <Clock className="h-4 w-4 text-gray-500" />;
      case "error":
        return <XCircle className="h-4 w-4 text-red-500" />;
      default:
        return <Clock className="h-4 w-4 text-gray-500" />;
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

  const formatTimestamp = (timestamp: string) => {
    return new Date(timestamp).toLocaleTimeString("es-ES", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="w-full">

      {/* Header */}
      <div className="bg-card/50 backdrop-blur-md border-b border-border sticky top-0 z-10 -mx-6 -mt-4 mb-8">
        <div className="w-full px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center space-x-4">
              <div>
                <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                  <Settings className="w-8 h-8 text-blue-500" />
                  {t("rag.chat.title", "Chat RAG")}
                </h1>
                <p className="text-muted-foreground text-lg">
                  {t(
                    "rag.chat.subtitle",
                    "Conversación inteligente con recuperación aumentada"
                  )}
                </p>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <Button
                onClick={() => setMessages([])}
                variant="outline"
                className="border-orange-200 text-orange-700 hover:bg-orange-50 dark:border-orange-800 dark:text-orange-300 dark:hover:bg-orange-900/20"
              >
                <Settings className="h-4 w-4 mr-2" />
                {t("rag.chat.settings", "Configuración")}
              </Button>
            </div>
          </div>
        </div>
      </div>

      <div className="w-full">
        {/* Development Banner */}
        <DevelopmentBanner
          customText="✅ Versión 1.0.0 - Operativa"
          showEarlyAdopterButton={false}
          className="justify-start mb-8"
        />

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Panel de Proyectos RAG */}
          <div className="lg:col-span-1">
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg">
              <div className="flex items-center space-x-3 mb-6">
                <Brain className="h-5 w-5 text-blue-600" />
                <h3 className="text-lg font-semibold text-foreground">
                  {t("rag.chat.ragProjects", "Proyectos RAG")}
                </h3>
              </div>

              {/* Selector de Proyecto */}
              <div className="space-y-2 mb-6">
                <label className="text-sm font-medium text-foreground">
                  {t("rag.chat.selectProject", "Seleccionar Proyecto")}
                </label>
                <Select
                  value={selectedProject}
                  onValueChange={setSelectedProject}
                >
                  <SelectTrigger className="bg-background/50 border-border">
                    <SelectValue
                      placeholder={t(
                        "rag.chat.selectProject",
                        "Seleccionar Proyecto"
                      )}
                    />
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

              {/* Información del Proyecto Seleccionado */}
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
                          {t("rag.chat.chunks", "Chunks")}
                        </div>
                      </div>
                      <div className="text-center p-2 bg-background/50 rounded">
                        <div className="font-semibold text-foreground">
                          {selectedRAGProject.totalSearches}
                        </div>
                        <div className="text-muted-foreground">
                          {t("rag.chat.searches", "Búsquedas")}
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Modelos Asignados */}
                  <div className="space-y-3">
                    <h5 className="font-medium text-foreground">
                      {t("rag.chat.assignedModels", "Modelos Asignados")}
                    </h5>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between p-2 bg-background/50 rounded border border-border">
                        <div className="flex items-center space-x-2">
                          <Database className="h-4 w-4 text-blue-500" />
                          <span className="text-sm text-foreground">
                            {t("rag.chat.embedding", "Embedding")}
                          </span>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          {selectedRAGProject.embeddingModel}
                        </span>
                      </div>
                      <div className="flex items-center justify-between p-2 bg-background/50 rounded border border-border">
                        <div className="flex items-center space-x-2">
                          <Zap className="h-4 w-4 text-green-500" />
                          <span className="text-sm text-foreground">
                            {t("rag.chat.llm", "LLM")}
                          </span>
                        </div>
                        <span className="text-xs text-muted-foreground">
                          {selectedRAGProject.llmModel}
                        </span>
                      </div>
                      {selectedRAGProject.rerankerModel && (
                        <div className="flex items-center justify-between p-2 bg-background/50 rounded border border-border">
                          <div className="flex items-center space-x-2">
                            <Brain className="h-4 w-4 text-purple-500" />
                            <span className="text-sm text-foreground">
                              {t("rag.chat.reranker", "Reranker")}
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
            </Card>
          </div>

          {/* Área de Chat Principal */}
          <div className="lg:col-span-3">
            <Card className="p-6 bg-card/50 backdrop-blur-md border border-border shadow-lg h-[600px] flex flex-col">
              {/* Header del Chat */}
              <div className="flex items-center justify-between mb-6 pb-4 border-b border-border">
                <div>
                  <h3 className="text-lg font-semibold text-foreground">
                    {t("rag.chat.conversation", "Conversación")}
                  </h3>
                  <p className="text-sm text-muted-foreground">
                    {selectedRAGProject?.name} • {messages.length}{" "}
                    {t("rag.chat.messages", "mensajes")}
                  </p>
                </div>
                <Button
                  onClick={() => setMessages([])}
                  variant="outline"
                  size="sm"
                  className="text-muted-foreground hover:text-foreground"
                >
                  <Plus className="h-4 w-4 mr-2" />
                  {t("rag.chat.newChat", "Nueva Conversación")}
                </Button>
              </div>

              {/* Mensajes */}
              <div className="flex-1 overflow-y-auto space-y-4 mb-6">
                {messages.map((message) => (
                  <div
                    key={message.id}
                    className={`flex ${
                      message.type === "user" ? "justify-end" : "justify-start"
                    }`}
                  >
                    <div
                      className={`max-w-3xl ${
                        message.type === "user"
                          ? "bg-gradient-to-r from-blue-600 to-purple-600 text-white"
                          : "bg-muted/30 backdrop-blur-md border border-border"
                      } rounded-lg p-4 shadow-sm`}
                    >
                      <div className="flex items-start space-x-3">
                        <div
                          className={`p-2 rounded-full ${
                            message.type === "user" ? "bg-blue-500" : "bg-muted"
                          }`}
                        >
                          {message.type === "user" ? (
                            <User className="h-4 w-4 text-white" />
                          ) : (
                            <Bot className="h-4 w-4 text-muted-foreground" />
                          )}
                        </div>
                        <div className="flex-1">
                          <div className="whitespace-pre-wrap text-sm">
                            {message.content}
                          </div>

                          {/* Metadata */}
                          {message.metadata && (
                            <div className="mt-3 pt-3 border-t border-border/50">
                              {/* Sources */}
                              {message.metadata.sources &&
                                message.metadata.sources.length > 0 && (
                                  <div className="mb-2">
                                    <p className="text-xs font-medium text-muted-foreground mb-1">
                                      {t("rag.chat.sources", "Fuentes")}:
                                    </p>
                                    <div className="space-y-1">
                                      {message.metadata.sources.map(
                                        (source) => (
                                          <div
                                            key={source.id}
                                            className="flex items-center space-x-2 text-xs"
                                          >
                                            <div
                                              className={`p-1 rounded ${
                                                source.type === "document"
                                                  ? "bg-blue-100 dark:bg-blue-500/20"
                                                  : source.type === "web"
                                                  ? "bg-green-100 dark:bg-green-500/20"
                                                  : "bg-purple-100 dark:bg-purple-500/20"
                                              }`}
                                            >
                                              {source.type === "document" ? (
                                                <FileText className="h-3 w-3" />
                                              ) : source.type === "web" ? (
                                                <Globe className="h-3 w-3" />
                                              ) : (
                                                <Database className="h-3 w-3" />
                                              )}
                                            </div>
                                            <span className="text-muted-foreground">
                                              {source.title}
                                            </span>
                                            <Badge
                                              variant="outline"
                                              className="text-xs"
                                            >
                                              {Math.round(
                                                source.relevance * 100
                                              )}
                                              %
                                            </Badge>
                                          </div>
                                        )
                                      )}
                                    </div>
                                  </div>
                                )}

                              {/* Stats */}
                              <div className="flex items-center space-x-4 text-xs text-muted-foreground">
                                {message.metadata.model && (
                                  <span>
                                    {t("rag.chat.model", "Modelo")}:{" "}
                                    {message.metadata.model}
                                  </span>
                                )}
                                {message.metadata.tokens && (
                                  <span>
                                    {t("rag.chat.tokens", "Tokens")}:{" "}
                                    {message.metadata.tokens}
                                  </span>
                                )}
                                {message.metadata.cost && (
                                  <span>
                                    {t("rag.chat.cost", "Costo")}: $
                                    {message.metadata.cost}
                                  </span>
                                )}
                                {message.metadata.responseTime && (
                                  <span>
                                    {t("rag.chat.responseTime", "Tiempo")}:{" "}
                                    {message.metadata.responseTime}ms
                                  </span>
                                )}
                              </div>
                            </div>
                          )}

                          <div className="text-xs text-muted-foreground mt-2">
                            {formatTimestamp(message.timestamp)}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}

                {/* Loading indicator */}
                {isLoading && (
                  <div className="flex justify-start">
                    <div className="bg-muted/30 backdrop-blur-md border border-border rounded-lg p-4 shadow-sm">
                      <div className="flex items-center space-x-3">
                        <div className="p-2 rounded-full bg-muted">
                          <Bot className="h-4 w-4 text-muted-foreground" />
                        </div>
                        <div className="flex items-center space-x-2">
                          <div className="flex space-x-1">
                            <div className="w-2 h-2 bg-muted-foreground rounded-full animate-bounce"></div>
                            <div
                              className="w-2 h-2 bg-muted-foreground rounded-full animate-bounce"
                              style={{ animationDelay: "0.1s" }}
                            ></div>
                            <div
                              className="w-2 h-2 bg-muted-foreground rounded-full animate-bounce"
                              style={{ animationDelay: "0.2s" }}
                            ></div>
                          </div>
                          <span className="text-sm text-muted-foreground">
                            {t("rag.chat.generating", "Generando respuesta...")}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                )}

                <div ref={messagesEndRef} />
              </div>

              {/* Input Area */}
              <div className="border-t border-border pt-4">
                <div className="flex items-end space-x-3">
                  <div className="flex-1">
                    <Input
                      value={newMessage}
                      onChange={(e) => setNewMessage(e.target.value)}
                      onKeyPress={handleKeyPress}
                      placeholder={t(
                        "rag.chat.inputPlaceholder",
                        "Escribe tu mensaje..."
                      )}
                      disabled={isLoading}
                      className="min-h-[44px] bg-background/50 border-border focus:border-blue-500 focus:ring-blue-500"
                    />
                  </div>
                  <Button
                    onClick={handleSendMessage}
                    disabled={!newMessage.trim() || isLoading}
                    className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white px-6"
                  >
                    <Send className="h-4 w-4" />
                  </Button>
                </div>

                <div className="flex items-center justify-between mt-2 text-xs text-muted-foreground">
                  <span>
                    {t(
                      "rag.chat.inputHelp",
                      "Presiona Enter para enviar, Shift+Enter para nueva línea"
                    )}
                  </span>
                  <div className="flex items-center space-x-4">
                    <span>
                      {t("rag.chat.project", "Proyecto")}:{" "}
                      {selectedRAGProject?.name}
                    </span>
                    <span>
                      {t("rag.chat.model", "Modelo")}:{" "}
                      {selectedRAGProject?.llmModel}
                    </span>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}
