"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ArrowLeft, BarChart3, CheckCircle, FileCheck, XCircle, AlertTriangle, Plus, Edit, Trash2, Users, Settings, Clock, Calendar, X, Network, GitBranch, GitMerge, Filter, ArrowRight, ArrowDown, Shield, Eye, Pencil, Trash, CheckCircle2, TrendingUp, AlertCircle } from "lucide-react";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { DataGovernanceDataset, DataGovernanceDatasetRisk, DataGovernanceQualityMetric, DataGovernanceDatasetPrivacy, DataGovernanceLineage, DataGovernanceDatasetDocumentation } from "../../types/data-governance";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export default function DatasetDetailPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const datasetId = params.id as string;
  const [dataset, setDataset] = useState<DataGovernanceDataset | null>(null);
  const [loading, setLoading] = useState(true);
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

  const handleStandardize = async () => {
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/standardize`, {
        method: "POST",
      });
      if (response.ok) {
        loadDataset();
      }
    } catch (error) {
      console.error("Error standardizing dataset:", error);
    }
  };

  const handleApprove = async () => {
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/approve?approvedBy=1`, {
        method: "POST",
      });
      if (response.ok) {
        loadDataset();
      }
    } catch (error) {
      console.error("Error approving dataset:", error);
    }
  };

  if (loading) {
    return <div className="w-full p-6">Cargando...</div>;
  }

  if (!dataset) {
    return <div className="w-full p-6">Dataset no encontrado</div>;
  }

  return (
    <div className="w-full p-6 space-y-6">
      {/* Línea 1: Título y Subtítulo */}
      <div>
        <h1 className="text-3xl font-bold">{dataset.dtgname}</h1>
        <p className="text-muted-foreground mt-2">
          {dataset.dtgdescription || t("governance.data.datasets.detail.description", "Detalle del dataset")}
        </p>
      </div>

      {/* Línea 2: Botón Volver (izquierda) y Botones de Acción (derecha) */}
      <div className="flex items-center justify-between">
        <Button
          variant="ghost"
          onClick={() => router.push("/governance/data/datasets/overview")}
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          {t("governance.data.datasets.detail.actions.back", "Volver")}
        </Button>
        <div className="flex gap-2">
          <Button
            variant="outline"
            onClick={() => setIsAnalysisConfigDialogOpen(true)}
          >
            <Settings className="mr-2 h-4 w-4" />
            {t("governance.data.datasets.detail.actions.configureAnalysis", "Configurar Análisis")}
          </Button>
          {!dataset.dtgstandardized && (
            <Button onClick={handleStandardize}>
              <FileCheck className="mr-2 h-4 w-4" />
              {t("governance.data.datasets.detail.actions.standardize", "Estandarizar a Parquet")}
            </Button>
          )}
          {dataset.dtgstatus === "VALIDATED" && !dataset.dtgapproved && (
            <>
              <Button onClick={handleApprove} variant="primary">
                <CheckCircle className="mr-2 h-4 w-4" />
                {t("governance.data.datasets.detail.actions.approve", "Aprobar")}
              </Button>
              <Button variant="danger">
                <XCircle className="mr-2 h-4 w-4" />
                {t("governance.data.datasets.detail.actions.reject", "Rechazar")}
              </Button>
            </>
          )}
        </div>
      </div>

      <Tabs defaultValue="overview">
        <TabsList>
          <TabsTrigger value="overview">
            {t("governance.data.datasets.detail.tabs.overview", "Información General")}
          </TabsTrigger>
          <TabsTrigger value="origins">
            {t("governance.data.datasets.detail.tabs.origins", "Orígenes")}
          </TabsTrigger>
          <TabsTrigger value="quality">
            {t("governance.data.datasets.detail.tabs.quality", "Calidad")}
          </TabsTrigger>
          <TabsTrigger value="bias">
            {t("governance.data.datasets.detail.tabs.bias", "Sesgos y Representatividad")}
          </TabsTrigger>
          <TabsTrigger value="lineage">
            {t("governance.data.datasets.detail.tabs.lineage", "Línea de Base")}
          </TabsTrigger>
          <TabsTrigger value="impact">
            {t("governance.data.datasets.detail.tabs.impact", "Análisis de Impacto")}
          </TabsTrigger>
          <TabsTrigger value="risks">
            {t("governance.data.datasets.detail.tabs.risks", "Riesgos")}
          </TabsTrigger>
          <TabsTrigger value="privacy">
            {t("governance.data.datasets.detail.tabs.privacy", "Privacidad")}
          </TabsTrigger>
          <TabsTrigger value="documentation">
            {t("governance.data.datasets.detail.tabs.documentation", "Documentación")}
          </TabsTrigger>
          <TabsTrigger value="compliance">
            {t("governance.data.datasets.detail.tabs.compliance", "Compliance")}
          </TabsTrigger>
          <TabsTrigger value="roles">
            {t("governance.data.datasets.detail.tabs.roles", "Roles y Responsabilidades")}
          </TabsTrigger>
        </TabsList>

        <TabsContent value="overview">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.datasets.detail.tabs.overview", "Información General")}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.name", "Nombre")}
                  </label>
                  <p className="text-sm">{dataset.dtgname}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.type", "Tipo")}
                  </label>
                  <p className="text-sm">
                    <Badge variant="outline">{dataset.dtgtype}</Badge>
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.status", "Estado")}
                  </label>
                  <p className="text-sm">
                    <Badge>{dataset.dtgstatus}</Badge>
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.version", "Versión")}
                  </label>
                  <p className="text-sm">{dataset.dtgversion}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.standardized", "Estandarizado")}
                  </label>
                  <p className="text-sm">
                    {dataset.dtgstandardized ? (
                      <Badge className="bg-green-100 text-green-800">
                        <FileCheck className="mr-1 h-3 w-3" />
                        {dataset.dtgstandardizedformat}
                      </Badge>
                    ) : (
                      <Badge variant="outline">No</Badge>
                    )}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">
                    {t("governance.data.datasets.detail.info.recordCount", "Total Registros")}
                  </label>
                  <p className="text-sm">{dataset.dtgrecordcount?.toLocaleString() || "-"}</p>
                </div>
                {dataset.dtgstandardizedpath && (
                  <div className="col-span-2">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.datasets.detail.info.standardizedPath", "Path Parquet")}
                    </label>
                    <p className="text-sm font-mono text-xs">{dataset.dtgstandardizedpath}</p>
                  </div>
                )}
              </div>
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="quality">
          <QualityMetricsTabContent datasetId={datasetId} overallScore={dataset.dtgqualityscore} />
        </TabsContent>

        <TabsContent value="bias">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.bias.title", "Análisis de Sesgos")}</CardTitle>
            </CardHeader>
            <CardBody>
              {dataset.dtgbiasscore ? (
                <div className="space-y-4">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.bias.score", "Score de Sesgos")}
                    </label>
                    <p className="text-2xl font-bold">
                      {(dataset.dtgbiasscore * 100).toFixed(0)}%
                    </p>
                  </div>
                  {dataset.dtgrepresentativityscore && (
                    <div>
                      <label className="text-sm font-medium text-muted-foreground">
                        {t("governance.data.bias.representativity", "Representatividad")}
                      </label>
                      <p className="text-2xl font-bold">
                        {(dataset.dtgrepresentativityscore * 100).toFixed(0)}%
                      </p>
                    </div>
                  )}
                  <Button onClick={() => {
                    fetch(`/api/v1/governance/data/datasets/${datasetId}/analyze-bias`, {
                      method: "POST",
                    }).then(() => loadDataset());
                  }}>
                    <BarChart3 className="mr-2 h-4 w-4" />
                    {t("governance.data.datasets.detail.actions.analyzeBias", "Analizar Sesgos")}
                  </Button>
                </div>
              ) : (
                <div className="text-center py-8">
                  <p className="text-muted-foreground mb-4">No hay análisis de sesgos disponible</p>
                  <Button onClick={() => {
                    fetch(`/api/v1/governance/data/datasets/${datasetId}/analyze-bias`, {
                      method: "POST",
                    }).then(() => loadDataset());
                  }}>
                    <BarChart3 className="mr-2 h-4 w-4" />
                    {t("governance.data.datasets.detail.actions.analyzeBias", "Analizar Sesgos")}
                  </Button>
                </div>
              )}
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="origins">
          <Card>
            <CardHeader>
              <CardTitle>{t("governance.data.datasets.detail.tabs.origins", "Orígenes")}</CardTitle>
            </CardHeader>
            <CardBody>
              <p className="text-muted-foreground">Orígenes asociados al dataset</p>
            </CardBody>
          </Card>
        </TabsContent>

        <TabsContent value="lineage">
          <LineageTabContent datasetId={datasetId} />
        </TabsContent>

        <TabsContent value="impact">
          <ImpactAnalysisTabContent datasetId={datasetId} />
        </TabsContent>

        <TabsContent value="risks">
          <RisksTabContent datasetId={datasetId} />
        </TabsContent>

        <TabsContent value="privacy">
          <PrivacyTabContent datasetId={datasetId} />
        </TabsContent>

        <TabsContent value="documentation">
          <DocumentationTabContent datasetId={datasetId} />
        </TabsContent>

        <TabsContent value="compliance">
          <div className="space-y-6">
            {/* Proyectos de Compliance Asociados */}
            <Card>
              <CardHeader>
                <CardTitle>
                  {t("governance.data.datasets.detail.compliance.projects.title", "Proyectos de Compliance")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                {dataset.idxproject ? (
                  <div className="space-y-4">
                    <div className="flex items-center justify-between p-4 border rounded-lg">
                      <div>
                        <p className="font-medium">
                          {t("governance.data.datasets.detail.compliance.projects.project", "Proyecto")}
                        </p>
                        <p className="text-sm text-muted-foreground">ID: {dataset.idxproject}</p>
                      </div>
                      <Button
                        variant="outline"
                        onClick={() => router.push(`/governance/compliance/projects/${dataset.idxproject}`)}
                      >
                        {t("governance.data.datasets.detail.compliance.projects.view", "Ver Proyecto")}
                      </Button>
                    </div>
                  </div>
                ) : (
                  <div className="text-center py-8">
                    <p className="text-muted-foreground mb-4">
                      {t("governance.data.datasets.detail.compliance.projects.noProject", "No hay proyecto de compliance asociado")}
                    </p>
                    <Button
                      variant="outline"
                      onClick={() => {
                        // TODO: Implementar modal para vincular proyecto
                        alert("Funcionalidad en desarrollo");
                      }}
                    >
                      {t("governance.data.datasets.detail.compliance.projects.link", "Vincular Proyecto")}
                    </Button>
                  </div>
                )}
              </CardBody>
            </Card>

            {/* Estado de Compliance */}
            <Card>
              <CardHeader>
                <CardTitle>
                  {t("governance.data.datasets.detail.compliance.status.title", "Estado de Compliance")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="p-4 border rounded-lg">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.datasets.detail.compliance.status.quality", "Calidad de Datos")}
                    </label>
                    <div className="mt-2">
                      {dataset.dtgqualityscore ? (
                        <div>
                          <p className="text-2xl font-bold">
                            {(dataset.dtgqualityscore * 100).toFixed(0)}%
                          </p>
                          <Badge className={dataset.dtgqualityscore >= 0.8 ? "bg-green-100 text-green-800" : dataset.dtgqualityscore >= 0.6 ? "bg-yellow-100 text-yellow-800" : "bg-red-100 text-red-800"}>
                            {dataset.dtgqualityscore >= 0.8 ? "Cumple" : dataset.dtgqualityscore >= 0.6 ? "Revisar" : "No Cumple"}
                          </Badge>
                        </div>
                      ) : (
                        <p className="text-muted-foreground">
                          {t("governance.data.datasets.detail.compliance.status.notAnalyzed", "No analizado")}
                        </p>
                      )}
                    </div>
                  </div>

                  <div className="p-4 border rounded-lg">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.datasets.detail.compliance.status.bias", "Sesgos y Equidad")}
                    </label>
                    <div className="mt-2">
                      {dataset.dtgbiasscore ? (
                        <div>
                          <p className="text-2xl font-bold">
                            {(dataset.dtgbiasscore * 100).toFixed(0)}%
                          </p>
                          <Badge className={dataset.dtgbiasscore >= 0.7 ? "bg-green-100 text-green-800" : dataset.dtgbiasscore >= 0.5 ? "bg-yellow-100 text-yellow-800" : "bg-red-100 text-red-800"}>
                            {dataset.dtgbiasscore >= 0.7 ? "Cumple" : dataset.dtgbiasscore >= 0.5 ? "Revisar" : "No Cumple"}
                          </Badge>
                        </div>
                      ) : (
                        <p className="text-muted-foreground">
                          {t("governance.data.datasets.detail.compliance.status.notAnalyzed", "No analizado")}
                        </p>
                      )}
                    </div>
                  </div>

                  <div className="p-4 border rounded-lg">
                    <label className="text-sm font-medium text-muted-foreground">
                      {t("governance.data.datasets.detail.compliance.status.pii", "Privacidad (PII)")}
                    </label>
                    <div className="mt-2">
                      {dataset.dtgpiidetected !== undefined ? (
                        <div>
                          <Badge className={!dataset.dtgpiidetected ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800"}>
                            {!dataset.dtgpiidetected ? "Sin PII" : "PII Detectado"}
                          </Badge>
                          {dataset.dtgpiidetected && (
                            <p className="text-xs text-muted-foreground mt-1">
                              {t("governance.data.datasets.detail.compliance.status.piiDetected", "Requiere medidas de protección")}
                            </p>
                          )}
                        </div>
                      ) : (
                        <p className="text-muted-foreground">
                          {t("governance.data.datasets.detail.compliance.status.notAnalyzed", "No analizado")}
                        </p>
                      )}
                    </div>
                  </div>
                </div>
              </CardBody>
            </Card>

            {/* Requisitos EU AI Act */}
            <Card>
              <CardHeader>
                <CardTitle>
                  {t("governance.data.datasets.detail.compliance.euAIAct.title", "Cumplimiento EU AI Act")}
                </CardTitle>
              </CardHeader>
              <CardBody>
                <div className="space-y-4">
                  <div className="flex items-start justify-between p-4 border rounded-lg">
                    <div className="flex-1">
                      <h4 className="font-medium mb-1">
                        Art. 10 - Requisitos de Datos de Entrenamiento
                      </h4>
                      <p className="text-sm text-muted-foreground">
                        {t("governance.data.datasets.detail.compliance.euAIAct.art10", "Los datos deben ser relevantes, representativos, libres de errores y completos")}
                      </p>
                    </div>
                    <Badge className={dataset.dtgqualityscore && dataset.dtgqualityscore >= 0.8 && dataset.dtgbiasscore && dataset.dtgbiasscore >= 0.7 ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800"}>
                      {dataset.dtgqualityscore && dataset.dtgqualityscore >= 0.8 && dataset.dtgbiasscore && dataset.dtgbiasscore >= 0.7 ? "Cumple" : "Revisar"}
                    </Badge>
                  </div>

                  <div className="flex items-start justify-between p-4 border rounded-lg">
                    <div className="flex-1">
                      <h4 className="font-medium mb-1">
                        Art. 11 - Documentación Técnica
                      </h4>
                      <p className="text-sm text-muted-foreground">
                        {t("governance.data.datasets.detail.compliance.euAIAct.art11", "El dataset debe estar documentado en la documentación técnica")}
                      </p>
                    </div>
                    <Badge variant="outline">
                      {t("governance.data.datasets.detail.compliance.euAIAct.pending", "Pendiente")}
                    </Badge>
                  </div>

                  <div className="flex items-start justify-between p-4 border rounded-lg">
                    <div className="flex-1">
                      <h4 className="font-medium mb-1">
                        Art. 27 - Evaluación FRIA
                      </h4>
                      <p className="text-sm text-muted-foreground">
                        {t("governance.data.datasets.detail.compliance.euAIAct.art27", "El dataset debe ser evaluado en FRIA si el sistema es de alto riesgo")}
                      </p>
                    </div>
                    <Badge variant="outline">
                      {t("governance.data.datasets.detail.compliance.euAIAct.pending", "Pendiente")}
                    </Badge>
                  </div>

                  <div className="flex items-start justify-between p-4 border rounded-lg">
                    <div className="flex-1">
                      <h4 className="font-medium mb-1">
                        Art. 17 - Sistema de Gestión de Calidad (QMS)
                      </h4>
                      <p className="text-sm text-muted-foreground">
                        {t("governance.data.datasets.detail.compliance.euAIAct.art17", "El dataset debe estar bajo control de calidad QMS")}
                      </p>
                    </div>
                    <Badge variant="outline">
                      {t("governance.data.datasets.detail.compliance.euAIAct.pending", "Pendiente")}
                    </Badge>
                  </div>
                </div>
              </CardBody>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="roles">
          <RolesTabContent datasetId={datasetId} datasetName={dataset.dtgname} />
        </TabsContent>
      </Tabs>

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
                  // En modo mock, solo cerramos el diálogo
                  // La configuración se guarda en el estado local
                  setIsAnalysisConfigDialogOpen(false);
                }}
                className="h-11"
              >
                <BarChart3 className="mr-2 h-4 w-4" />
                {analysisConfig.scheduleEnabled
                  ? t("governance.data.datasets.detail.analysisConfig.saveSchedule", "Guardar y Programar")
                  : t("governance.data.datasets.detail.analysisConfig.save", "Guardar Configuración")}
              </Button>
            </div>
          </Card>
        </DialogContent>
      </Dialog>
    </div>
  );
}

// Componente: Pestaña de Riesgos
function RisksTabContent({ datasetId }: { datasetId: string }) {
  const { t } = useTranslation();
  const [risks, setRisks] = useState<DataGovernanceDatasetRisk[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    loadRisks();
  }, [datasetId]);

  const loadRisks = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/risks`);
      if (response.ok) {
        const data = await response.json();
        setRisks(data.risks || []);
      }
    } catch (error) {
      console.error("Error loading risks:", error);
    } finally {
      setLoading(false);
    }
  };

  const getRiskScoreColor = (score?: number) => {
    if (!score) return "bg-gray-100 text-gray-800";
    if (score >= 0.8) return "bg-red-100 text-red-800";
    if (score >= 0.6) return "bg-yellow-100 text-yellow-800";
    return "bg-green-100 text-green-800";
  };

  const getRiskTypeColor = (type: string) => {
    const colors: Record<string, string> = {
      QUALITY: "bg-blue-100 text-blue-800",
      BIAS: "bg-purple-100 text-purple-800",
      SECURITY: "bg-red-100 text-red-800",
      PRIVACITY: "bg-orange-100 text-orange-800",
      COMPLIANCE: "bg-yellow-100 text-yellow-800",
      LEGAL: "bg-indigo-100 text-indigo-800",
    };
    return colors[type] || "bg-gray-100 text-gray-800";
  };

  if (loading) {
    return <div className="text-center py-8">Cargando riesgos...</div>;
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>
              {t("governance.data.datasets.detail.risks.title", "Gestión de Riesgos")}
            </CardTitle>
            <Dialog open={openDialog} onOpenChange={setOpenDialog}>
              <DialogTrigger asChild>
                <Button>
                  <Plus className="mr-2 h-4 w-4" />
                  {t("governance.data.datasets.detail.risks.add", "Agregar Riesgo")}
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-2xl">
                <DialogHeader>
                  <DialogTitle>
                    {t("governance.data.datasets.detail.risks.add", "Agregar Riesgo")}
                  </DialogTitle>
                </DialogHeader>
                <RiskForm
                  datasetId={datasetId}
                  onSuccess={() => {
                    setOpenDialog(false);
                    loadRisks();
                  }}
                />
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardBody>
          {risks.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              {t("governance.data.datasets.detail.risks.noRisks", "No hay riesgos identificados")}
            </div>
          ) : (
            <div className="space-y-4">
              {risks.map((risk) => (
                <Card key={risk.idxrisk} className="border-l-4 border-l-orange-500">
                  <CardBody>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <Badge className={getRiskTypeColor(risk.dtgrisktype)}>
                            {risk.dtgrisktype}
                          </Badge>
                          <h4 className="font-semibold">{risk.dtgriskname}</h4>
                          {risk.dtgriskscore && (
                            <Badge className={getRiskScoreColor(risk.dtgriskscore)}>
                              Score: {(risk.dtgriskscore * 100).toFixed(0)}%
                            </Badge>
                          )}
                        </div>
                        <p className="text-sm text-muted-foreground mb-3">
                          {risk.dtgriskdescription}
                        </p>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                          <div>
                            <span className="text-muted-foreground">Probabilidad:</span>
                            <Badge variant="outline" className="ml-2">
                              {risk.dtgriskprobability}
                            </Badge>
                          </div>
                          <div>
                            <span className="text-muted-foreground">Impacto:</span>
                            <Badge variant="outline" className="ml-2">
                              {risk.dtgriskimpact}
                            </Badge>
                          </div>
                          <div>
                            <span className="text-muted-foreground">Estado:</span>
                            <Badge variant="outline" className="ml-2">
                              {risk.dtgriskstatus}
                            </Badge>
                          </div>
                          <div>
                            <span className="text-muted-foreground">Mitigación:</span>
                            <Badge variant="outline" className="ml-2">
                              {risk.dtgmitigationstatus || "N/A"}
                            </Badge>
                          </div>
                        </div>
                        {risk.dtgmitigationplan && (
                          <div className="mt-3 p-3 bg-blue-50 rounded-md">
                            <p className="text-sm font-medium mb-1">Plan de Mitigación:</p>
                            <p className="text-sm">{risk.dtgmitigationplan}</p>
                          </div>
                        )}
                      </div>
                      <div className="flex gap-2 ml-4">
                        <Button variant="ghost" size="sm">
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="sm">
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              ))}
            </div>
          )}
        </CardBody>
      </Card>

      {/* Matriz de Riesgos */}
      <Card>
        <CardHeader>
          <CardTitle>
            {t("governance.data.datasets.detail.risks.matrix", "Matriz de Riesgos")}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <RiskMatrix risks={risks} />
        </CardBody>
      </Card>
    </div>
  );
}

// Componente: Matriz de Riesgos
function RiskMatrix({ risks }: { risks: DataGovernanceDatasetRisk[] }) {
  const matrix = [
    ["", "LOW", "MEDIUM", "HIGH", "CRITICAL"],
    ["LOW", [], [], [], []],
    ["MEDIUM", [], [], [], []],
    ["HIGH", [], [], [], []],
    ["CRITICAL", [], [], [], []],
  ];

  risks.forEach((risk) => {
    const probIndex = ["LOW", "MEDIUM", "HIGH", "CRITICAL"].indexOf(risk.dtgriskprobability);
    const impactIndex = ["LOW", "MEDIUM", "HIGH", "CRITICAL"].indexOf(risk.dtgriskimpact);
    if (probIndex >= 0 && impactIndex >= 0 && matrix[probIndex + 1] && matrix[probIndex + 1][impactIndex + 1]) {
      (matrix[probIndex + 1][impactIndex + 1] as any[]).push(risk);
    }
  });

  return (
    <div className="overflow-x-auto">
      <table className="w-full border-collapse">
        <thead>
          <tr>
            {matrix[0].map((header, idx) => (
              <th
                key={idx}
                className="border p-2 bg-gray-100 font-semibold text-sm"
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {matrix.slice(1).map((row, probIdx) => (
            <tr key={probIdx}>
              <td className="border p-2 bg-gray-50 font-semibold text-sm">
                {row[0]}
              </td>
              {row.slice(1).map((cell, impactIdx) => {
                const risksInCell = cell as DataGovernanceDatasetRisk[];
                const score = probIdx * 0.25 + impactIdx * 0.25;
                const bgColor =
                  score >= 0.75
                    ? "bg-red-100"
                    : score >= 0.5
                    ? "bg-yellow-100"
                    : "bg-green-100";
                return (
                  <td
                    key={impactIdx}
                    className={`border p-2 ${bgColor} min-w-[120px]`}
                  >
                    {risksInCell.length > 0 && (
                      <div className="space-y-1">
                        {risksInCell.map((risk) => (
                          <div
                            key={risk.idxrisk}
                            className="text-xs p-1 bg-white rounded border"
                          >
                            {risk.dtgriskname}
                          </div>
                        ))}
                      </div>
                    )}
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

// Componente: Formulario de Riesgo
function RiskForm({
  datasetId,
  onSuccess,
}: {
  datasetId: string;
  onSuccess: () => void;
}) {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    dtgrisktype: "QUALITY" as DataGovernanceDatasetRisk["dtgrisktype"],
    dtgriskname: "",
    dtgriskdescription: "",
    dtgriskprobability: "MEDIUM" as DataGovernanceDatasetRisk["dtgriskprobability"],
    dtgriskimpact: "MEDIUM" as DataGovernanceDatasetRisk["dtgriskimpact"],
    dtgmitigationplan: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/risks`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      if (response.ok) {
        onSuccess();
      }
    } catch (error) {
      console.error("Error creating risk:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <Label>Tipo de Riesgo</Label>
        <Select
          value={formData.dtgrisktype}
          onValueChange={(value: any) =>
            setFormData({ ...formData, dtgrisktype: value })
          }
        >
          <SelectTrigger>
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="QUALITY">Calidad</SelectItem>
            <SelectItem value="BIAS">Sesgos</SelectItem>
            <SelectItem value="SECURITY">Seguridad</SelectItem>
            <SelectItem value="PRIVACITY">Privacidad</SelectItem>
            <SelectItem value="COMPLIANCE">Compliance</SelectItem>
            <SelectItem value="LEGAL">Legal</SelectItem>
          </SelectContent>
        </Select>
      </div>
      <div>
        <Label>Nombre del Riesgo *</Label>
        <Input
          value={formData.dtgriskname}
          onChange={(e) => setFormData({ ...formData, dtgriskname: e.target.value })}
          required
        />
      </div>
      <div>
        <Label>Descripción</Label>
        <Textarea
          value={formData.dtgriskdescription}
          onChange={(e) => setFormData({ ...formData, dtgriskdescription: e.target.value })}
          rows={3}
        />
      </div>
      <div className="grid grid-cols-2 gap-4">
        <div>
          <Label>Probabilidad</Label>
          <Select
            value={formData.dtgriskprobability}
            onValueChange={(value: any) =>
              setFormData({ ...formData, dtgriskprobability: value })
            }
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="LOW">Baja</SelectItem>
              <SelectItem value="MEDIUM">Media</SelectItem>
              <SelectItem value="HIGH">Alta</SelectItem>
              <SelectItem value="CRITICAL">Crítica</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label>Impacto</Label>
          <Select
            value={formData.dtgriskimpact}
            onValueChange={(value: any) =>
              setFormData({ ...formData, dtgriskimpact: value })
            }
          >
            <SelectTrigger>
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="LOW">Bajo</SelectItem>
              <SelectItem value="MEDIUM">Medio</SelectItem>
              <SelectItem value="HIGH">Alto</SelectItem>
              <SelectItem value="CRITICAL">Crítico</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>
      <div>
        <Label>Plan de Mitigación</Label>
        <Textarea
          value={formData.dtgmitigationplan}
          onChange={(e) => setFormData({ ...formData, dtgmitigationplan: e.target.value })}
          rows={3}
        />
      </div>
      <div className="flex justify-end gap-2">
        <Button type="submit">Crear Riesgo</Button>
      </div>
    </form>
  );
}

// Componente: Pestaña de Métricas de Calidad Detalladas
function QualityMetricsTabContent({ datasetId, overallScore }: { datasetId: string; overallScore?: number }) {
  const { t } = useTranslation();
  const [metrics, setMetrics] = useState<DataGovernanceQualityMetric[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMetrics();
  }, [datasetId]);

  const loadMetrics = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/quality-metrics`);
      if (response.ok) {
        const data = await response.json();
        setMetrics(data.metrics || []);
      }
    } catch (error) {
      console.error("Error loading quality metrics:", error);
    } finally {
      setLoading(false);
    }
  };

  const dimensionNames: Record<string, string> = {
    COMPLETENESS: "Completitud",
    ACCURACY: "Precisión",
    CONSISTENCY: "Consistencia",
    VALIDITY: "Validez",
    TIMELINESS: "Puntualidad",
    UNIQUENESS: "Unicidad",
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PASS": return "bg-green-100 text-green-800";
      case "WARNING": return "bg-yellow-100 text-yellow-800";
      case "FAIL": return "bg-red-100 text-red-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  if (loading) {
    return <div className="text-center py-8">Cargando métricas...</div>;
  }

  return (
    <div className="space-y-6">
      {overallScore && (
        <Card>
          <CardHeader>
            <CardTitle>Score Global de Calidad</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="text-4xl font-bold mb-2">
              {(overallScore * 100).toFixed(0)}%
            </div>
            <p className="text-sm text-muted-foreground">
              Promedio de las 6 dimensiones de calidad
            </p>
          </CardBody>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Métricas por Dimensión (ISO 8000)</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {metrics.map((metric) => (
              <Card key={metric.idxmetric} className="border-l-4 border-l-blue-500">
                <CardBody>
                  <div className="flex items-center justify-between mb-2">
                    <h4 className="font-semibold">
                      {dimensionNames[metric.dtgqualitydimension] || metric.dtgqualitydimension}
                    </h4>
                    <Badge className={getStatusColor(metric.dtgqualitystatus)}>
                      {metric.dtgqualitystatus}
                    </Badge>
                  </div>
                  <div className="text-2xl font-bold mb-2">
                    {(metric.dtgqualityscore * 100).toFixed(0)}%
                  </div>
                  {metric.dtgqualitythreshold && (
                    <div className="text-xs text-muted-foreground">
                      Umbral: {(metric.dtgqualitythreshold * 100).toFixed(0)}%
                    </div>
                  )}
                  {metric.dtgqualitydetails && (
                    <div className="mt-3 text-xs space-y-1">
                      {Object.entries(metric.dtgqualitydetails).slice(0, 3).map(([key, value]) => (
                        <div key={key} className="flex justify-between">
                          <span className="text-muted-foreground">{key}:</span>
                          <span>{String(value)}</span>
                        </div>
                      ))}
                    </div>
                  )}
                </CardBody>
              </Card>
            ))}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}

// Componente: Pestaña de Privacidad/GDPR
function PrivacyTabContent({ datasetId }: { datasetId: string }) {
  const { t } = useTranslation();
  const [privacy, setPrivacy] = useState<DataGovernanceDatasetPrivacy | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPrivacy();
  }, [datasetId]);

  const loadPrivacy = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/privacy`);
      if (response.ok) {
        const data = await response.json();
        setPrivacy(data.privacy);
      }
    } catch (error) {
      console.error("Error loading privacy:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-center py-8">Cargando información de privacidad...</div>;
  }

  if (!privacy) {
    return (
      <Card>
        <CardBody className="text-center py-8">
          <p className="text-muted-foreground mb-4">No hay información de privacidad configurada</p>
          <Button onClick={() => {
            // TODO: Implementar creación
            alert("Funcionalidad en desarrollo");
          }}>
            Configurar Privacidad
          </Button>
        </CardBody>
      </Card>
    );
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Detección de PII</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span>PII Detectado:</span>
              <Badge className={privacy.dtgpiipresent ? "bg-red-100 text-red-800" : "bg-green-100 text-green-800"}>
                {privacy.dtgpiipresent ? "Sí" : "No"}
              </Badge>
            </div>
            {privacy.dtgpiitypes && (
              <div>
                <span className="text-sm font-medium">Tipos de PII:</span>
                <div className="flex flex-wrap gap-2 mt-2">
                  {privacy.dtgpiitypes.split(", ").map((type) => (
                    <Badge key={type} variant="outline">{type}</Badge>
                  ))}
                </div>
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Base Legal (GDPR Art. 6)</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div>
              <span className="text-sm font-medium">Base Legal:</span>
              <Badge className="ml-2">{privacy.dtglegalbasis || "No configurado"}</Badge>
            </div>
            {privacy.dtgconsentrequired && (
              <div>
                <span className="text-sm font-medium">Consentimiento:</span>
                <Badge className={`ml-2 ${privacy.dtgconsentobtained ? "bg-green-100 text-green-800" : "bg-yellow-100 text-yellow-800"}`}>
                  {privacy.dtgconsentobtained ? "Obtenido" : "Pendiente"}
                </Badge>
                {privacy.dtgconsentdate && (
                  <p className="text-xs text-muted-foreground mt-1">
                    Fecha: {new Date(privacy.dtgconsentdate).toLocaleDateString()}
                  </p>
                )}
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Retención de Datos</CardTitle>
        </CardHeader>
        <CardBody>
          {privacy.dtgretentionperiod ? (
            <div className="space-y-2">
              <p><span className="font-medium">Período:</span> {privacy.dtgretentionperiod} días</p>
              {privacy.dtgretentionpolicy && (
                <p className="text-sm text-muted-foreground">{privacy.dtgretentionpolicy}</p>
              )}
              {privacy.dtgretentionenddate && (
                <p className="text-xs text-muted-foreground">
                  Eliminación automática: {new Date(privacy.dtgretentionenddate).toLocaleDateString()}
                </p>
              )}
            </div>
          ) : (
            <p className="text-muted-foreground">No configurado</p>
          )}
        </CardBody>
      </Card>

      {privacy.dtgdpiacompleted && (
        <Card>
          <CardHeader>
            <CardTitle>DPIA (Data Protection Impact Assessment)</CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-2">
              <Badge className={privacy.dtgdpiaresult === "HIGH_RISK" ? "bg-red-100 text-red-800" : privacy.dtgdpiaresult === "MEDIUM_RISK" ? "bg-yellow-100 text-yellow-800" : "bg-green-100 text-green-800"}>
                {privacy.dtgdpiaresult}
              </Badge>
              {privacy.dtgdpiadate && (
                <p className="text-xs text-muted-foreground">
                  Fecha: {new Date(privacy.dtgdpiadate).toLocaleDateString()}
                </p>
              )}
              {privacy.dtgdpiarecommendations && (
                <p className="text-sm mt-2">{privacy.dtgdpiarecommendations}</p>
              )}
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
}

// Componente: Pestaña de Línea de Base
function LineageTabContent({ datasetId }: { datasetId: string }) {
  const { t } = useTranslation();
  const [lineage, setLineage] = useState<DataGovernanceLineage[]>([]);
  const [loading, setLoading] = useState(true);
  const [viewMode, setViewMode] = useState<"graph" | "table">("graph");
  const [currentDataset, setCurrentDataset] = useState<DataGovernanceDataset | null>(null);

  useEffect(() => {
    loadLineage();
    loadCurrentDataset();
  }, [datasetId]);

  const loadCurrentDataset = async () => {
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}`);
      if (response.ok) {
        const data = await response.json();
        setCurrentDataset(data);
      }
    } catch (error) {
      console.error("Error loading dataset:", error);
    }
  };

  const loadLineage = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/lineage`);
      if (response.ok) {
        const data = await response.json();
        setLineage(data.lineage || []);
      }
    } catch (error) {
      console.error("Error loading lineage:", error);
    } finally {
      setLoading(false);
    }
  };

  const typeNames: Record<string, string> = {
    TRANSFORMATION: "Transformación",
    AGGREGATION: "Agregación",
    FILTER: "Filtro",
    JOIN: "Join",
    SPLIT: "División",
    MERGE: "Fusión",
  };

  const typeIcons: Record<string, any> = {
    TRANSFORMATION: GitBranch,
    AGGREGATION: Network,
    FILTER: Filter,
    JOIN: GitMerge,
    SPLIT: ArrowDown,
    MERGE: GitMerge,
  };

  const typeColors: Record<string, string> = {
    TRANSFORMATION: "bg-blue-100 text-blue-800 border-blue-300",
    AGGREGATION: "bg-purple-100 text-purple-800 border-purple-300",
    FILTER: "bg-yellow-100 text-yellow-800 border-yellow-300",
    JOIN: "bg-green-100 text-green-800 border-green-300",
    SPLIT: "bg-orange-100 text-orange-800 border-orange-300",
    MERGE: "bg-pink-100 text-pink-800 border-pink-300",
  };

  if (loading) {
    return <div className="text-center py-8">Cargando línea de base...</div>;
  }

  // Construir grafo de dependencias
  const buildGraph = () => {
    const nodes: Array<{ id: string; label: string; type: string; data: any }> = [];
    const edges: Array<{ from: string; to: string; type: string; data: any }> = [];

    // Nodo central (dataset actual)
    if (currentDataset) {
      nodes.push({
        id: `dataset-${datasetId}`,
        label: currentDataset.dtgname || `Dataset ${datasetId}`,
        type: "DATASET",
        data: currentDataset,
      });
    }

    // Nodos y aristas de transformaciones
    lineage.forEach((item) => {
      const nodeId = `lineage-${item.idxlineage}`;
      const Icon = typeIcons[item.dtglineagetype || ""] || Network;

      nodes.push({
        id: nodeId,
        label: typeNames[item.dtglineagetype || ""] || item.dtglineagetype || "Transformación",
        type: item.dtglineagetype || "TRANSFORMATION",
        data: item,
      });

      // Conectar con dataset origen si existe
      if (item.idxsourcedataset) {
        edges.push({
          from: `dataset-${item.idxsourcedataset}`,
          to: nodeId,
          type: item.dtglineagetype || "TRANSFORMATION",
          data: item,
        });
      }

      // Conectar transformación con dataset actual
      edges.push({
        from: nodeId,
        to: `dataset-${datasetId}`,
        type: item.dtglineagetype || "TRANSFORMATION",
        data: item,
      });
    });

    return { nodes, edges };
  };

  const { nodes, edges } = buildGraph();

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Network className="h-5 w-5" />
              Línea de Base y Dependencias
            </CardTitle>
            <div className="flex gap-2">
              <Button
                variant={viewMode === "graph" ? "primary" : "outline"}
                size="sm"
                onClick={() => setViewMode("graph")}
              >
                <Network className="h-4 w-4 mr-2" />
                Vista Gráfica
              </Button>
              <Button
                variant={viewMode === "table" ? "primary" : "outline"}
                size="sm"
                onClick={() => setViewMode("table")}
              >
                <FileCheck className="h-4 w-4 mr-2" />
                Vista Tabular
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardBody>
          {lineage.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">
              No hay transformaciones registradas
            </p>
          ) : viewMode === "graph" ? (
            <LineageGraphView nodes={nodes} edges={edges} typeNames={typeNames} typeColors={typeColors} typeIcons={typeIcons} />
          ) : (
            <div className="space-y-4">
              {lineage.map((item, idx) => (
                <Card key={item.idxlineage} className="border-l-4 border-l-blue-500">
                  <CardBody>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <Badge>{typeNames[item.dtglineagetype || ""] || item.dtglineagetype}</Badge>
                          {item.idxsourcedataset && (
                            <span className="text-sm text-muted-foreground">
                              Desde dataset: {item.idxsourcedataset}
                            </span>
                          )}
                        </div>
                        {item.dtglineagedetails && (
                          <div className="space-y-1 text-sm">
                            {Object.entries(item.dtglineagedetails).slice(0, 4).map(([key, value]) => (
                              <div key={key} className="flex justify-between">
                                <span className="text-muted-foreground">{key}:</span>
                                <span>{String(value)}</span>
                              </div>
                            ))}
                          </div>
                        )}
                        {item.dtglineagetimestamp && (
                          <p className="text-xs text-muted-foreground mt-2">
                            {new Date(item.dtglineagetimestamp).toLocaleString()}
                          </p>
                        )}
                      </div>
                    </div>
                  </CardBody>
                </Card>
              ))}
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}

// Componente: Vista Gráfica de Línea de Base
function LineageGraphView({
  nodes,
  edges,
  typeNames,
  typeColors,
  typeIcons
}: {
  nodes: Array<{ id: string; label: string; type: string; data: any }>;
  edges: Array<{ from: string; to: string; type: string; data: any }>;
  typeNames: Record<string, string>;
  typeColors: Record<string, string>;
  typeIcons: Record<string, any>;
}) {
  const [selectedNode, setSelectedNode] = useState<string | null>(null);

  // Layout simple: dataset central, transformaciones alrededor
  const centerX = 400;
  const centerY = 300;
  const radius = 200;
  const angleStep = (2 * Math.PI) / Math.max(nodes.length - 1, 1);

  const getNodePosition = (index: number, isCenter: boolean) => {
    if (isCenter) {
      return { x: centerX, y: centerY };
    }
    const angle = (index - 1) * angleStep;
    return {
      x: centerX + radius * Math.cos(angle),
      y: centerY + radius * Math.sin(angle),
    };
  };

  const getNodeColor = (type: string) => {
    if (type === "DATASET") return "bg-blue-500";
    return typeColors[type]?.split(" ")[0] || "bg-gray-500";
  };

  return (
    <div className="relative w-full h-[600px] border rounded-lg bg-gray-50 dark:bg-gray-900 overflow-hidden">
      <svg width="100%" height="100%" className="absolute inset-0">
        {/* Dibujar aristas */}
        {edges.map((edge, idx) => {
          const fromNode = nodes.find(n => n.id === edge.from);
          const toNode = nodes.find(n => n.id === edge.to);
          if (!fromNode || !toNode) return null;

          const fromIndex = nodes.indexOf(fromNode);
          const toIndex = nodes.indexOf(toNode);
          const fromPos = getNodePosition(fromIndex, fromNode.type === "DATASET");
          const toPos = getNodePosition(toIndex, toNode.type === "DATASET");

          return (
            <g key={`edge-${idx}`}>
              <line
                x1={fromPos.x}
                y1={fromPos.y}
                x2={toPos.x}
                y2={toPos.y}
                stroke="#94a3b8"
                strokeWidth="2"
                markerEnd="url(#arrowhead)"
                className="hover:stroke-blue-500 transition-colors"
              />
            </g>
          );
        })}

        {/* Definir flecha */}
        <defs>
          <marker
            id="arrowhead"
            markerWidth="10"
            markerHeight="10"
            refX="9"
            refY="3"
            orient="auto"
          >
            <polygon points="0 0, 10 3, 0 6" fill="#94a3b8" />
          </marker>
        </defs>

        {/* Dibujar nodos */}
        {nodes.map((node, idx) => {
          const pos = getNodePosition(idx, node.type === "DATASET");
          const Icon = typeIcons[node.type] || Network;
          const isSelected = selectedNode === node.id;

          return (
            <g key={node.id}>
              {/* Círculo del nodo */}
              <circle
                cx={pos.x}
                cy={pos.y}
                r={node.type === "DATASET" ? 50 : 35}
                fill={node.type === "DATASET" ? "#3b82f6" : "#64748b"}
                stroke={isSelected ? "#f59e0b" : "#1e293b"}
                strokeWidth={isSelected ? 3 : 2}
                className="cursor-pointer hover:opacity-80 transition-opacity"
                onClick={() => setSelectedNode(isSelected ? null : node.id)}
              />
              {/* Icono */}
              <foreignObject
                x={pos.x - (node.type === "DATASET" ? 20 : 15)}
                y={pos.y - (node.type === "DATASET" ? 20 : 15)}
                width={node.type === "DATASET" ? 40 : 30}
                height={node.type === "DATASET" ? 40 : 30}
              >
                <div className="flex items-center justify-center h-full text-white">
                  <Icon className={`h-${node.type === "DATASET" ? "6" : "4"} w-${node.type === "DATASET" ? "6" : "4"}`} />
                </div>
              </foreignObject>
              {/* Etiqueta */}
              <text
                x={pos.x}
                y={pos.y + (node.type === "DATASET" ? 75 : 55)}
                textAnchor="middle"
                className="text-xs font-medium fill-gray-700 dark:fill-gray-300"
                style={{ fontSize: node.type === "DATASET" ? "12px" : "10px" }}
              >
                {node.label.length > 15 ? node.label.substring(0, 15) + "..." : node.label}
              </text>
            </g>
          );
        })}
      </svg>

      {/* Panel de detalles del nodo seleccionado */}
      {selectedNode && (
        <div className="absolute top-4 right-4 w-80 bg-white dark:bg-gray-800 border rounded-lg shadow-lg p-4 z-10">
          {(() => {
            const node = nodes.find(n => n.id === selectedNode);
            if (!node) return null;
            return (
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <h4 className="font-semibold">{node.label}</h4>
                  <Button
                    variant="ghost"
                    size="icon"
                    className="h-6 w-6"
                    onClick={() => setSelectedNode(null)}
                  >
                    <X className="h-4 w-4" />
                  </Button>
                </div>
                <Badge className={typeColors[node.type] || "bg-gray-100 text-gray-800"}>
                  {typeNames[node.type] || node.type}
                </Badge>
                {node.data.dtglineagedetails && (
                  <div className="space-y-2 text-sm">
                    {Object.entries(node.data.dtglineagedetails).slice(0, 5).map(([key, value]) => (
                      <div key={key} className="flex justify-between">
                        <span className="text-muted-foreground">{key}:</span>
                        <span className="font-medium">{String(value)}</span>
                      </div>
                    ))}
                  </div>
                )}
                {node.data.dtglineagetimestamp && (
                  <p className="text-xs text-muted-foreground">
                    {new Date(node.data.dtglineagetimestamp).toLocaleString()}
                  </p>
                )}
              </div>
            );
          })()}
        </div>
      )}
    </div>
  );
}

// Componente: Pestaña de Documentación
function DocumentationTabContent({ datasetId }: { datasetId: string }) {
  const { t } = useTranslation();
  const [docs, setDocs] = useState<DataGovernanceDatasetDocumentation[]>([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    loadDocumentation();
  }, [datasetId]);

  const loadDocumentation = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/documentation`);
      if (response.ok) {
        const data = await response.json();
        setDocs(data.documentation || []);
      }
    } catch (error) {
      console.error("Error loading documentation:", error);
    } finally {
      setLoading(false);
    }
  };

  const typeNames: Record<string, string> = {
    DECISION: "Decisión",
    TRANSFORMATION: "Transformación",
    APPROVAL: "Aprobación",
    REJECTION: "Rechazo",
    CHANGE: "Cambio",
    INCIDENT: "Incidente",
  };

  if (loading) {
    return <div className="text-center py-8">Cargando documentación...</div>;
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Documentación y Trazabilidad</CardTitle>
            <Dialog open={openDialog} onOpenChange={setOpenDialog}>
              <DialogTrigger asChild>
                <Button>
                  <Plus className="mr-2 h-4 w-4" />
                  Agregar Documentación
                </Button>
              </DialogTrigger>
              <DialogContent className="max-w-2xl">
                <DialogHeader>
                  <DialogTitle>Nueva Documentación</DialogTitle>
                </DialogHeader>
                <DocumentationForm
                  datasetId={datasetId}
                  onSuccess={() => {
                    setOpenDialog(false);
                    loadDocumentation();
                  }}
                />
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardBody>
          {docs.length === 0 ? (
            <p className="text-muted-foreground text-center py-8">
              No hay documentación registrada
            </p>
          ) : (
            <div className="space-y-4">
              {docs.map((doc) => (
                <Card key={doc.idxdoc} className="border-l-4 border-l-green-500">
                  <CardBody>
                    <div className="flex items-start justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-2">
                          <Badge variant="outline">
                            {typeNames[doc.dtgdocumenttype] || doc.dtgdocumenttype}
                          </Badge>
                          <h4 className="font-semibold">{doc.dtgdocumenttitle}</h4>
                          {doc.dtgdocumentversion && (
                            <Badge variant="outline" className="text-xs">
                              v{doc.dtgdocumentversion}
                            </Badge>
                          )}
                        </div>
                        <p className="text-sm text-muted-foreground mb-2">{doc.dtgdocumentcontent}</p>
                        <div className="flex items-center gap-4 text-xs text-muted-foreground">
                          <span>{new Date(doc.dtgdocumentdate).toLocaleString()}</span>
                          {doc.dtgdocumenttags && (
                            <div className="flex gap-1">
                              {doc.dtgdocumenttags.split(", ").map((tag) => (
                                <Badge key={tag} variant="outline" className="text-xs">{tag}</Badge>
                              ))}
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  </CardBody>
                </Card>
              ))}
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}

// Componente: Formulario de Documentación
function DocumentationForm({
  datasetId,
  onSuccess,
}: {
  datasetId: string;
  onSuccess: () => void;
}) {
  const [formData, setFormData] = useState({
    dtgdocumenttype: "DECISION" as DataGovernanceDatasetDocumentation["dtgdocumenttype"],
    dtgdocumenttitle: "",
    dtgdocumentcontent: "",
    dtgdocumenttags: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/documentation`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });
      if (response.ok) {
        onSuccess();
      }
    } catch (error) {
      console.error("Error creating documentation:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <Label>Tipo de Documentación</Label>
        <Select
          value={formData.dtgdocumenttype}
          onValueChange={(value: any) =>
            setFormData({ ...formData, dtgdocumenttype: value })
          }
        >
          <SelectTrigger>
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="DECISION">Decisión</SelectItem>
            <SelectItem value="TRANSFORMATION">Transformación</SelectItem>
            <SelectItem value="APPROVAL">Aprobación</SelectItem>
            <SelectItem value="REJECTION">Rechazo</SelectItem>
            <SelectItem value="CHANGE">Cambio</SelectItem>
            <SelectItem value="INCIDENT">Incidente</SelectItem>
          </SelectContent>
        </Select>
      </div>
      <div>
        <Label>Título *</Label>
        <Input
          value={formData.dtgdocumenttitle}
          onChange={(e) => setFormData({ ...formData, dtgdocumenttitle: e.target.value })}
          required
        />
      </div>
      <div>
        <Label>Contenido *</Label>
        <Textarea
          value={formData.dtgdocumentcontent}
          onChange={(e) => setFormData({ ...formData, dtgdocumentcontent: e.target.value })}
          rows={5}
          required
        />
      </div>
      <div>
        <Label>Tags (separados por comas)</Label>
        <Input
          value={formData.dtgdocumenttags}
          onChange={(e) => setFormData({ ...formData, dtgdocumenttags: e.target.value })}
          placeholder="aprobación, calidad, transformación"
        />
      </div>
      <div className="flex justify-end gap-2">
        <Button type="submit">Crear Documentación</Button>
      </div>
    </form>
  );
}

// Componente: Pestaña de Roles
function RolesTabContent({ datasetId, datasetName }: { datasetId: string; datasetName: string }) {
  const { t } = useTranslation();
  const [roles, setRoles] = useState<Array<{
    roleId?: number;
    userId: number;
    userName: string;
    roleType: "OWNER" | "STEWARD" | "COMPLIANCE_OFFICER" | "DATA_SCIENTIST" | "REVIEWER" | "APPROVER";
    status: "ACTIVE" | "INACTIVE";
    permissions: {
      read: boolean;
      write: boolean;
      delete: boolean;
      approve: boolean;
    };
  }>>([]);
  const [users, setUsers] = useState<Array<{ id: number; name: string; email: string }>>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingRole, setEditingRole] = useState<typeof roles[0] | null>(null);
  const [formData, setFormData] = useState({
    userId: "",
    roleType: "" as typeof roles[0]["roleType"] | "",
    status: "ACTIVE" as "ACTIVE" | "INACTIVE",
    permissions: {
      read: true,
      write: false,
      delete: false,
      approve: false,
    },
  });

  const roleNames: Record<typeof roles[0]["roleType"], string> = {
    OWNER: "Propietario",
    STEWARD: "Data Steward",
    COMPLIANCE_OFFICER: "Compliance Officer",
    DATA_SCIENTIST: "Data Scientist",
    REVIEWER: "Revisor",
    APPROVER: "Aprobador",
  };

  useEffect(() => {
    loadRoles();
    loadUsers();
  }, [datasetId]);

  const loadRoles = async () => {
    try {
      // TODO: Llamar al backend real
      // const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/roles`);
      // const data = await response.json();
      // setRoles(data);

      // Mock data
      setRoles([
        {
          roleId: 1,
          userId: 1,
          userName: "Juan Pérez",
          roleType: "OWNER",
          status: "ACTIVE",
          permissions: { read: true, write: true, delete: true, approve: true }
        },
        {
          roleId: 2,
          userId: 2,
          userName: "María García",
          roleType: "STEWARD",
          status: "ACTIVE",
          permissions: { read: true, write: true, delete: false, approve: false }
        },
      ]);
    } catch (error) {
      console.error("Error loading roles:", error);
    }
  };

  const loadUsers = async () => {
    try {
      // TODO: Llamar al backend real
      // const response = await fetch("/api/v1/users");
      // const data = await response.json();
      // setUsers(data);

      // Mock data
      setUsers([
        { id: 1, name: "Juan Pérez", email: "juan.perez@example.com" },
        { id: 2, name: "María García", email: "maria.garcia@example.com" },
        { id: 3, name: "Carlos López", email: "carlos.lopez@example.com" },
        { id: 4, name: "Ana Martínez", email: "ana.martinez@example.com" },
        { id: 5, name: "Pedro Sánchez", email: "pedro.sanchez@example.com" },
      ]);
    } catch (error) {
      console.error("Error loading users:", error);
    }
  };

  const handleOpenDialog = (role?: typeof roles[0]) => {
    if (role) {
      setEditingRole(role);
      setFormData({
        userId: role.userId.toString(),
        roleType: role.roleType,
        status: role.status,
        permissions: role.permissions,
      });
    } else {
      setEditingRole(null);
      setFormData({
        userId: "",
        roleType: "" as typeof roles[0]["roleType"] | "",
        status: "ACTIVE",
        permissions: {
          read: true,
          write: false,
          delete: false,
          approve: false,
        },
      });
    }
    setIsDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setIsDialogOpen(false);
    setEditingRole(null);
    setFormData({
      userId: "",
      roleType: "" as typeof roles[0]["roleType"] | "",
      status: "ACTIVE",
      permissions: {
        read: true,
        write: false,
        delete: false,
        approve: false,
      },
    });
  };

  const handleSaveRole = async () => {
    if (!formData.userId || !formData.roleType) {
      alert("Por favor complete todos los campos requeridos");
      return;
    }

    try {
      const userId = parseInt(formData.userId);
      const user = users.find((u) => u.id === userId);

      if (editingRole) {
        setRoles((prev) =>
          prev.map((r) =>
            r.roleId === editingRole.roleId
              ? {
                  ...r,
                  userId,
                  userName: user?.name || "",
                  roleType: formData.roleType as typeof roles[0]["roleType"],
                  status: formData.status,
                  permissions: formData.permissions,
                }
              : r
          )
        );
      } else {
        const newRole = {
          roleId: Date.now(),
          userId,
          userName: user?.name || "",
          roleType: formData.roleType as typeof roles[0]["roleType"],
          status: formData.status,
          permissions: formData.permissions,
        };
        setRoles((prev) => [...prev, newRole]);
      }

      // TODO: Llamar al backend real
      handleCloseDialog();
      loadRoles();
    } catch (error) {
      console.error("Error saving role:", error);
      alert("Error al guardar el rol");
    }
  };

  const handleDeleteRole = async (roleId: number) => {
    if (!confirm("¿Está seguro de eliminar este rol?")) {
      return;
    }

    try {
      setRoles((prev) => prev.filter((r) => r.roleId !== roleId));
      // TODO: Llamar al backend real
      loadRoles();
    } catch (error) {
      console.error("Error deleting role:", error);
      alert("Error al eliminar el rol");
    }
  };

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <Users className="h-5 w-5" />
            {t("governance.data.datasets.detail.tabs.roles", "Roles y Responsabilidades")}
          </CardTitle>
          <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
            <DialogTrigger asChild>
              <Button onClick={() => handleOpenDialog()}>
                <Plus className="h-4 w-4 mr-2" />
                Asignar Rol
              </Button>
            </DialogTrigger>
            <DialogContent className="p-0" maxWidth="800px">
              <div className="p-6" style={{ padding: '1.5rem', maxHeight: '90vh', overflowY: 'auto' }}>
              <div className="mb-4 pb-4 border-b" style={{ borderBottomColor: 'hsl(var(--border))' }}>
              <DialogHeader>
                <div className="d-flex align-items-center gap-3 mb-3">
                  <div className="p-2 rounded" style={{ padding: '0.5rem', borderRadius: '0.5rem', backgroundColor: 'rgb(219 234 254)' }}>
                    <Users className="h-6 w-6" style={{ height: '1.5rem', width: '1.5rem', color: 'rgb(37 99 235)' }} />
                  </div>
                  <div className="flex-grow-1">
                    <DialogTitle className="mb-2 text-2xl font-bold">
                      {editingRole ? "Editar Rol Asignado" : "Asignar Nuevo Rol"}
                    </DialogTitle>
                    <p className="text-muted mb-0" style={{ fontSize: '0.875rem', color: 'hsl(var(--muted-foreground))', marginBottom: 0 }}>
                      {editingRole
                        ? "Modifica los permisos y responsabilidades del usuario en este dataset"
                        : "Asigna responsabilidades y permisos a un usuario para este dataset"}
                    </p>
                  </div>
                </div>
                <div className="p-3 rounded" style={{ padding: '0.75rem', borderRadius: '0.5rem', backgroundColor: 'hsl(var(--muted) / 0.5)' }}>
                  <p className="text-muted mb-1 small" style={{ fontSize: '0.75rem', fontWeight: '500', marginBottom: '0.25rem' }}>Dataset</p>
                  <p className="mb-0 fw-semibold" style={{ fontSize: '0.875rem', fontWeight: '600', marginBottom: 0 }}>{datasetName}</p>
                </div>
              </DialogHeader>
              </div>

              <div className="space-y-6 mt-6">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="user" className="text-base font-semibold">
                      Usuario *
                    </Label>
                    <span className="text-xs text-muted-foreground">Requerido</span>
                  </div>
                  <p className="text-xs text-muted-foreground mb-2">
                    Selecciona el usuario al que se asignará el rol
                  </p>
                  <select
                    id="user"
                    value={formData.userId}
                    onChange={(e) => setFormData({ ...formData, userId: e.target.value })}
                    className="w-100 form-select"
                    style={{ height: '44px', padding: '0.5rem 0.75rem', fontSize: '0.875rem' }}
                  >
                    <option value="">Seleccionar usuario...</option>
                    {users.map((user) => (
                      <option key={user.id} value={user.id.toString()}>
                        {user.name} ({user.email})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label htmlFor="roleType" className="text-base font-semibold">
                      Tipo de Rol *
                    </Label>
                    <span className="text-xs text-muted-foreground">Requerido</span>
                  </div>
                  <p className="text-xs text-muted-foreground mb-2">
                    Define las responsabilidades y permisos del usuario
                  </p>
                  <select
                    id="roleType"
                    value={formData.roleType}
                    onChange={(e) => setFormData({ ...formData, roleType: e.target.value as typeof roles[0]["roleType"] })}
                    className="w-100 form-select"
                    style={{ height: '44px', padding: '0.5rem 0.75rem', fontSize: '0.875rem' }}
                  >
                    <option value="">Seleccionar tipo de rol...</option>
                    {Object.entries(roleNames).map(([key, name]) => (
                      <option key={key} value={key}>
                        {name}
                      </option>
                    ))}
                  </select>
                  {formData.roleType && (
                    <p className="text-xs text-muted-foreground mt-2" style={{ fontSize: '0.75rem', marginTop: '0.5rem' }}>
                      {formData.roleType === "OWNER" && "Control total sobre el dataset. Puede modificar, eliminar y gestionar todos los aspectos."}
                      {formData.roleType === "STEWARD" && "Responsable de la calidad y gobierno del dataset. Puede revisar y aprobar cambios."}
                      {formData.roleType === "COMPLIANCE_OFFICER" && "Responsable de compliance y privacidad. Gestiona aspectos regulatorios."}
                      {formData.roleType === "DATA_SCIENTIST" && "Puede usar el dataset para análisis y modelos. Acceso de lectura y uso."}
                      {formData.roleType === "REVIEWER" && "Puede revisar el dataset pero no modificarlo. Solo lectura y comentarios."}
                      {formData.roleType === "APPROVER" && "Puede aprobar o rechazar cambios en el dataset. Permisos de aprobación."}
                    </p>
                  )}
                </div>

                <div className="space-y-2">
                  <Label htmlFor="status" className="text-base font-semibold">
                    Estado
                  </Label>
                  <p className="text-xs text-muted-foreground mb-2">
                    Define si el rol está activo o inactivo
                  </p>
                  <select
                    id="status"
                    value={formData.status}
                    onChange={(e) => setFormData({ ...formData, status: e.target.value as "ACTIVE" | "INACTIVE" })}
                    className="w-100 form-select"
                    style={{ height: '44px', padding: '0.5rem 0.75rem', fontSize: '0.875rem' }}
                  >
                    <option value="ACTIVE">Activo</option>
                    <option value="INACTIVE">Inactivo</option>
                  </select>
                </div>

                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label className="text-base font-semibold flex items-center gap-2">
                      <Shield className="h-4 w-4" />
                      Permisos Granulares
                    </Label>
                  </div>
                  <p className="text-xs text-muted-foreground mb-3">
                    Define permisos específicos por acción para este usuario
                  </p>
                  <div className="space-y-3 p-4 border rounded-lg bg-gray-50 dark:bg-gray-900">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Eye className="h-4 w-4 text-muted-foreground" />
                        <Label htmlFor="perm-read" className="text-sm font-medium cursor-pointer">
                          Lectura
                        </Label>
                      </div>
                      <Switch
                        id="perm-read"
                        checked={formData.permissions.read}
                        onChange={(checked) =>
                          setFormData({
                            ...formData,
                            permissions: { ...formData.permissions, read: checked },
                          })
                        }
                      />
                    </div>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Pencil className="h-4 w-4 text-muted-foreground" />
                        <Label htmlFor="perm-write" className="text-sm font-medium cursor-pointer">
                          Escritura
                        </Label>
                      </div>
                      <Switch
                        id="perm-write"
                        checked={formData.permissions.write}
                        onChange={(checked) =>
                          setFormData({
                            ...formData,
                            permissions: { ...formData.permissions, write: checked },
                          })
                        }
                      />
                    </div>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Trash className="h-4 w-4 text-muted-foreground" />
                        <Label htmlFor="perm-delete" className="text-sm font-medium cursor-pointer">
                          Eliminación
                        </Label>
                      </div>
                      <Switch
                        id="perm-delete"
                        checked={formData.permissions.delete}
                        onChange={(checked) =>
                          setFormData({
                            ...formData,
                            permissions: { ...formData.permissions, delete: checked },
                          })
                        }
                      />
                    </div>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <CheckCircle2 className="h-4 w-4 text-muted-foreground" />
                        <Label htmlFor="perm-approve" className="text-sm font-medium cursor-pointer">
                          Aprobación
                        </Label>
                      </div>
                      <Switch
                        id="perm-approve"
                        checked={formData.permissions.approve}
                        onChange={(checked) =>
                          setFormData({
                            ...formData,
                            permissions: { ...formData.permissions, approve: checked },
                          })
                        }
                      />
                    </div>
                  </div>
                </div>

                {formData.userId && formData.roleType && (
                  <div className="p-4 bg-blue-50 dark:bg-blue-950/30 border border-blue-200 dark:border-blue-800 rounded-lg">
                    <p className="text-sm font-semibold text-blue-900 dark:text-blue-100 mb-2">
                      Resumen de la asignación
                    </p>
                    <div className="space-y-2 text-sm">
                      <div className="flex items-center justify-between">
                        <span className="text-muted-foreground">Usuario:</span>
                        <span className="font-medium">
                          {users.find(u => u.id.toString() === formData.userId)?.name}
                        </span>
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-muted-foreground">Rol:</span>
                        <Badge variant="outline" className="font-medium">
                          {roleNames[formData.roleType]}
                        </Badge>
                      </div>
                      <div className="flex items-center justify-between">
                        <span className="text-muted-foreground">Estado:</span>
                        <Badge className={formData.status === "ACTIVE" ? "bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-200" : "bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-200"}>
                          {formData.status === "ACTIVE" ? "Activo" : "Inactivo"}
                        </Badge>
                      </div>
                    </div>
                  </div>
                )}
              </div>

              <div className="flex justify-end gap-3 pt-6 mt-6 border-t">
                <Button variant="outline" onClick={handleCloseDialog}>
                  Cancelar
                </Button>
                <Button
                  onClick={handleSaveRole}
                  disabled={!formData.userId || !formData.roleType}
                  className="min-w-[120px]"
                >
                  {editingRole ? (
                    <>
                      <Edit className="h-4 w-4 mr-2" />
                      Actualizar Rol
                    </>
                  ) : (
                    <>
                      <Plus className="h-4 w-4 mr-2" />
                      Asignar Rol
                    </>
                  )}
                </Button>
              </div>
              </div>
            </DialogContent>
          </Dialog>
        </div>
      </CardHeader>
      <CardBody>
        {roles.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            No hay roles asignados para este dataset. Haga clic en "Asignar Rol" para comenzar.
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {roles.map((role) => {
              const permissionIcons = {
                read: <Eye className="h-3 w-3" />,
                write: <Pencil className="h-3 w-3" />,
                delete: <Trash className="h-3 w-3" />,
                approve: <CheckCircle2 className="h-3 w-3" />,
              };
              return (
              <div key={role.roleId} className="p-4 border rounded-lg relative group">
                <div className="flex items-center justify-between mb-2">
                  <span className="font-medium">{role.userName}</span>
                  <Badge variant="outline">{roleNames[role.roleType] || role.roleType}</Badge>
                </div>
                <p className="text-xs text-muted-foreground mb-2">
                  Estado:{" "}
                  <Badge
                    className={`text-xs ${
                      role.status === "ACTIVE"
                        ? "bg-green-100 dark:bg-green-900/40 text-green-800 dark:text-green-200"
                        : "bg-gray-100 dark:bg-gray-800 text-gray-800 dark:text-gray-200"
                    }`}
                  >
                    {role.status}
                  </Badge>
                </p>
                <div className="mt-3 pt-3 border-t">
                  <p className="text-xs font-medium text-muted-foreground mb-2">Permisos:</p>
                  <div className="flex flex-wrap gap-2">
                    {role.permissions.read && (
                      <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <Eye className="h-3 w-3" /> Lectura
                      </Badge>
                    )}
                    {role.permissions.write && (
                      <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <Pencil className="h-3 w-3" /> Escritura
                      </Badge>
                    )}
                    {role.permissions.delete && (
                      <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <Trash className="h-3 w-3" /> Eliminación
                      </Badge>
                    )}
                    {role.permissions.approve && (
                      <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <CheckCircle2 className="h-3 w-3" /> Aprobación
                      </Badge>
                    )}
                    {!role.permissions.read && !role.permissions.write && !role.permissions.delete && !role.permissions.approve && (
                      <span className="text-xs text-muted-foreground">Sin permisos específicos</span>
                    )}
                  </div>
                </div>
                <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity flex gap-1">
                  <Button
                    variant="ghost"
                    size="sm"
                    className="h-6 w-6 p-0"
                    onClick={() => handleOpenDialog(role)}
                    title="Editar rol"
                  >
                    <Edit className="h-3 w-3" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    className="h-6 w-6 p-0 text-red-600 hover:text-red-700"
                    onClick={() => handleDeleteRole(role.roleId!)}
                    title="Eliminar rol"
                  >
                    <Trash2 className="h-3 w-3" />
                  </Button>
                </div>
              </div>
            );
            })}
          </div>
        )}
      </CardBody>
    </Card>
  );
}

// Componente: Análisis de Impacto de Cambios
// Componente: Análisis de Impacto de Cambios
function ImpactAnalysisTabContent({ datasetId }: { datasetId: string }) {
  const { t } = useTranslation();
  const router = useRouter();
  const [impactData, setImpactData] = useState<{
    affectedDatasets: Array<{ id: string; name: string; impact: "HIGH" | "MEDIUM" | "LOW"; reason: string }>;
    affectedOrigins: Array<{ id: string; name: string; type: string }>;
    totalImpact: number;
  } | null>(null);
  const [loading, setLoading] = useState(true);
  const [selectedOrigin, setSelectedOrigin] = useState<string | null>(null);

  useEffect(() => {
    loadImpactAnalysis();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [datasetId]);

  const loadImpactAnalysis = async () => {
    try {
      setLoading(true);
      // TODO: Llamar al backend real
      // const response = await fetch(`/api/v1/governance/data/datasets/${datasetId}/impact-analysis?originId=${selectedOrigin || ""}`);

      // Mock data
      setTimeout(() => {
        setImpactData({
          affectedDatasets: [
            { id: "2", name: "Dataset de Ventas Agregado", impact: "HIGH", reason: "Depende directamente de este dataset" },
            { id: "3", name: "Dataset de Análisis ML", impact: "MEDIUM", reason: "Usa este dataset como fuente de entrenamiento" },
            { id: "4", name: "Dataset de Reportes", impact: "LOW", reason: "Referencia indirecta" },
          ],
          affectedOrigins: [
            { id: "1", name: "Origen PostgreSQL - Ventas", type: "POSTGRESQL" },
            { id: "2", name: "Origen S3 - Datos Históricos", type: "S3" },
          ],
          totalImpact: 3,
        });
        setLoading(false);
      }, 500);
    } catch (error) {
      console.error("Error loading impact analysis:", error);
      setLoading(false);
    }
  };

  const impactColors = {
    HIGH: "bg-red-100 text-red-800 border-red-300 dark:bg-red-900/40 dark:text-red-200",
    MEDIUM: "bg-yellow-100 text-yellow-800 border-yellow-300 dark:bg-yellow-900/40 dark:text-yellow-200",
    LOW: "bg-green-100 text-green-800 border-green-300 dark:bg-green-900/40 dark:text-green-200",
  };

  if (loading) {
    return <div className="text-center py-8">Analizando impacto de cambios...</div>;
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="h-5 w-5" />
              Análisis de Impacto de Cambios
            </CardTitle>
            <Button
              variant="outline"
              size="sm"
              onClick={loadImpactAnalysis}
            >
              <AlertCircle className="h-4 w-4 mr-2" />
              Recalcular Impacto
            </Button>
          </div>
        </CardHeader>
        <CardBody>
          <div className="mb-6 p-4 bg-blue-50 dark:bg-blue-950/30 border border-blue-200 dark:border-blue-800 rounded-lg">
            <p className="text-sm font-semibold text-blue-900 dark:text-blue-100 mb-2">
              ¿Qué es el Análisis de Impacto?
            </p>
            <p className="text-sm text-blue-800 dark:text-blue-200">
              Este análisis muestra qué datasets y orígenes de datos se verían afectados si se realizan cambios en este dataset o en sus orígenes dependientes.
              Útil para planificar cambios y entender las dependencias del ecosistema de datos.
            </p>
          </div>

          {impactData ? (
            <>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <Card>
                  <CardBody>
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-muted-foreground">Total Datasets Afectados</p>
                        <p className="text-2xl font-bold">{impactData.totalImpact}</p>
                      </div>
                      <AlertCircle className="h-8 w-8 text-orange-500" />
                    </div>
                  </CardBody>
                </Card>
                <Card>
                  <CardBody>
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-muted-foreground">Impacto Alto</p>
                        <p className="text-2xl font-bold text-red-600">
                          {impactData.affectedDatasets.filter(d => d.impact === "HIGH").length}
                        </p>
                      </div>
                      <XCircle className="h-8 w-8 text-red-500" />
                    </div>
                  </CardBody>
                </Card>
                <Card>
                  <CardBody>
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-muted-foreground">Orígenes Relacionados</p>
                        <p className="text-2xl font-bold">{impactData.affectedOrigins.length}</p>
                      </div>
                      <Network className="h-8 w-8 text-blue-500" />
                    </div>
                  </CardBody>
                </Card>
              </div>

              <div className="space-y-4">
                <div>
                  <h3 className="text-lg font-semibold mb-3">Datasets Afectados</h3>
                  <div className="space-y-3">
                    {impactData.affectedDatasets.map((dataset) => (
                      <Card key={dataset.id} className="border-l-4 border-l-orange-500">
                        <CardBody>
                          <div className="flex items-start justify-between">
                            <div className="flex-1">
                              <div className="flex items-center gap-2 mb-2">
                                <h4 className="font-semibold">{dataset.name}</h4>
                                <Badge className={impactColors[dataset.impact]}>
                                  Impacto {dataset.impact}
                                </Badge>
                              </div>
                              <p className="text-sm text-muted-foreground">{dataset.reason}</p>
                            </div>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => router.push(`/governance/data/datasets/${dataset.id}`)}
                            >
                              Ver Dataset
                            </Button>
                          </div>
                        </CardBody>
                      </Card>
                    ))}
                  </div>
                </div>

                <div>
                  <h3 className="text-lg font-semibold mb-3">Orígenes de Datos Relacionados</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    {impactData.affectedOrigins.map((origin) => (
                      <Card key={origin.id} className="border-l-4 border-l-blue-500">
                        <CardBody>
                          <div className="flex items-center justify-between">
                            <div>
                              <h4 className="font-semibold">{origin.name}</h4>
                              <Badge variant="outline" className="mt-1">{origin.type}</Badge>
                            </div>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => router.push(`/governance/data/origins/${origin.id}`)}
                            >
                              Ver Origen
                            </Button>
                          </div>
                        </CardBody>
                      </Card>
                    ))}
                  </div>
                </div>
              </div>
            </>
          ) : (
            <div className="text-center py-8">
              <p className="text-muted-foreground">No hay datos de impacto disponibles</p>
            </div>
          )}
        </CardBody>
      </Card>
    </div>
  );
}
