"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Tabs, TabsList, TabsTrigger, TabsContent } from "@/components/ui/tabs";
import {
  Tag,
  AlertTriangle,
  CheckCircle,
  FileText,
  Download,
  Sparkles,
  Loader2,
  Info,
  XCircle,
  ArrowLeft,
  Shield,
  Calendar,
  User,
  Building2,
  Database,
  FileCheck,
  History,
  ChevronDown,
  ChevronUp,
  ExternalLink,
  Ban,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import {
  mockAnnexIIICategories,
  mockProject,
  mockAISuggestion,
  mockClassifiedProjects,
  parseAnnexIIICategories,
  createAnnexIIICategoriesJSONB,
  type AnnexIIICategory,
  type AnnexIIISubcategory,
  type AISuggestion,
  type ProjectDetails,
  type ClassificationDetails,
  type ClassificationRequest,
} from "../../data/mockClassification";

interface ClassificationData extends ClassificationDetails {
  project: ProjectDetails;
  selectedCategory: string | null; // Helper: extraído de prjannexiiicategories
  selectedSubcategories: string[]; // Helper: extraído de prjannexiiicategories
  justification: string; // Helper: se almacenará en metadata o se añadirá como campo
  categories: AnnexIIICategory[];
}

export default function ClassificationPage() {
  const { t, language, mounted } = useTranslation();
  const searchParams = useSearchParams();
  const [data, setData] = useState<ClassificationData | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [aiSuggestion, setAiSuggestion] = useState<AISuggestion | null>(null);
  const [aiLoading, setAiLoading] = useState(false);
  const [classifying, setClassifying] = useState(false);
  const [validationErrors, setValidationErrors] = useState<string[]>([]);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [prohibitedSystemCheck, setProhibitedSystemCheck] = useState<{
    checked: boolean;
    detected: boolean;
    systems: any[];
    loading: boolean;
  }>({
    checked: false,
    detected: false,
    systems: [],
    loading: false,
  });

  useEffect(() => {
    loadData();
  }, []);

  // Verificar sistemas prohibidos automáticamente cuando se carga el proyecto
  useEffect(() => {
    if (data?.project?.id && !prohibitedSystemCheck.checked) {
      checkProhibitedSystems(data.project.id);
    }
  }, [data?.project?.id]);

  const loadData = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));

      const projectIdParam = searchParams.get("projectId");
      if (projectIdParam) {
        const projectId = parseInt(projectIdParam, 10);
        const existingProject = mockClassifiedProjects.find((p) => p.id === projectId);

        if (existingProject) {
          const project: ProjectDetails = {
            ...mockProject,
            id: existingProject.id,
            name: existingProject.name,
            description: existingProject.description,
          };

          // Parsear prjannexiiicategories desde JSONB (simulado)
          const annexIIIData = parseAnnexIIICategories(
            createAnnexIIICategoriesJSONB(existingProject.category, existingProject.subcategories)
          );

          setData({
            project,
            prjishighrisk: true,
            prjannexiiicategories: createAnnexIIICategoriesJSONB(existingProject.category, existingProject.subcategories),
            prjclassificationdate: existingProject.classifiedDate,
            prjclassificationauthor: existingProject.classifiedBy,
            prjprohibitedusechecked: true,
            selectedCategory: annexIIIData.category || existingProject.category,
            selectedSubcategories: annexIIIData.subcategories || existingProject.subcategories,
            justification: existingProject.justification,
            categories: mockAnnexIIICategories,
            confidence: 0.95,
          });
          setSelectedCategory(existingProject.category);
          return;
        }
      }

      // Modo creación
      setData({
        project: mockProject,
        prjishighrisk: false,
        prjannexiiicategories: undefined,
        prjclassificationdate: undefined,
        prjclassificationauthor: undefined,
        prjprohibitedusechecked: false,
        prjregulatedsector: false,
        prjannexilegislation: undefined,
        selectedCategory: null,
        selectedSubcategories: [],
        justification: "",
        categories: mockAnnexIIICategories,
      });
    } catch (error) {
      console.error("Error loading classification data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCategorySelect = (categoryCode: string) => {
    setSelectedCategory(categoryCode);
    if (data) {
      const updatedSubcategories: string[] = [];
      const updatedAnnexIII = createAnnexIIICategoriesJSONB(categoryCode, updatedSubcategories);
      setData({
        ...data,
        selectedCategory: categoryCode,
        selectedSubcategories: updatedSubcategories,
        prjannexiiicategories: updatedAnnexIII,
      });
    }
    setValidationErrors([]);
  };

  const handleSubcategoryToggle = (subcategoryCode: string) => {
    if (!data) return;
    const current = data.selectedSubcategories || [];
    const updated = current.includes(subcategoryCode)
      ? current.filter((c) => c !== subcategoryCode)
      : [...current, subcategoryCode];

    // Actualizar prjannexiiicategories JSONB
    const category = data.selectedCategory || "";
    const updatedAnnexIII = createAnnexIIICategoriesJSONB(category, updated);

    setData({
      ...data,
      selectedSubcategories: updated,
      prjannexiiicategories: updatedAnnexIII,
    });
    setValidationErrors([]);
  };

  const validateJustificationQuality = (justification: string, categoryName: string): string[] => {
    const errors: string[] = [];
    const justificationLower = justification.toLowerCase();
    const categoryNameLower = categoryName.toLowerCase();

    if (justification.length < 100) {
      errors.push(t("governance.classification.justificationMinLength", "La justificación debe tener al menos 100 caracteres"));
    }

    if (!justificationLower.includes(categoryNameLower)) {
      errors.push(t("governance.classification.justificationCategory", "La justificación debe mencionar la categoría seleccionada"));
    }

    const riskKeywords = ["riesgo", "alto riesgo", "vulnerable", "crítico", "risk", "high risk", "critical", "vulnerable"];
    const keywordCount = riskKeywords.filter((keyword) =>
      justificationLower.includes(keyword.toLowerCase())
    ).length;

    if (keywordCount < 2) {
      errors.push(t("governance.classification.justificationKeywords", "La justificación debe contener al menos 2 palabras clave de riesgo"));
    }

    return errors;
  };

  const validateClassification = (): boolean => {
    if (!data) return false;

    const errors: string[] = [];

    if (!data.prjprohibitedusechecked) {
      errors.push("Debe verificar que el sistema no está prohibido según el Art. 5 del EU AI Act");
    }

    if (!data.selectedCategory) {
      errors.push(t("governance.classification.categoryRequired", "Debe seleccionar una categoría principal"));
    }

    if ((data.selectedSubcategories || []).length === 0) {
      errors.push(t("governance.classification.subcategoryRequired", "Debe seleccionar al menos una subcategoría"));
    }

    if (data.selectedCategory) {
      const selectedCategoryData = data.categories.find((c) => c.anncategorycode === data.selectedCategory);
      if (selectedCategoryData) {
        const justificationErrors = validateJustificationQuality(
          data.justification || "",
          selectedCategoryData.anncategoryname
        );
        errors.push(...justificationErrors);
      }
    }

    setValidationErrors(errors);
    return errors.length === 0;
  };

  const checkProhibitedSystems = async (projectId: number) => {
    try {
      setProhibitedSystemCheck((prev) => ({ ...prev, loading: true }));

      const response = await fetch(`/api/compliance/prohibited-systems/check?projectId=${projectId}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      if (!response.ok) {
        throw new Error("Failed to check prohibited systems");
      }

      const result = await response.json();

      const detected = result.detected || false;
      const systems = result.systems || result.detectedSystems || [];

      setProhibitedSystemCheck({
        checked: true,
        detected,
        systems,
        loading: false,
      });

      // Si se detectan sistemas prohibidos, actualizar el estado del proyecto
      if (detected && data) {
        setData({
          ...data,
          prjprohibitedusechecked: false, // No puede estar verificado si hay sistemas prohibidos
        });
      } else if (!detected && data) {
        // Si no hay sistemas prohibidos, marcar como verificado automáticamente
        setData({
          ...data,
          prjprohibitedusechecked: true,
        });
      }
    } catch (error) {
      console.error("Error checking prohibited systems:", error);
      setProhibitedSystemCheck((prev) => ({ ...prev, loading: false }));
    }
  };

  const handleAISuggest = async () => {
    if (!data) return;

    try {
      setAiLoading(true);
      setAiSuggestion(null);
      await new Promise((resolve) => setTimeout(resolve, 2000));

      const suggestion = mockAISuggestion;
      setAiSuggestion(suggestion);

      if (data) {
        setSelectedCategory(suggestion.suggestedCategory);
        const updatedAnnexIII = createAnnexIIICategoriesJSONB(
          suggestion.suggestedCategory,
          suggestion.suggestedSubcategories
        );
        setData({
          ...data,
          selectedCategory: suggestion.suggestedCategory,
          selectedSubcategories: suggestion.suggestedSubcategories,
          prjannexiiicategories: updatedAnnexIII,
          confidence: suggestion.confidence,
        });
      }
    } catch (error) {
      console.error("Error generating AI suggestion:", error);
    } finally {
      setAiLoading(false);
    }
  };

  const handleClassify = async () => {
    if (!data) return;

    // Verificar sistemas prohibidos antes de clasificar
    if (!prohibitedSystemCheck.checked && data.project.id) {
      await checkProhibitedSystems(data.project.id);
    }

    // Si se detectaron sistemas prohibidos, no permitir clasificar
    if (prohibitedSystemCheck.detected) {
      setValidationErrors([
        "No se puede clasificar el proyecto: se han detectado sistemas prohibidos según el Art. 5 del EU AI Act. El despliegue ha sido bloqueado automáticamente.",
      ]);
      return;
    }

    if (!validateClassification()) {
      return;
    }

    try {
      setClassifying(true);
      setSuccessMessage(null);
      setValidationErrors([]);

      // Crear ClassificationRequest según DTO
      const classificationRequest: ClassificationRequest = {
        projectId: data.project.id!,
        category: data.selectedCategory!,
        subcategories: data.selectedSubcategories,
        justification: data.justification || "",
        prohibitedUseChecked: data.prjprohibitedusechecked,
        prohibitedUseJustification: data.prjprohibitedusejustification,
        regulatedSector: data.prjregulatedsector,
        annexILegislation: data.prjannexilegislation ? JSON.parse(data.prjannexilegislation) : undefined,
      };

      const response = await fetch("/api/compliance/classification/classify", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(classificationRequest),
      });

      if (!response.ok) throw new Error("Classification failed");

      const result = await response.json();

      if (!result.success) {
        // Manejar errores de validación del backend
        if (result.error?.details) {
          setValidationErrors(result.error.details.map((d: any) => d.message || d.field));
        } else {
          setValidationErrors([result.error?.message || "Error al clasificar el proyecto"]);
        }
        return;
      }

      // Mapear respuesta a ClassificationDetails según especificación
      if (result.data && data) {
        setData({
          ...data,
          prjishighrisk: result.data.isHighRisk,
          prjannexiiicategories: createAnnexIIICategoriesJSONB(
            result.data.category,
            result.data.subcategories
          ),
          prjclassificationdate: result.data.classificationDate,
          prjclassificationauthor: result.data.classifiedBy,
          prjclassificationjustification: classificationRequest.justification,
          selectedCategory: result.data.category,
          selectedSubcategories: result.data.subcategories,
          justification: classificationRequest.justification,
          workflowInstanceId: result.data.workflowInstanceId,
          workflowStatus: result.data.workflowTriggered ? "active" : undefined,
        });
      }

      setSuccessMessage(t("governance.classification.classificationSuccess", "Proyecto clasificado exitosamente como alto riesgo"));

      if (result.data?.workflowTriggered) {
        setTimeout(() => {
          alert(t("governance.classification.workflowTriggered", "Workflow BPMN activado para sistema de alto riesgo"));
        }, 500);
      }

      setTimeout(() => {
        window.location.href = "/governance/compliance/classification/projects";
      }, 1500);
    } catch (error) {
      console.error("Error classifying project:", error);
      setValidationErrors([t("governance.classification.classificationError", "Error al clasificar el proyecto")]);
    } finally {
      setClassifying(false);
    }
  };

  if (loading || !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  const selectedCategoryData = data.categories.find((c) => c.anncategorycode === selectedCategory);
  const isHighRisk = data.prjishighrisk || data.selectedCategory !== null;
  const justificationLength = (data.justification || "").length;
  const minJustificationLength = 100;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80">
      <style dangerouslySetInnerHTML={{__html: `
        .nav-tabs {
          list-style: none;
          margin: 0;
          padding: 0;
          display: flex;
          gap: 0.5rem;
        }
        .nav-item {
          list-style: none;
          margin: 0;
          padding: 0;
          flex: 1;
        }
        .nav-link {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 100%;
          border: none;
          background: transparent;
        }
        .nav-link.active[data-tab="general"] {
          background: linear-gradient(to bottom right, #3b82f6, #2563eb) !important;
          color: white !important;
          box-shadow: 0 10px 15px -3px rgb(59 130 246 / 0.3) !important;
          transform: scale(1.02) !important;
        }
        .nav-link.active[data-tab="classification"] {
          background: linear-gradient(to bottom right, #eab308, #ca8a04) !important;
          color: white !important;
          box-shadow: 0 10px 15px -3px rgb(234 179 8 / 0.3) !important;
          transform: scale(1.02) !important;
        }
        .nav-link.active[data-tab="additional"] {
          background: linear-gradient(to bottom right, #a855f7, #9333ea) !important;
          color: white !important;
          box-shadow: 0 10px 15px -3px rgb(168 85 247 / 0.3) !important;
          transform: scale(1.02) !important;
        }
      `}} />
      <div className="w-full px-4 py-3 space-y-3">
        {/* Header */}
        <div className="flex items-center justify-between bg-card/80 backdrop-blur-sm border rounded-lg p-4">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => {
                window.location.href = "/governance/compliance/classification/projects";
              }}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              {t("common.back", "Volver")}
            </Button>
          <div>
              <div className="flex items-center gap-2 mb-1">
                <Tag className="w-6 h-6 text-yellow-500" />
                <h1 className="text-2xl font-bold bg-gradient-to-r from-yellow-500 to-yellow-700 bg-clip-text text-transparent">
                {t("governance.classification.title", "Clasificación de Sistemas de Alto Riesgo")}
              </h1>
            </div>
              <p className="text-sm text-muted-foreground">
                {t("governance.classification.subtitle", "EU AI Act Art. 6 y Anexo III - Clasificación Completa")}
            </p>
            </div>
          </div>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" onClick={handleAISuggest} disabled={aiLoading}>
              {aiLoading ? (
                <>
                  <Loader2 className="h-3 w-3 mr-1 animate-spin" />
                  {t("governance.classification.aiSuggestionLoading", "Generando...")}
                </>
              ) : (
                <>
                  <Sparkles className="h-3 w-3 mr-1" />
                  {t("governance.classification.aiSuggestion", "Sugerencia IA")}
                </>
              )}
            </Button>
            <Button variant="outline" size="sm">
              <Download className="h-3 w-3 mr-1" />
              {t("governance.classification.exportPDF", "Exportar PDF")}
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => {
                window.location.href = "/governance/compliance/classification/projects";
              }}
            >
              {t("common.cancel", "Cancelar")}
            </Button>
            <Button onClick={handleClassify} disabled={classifying} size="sm" className="bg-primary hover:bg-primary/90">
              {classifying ? (
                <>
                  <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                  {t("common.loading", "Cargando...")}
                </>
              ) : (
                <>
                  <FileText className="h-4 w-4 mr-2" />
                  {t("governance.classification.classifyProject", "Clasificar Proyecto")}
                </>
              )}
            </Button>
          </div>
        </div>

        {/* Mensajes */}
        {successMessage && (
          <Alert className="bg-green-50 border-green-200 mb-4">
            <CheckCircle className="h-4 w-4 text-green-600" />
            <AlertDescription className="text-green-800">{successMessage}</AlertDescription>
          </Alert>
        )}

        {/* Alerta de sistemas prohibidos detectados */}
        {prohibitedSystemCheck.detected && (
          <Alert variant="danger" className="mb-4 border-red-500">
            <Ban className="h-4 w-4 text-red-600" />
            <AlertDescription className="text-red-800">
              <div className="space-y-2">
                <div className="font-semibold">
                  Sistema Prohibido Detectado - Art. 5 EU AI Act
                </div>
                <div className="text-sm">
                  Se han detectado {prohibitedSystemCheck.systems.length} sistema(s) prohibido(s) en este proyecto.
                  El despliegue ha sido bloqueado automáticamente.
                </div>
                <div className="text-sm space-y-1">
                  {prohibitedSystemCheck.systems.map((system, idx) => (
                    <div key={idx} className="flex items-center gap-2">
                      <Badge variant="danger" className="text-xs">
                        {system.category || system.prscategory}
                      </Badge>
                      <span className="font-medium">{system.name || system.prsname}</span>
                    </div>
                  ))}
                </div>
                <div className="text-xs mt-2 pt-2 border-t border-red-300">
                  No se puede clasificar un proyecto que contiene sistemas prohibidos.
                  Por favor, revise el proyecto y elimine los sistemas prohibidos antes de continuar.
                </div>
              </div>
            </AlertDescription>
          </Alert>
        )}

        {/* Alerta de verificación exitosa */}
        {prohibitedSystemCheck.checked && !prohibitedSystemCheck.detected && !prohibitedSystemCheck.loading && (
          <Alert className="mb-4 bg-green-50 border-green-200">
            <CheckCircle className="h-4 w-4 text-green-600" />
            <AlertDescription className="text-green-800">
              <div className="font-semibold">
                ✓ Verificación de Sistemas Prohibidos Completada
              </div>
              <div className="text-sm">
                No se han detectado sistemas prohibidos en este proyecto. Puede continuar con la clasificación.
              </div>
            </AlertDescription>
          </Alert>
        )}

        {/* Loading de verificación */}
        {prohibitedSystemCheck.loading && (
          <Alert className="mb-4 bg-blue-50 border-blue-200">
            <Loader2 className="h-4 w-4 text-blue-600 animate-spin" />
            <AlertDescription className="text-blue-800">
              Verificando sistemas prohibidos...
            </AlertDescription>
          </Alert>
        )}

        {validationErrors.length > 0 && (
          <Alert variant="danger" className="mb-4">
            <XCircle className="h-4 w-4" />
            <AlertDescription>
              <div className="space-y-1">
                {validationErrors.map((error, index) => (
                  <div key={index}>{error}</div>
                ))}
              </div>
            </AlertDescription>
          </Alert>
        )}

        {aiSuggestion && (
          <Alert className="bg-blue-50 border-blue-200 mb-4">
            <Info className="h-4 w-4 text-blue-600" />
            <AlertDescription className="text-blue-800">
              <div className="space-y-1">
                <div className="font-semibold">
                  {t("governance.classification.aiSuggestionSuccess", "Sugerencia generada")} ({Math.round((aiSuggestion.confidence || 0) * 100)}%)
                </div>
                <div className="text-sm">{aiSuggestion.reasoning}</div>
              </div>
            </AlertDescription>
          </Alert>
        )}

        {/* Tabs para organizar contenido */}
        <Tabs defaultValue="general" className="w-full">
          <TabsList className="inline-flex w-full mb-6 p-1.5 bg-gradient-to-r from-muted/50 via-muted/30 to-muted/50 rounded-xl border-2 border-border/50 shadow-lg backdrop-blur-sm">
            <TabsTrigger value="general" data-tab="general" className="flex items-center gap-2.5 px-6 py-3 text-sm font-semibold rounded-lg transition-all duration-300 hover:bg-accent/50 hover:scale-[1.01] text-muted-foreground flex-1">
              <Building2 className="h-5 w-5" />
              General
            </TabsTrigger>
            <TabsTrigger value="classification" data-tab="classification" className="flex items-center gap-2.5 px-6 py-3 text-sm font-semibold rounded-lg transition-all duration-300 hover:bg-accent/50 hover:scale-[1.01] text-muted-foreground flex-1">
              <Tag className="h-5 w-5" />
              Clasificación
            </TabsTrigger>
            <TabsTrigger value="additional" data-tab="additional" className="flex items-center gap-2.5 px-6 py-3 text-sm font-semibold rounded-lg transition-all duration-300 hover:bg-accent/50 hover:scale-[1.01] text-muted-foreground flex-1">
              <Database className="h-5 w-5" />
              Adicional
            </TabsTrigger>
          </TabsList>

          {/* Tab 1: Información General */}
          <TabsContent value="general" className="space-y-4 mt-6">
            <div className="grid grid-cols-3 gap-4">
              {/* Card 1: Información del Proyecto */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <Building2 className="h-4 w-4 text-primary" />
                    Información del Proyecto
                  </CardTitle>
          </CardHeader>
                <CardBody className="space-y-3 pt-0">
                  <div>
                    <Label className="text-xs font-semibold">Nombre *</Label>
                    <Input value={data.project.name} disabled className="mt-1 h-8 text-sm" />
                  </div>
                  <div className="grid grid-cols-2 gap-2">
              <div>
                      <Label className="text-xs font-semibold">Código</Label>
                      <Input value={data.project.code} disabled className="mt-1 h-8 text-xs" />
              </div>
              <div>
                      <Label className="text-xs font-semibold">Tipo</Label>
                      <Input value={data.project.type} disabled className="mt-1 h-8 text-xs" />
                    </div>
              </div>
                <div>
                    <Label className="text-xs font-semibold">Estado</Label>
                    <div className="mt-1">
                      <Badge variant="outline" className="text-xs">{data.project.status}</Badge>
                    </div>
                </div>
                <div>
                    <Label className="text-xs font-semibold">Descripción</Label>
                    <Textarea value={data.project.description} disabled rows={3} className="mt-1 text-xs" />
                  </div>
                  <div className="pt-2 border-t">
                    <div className="flex flex-wrap gap-1">
                      <Badge variant={isHighRisk ? "danger" : "secondary"} className="text-xs">
                        {isHighRisk ? "ALTO RIESGO" : "NO ALTO RIESGO"}
                      </Badge>
                      {data.prjclassificationdate && (
                        <Badge variant="outline" className="text-xs">
                          {new Date(data.prjclassificationdate).toLocaleDateString()}
                        </Badge>
                      )}
                  </div>
                </div>
                </CardBody>
              </Card>

              {/* Card 2: Propietario y Cliente */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <User className="h-4 w-4 text-primary" />
                    Propietario y Cliente
                  </CardTitle>
                </CardHeader>
                <CardBody className="space-y-3 pt-0">
                  <div>
                    <Label className="text-xs font-semibold">Propietario</Label>
                    <Input value={data.project.owner} disabled className="mt-1 h-8 text-sm" />
                  </div>
                  <div>
                    <Label className="text-xs font-semibold">Email</Label>
                    <Input value={data.project.ownerEmail} disabled className="mt-1 h-8 text-xs" />
              </div>
                <div>
                    <Label className="text-xs font-semibold">Cliente</Label>
                    <Input value={data.project.client || "N/A"} disabled className="mt-1 h-8 text-sm" />
                  </div>
                  <div className="pt-2 border-t">
                    <Label className="text-xs font-semibold flex items-center gap-2 mb-2">
                      <Calendar className="h-3 w-3" />
                      Fechas
                    </Label>
                    <div className="space-y-1">
                      <div className="text-xs">
                        <span className="text-muted-foreground">Inicio:</span> {data.project.startDate || "N/A"}
                      </div>
                      <div className="text-xs">
                        <span className="text-muted-foreground">Fin:</span> {data.project.endDate || "N/A"}
                  </div>
                </div>
            </div>
          </CardBody>
        </Card>

              {/* Card 3: Validación Art. 5 */}
              <Card className="border-2 border-red-200">
                <CardHeader className="pb-3 bg-red-50/50">
                  <CardTitle className="flex items-center gap-2 text-sm text-red-700">
                    <Shield className="h-4 w-4" />
                    Validación Art. 5 *
                  </CardTitle>
                </CardHeader>
                <CardBody className="space-y-3 pt-0">
                  <Alert className="bg-yellow-50 border-yellow-200 p-2">
                    <AlertTriangle className="h-3 w-3 text-yellow-600" />
                    <AlertDescription className="text-yellow-800 text-xs">
                      <strong>Art. 5 EU AI Act:</strong> Verificar que el sistema no está prohibido.
                    </AlertDescription>
                  </Alert>
                  <div className="flex items-start gap-2 p-3 border rounded-lg">
                    <input
                      type="checkbox"
                      checked={data.prjprohibitedusechecked}
                      onChange={(e) => setData({ ...data, prjprohibitedusechecked: e.target.checked })}
                      className="mt-0.5 h-4 w-4"
                    />
                    <div className="flex-1">
                      <Label className="text-xs font-semibold cursor-pointer">
                        Confirmo que NO está prohibido según Art. 5
                      </Label>
                      <p className="text-xs text-muted-foreground mt-1">
                        No utiliza técnicas prohibidas (manipulación subliminal, scoring social, etc.)
                      </p>
                    </div>
                  </div>
                </CardBody>
              </Card>
            </div>
          </TabsContent>

          {/* Tab 2: Clasificación */}
          <TabsContent value="classification" className="space-y-4 mt-6">
            <div className="grid grid-cols-3 gap-4">
              {/* Card 1: Categorías Principales */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <Tag className="h-4 w-4 text-primary" />
                    Categorías Principales *
                  </CardTitle>
            </CardHeader>
                <CardBody className="pt-0">
                  <div className="space-y-2 max-h-[calc(100vh-28rem)] overflow-y-auto">
                {data.categories.map((category) => (
                  <div
                        key={category.anncategorycode}
                        className={`p-2 border rounded cursor-pointer transition-all ${
                          selectedCategory === category.anncategorycode
                            ? "border-primary bg-primary/10 shadow-sm"
                        : "border-border hover:border-primary/50 hover:bg-accent/50"
                    }`}
                        onClick={() => handleCategorySelect(category.anncategorycode)}
                  >
                        <div className="flex items-start justify-between gap-2">
                          <div className="flex-1 min-w-0">
                            <div className="flex items-center gap-1.5 mb-1">
                              <Badge variant="outline" className="font-mono text-xs">
                                {category.anncategorycode}
                          </Badge>
                              <span className="font-semibold text-xs truncate">{category.anncategoryname}</span>
                        </div>
                            <p className="text-xs text-muted-foreground line-clamp-2">
                              {category.anncategorydescription}
                        </p>
                            <div className="text-xs text-muted-foreground mt-1">
                              {category.subcategories?.length || 0} subcategorías
                            </div>
                          </div>
                          {selectedCategory === category.anncategorycode && (
                            <CheckCircle className="h-4 w-4 text-primary flex-shrink-0" />
                          )}
                        </div>
                      </div>
                ))}
              </div>
            </CardBody>
          </Card>

              {/* Card 2: Subcategorías */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <Tag className="h-4 w-4 text-primary" />
                    Subcategorías *
              </CardTitle>
            </CardHeader>
                <CardBody className="pt-0">
              {selectedCategoryData ? (
                    <div className="space-y-2 max-h-[calc(100vh-28rem)] overflow-y-auto">
                      <p className="text-xs text-muted-foreground mb-2 p-2 bg-accent/50 rounded">
                        {selectedCategoryData.anncategorydescription}
                      </p>
                      {selectedCategoryData.subcategories?.map((subcat) => (
                      <div
                          key={subcat.annsubcategorycode}
                          className={`flex items-start gap-2 p-2 border rounded transition-colors ${
                            (data.selectedSubcategories || []).includes(subcat.annsubcategorycode)
                            ? "border-primary bg-primary/5"
                            : "border-border hover:border-primary/50"
                        }`}
                      >
                        <input
                          type="checkbox"
                            checked={(data.selectedSubcategories || []).includes(subcat.annsubcategorycode)}
                            onChange={() => handleSubcategoryToggle(subcat.annsubcategorycode)}
                            className="mt-0.5 h-3.5 w-3.5"
                        />
                          <div className="flex-1 min-w-0">
                            <div className="flex items-center gap-1.5 mb-0.5">
                            <Badge variant="outline" className="text-xs font-mono">
                                {subcat.annsubcategorycode}
                            </Badge>
                              <span className="font-medium text-xs truncate">{subcat.annsubcategoryname}</span>
                          </div>
                            <p className="text-xs text-muted-foreground line-clamp-2">{subcat.annsubcategorydescription}</p>
                        </div>
                      </div>
                    ))}
                </div>
              ) : (
                    <div className="border rounded-lg p-6 text-center text-muted-foreground text-sm">
                      Selecciona una categoría
                </div>
              )}
            </CardBody>
          </Card>

              {/* Card 3: Justificación */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <FileText className="h-4 w-4 text-primary" />
                    Justificación Técnica *
                  </CardTitle>
          </CardHeader>
                <CardBody className="pt-0 space-y-2">
              <Textarea
                    value={data.justification || ""}
                    onChange={(e) => {
                      const justificationValue = e.target.value;
                      setData({
                        ...data,
                        justification: justificationValue,
                        prjclassificationjustification: justificationValue, // Mapear también al campo de BD
                      });
                    }}
                    rows={12}
                    placeholder="Justificación detallada (mín. 100 caracteres). Debe mencionar la categoría y contener al menos 2 palabras clave de riesgo..."
                    className={`text-sm ${
                  justificationLength < minJustificationLength
                    ? "border-yellow-300 focus:border-yellow-500"
                    : ""
                }`}
              />
              <div className="flex items-center justify-between">
                <p className="text-xs text-muted-foreground">
                      Mencionar categoría y 2+ palabras clave de riesgo
                </p>
                <div className={`text-xs font-medium ${
                  justificationLength < minJustificationLength
                    ? "text-yellow-600"
                    : "text-green-600"
                }`}>
                      {justificationLength} / {minJustificationLength}
              </div>
            </div>
          </CardBody>
        </Card>
            </div>
          </TabsContent>

          {/* Tab 3: Información Adicional */}
          <TabsContent value="additional" className="space-y-4 mt-6">
            <div className="grid grid-cols-3 gap-4">
              {/* Card 1: Modelos Asociados */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <Database className="h-4 w-4 text-primary" />
                    Modelos ({data.project.models.length})
                  </CardTitle>
                </CardHeader>
                <CardBody className="pt-0 space-y-2 max-h-[calc(100vh-28rem)] overflow-y-auto">
                  {data.project.models.map((model) => (
                    <div key={model.id} className="border rounded-lg p-2 space-y-1.5">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-1.5 flex-wrap">
                          <span className="font-semibold text-xs">{model.name}</span>
                          <Badge variant="outline" className="text-xs">{model.version}</Badge>
                          <Badge variant="outline" className="text-xs">{model.type}</Badge>
                        </div>
                        {model.isGPAI && (
                          <Badge variant="danger" className="text-xs">
                            <Info className="h-2.5 w-2.5 mr-0.5" />
                            GPAI
                          </Badge>
                        )}
                      </div>
                      {model.isGPAI && (
                        <div className="bg-yellow-50 border border-yellow-200 rounded p-2 space-y-1">
                          <div className="text-xs font-semibold">Modelo de Propósito General</div>
                          {model.flopsTraining && (
                            <div className="text-xs">
                              <strong>FLOPs:</strong> {model.flopsTraining.toLocaleString()}
                            </div>
                          )}
                          {model.systemicRisk && (
                            <Badge variant="danger" className="text-xs">
                              <AlertTriangle className="h-2.5 w-2.5 mr-0.5" />
                              Riesgo Sistémico
                            </Badge>
                          )}
                        </div>
                      )}
                    </div>
                  ))}
                </CardBody>
              </Card>

              {/* Card 2: Metadatos */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <FileCheck className="h-4 w-4 text-primary" />
                    Metadatos
                  </CardTitle>
                </CardHeader>
                <CardBody className="pt-0 space-y-3">
                  <div>
                    <Label className="text-xs font-semibold">Confianza (%)</Label>
                    <Input
                      type="number"
                      min="0"
                      max="100"
                      value={data.confidence ? Math.round(data.confidence * 100) : ""}
                      onChange={(e) =>
                        setData({
                          ...data,
                          confidence: e.target.value ? parseFloat(e.target.value) / 100 : undefined,
                        })
                      }
                      placeholder="0-100"
                      className="mt-1 h-8 text-sm"
                    />
                  </div>
                  <div>
                    <Label className="text-xs font-semibold">Próxima Revisión</Label>
                    <Input
                      type="date"
                      value={data.nextReviewDate || ""}
                      onChange={(e) => setData({ ...data, nextReviewDate: e.target.value })}
                      className="mt-1 h-8 text-sm"
                    />
                  </div>
                  <div>
                    <Label className="text-xs font-semibold">Responsable</Label>
                    <Input
                      value={data.complianceOfficer || ""}
                      onChange={(e) => setData({ ...data, complianceOfficer: e.target.value })}
                      placeholder="Nombre del responsable"
                      className="mt-1 h-8 text-sm"
                    />
                  </div>
                  <div>
                    <Label className="text-xs font-semibold">Notas Internas</Label>
                    <Textarea
                      value={data.internalNotes || ""}
                      onChange={(e) => setData({ ...data, internalNotes: e.target.value })}
                      rows={4}
                      placeholder="Notas adicionales..."
                      className="mt-1 text-sm"
                    />
                  </div>
                  {data.workflowInstanceId && (
                    <div className="bg-blue-50 border border-blue-200 rounded p-2">
                      <div className="flex items-center gap-2">
                        <Info className="h-3 w-3 text-blue-600" />
                        <div className="text-xs">
                          <div className="font-semibold">Workflow BPMN</div>
                          <div className="text-muted-foreground">
                            {data.workflowInstanceId} | {data.workflowStatus || "Activo"}
                          </div>
                        </div>
                      </div>
                    </div>
                  )}
                </CardBody>
              </Card>

              {/* Card 3: Historial */}
              <Card className="border-2">
                <CardHeader className="pb-3">
                  <CardTitle className="flex items-center gap-2 text-sm">
                    <History className="h-4 w-4 text-primary" />
                    Historial
                  </CardTitle>
                </CardHeader>
                <CardBody className="pt-0">
                  {data.previousClassifications && data.previousClassifications.length > 0 ? (
                    <div className="space-y-2 max-h-[calc(100vh-28rem)] overflow-y-auto">
                      {data.previousClassifications.map((prev, index) => (
                        <div key={index} className="border rounded-lg p-2">
                          <div className="flex items-center justify-between mb-1">
                            <Badge variant="outline" className="text-xs">{prev.category}</Badge>
                            <div className="text-xs text-muted-foreground">
                              {new Date(prev.date).toLocaleDateString()}
                            </div>
                          </div>
                          <p className="text-xs text-muted-foreground mb-1">Por: {prev.by}</p>
                          <p className="text-xs">{prev.reason}</p>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="border rounded-lg p-6 text-center text-muted-foreground text-sm">
                      Sin historial
                    </div>
                  )}
                </CardBody>
              </Card>
        </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}
