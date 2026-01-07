"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import {
  AlertTriangle,
  ArrowLeft,
  CheckCircle,
  ClipboardCheck,
  FolderOpen,
  UserCheck,
  XCircle
} from "lucide-react";
import { useSearchParams } from "next/navigation";
import { Suspense, useEffect, useState } from "react";
import { mockQmsData, QmsGap } from "../../data/mockQms";

interface QmsGapWithAcceptable extends QmsGap {
  isAcceptable?: boolean;
}

interface QmsGapsReviewData {
  projectId: number;
  projectName: string;
  overallScore: number;
  gaps: QmsGapWithAcceptable[];
  reviewerName: string;
  reviewNotes: string;
  decision: "PENDING" | "APPROVED" | "CORRECTIONS_REQUIRED" | null;
}

const DEFAULT_PROJECT_ID = 1;

const mockReviewData: QmsGapsReviewData = {
  projectId: mockQmsData.projectId,
  projectName: mockQmsData.projectName,
  overallScore: mockQmsData.overallScore,
  gaps: mockQmsData.gaps,
  reviewerName: "",
  reviewNotes: "",
  decision: null,
};

function ConformityReviewPageContent() {
  const { t, language } = useTranslation();
  const searchParams = useSearchParams();
  const [data, setData] = useState<QmsGapsReviewData>(mockReviewData);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  // Helper functions
  const getProjectId = (): number => {
    const projectIdParam = searchParams.get("projectId");
    if (projectIdParam) {
      const parsed = parseInt(projectIdParam, 10);
      if (!isNaN(parsed)) {
        return parsed;
      }
    }
    return DEFAULT_PROJECT_ID;
  };

  const getCurrentUserName = (): string => {
    if (typeof window !== "undefined") {
      const userName = localStorage.getItem("userName") ||
                      localStorage.getItem("user-name") ||
                      localStorage.getItem("currentUserName") ||
                      sessionStorage.getItem("userName") ||
                      sessionStorage.getItem("user-name");

      if (userName) {
        return userName;
      }

      const userStr = localStorage.getItem("user") || sessionStorage.getItem("user");
      if (userStr) {
        try {
          const user = JSON.parse(userStr);
          return user.name || user.fullName || user.username || user.displayName || "";
        } catch (e) {
          // Ignore parse errors
        }
      }
    }
    return "";
  };

  const loadReviewData = async () => {
    try {
      setLoading(true);

      // Obtener projectId de la URL o contexto
      const projectId = getProjectId();

      // Obtener nombre del usuario actual
      const currentUserName = getCurrentUserName();

      // Cargar gaps del QMS
      const gapsResponse = await fetch(`/api/governance/compliance/qms/gaps?projectId=${projectId}`);
      if (gapsResponse.ok) {
        const gapsResult = await gapsResponse.json();
        if (gapsResult.success) {
          // Obtener datos del proyecto desde el endpoint principal
          const qmsResponse = await fetch(`/api/governance/compliance/qms?projectId=${projectId}`);
          let projectName = `Project ${projectId}`;
          if (qmsResponse.ok) {
            const qmsResult = await qmsResponse.json();
            if (qmsResult.success && qmsResult.data) {
              projectName = qmsResult.data.projectName || projectName;
            }
          }

          setData({
            ...mockReviewData,
            projectId: projectId,
            projectName: projectName,
            gaps: gapsResult.data.gaps,
            overallScore: gapsResult.data.overallScore,
            reviewerName: currentUserName || mockReviewData.reviewerName,
          });
          return;
        }
      }

      // Fallback a mock data
      setData({
        ...mockReviewData,
        projectId: projectId,
        projectName: `Project ${projectId}`,
        reviewerName: currentUserName || mockReviewData.reviewerName,
      });
    } catch (error) {
      console.error("Error loading review data:", error);
      // Fallback a mock data en caso de error
      const currentUserName = getCurrentUserName();
      setData({
        ...mockReviewData,
        reviewerName: currentUserName || mockReviewData.reviewerName,
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const currentUserName = getCurrentUserName();
    if (currentUserName) {
      setData(prev => ({ ...prev, reviewerName: currentUserName }));
    }
    loadReviewData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchParams]);

  const handleApproveReview = async () => {
    if (!data.reviewNotes.trim()) {
      alert(t("governance.compliance.qms.review.requiredFields", "Las notas de revisión son requeridas"));
      return;
    }
    if (!data.reviewerName.trim()) {
      alert(t("governance.compliance.qms.review.reviewerRequired", "No se pudo obtener el nombre del revisor. Por favor, recargue la página."));
      return;
    }

    try {
      setSubmitting(true);

      const response = await fetch("/api/governance/compliance/qms/review", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          projectId: data.projectId,
          reviewerName: data.reviewerName,
          reviewNotes: data.reviewNotes,
          decision: "APPROVED",
          // TODO: Obtener taskId de la tarea BPMN si está disponible
          // taskId: taskId,
        }),
      });

      if (response.ok) {
        const result = await response.json();
        if (result.success) {
          alert(t("governance.compliance.qms.review.approvedSuccess", "QMS aprobado exitosamente"));
          setData({ ...data, decision: "APPROVED", reviewerName: "", reviewNotes: "" });
        } else {
          throw new Error(result.error || "Failed to approve QMS");
        }
      } else {
        throw new Error("Failed to approve QMS");
      }
    } catch (error) {
      console.error("Error approving review:", error);
      alert(t("governance.compliance.qms.review.error", "Error al aprobar la revisión"));
    } finally {
      setSubmitting(false);
    }
  };

  const handleRejectReview = async () => {
    if (!data.reviewNotes.trim()) {
      alert(t("governance.compliance.qms.review.requiredFields", "Las notas de revisión son requeridas"));
      return;
    }
    if (!data.reviewerName.trim()) {
      alert(t("governance.compliance.qms.review.reviewerRequired", "No se pudo obtener el nombre del revisor. Por favor, recargue la página."));
      return;
    }

    try {
      setSubmitting(true);

      const response = await fetch("/api/governance/compliance/qms/review", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          projectId: data.projectId,
          reviewerName: data.reviewerName,
          reviewNotes: data.reviewNotes,
          decision: "CORRECTIONS_REQUIRED",
          // TODO: Obtener taskId de la tarea BPMN si está disponible
          // taskId: taskId,
        }),
      });

      if (response.ok) {
        const result = await response.json();
        if (result.success) {
          alert(t("governance.compliance.qms.review.correctionsRequired", "Se solicitaron correcciones"));
          setData({ ...data, decision: "CORRECTIONS_REQUIRED", reviewerName: "", reviewNotes: "" });
        } else {
          throw new Error(result.error || "Failed to request corrections");
        }
      } else {
        throw new Error("Failed to request corrections");
      }
    } catch (error) {
      console.error("Error rejecting review:", error);
      alert(t("governance.qms.review.error", "Error al procesar la revisión"));
    } finally {
      setSubmitting(false);
    }
  };

  const handleMarkGapAcceptable = (gapIndex: number) => {
    const updatedGaps = [...data.gaps];
    const gap = updatedGaps[gapIndex];

    // Toggle el estado de aceptable
    gap.isAcceptable = !gap.isAcceptable;

    setData({
      ...data,
      gaps: updatedGaps,
    });
  };

  const getLocale = () => {
    const localeMap: Record<string, string> = {
      es: "es-ES",
      en: "en-US",
      fr: "fr-FR",
      de: "de-DE",
      it: "it-IT",
      pt: "pt-PT",
    };
    return localeMap[language] || "es-ES";
  };

  const getScoreColor = (score: number) => {
    if (score >= 0.85) return "text-green-600";
    if (score >= 0.70) return "text-yellow-600";
    return "text-red-600";
  };

  const getSeverityBadge = (severity: string) => {
    switch (severity.toUpperCase()) {
      case "HIGH":
        return <Badge variant="danger">{t("governance.compliance.qms.severity.high", "Alta")}</Badge>;
      case "MEDIUM":
        return <Badge className="bg-yellow-500">{t("governance.compliance.qms.severity.medium", "Media")}</Badge>;
      default:
        return <Badge variant="outline">{t("governance.compliance.qms.severity.low", "Baja")}</Badge>;
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    );
  }

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
                onClick={() => window.location.href = "/governance/compliance/qms/projects"}
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                {t("common.back", "Volver")}
              </Button>
              <div className="flex items-center gap-3">
                <ClipboardCheck className="w-8 h-8 text-indigo-500" />
                <div>
                  <h1 className="text-3xl font-bold text-foreground">
                    {t("governance.compliance.qms.review.title", "Revisión de Gaps QMS")}
                  </h1>
                  <p className="text-muted-foreground text-sm mt-1">
                    {t("governance.compliance.qms.review.subtitle", "Art. 17 EU AI Act - Revisión de gaps del Sistema de Gestión de Calidad")}
                  </p>
                  {data.projectId && data.projectName && (
                    <div className="mt-2">
                      <Badge variant="outline" className="text-xs">
                        {t("governance.compliance.qms.review.projectName", "Proyecto")}: {data.projectName}
                      </Badge>
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
                    {t("governance.compliance.qms.review.projectName", "Proyecto")}
                  </div>
                  <div className="font-semibold text-lg">
                    {data.projectName} <span className="text-muted-foreground text-sm">(ID: {data.projectId})</span>
                  </div>
                  <div className="text-xs text-muted-foreground mt-1">
                    Score Overall: <span className={`font-semibold ${getScoreColor(data.overallScore)}`}>
                      {(data.overallScore * 100).toFixed(0)}%
                    </span>
                  </div>
                </div>
              </div>
            )}

            {/* Estadísticas en el header */}
            <div className="flex items-center gap-6">
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.qms.review.gapsTitle", "Gaps Detectados")}</div>
                <div className="font-semibold text-lg">{data.gaps.length}</div>
              </div>
              <div className="text-center">
                <div className="text-sm text-muted-foreground">{t("governance.compliance.qms.review.overallScore", "Score Overall")}</div>
                <div className={`font-semibold text-lg ${getScoreColor(data.overallScore)}`}>
                  {(data.overallScore * 100).toFixed(0)}%
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Botones de acción */}
        <div className="flex items-center justify-end gap-2">
          <Button variant="outline">{t("common.export", "Exportar")}</Button>
          <Button
            onClick={handleApproveReview}
            disabled={submitting || !data.reviewNotes.trim() || data.decision !== null}
            className="bg-green-600 hover:bg-green-700"
          >
            <CheckCircle className="w-4 h-4 mr-2" />
            {submitting ? t("common.processing", "Procesando...") : t("governance.compliance.qms.review.approve", "Aprobar QMS")}
          </Button>
          <Button
            onClick={handleRejectReview}
            disabled={submitting || !data.reviewNotes.trim() || data.decision !== null}
            variant="danger"
          >
            <XCircle className="w-4 h-4 mr-2" />
            {submitting ? t("common.processing", "Procesando...") : t("governance.compliance.qms.review.requestCorrections", "Solicitar Correcciones")}
          </Button>
        </div>

        {/* Gaps y Campos de Entrada - 50% cada uno */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Gaps Detectados - 50% */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 flex flex-col">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertTriangle className="w-5 h-5 text-yellow-500" />
                {t("governance.compliance.qms.review.gapsTitle", "Gaps Detectados")} ({data.gaps.length})
              </CardTitle>
            </CardHeader>
            <CardBody className="overflow-auto max-h-[600px] flex-1">
              {data.gaps.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  <CheckCircle className="w-12 h-12 mx-auto mb-2 text-green-500" />
                  <p>{t("governance.compliance.qms.review.noGaps", "No se detectaron gaps. El QMS cumple con todos los requisitos.")}</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {data.gaps.map((gap, idx) => (
                    <div key={idx} className={`p-4 border rounded-lg backdrop-blur-sm bg-background/40 ${gap.isAcceptable ? "border-green-500 bg-green-50/10" : ""}`}>
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-2">
                          <Badge variant="outline">
                            {t("governance.compliance.qms.review.module", "Módulo")}: {t(`governance.qms.moduleNames.${gap.module}`, gap.module)}
                          </Badge>
                          {getSeverityBadge(gap.severity)}
                          {gap.isAcceptable && (
                            <Badge className="bg-green-500 text-white">
                              <CheckCircle className="h-3 w-3 mr-1" />
                              {t("governance.compliance.qms.review.acceptable", "Aceptable")}
                            </Badge>
                          )}
                        </div>
                        {gap.currentScore !== undefined && (
                          <div className={`text-lg font-bold ${getScoreColor(gap.currentScore)}`}>
                            {(gap.currentScore * 100).toFixed(0)}%
                          </div>
                        )}
                      </div>
                      <div className="text-sm mb-3">{gap.description}</div>
                      {gap.currentScore !== undefined && gap.targetScore !== undefined && (
                        <div className="mb-3">
                          <div className="flex items-center justify-between text-xs text-muted-foreground mb-1">
                            <span>{t("governance.compliance.qms.review.currentScore", "Score Actual")}: {(gap.currentScore * 100).toFixed(0)}%</span>
                            <span>{t("governance.compliance.qms.review.targetScore", "Score Objetivo")}: {(gap.targetScore * 100).toFixed(0)}%</span>
                            {gap.gap !== undefined && (
                              <span className="text-red-500 font-semibold">
                                {t("governance.compliance.qms.review.gap", "Gap")}: {(gap.gap * 100).toFixed(1)}%
                              </span>
                            )}
                          </div>
                          <div className="w-full bg-muted rounded-full h-2">
                            <div
                              className="bg-yellow-500 h-2 rounded-full"
                              style={{ width: `${(gap.currentScore / gap.targetScore) * 100}%` }}
                            />
                          </div>
                        </div>
                      )}
                      {gap.recommendedActions && gap.recommendedActions.length > 0 && (
                        <div className="mt-3 pt-3 border-t">
                          <div className="text-xs font-semibold mb-2">{t("governance.compliance.qms.review.recommendedActions", "Acciones Recomendadas")}:</div>
                          <ul className="list-disc list-inside space-y-1 text-xs text-muted-foreground">
                            {gap.recommendedActions.map((action, actionIdx) => (
                              <li key={actionIdx}>{action}</li>
                            ))}
                          </ul>
                        </div>
                      )}
                      <div className="mt-3 pt-3 border-t">
                        <Button
                          variant={gap.isAcceptable ? "primary" : "outline"}
                          size="sm"
                          onClick={() => handleMarkGapAcceptable(idx)}
                          className={gap.isAcceptable ? "bg-green-600 hover:bg-green-700" : ""}
                        >
                          {gap.isAcceptable ? (
                            <>
                              <CheckCircle className="h-4 w-4 mr-2" />
                              {t("governance.compliance.qms.review.markedAcceptable", "Marcado como Aceptable")}
                            </>
                          ) : (
                            t("governance.compliance.qms.review.markAcceptable", "Marcar como Aceptable")
                          )}
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardBody>
          </Card>

          {/* Campos de Entrada - 50% */}
          <Card className="backdrop-blur-md bg-background/60 border-border/50 flex flex-col">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <UserCheck className="w-5 h-5" />
                {t("governance.compliance.qms.review.decision", "Decisión de Revisión")}
              </CardTitle>
            </CardHeader>
            <CardBody className="space-y-4 flex-1">
            {data.decision && (
              <div className="p-3 rounded-lg bg-muted">
                <div className="flex items-center gap-2">
                  {data.decision === "APPROVED" ? (
                    <>
                      <CheckCircle className="w-5 h-5 text-green-500" />
                      <span className="font-semibold text-green-700 dark:text-green-300">
                        {t("governance.compliance.qms.review.approved", "QMS Aprobado")}
                      </span>
                    </>
                  ) : (
                    <>
                      <XCircle className="w-5 h-5 text-yellow-500" />
                      <span className="font-semibold text-yellow-700 dark:text-yellow-300">
                        {t("governance.compliance.qms.review.correctionsRequired", "Correcciones Requeridas")}
                      </span>
                    </>
                  )}
                </div>
              </div>
            )}
            <div>
              <label className="block text-sm font-medium mb-2">
                {t("governance.compliance.qms.review.reviewerName", "Nombre del Revisor")} <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                value={data.reviewerName}
                readOnly
                className="w-full px-3 py-2 border rounded-md bg-muted cursor-not-allowed"
                disabled={true}
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">
                {t("governance.compliance.qms.review.reviewNotes", "Notas de Revisión")} <span className="text-red-500">*</span>
              </label>
              <textarea
                value={data.reviewNotes}
                onChange={(e) =>
                  setData({ ...data, reviewNotes: e.target.value })
                }
                placeholder={t("governance.compliance.qms.review.reviewNotesPlaceholder", "Ingrese sus observaciones y justificación...")}
                rows={6}
                className="w-full px-3 py-2 border rounded-md bg-background"
                disabled={submitting || data.decision !== null}
              />
              {!data.reviewNotes.trim() && (
                <p className="text-xs text-muted-foreground mt-1">
                  {t("governance.compliance.qms.review.notesRequired", "Las notas de revisión son requeridas para aprobar o solicitar correcciones")}
                </p>
              )}
            </div>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default function ConformityReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      </div>
    }>
      <ConformityReviewPageContent />
    </Suspense>
  );
}
