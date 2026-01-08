"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { ArrowLeft, BarChart3, FileCheck, AlertTriangle, CheckCircle, Loader2, Calendar, Clock, X } from "lucide-react";
import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { DataGovernanceDataset } from "../../../types/data-governance";

type AnalysisType = "quality" | "bias" | "compliance" | "all";

export default function DatasetAnalyzePage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const datasetId = params.id as string;
  const [dataset, setDataset] = useState<DataGovernanceDataset | null>(null);
  const [loading, setLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState<AnalysisType | null>(null);
  const [results, setResults] = useState<{
    quality?: { score: number; completed: boolean };
    bias?: { score: number; representativity: number; completed: boolean };
    compliance?: { status: string; completed: boolean };
  }>({});
  const [isConfigDialogOpen, setIsConfigDialogOpen] = useState(false);
  const [pendingAnalysisType, setPendingAnalysisType] = useState<AnalysisType | null>(null);
  const [analysisConfig, setAnalysisConfig] = useState({
    samplePercentage: 10,
    scheduleEnabled: false,
    scheduleTime: "02:00",
    scheduleDays: {
      monday: false,
      tuesday: false,
      wednesday: false,
      thursday: false,
      friday: false,
      saturday: false,
      sunday: false,
    },
  });

  useEffect(() => {
    if (datasetId) {
      loadDataset();
    }
  }, [datasetId]);

  const loadDataset = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}`);
      if (response.ok) {
        const data = await response.json();
        setDataset(data);
      }
    } catch (error) {
      console.error("Error loading dataset:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAnalyzeClick = (type: AnalysisType) => {
    setPendingAnalysisType(type);
    setIsConfigDialogOpen(true);
  };

  const handleAnalyze = async (type: AnalysisType, config?: typeof analysisConfig) => {
    const finalConfig = config || analysisConfig;

    try {
      setAnalyzing(type);
      setResults({});
      setIsConfigDialogOpen(false);

      const payload = {
        samplePercentage: finalConfig.samplePercentage,
        scheduleEnabled: finalConfig.scheduleEnabled,
        scheduleTime: finalConfig.scheduleEnabled ? finalConfig.scheduleTime : null,
        scheduleDays: finalConfig.scheduleEnabled ? finalConfig.scheduleDays : null,
      };

      if (type === "quality" || type === "all") {
        const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/analyze-quality`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (response.ok) {
          const data = await response.json();
          setResults((prev) => ({
            ...prev,
            quality: { score: data.qualityScore || 0.85, completed: true },
          }));
        }
      }

      if (type === "bias" || type === "all") {
        const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/analyze-bias`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (response.ok) {
          const data = await response.json();
          setResults((prev) => ({
            ...prev,
            bias: {
              score: data.biasScore || 0.75,
              representativity: data.representativityScore || 0.80,
              completed: true,
            },
          }));
        }
      }

      if (type === "compliance" || type === "all") {
        const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/analyze-compliance`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload),
        });
        if (response.ok) {
          const data = await response.json();
          setResults((prev) => ({
            ...prev,
            compliance: { status: data.status || "COMPLIANT", completed: true },
          }));
        }
      }

      // Recargar dataset para obtener los scores actualizados
      await loadDataset();
    } catch (error) {
      console.error("Error analyzing dataset:", error);
    } finally {
      setAnalyzing(null);
      setPendingAnalysisType(null);
    }
  };

  if (loading) {
    return (
      <div className="w-full p-6">
        <div className="flex items-center justify-center h-64">
          <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
        </div>
      </div>
    );
  }

  if (!dataset) {
    return (
      <div className="w-full p-6">
        <p>Dataset no encontrado</p>
      </div>
    );
  }

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center gap-4 mb-4">
        <Button
          variant="ghost"
          onClick={() => router.push(`/governance/data/datasets/${datasetId}`)}
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("governance.data.datasets.analyze.actions.back", "Volver")}
        </Button>
      </div>

      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">
            {t("governance.data.datasets.analyze.title", "Análisis del Dataset")}
          </h1>
          <p className="text-muted-foreground mt-1">{dataset.dtgname}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <BarChart3 className="h-5 w-5" />
              {t("governance.data.datasets.analyze.quality.title", "Análisis de Calidad")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            {dataset.dtgqualityscore ? (
              <div>
                <div className="text-3xl font-bold mb-2">
                  {(dataset.dtgqualityscore * 100).toFixed(0)}%
                </div>
                <Badge className="bg-green-100 text-green-800">
                  {t("governance.data.datasets.analyze.quality.completed", "Completado")}
                </Badge>
              </div>
            ) : (
              <div className="text-muted-foreground">
                {t("governance.data.datasets.analyze.quality.notAnalyzed", "No analizado")}
              </div>
            )}
            <Button
              onClick={() => handleAnalyzeClick("quality")}
              disabled={analyzing === "quality" || analyzing === "all"}
              className="w-full"
              variant="outline"
            >
              {analyzing === "quality" || analyzing === "all" ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  {t("governance.data.datasets.analyze.quality.analyzing", "Analizando...")}
                </>
              ) : (
                <>
                  <BarChart3 className="mr-2 h-4 w-4" />
                  {t("governance.data.datasets.analyze.quality.analyze", "Analizar Calidad")}
                </>
              )}
            </Button>
            {results.quality?.completed && (
              <div className="mt-2 text-sm text-green-600">
                <CheckCircle className="h-4 w-4 inline mr-1" />
                {t("governance.data.datasets.analyze.quality.completed", "Análisis completado")}
              </div>
            )}
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5" />
              {t("governance.data.datasets.analyze.bias.title", "Análisis de Sesgos")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            {dataset.dtgbiasscore ? (
              <div>
                <div className="text-3xl font-bold mb-2">
                  {(dataset.dtgbiasscore * 100).toFixed(0)}%
                </div>
                {dataset.dtgrepresentativityscore && (
                  <div className="text-sm text-muted-foreground mb-2">
                    {t("governance.data.datasets.analyze.bias.representativity", "Representatividad")}:{" "}
                    {(dataset.dtgrepresentativityscore * 100).toFixed(0)}%
                  </div>
                )}
                <Badge className="bg-green-100 text-green-800">
                  {t("governance.data.datasets.analyze.bias.completed", "Completado")}
                </Badge>
              </div>
            ) : (
              <div className="text-muted-foreground">
                {t("governance.data.datasets.analyze.bias.notAnalyzed", "No analizado")}
              </div>
            )}
            <Button
              onClick={() => handleAnalyzeClick("bias")}
              disabled={analyzing === "bias" || analyzing === "all"}
              className="w-full"
              variant="outline"
            >
              {analyzing === "bias" || analyzing === "all" ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  {t("governance.data.datasets.analyze.bias.analyzing", "Analizando...")}
                </>
              ) : (
                <>
                  <AlertTriangle className="mr-2 h-4 w-4" />
                  {t("governance.data.datasets.analyze.bias.analyze", "Analizar Sesgos")}
                </>
              )}
            </Button>
            {results.bias?.completed && (
              <div className="mt-2 text-sm text-green-600">
                <CheckCircle className="h-4 w-4 inline mr-1" />
                {t("governance.data.datasets.analyze.bias.completed", "Análisis completado")}
              </div>
            )}
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <FileCheck className="h-5 w-5" />
              {t("governance.data.datasets.analyze.compliance.title", "Compliance")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            {results.compliance?.status ? (
              <div>
                <Badge className="mb-2">{results.compliance.status}</Badge>
                <div className="text-sm text-muted-foreground">
                  {t("governance.data.datasets.analyze.compliance.status", "Estado verificado")}
                </div>
              </div>
            ) : (
              <div className="text-muted-foreground">
                {t("governance.data.datasets.analyze.compliance.notAnalyzed", "No analizado")}
              </div>
            )}
            <Button
              onClick={() => handleAnalyzeClick("compliance")}
              disabled={analyzing === "compliance" || analyzing === "all"}
              className="w-full"
              variant="outline"
            >
              {analyzing === "compliance" || analyzing === "all" ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  {t("governance.data.datasets.analyze.compliance.analyzing", "Analizando...")}
                </>
              ) : (
                <>
                  <FileCheck className="mr-2 h-4 w-4" />
                  {t("governance.data.datasets.analyze.compliance.analyze", "Analizar Compliance")}
                </>
              )}
            </Button>
            {results.compliance?.completed && (
              <div className="mt-2 text-sm text-green-600">
                <CheckCircle className="h-4 w-4 inline mr-1" />
                {t("governance.data.datasets.analyze.compliance.completed", "Análisis completado")}
              </div>
            )}
          </CardBody>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.data.datasets.analyze.all.title", "Análisis Completo")}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <p className="text-muted-foreground mb-4">
            {t(
              "governance.data.datasets.analyze.all.description",
              "Ejecuta todos los análisis disponibles: calidad, sesgos y compliance"
            )}
          </p>
          <Button
            onClick={() => handleAnalyzeClick("all")}
            disabled={analyzing !== null}
            className="w-full md:w-auto"
            size="lg"
          >
            {analyzing === "all" ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                {t("governance.data.datasets.analyze.all.analyzing", "Ejecutando análisis completo...")}
              </>
            ) : (
              <>
                <BarChart3 className="mr-2 h-4 w-4" />
                {t("governance.data.datasets.analyze.all.analyze", "Ejecutar Análisis Completo")}
              </>
            )}
          </Button>
        </CardBody>
      </Card>

      {/* Dialog de Configuración de Análisis */}
      <Dialog open={isConfigDialogOpen} onOpenChange={setIsConfigDialogOpen}>
        <DialogContent className="max-w-2xl p-0">
          <Card className="border-0 shadow-none">
            {/* HEADER */}
            <CardHeader className="flex flex-row items-center justify-between pb-4">
              <div className="flex-1">
                <CardTitle className="text-2xl font-bold">
                  {t("governance.data.datasets.analyze.config.title", "Configurar Análisis")}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-2">
                  {t("governance.data.datasets.analyze.config.description", "Configura el porcentaje de datos a analizar y programa la ejecución")}
                </p>
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="h-6 w-6"
                onClick={() => setIsConfigDialogOpen(false)}
              >
                <X className="h-4 w-4" />
              </Button>
            </CardHeader>

            {/* BODY */}
            <CardBody className="space-y-6">
              {/* Porcentaje de Muestra */}
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <Label className="text-base font-semibold">
                    {t("governance.data.datasets.analyze.config.samplePercentage", "Porcentaje de Datos a Analizar")} *
                  </Label>
                  <span className="text-sm font-bold text-primary">
                    {analysisConfig.samplePercentage}%
                  </span>
                </div>
                <div className="space-y-2">
                  <input
                    type="range"
                    min="10"
                    max="100"
                    step="5"
                    value={analysisConfig.samplePercentage}
                    onChange={(e) => setAnalysisConfig({ ...analysisConfig, samplePercentage: parseInt(e.target.value) })}
                    className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
                  />
                  <div className="flex justify-between text-xs text-muted-foreground">
                    <span>10% {t("governance.data.datasets.analyze.config.recommended", "(Recomendado)")}</span>
                    <span>100%</span>
                  </div>
                  {analysisConfig.samplePercentage > 10 && (
                    <div className="bg-amber-50 dark:bg-amber-950/30 border border-amber-200 dark:border-amber-800 rounded-lg p-3 flex items-start gap-2">
                      <AlertTriangle className="h-5 w-5 text-amber-600 dark:text-amber-400 flex-shrink-0 mt-0.5" />
                      <div className="text-sm text-amber-900 dark:text-amber-100">
                        <p className="font-medium mb-1">
                          {t("governance.data.datasets.analyze.config.performanceWarning", "Advertencia de Rendimiento")}
                        </p>
                        <p>
                          {t("governance.data.datasets.analyze.config.performanceWarningDescription", "Analizar más del 10% puede afectar significativamente el rendimiento del sistema. Asegúrese de que tiene recursos suficientes.")}
                        </p>
                      </div>
                    </div>
                  )}
                </div>
              </div>

              {/* Programación */}
              <div className="space-y-3 border-t pt-4">
                <div className="flex items-center justify-between">
                  <div>
                    <Label className="text-base font-semibold">
                      {t("governance.data.datasets.analyze.config.schedule", "Programar Análisis")}
                    </Label>
                    <p className="text-xs text-muted-foreground mt-1">
                      {t("governance.data.datasets.analyze.config.scheduleDescription", "Ejecutar automáticamente en horarios programados")}
                    </p>
                  </div>
                  <Switch
                    checked={analysisConfig.scheduleEnabled}
                    onChange={(checked) => setAnalysisConfig({ ...analysisConfig, scheduleEnabled: checked })}
                  />
                </div>

                {analysisConfig.scheduleEnabled && (
                  <div className="space-y-4 pl-4 border-l-2 border-primary/20">
                    {/* Hora */}
                    <div className="space-y-2">
                      <Label className="text-sm font-medium flex items-center gap-2">
                        <Clock className="h-4 w-4" />
                        {t("governance.data.datasets.analyze.config.scheduleTime", "Hora de Ejecución")}
                      </Label>
                      <Input
                        type="time"
                        value={analysisConfig.scheduleTime}
                        onChange={(e) => setAnalysisConfig({ ...analysisConfig, scheduleTime: e.target.value })}
                        className="w-40"
                      />
                    </div>

                    {/* Días de la Semana */}
                    <div className="space-y-2">
                      <Label className="text-sm font-medium flex items-center gap-2">
                        <Calendar className="h-4 w-4" />
                        {t("governance.data.datasets.analyze.config.scheduleDays", "Días de la Semana")}
                      </Label>
                      <div className="grid grid-cols-4 gap-2">
                        {[
                          { key: "monday", label: t("common.days.monday", "Lun") },
                          { key: "tuesday", label: t("common.days.tuesday", "Mar") },
                          { key: "wednesday", label: t("common.days.wednesday", "Mié") },
                          { key: "thursday", label: t("common.days.thursday", "Jue") },
                          { key: "friday", label: t("common.days.friday", "Vie") },
                          { key: "saturday", label: t("common.days.saturday", "Sáb") },
                          { key: "sunday", label: t("common.days.sunday", "Dom") },
                        ].map((day) => (
                          <label
                            key={day.key}
                            className="flex items-center space-x-2 p-2 border rounded-lg cursor-pointer hover:bg-muted/50 transition-colors"
                          >
                            <input
                              type="checkbox"
                              checked={analysisConfig.scheduleDays[day.key as keyof typeof analysisConfig.scheduleDays]}
                              onChange={(e) =>
                                setAnalysisConfig({
                                  ...analysisConfig,
                                  scheduleDays: {
                                    ...analysisConfig.scheduleDays,
                                    [day.key]: e.target.checked,
                                  },
                                })
                              }
                              className="rounded"
                            />
                            <span className="text-sm">{day.label}</span>
                          </label>
                        ))}
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </CardBody>

            {/* FOOTER */}
            <div className="border-t px-6 py-4 flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => {
                  setIsConfigDialogOpen(false);
                  setPendingAnalysisType(null);
                }}
                className="h-11"
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={() => {
                  if (pendingAnalysisType) {
                    handleAnalyze(pendingAnalysisType);
                  }
                }}
                disabled={!pendingAnalysisType}
                className="h-11"
              >
                <BarChart3 className="mr-2 h-4 w-4" />
                {analysisConfig.scheduleEnabled
                  ? t("governance.data.datasets.analyze.config.scheduleAnalysis", "Programar Análisis")
                  : t("governance.data.datasets.analyze.config.runAnalysis", "Ejecutar Análisis")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}
