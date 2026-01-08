"use client";

import { useTranslation } from "@/app/config/i18n";
import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription } from "@/components/ui/alert";
import {
  Shield,
  ArrowLeft,
  Info,
  AlertTriangle,
  CheckCircle,
} from "lucide-react";

/**
 * Página de Clasificación de Proyectos RAG
 *
 * Esta página reutiliza la lógica de clasificación existente pero está
 * específicamente adaptada para proyectos RAG.
 *
 * La clasificación se realiza mediante la página general de clasificación
 * filtrando por proyectos con PRJISTYPE = "RAG"
 */
export default function RAGClassificationPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const searchParams = useSearchParams();
  const projectId = searchParams.get("projectId");

  // Redirigir a la página de clasificación general con filtro para RAG solo si hay projectId
  useEffect(() => {
    if (projectId) {
      // Redirigir a la clasificación general con el projectId y filtro RAG
      router.push(`/governance/compliance/classification?projectId=${projectId}&projectType=RAG`);
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
            <Shield className="w-6 h-6 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">
              {t("governance.rag.classification.title", "Clasificación de Proyectos RAG")}
            </h1>
          </div>
        </div>
      </div>

      {/* Info Alert */}
      <Alert>
        <Info className="h-4 w-4" />
        <AlertDescription>
          {t(
            "governance.rag.classification.info",
            "La clasificación de proyectos RAG utiliza el mismo sistema de clasificación que otros sistemas de IA. Serás redirigido a la página de clasificación general."
          )}
        </AlertDescription>
      </Alert>

      {/* Card de información */}
      <Card className="backdrop-blur-md bg-background/60 border-border/50">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Shield className="h-5 w-5 text-primary" />
            {t("governance.rag.classification.about.title", "Acerca de la Clasificación RAG")}
          </CardTitle>
        </CardHeader>
        <CardBody className="space-y-4">
          <p className="text-sm text-muted-foreground">
            {t(
              "governance.rag.classification.about.description",
              "Los proyectos RAG deben ser clasificados según el Art. 6 y Anexo III del EU AI Act, igual que otros sistemas de IA. La clasificación determina si un sistema RAG es de alto riesgo y requiere medidas adicionales de cumplimiento."
            )}
          </p>

          <div className="space-y-2">
            <h4 className="font-medium text-foreground">
              {t("governance.rag.classification.about.requirements", "Requisitos de Clasificación:")}
            </h4>
            <ul className="list-disc list-inside space-y-1 text-sm text-muted-foreground ml-4">
              <li>
                {t(
                  "governance.rag.classification.about.requirement1",
                  "Evaluar si el sistema RAG cae en alguna de las categorías de alto riesgo del Anexo III"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.classification.about.requirement2",
                  "Proporcionar justificación de al menos 100 caracteres"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.classification.about.requirement3",
                  "Incluir al menos 2 palabras clave relacionadas con riesgos"
                )}
              </li>
              <li>
                {t(
                  "governance.rag.classification.about.requirement4",
                  "Si es clasificado como alto riesgo, se requerirá completar una evaluación FRIA"
                )}
              </li>
            </ul>
          </div>

          <div className="pt-4 border-t border-border">
            <Button
              onClick={() => {
                if (projectId) {
                  router.push(`/governance/compliance/classification?projectId=${projectId}&projectType=RAG`);
                } else {
                  router.push("/governance/compliance/classification/projects?projectType=RAG");
                }
              }}
              className="w-full"
            >
              {t("governance.rag.classification.goToClassification", "Ir a Clasificación")}
              <ArrowLeft className="h-4 w-4 ml-2 rotate-180" />
            </Button>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
