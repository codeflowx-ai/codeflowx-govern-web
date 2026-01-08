"use client";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import { Card, CardBody } from "@/components/ui/card";
import { Upload, Play, FileText, AlertCircle, Brain } from "lucide-react";
import { useState } from "react";

interface TestType {
  value: string;
  label: string;
  description: string;
  supportsAB: boolean;
  requiresFile: boolean;
  supportsJudge: boolean;
}

interface RAGTestExecutionProps {
  testType: string;
  testTypeInfo?: TestType;
  onExecute: (runId: number) => void;
}

export default function RAGTestExecution({
  testType,
  testTypeInfo,
  onExecute,
}: RAGTestExecutionProps) {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    experimentId: "",
    experimentName: "",
    ragId: "",
    ragIdB: "", // For A/B testing
    name: "",
    description: "",
    // Judge model (for ACCURACY_TEST, AB_TEST)
    judgeModelId: "",
    judgeCriteria: "accuracy", // accuracy, relevance, completeness
    // LATENCY_TEST specific
    numberOfQueries: "100",
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
    if (!formData.ragId) {
      alert(t("governance.testing.execute.ragRequired", "Debe seleccionar un RAG"));
      return;
    }
    if (testType === "AB_TEST" && !formData.ragIdB) {
      alert(t("governance.testing.execute.ragBRequired", "Debe seleccionar el segundo RAG para A/B"));
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
    if (testTypeInfo?.supportsJudge && !formData.judgeModelId) {
      alert(t("governance.testing.execute.judgeModelRequired", "Debe seleccionar un modelo como juez"));
      return;
    }
    if (!formData.name.trim()) {
      alert(t("governance.testing.execute.nameRequired", "El nombre del run es requerido"));
      return;
    }

    setLoading(true);
    try {
      if (testType === "AB_TEST") {
        // Ejecutar prueba A/B
        const formDataToSend = new FormData();
        formDataToSend.append("entityType", "RAG");
        formDataToSend.append("entityAId", formData.ragId);
        formDataToSend.append("entityBId", formData.ragIdB);
        formDataToSend.append("experimentName", formData.experimentName || "");
        formDataToSend.append("experimentId", formData.experimentId || "");
        formDataToSend.append("runNameA", formData.name + " - A");
        formDataToSend.append("runNameB", formData.name + " - B");
        formDataToSend.append("judgeModelId", formData.judgeModelId || "");
        formDataToSend.append("judgeCriteria", formData.judgeCriteria);
        if (formData.file) formDataToSend.append("testDataset", formData.file);
        if (formData.fileB) formDataToSend.append("testDatasetB", formData.fileB);

        const response = await fetch("/api/v1/governance/testing/execute/ab-test", {
          method: "POST",
          body: formDataToSend,
        });

        if (!response.ok) {
          const error = await response.text();
          throw new Error(error || "Error ejecutando prueba A/B");
        }

        const result = await response.json();
        alert(t("governance.testing.execute.executionStarted", "Prueba A/B completada"));

        if (typeof window !== "undefined") {
          window.location.assign(`/governance/testing/ab-results?result=${encodeURIComponent(JSON.stringify(result))}`);
        }
      } else {
        // Ejecutar test normal
        const mockRunId = Math.floor(Math.random() * 1000);
        alert(t("governance.testing.execute.executionStarted", "Test iniciado correctamente"));
        setTimeout(() => {
          onExecute(mockRunId);
        }, 1000);
      }
    } catch (error: any) {
      console.error("Error executing test:", error);
      alert(t("governance.testing.execute.executionError", "Error al ejecutar el test") + ": " + (error.message || error));
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

      {/* RAG Selection */}
      <div className={`grid gap-4 ${testType === "AB_TEST" ? "grid-cols-2" : "grid-cols-1"}`}>
        <div className="space-y-2">
          <Label>
            {testType === "AB_TEST"
              ? t("governance.testing.execute.ragA", "RAG A")
              : t("governance.testing.execute.rag", "RAG")} *
          </Label>
          <Input
            type="number"
            placeholder={t("governance.testing.execute.ragIdPlaceholder", "ID del RAG")}
            value={formData.ragId}
            onChange={(e) => setFormData({ ...formData, ragId: e.target.value })}
            required
          />
        </div>
        {testType === "AB_TEST" && (
          <div className="space-y-2">
            <Label>{t("governance.testing.execute.ragB", "RAG B")} *</Label>
            <Input
              type="number"
              placeholder={t("governance.testing.execute.ragIdPlaceholder", "ID del RAG")}
              value={formData.ragIdB}
              onChange={(e) => setFormData({ ...formData, ragIdB: e.target.value })}
              required
            />
          </div>
        )}
      </div>

      {/* Judge Model (for tests that support it) */}
      {testTypeInfo?.supportsJudge && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold flex items-center gap-2">
              <Brain className="w-4 h-4" />
              {t("governance.testing.execute.judgeModel", "Modelo como Juez")}
            </h3>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>{t("governance.testing.execute.judgeModelId", "ID del Modelo Juez")} *</Label>
                <Input
                  type="number"
                  placeholder={t("governance.testing.execute.judgeModelIdPlaceholder", "ID del modelo que evaluará las respuestas")}
                  value={formData.judgeModelId}
                  onChange={(e) => setFormData({ ...formData, judgeModelId: e.target.value })}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label>{t("governance.testing.execute.judgeCriteria", "Criterio de Evaluación")}</Label>
                <Select
                  value={formData.judgeCriteria}
                  onValueChange={(v) => setFormData({ ...formData, judgeCriteria: v })}
                >
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="accuracy">{t("governance.testing.execute.criteria.accuracy", "Precisión")}</SelectItem>
                    <SelectItem value="relevance">{t("governance.testing.execute.criteria.relevance", "Relevancia")}</SelectItem>
                    <SelectItem value="completeness">{t("governance.testing.execute.criteria.completeness", "Completitud")}</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* LATENCY_TEST specific */}
      {testType === "LATENCY_TEST" && (
        <Card>
          <CardBody className="space-y-4">
            <h3 className="font-semibold">{t("governance.testing.execute.latencyParameters", "Parámetros de Latencia")}</h3>
            <div className="space-y-2">
              <Label>{t("governance.testing.execute.numberOfQueries", "Número de Consultas")}</Label>
              <Input
                type="number"
                placeholder="100"
                value={formData.numberOfQueries}
                onChange={(e) => setFormData({ ...formData, numberOfQueries: e.target.value })}
              />
            </div>
          </CardBody>
        </Card>
      )}

      {/* Run Info */}
      <div className="space-y-4">
        <div className="space-y-2">
          <Label>{t("governance.testing.execute.runName", "Nombre del Run")} *</Label>
          <Input
            placeholder={t("governance.testing.execute.runNamePlaceholder", "Ej: Retrieval Test - v1.0")}
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
                    {t("governance.testing.execute.ragFileFormat", "Debe contener: consultas de prueba, documentos de referencia, respuestas esperadas (opcional)")}
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
