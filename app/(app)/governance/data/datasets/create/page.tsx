"use client";

import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { ArrowLeft, Save, Upload, Link as LinkIcon, Database, Settings, Clock, Calendar, X, AlertTriangle, BarChart3 } from "lucide-react";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { DataGovernanceOrigin } from "../../types/data-governance";
import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Switch } from "@/components/ui/switch";

export default function CreateDatasetPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [saving, setSaving] = useState(false);
  const [origins, setOrigins] = useState<DataGovernanceOrigin[]>([]);
  const [sourceType, setSourceType] = useState<"HUGGINGFACE" | "KAGGLE" | "API" | "UPLOAD" | "INTERNAL">("HUGGINGFACE");
  const [isAnalysisConfigDialogOpen, setIsAnalysisConfigDialogOpen] = useState(false);
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

  const [formData, setFormData] = useState({
    dtgname: "",
    dtgdescription: "",
    dtgtype: "TRAINING" as "TRAINING" | "VALIDATION" | "TEST" | "PRODUCTION" | "RAG",
    dtgorigintype: "EXTERNAL" as "INTERNAL" | "EXTERNAL",
    idxorigin: "",
    dtgformat: "CSV" as string,
    dtgstandardized: false,
    idxproject: "",
    sourceId: "",
    sourceUrl: "",
  });

  useEffect(() => {
    loadOrigins();
  }, []);

  const loadOrigins = async () => {
    try {
      const response = await fetch("/api/v1/governance/data/origins");
      if (response.ok) {
        const data = await response.json();
        setOrigins(data.origins || []);
      }
    } catch (error) {
      console.error("Error loading origins:", error);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setSaving(true);
      const payload = {
        ...formData,
        idxorigin: formData.idxorigin ? parseInt(formData.idxorigin) : null,
        idxproject: formData.idxproject ? parseInt(formData.idxproject) : null,
        analysisConfig: analysisConfig, // Incluir configuración de análisis
      };

      const response = await fetch(`/api/v1/governance/data/datasets`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        const datasetId = data.dataset?.idxdataset || data.idxdataset;

        // En modo mock, la configuración de análisis se guarda en el estado local
        // y se incluye en el payload del dataset si es necesario

        router.push(`/governance/data/datasets/${datasetId}`);
      } else {
        alert("Error al crear el dataset");
      }
    } catch (error) {
      console.error("Error creating dataset:", error);
      alert("Error al crear el dataset");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="w-full p-6 space-y-4">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">
          {t("governance.data.datasets.create.title", "Crear Dataset")}
        </h1>
        <p className="text-muted-foreground mt-1 text-sm">
          {t("governance.data.datasets.create.description", "Crea un nuevo dataset para entrenamiento, validación o producción")}
        </p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          onClick={() => router.push("/governance/data/datasets/overview")}
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("governance.data.datasets.create.actions.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button
            variant="outline"
            type="button"
            onClick={() => setIsAnalysisConfigDialogOpen(true)}
          >
            <Settings className="mr-2 h-4 w-4" />
            {t("governance.data.datasets.detail.actions.configureAnalysis", "Configurar Análisis")}
          </Button>
          <Button type="submit" form="dataset-create-form" disabled={saving}>
            {saving ? (
              <>
                <Save className="mr-2 h-4 w-4 animate-spin" />
                Guardando...
              </>
            ) : (
              <>
                <Save className="mr-2 h-4 w-4" />
                Crear Dataset
              </>
            )}
          </Button>
        </div>
      </div>

      <form onSubmit={handleSubmit} id="dataset-create-form">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-xl">
              {t("governance.data.datasets.create.form.title", "Información del Dataset")}
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div className="md:col-span-2">
                <Label htmlFor="name">
                  {t("governance.data.datasets.create.form.name", "Nombre")} *
                </Label>
                <Input
                  id="name"
                  value={formData.dtgname}
                  onChange={(e) => setFormData({ ...formData, dtgname: e.target.value })}
                  required
                />
              </div>

              <div className="md:col-span-2">
                <Label htmlFor="description">
                  {t("governance.data.datasets.create.form.description", "Descripción")}
                </Label>
                <Textarea
                  id="description"
                  value={formData.dtgdescription}
                  onChange={(e) => setFormData({ ...formData, dtgdescription: e.target.value })}
                  rows={2}
                />
              </div>

              <div>
                <Label htmlFor="type">
                  {t("governance.data.datasets.create.form.type", "Tipo")} *
                </Label>
                <select
                  id="type"
                  value={formData.dtgtype}
                  onChange={(e) =>
                    setFormData({ ...formData, dtgtype: e.target.value as any })
                  }
                  className="w-full h-10 px-3 py-2 text-sm border rounded-md bg-background"
                  required
                >
                  <option value="TRAINING">Training</option>
                  <option value="VALIDATION">Validation</option>
                  <option value="TEST">Test</option>
                  <option value="PRODUCTION">Production</option>
                  <option value="RAG">RAG</option>
                </select>
              </div>

              <div>
                <Label htmlFor="originType">
                  {t("governance.data.datasets.create.form.originType", "Tipo de Origen")} *
                </Label>
                <select
                  id="originType"
                  value={formData.dtgorigintype}
                  onChange={(e) =>
                    setFormData({ ...formData, dtgorigintype: e.target.value as any })
                  }
                  className="w-full h-10 px-3 py-2 text-sm border rounded-md bg-background"
                  required
                >
                  <option value="INTERNAL">Interno</option>
                  <option value="EXTERNAL">Externo</option>
                </select>
              </div>

              <div className="md:col-span-2">
                <Label htmlFor="origin">
                  {t("governance.data.datasets.create.form.origin", "Origen")}
                </Label>
                <select
                  id="origin"
                  value={formData.idxorigin}
                  onChange={(e) => setFormData({ ...formData, idxorigin: e.target.value })}
                  className="w-full h-10 px-3 py-2 text-sm border rounded-md bg-background"
                >
                  <option value="">Seleccionar origen</option>
                  {origins.map((origin) => (
                    <option key={origin.idxorigin} value={origin.idxorigin.toString()}>
                      {origin.dtgorname} ({origin.dtgortype})
                    </option>
                  ))}
                </select>
              </div>

              <div className="md:col-span-2">
                <Label>
                  {t("governance.data.datasets.create.source.title", "Origen del Dataset")}
                </Label>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-2 mt-1">
                  <Button
                    type="button"
                    variant={sourceType === "HUGGINGFACE" ? "primary" : "outline"}
                    onClick={() => setSourceType("HUGGINGFACE")}
                    className="flex items-center gap-2"
                  >
                    <Database className="h-4 w-4" />
                    {t("governance.data.datasets.create.source.huggingface", "HuggingFace")}
                  </Button>
                  <Button
                    type="button"
                    variant={sourceType === "KAGGLE" ? "primary" : "outline"}
                    onClick={() => setSourceType("KAGGLE")}
                    className="flex items-center gap-2"
                  >
                    <Database className="h-4 w-4" />
                    {t("governance.data.datasets.create.source.kaggle", "Kaggle")}
                  </Button>
                  <Button
                    type="button"
                    variant={sourceType === "API" ? "primary" : "outline"}
                    onClick={() => setSourceType("API")}
                    className="flex items-center gap-2"
                  >
                    <LinkIcon className="h-4 w-4" />
                    {t("governance.data.datasets.create.source.api", "API Externa")}
                  </Button>
                  <Button
                    type="button"
                    variant={sourceType === "UPLOAD" ? "primary" : "outline"}
                    onClick={() => setSourceType("UPLOAD")}
                    className="flex items-center gap-2"
                  >
                    <Upload className="h-4 w-4" />
                    {t("governance.data.datasets.create.source.upload", "Subir Archivo")}
                  </Button>
                </div>
              </div>

              {sourceType === "HUGGINGFACE" && (
                <div className="md:col-span-2">
                  <Label htmlFor="sourceId">
                    {t("governance.data.datasets.create.source.sourceId", "ID del Dataset")}
                  </Label>
                  <Input
                    id="sourceId"
                    value={formData.sourceId}
                    onChange={(e) => setFormData({ ...formData, sourceId: e.target.value })}
                    placeholder={t("governance.data.datasets.create.source.sourceIdPlaceholder", "ej: glue, squad, imdb")}
                  />
                </div>
              )}

              {sourceType === "KAGGLE" && (
                <div className="md:col-span-2">
                  <Label htmlFor="sourceId">
                    {t("governance.data.datasets.create.source.sourceId", "ID del Dataset")}
                  </Label>
                  <Input
                    id="sourceId"
                    value={formData.sourceId}
                    onChange={(e) => setFormData({ ...formData, sourceId: e.target.value })}
                    placeholder="usuario/dataset-name"
                  />
                </div>
              )}

              {sourceType === "API" && (
                <div className="md:col-span-2">
                  <Label htmlFor="sourceUrl">
                    {t("governance.data.datasets.create.source.sourceUrl", "URL del Dataset")}
                  </Label>
                  <Input
                    id="sourceUrl"
                    type="url"
                    value={formData.sourceUrl}
                    onChange={(e) => setFormData({ ...formData, sourceUrl: e.target.value })}
                    placeholder="https://api.example.com/datasets/123"
                  />
                </div>
              )}

              {sourceType === "UPLOAD" && (
                <div className="md:col-span-2">
                  <Label htmlFor="file">
                    {t("governance.data.datasets.create.source.uploadFile", "Archivo")}
                  </Label>
                  <Input
                    id="file"
                    type="file"
                    accept=".csv,.json,.parquet"
                    onChange={(e) => {
                      const file = e.target.files?.[0];
                      if (file) {
                        setFormData({ ...formData, dtgformat: file.name.split('.').pop()?.toUpperCase() || "CSV" });
                      }
                    }}
                  />
                </div>
              )}

              <div>
                <Label htmlFor="format">
                  {t("governance.data.datasets.create.form.format", "Formato")}
                </Label>
                <select
                  id="format"
                  value={formData.dtgformat}
                  onChange={(e) => setFormData({ ...formData, dtgformat: e.target.value })}
                  className="w-full h-10 px-3 py-2 text-sm border rounded-md bg-background"
                >
                  <option value="CSV">CSV</option>
                  <option value="JSON">JSON</option>
                  <option value="PARQUET">Parquet</option>
                  <option value="XML">XML</option>
                </select>
              </div>

              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="autoStandardize"
                  checked={formData.dtgstandardized}
                  onChange={(e) => setFormData({ ...formData, dtgstandardized: e.target.checked })}
                  className="h-4 w-4"
                />
                <Label htmlFor="autoStandardize" className="cursor-pointer">
                  {t("governance.data.datasets.create.form.autoStandardize", "Estandarizar Automáticamente")}
                </Label>
              </div>
            </div>

            {formData.dtgstandardized && (
              <div className="bg-blue-50 dark:bg-blue-950/30 p-3 rounded-md">
                <p className="text-sm text-blue-800 dark:text-blue-200">
                  {t("governance.data.datasets.create.form.autoStandardizeDescription", "El dataset se convertirá automáticamente a formato Apache Parquet y se validará el schema")}
                </p>
              </div>
            )}
          </CardBody>
        </Card>
      </form>

      {/* Dialog de Configuración de Análisis */}
      <Dialog open={isAnalysisConfigDialogOpen} onOpenChange={setIsAnalysisConfigDialogOpen}>
        <DialogContent className="max-w-2xl p-0">
          <Card className="border-0 shadow-none">
            {/* HEADER */}
            <CardHeader className="flex flex-row items-center justify-between pb-4">
              <div className="flex-1">
                <CardTitle className="text-2xl font-bold">
                  {t("governance.data.datasets.detail.analysisConfig.title", "Configurar Análisis del Dataset")}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-2">
                  {t("governance.data.datasets.detail.analysisConfig.description", "Configura el porcentaje de datos a analizar y programa la ejecución automática")}
                </p>
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="h-6 w-6"
                onClick={() => setIsAnalysisConfigDialogOpen(false)}
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
                    {t("governance.data.datasets.detail.analysisConfig.samplePercentage", "Porcentaje de Datos a Analizar")} *
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
                    <span>10% {t("governance.data.datasets.detail.analysisConfig.recommended", "(Recomendado)")}</span>
                    <span>100%</span>
                  </div>
                  {analysisConfig.samplePercentage > 10 && (
                    <div className="bg-amber-50 dark:bg-amber-950/30 border border-amber-200 dark:border-amber-800 rounded-lg p-3 flex items-start gap-2">
                      <AlertTriangle className="h-5 w-5 text-amber-600 dark:text-amber-400 flex-shrink-0 mt-0.5" />
                      <div className="text-sm text-amber-900 dark:text-amber-100">
                        <p className="font-medium mb-1">
                          {t("governance.data.datasets.detail.analysisConfig.performanceWarning", "Advertencia de Rendimiento")}
                        </p>
                        <p>
                          {t("governance.data.datasets.detail.analysisConfig.performanceWarningDescription", "Analizar más del 10% puede afectar significativamente el rendimiento del sistema. Asegúrese de que tiene recursos suficientes.")}
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
                      {t("governance.data.datasets.detail.analysisConfig.schedule", "Programar Análisis Automático")}
                    </Label>
                    <p className="text-xs text-muted-foreground mt-1">
                      {t("governance.data.datasets.detail.analysisConfig.scheduleDescription", "Ejecutar automáticamente en horarios programados")}
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
                        {t("governance.data.datasets.detail.analysisConfig.scheduleTime", "Hora de Ejecución")}
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
                        {t("governance.data.datasets.detail.analysisConfig.scheduleDays", "Días de la Semana")}
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
                  setIsAnalysisConfigDialogOpen(false);
                }}
                className="h-11"
              >
                {t("common.cancel", "Cancelar")}
              </Button>
              <Button
                onClick={() => {
                  // La configuración se guarda en el estado y se incluirá al crear el dataset
                  setIsAnalysisConfigDialogOpen(false);
                }}
                className="h-11"
              >
                <BarChart3 className="mr-2 h-4 w-4" />
                {t("governance.data.datasets.detail.analysisConfig.save", "Guardar Configuración")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}
