"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { AlertTriangle, TrendingUp, Shield, AlertCircle } from "lucide-react";

interface RiskAssessment {
  totalRisks: number;
  highRisks: number;
  mediumRisks: number;
  lowRisks: number;
  risks: Array<{
    id: number;
    projectName: string;
    riskType: "bias" | "security" | "compliance" | "ethical" | "performance";
    severity: "high" | "medium" | "low";
    description: string;
    mitigation: string;
    status: "open" | "mitigated" | "closed";
  }>;
}

const mockRiskAssessment: RiskAssessment = {
  totalRisks: 12,
  highRisks: 3,
  mediumRisks: 5,
  lowRisks: 4,
  risks: [
    {
      id: 1,
      projectName: "Proyecto Alpha",
      riskType: "bias",
      severity: "high",
      description: "Detección de bias en modelo de clasificación de imágenes",
      mitigation: "Reentrenar modelo con dataset balanceado y aplicar técnicas de debiasing",
      status: "open",
    },
    {
      id: 2,
      projectName: "Proyecto Beta",
      riskType: "security",
      severity: "high",
      description: "Riesgo de model poisoning en modelo de recomendaciones",
      mitigation: "Implementar validación de datos de entrada y monitoreo de drift",
      status: "open",
    },
    {
      id: 3,
      projectName: "Proyecto Gamma",
      riskType: "compliance",
      severity: "medium",
      description: "Riesgo de incumplimiento GDPR por falta de explicabilidad",
      mitigation: "Implementar SHAP values y documentar decisiones del modelo",
      status: "mitigated",
    },
    {
      id: 4,
      projectName: "Proyecto Alpha",
      riskType: "ethical",
      severity: "medium",
      description: "Uso de datos sensibles sin consentimiento explícito",
      mitigation: "Revisar políticas de privacidad y obtener consentimientos necesarios",
      status: "open",
    },
    {
      id: 5,
      projectName: "Proyecto Beta",
      riskType: "performance",
      severity: "low",
      description: "Degradación de performance en producción vs. entrenamiento",
      mitigation: "Implementar A/B testing y monitoreo continuo de métricas",
      status: "open",
    },
  ],
};

const getSeverityColor = (severity: string) => {
  switch (severity) {
    case "high":
      return "danger";
    case "medium":
      return "primary";
    case "low":
      return "secondary";
    default:
      return "outline";
  }
};

export default function ProjectRiskAssessmentPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <AlertTriangle className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("projects.riskAssessment.title", "Evaluación de Riesgos de IA")}
            </h1>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.riskAssessment.totalRisks", "Total Riesgos")}
              </CardTitle>
              <AlertCircle className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{mockRiskAssessment.totalRisks}</div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-red-500/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.riskAssessment.highRisks", "Riesgos Altos")}
              </CardTitle>
              <AlertTriangle className="h-4 w-4 text-red-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-500">{mockRiskAssessment.highRisks}</div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-yellow-500/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.riskAssessment.mediumRisks", "Riesgos Medios")}
              </CardTitle>
              <Shield className="h-4 w-4 text-yellow-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-yellow-500">{mockRiskAssessment.mediumRisks}</div>
            </CardContent>
          </Card>

          <Card className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-green-500/50 transition-all duration-300">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">
                {t("projects.riskAssessment.lowRisks", "Riesgos Bajos")}
              </CardTitle>
              <TrendingUp className="h-4 w-4 text-green-500" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-500">{mockRiskAssessment.lowRisks}</div>
            </CardContent>
          </Card>
        </div>

        <div className="space-y-4">
          {mockRiskAssessment.risks.map((risk) => (
            <Card
              key={risk.id}
              className="backdrop-blur-md bg-background/60 border-border/50 hover:shadow-3xl hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{risk.projectName}</CardTitle>
                    <div className="flex items-center gap-2 mb-2">
                      <Badge variant={getSeverityColor(risk.severity) as any}>
                        {t(`projects.riskAssessment.severity.${risk.severity}`, risk.severity)}
                      </Badge>
                      <Badge variant="outline">
                        {t(`projects.riskAssessment.riskType.${risk.riskType}`, risk.riskType)}
                      </Badge>
                      <Badge variant={risk.status === "closed" ? "secondary" : "primary"}>
                        {t(`projects.riskAssessment.status.${risk.status}`, risk.status)}
                      </Badge>
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("projects.riskAssessment.description", "Descripción")}
                  </label>
                  <p className="text-foreground">{risk.description}</p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("projects.riskAssessment.mitigation", "Mitigación")}
                  </label>
                  <p className="text-foreground">{risk.mitigation}</p>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}


