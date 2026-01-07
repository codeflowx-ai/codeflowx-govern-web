"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  DollarSign,
  Eye,
  Search,
  TrendingUp,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface InfrastructureCost {
  id: number;
  resourceName: string;
  resourceType: string;
  costPerMonth: number;
  period: string;
}

const mockCosts: InfrastructureCost[] = [
  {
    id: 1,
    resourceName: "Production Load Balancer",
    resourceType: "LOAD_BALANCER",
    costPerMonth: 16.2,
    period: "2024-01",
  },
  {
    id: 2,
    resourceName: "Main Database Cluster",
    resourceType: "DATABASE",
    costPerMonth: 234.5,
    period: "2024-01",
  },
];

export default function InfrastructureCostOverviewPage() {   const { t } = useTranslation();
  const [costs, setCosts] = useState<InfrastructureCost[]>(mockCosts);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredCosts = costs.filter((cost) =>
    cost.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cost.resourceType.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalCost = costs.reduce((sum, cost) => sum + cost.costPerMonth, 0);

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <DollarSign className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.infrastructureCostOverview", "Infrastructure Cost Overview")}
            </h1>
          </div>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="flex items-center justify-between mb-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search costs...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex items-center gap-4 pt-4 border-t border-border">
              <div>
                <div className="text-sm text-muted-foreground">{t("infrastructure.totalCost", "Total Cost")}</div>
                <div className="text-3xl font-bold text-foreground flex items-center gap-2">
                  <TrendingUp className="w-6 h-6 text-primary" />
                  ${totalCost.toFixed(2)}/mo
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredCosts.map((cost) => (
            <Card
              key={cost.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{cost.resourceName}</CardTitle>
                <Badge variant="outline" className="mt-2">{cost.resourceType}</Badge>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                    <span className="text-2xl font-bold text-foreground">
                      ${cost.costPerMonth.toFixed(2)}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-sm text-muted-foreground">
                    <span>{t("infrastructure.period", "Period")}</span>
                    <span>{cost.period}</span>
                  </div>
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => window.location.href = `/infrastructure/infrastructure-cost-detail?id=${cost.id}`}
                >
                  <Eye className="w-4 h-4 mr-2" />
                  {t("common.details", "Details")}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredCosts.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <DollarSign className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noCostsFound", "No costs found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


