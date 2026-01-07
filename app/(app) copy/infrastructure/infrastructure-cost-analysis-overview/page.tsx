"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  DollarSign,
  TrendingDown,
  TrendingUp,
} from "lucide-react";

interface CostAnalysis {
  totalCost: number;
  costByProvider: { provider: string; cost: number }[];
  costByResourceType: { type: string; cost: number }[];
  trend: "up" | "down";
  percentageChange: number;
}

const mockAnalysis: CostAnalysis = {
  totalCost: 5289.45,
  costByProvider: [
    { provider: "AWS", cost: 2847.32 },
    { provider: "GCP", cost: 1456.78 },
    { provider: "AZURE", cost: 987.45 },
  ],
  costByResourceType: [
    { type: "DATABASE", cost: 234.5 },
    { type: "LOAD_BALANCER", cost: 16.2 },
    { type: "STORAGE", cost: 45.8 },
  ],
  trend: "down",
  percentageChange: 12.5,
};

export default function InfrastructureCostAnalysisOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between"><div className="flex items-center gap-3">
          <DollarSign className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold text-foreground">
            {t("infrastructure.infrastructureCostAnalysisOverview", "Infrastructure Cost Analysis")}
          </h1>
        </div></div>

        <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <DollarSign className="w-5 h-5 text-primary" />
              {t("infrastructure.totalCost", "Total Cost")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-4xl font-bold text-foreground">
                ${mockAnalysis.totalCost.toFixed(2)}
              </div>
              <div className={`flex items-center gap-2 ${mockAnalysis.trend === "down" ? "text-green-500" : "text-red-500"}`}>
                {mockAnalysis.trend === "down" ? (
                  <TrendingDown className="w-5 h-5" />
                ) : (
                  <TrendingUp className="w-5 h-5" />
                )}
                <span className="text-lg font-medium">
                  {mockAnalysis.percentageChange}%
                </span>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="border-border">
            <CardHeader>
              <CardTitle>{t("infrastructure.costByProvider", "Cost by Provider")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {mockAnalysis.costByProvider.map((item, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-foreground font-medium">{item.provider}</span>
                    <span className="text-foreground font-bold">${item.cost.toFixed(2)}</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader>
              <CardTitle>{t("infrastructure.costByResourceType", "Cost by Resource Type")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {mockAnalysis.costByResourceType.map((item, index) => (
                  <div key={index} className="flex items-center justify-between">
                    <span className="text-foreground font-medium">{item.type}</span>
                    <span className="text-foreground font-bold">${item.cost.toFixed(2)}</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}


