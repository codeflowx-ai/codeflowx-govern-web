"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Cloud,
  Edit,
  Eye,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface CloudProvider {
  id: number;
  providerName: string;
  providerType: string;
  region?: string;
  isActive: boolean;
  costPerMonth?: number;
}

const mockProviders: CloudProvider[] = [
  {
    id: 1,
    providerName: "AWS Production",
    providerType: "AWS",
    region: "us-east-1",
    isActive: true,
    costPerMonth: 2847.32,
  },
  {
    id: 2,
    providerName: "Google Cloud",
    providerType: "GCP",
    region: "europe-west1",
    isActive: true,
    costPerMonth: 1456.78,
  },
  {
    id: 3,
    providerName: "Azure",
    providerType: "AZURE",
    region: "West Europe",
    isActive: true,
    costPerMonth: 987.45,
  },
];

export default function CloudProviderOverviewPage() {   const { t } = useTranslation();
  const [providers, setProviders] = useState<CloudProvider[]>(mockProviders);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredProviders = providers.filter((provider) =>
    provider.providerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    provider.providerType.toLowerCase().includes(searchTerm.toLowerCase())
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
            <Cloud className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.cloudProviderOverview", "Cloud Provider Overview")}
            </h1>
          </div>
          <span onClick={() => window.location.href = "/infrastructure/providers/new"}>
            <Button>
              <Plus className="w-4 h-4 mr-2" />
              {t("common.add", "Add Provider")}
            </Button>
          </span>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search providers...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProviders.map((provider) => (
            <Card
              key={provider.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{provider.providerName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{provider.providerType}</Badge>
                  <Badge variant={provider.isActive ? "primary" : "secondary"}>
                    {provider.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  {provider.region && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.region", "Region")}</span>
                      <span className="text-foreground font-medium">{provider.region}</span>
                    </div>
                  )}
                  {provider.costPerMonth && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                      <span className="text-foreground font-medium">${provider.costPerMonth.toFixed(2)}</span>
                    </div>
                  )}
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <span onClick={() => window.location.href = `/infrastructure/cloud-provider-detail?id=${provider.id}`} className="flex-1">
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="w-4 h-4 mr-2" />
                      {t("common.details", "Details")}
                    </Button>
                  </span>
                  <Button variant="outline" size="sm">
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {filteredProviders.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <Cloud className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noProvidersFound", "No providers found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


