"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  ArrowLeft,
  FileUp,
  Play,
  AlertTriangle,
  Lightbulb,
  Loader2,
} from "lucide-react";
import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface Model {
  id: number;
  modelName: string;
  modelId: string;
  type: string;
}

export default function NewRunPage() {
  const { t } = useTranslation();
  const params = useParams();
  const router = useRouter();
  const modelId = params.id as string;

  const [model, setModel] = useState<Model | null>(null);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [activeTab, setActiveTab] = useState<"BIAS" | "EXPLAINABILITY">("BIAS");

  // Form state para bias analysis
  const [biasForm, setBiasForm] = useState({
    file: null as File | null,
    protectedAttribute: "",
    favorableOutcome: "1",
    threshold: "0.8",
  });

  // Form state para explainability
  const [explainForm, setExplainForm] = useState({
    file: null as File | null,
    method: "shap",
    predictionColumn: "prediction",
  });

  const loadModel = async () => {
    try {
      setLoading(true);
      // TODO: Reemplazar con llamada API real
      // const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL || ''}/api/v1/models/${modelId}`);
      // const data = await response.json();

      // Mock data
      setModel({
        id: parseInt(modelId),
        modelName: "GPT-4 Fine-tuned Classification",
        modelId: "gpt-4-ft-001",
        type: "TEXT",
      });
    } catch (error) {
      console.error("Error loading model:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadModel();
  }, [modelId]);

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>, type: "BIAS" | "EXPLAINABILITY") => {
    const file = e.target.files?.[0];
    if (file && file.name.endsWith(".csv")) {
      if (type === "BIAS") {
        setBiasForm({ ...biasForm, file });
      } else {
        setExplainForm({ ...explainForm, file });
      }
    } else {
      alert("Por favor selecciona un archivo CSV");
    }
  };

  const handleExecuteBiasAnalysis = async () => {
    if (!biasForm.file || !biasForm.protectedAttribute) {
      alert("Por favor completa todos los campos requeridos");
      return;
    }

    try {
      setUploading(true);

      const formData = new FormData();
      formData.append("file", biasForm.file);
      formData.append("protectedAttribute", biasForm.protectedAttribute);
      formData.append("favorableOutcome", biasForm.favorableOutcome);
      formData.append("threshold", biasForm.threshold);

      const response = await fetch(
        `/api/v1/bff/compliance/models/${modelId}/bias-analysis/execute`,
        {
          method: "POST",
          body: formData,
        }
      );

      if (response.ok) {
        const result = await response.json();
        alert("Análisis de sesgo ejecutado exitosamente");
        router.push(`/models/registry/${modelId}/testing`);
      } else {
        const error = await response.json();
        alert(`Error: ${error.message || "Error ejecutando análisis"}`);
      }
    } catch (error) {
      console.error("Error executing bias analysis:", error);
      alert("Error ejecutando análisis de sesgo");
    } finally {
      setUploading(false);
    }
  };

  const handleExecuteExplainability = async () => {
    if (!explainForm.file) {
      alert("Por favor selecciona un archivo CSV");
      return;
    }

    try {
      setUploading(true);

      const formData = new FormData();
      formData.append("file", explainForm.file);
      formData.append("method", explainForm.method);
      formData.append("predictionColumn", explainForm.predictionColumn);

      const response = await fetch(
        `/api/v1/bff/compliance/models/${modelId}/explainability/execute`,
        {
          method: "POST",
          body: formData,
        }
      );

      if (response.ok) {
        const result = await response.json();
        alert("Análisis de explicabilidad ejecutado exitosamente");
        router.push(`/models/registry/${modelId}/testing`);
      } else {
        const error = await response.json();
        alert(`Error: ${error.message || "Error ejecutando análisis"}`);
      }
    } catch (error) {
      console.error("Error executing explainability:", error);
      alert("Error ejecutando análisis de explicabilidad");
    } finally {
      setUploading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="w-8 h-8 animate-spin" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <Link href={`/models/registry/${modelId}/testing`}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4 mr-2" />
                Volver
              </Button>
            </Link>
            <Play className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.models.testing.newRun.title", "Nuevo Run")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {model?.modelName} - {t("governance.models.testing.newRun.subtitle", "Ejecutar análisis de sesgo o explicabilidad")}
          </p>
        </div>
      </div>

      {/* Tabs */}
      <Tabs value={activeTab} onValueChange={(v) => setActiveTab(v as "BIAS" | "EXPLAINABILITY")} defaultValue="BIAS" className="w-full">
        <TabsList className="grid w-full grid-cols-2 max-w-md">
          <TabsTrigger value="BIAS" className="flex items-center justify-center gap-2">
            <AlertTriangle className="w-4 h-4" />
            Bias Analysis
          </TabsTrigger>
          <TabsTrigger value="EXPLAINABILITY" className="flex items-center justify-center gap-2">
            <Lightbulb className="w-4 h-4" />
            Explainability
          </TabsTrigger>
        </TabsList>

        {/* Tab: Bias Analysis */}
        <TabsContent value="BIAS" className="space-y-6 mt-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <AlertTriangle className="w-5 h-5" />
                {t("governance.models.testing.newRun.bias.title", "Análisis de Sesgo")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div>
                <label className="text-sm font-medium mb-2 block">
                  {t("governance.models.testing.newRun.bias.csvFile", "Archivo CSV")} *
                </label>
                <div className="flex items-center gap-2">
                  <Input
                    type="file"
                    accept=".csv"
                    onChange={(e) => handleFileSelect(e, "BIAS")}
                    className="flex-1"
                  />
                  {biasForm.file && (
                    <Badge variant="outline" className="flex items-center gap-2">
                      <FileUp className="w-3 h-3" />
                      {biasForm.file.name}
                    </Badge>
                  )}
                </div>
                <p className="text-xs text-muted-foreground mt-2">
                  {t("governance.models.testing.newRun.bias.csvFormat", "Formato: y_true,y_pred,protected_attribute")}
                </p>
              </div>

              <div>
                <label className="text-sm font-medium mb-2 block">
                  {t("governance.models.testing.newRun.bias.protectedAttribute", "Atributo Protegido")} *
                </label>
                <Input
                  value={biasForm.protectedAttribute}
                  onChange={(e) => setBiasForm({ ...biasForm, protectedAttribute: e.target.value })}
                  placeholder="gender, race, age, etc."
                  className="max-w-md"
                />
                <p className="text-xs text-muted-foreground mt-1">
                  {t("governance.models.testing.newRun.bias.protectedAttributeHelp", "Nombre de la columna que contiene el atributo protegido")}
                </p>
              </div>

              <div className="grid grid-cols-2 gap-4 max-w-md">
                <div>
                  <label className="text-sm font-medium mb-2 block">
                    {t("governance.models.testing.newRun.bias.favorableOutcome", "Resultado Favorable")}
                  </label>
                  <Input
                    value={biasForm.favorableOutcome}
                    onChange={(e) => setBiasForm({ ...biasForm, favorableOutcome: e.target.value })}
                    placeholder="1"
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.models.testing.newRun.bias.favorableOutcomeHelp", "Valor considerado como resultado favorable (por defecto: 1)")}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium mb-2 block">
                    {t("governance.models.testing.newRun.bias.threshold", "Umbral")}
                  </label>
                  <Input
                    type="number"
                    step="0.1"
                    min="0"
                    max="1"
                    value={biasForm.threshold}
                    onChange={(e) => setBiasForm({ ...biasForm, threshold: e.target.value })}
                    placeholder="0.8"
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.models.testing.newRun.bias.thresholdHelp", "Umbral para clasificación (por defecto: 0.8)")}
                  </p>
                </div>
              </div>

              <div className="flex gap-4 pt-4">
                <Button
                  onClick={handleExecuteBiasAnalysis}
                  disabled={uploading || !biasForm.file || !biasForm.protectedAttribute}
                  size="lg"
                >
                  {uploading ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      {t("common.executing", "Ejecutando...")}
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4 mr-2" />
                      {t("governance.models.testing.newRun.execute", "Ejecutar Análisis")}
                    </>
                  )}
                </Button>
                <Link href={`/models/registry/${modelId}/testing`}>
                  <Button variant="outline" size="lg">
                    {t("common.cancel", "Cancelar")}
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Tab: Explainability */}
        <TabsContent value="EXPLAINABILITY" className="space-y-6 mt-6">
          <Card className="border-2">
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Lightbulb className="w-5 h-5" />
                {t("governance.models.testing.newRun.explainability.title", "Análisis de Explicabilidad")}
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div>
                <label className="text-sm font-medium mb-2 block">
                  {t("governance.models.testing.newRun.explainability.csvFile", "Archivo CSV")} *
                </label>
                <div className="flex items-center gap-2">
                  <Input
                    type="file"
                    accept=".csv"
                    onChange={(e) => handleFileSelect(e, "EXPLAINABILITY")}
                    className="flex-1"
                  />
                  {explainForm.file && (
                    <Badge variant="outline" className="flex items-center gap-2">
                      <FileUp className="w-3 h-3" />
                      {explainForm.file.name}
                    </Badge>
                  )}
                </div>
                <p className="text-xs text-muted-foreground mt-2">
                  {t("governance.models.testing.newRun.explainability.csvFormat", "Formato: características del modelo y columna de predicciones")}
                </p>
              </div>

              <div className="grid grid-cols-2 gap-4 max-w-md">
                <div>
                  <label className="text-sm font-medium mb-2 block">
                    {t("governance.models.testing.newRun.explainability.method", "Método")}
                  </label>
                  <Select
                    value={explainForm.method}
                    onValueChange={(value) => setExplainForm({ ...explainForm, method: value })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="shap">SHAP</SelectItem>
                      <SelectItem value="lime">LIME</SelectItem>
                      <SelectItem value="integrated_gradients">Integrated Gradients</SelectItem>
                    </SelectContent>
                  </Select>
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.models.testing.newRun.explainability.methodHelp", "Método de explicabilidad a utilizar")}
                  </p>
                </div>
                <div>
                  <label className="text-sm font-medium mb-2 block">
                    {t("governance.models.testing.newRun.explainability.predictionColumn", "Columna de Predicciones")}
                  </label>
                  <Input
                    value={explainForm.predictionColumn}
                    onChange={(e) => setExplainForm({ ...explainForm, predictionColumn: e.target.value })}
                    placeholder="prediction"
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    {t("governance.models.testing.newRun.explainability.predictionColumnHelp", "Nombre de la columna que contiene las predicciones (por defecto: prediction)")}
                  </p>
                </div>
              </div>

              <div className="flex gap-4 pt-4">
                <Button
                  onClick={handleExecuteExplainability}
                  disabled={uploading || !explainForm.file}
                  size="lg"
                >
                  {uploading ? (
                    <>
                      <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      {t("common.executing", "Ejecutando...")}
                    </>
                  ) : (
                    <>
                      <Play className="w-4 h-4 mr-2" />
                      {t("governance.models.testing.newRun.execute", "Ejecutar Análisis")}
                    </>
                  )}
                </Button>
                <Link href={`/models/registry/${modelId}/testing`}>
                  <Button variant="outline" size="lg">
                    {t("common.cancel", "Cancelar")}
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
}
