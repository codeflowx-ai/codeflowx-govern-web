"use client";

import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { XCircle, CheckCircle2, AlertTriangle } from "lucide-react";
import { useTranslation } from "@/app/config/i18n";

interface KPICardProps {
  code: string;
  name: string;
  value: number | string;
  unit?: string;
  meta: number | string;
  criticalThreshold?: number | string;
  status: "good" | "warning" | "critical";
  description?: string;
  trend?: "up" | "down" | "stable";
  trendValue?: number;
}

export function KPICard({
  code,
  name,
  value,
  unit = "",
  meta,
  criticalThreshold,
  status,
  description,
  trend,
  trendValue,
}: KPICardProps) {
  const { t } = useTranslation();
  const statusConfig = {
    good: {
      color: "text-green-500",
      bg: "bg-green-500/10",
      border: "border-green-500/20",
      icon: CheckCircle2,
      label: t("compliance.odsImpact.kpiCard.fulfilled", "Cumplido"),
    },
    warning: {
      color: "text-yellow-500",
      bg: "bg-yellow-500/10",
      border: "border-yellow-500/20",
      icon: AlertTriangle,
      label: t("compliance.odsImpact.kpiCard.attention", "Atención"),
    },
    critical: {
      color: "text-red-500",
      bg: "bg-red-500/10",
      border: "border-red-500/20",
      icon: XCircle,
      label: t("compliance.odsImpact.kpiCard.critical", "Crítico"),
    },
  };

  const config = statusConfig[status];
  const Icon = config.icon;

  // Calcular porcentaje de cumplimiento
  const calculateCompliance = () => {
    if (typeof value === "number" && typeof meta === "number") {
      return (value / meta) * 100;
    }
    return null;
  };

  const compliance = calculateCompliance();

  // Traducir nombre del KPI si está disponible
  const getTranslatedName = () => {
    const kpiNameMap: Record<string, string> = {
      "Tasa de FRIA Completada": t("compliance.odsImpact.kpiNames.friaCompletedRate", "Tasa de FRIA Completada"),
      "Tasa de Detección de Sesgos en Datasets": t("compliance.odsImpact.kpiNames.biasDetectionRate", "Tasa de Detección de Sesgos en Datasets"),
      "Tasa de Reutilización de Modelos": t("compliance.odsImpact.kpiNames.modelReuseRate", "Tasa de Reutilización de Modelos"),
      "Tasa de Compartir Agentes en Marketplace": t("compliance.odsImpact.kpiNames.agentSharingMarketplace", "Tasa de Compartir Agentes en Marketplace"),
      "Tasa de Uso Educativo de LLMs Open Source": t("compliance.odsImpact.kpiNames.educationalLLMUsageRate", "Tasa de Uso Educativo de LLMs Open Source"),
    };
    return kpiNameMap[name] || name;
  };

  const translatedName = getTranslatedName();

  return (
    <Card className={`${config.bg} ${config.border} border`}>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <Icon className={`h-4 w-4 ${config.color}`} />
              <CardTitle className="text-sm font-medium">{translatedName}</CardTitle>
            </div>
            <div className="text-xs text-muted-foreground font-mono">
              {code}
            </div>
          </div>
          <Badge variant="outline" className={config.bg}>
            {config.label}
          </Badge>
        </div>
        {description && (
          <p className="text-xs text-muted-foreground mt-2">
            {description.includes("Porcentaje de sistemas de alto riesgo con FRIA completada")
              ? t("compliance.odsImpact.kpiNames.highRiskSystemsWithFria", description)
              : description}
          </p>
        )}
      </CardHeader>
      <CardBody className="pt-0">
        <div className="space-y-3">
          <div className="flex items-end justify-between">
            <div>
              <div className={`text-2xl font-bold ${config.color}`}>
                {typeof value === "number" ? value.toFixed(2) : value}
                {unit && <span className="text-sm ml-1">{unit}</span>}
              </div>
              {compliance !== null && (
                <div className="text-xs text-muted-foreground mt-1">
                  {compliance.toFixed(1)}% {t("compliance.odsImpact.kpiCard.ofTarget", "de meta")}
                </div>
              )}
            </div>
            {trend && trendValue !== undefined && (
              <div
                className={`text-sm ${
                  trend === "up"
                    ? "text-green-500"
                    : trend === "down"
                    ? "text-red-500"
                    : "text-gray-500"
                }`}
              >
                {trend === "up" ? "+" : trend === "down" ? "-" : ""}
                {Math.abs(trendValue).toFixed(1)}%
              </div>
            )}
          </div>
          <div className="space-y-1 text-xs">
            <div className="flex justify-between">
              <span className="text-muted-foreground">
                {t("compliance.odsImpact.kpiCard.target", "Meta")}:
              </span>
              <span className="font-medium">
                {typeof meta === "number" ? meta.toFixed(2) : meta} {unit}
              </span>
            </div>
            {criticalThreshold !== undefined && (
              <div className="flex justify-between">
                <span className="text-muted-foreground">
                  {t("compliance.odsImpact.kpiCard.criticalThreshold", "Umbral crítico")}:
                </span>
                <span className="font-medium text-red-500">
                  {typeof criticalThreshold === "number"
                    ? criticalThreshold.toFixed(2)
                    : criticalThreshold}{" "}
                  {unit}
                </span>
              </div>
            )}
          </div>
        </div>
      </CardBody>
    </Card>
  );
}


