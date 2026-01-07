"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Award,
  FileText,
  Download,
  RefreshCw,
  CheckCircle,
  Clock,
  ArrowLeft,
  FolderOpen,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useSearchParams, useRouter } from "next/navigation";

interface Assessment {
  idxcomplianceassessment: number;
  assessmentname: string;
  projectId?: number;
  projectName?: string;
}

interface Declaration {
  id: number;
  projectId: number;
  projectName?: string;
  system_name: string;
  provider_name: string;
  declaration_date: string;
  status: "DRAFT" | "SIGNED";
  version?: string;
}

interface DeclarationPreview {
  providerName: string;
  systemName: string;
  complianceScore: number;
  articlesCompliance: string;
}

interface ConformityDeclarationData {
  projectId: number;
  projectName: string;
  assessments: Assessment[];
  selectedAssessmentId: number | null;
  generating: boolean;
  preview: DeclarationPreview | null;
  declarations: Declaration[];
}

const DEFAULT_PROJECT_ID = 1;

const mockAssessments: Assessment[] = [
  {
    idxcomplianceassessment: 1,
    assessmentname: "AI System - Healthcare Diagnostics",
    projectId: 1,
    projectName: "Healthcare AI Project",
  },
  {
    idxcomplianceassessment: 2,
    assessmentname: "AI System - Financial Fraud Detection",
    projectId: 1,
    projectName: "Healthcare AI Project",
  },
  {
    idxcomplianceassessment: 3,
    assessmentname: "AI System - Manufacturing Quality Control",
    projectId: 2,
    projectName: "Manufacturing AI Project",
  },
];

const mockDeclarations: Declaration[] = [
  {
    id: 1,
    projectId: 1,
    projectName: "Healthcare AI Project",
    system_name: "Healthcare Diagnostics AI",
    provider_name: "TechCorp Medical",
    declaration_date: "2025-01-15",
    status: "SIGNED",
    version: "v1.0",
  },
  {
    id: 2,
    projectId: 1,
    projectName: "Healthcare AI Project",
    system_name: "Healthcare Diagnostics AI",
    provider_name: "TechCorp Medical",
    declaration_date: "2025-01-20",
    status: "DRAFT",
    version: "v1.1",
  },
  {
    id: 3,
    projectId: 2,
    projectName: "Manufacturing AI Project",
    system_name: "Manufacturing Quality Control",
    provider_name: "ManufacturingAI Solutions",
    declaration_date: "2025-01-14",
    status: "SIGNED",
    version: "v1.0",
  },
];

const mockPreview: DeclarationPreview = {
  providerName: "TechCorp Medical",
  systemName: "Healthcare Diagnostics AI",
  complianceScore: 87.5,
  articlesCompliance: "Art. 9, 10, 11, 12, 13, 14, 15",
};

export default function ConformityDeclarationManagerPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const [data, setData] = useState<ConformityDeclarationData>({
    projectId: DEFAULT_PROJECT_ID,
    projectName: "",
    assessments: [],
    selectedAssessmentId: null,
    generating: false,
    preview: null,
    declarations: [],
  });

  // Obtener projectId de la URL o usar valor por defecto
  const getProjectId = (): number => {
    if (searchParams) {
      const projectIdParam = searchParams.get("projectId");
      if (projectIdParam) {
        const parsed = parseInt(projectIdParam, 10);
        if (!isNaN(parsed)) {
          return parsed;
        }
      }
    }
    return DEFAULT_PROJECT_ID;
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  const loadData = async () => {
    try {
      const projectId = getProjectId();

      const response = await fetch(`/api/governance/compliance/conformity-declaration/manager?projectId=${projectId}`);

      if (!response.ok) {
        throw new Error("Failed to load data");
      }

      const result = await response.json();

      if (result.success && result.data) {
        const managerData = result.data;
        setData({
          projectId: managerData.projectId || projectId,
          projectName: managerData.projectName || `Project ${projectId}`,
          assessments: managerData.assessments?.map((a: any) => ({
            idxcomplianceassessment: a.idxcomplianceassessment || a.idxcomplianceassessment,
            assessmentname: a.assessmentname || a.comassessmentname || "Assessment",
            projectId: a.idxproject || projectId,
            projectName: managerData.projectName,
          })) || [],
          selectedAssessmentId: null,
          generating: false,
          preview: managerData.preview || null,
          declarations: managerData.declarations?.map((d: any) => ({
            id: d.idxDeclaration || d.id,
            projectId: d.projectId || projectId,
            projectName: d.projectName || managerData.projectName,
            system_name: d.aiSystemName || d.system_name || "System",
            provider_name: d.providerName || d.provider_name || "Provider",
            declaration_date: d.createdAt ? new Date(d.createdAt).toISOString().split("T")[0] :
                             d.declaration_date || new Date().toISOString().split("T")[0],
            status: d.status || "DRAFT",
            version: d.aiSystemVersion || d.version || "v1.0",
          })) || [],
        });
      } else {
        // Fallback a datos mock si falla
        const projectAssessments = mockAssessments.filter(a => a.projectId === projectId);
        const projectDeclarations = mockDeclarations.filter(d => d.projectId === projectId);
        const projectName = projectAssessments[0]?.projectName || projectDeclarations[0]?.projectName || `Project ${projectId}`;

        setData({
          projectId: projectId,
          projectName: projectName,
          assessments: projectAssessments,
          selectedAssessmentId: null,
          generating: false,
          preview: null,
          declarations: projectDeclarations,
        });
      }
    } catch (error) {
      console.error("Error loading data:", error);
      // Fallback a datos mock en caso de error
      const projectId = getProjectId();
      const projectAssessments = mockAssessments.filter(a => a.projectId === projectId);
      const projectDeclarations = mockDeclarations.filter(d => d.projectId === projectId);
      const projectName = projectAssessments[0]?.projectName || projectDeclarations[0]?.projectName || `Project ${projectId}`;

      setData({
        projectId: projectId,
        projectName: projectName,
        assessments: projectAssessments,
        selectedAssessmentId: null,
        generating: false,
        preview: null,
        declarations: projectDeclarations,
      });
    }
  };

  const handleSelectAssessment = (assessmentId: number) => {
    // Simular preview basado en el assessment seleccionado
    setData({
      ...data,
      selectedAssessmentId: assessmentId,
      preview: mockPreview,
    });
  };

  const handleGenerateDeclaration = async () => {
    if (!data.selectedAssessmentId) return;

    try {
      setData({ ...data, generating: true });

      const response = await fetch("/api/governance/compliance/conformity-declaration", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          assessmentId: data.selectedAssessmentId,
          providerName: data.preview?.providerName || "Provider",
          aiSystemName: data.preview?.systemName || "AI System",
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to generate declaration");
      }

      const result = await response.json();

      if (result.success && result.data) {
        const newDeclaration: Declaration = {
          id: result.data.idxDeclaration || result.data.id,
          projectId: result.data.projectId || data.projectId,
          projectName: result.data.projectName || data.projectName,
          system_name: result.data.aiSystemName || result.data.system_name || "New System",
          provider_name: result.data.providerName || result.data.provider_name || "Provider",
          declaration_date: result.data.createdAt ? new Date(result.data.createdAt).toISOString().split("T")[0] :
                           new Date().toISOString().split("T")[0],
          status: result.data.status || "DRAFT",
          version: result.data.aiSystemVersion || result.data.version || "v1.0",
        };
        setData({
          ...data,
          generating: false,
          declarations: [newDeclaration, ...data.declarations],
        });
        alert(t("governance.compliance.conformity.conformityDeclaration.declarationGenerated", "Declaración generada exitosamente"));
      } else {
        throw new Error(result.error || "Failed to generate declaration");
      }
    } catch (error) {
      console.error("Error generating declaration:", error);
      setData({ ...data, generating: false });
      alert(t("governance.compliance.conformity.conformityDeclaration.declarationGenerated", "Error al generar declaración"));
    }
  };

  const handleSignDeclaration = async (declId: number) => {
    try {
      const response = await fetch("/api/governance/compliance/conformity-declaration/sign", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          declarationId: declId,
          signedBy: "Current User", // TODO: Obtener del contexto de usuario
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to sign declaration");
      }

      const result = await response.json();

      if (result.success && result.data) {
        setData({
          ...data,
          declarations: data.declarations.map((decl) =>
            decl.id === declId ? { ...decl, status: "SIGNED" as const } : decl
          ),
        });
        alert(t("governance.compliance.conformity.conformityDeclaration.declarationSigned", "Declaración firmada exitosamente"));
      } else {
        throw new Error(result.error || "Failed to sign declaration");
      }
    } catch (error) {
      console.error("Error signing declaration:", error);
      alert(t("governance.compliance.conformity.conformityDeclaration.declarationSigned", "Error al firmar declaración"));
    }
  };

  const handleDownloadDeclaration = async (declId: number) => {
    try {
      const response = await fetch(`/api/governance/compliance/conformity-declaration/${declId}/pdf`);

      if (!response.ok) {
        throw new Error("Failed to download PDF");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `declaration_${declId}.pdf`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    } catch (error) {
      console.error("Error downloading declaration:", error);
      alert(`${t("governance.compliance.conformity.conformityDeclaration.downloading", "Error al descargar declaración")}`);
    }
  };

  const signedCount = data.declarations.filter((d) => d.status === "SIGNED").length;
  const draftCount = data.declarations.filter((d) => d.status === "DRAFT").length;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10 w-full max-w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-4">
          <div className="flex items-center justify-between gap-4">
            <div className="flex items-center gap-3">
              <Button
                variant="outline"
                onClick={() => {
                  if (data.projectId) {
                    window.location.href = "/governance/compliance/conformity-declaration/projects";
                  } else {
                    window.location.href = "/governance/compliance/dashboard";
                  }
                }}
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                {t("common.back", "Volver")}
              </Button>
              <div className="flex items-center gap-3">
                <Award className="w-8 h-8 text-indigo-500" />
                <div>
                  <h1 className="text-3xl font-bold text-foreground">
                    {t(
                      "governance.compliance.conformity.conformityDeclaration.title",
                      "EU Declaration of Conformity Manager"
                    )}
                  </h1>
                  <p className="text-muted-foreground text-sm mt-1">
                    {t(
                      "governance.compliance.conformity.conformityDeclaration.subtitle",
                      "Gestión de declaraciones de conformidad según EU AI Act Annex V"
                    )}
                  </p>
                  {data.projectId && data.projectName && (
                    <div className="mt-2">
                      <Badge variant="outline" className="text-xs">
                        {t("governance.compliance.conformity.conformityDeclaration.project", "Proyecto")}: {data.projectName}
                      </Badge>
                      <p className="text-xs text-muted-foreground mt-1">
                        {t("governance.compliance.conformity.conformityDeclaration.projectDeclarations", "Este proyecto puede tener múltiples declaraciones de conformidad (versiones, actualizaciones, etc.)")}
                      </p>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Información del Proyecto */}
            {data.projectName && (
              <div className="flex items-center gap-3 p-4 rounded-lg border bg-card">
                <FolderOpen className="w-5 h-5 text-primary" />
                <div>
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.conformity.conformityDeclaration.project", "Proyecto")}
                  </div>
                  <div className="font-semibold text-lg">
                    {data.projectName} <span className="text-muted-foreground text-sm">(ID: {data.projectId})</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">
                    {t("governance.compliance.conformity.conformityDeclaration.projectDeclarations", "Este proyecto puede tener múltiples declaraciones de conformidad (versiones, actualizaciones, etc.)")}
                  </div>
                </div>
              </div>
            )}

            {/* Estadísticas en el header */}
            <div className="flex items-center gap-6">
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.conformity.conformityDeclaration.totalDeclarations", "Total")}</div>
                <div className="font-semibold text-lg">{data.declarations.length}</div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.conformity.conformityDeclaration.signed", "Firmadas")}</div>
                <div className="font-semibold text-lg text-green-600">{signedCount}</div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.conformity.conformityDeclaration.drafts", "Borradores")}</div>
                <div className="font-semibold text-lg text-yellow-600">{draftCount}</div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.conformity.conformityDeclaration.availableAssessments", "Assessments")}</div>
                <div className="font-semibold text-lg">{data.assessments.length}</div>
              </div>
            </div>
          </div>
        </div>

        {/* Selección y Generación */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Generate New Declaration */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardHeader>
              <CardTitle>{t("governance.compliance.conformity.conformityDeclaration.generateNew", "Generar Nueva Declaración")}</CardTitle>
            </CardHeader>
            <CardBody className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">
                  {t("governance.compliance.conformity.conformityDeclaration.selectAssessmentRequired", "Seleccionar Assessment")} <span className="text-red-500">*</span>
                </label>
                <select
                  value={data.selectedAssessmentId || ""}
                  onChange={(e) =>
                    handleSelectAssessment(Number(e.target.value))
                  }
                  className="w-full px-3 py-2 border rounded-md bg-background"
                >
                  <option value="">{t("governance.compliance.conformity.conformityDeclaration.selectAssessmentPlaceholder", "Selecciona un assessment conforme...")}</option>
                  {data.assessments.map((assessment) => (
                    <option
                      key={assessment.idxcomplianceassessment}
                      value={assessment.idxcomplianceassessment}
                    >
                      {assessment.assessmentname}
                    </option>
                  ))}
                </select>
              </div>
              <Button
                onClick={handleGenerateDeclaration}
                disabled={data.generating || !data.selectedAssessmentId}
                className="w-full"
              >
                {data.generating ? (
                  <>
                    <RefreshCw className="w-4 h-4 mr-2 animate-spin" />
                    {t("governance.compliance.conformity.conformityDeclaration.generating", "Generando...")}
                  </>
                ) : (
                  <>
                    <Award className="w-4 h-4 mr-2" />
                    {t("governance.compliance.conformity.conformityDeclaration.generate", "Generar Declaración")}
                  </>
                )}
              </Button>
            </CardBody>
          </Card>

          {/* Declaration Preview */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl transition-all duration-300">
            <CardHeader>
              <CardTitle>{t("governance.compliance.conformity.conformityDeclaration.declarationPreview", "Vista Previa de Declaración")}</CardTitle>
            </CardHeader>
            <CardBody>
              {data.preview ? (
                <div className="space-y-3">
                  <div>
                    <span className="font-semibold">{t("governance.compliance.conformity.conformityDeclaration.provider", "Proveedor")}:</span>
                    <p className="text-muted-foreground">{data.preview.providerName}</p>
                  </div>
                  <div>
                    <span className="font-semibold">{t("governance.compliance.conformity.conformityDeclaration.aiSystem", "Sistema de IA")}:</span>
                    <p className="text-muted-foreground">{data.preview.systemName}</p>
                  </div>
                  <div>
                    <span className="font-semibold">{t("governance.compliance.conformity.conformityDeclaration.complianceScore", "Score de Conformidad")}:</span>
                    <p className="text-muted-foreground">{data.preview.complianceScore}%</p>
                  </div>
                  <div>
                    <span className="font-semibold">{t("governance.compliance.conformity.conformityDeclaration.articles", "Artículos")}:</span>
                    <p className="text-muted-foreground">{data.preview.articlesCompliance}</p>
                  </div>
                </div>
              ) : (
                <p className="text-muted-foreground text-center py-4">
                  {t("governance.compliance.conformity.conformityDeclaration.selectAssessmentForPreview", "Selecciona un assessment para ver la vista previa")}
                </p>
              )}
            </CardBody>
          </Card>
        </div>

        {/* Lista de Declaraciones */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>{t("governance.compliance.conformity.conformityDeclaration.existingDeclarations", "Declaraciones Existentes")}</CardTitle>
              <Button onClick={loadData} variant="outline" size="sm">
                <RefreshCw className="w-4 h-4" />
              </Button>
            </div>
          </CardHeader>
          <CardBody>
            {data.declarations.length === 0 ? (
              <p className="text-center text-muted-foreground py-8">
                {t("governance.compliance.conformity.conformityDeclaration.noDeclarations", "Aún no hay declaraciones")}
              </p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.id", "ID")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.systemName", "Nombre del Sistema")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.version", "Versión")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.providerName", "Proveedor")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.date", "Fecha")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.status", "Estado")}</th>
                      <th className="text-left p-2">{t("governance.compliance.conformity.conformityDeclaration.actions", "Acciones")}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.declarations.map((decl) => (
                      <tr key={decl.id} className="border-b hover:bg-muted/50">
                        <td className="p-2">{decl.id}</td>
                        <td className="p-2">{decl.system_name}</td>
                        <td className="p-2">
                          <Badge variant="outline">{decl.version || "v1.0"}</Badge>
                        </td>
                        <td className="p-2">{decl.provider_name}</td>
                        <td className="p-2">{decl.declaration_date}</td>
                        <td className="p-2">
                          <Badge
                            className={
                              decl.status === "SIGNED"
                                ? "bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300"
                                : "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-300"
                            }
                          >
                            {decl.status === "SIGNED" ? (
                              <>
                                <CheckCircle className="w-3 h-3 mr-1 inline" />
                                {t("governance.compliance.conformity.conformityDeclaration.signedStatus", "FIRMADA")}
                              </>
                            ) : (
                              <>
                                <Clock className="w-3 h-3 mr-1 inline" />
                                {t("governance.compliance.conformity.conformityDeclaration.draftStatus", "BORRADOR")}
                              </>
                            )}
                          </Badge>
                        </td>
                        <td className="p-2">
                          <div className="flex items-center gap-2">
                            {decl.status === "DRAFT" && (
                              <Button
                                onClick={() => handleSignDeclaration(decl.id)}
                                variant="outline"
                                size="sm"
                                title={t("governance.compliance.conformity.conformityDeclaration.sign", "Firmar")}
                              >
                                <FileText className="w-4 h-4" />
                              </Button>
                            )}
                            <Button
                              onClick={() => handleDownloadDeclaration(decl.id)}
                              variant="outline"
                              size="sm"
                              title={t("governance.compliance.conformity.conformityDeclaration.download", "Descargar PDF")}
                            >
                              <Download className="w-4 h-4" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
