"use client";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Database, ArrowLeft, Upload } from "lucide-react";
import { useState } from "react";
import DatasetTestExecution from "../components/DatasetTestExecution";

const TEST_TYPES = [
  {
    value: "QUALITY_CHECK",
    label: "Verificación de Calidad",
    description: "Verifica la calidad del dataset",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "VALIDATION",
    label: "Validación",
    description: "Valida la estructura y contenido del dataset",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "COMPLIANCE_CHECK",
    label: "Verificación de Cumplimiento",
    description: "Verifica cumplimiento regulatorio del dataset",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
];

export default function DatasetTestPage() {
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
            <Database className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.execute.dataset.title", "Ejecutar Test de Dataset")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.testing.execute.dataset.subtitle", "Ejecuta tests y validaciones para datasets")}
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
                      </div>
                      <p className="text-sm text-muted-foreground mb-3">{test.description}</p>
                      <div className="flex gap-2 text-xs text-muted-foreground">
                        {test.requiresFile && (
                          <span className="flex items-center gap-1">
                            <Upload className="w-3 h-3" />
                            CSV
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
            <DatasetTestExecution
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
