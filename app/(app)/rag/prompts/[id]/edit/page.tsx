"use client";

import { useTranslation } from "@/app/config/i18n";
import { Brain, Save, ArrowLeft, FileText, FileCode, X, Plus, Bot, Cpu, Database, BarChart3, TrendingUp, Loader2 } from "lucide-react";
import { useState, useEffect, useRef } from "react";
import { useRouter, useParams } from "next/navigation";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import { CodeEditor } from "@/components/ui/code-editor";
import { getPromptyTemplate } from "@/lib/prompty-template";
import { Badge } from "@/components/ui/badge";

interface PromptFormData {
  prmname: string;
  prmdescription: string;
  prmcontent: string;
  prmparameters: string;
  prmchanges: string;
  prmstatus: string;
  prmversion?: string;
}

export default function PromptEditPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const params = useParams();
  const promptId = params.id as string;

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);
  const [evaluating, setEvaluating] = useState(false);
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
  const [formData, setFormData] = useState<PromptFormData>({
    prmname: "",
    prmdescription: "",
    prmcontent: "",
    prmparameters: "temperature=0.7, max_tokens=1000",
    prmchanges: "",
    prmstatus: "DRAFT",
    prmversion: "1.0.0",
  });

  useEffect(() => {
    if (promptId) {
      loadPromptData(parseInt(promptId));
    }
  }, [promptId]);

  const loadPromptData = async (id: number) => {
    setIsLoading(true);
    try {
      const response = await fetch(`/api/prompts/${id}`);
      if (response.ok) {
        const prompt = await response.json();
        setFormData({
          prmname: prompt.prmname || prompt.name || "",
          prmdescription: prompt.prmdescription || prompt.description || "",
          prmcontent: prompt.prmcontent || prompt.content || "",
          prmparameters: prompt.prmparameters || prompt.parameters || "temperature=0.7, max_tokens=1000",
          prmchanges: "",
          prmstatus: prompt.prmstatus || prompt.status || "DRAFT",
          prmversion: prompt.prmversion || prompt.version || "1.0.0",
        });
        originalContentRef.current = prompt.prmcontent || prompt.content || "";

        // Cargar relaciones
        loadRelations(id);
      }
    } catch (error) {
      console.error("Error al cargar prompt:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const loadRelations = async (id: number) => {
    try {
      const [agentsRes, modelsRes, ragsRes] = await Promise.all([
        fetch(`/api/governance/prompts/${id}/agents`).catch(() => ({ ok: false })),
        fetch(`/api/governance/prompts/${id}/models`).catch(() => ({ ok: false })),
        fetch(`/api/governance/prompts/${id}/rags`).catch(() => ({ ok: false })),
      ]);

      const agents = agentsRes.ok && 'json' in agentsRes ? await agentsRes.json() : [];
      const models = modelsRes.ok && 'json' in modelsRes ? await modelsRes.json() : [];
      const rags = ragsRes.ok && 'json' in ragsRes ? await ragsRes.json() : [];

      setRelations({
        agents: Array.isArray(agents) ? agents.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
        models: Array.isArray(models) ? models.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
        rags: Array.isArray(rags) ? rags.map((r: any) => ({
          id: r.relatedId || r.id,
          name: r.relatedName || r.name,
          relationId: r.id || r.relationId
        })) : [],
      });
    } catch (error) {
      console.error("Error al cargar relaciones:", error);
    }
  };

  const handleDisassociate = async (type: 'agents' | 'models' | 'rags', relationId: number, relatedId: number) => {
    try {
      const endpoint = type === 'agents' ? 'agents' : type === 'models' ? 'models' : 'rags';
      const paramName = type === 'agents' ? 'agentId' : type === 'models' ? 'modelId' : 'ragSystemId';
      const response = await fetch(`/api/governance/prompts/${promptId}/${endpoint}?${paramName}=${relatedId}`, {
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

  const loadPromptyTemplate = () => {
    const template = getPromptyTemplate(formData.prmname || "My New Prompt", formData.prmdescription || "A description for my new prompt.");
    setFormData((prev) => ({ ...prev, prmcontent: template }));
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
      const contentChanged = formData.prmcontent !== originalContentRef.current;

      if (contentChanged) {
        // Crear nueva versión automáticamente
        const response = await fetch(`/api/prompts/${promptId}/versions`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            prmversion: formData.prmversion,
            prmdescription: formData.prmdescription,
            prmcontent: formData.prmcontent,
            prmparameters: formData.prmparameters,
            prmstatus: formData.prmstatus,
            prmchanges: formData.prmchanges || "Actualización automática: contenido modificado",
          }),
        });

        if (!response.ok) {
          throw new Error("Error al crear nueva versión");
        }
      } else {
        // Actualizar sin crear nueva versión (solo metadata)
        const response = await fetch(`/api/prompts/${promptId}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            prmname: formData.prmname,
            prmdescription: formData.prmdescription,
            prmparameters: formData.prmparameters,
            prmstatus: formData.prmstatus,
          }),
        });

        if (!response.ok) {
          throw new Error("Error al actualizar el prompt");
        }
      }

      // Redirigir a la página de detalle después de guardar
      router.push(`/governance/prompts/${promptId}`);
    } catch (error) {
      console.error("Error al guardar prompt:", error);
      alert(t("governance.prompts.register.error", "Error al guardar el prompt"));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    router.push(`/governance/prompts/${promptId}`);
  };

  const handleAnalyze = async () => {
    try {
      setAnalyzing(true);

      const validationData = {
        prmvalidationtype: "COMPLIANCE",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "Análisis completo de compliance, safety, bias y performance",
        prmissuesfound: 0,
        prmstatus: "ACTIVE",
      };

      const response = await fetch(`/api/prompts/${promptId}/validations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(validationData),
      });

      if (response.ok) {
        alert(t("prompts.register.analyze.success", "Análisis iniciado correctamente. Ver resultados en la página de detalle."));
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
    try {
      setEvaluating(true);

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

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative">
        <div className="relative z-10 w-full px-6 py-6 flex items-center justify-center min-h-[60vh]">
          <Loader2 className="w-8 h-8 animate-spin text-primary" />
        </div>
      </div>
    );
  }

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
                {t("prompts.register.title.edit", "Editar Prompt")}
              </h1>
              <p className="text-sm text-muted-foreground mt-1">
                {t("prompts.register.subtitle.edit", "Edita y actualiza el prompt de IA")}
              </p>
            </div>
          </div>

          {/* Segunda línea: Botón Volver (izquierda) */}
          <div className="flex items-center justify-between">
            <Button
              variant="ghost"
              onClick={() => router.push(`/governance/prompts/${promptId}`)}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("common.back", "Volver")}
            </Button>
            <div></div>
          </div>
        </div>

        {/* Formulario */}
        <form onSubmit={handleSubmit}>
          <Card className="backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>
                  {t("prompts.register.form.title", "Información del Prompt")}
                </CardTitle>
                <div className="flex items-center gap-2">
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
                  <Button
                    type="button"
                    variant="outline"
                    onClick={handleCancel}
                    disabled={isSubmitting}
                  >
                    <ArrowLeft className="w-4 h-4 mr-2" />
                    {t("common.cancel", "Cancelar")}
                  </Button>
                  <Button type="submit" disabled={isSubmitting}>
                    <Save className="w-4 h-4 mr-2" />
                    {isSubmitting
                      ? t("prompts.register.form.saving", "Guardando...")
                      : t("governance.prompts.register.form.save", "Guardar Prompt")}
                  </Button>
                </div>
              </div>
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
                    onChange={(e) => handleInputChange("prmname", e.target.value)}
                    placeholder={t("prompts.register.form.namePlaceholder", "Nombre del prompt")}
                    required
                  />
                </div>

                {/* Versión (solo lectura) */}
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="prmversion">
                      {t("prompts.register.form.version", "Versión")}
                    </Label>
                    <Button
                      type="button"
                      variant="ghost"
                      size="sm"
                      onClick={() => router.push("/governance/prompts/versioning/overview")}
                      className="text-xs h-auto py-1 px-2"
                    >
                      {t("prompts.register.form.viewVersions", "Ver Versiones")}
                    </Button>
                  </div>
                  <Input
                    id="prmversion"
                    value={formData.prmversion || "1.0.0"}
                    readOnly
                    disabled
                    className="bg-muted cursor-not-allowed"
                  />
                  {formData.prmcontent !== originalContentRef.current && (
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

              {/* Segunda fila: Descripción, Cambios, Parámetros */}
              <div className="flex flex-col md:flex-row gap-4">
                <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                  <Label htmlFor="prmdescription">
                    {t("prompts.register.form.description", "Descripción")}
                  </Label>
                  <Textarea
                    id="prmdescription"
                    value={formData.prmdescription}
                    onChange={(e) => handleInputChange("prmdescription", e.target.value)}
                    placeholder={t("prompts.register.form.descriptionPlaceholder", "Descripción detallada del prompt")}
                    rows={3}
                    className="w-full"
                  />
                </div>

                <div className="space-y-2 flex-1" style={{ flex: '0 0 35%' }}>
                  <Label htmlFor="prmchanges">
                    {t("prompts.register.form.changes", "Cambios / Notas")}
                  </Label>
                  <Textarea
                    id="prmchanges"
                    value={formData.prmchanges}
                    onChange={(e) => handleInputChange("prmchanges", e.target.value)}
                    placeholder={t("prompts.register.form.changesPlaceholder", "Notas sobre los cambios o características de esta versión...")}
                    rows={3}
                    className="w-full"
                  />
                </div>

                <div className="space-y-2 flex-1" style={{ flex: '0 0 30%' }}>
                  <Label htmlFor="prmparameters">
                    {t("prompts.register.form.parameters", "Parámetros")}
                  </Label>
                  <Textarea
                    id="prmparameters"
                    value={formData.prmparameters}
                    onChange={(e) => handleInputChange("prmparameters", e.target.value)}
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
                  placeholder={t("prompts.register.form.contentPlaceholder", "Escribe el contenido del prompt aquí... Usa el formato Prompty para una estructura profesional.")}
                />
              </div>

              {/* Relaciones */}
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
            </CardContent>
          </Card>
        </form>
      </div>
    </div>
  );
}
