"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Edit,
  Gauge,
  Settings,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface ResourceQuotaDetail {
  id: number;
  resourceType: string;
  quotaName: string;
  limit: number;
  used: number;
  unit: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

const mockQuota: ResourceQuotaDetail = {
  id: 1,
  resourceType: "CPU",
  quotaName: "CPU Quota",
  limit: 100,
  used: 45,
  unit: "cores",
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
};

export default function ResourceQuotaDetailPage() {
  const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const quota = mockQuota;

  const usagePercentage = (quota.used / quota.limit) * 100;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/resource-quota-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <Gauge className="w-8 h-8 text-primary" />
                {quota.quotaName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={quota.isActive ? "primary" : "secondary"}>
              {quota.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
            </Badge>
            <Button>
              <Edit className="w-4 h-4 mr-2" />
              {t("common.edit", "Edit")}
            </Button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Gauge className="w-5 h-5 text-primary" />
                  {t("infrastructure.quotaDetails", "Quota Details")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.resourceType", "Resource Type")}
                    </label>
                    <p className="text-foreground font-medium">{quota.resourceType}</p>
                  </div>
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.unit", "Unit")}
                    </label>
                    <p className="text-foreground font-medium">{quota.unit}</p>
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="text-muted-foreground">{t("infrastructure.used", "Used")}</span>
                    <span className="text-foreground font-medium">
                      {quota.used} / {quota.limit} {quota.unit}
                    </span>
                  </div>
                  <div className="w-full bg-muted rounded-full h-4">
                    <div
                      className={`h-4 rounded-full transition-all duration-500 ${
                        usagePercentage >= 80
                          ? "bg-red-500"
                          : usagePercentage >= 60
                          ? "bg-yellow-500"
                          : "bg-green-500"
                      }`}
                      style={{ width: `${usagePercentage}%` }}
                    />
                  </div>
                  <div className="text-sm text-muted-foreground text-right">
                    {usagePercentage.toFixed(1)}% {t("infrastructure.used", "used")}
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(quota.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(quota.updatedAt).toLocaleString()}
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


