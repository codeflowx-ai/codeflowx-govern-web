"use client";
import { useTranslation } from "@/app/config/i18n";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Bot, ArrowLeft, Upload } from "lucide-react";
import { useState } from "react";
import AgentTestExecution from "../components/AgentTestExecution";

const TEST_TYPES = [
  {
    value: "BEHAVIOR_TEST",
    label: "Test de Comportamiento",
    description: "Evalúa el comportamiento del agente en diferentes escenarios",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "AB_TEST",
    label: "Prueba A/B",
    description: "Compara dos agentes en los mismos escenarios",
    supportsAB: true,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "COMPLIANCE_TEST",
    label: "Test de Cumplimiento",
    description: "Verifica cumplimiento regulatorio del agente",
    supportsAB: false,
    requiresFile: true,
    supportsJudge: false,
  },
  {
    value: "PERFORMANCE_TEST",
    label: "Test de Rendimiento",
    description: "Evalúa el rendimiento del agente",
    supportsAB: false,
    requiresFile: false,
    supportsJudge: false,
  },
];

export default function AgentTestPage() {
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
            <Bot className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.execute.agent.title", "Ejecutar Test de Agente")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.testing.execute.agent.subtitle", "Ejecuta tests y validaciones para agentes de IA")}
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
            <AgentTestExecution
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
