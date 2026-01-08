"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  FileText,
  Download,
  CheckCircle,
  AlertCircle,
  Sparkles,
  Save,
  X,
  FileCheck,
  RefreshCw,
  Edit,
  ArrowLeft,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import {
  mockTechnicalDocs,
  type TechnicalDocsData,
  type TechnicalDocSection,
} from "@/app/(app)/governance/data/mockTechnicalDocs";

export default function TechnicalDocsDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const modelId = params?.modelId ? parseInt(params.modelId as string) : null;

  const [data, setData] = useState<TechnicalDocsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedSection, setSelectedSection] = useState<string | null>(null);
  const [editingSection, setEditingSection] = useState<string | null>(null);
  const [editContent, setEditContent] = useState<string>("");
  const [generating, setGenerating] = useState(false);
  const [validating, setValidating] = useState(false);
  const [generatingPdf, setGeneratingPdf] = useState(false);

  useEffect(() => {
    if (modelId) {
      loadModelDocumentation(modelId);
    }
  }, [modelId]);

  const loadModelDocumentation = async (id: number) => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 300));
      // En producción: GET /api/compliance/technical-docs/[modelId]
      setData(mockTechnicalDocs);
      setSelectedSection(null);
      setEditingSection(null);
    } catch (error) {
      console.error("Error loading model documentation:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleGenerateDocumentation = async () => {
    if (!modelId) return;
    try {
      setGenerating(true);
      await new Promise((resolve) => setTimeout(resolve, 2000));
      // En producción: POST /api/compliance/technical-docs/[modelId]/generate
      alert(t("governance.compliance.technicalDocs.messages.generated"));
      await loadModelDocumentation(modelId);
    } catch (error) {
      console.error("Error generating documentation:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setGenerating(false);
    }
  };

  const handleValidateCompleteness = async () => {
    if (!modelId) return;
    try {
      setValidating(true);
      await new Promise((resolve) => setTimeout(resolve, 1500));
      // En producción: POST /api/compliance/technical-docs/[modelId]/validate
      alert(t("governance.compliance.technicalDocs.messages.validated"));
      await loadModelDocumentation(modelId);
    } catch (error) {
      console.error("Error validating completeness:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setValidating(false);
    }
  };

  const handleGeneratePdf = async () => {
    if (!modelId) return;
    try {
      setGeneratingPdf(true);
      await new Promise((resolve) => setTimeout(resolve, 2000));
      // En producción: POST /api/compliance/technical-docs/[modelId]/pdf
      alert(t("governance.compliance.technicalDocs.messages.pdfGenerated"));
      await loadModelDocumentation(modelId);
    } catch (error) {
      console.error("Error generating PDF:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setGeneratingPdf(false);
    }
  };

  const handleEditSection = (section: TechnicalDocSection) => {
    setEditingSection(section.name);
    setEditContent(section.content);
  };

  const handleSaveSection = async () => {
    if (!editingSection || !modelId || !data) return;
    try {
      // En producción: PUT /api/compliance/technical-docs/[modelId]/sections/[sectionName]
      const updatedSections = data.sections.map((s) =>
        s.name === editingSection
          ? { ...s, content: editContent, complete: editContent.trim().length > 50 }
          : s
      );
      setData({ ...data, sections: updatedSections });
      setEditingSection(null);
      setEditContent("");
      alert(t("governance.compliance.technicalDocs.messages.sectionSaved"));
    } catch (error) {
      console.error("Error saving section:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    }
  };

  const handleCancelEdit = () => {
    setEditingSection(null);
    setEditContent("");
  };

  if (loading && !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!data || !modelId) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <p className="text-muted-foreground mb-4">
            {t("governance.compliance.technicalDocs.modelNotFound", "Model not found")}
          </p>
          <Button onClick={() => (window.location.href = "/governance/compliance/technical-docs")}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("governance.compliance.traceability.back", "Back")}
          </Button>
        </div>
      </div>
    );
  }

  const completedSections = data.sections.filter((s) => s.complete).length;
  const totalSections = data.sections.length;
  const selectedSectionData =
    selectedSection && data ? data.sections.find((s) => s.name === selectedSection) : null;

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-teal-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-teal-400/25 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="text-left space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <Button
              variant="outline"
              size="sm"
              onClick={() => (window.location.href = "/governance/compliance/technical-docs")}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              {t("governance.compliance.traceability.back", "Back")}
            </Button>
            <FileText className="w-8 h-8 text-teal-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-teal-500 to-teal-700 bg-clip-text text-transparent">
              {data.modelName}
            </h1>
          </div>
          <p className="text-muted-foreground">{t("governance.compliance.technicalDocs.subtitle")}</p>
        </div>

        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>{t("governance.compliance.technicalDocs.modelInformation")}</CardTitle>
              <div className="flex items-center gap-2">
                <div className="text-right mr-4">
                  <div className="text-sm text-muted-foreground">
                    {t("governance.compliance.technicalDocs.completed")}
                  </div>
                  <div className="text-2xl font-bold">
                    {completedSections}/{totalSections}
                  </div>
                </div>
                <Button variant="outline" onClick={handleGenerateDocumentation} disabled={generating}>
                  {generating ? (
                    <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                  ) : (
                    <Sparkles className="h-4 w-4 mr-2" />
                  )}
                  {t("governance.compliance.technicalDocs.generate")}
                </Button>
                <Button variant="outline" onClick={handleValidateCompleteness} disabled={validating}>
                  {validating ? (
                    <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                  ) : (
                    <FileCheck className="h-4 w-4 mr-2" />
                  )}
                  {t("governance.compliance.technicalDocs.validateCompleteness")}
                </Button>
                <Button
                  variant="outline"
                  onClick={handleGeneratePdf}
                  disabled={generatingPdf || !data.isComplete}
                >
                  {generatingPdf ? (
                    <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                  ) : (
                    <Download className="h-4 w-4 mr-2" />
                  )}
                  {t("governance.compliance.technicalDocs.generatePdf")}
                </Button>
              </div>
            </div>
          </CardHeader>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.technicalDocs.modelName")}
                </div>
                <div className="font-semibold text-lg">{data.modelName}</div>
              </div>
              <div className="text-right">
                <div className="text-sm text-muted-foreground">
                  {t("governance.compliance.technicalDocs.overallScore")}
                </div>
                <div className="text-2xl font-bold">{(data.overallScore * 100).toFixed(0)}%</div>
              </div>
            </div>
            <div className="mt-4 w-full bg-muted rounded-full h-2">
              <div
                className="bg-teal-500 h-2 rounded-full transition-all"
                style={{ width: `${data.overallScore * 100}%` }}
              />
            </div>
          </CardBody>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Card className="lg:col-span-1 backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>{t("governance.compliance.technicalDocs.sections")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-2 max-h-[600px] overflow-y-auto">
                {data.sections.map((section) => (
                  <div
                    key={section.name}
                    className={`p-3 border rounded-lg cursor-pointer transition-colors ${
                      selectedSection === section.name
                        ? "border-teal-500 bg-teal-500/5"
                        : "border-border hover:border-teal-500/50"
                    }`}
                    onClick={() => setSelectedSection(section.name)}
                  >
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <Badge variant="outline">{section.id}</Badge>
                          <span className="font-semibold text-sm">
                            {t(`governance.compliance.technicalDocs.sectionNames.${section.name}`) ||
                              section.displayName}
                          </span>
                        </div>
                        <p className="text-xs text-muted-foreground">
                          {t(
                            `governance.compliance.technicalDocs.sectionDescriptions.${section.name}`
                          ) || section.description}
                        </p>
                      </div>
                      <div className="ml-2">
                        {section.complete ? (
                          <CheckCircle className="h-4 w-4 text-green-600" />
                        ) : (
                          <AlertCircle className="h-4 w-4 text-yellow-600" />
                        )}
                      </div>
                    </div>
                    <div className="mt-2 text-xs text-muted-foreground">
                      {t("governance.compliance.technicalDocs.score")}:{" "}
                      {(section.score * 100).toFixed(0)}%
                    </div>
                  </div>
                ))}
              </div>
            </CardBody>
          </Card>

          <Card className="lg:col-span-2 backdrop-blur-md bg-background/60 border-border/50">
            <CardHeader>
              <CardTitle>
                {selectedSectionData
                  ? `${t("governance.compliance.technicalDocs.section")} ${selectedSectionData.id}: ${
                      t(`governance.compliance.technicalDocs.sectionNames.${selectedSectionData.name}`) ||
                      selectedSectionData.displayName
                    }`
                  : t("governance.compliance.technicalDocs.viewDetails")}
              </CardTitle>
            </CardHeader>
            <CardBody>
              {selectedSectionData ? (
                <div className="space-y-4">
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <Badge variant={selectedSectionData.complete ? "success" : "secondary"}>
                        {selectedSectionData.complete
                          ? t("governance.compliance.technicalDocs.completed")
                          : t("governance.compliance.technicalDocs.incomplete")}
                      </Badge>
                      <div className="text-sm text-muted-foreground">
                        {t("governance.compliance.technicalDocs.score")}:{" "}
                        {(selectedSectionData.score * 100).toFixed(0)}%
                      </div>
                    </div>
                    <p className="text-sm text-muted-foreground mb-4">
                      {t(
                        `governance.compliance.technicalDocs.sectionDescriptions.${selectedSectionData.name}`
                      ) || selectedSectionData.description}
                    </p>
                  </div>

                  <div>
                    <div className="text-sm font-medium mb-2">
                      {t("governance.compliance.technicalDocs.content")}
                    </div>
                    {editingSection === selectedSectionData.name ? (
                      <div className="space-y-2">
                        <Textarea
                          value={editContent}
                          onChange={(e) => setEditContent(e.target.value)}
                          rows={12}
                          className="font-mono text-sm"
                        />
                        <div className="flex gap-2">
                          <Button onClick={handleSaveSection} className="flex-1">
                            <Save className="h-4 w-4 mr-2" />
                            {t("governance.compliance.technicalDocs.save")}
                          </Button>
                          <Button variant="outline" onClick={handleCancelEdit} className="flex-1">
                            <X className="h-4 w-4 mr-2" />
                            {t("governance.compliance.technicalDocs.cancel")}
                          </Button>
                        </div>
                      </div>
                    ) : (
                      <div className="p-4 bg-muted rounded-lg text-sm whitespace-pre-wrap">
                        {selectedSectionData.content}
                      </div>
                    )}
                  </div>

                  {editingSection !== selectedSectionData.name && (
                    <div className="flex gap-2">
                      <Button
                        variant="outline"
                        className="flex-1"
                        onClick={() => handleEditSection(selectedSectionData)}
                      >
                        <Edit className="h-4 w-4 mr-2" />
                        {t("governance.compliance.technicalDocs.edit")}
                      </Button>
                      <Button variant="outline" className="flex-1">
                        <FileCheck className="h-4 w-4 mr-2" />
                        {t("governance.compliance.technicalDocs.validate")}
                      </Button>
                    </div>
                  )}
                </div>
              ) : (
                <div className="text-center py-12 text-muted-foreground">
                  {t("governance.compliance.technicalDocs.viewDetails")}
                </div>
              )}
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}
