"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Gauge,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface ResourceQuota {
  id: number;
  quotaName: string;
  resourceType: string;
  limit: number;
  used: number;
  unit: string;
  isActive: boolean;
}

const mockQuotas: ResourceQuota[] = [
  {
    id: 1,
    quotaName: "CPU Quota",
    resourceType: "CPU",
    limit: 100,
    used: 45,
    unit: "cores",
    isActive: true,
  },
  {
    id: 2,
    quotaName: "Memory Quota",
    resourceType: "MEMORY",
    limit: 512,
    used: 256,
    unit: "GB",
    isActive: true,
  },
];

export default function ResourceQuotaOverviewPage() {   const { t } = useTranslation();
  const [quotas, setQuotas] = useState<ResourceQuota[]>(mockQuotas);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredQuotas = quotas.filter((quota) =>
    quota.quotaName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    quota.resourceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Gauge className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.resourceQuotaOverview", "Resource Quota Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Quota")}
          </Button>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search quotas...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredQuotas.map((quota) => {
            const usagePercentage = (quota.used / quota.limit) * 100;
            return (
              <Card
                key={quota.id}
                className="border-border hover:border-primary/50 transition-all duration-300"
              >
                <CardHeader>
                  <CardTitle className="text-lg">{quota.quotaName}</CardTitle>
                  <div className="flex items-center gap-2 mt-2">
                    <Badge variant="outline">{quota.resourceType}</Badge>
                    <Badge variant={quota.isActive ? "primary" : "secondary"}>
                      {quota.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="text-muted-foreground">{t("infrastructure.used", "Used")}</span>
                      <span className="text-foreground font-medium">
                        {quota.used} / {quota.limit} {quota.unit}
                      </span>
                    </div>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${
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
                      {usagePercentage.toFixed(1)}%
                    </div>
                  </div>
                  <div className="flex items-center gap-2 pt-2 border-t border-border">
                    <Button
                      variant="outline"
                      size="sm"
                      className="flex-1"
                      onClick={() => window.location.href = `/infrastructure/resource-quota-detail?id=${quota.id}`}
                    >
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                    <Button variant="outline" size="sm">
                      <Edit className="w-4 h-4" />
                    </Button>
                    <Button variant="outline" size="sm">
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>

        {filteredQuotas.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <Gauge className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noQuotasFound", "No quotas found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


