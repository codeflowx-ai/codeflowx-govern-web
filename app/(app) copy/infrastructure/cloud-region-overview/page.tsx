"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  Globe,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface CloudRegion {
  id: number;
  regionName: string;
  regionCode: string;
  cloudProvider: string;
  isActive: boolean;
}

const mockRegions: CloudRegion[] = [
  {
    id: 1,
    regionName: "US East (N. Virginia)",
    regionCode: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
  },
  {
    id: 2,
    regionName: "Europe West 1",
    regionCode: "europe-west1",
    cloudProvider: "GCP",
    isActive: true,
  },
  {
    id: 3,
    regionName: "West Europe",
    regionCode: "westeurope",
    cloudProvider: "AZURE",
    isActive: true,
  },
];

export default function CloudRegionOverviewPage() {   const { t } = useTranslation();
  const [regions, setRegions] = useState<CloudRegion[]>(mockRegions);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredRegions = regions.filter((region) =>
    region.regionName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    region.regionCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
    region.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
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
            <Globe className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.cloudRegionOverview", "Cloud Region Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Region")}
          </Button>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search regions...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredRegions.map((region) => (
            <Card
              key={region.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{region.regionName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{region.regionCode}</Badge>
                  <Badge variant={region.isActive ? "primary" : "secondary"}>
                    {region.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{region.cloudProvider}</span>
                  </div>
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <span onClick={() => window.location.href = `/infrastructure/cloud-region-detail?id=${region.id}`} className="flex-1">
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

        {filteredRegions.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <Globe className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noRegionsFound", "No regions found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


