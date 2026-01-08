"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  TrendingUp,
} from "lucide-react";

interface CapacityForecast {
  currentUsage: number;
  forecastedUsage: number;
  growthRate: number;
  timeHorizon: string;
}

const mockForecast: CapacityForecast = {
  currentUsage: 75,
  forecastedUsage: 92,
  growthRate: 12.5,
  timeHorizon: "3 months",
};

export default function CapacityForecastOverviewPage() {
  const { t } = useTranslation();

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header - Título y subtítulo alineados a la izquierda */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <TrendingUp className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("infrastructure.capacityForecastOverview", "Capacity Forecast")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("infrastructure.capacityForecastDesc", "Forecast capacity usage and growth trends")}
            </p>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="border-border">
            <CardHeader>
              <CardTitle>{t("infrastructure.currentUsage", "Current Usage")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold text-foreground mb-2">
                {mockForecast.currentUsage}%
              </div>
              <div className="w-full bg-muted rounded-full h-4">
                <div
                  className="h-4 rounded-full bg-blue-500"
                  style={{ width: `${mockForecast.currentUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>

          <Card className="border-border">
            <CardHeader>
              <CardTitle>{t("infrastructure.forecastedUsage", "Forecasted Usage")}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-4xl font-bold text-foreground mb-2">
                {mockForecast.forecastedUsage}%
              </div>
              <div className="w-full bg-muted rounded-full h-4">
                <div
                  className="h-4 rounded-full bg-yellow-500"
                  style={{ width: `${mockForecast.forecastedUsage}%` }}
                />
              </div>
            </CardContent>
          </Card>
        </div>

        <Card className="border-border">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TrendingUp className="w-5 h-5 text-primary" />
              {t("infrastructure.forecastDetails", "Forecast Details")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <div className="text-sm text-muted-foreground">
                  {t("infrastructure.growthRate", "Growth Rate")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {mockForecast.growthRate}%
                </div>
              </div>
              <div>
                <div className="text-sm text-muted-foreground">
                  {t("infrastructure.timeHorizon", "Time Horizon")}
                </div>
                <div className="text-2xl font-bold text-foreground">
                  {mockForecast.timeHorizon}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


