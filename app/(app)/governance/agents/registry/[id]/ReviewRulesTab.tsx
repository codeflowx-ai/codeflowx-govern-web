"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { Shield, Search, CheckCircle2, FileCode, Loader2, Brain, GripVertical, ArrowUp, ArrowDown, Trash2 } from "lucide-react";
import { useState, useEffect, useMemo } from "react";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

interface DroolsRuleInfo {
  fileName: string;
  category: string;
  description?: string;
  lastModified?: string;
}

interface LLMPromptInfo {
  id: string;
  uuid?: string;
  name: string;
  description?: string;
  promptTemplate?: string;
  model?: string;
  category?: string;
  status?: string;
  complianceStatus?: string;
  version?: string;
  createdAt?: string;
  updatedAt?: string;
}

interface SelectedRule {
  id: string;
  type: "DROOLS" | "LLM_PROMPT";
  key: string; // category/fileName para Drools, id para LLM
  name: string;
  description?: string;
  priority: number;
  enabled: boolean;
}

interface ReviewRulesTabProps {
  agentId: string;
  agentUuid?: string;
}

export default function ReviewRulesTab({ agentId, agentUuid }: ReviewRulesTabProps) {
  const { t, mounted } = useTranslation();
  const [droolsRules, setDroolsRules] = useState<Map<string, DroolsRuleInfo[]>>(new Map());
  const [llmPrompts, setLlmPrompts] = useState<LLMPromptInfo[]>([]);
  const [selectedRules, setSelectedRules] = useState<SelectedRule[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [ruleTypeFilter, setRuleTypeFilter] = useState<"ALL" | "DROOLS" | "LLM_PROMPT">("ALL");
  const [draggedIndex, setDraggedIndex] = useState<number | null>(null);

  useEffect(() => {
    loadDroolsRules();
    loadLLMPrompts();
    loadAgentReviewRules();
  }, [agentId]);

  const loadDroolsRules = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/drools/rules");
      if (response.ok) {
        const data: Record<string, DroolsRuleInfo[]> = await response.json();
        // Convertir objeto plano a Map
        const rulesMap = new Map<string, DroolsRuleInfo[]>();
        Object.entries(data).forEach(([category, rules]) => {
          rulesMap.set(category, rules);
        });
        setDroolsRules(rulesMap);
      } else {
        // Mock data para desarrollo
        const mockRules = new Map<string, DroolsRuleInfo[]>([
          [
            "evaluation",
            [
              {
                fileName: "llm-scoring.drl",
                category: "evaluation",
                description: "Regla para evaluación de LLM y scoring automático",
              },
            ],
          ],
          [
            "runtime",
            [
              {
                fileName: "ai-runtime-health.drl",
                category: "runtime",
                description: "Regla para monitoreo de salud en tiempo de ejecución",
              },
            ],
          ],
          [
            "decision",
            [
              {
                fileName: "decision-review.drl",
                category: "decision",
                description: "Regla para determinar cuándo una decisión requiere revisión HITL",
              },
              {
                fileName: "low-confidence-review.drl",
                category: "decision",
                description: "Regla para revisar decisiones con baja confianza",
              },
            ],
          ],
        ]);
        setDroolsRules(mockRules);
      }
    } catch (error) {
      console.error("Error loading Drools rules:", error);
      // En caso de error, usar mock data
      const mockRules = new Map<string, DroolsRuleInfo[]>([
        [
          "decision",
          [
            {
              fileName: "decision-review.drl",
              category: "decision",
              description: "Regla para determinar cuándo una decisión requiere revisión HITL",
            },
            {
              fileName: "low-confidence-review.drl",
              category: "decision",
              description: "Regla para revisar decisiones con baja confianza",
            },
          ],
        ],
      ]);
      setDroolsRules(mockRules);
    } finally {
      setLoading(false);
    }
  };

  const loadLLMPrompts = async () => {
    try {
      // Cargar prompts desde el módulo de prompts con filtro por categoría de revisión
      // Estos prompts están sujetos a las mismas reglas de cumplimiento, versionado y governance
      const response = await fetch("/api/governance/prompts?category=DECISION_REVIEW&status=ACTIVE");
      if (response.ok) {
        const data = await response.json();
        const prompts = data.items || data || [];
        // Mapear a formato LLMPromptInfo
        setLlmPrompts(
          prompts.map((p: any) => ({
            id: p.id?.toString() || p.uuid || "",
            uuid: p.uuid,
            name: p.name || p.promptName || "",
            description: p.description || p.promptDescription || "",
            promptTemplate: p.promptTemplate || p.content || "",
            model: p.model || p.modelName || "",
            category: p.category || "DECISION_REVIEW",
            status: p.status || "ACTIVE",
            complianceStatus: p.complianceStatus,
            version: p.version || "1.0.0",
            createdAt: p.createdAt,
            updatedAt: p.updatedAt,
          }))
        );
      } else {
        // Mock data para desarrollo - estos serían prompts reales del módulo de prompts
        setLlmPrompts([
          {
            id: "llm-review-1",
            name: "Revisión de Decisión con LLM",
            description: "Usa un modelo LLM para evaluar si una decisión requiere revisión humana basándose en contexto y razonamiento. Este prompt está sujeto a cumplimiento AI Act y revisión de sesgos.",
            model: "gpt-4o-mini",
            category: "DECISION_REVIEW",
            status: "ACTIVE",
            complianceStatus: "COMPLIANT",
            version: "1.2.0",
          },
          {
            id: "llm-review-2",
            name: "Análisis de Confianza con LLM",
            description: "Analiza la confianza de una decisión usando razonamiento de lenguaje natural. Requiere revisión periódica para detectar sesgos.",
            model: "gpt-4o-mini",
            category: "DECISION_REVIEW",
            status: "ACTIVE",
            complianceStatus: "PENDING_REVIEW",
            version: "1.0.0",
          },
        ]);
      }
    } catch (error) {
      console.warn("Error loading LLM prompts:", error);
    }
  };

  const loadAgentReviewRules = async () => {
    try {
      // Cargar reglas asociadas al agente
      const response = await fetch(`/api/governance/agents/${agentId}/review-rules`);
      if (response.ok) {
        const data = await response.json();
        if (data.rules && Array.isArray(data.rules)) {
          setSelectedRules(data.rules);
        }
      }
    } catch (error) {
      console.warn("Error loading agent review rules:", error);
      // Si no existe el endpoint, usar reglas por defecto
      setSelectedRules([
        {
          id: "rule-1",
          type: "DROOLS",
          key: "decision/decision-review.drl",
          name: "decision-review.drl",
          description: "Regla para determinar cuándo una decisión requiere revisión HITL",
          priority: 1,
          enabled: true,
        },
      ]);
    }
  };

  const handleToggleDroolsRule = (category: string, fileName: string, description?: string) => {
    const ruleKey = `${category}/${fileName}`;
    const existingIndex = selectedRules.findIndex((r) => r.key === ruleKey);

    if (existingIndex >= 0) {
      // Remover regla
      setSelectedRules(selectedRules.filter((_, i) => i !== existingIndex).map((r, i) => ({ ...r, priority: i + 1 })));
    } else {
      // Agregar regla
      const newRule: SelectedRule = {
        id: `drools-${Date.now()}`,
        type: "DROOLS",
        key: ruleKey,
        name: fileName,
        description: description,
        priority: selectedRules.length + 1,
        enabled: true,
      };
      setSelectedRules([...selectedRules, newRule]);
    }
  };

  const handleToggleLLMPrompt = (prompt: LLMPromptInfo) => {
    const existingIndex = selectedRules.findIndex((r) => r.key === prompt.id);

    if (existingIndex >= 0) {
      // Remover prompt
      setSelectedRules(selectedRules.filter((_, i) => i !== existingIndex).map((r, i) => ({ ...r, priority: i + 1 })));
    } else {
      // Agregar prompt
      const newRule: SelectedRule = {
        id: `llm-${Date.now()}`,
        type: "LLM_PROMPT",
        key: prompt.id,
        name: prompt.name,
        description: prompt.description,
        priority: selectedRules.length + 1,
        enabled: true,
      };
      setSelectedRules([...selectedRules, newRule]);
    }
  };

  const handlePriorityChange = (index: number, direction: "up" | "down") => {
    const newRules = [...selectedRules];
    if (direction === "up" && index > 0) {
      [newRules[index], newRules[index - 1]] = [newRules[index - 1], newRules[index]];
    } else if (direction === "down" && index < newRules.length - 1) {
      [newRules[index], newRules[index + 1]] = [newRules[index + 1], newRules[index]];
    }
    setSelectedRules(newRules.map((r, i) => ({ ...r, priority: i + 1 })));
  };

  const handleRemoveRule = (index: number) => {
    setSelectedRules(selectedRules.filter((_, i) => i !== index).map((r, i) => ({ ...r, priority: i + 1 })));
  };

  const handleDragStart = (index: number) => {
    setDraggedIndex(index);
  };

  const handleDragOver = (e: React.DragEvent, index: number) => {
    e.preventDefault();
    if (draggedIndex === null || draggedIndex === index) return;

    const newRules = [...selectedRules];
    const draggedRule = newRules[draggedIndex];
    newRules.splice(draggedIndex, 1);
    newRules.splice(index, 0, draggedRule);
    setSelectedRules(newRules.map((r, i) => ({ ...r, priority: i + 1 })));
    setDraggedIndex(index);
  };

  const handleDragEnd = () => {
    setDraggedIndex(null);
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      const response = await fetch(`/api/governance/agents/${agentId}/review-rules`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          agentUuid: agentUuid,
          rules: selectedRules.sort((a, b) => a.priority - b.priority),
        }),
      });
      if (response.ok) {
        alert(t("agents.registry.detail.reviewRules.saveSuccess", "Reglas guardadas correctamente"));
      } else {
        const error = await response.json();
        alert(t("agents.registry.detail.reviewRules.saveError", "Error al guardar: ") + (error.message || "Error desconocido"));
      }
    } catch (error) {
      console.error("Error saving review rules:", error);
      alert(t("agents.registry.detail.reviewRules.saveError", "Error al guardar las reglas"));
    } finally {
      setSaving(false);
    }
  };

  const filteredDroolsRules = useMemo(() => {
    if (ruleTypeFilter === "LLM_PROMPT") return new Map();
    const filtered = new Map<string, DroolsRuleInfo[]>();
    droolsRules.forEach((rules, category) => {
      const filteredRulesList = rules.filter(
        (rule) =>
          rule.fileName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          rule.description?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          category.toLowerCase().includes(searchTerm.toLowerCase())
      );
      if (filteredRulesList.length > 0) {
        filtered.set(category, filteredRulesList);
      }
    });
    return filtered;
  }, [droolsRules, searchTerm, ruleTypeFilter]);

  const filteredLLMPrompts = useMemo(() => {
    if (ruleTypeFilter === "DROOLS") return [];
    return llmPrompts.filter(
      (prompt) =>
        prompt.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        prompt.description?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [llmPrompts, searchTerm, ruleTypeFilter]);

  if (!mounted || loading) {
    return (
      <Card className="border-2">
        <CardBody className="flex items-center justify-center h-64">
          <Loader2 className="w-8 h-8 animate-spin text-muted-foreground" />
        </CardBody>
      </Card>
    );
  }

  return (
    <Card className="border-2">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Shield className="w-5 h-5" />
          {t("agents.registry.detail.reviewRules.title", "Configuración de Reglas de Revisión HITL")}
        </CardTitle>
      </CardHeader>
      <CardBody>
        <div className="space-y-6">
          <div className="bg-blue-50 dark:bg-blue-950/20 p-4 rounded-lg border border-blue-200 dark:border-blue-800">
            <p className="text-sm text-blue-900 dark:text-blue-100">
              <strong>{t("agents.registry.detail.reviewRules.info", "Información:")}</strong>{" "}
              {t(
                "agents.registry.detail.reviewRules.description",
                "Seleccione reglas Drools (deterministas y rápidas) o prompts LLM (razonamiento complejo) que determinan cuándo las decisiones requieren revisión HITL. Las reglas se ejecutan en orden de prioridad."
              )}
            </p>
            <p className="text-xs text-blue-800 dark:text-blue-200 mt-2">
              <strong>{t("agents.registry.detail.reviewRules.complianceNote", "Nota de Cumplimiento:")}</strong>{" "}
              {t(
                "agents.registry.detail.reviewRules.complianceDescription",
                "Los prompts LLM están sujetos a las mismas reglas de cumplimiento, versionado y revisión de sesgos que otros prompts del sistema. Se gestionan en el módulo de Prompts y deben cumplir con AI Act y regulaciones aplicables."
              )}
            </p>
          </div>

          {/* Filtros y Búsqueda */}
          <div className="flex gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t("agents.registry.detail.reviewRules.searchPlaceholder", "Buscar reglas...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 rounded-lg border border-border bg-background"
              />
            </div>
            <Select value={ruleTypeFilter} onValueChange={(value: string) => setRuleTypeFilter(value as "ALL" | "DROOLS" | "LLM_PROMPT")}>
              <SelectTrigger className="w-48">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALL">{t("agents.registry.detail.reviewRules.allTypes", "Todos los Tipos")}</SelectItem>
                <SelectItem value="DROOLS">{t("agents.registry.detail.reviewRules.droolsOnly", "Solo Drools")}</SelectItem>
                <SelectItem value="LLM_PROMPT">{t("agents.registry.detail.reviewRules.llmOnly", "Solo LLM")}</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Reglas Seleccionadas con Prioridad */}
          {selectedRules.length > 0 && (
            <div className="space-y-3 border-b pb-4">
              <Label className="text-base font-semibold">
                {t("agents.registry.detail.reviewRules.selectedRules", "Reglas Seleccionadas (Orden de Ejecución)")}
              </Label>
              <p className="text-xs text-muted-foreground">
                {t("agents.registry.detail.reviewRules.dragHint", "Arrastra las reglas para reordenarlas o usa los botones ↑↓")}
              </p>
              <div className="space-y-2">
                {selectedRules
                  .sort((a, b) => a.priority - b.priority)
                  .map((rule, index) => (
                    <div
                      key={rule.id}
                      draggable
                      onDragStart={() => handleDragStart(index)}
                      onDragOver={(e) => handleDragOver(e, index)}
                      onDragEnd={handleDragEnd}
                      className={`flex items-center gap-3 p-3 rounded-lg border bg-primary/5 border-primary/30 cursor-move transition-all ${
                        draggedIndex === index ? "opacity-50 scale-95" : "hover:bg-primary/10"
                      }`}
                    >
                      <GripVertical className="w-4 h-4 text-muted-foreground cursor-grab active:cursor-grabbing" />
                      <div className="flex-1">
                        <div className="flex items-center gap-2">
                          {rule.type === "DROOLS" ? (
                            <FileCode className="w-4 h-4 text-primary" />
                          ) : (
                            <Brain className="w-4 h-4 text-purple-500" />
                          )}
                          <Label className="text-sm font-medium">{rule.name}</Label>
                          <Badge variant={rule.type === "DROOLS" ? "primary" : "secondary"} className="text-xs">
                            {rule.type}
                          </Badge>
                          <Badge variant="outline" className="text-xs">
                            {t("agents.registry.detail.reviewRules.priority", "Prioridad")} {rule.priority}
                          </Badge>
                        </div>
                        {rule.description && (
                          <p className="text-xs text-muted-foreground mt-1 line-clamp-1">{rule.description}</p>
                        )}
                      </div>
                      <div className="flex gap-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 w-8 p-0"
                          onClick={() => handlePriorityChange(index, "up")}
                          disabled={index === 0}
                          title={t("agents.registry.detail.reviewRules.moveUp", "Mover arriba")}
                        >
                          <ArrowUp className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 w-8 p-0"
                          onClick={() => handlePriorityChange(index, "down")}
                          disabled={index === selectedRules.length - 1}
                          title={t("agents.registry.detail.reviewRules.moveDown", "Mover abajo")}
                        >
                          <ArrowDown className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="h-8 w-8 p-0 text-destructive"
                          onClick={() => handleRemoveRule(index)}
                          title={t("agents.registry.detail.reviewRules.remove", "Eliminar")}
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}

          {/* Lista de Reglas en Dos Columnas */}
          {(filteredDroolsRules.size > 0 || filteredLLMPrompts.length > 0) ? (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Columna 1: Reglas Drools */}
              {filteredDroolsRules.size > 0 && (
                <div className="space-y-4">
                  <Label className="text-base font-semibold flex items-center gap-2">
                    <FileCode className="w-4 h-4 text-primary" />
                    {t("agents.registry.detail.reviewRules.droolsRules", "Reglas Drools")}
                  </Label>
                  <div className="space-y-3 max-h-[600px] overflow-y-auto pr-2">
                    {Array.from(filteredDroolsRules.entries()).map(([category, rules]) => (
                      <div key={category} className="space-y-2">
                        <div className="flex items-center gap-2">
                          <Label className="text-sm font-medium capitalize">{category}</Label>
                          <Badge variant="outline" className="text-xs">
                            {rules.length} {t("agents.registry.detail.reviewRules.rules", "reglas")}
                          </Badge>
                        </div>
                        <div className="space-y-2">
                          {rules.map((rule: DroolsRuleInfo) => {
                            const ruleKey = `${category}/${rule.fileName}`;
                            const isSelected = selectedRules.some((r) => r.key === ruleKey);
                            return (
                              <div
                                key={ruleKey}
                                className={`flex items-start gap-3 p-3 rounded-lg border transition-colors cursor-pointer ${
                                  isSelected
                                    ? "bg-primary/10 border-primary/50"
                                    : "bg-muted/30 border-border hover:bg-muted/50"
                                }`}
                                onClick={() => handleToggleDroolsRule(category, rule.fileName, rule.description)}
                              >
                                <div className="flex-shrink-0 mt-1">
                                  {isSelected ? (
                                    <CheckCircle2 className="w-5 h-5 text-primary" />
                                  ) : (
                                    <div className="w-5 h-5 rounded-full border-2 border-muted-foreground" />
                                  )}
                                </div>
                                <div className="flex-1 min-w-0">
                                  <div className="flex items-center gap-2 flex-wrap">
                                    <Label className="text-sm font-medium cursor-pointer">{rule.fileName}</Label>
                                    <Badge variant="outline" className="text-xs">DROOLS</Badge>
                                  </div>
                                  {rule.description && (
                                    <p className="text-xs text-muted-foreground mt-1 line-clamp-2">{rule.description}</p>
                                  )}
                                </div>
                              </div>
                            );
                          })}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {/* Columna 2: Prompts LLM */}
              {filteredLLMPrompts.length > 0 && (
                <div className="space-y-4">
                  <Label className="text-base font-semibold flex items-center gap-2">
                    <Brain className="w-4 h-4 text-purple-500" />
                    {t("agents.registry.detail.reviewRules.llmPrompts", "Prompts LLM")}
                  </Label>
                  <div className="space-y-2 max-h-[600px] overflow-y-auto pr-2">
                    {filteredLLMPrompts.map((prompt) => {
                      const isSelected = selectedRules.some((r) => r.key === prompt.id);
                      return (
                        <div
                          key={prompt.id}
                          className={`flex items-start gap-3 p-3 rounded-lg border transition-colors cursor-pointer ${
                            isSelected
                              ? "bg-purple-500/10 border-purple-500/50"
                              : "bg-muted/30 border-border hover:bg-muted/50"
                          }`}
                          onClick={() => handleToggleLLMPrompt(prompt)}
                        >
                          <div className="flex-shrink-0 mt-1">
                            {isSelected ? (
                              <CheckCircle2 className="w-5 h-5 text-purple-500" />
                            ) : (
                              <div className="w-5 h-5 rounded-full border-2 border-muted-foreground" />
                            )}
                          </div>
                          <div className="flex-1 min-w-0">
                            <div className="flex items-center gap-2 flex-wrap">
                              <Label className="text-sm font-medium cursor-pointer">{prompt.name}</Label>
                              <Badge variant="secondary" className="text-xs">LLM</Badge>
                              {prompt.model && (
                                <Badge variant="outline" className="text-xs">{prompt.model}</Badge>
                              )}
                              {prompt.complianceStatus && (
                                <Badge
                                  variant={
                                    prompt.complianceStatus === "COMPLIANT"
                                      ? "primary"
                                      : prompt.complianceStatus === "PENDING_REVIEW"
                                      ? "secondary"
                                      : "danger"
                                  }
                                  className="text-xs"
                                >
                                  {prompt.complianceStatus}
                                </Badge>
                              )}
                              {prompt.version && (
                                <Badge variant="outline" className="text-xs">v{prompt.version}</Badge>
                              )}
                            </div>
                            {prompt.description && (
                              <p className="text-xs text-muted-foreground mt-1 line-clamp-2">{prompt.description}</p>
                            )}
                            <div className="flex items-center gap-2 mt-1">
                              {prompt.status && (
                                <span className="text-xs text-muted-foreground">
                                  {t("agents.registry.detail.reviewRules.status", "Estado")}: {prompt.status}
                                </span>
                              )}
                              {prompt.updatedAt && (
                                <span className="text-xs text-muted-foreground">
                                  • {new Date(prompt.updatedAt).toLocaleDateString("es-ES")}
                                </span>
                              )}
                            </div>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              {t("agents.registry.detail.reviewRules.noRules", "No se encontraron reglas")}
            </div>
          )}

          {/* Botón Guardar */}
          <div className="flex justify-end pt-4 border-t">
            <Button onClick={handleSave} disabled={saving} className="bg-primary hover:bg-primary/90">
              {saving ? (
                <>
                  <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                  {t("common.saving", "Guardando...")}
                </>
              ) : (
                <>
                  <CheckCircle2 className="w-4 h-4 mr-2" />
                  {t("common.save", "Guardar Reglas")}
                </>
              )}
            </Button>
          </div>

          {/* Resumen */}
          {selectedRules.length > 0 && (
            <div className="bg-green-50 dark:bg-green-950/20 p-4 rounded-lg border border-green-200 dark:border-green-800">
              <p className="text-sm text-green-900 dark:text-green-100">
                <strong>{t("agents.registry.detail.reviewRules.summary", "Resumen:")}</strong>{" "}
                {t("agents.registry.detail.reviewRules.selectedCount", "{{count}} regla(s) seleccionada(s)", {
                  count: selectedRules.length.toString(),
                })}
                {" - "}
                {selectedRules.filter((r) => r.type === "DROOLS").length.toString()} Drools, {selectedRules.filter((r) => r.type === "LLM_PROMPT").length.toString()} LLM
              </p>
              <p className="text-xs text-green-800 dark:text-green-200 mt-1">
                {t("agents.registry.detail.reviewRules.executionOrder", "Las reglas se ejecutarán en el orden de prioridad mostrado arriba.")}
              </p>
            </div>
          )}
        </div>
      </CardBody>
    </Card>
  );
}
