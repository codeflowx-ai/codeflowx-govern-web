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

interface AgentTestExecutionProps {
  testType: string;
  testTypeInfo?: TestType;
  onExecute: (runId: number) => void;
}

export default function AgentTestExecution({
  testType,
  testTypeInfo,
  onExecute,
}: AgentTestExecutionProps) {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    experimentId: "",
    experimentName: "",
    agentId: "",
    agentIdB: "", // For A/B testing
    name: "",
    description: "",
    // PERFORMANCE_TEST specific
    metricName: "",
    metricValue: "",
    // File upload
    file: null as File | null,
    fileB: null as File | null, // For A/B testing
  });

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>, field: "file" | "fileB" = "file") => {
    const file = e.target.files?.[0];
    if (file) {
      if (!file.name.endsWith(".csv")) {
        alert(t("governance.testing.execute.fileMustBeCSV", "El archivo debe ser un CSV"));
        return;
      }
      setFormData({ ...formData, [field]: file });
    }
  };

  const handleSubmit = async () => {
    // Validation
    if (!formData.experimentName && !formData.experimentId) {
      alert(t("governance.testing.execute.experimentRequired", "Debe especificar un experimento"));
      return;
    }
    if (!formData.agentId) {
      alert(t("governance.testing.execute.agentRequired", "Debe seleccionar un agente"));
      return;
    }
    if (testType === "AB_TEST" && !formData.agentIdB) {
      alert(t("governance.testing.execute.agentBRequired", "Debe seleccionar el segundo agente para A/B"));
      return;
    }
    if (testTypeInfo?.requiresFile && !formData.file) {
      alert(t("governance.testing.execute.fileRequired", "Debe subir un archivo CSV"));
      return;
    }
    if (testType === "AB_TEST" && !formData.fileB) {
      alert(t("governance.testing.execute.fileBRequired", "Debe subir el segundo archivo CSV para A/B"));
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

      {/* Agent Selection */}
      <div className={`grid gap-4 ${testType === "AB_TEST" ? "grid-cols-2" : "grid-cols-1"}`}>
        <div className="space-y-2">
          <Label>
            {testType === "AB_TEST"
              ? t("governance.testing.execute.agentA", "Agente A")
              : t("governance.testing.execute.agent", "Agente")} *
          </Label>
          <Input
            type="number"
            placeholder={t("governance.testing.execute.agentIdPlaceholder", "ID del agente")}
            value={formData.agentId}
            onChange={(e) => setFormData({ ...formData, agentId: e.target.value })}
            required
          />
        </div>
        {testType === "AB_TEST" && (
          <div className="space-y-2">
            <Label>{t("governance.testing.execute.agentB", "Agente B")} *</Label>
            <Input
              type="number"
              placeholder={t("governance.testing.execute.agentIdPlaceholder", "ID del agente")}
              value={formData.agentIdB}
              onChange={(e) => setFormData({ ...formData, agentIdB: e.target.value })}
              required
            />
          </div>
        )}
      </div>

      {/* Run Info */}
      <div className="space-y-4">
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.runName", "Nombre del Run")} *</Label>
          <Input
            placeholder={t("governance.testing.execute.runNamePlaceholder", "Ej: Behavior Test - Scenario 1")}
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

      {/* PERFORMANCE_TEST specific */}
      {testType === "PERFORMANCE_TEST" && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold">{t("governance.testing.execute.performanceParameters", "Parámetros de Rendimiento")}</h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>{t("governance.testing.execute.metricName", "Nombre de Métrica")}</Label>
                <Input
                  placeholder={t("governance.testing.execute.metricNamePlaceholder", "Ej: response_time")}
                  value={formData.metricName}
                  onChange={(e) => setFormData({ ...formData, metricName: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label>{t("governance.testing.execute.metricValue", "Valor")}</Label>
                <Input
                  type="number"
                  step="0.01"
                  placeholder="2.5"
                  value={formData.metricValue}
                  onChange={(e) => setFormData({ ...formData, metricValue: e.target.value })}
                />
              </div>
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
            <div className={`grid gap-4 ${testType === "AB_TEST" ? "grid-cols-2" : "grid-cols-1"}`}>
              <div className="space-y-2">
                <Label>
                  {testType === "AB_TEST"
                    ? t("governance.testing.execute.fileA", "Archivo CSV A")
                    : t("governance.testing.execute.file", "Archivo CSV")} *
                </Label>
                <div className="flex items-center gap-2">
                  <Input
                    type="file"
                    accept=".csv"
                    onChange={(e) => handleFileChange(e, "file")}
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
              {testType === "AB_TEST" && (
                <div className="space-y-2">
                  <Label>{t("governance.testing.execute.fileB", "Archivo CSV B")} *</Label>
                  <div className="flex items-center gap-2">
                    <Input
                      type="file"
                      accept=".csv"
                      onChange={(e) => handleFileChange(e, "fileB")}
                      className="hidden"
                      id="file-upload-b"
                    />
                    <label
                      htmlFor="file-upload-b"
                      className="flex items-center gap-2 px-4 py-2 border rounded-lg cursor-pointer hover:bg-muted"
                    >
                      <Upload className="w-4 h-4" />
                      {formData.fileB ? formData.fileB.name : t("governance.testing.execute.selectFile", "Seleccionar archivo")}
                    </label>
                  </div>
                  {formData.fileB && (
                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                      <FileText className="w-4 h-4" />
                      {formData.fileB.name} ({(formData.fileB.size / 1024).toFixed(2)} KB)
                    </div>
                  )}
                </div>
              )}
            </div>
            <div className="flex items-start gap-2 p-3 bg-blue-50 rounded-lg text-sm text-blue-800">
              <AlertCircle className="w-4 h-4 mt-0.5 flex-shrink-0" />
              <div>
                <p className="font-medium mb-1">{t("governance.testing.execute.fileFormat", "Formato del archivo CSV:")}</p>
                <ul className="list-disc list-inside space-y-1 text-xs">
                  <li>
                    {t("governance.testing.execute.agentFileFormat", "Debe contener: escenarios de prueba, inputs del usuario, contexto, resultados esperados (opcional)")}
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


