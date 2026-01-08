"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  DollarSign,
  Edit,
  TrendingUp,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface InfrastructureCostDetail {
  id: number;
  resourceId: number;
  resourceType: string;
  costPerHour: number;
  costPerDay: number;
  costPerMonth: number;
  period: string;
  createdAt: string;
  updatedAt: string;
  resourceName: string;
}

const mockCost: InfrastructureCostDetail = {
  id: 1,
  resourceId: 1,
  resourceType: "LOAD_BALANCER",
  costPerHour: 0.0225,
  costPerDay: 0.54,
  costPerMonth: 16.2,
  period: "2024-01",
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
  resourceName: "Production Load Balancer",
};

export default function InfrastructureCostDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const cost = mockCost;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/infrastructure-cost-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <DollarSign className="w-8 h-8 text-primary" />
                {cost.resourceName} - {t("infrastructure.cost", "Cost")}
              </h1>
            </div>
          </div>
          <Button>
            <Edit className="w-4 h-4 mr-2" />
            {t("common.edit", "Edit")}
          </Button>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="w-5 h-5 text-primary" />
                  {t("infrastructure.costDetails", "Cost Details")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerHour", "Cost per Hour")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerHour.toFixed(4)}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerDay", "Cost per Day")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerDay.toFixed(2)}
                    </p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.costPerMonth", "Cost per Month")}
                    </label>
                    <p className="text-2xl font-bold text-foreground">
                      ${cost.costPerMonth.toFixed(2)}
                    </p>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="w-5 h-5 text-primary" />
                  {t("infrastructure.resourceInformation", "Resource Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{cost.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.period", "Period")}
                    </label>
                    <p className="text-foreground font-medium">{cost.period}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cost.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(cost.updatedAt).toLocaleString()}
                  </p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}


