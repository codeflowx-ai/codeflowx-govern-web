"use client";

import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { TrendingUp, TrendingDown, Minus } from "lucide-react";
import { useTranslation } from "@/app/config/i18n";

interface ODSCardProps {
  odsNumber: number;
  odsName: string;
  score: number; // 0-100
  trend?: "up" | "down" | "stable";
  modulesCount: number;
  status: "good" | "warning" | "critical";
  href?: string;
}

export function ODSCard({
  odsNumber,
  odsName,
  score,
  trend,
  modulesCount,
  status,
  href,
}: ODSCardProps) {
  const { t } = useTranslation();
  const statusColors = {
    good: "bg-green-500/10 text-green-500 border-green-500/20",
    warning: "bg-yellow-500/10 text-yellow-500 border-yellow-500/20",
    critical: "bg-red-500/10 text-red-500 border-red-500/20",
  };

  const TrendIcon = trend === "up" ? TrendingUp : trend === "down" ? TrendingDown : Minus;
  const trendColor =
    trend === "up"
      ? "text-green-500"
      : trend === "down"
      ? "text-red-500"
      : "text-gray-500";

  const handleClick = () => {
    if (href) {
      window.location.href = href;
    }
  };

  const cardContent = (
    <Card
      className={`cursor-pointer hover:shadow-lg transition-shadow ${
        href ? "hover:border-primary" : ""
      } ${statusColors[status]}`}
    >
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-sm font-medium">
            ODS {odsNumber}
          </CardTitle>
          <Badge variant="outline" className={statusColors[status]}>
            {status === "good"
              ? t("compliance.odsImpact.odsCard.good", "Bueno")
              : status === "warning"
              ? t("compliance.odsImpact.odsCard.attention", "Atención")
              : t("compliance.odsImpact.odsCard.critical", "Crítico")}
          </Badge>
        </div>
        <p className="text-xs text-muted-foreground mt-1">
          {t(`compliance.odsImpact.odsNames.ods${odsNumber}`, odsName)}
        </p>
      </CardHeader>
      <CardBody className="pt-0">
        <div className="flex items-end justify-between">
          <div>
            <div className="text-3xl font-bold">{score.toFixed(1)}</div>
            <div className="text-xs text-muted-foreground">
              {t("compliance.odsImpact.odsCard.score", "Score")}
            </div>
          </div>
          <div className="text-right">
            {trend && (
              <div className={`flex items-center gap-1 ${trendColor}`}>
                <TrendIcon className="h-4 w-4" />
                <span className="text-xs">
                  {trend === "up" ? "+" : trend === "down" ? "-" : ""}2.5%
                </span>
              </div>
            )}
            <div className="text-xs text-muted-foreground mt-1">
              {modulesCount} {t("compliance.odsImpact.odsCard.modules", "módulos")}
            </div>
          </div>
        </div>
      </CardBody>
    </Card>
  );

  if (href) {
    return (
      <div
        onClick={handleClick}
        onKeyDown={(e) => {
          if (e.key === "Enter" || e.key === " ") {
            e.preventDefault();
            handleClick();
          }
        }}
        role="button"
        tabIndex={0}
        className="w-full"
      >
        {cardContent}
      </div>
    );
  }

  return cardContent;
}


