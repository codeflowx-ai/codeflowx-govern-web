"use client";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { FileText, ArrowLeft, Upload, Brain } from "lucide-react";
import { useState } from "react";
import PromptTestExecution from "../components/PromptTestExecution";

const TEST_TYPES = [
  {
    value: "PROMPT_TEST",
    label: "Test de Prompt",
    description: "Evalúa un prompt con casos de prueba",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: true,
  },
  {
    value: "AB_TEST",
    label: "Prueba A/B",
    description: "Compara dos prompts en el mismo conjunto de casos",
    supportsAB: true,
    requiresFile: true,
    supportsJudge: true,
  },
  {
    value: "COMPLIANCE_CHECK",
    label: "Verificación de Cumplimiento",
    description: "Verifica cumplimiento regulatorio del prompt",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "QUALITY_ASSESSMENT",
    label: "Evaluación de Calidad",
    description: "Evalúa la calidad general del prompt",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: true,
  },
];

export default function PromptTestPage() {
  const { t } = useTranslation();
  const [selectedTestType, setSelectedTestType] = useState<string>("");

  const handleBack = () => {
    if (typeof window !== "undefined") {
      window.location.assign("/governance/testing/execute");
    }
  };

  const handleExecute = (runId: number) => {
    if (typeof window !== "undefined") {
      window.location.assign(`/governance/testing/runs/${runId}`);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <ArrowLeft
              className="w-5 h-5 cursor-pointer text-muted-foreground hover:text-foreground"
              onClick={handleBack}
            />
            <FileText className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.execute.prompt.title", "Ejecutar Test de Prompt")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.testing.execute.prompt.subtitle", "Ejecuta tests y validaciones para prompts de IA")}
          </p>
        </div>
      </div>

      {/* Test Type Selection */}
      {!selectedTestType ? (
        <Card>
          <CardHeader>
            <CardTitle>
              {t("governance.testing.execute.selectTestType", "Seleccionar Tipo de Test")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {TEST_TYPES.map((test) => (
                <div
                  key={test.value}
                  className="cursor-pointer"
                  onClick={() => setSelectedTestType(test.value)}
                >
                  <Card
                    className={`transition-all hover:border-primary ${
                      selectedTestType === test.value ? "border-primary border-2" : ""
                    }`}
                  >
                    <CardBody className="p-4">
                      <div className="flex items-start justify-between mb-2">
                        <h3 className="font-semibold">{test.label}</h3>
                        {test.supportsAB && (
                          <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">
                            A/B
                          </span>
                        )}
                      </div>
                      <p className="text-sm text-muted-foreground mb-3">{test.description}</p>
                      <div className="flex gap-2 text-xs text-muted-foreground">
                        {test.requiresFile && (
                          <span className="flex items-center gap-1">
                            <Upload className="w-3 h-3" />
                            CSV
                          </span>
                        )}
                        {test.supportsJudge && (
                          <span className="flex items-center gap-1">
                            <Brain className="w-3 h-3" />
                            Juez
                          </span>
                        )}
                      </div>
                    </CardBody>
                  </Card>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      ) : (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle>
                {t("governance.testing.execute.configureTest", "Configurar y Ejecutar Test")}
              </CardTitle>
              <Button
                variant="outline"
                onClick={() => setSelectedTestType("")}
                size="sm"
              >
                {t("common.back", "Volver")}
              </Button>
            </div>
          </CardHeader>
          <CardBody>
            <PromptTestExecution
              testType={selectedTestType}
              testTypeInfo={TEST_TYPES.find((t) => t.value === selectedTestType)}
              onExecute={handleExecute}
            />
          </CardBody>
        </Card>
      )}
    </div>
  );
}
