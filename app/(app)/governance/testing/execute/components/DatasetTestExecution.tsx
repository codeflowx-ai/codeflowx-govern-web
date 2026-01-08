"use client";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";
import { Card, CardBody } from "@/components/ui/card";
import { Upload, Play, FileText, AlertCircle } from "lucide-react";
import { useState } from "react";

interface TestType {
  value: string;
  label: string;
  description: string;
  supportsAB: boolean;
  requiresFile: boolean;
  supportsJudge: boolean;
}

interface DatasetTestExecutionProps {
  testType: string;
  testTypeInfo?: TestType;
  onExecute: (runId: number) => void;
}

export default function DatasetTestExecution({
  testType,
  testTypeInfo,
  onExecute,
}: DatasetTestExecutionProps) {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    experimentId: "",
    experimentName: "",
    datasetId: "",
    name: "",
    description: "",
    // QUALITY_CHECK specific
    qualityThreshold: "0.8",
    // VALIDATION specific
    schemaValidation: true,
    // File upload
    file: null as File | null,
  });

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (!file.name.endsWith(".csv")) {
        alert(t("governance.testing.execute.fileMustBeCSV", "El archivo debe ser un CSV"));
        return;
      }
      setFormData({ ...formData, file });
    }
  };

  const handleSubmit = async () => {
    // Validation
    if (!formData.experimentName && !formData.experimentId) {
      alert(t("governance.testing.execute.experimentRequired", "Debe especificar un experimento"));
      return;
    }
    if (!formData.datasetId) {
      alert(t("governance.testing.execute.datasetRequired", "Debe seleccionar un dataset"));
      return;
    }
    if (testTypeInfo?.requiresFile && !formData.file) {
      alert(t("governance.testing.execute.fileRequired", "Debe subir un archivo CSV"));
      return;
    }
    if (!formData.name.trim()) {
      alert(t("governance.testing.execute.nameRequired", "El nombre del run es requerido"));
      return;
    }

    setLoading(true);
    try {
      // TODO: Replace with actual API call
      // Mock execution
      const mockRunId = Math.floor(Math.random() * 1000);
      alert(t("governance.testing.execute.executionStarted", "Test iniciado correctamente"));
      setTimeout(() => {
        onExecute(mockRunId);
      }, 1000);
    } catch (error) {
      console.error("Error executing test:", error);
      alert(t("governance.testing.execute.executionError", "Error al ejecutar el test"));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Experiment Selection */}
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.experimentName", "Nombre del Experimento")}</Label>
          <Input
            placeholder={t("governance.testing.execute.experimentNamePlaceholder", "Nuevo experimento o existente")}
            value={formData.experimentName}
            onChange={(e) => setFormData({ ...formData, experimentName: e.target.value })}
          />
        </div>
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.experimentId", "ID Experimento Existente (opcional)")}</Label>
          <Input
            type="number"
            placeholder={t("governance.testing.execute.experimentIdPlaceholder", "Dejar vacío para crear nuevo")}
            value={formData.experimentId}
            onChange={(e) => setFormData({ ...formData, experimentId: e.target.value })}
          />
        </div>
      </div>

      {/* Dataset Selection */}
      <div className="space-y-2">
        <Label>{t("governance.testing.execute.dataset", "Dataset")} *</Label>
        <Input
          type="number"
          placeholder={t("governance.testing.execute.datasetIdPlaceholder", "ID del dataset")}
          value={formData.datasetId}
          onChange={(e) => setFormData({ ...formData, datasetId: e.target.value })}
          required
        />
      </div>

      {/* Run Info */}
      <div className="space-y-4">
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.runName", "Nombre del Run")} *</Label>
          <Input
            placeholder={t("governance.testing.execute.runNamePlaceholder", "Ej: Quality Check - Dataset v1")}
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
          />
        </div>
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.runDescription", "Descripción")}</Label>
          <Textarea
            placeholder={t("governance.testing.execute.runDescriptionPlaceholder", "Descripción del test...")}
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            rows={3}
          />
        </div>
      </div>

      {/* Test-specific parameters */}
      {testType === "QUALITY_CHECK" && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold">{t("governance.testing.execute.qualityParameters", "Parámetros de Calidad")}</h3>
            <div className="space-y-2">
              <Label>{t("governance.testing.execute.qualityThreshold", "Umbral de Calidad")}</Label>
              <Input
                type="number"
                step="0.1"
                placeholder="0.8"
                value={formData.qualityThreshold}
                onChange={(e) => setFormData({ ...formData, qualityThreshold: e.target.value })}
              />
            </div>
          </CardBody>
        </Card>
      )}

      {testType === "VALIDATION" && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold">{t("governance.testing.execute.validationParameters", "Parámetros de Validación")}</h3>
            <div className="flex items-center gap-2">
              <input
                type="checkbox"
                id="schemaValidation"
                checked={formData.schemaValidation}
                onChange={(e) => setFormData({ ...formData, schemaValidation: e.target.checked })}
                className="w-4 h-4"
              />
              <Label htmlFor="schemaValidation" className="cursor-pointer">
                {t("governance.testing.execute.schemaValidation", "Validar esquema del dataset")}
              </Label>
            </div>
          </CardBody>
        </Card>
      )}

      {/* File Upload */}
      {testTypeInfo?.requiresFile && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold flex items-center gap-2">
              <Upload className="w-4 h-4" />
              {t("governance.testing.execute.uploadFile", "Subir Archivo CSV")}
            </h3>
            <div className="space-y-2">
              <Label>{t("governance.testing.execute.file", "Archivo CSV")} *</Label>
              <div className="flex items-center gap-2">
                <Input
                  type="file"
                  accept=".csv"
                  onChange={handleFileChange}
                  className="hidden"
                  id="file-upload"
                />
                <label
                  htmlFor="file-upload"
                  className="flex items-center gap-2 px-4 py-2 border rounded-lg cursor-pointer hover:bg-muted"
                >
                  <Upload className="w-4 h-4" />
                  {formData.file ? formData.file.name : t("governance.testing.execute.selectFile", "Seleccionar archivo")}
                </label>
              </div>
              {formData.file && (
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <FileText className="w-4 h-4" />
                  {formData.file.name} ({(formData.file.size / 1024).toFixed(2)} KB)
                </div>
              )}
            </div>
            <div className="flex items-start gap-2 p-3 bg-blue-50 rounded-lg text-sm text-blue-800">
              <AlertCircle className="w-4 h-4 mt-0.5 flex-shrink-0" />
              <div>
                <p className="font-medium mb-1">{t("governance.testing.execute.fileFormat", "Formato del archivo CSV:")}</p>
                <ul className="list-disc list-inside space-y-1 text-xs">
                  <li>
                    {t("governance.testing.execute.datasetFileFormat", "Debe contener el dataset completo con todas las columnas y filas a validar")}
                  </li>
                </ul>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Submit Button */}
      <div className="flex justify-end">
        <Button onClick={handleSubmit} disabled={loading} className="bg-primary hover:bg-primary/90">
          <Play className="w-4 h-4 mr-2" />
          {loading
            ? t("governance.testing.execute.executing", "Ejecutando...")
            : t("governance.testing.execute.execute", "Ejecutar Test")}
        </Button>
      </div>
    </div>
  );
}
