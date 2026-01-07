"use client";
import { useTranslation } from "@/app/config/i18n";
import { usePageTitle } from "@/components/contexts/PageTitleContext";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { CodeEditor } from "@/components/ui/code-editor";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { getPromptyTemplate } from "@/lib/prompty-template";
import { ArrowLeft, BarChart3, Bot, Brain, Cpu, Database, FileCode, Loader2, Save, TrendingUp, X } from "lucide-react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useEffect, useRef, useState } from "react";

interface PromptFormData {
  prmname: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
  prmstatus: string;
  prmversion?: string; // Solo lectura, se muestra pero no se edita
}

export default function PromptRegisterPage() {
  const { t } = useTranslation();
  const { setPageTitle } = usePageTitle();
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [promptId, setPromptId] = useState<number | null>(null);
  const [analyzing, setAnalyzing] = useState(false);
  const [evaluating, setEvaluating] = useState(false);
  const [activeTab, setActiveTab] = useState("form");
  const [versions, setVersions] = useState<any[]>([]);
  const [validations, setValidations] = useState<any[]>([]);
  const originalContentRef = useRef<string>("");
  const [relations, setRelations] = useState<{
    agents: Array<{ id: number; name: string; relationId: number }>;
    models: Array<{ id: number; name: string; relationId: number }>;
    rags: Array<{ id: number; name: string; relationId: number }>;
  }>({
    agents: [],
    models: [],
    rags: [],
  });
  const [analyzeMessage, setAnalyzeMessage] = useState<{ type: 'success' | 'error' | 'info' | null; text: string }>({ type: null, text: '' });
  const [evaluateMessage, setEvaluateMessage] = useState<{ type: 'success' | 'error' | 'info' | null; text: string }>({ type: null, text: '' });
  const [formData, setFormData] = useState<PromptFormData>({
    prmname: "",
    prmdescription: "",
    prmcontent: "",
    prmparameters: "temperature=0.7, max_tokens=1000",
    prmchanges: "",
    prmstatus: "DRAFT",
    prmversion: "1.0.0", // Versión inicial, se generará automáticamente
  });
  const [showTemplate, setShowTemplate] = useState(false);

  // Detectar si es modo edición
  useEffect(() => {
    // Verificar si hay un ID en los searchParams o en la ruta
    const idParam = searchParams.get('id');
    if (idParam) {
      const id = parseInt(idParam);
      if (!isNaN(id)) {
        setIsEditMode(true);
        setPromptId(id);
        loadPromptData(id);
        return;
      }
    }

    // Verificar en el pathname
    const pathParts = pathname.split('/');
    const editIndex = pathParts.findIndex(part => part === 'edit');
    if (editIndex > 0) {
      const id = parseInt(pathParts[editIndex - 1]);
      if (!isNaN(id)) {
        setIsEditMode(true);
        setPromptId(id);
        loadPromptData(id);
        return;
      }
    }

    // Verificar si el pathname tiene un ID directamente después de prompts
    const promptsIndex = pathParts.findIndex(part => part === 'prompts');
    if (promptsIndex >= 0 && pathParts.length > promptsIndex + 1) {
      const possibleId = pathParts[promptsIndex + 1];
      if (possibleId && possibleId !== 'register' && possibleId !== 'testing') {
        const id = parseInt(possibleId);
        if (!isNaN(id)) {
          setIsEditMode(true);
          setPromptId(id);
          loadPromptData(id);
        }
      }
    }
  }, [pathname, searchParams]);

  // Establecer título de la página
  useEffect(() => {
    const title = isEditMode
      ? t("prompts.register.title.edit", "Editar Prompt")
      : t("prompts.register.title", "Registrar Nuevo Prompt");
    document.title = `${title} - CodeflowX`;
    setPageTitle(title);

    return () => {
      setPageTitle(null);
    };
  }, [isEditMode, t, setPageTitle]);

  // Cargar datos del prompt en modo edición
  const loadPromptData = async (id: number) => {
    setIsLoading(true);
    try {
      const response = await fetch(`/api/governance/prompts/${id}`);
      if (response.ok) {
        const prompt = await response.json();
        setFormData({
          prmname: prompt.name || "",
          prmdescription: prompt.description || "",
          prmcontent: prompt.content || "",
          prmparameters: prompt.parameters || "temperature=0.7, max_tokens=1000",
          prmchanges: "",
          prmstatus: prompt.status || "DRAFT",
          prmversion: prompt.version || "1.0.0",
        });
        originalContentRef.current = prompt.content || "";

        // Cargar relaciones
        loadRelations(id);
      }
    } catch (error) {
      console.error("Error al cargar prompt:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // Cargar relaciones del prompt
  const loadRelations = async (id: number) => {
    try {
      const [agentsRes, modelsRes, ragsRes] = await Promise.all([
        fetch(`/api/governance/prompts/${id}/agents`),
        fetch(`/api/governance/prompts/${id}/models`),
        fetch(`/api/governance/prompts/${id}/rags`),
      ]);

      const agents = agentsRes.ok ? await agentsRes.json() : [];
      const models = modelsRes.ok ? await modelsRes.json() : [];
      const rags = ragsRes.ok ? await ragsRes.json() : [];

      setRelations({
        agents: agents.map((r: any) => ({ id: r.relatedId, name: r.relatedName, relationId: r.id })),
        models: models.map((r: any) => ({ id: r.relatedId, name: r.relatedName, relationId: r.id })),
        rags: rags.map((r: any) => ({ id: r.relatedId, name: r.relatedName, relationId: r.id })),
      });
    } catch (error) {
      console.error("Error al cargar relaciones:", error);
    }
  };

  // Desasociar relación
  const handleDisassociate = async (type: 'agents' | 'models' | 'rags', relationId: number, relatedId: number) => {
    if (!promptId) return;

    try {
      const endpoint = type === 'agents' ? 'agents' : type === 'models' ? 'models' : 'rags';
      const response = await fetch(`/api/governance/prompts/${promptId}/${endpoint}?${type === 'agents' ? 'agentId' : type === 'models' ? 'modelId' : 'ragSystemId'}=${relatedId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setRelations(prev => ({
          ...prev,
          [type]: prev[type].filter(r => r.relationId !== relationId),
        }));
      }
    } catch (error) {
      console.error(`Error al desasociar ${type}:`, error);
    }
  };

  // Cargar plantilla Prompty cuando el usuario la solicite
  const loadPromptyTemplate = () => {
    const template = getPromptyTemplate(formData.prmname || "My New Prompt", formData.prmdescription || "A description for my new prompt.");
    setFormData((prev) => ({ ...prev, prmcontent: template }));
    setShowTemplate(true);
  };

  const handleInputChange = (
    field: keyof PromptFormData,
    value: string
  ) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      // Si es modo edición y el contenido cambió, crear nueva versión
      const contentChanged = isEditMode && formData.prmcontent !== originalContentRef.current;

      if (isEditMode && contentChanged) {
        // Crear nueva versión automáticamente
        const response = await fetch(`/api/governance/prompts/${promptId}/versions`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: formData.prmname,
            description: formData.prmdescription,
            content: formData.prmcontent,
            parameters: formData.prmparameters,
            status: formData.prmstatus,
            changes: formData.prmchanges || "Actualización automática: contenido modificado",
          }),
        });

        if (!response.ok) {
          throw new Error("Error al crear nueva versión");
        }
      } else if (isEditMode) {
        // Actualizar sin crear nueva versión (solo metadata)
        const response = await fetch(`/api/governance/prompts/${promptId}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: formData.prmname,
            description: formData.prmdescription,
            parameters: formData.prmparameters,
            status: formData.prmstatus,
          }),
        });

        if (!response.ok) {
          throw new Error("Error al actualizar el prompt");
        }
      } else {
        // Crear nuevo prompt
        const response = await fetch("/api/governance/prompts", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: formData.prmname,
            description: formData.prmdescription,
            content: formData.prmcontent,
            parameters: formData.prmparameters,
            status: formData.prmstatus,
            changes: formData.prmchanges,
          }),
        });

        if (!response.ok) {
          throw new Error("Error al registrar el prompt");
        }
      }

      // Redirigir a la página principal de prompts después de guardar
      router.push("/governance/prompts");
    } catch (error) {
      console.error("Error al guardar prompt:", error);
      alert(t("governance.prompts.register.error", "Error al guardar el prompt"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.back();
  };

  const handleAnalyze = async () => {
    if (!promptId && !formData.prmcontent) {
      alert(t("prompts.register.analyze.noContent", "Debe tener contenido para analizar"));
      return;
    }

    try {
      setAnalyzing(true);

      // Si no hay promptId, es un prompt nuevo - guardar primero o analizar el contenido actual
      const targetId = promptId || "new";

      // Crear validación de análisis completo
      const validationData = {
        prmvalidationtype: "COMPLIANCE",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "Análisis completo de compliance, safety, bias y performance",
        prmissuesfound: 0,
        prmstatus: "ACTIVE",
      };

      // Si es nuevo, mostrar mensaje de que debe guardarse primero
      if (!promptId) {
        alert(t("prompts.register.analyze.saveFirst", "Debe guardar el prompt primero para analizarlo"));
        return;
      }

      const response = await fetch(`/api/prompts/${promptId}/validations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(validationData),
      });

      if (response.ok) {
        alert(t("prompts.register.analyze.success", "Análisis iniciado correctamente. Ver resultados en la página de detalle."));
        // Opcional: redirigir a la página de detalle
        router.push(`/governance/prompts/${promptId}`);
      } else {
        alert(t("prompts.register.analyze.error", "Error al iniciar el análisis"));
      }
    } catch (error) {
      console.error("Error analyzing prompt:", error);
      alert(t("prompts.register.analyze.error", "Error al iniciar el análisis"));
    } finally {
      setAnalyzing(false);
    }
  };

  const handleEvaluate = async () => {
    if (!promptId && !formData.prmcontent) {
      alert(t("prompts.register.evaluate.noContent", "Debe tener contenido para evaluar"));
      return;
    }

    try {
      setEvaluating(true);

      // Si es nuevo, mostrar mensaje de que debe guardarse primero
      if (!promptId) {
        alert(t("prompts.register.evaluate.saveFirst", "Debe guardar el prompt primero para evaluarlo"));
        return;
      }

      // Crear validación de evaluación de performance
      const validationData = {
        prmvalidationtype: "PERFORMANCE",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "Evaluación de performance y métricas del prompt",
        prmissuesfound: 0,
        prmstatus: "ACTIVE",
      };

      const response = await fetch(`/api/prompts/${promptId}/validations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(validationData),
      });

      if (response.ok) {
        alert(t("prompts.register.evaluate.success", "Evaluación iniciada correctamente. Ver resultados en la página de detalle."));
        // Opcional: redirigir a la página de detalle
        router.push(`/governance/prompts/${promptId}`);
      } else {
        alert(t("prompts.register.evaluate.error", "Error al iniciar la evaluación"));
      }
    } catch (error) {
      console.error("Error evaluating prompt:", error);
      alert(t("prompts.register.evaluate.error", "Error al iniciar la evaluación"));
    } finally {
      setEvaluating(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full px-6 py-6 space-y-6 pb-20">
        {/* Header */}
        <div className="space-y-4">
          {/* Primera línea: Título y Subtítulo */}
          <div className="flex items-center gap-3">
            <Brain className="w-6 h-6 text-primary flex-shrink-0" />
            <div className="flex-1">
              <h1 className="text-2xl font-bold text-foreground">
                {isEditMode
                  ? t("prompts.register.title.edit", "Editar Prompt")
                  : t("prompts.register.title", "Registrar Nuevo Prompt")}
              </h1>
              <p className="text-sm text-muted-foreground mt-1">
                {isEditMode
                  ? t("prompts.register.subtitle.edit", "Edita y actualiza el prompt de IA")
                  : t("prompts.register.subtitle", "Crea y registra un nuevo prompt de IA")}
              </p>
            </div>
          </div>

          {/* Segunda línea: Botón Volver (izquierda) + Botones de acción (derecha) */}
          <div className="flex items-center justify-between">
            <Button
              variant="ghost"
              onClick={() => router.back()}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("common.back", "Volver")}
            </Button>
            <div className="flex items-center gap-2">
              {isEditMode && promptId && (
                <>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={handleAnalyze}
                    disabled={analyzing || isSubmitting}
                    className="gap-2"
                  >
                    {analyzing ? (
                      <Loader2 className="w-4 h-4 animate-spin" />
                    ) : (
                      <BarChart3 className="w-4 h-4" />
                    )}
                    {t("prompts.register.analyze", "Analizar")}
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={handleEvaluate}
                    disabled={evaluating || isSubmitting}
                    className="gap-2"
                  >
                    {evaluating ? (
                      <Loader2 className="w-4 h-4 animate-spin" />
                    ) : (
                      <TrendingUp className="w-4 h-4" />
                    )}
                    {t("prompts.register.evaluate", "Evaluar")}
                  </Button>
                </>
              )}
              <Button
                type="button"
                variant="outline"
                onClick={handleCancel}
                disabled={isSubmitting}
              >
                <ArrowLeft className="w-4 h-4 mr-2" />
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                type="button"
                onClick={() => {
                  const mockEvent = { preventDefault: () => {}, stopPropagation: () => {} } as React.FormEvent;
                  handleSubmit(mockEvent);
                }}
                disabled={isSubmitting}
              >
                <Save className="w-4 h-4 mr-2" />
                {isSubmitting
                  ? t("prompts.register.form.saving", "Guardando...")
                  : t("governance.prompts.register.form.save", "Guardar Prompt")}
              </Button>
            </div>
          </div>
        </div>

      {/* Formulario */}
      <form onSubmit={handleSubmit}>
        <Card>
          <CardHeader>
            <CardTitle>
              {t("prompts.register.form.title", "Información del Prompt")}
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {/* Primera fila: Nombre, Versión y Estado */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {/* Nombre */}
              <div className="space-y-2">
                <Label htmlFor="prmname">
                  {t("prompts.register.form.name", "Nombre")}
                </Label>
                <Input
                  id="prmname"
                  value={formData.prmname}
                  onChange={(e) =>
                    handleInputChange("prmname", e.target.value)
                  }
                  placeholder={t(
                    "prompts.register.form.namePlaceholder",
                    "Nombre del prompt"
                  )}
                  required
                />
              </div>

              {/* Versión (solo lectura) */}
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <Label htmlFor="prmversion">
                    {t("prompts.register.form.version", "Versión")}
                  </Label>
                  {isEditMode && (
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      onClick={() => window.location.href = "/governance/prompts/versioning/overview"}
                      className="text-xs h-auto py-1 px-2"
                    >
                      {t("prompts.register.form.viewVersions", "Ver Versiones")}
                    </Button>
                  )}
                </div>
                <Input
                  id="prmversion"
                  value={formData.prmversion || "1.0.0"}
                  readOnly
                  disabled
                  className="bg-muted cursor-not-allowed"
                />
                {isEditMode && formData.prmcontent !== originalContentRef.current && (
                  <p className="text-xs text-amber-600 dark:text-amber-400">
                    {t("prompts.register.form.newVersionWarning", "⚠ Se creará una nueva versión al guardar porque el contenido ha cambiado")}
                  </p>
                )}
              </div>

              {/* Estado */}
              <div className="space-y-2">
                <Label htmlFor="prmstatus">
                  {t("prompts.register.form.status", "Estado")}
                </Label>
                <Select
                  value={formData.prmstatus}
                  onValueChange={(value) => handleInputChange("prmstatus", value)}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="DRAFT">
                      {t("prompts.register.form.status.draft", "Borrador")}
                    </SelectItem>
                    <SelectItem value="ACTIVE">
                      {t("prompts.register.form.status.active", "Activo")}
                    </SelectItem>
                    <SelectItem value="INACTIVE">
                      {t("prompts.register.form.status.inactive", "Inactivo")}
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Segunda fila: Descripción (35%), Cambios (35%), Parámetros (30%) */}
            <div className="flex flex-col md:flex-row gap-4">
              {/* Descripción */}
              <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                <Label htmlFor="prmdescription">
                  {t("prompts.register.form.description", "Descripción")}
                </Label>
                <Textarea
                  id="prmdescription"
                  value={formData.prmdescription}
                  onChange={(e) =>
                    handleInputChange("prmdescription", e.target.value)
                  }
                  placeholder={t(
                    "prompts.register.form.descriptionPlaceholder",
                    "Descripción detallada del prompt"
                  )}
                  rows={3}
                  className="w-full"
                />
              </div>

              {/* Cambios/Notas */}
              <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                <Label htmlFor="prmchanges">
                  {t("prompts.register.form.changes", "Cambios / Notas")}
                </Label>
                <Textarea
                  id="prmchanges"
                  value={formData.prmchanges}
                  onChange={(e) => handleInputChange("prmchanges", e.target.value)}
                  placeholder={t(
                    "prompts.register.form.changesPlaceholder",
                    "Notas sobre los cambios o características de esta versión..."
                  )}
                  rows={3}
                  className="w-full"
                />
              </div>

              {/* Parámetros */}
              <div className="space-y-2 flex-1" style={{ flex: '0 0 30%' }}>
                <Label htmlFor="prmparameters">
                  {t("prompts.register.form.parameters", "Parámetros")}
                </Label>
                <Textarea
                  id="prmparameters"
                  value={formData.prmparameters}
                  onChange={(e) =>
                    handleInputChange("prmparameters", e.target.value)
                  }
                  placeholder="temperature=0.7, max_tokens=1000"
                  rows={3}
                  className="w-full"
                  required
                />
              </div>
            </div>

            {/* Contenido del Prompt con Editor de Código */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <Label htmlFor="prmcontent">
                  {t("prompts.register.form.content", "Contenido del Prompt")}
                </Label>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={loadPromptyTemplate}
                  className="flex items-center gap-2"
                >
                  <FileCode className="w-4 h-4" />
                  {t("prompts.register.form.loadTemplate", "Cargar Plantilla")}
                </Button>
              </div>
              <CodeEditor
                value={formData.prmcontent}
                onChange={(value) => handleInputChange("prmcontent", value || "")}
                language="markdown"
                height="400px"
                placeholder={t(
                    "prompts.register.form.contentPlaceholder",
                  "Escribe el contenido del prompt aquí... Usa el formato Prompty para una estructura profesional."
                )}
              />
            </div>

            {/* Relaciones (solo en modo edición) */}
            {isEditMode && promptId && (
              <div className="space-y-4 pt-4 border-t">
                <div>
                  <h3 className="text-sm font-semibold mb-3">
                    {t("prompts.register.form.relations", "Relaciones")}
                  </h3>

                  {/* Agents */}
                  <div className="space-y-2 mb-4">
                    <div className="flex items-center gap-2">
                      <Bot className="w-4 h-4 text-muted-foreground" />
                      <Label className="text-sm">
                        {t("prompts.register.form.agents", "Agents")}
                      </Label>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {relations.agents.length === 0 ? (
                        <span className="text-sm text-muted-foreground">
                          {t("prompts.register.form.noAgents", "No hay agents asociados")}
                        </span>
                      ) : (
                        relations.agents.map((agent) => (
                          <Badge key={agent.relationId} variant="secondary" className="flex items-center gap-1">
                            {agent.name}
                            <button
                              type="button"
                              onClick={() => handleDisassociate('agents', agent.relationId, agent.id)}
                              className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                            >
                              <X className="w-3 h-3" />
                            </button>
                          </Badge>
                        ))
                      )}
                    </div>
                  </div>

                  {/* Models */}
                  <div className="space-y-2 mb-4">
                    <div className="flex items-center gap-2">
                      <Cpu className="w-4 h-4 text-muted-foreground" />
                      <Label className="text-sm">
                        {t("prompts.register.form.models", "Models")}
                      </Label>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {relations.models.length === 0 ? (
                        <span className="text-sm text-muted-foreground">
                          {t("prompts.register.form.noModels", "No hay models asociados")}
                        </span>
                      ) : (
                        relations.models.map((model) => (
                          <Badge key={model.relationId} variant="secondary" className="flex items-center gap-1">
                            {model.name}
                            <button
                              type="button"
                              onClick={() => handleDisassociate('models', model.relationId, model.id)}
                              className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                            >
                              <X className="w-3 h-3" />
                            </button>
                          </Badge>
                        ))
                      )}
                    </div>
                  </div>

                  {/* RAG Systems */}
                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      <Database className="w-4 h-4 text-muted-foreground" />
                      <Label className="text-sm">
                        {t("prompts.register.form.rags", "RAG Systems")}
                      </Label>
                    </div>
                    <div className="flex flex-wrap gap-2">
                      {relations.rags.length === 0 ? (
                        <span className="text-sm text-muted-foreground">
                          {t("prompts.register.form.noRags", "No hay RAG systems asociados")}
                        </span>
                      ) : (
                        relations.rags.map((rag) => (
                          <Badge key={rag.relationId} variant="secondary" className="flex items-center gap-1">
                            {rag.name}
                            <button
                              type="button"
                              onClick={() => handleDisassociate('rags', rag.relationId, rag.id)}
                              className="ml-1 hover:bg-destructive/20 rounded-full p-0.5"
                            >
                              <X className="w-3 h-3" />
                            </button>
                          </Badge>
                        ))
                      )}
                    </div>
                  </div>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </form>
      </div>
    </div>
  );
}
