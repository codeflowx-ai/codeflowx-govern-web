"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import {
  FileText,
  CheckCircle,
  AlertCircle,
  Save,
  FileCheck,
  Download,
  RefreshCw,
  ClipboardCheck,
} from "lucide-react";
import { useEffect, useState } from "react";
import { mockCompleteDocumentation } from "@/app/(app)/governance/data/mockTechnicalDocs";

interface IncompleteSection {
  name: string;
  displayName: string;
  currentContent: string;
  required: boolean;
}

interface CompleteDocumentationData {
  modelId: number;
  modelName: string;
  currentScore: number;
  incompleteSections: IncompleteSection[];
  canComplete: boolean;
}

export default function CompleteDocumentationPage() {
  const { t } = useTranslation();
  const [data, setData] = useState<CompleteDocumentationData | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [validating, setValidating] = useState(false);
  const [generatingPdf, setGeneratingPdf] = useState(false);
  const [sectionContents, setSectionContents] = useState<Record<string, string>>({});

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      // In production: GET /api/bpmn/tasks/[taskId] or GET /api/compliance/technical-docs/complete/[modelId]
      await new Promise((resolve) => setTimeout(resolve, 500));
      const mockData = mockCompleteDocumentation;
      setData(mockData);
      // Initialize section contents
      const contents: Record<string, string> = {};
      mockData.incompleteSections.forEach((section) => {
        contents[section.name] = section.currentContent;
      });
      setSectionContents(contents);
    } catch (error) {
      console.error("Error loading documentation data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveSection = async (sectionName: string) => {
    if (!data) return;
    try {
      setSaving(true);
      // In production: PUT /api/compliance/technical-docs/[modelId]/sections/[sectionName]
      await new Promise((resolve) => setTimeout(resolve, 800));
      alert(t("governance.compliance.technicalDocs.messages.sectionSaved"));
      await loadData();
    } catch (error) {
      console.error("Error saving section:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setSaving(false);
    }
  };

  const handleValidateCompleteness = async () => {
    if (!data) return;
    try {
      setValidating(true);
      // In production: POST /api/compliance/technical-docs/[data.modelId]/validate
      await new Promise((resolve) => setTimeout(resolve, 1500));
      alert(t("governance.compliance.technicalDocs.messages.validated"));
      await loadData();
    } catch (error) {
      console.error("Error validating completeness:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setValidating(false);
    }
  };

  const handleGeneratePdf = async () => {
    if (!data) return;
    try {
      setGeneratingPdf(true);
      // In production: POST /api/compliance/technical-docs/[data.modelId]/pdf
      await new Promise((resolve) => setTimeout(resolve, 2000));
      alert(t("governance.compliance.technicalDocs.messages.pdfGenerated"));
      await loadData();
    } catch (error) {
      console.error("Error generating PDF:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setGeneratingPdf(false);
    }
  };

  const handleMarkComplete = async () => {
    if (!data || !data.canComplete) return;
    try {
      setSaving(true);
      // In production: POST /api/compliance/technical-docs/[data.modelId]/complete
      // Also complete BPMN task: POST /api/bpmn/tasks/[taskId]/complete
      await new Promise((resolve) => setTimeout(resolve, 1000));
      alert(t("governance.compliance.technicalDocs.messages.saved"));
      // Redirect or reload
      await loadData();
    } catch (error) {
      console.error("Error marking as complete:", error);
      alert(t("governance.compliance.technicalDocs.messages.error"));
    } finally {
      setSaving(false);
    }
  };

  if (loading || !data) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  const allSectionsComplete = data.incompleteSections.every(
    (section) => sectionContents[section.name]?.trim().length > 50
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-teal-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-teal-400/25 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-left space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <ClipboardCheck className="w-8 h-8 text-teal-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-teal-500 to-teal-700 bg-clip-text text-transparent">
              {t("governance.compliance.conformityReview.completeSections")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.conformityReview.subtitle")}
          </p>
        </div>

        {/* Model Information */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.compliance.technicalDocs.modelInformation")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <div className="text-sm text-muted-foreground">{t("governance.compliance.technicalDocs.modelName")}</div>
                <div className="font-semibold text-lg">{data.modelName}</div>
              </div>
              <div>
                <div className="text-sm text-muted-foreground">{t("governance.compliance.technicalDocs.overallScore")}</div>
                <div className="text-2xl font-bold">
                  {(data.currentScore * 100).toFixed(0)}%
                </div>
              </div>
              <div>
                <div className="text-sm text-muted-foreground">{t("governance.compliance.conformityReview.canComplete")}</div>
                <div className="mt-1">
                  {data.canComplete ? (
                    <Badge className="bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300">
                      <CheckCircle className="w-3 h-3 mr-1 inline" />
                      {t("governance.compliance.conformityReview.canComplete")}
                    </Badge>
                  ) : (
                    <Badge variant="secondary">
                      <AlertCircle className="w-3 h-3 mr-1 inline" />
                      {t("governance.compliance.conformityReview.cannotComplete")}
                    </Badge>
                  )}
                </div>
              </div>
            </div>
            <div className="mt-4 w-full bg-muted rounded-full h-2">
              <div
                className="bg-teal-500 h-2 rounded-full transition-all"
                style={{ width: `${data.currentScore * 100}%` }}
              />
            </div>
          </CardBody>
        </Card>

        {/* Incomplete Sections */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.compliance.conformityReview.incompleteSections")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-6">
              {data.incompleteSections.map((section) => (
                <div key={section.name} className="border rounded-lg p-4 space-y-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="font-semibold text-lg">
                        {t(`governance.compliance.technicalDocs.sectionNames.${section.name}`) || section.displayName}
                      </h3>
                      <p className="text-sm text-muted-foreground">
                        {t(`governance.compliance.technicalDocs.sectionDescriptions.${section.name}`)}
                      </p>
                    </div>
                    {section.required && (
                      <Badge variant="danger">{t("governance.compliance.conformityReview.required")}</Badge>
                    )}
                  </div>
                  <div>
                    <label className="block text-sm font-medium mb-2">
                      {t("governance.compliance.conformityReview.currentContent")}
                    </label>
                    <Textarea
                      value={sectionContents[section.name] || ""}
                      onChange={(e) =>
                        setSectionContents({
                          ...sectionContents,
                          [section.name]: e.target.value,
                        })
                      }
                      rows={8}
                      className="font-mono text-sm"
                      placeholder={t(`governance.compliance.technicalDocs.sectionDescriptions.${section.name}`)}
                    />
                  </div>
                  <div className="flex justify-end">
                    <Button
                      onClick={() => handleSaveSection(section.name)}
                      disabled={saving || !sectionContents[section.name]?.trim()}
                    >
                      {saving ? (
                        <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                      ) : (
                        <Save className="h-4 w-4 mr-2" />
                      )}
                      {t("governance.compliance.technicalDocs.save")}
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Actions */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.compliance.technicalDocs.actions")}</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="flex flex-wrap gap-4">
              <Button
                onClick={handleValidateCompleteness}
                disabled={validating}
                variant="outline"
              >
                {validating ? (
                  <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                ) : (
                  <FileCheck className="h-4 w-4 mr-2" />
                )}
                {t("governance.compliance.technicalDocs.validateCompleteness")}
              </Button>
              <Button
                onClick={handleGeneratePdf}
                disabled={generatingPdf || !data.canComplete}
                variant="outline"
              >
                {generatingPdf ? (
                  <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                ) : (
                  <Download className="h-4 w-4 mr-2" />
                )}
                {t("governance.compliance.technicalDocs.generatePdf")}
              </Button>
              <Button
                onClick={handleMarkComplete}
                disabled={saving || !data.canComplete || !allSectionsComplete}
                className="bg-teal-600 hover:bg-teal-700"
              >
                {saving ? (
                  <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                ) : (
                  <CheckCircle className="h-4 w-4 mr-2" />
                )}
                {t("governance.compliance.technicalDocs.markComplete")}
              </Button>
            </div>
            {!allSectionsComplete && (
              <div className="mt-4 p-3 bg-yellow-50 dark:bg-yellow-900/20 border border-yellow-200 dark:border-yellow-800 rounded-lg">
                <p className="text-sm text-yellow-800 dark:text-yellow-200">
                  {t("governance.compliance.conformityReview.cannotComplete")}
                </p>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
