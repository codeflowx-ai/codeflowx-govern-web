"use client";
import React from "react";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { FlaskConical, Brain, FileText, Bot, Search, Database, ArrowRight } from "lucide-react";

type EntityType = "MODEL" | "PROMPT" | "AGENT" | "RAG" | "DATASET";

interface EntityOption {
  type: EntityType;
  name: string;
  description: string;
  icon: any;
  href: string;
  testCount: number;
}

const ENTITY_OPTIONS: EntityOption[] = [
  {
    type: "MODEL",
    name: "Modelo",
    description: "Ejecuta tests de sesgo, explicabilidad, rendimiento y pruebas A/B para modelos de IA",
    icon: Brain,
    href: "/governance/testing/execute/model",
    testCount: 4,
  },
  {
    type: "PROMPT",
    name: "Prompt",
    description: "Evalúa prompts con tests de calidad, cumplimiento, pruebas A/B y modelo como juez",
    icon: FileText,
    href: "/governance/testing/execute/prompt",
    testCount: 4,
  },
  {
    type: "AGENT",
    name: "Agente",
    description: "Prueba el comportamiento, cumplimiento y rendimiento de agentes de IA",
    icon: Bot,
    href: "/governance/testing/execute/agent",
    testCount: 4,
  },
  {
    type: "RAG",
    name: "RAG",
    description: "Evalúa sistemas RAG con tests de recuperación, precisión, latencia y pruebas A/B",
    icon: Search,
    href: "/governance/testing/execute/rag",
    testCount: 4,
  },
  {
    type: "DATASET",
    name: "Dataset",
    description: "Valida la calidad, estructura y cumplimiento regulatorio de datasets",
    icon: Database,
    href: "/governance/testing/execute/dataset",
    testCount: 3,
  },
];

export default function ExecuteTestPage() {
  const { t } = useTranslation();

  const handleEntityClick = (href: string, e?: React.MouseEvent) => {
    if (e) {
      e.preventDefault();
      e.stopPropagation();
    }
    console.log("Navigating to:", href);
    if (typeof window !== "undefined") {
      window.location.assign(href);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <FlaskConical className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.testing.execute.title", "Ejecutar Test")}
            </h1>
          </div>
          <p className="text-sm text-muted-foreground ml-9">
            {t("governance.testing.execute.subtitle", "Selecciona el tipo de entidad para ejecutar tests y validaciones")}
          </p>
        </div>
      </div>

      {/* Entity Selection Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {ENTITY_OPTIONS.map((entity) => {
          const IconComponent = entity.icon;
          return (
            <div
              key={entity.type}
              className="cursor-pointer"
              onClick={(e) => handleEntityClick(entity.href, e)}
            >
            <Card
              className="transition-all hover:border-primary hover:shadow-lg group"
            >
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="p-2 bg-primary/10 rounded-lg group-hover:bg-primary/20 transition-colors">
                      <IconComponent className="w-6 h-6 text-primary" />
                    </div>
                    <CardTitle className="text-lg">{entity.name}</CardTitle>
                  </div>
                  <ArrowRight className="w-5 h-5 text-muted-foreground group-hover:text-primary transition-colors" />
                </div>
              </CardHeader>
              <CardBody>
                <p className="text-sm text-muted-foreground mb-4">{entity.description}</p>
                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                  <span className="px-2 py-1 bg-muted rounded">
                    {entity.testCount} {t("governance.testing.execute.testTypes", "tipos de test")}
                  </span>
                </div>
              </CardBody>
            </Card>
            </div>
          );
        })}
      </div>

      {/* Quick Info */}
      <Card>
        <CardHeader>
          <CardTitle>{t("governance.testing.execute.info.title", "Información")}</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-2 text-sm text-muted-foreground">
            <p>
              {t(
                "governance.testing.execute.info.description",
                "Cada tipo de entidad tiene tests específicos diseñados para evaluar diferentes aspectos:"
              )}
            </p>
            <ul className="list-disc list-inside space-y-1 ml-4">
              <li>
                {t(
                  "governance.testing.execute.info.models",
                  "Modelos: Análisis de sesgo, explicabilidad, rendimiento y comparación A/B"
                )}
              </li>
              <li>
                {t(
                  "governance.testing.execute.info.prompts",
                  "Prompts: Evaluación de calidad, cumplimiento, pruebas A/B con modelo como juez"
                )}
              </li>
              <li>
                {t(
                  "governance.testing.execute.info.agents",
                  "Agentes: Tests de comportamiento, cumplimiento y rendimiento"
                )}
              </li>
              <li>
                {t(
                  "governance.testing.execute.info.rag",
                  "RAG: Tests de recuperación, precisión, latencia y comparación A/B"
                )}
              </li>
              <li>
                {t(
                  "governance.testing.execute.info.datasets",
                  "Datasets: Validación de calidad, estructura y cumplimiento regulatorio"
                )}
              </li>
            </ul>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
