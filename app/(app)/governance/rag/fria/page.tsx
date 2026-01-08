"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  FileText,
  ArrowLeft,
  Info,
  AlertTriangle,
  CheckCircle,
} from "lucide-react";

/**
 * Página de FRIA para Proyectos RAG
 *
 * Esta página reutiliza el wizard FRIA existente pero está
 * específicamente adaptada para proyectos RAG.
 *
 * El wizard FRIA se realiza mediante la página general de FRIA
 * filtrando por proyectos con PRJISTYPE = "RAG"
 */
export default function RAGFriaPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const projectId = searchParams.get("projectId");

  // Redirigir a la página de FRIA general con filtro para RAG solo si hay projectId
  useEffect(() => {
    if (projectId) {
      // Redirigir a FRIA general con el projectId y filtro RAG
      router.push(`/governance/compliance/fria?projectId=${projectId}&projectType=RAG`);
    }
  }, [projectId, router]);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => router.push("/governance/rag/registry")}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {t("common.back", "Volver")}
          </Button>
          <div className="flex items-center gap-3">
            <FileText className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.rag.fria.title", "FRIA para Proyectos RAG")}
            </h1>
          </div>
        </div>
      </div>

      {/* Info Alert */}
      <Alert>
        <Info className="h-4 w-4" />
        <AlertDescription>
          {t(
            "governance.rag.fria.info",
            "La evaluación FRIA para proyectos RAG utiliza el mismo wizard que otros sistemas de IA. Serás redirigido al wizard FRIA general adaptado para proyectos RAG."
          )}
        </AlertDescription>
      </Alert>

      {/* Card de información */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <FileText className="h-5 w-5 text-primary" />
            {t("governance.rag.fria.about.title", "Acerca de FRIA para RAG")}
          </CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <p className="text-sm text-muted-foreground">
            {t(
              "governance.rag.fria.about.description",
              "La evaluación FRIA (Fundamental Rights Impact Assessment) según el Art. 27 del EU AI Act es obligatoria para proyectos RAG clasificados como de alto riesgo. El wizard consta de 6 pasos que evalúan el impacto en derechos fundamentales."
            )}
          </p>

          <div className="space-y-2">
            <h4 className="font-medium text-foreground">
              {t("governance.rag.fria.about.risks.title", "Riesgos Específicos RAG:")}
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground ml-4">
              <li>
                {t(
                  "governance.rag.fria.about.risks.embeddingBias",
                  "Sesgo en modelos de embedding"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.risks.chunkQuality",
                  "Calidad y veracidad de chunks"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.risks.piiProtection",
                  "Protección de datos personales en chunks"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.risks.dataTransparency",
                  "Transparencia de fuentes de datos"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.risks.accuracy",
                  "Exactitud de información recuperada"
                )}
              </li>
            </ul>
          </div>

          <div className="space-y-2">
            <h4 className="font-medium text-foreground">
              {t("governance.rag.fria.about.steps.title", "Pasos del Wizard FRIA (Art. 27.1):")}
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground ml-4">
              <li>
                {t(
                  "governance.rag.fria.about.steps.process",
                  "a) Descripción de procesos RAG"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.steps.usage",
                  "b) Período y frecuencia de uso"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.steps.categories",
                  "c) Categorías de personas afectadas"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.steps.risks",
                  "d) Riesgos específicos (sesgo, calidad, PII, etc.)"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.steps.oversight",
                  "e) Medidas de supervisión humana (HITL)"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.fria.about.steps.mitigation",
                  "f) Medidas de mitigación"
                )}
              </li>
            </ul>
          </div>

          <div className="pt-4 border-t border-border">
            <Button
              onClick={() => {
                if (projectId) {
                  router.push(`/governance/compliance/fria?projectId=${projectId}&projectType=RAG`);
                } else {
                  router.push("/governance/compliance/fria/projects?projectType=RAG");
                }
              }}
              className="w-full"
            >
              {t("governance.rag.fria.goToFria", "Ir a Wizard FRIA")}
              <ArrowLeft className="h-4 w-4 ml-2 rotate-180" />
            </Button>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
