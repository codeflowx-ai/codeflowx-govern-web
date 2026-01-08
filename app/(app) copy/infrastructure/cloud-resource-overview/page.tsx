"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Database,
  Edit,
  Eye,
  Plus,
  Search,
  Server,
  Trash2,
} from "lucide-react";
import { useState } from "react";

interface CloudResource {
  id: number;
  resourceType: string;
  resourceName: string;
  resourceId?: string;
  region?: string;
  cloudProvider: string;
  isActive: boolean;
  costPerMonth?: number;
  currentInstances: number;
}

// Mock data
const mockResources: CloudResource[] = [
  {
    id: 1,
    resourceType: "LOAD_BALANCER",
    resourceName: "Production Load Balancer",
    resourceId: "lb-prod-001",
    region: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 16.2,
    currentInstances: 3,
  },
  {
    id: 2,
    resourceType: "STORAGE",
    resourceName: "Main Storage Bucket",
    resourceId: "storage-main-001",
    region: "us-east-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 45.8,
    currentInstances: 1,
  },
  {
    id: 3,
    resourceType: "DATABASE",
    resourceName: "Main Database Cluster",
    resourceId: "db-main-001",
    region: "eu-west-1",
    cloudProvider: "AWS",
    isActive: true,
    costPerMonth: 234.5,
    currentInstances: 2,
  },
];

export default function CloudResourceOverviewPage() {
  const { t } = useTranslation();
    const [resources, setResources] = useState<CloudResource[]>(mockResources);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredResources = resources.filter((resource) =>
    resource.resourceName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    resource.resourceType.toLowerCase().includes(searchTerm.toLowerCase()) ||
    resource.cloudProvider.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getResourceTypeBadge = (type: string) => {
    const colors: Record<string, string> = {
      LOAD_BALANCER: "bg-blue-500/20 text-blue-500 border-blue-500/50",
      STORAGE: "bg-green-500/20 text-green-500 border-green-500/50",
      DATABASE: "bg-purple-500/20 text-purple-500 border-purple-500/50",
      CACHE: "bg-yellow-500/20 text-yellow-500 border-yellow-500/50",
      MESSAGE_QUEUE: "bg-orange-500/20 text-orange-500 border-orange-500/50",
    };
    return colors[type] || "bg-gray-500/20 text-gray-500 border-gray-500/50";
  };

  const getStatusBadge = (isActive: boolean) => {
    return isActive ? (
      <Badge className="bg-green-500/20 text-green-500 border-green-500/50">
        {t("common.active", "Active")}
      </Badge>
    ) : (
      <Badge variant="secondary">{t("common.inactive", "Inactive")}</Badge>
    );
  };

  return (
    <div className="min-h-screen bg-background">
      <div className="w-full px-4 py-6 space-y-6">
        {/* Header - Título y subtítulo alineados a la izquierda */}
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <Server className="w-6 h-6 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">
                {t("infrastructure.cloudResources", "Cloud Resources")}
              </h1>
            </div>
            <p className="text-sm text-muted-foreground ml-9">
              {t("infrastructure.cloudResourcesDesc", "Manage and monitor cloud resources")}
            </p>
          </div>
          <Button onClick={() => window.location.href = "/infrastructure/resources/new"}>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Resource")}
          </Button>
        </div>

            {/* Search and Filters */}
            <Card className="border-border">
          <CardContent className="pt-6">
            <div className="flex items-center gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Search resources...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Resources Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredResources.map((resource) => (
            <Card
              key={resource.id}
              className="border-border hover:border-primary/50 transition-all duration-300 hover:shadow-xl"
            >
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg mb-2">{resource.resourceName}</CardTitle>
                    <div className="flex items-center gap-2 mb-2">
                      <Badge variant="outline" className={getResourceTypeBadge(resource.resourceType)}>
                        {resource.resourceType}
                      </Badge>
                      {getStatusBadge(resource.isActive)}
                    </div>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.cloudProvider", "Provider")}</span>
                    <span className="text-foreground font-medium">{resource.cloudProvider}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">{t("infrastructure.region", "Region")}</span>
                    <span className="text-foreground font-medium">{resource.region || "N/A"}</span>
                  </div>
                  {resource.costPerMonth && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.costPerMonth", "Cost/Month")}</span>
                      <span className="text-foreground font-medium">${resource.costPerMonth.toFixed(2)}</span>
                    </div>
                  )}
                  {resource.currentInstances > 0 && (
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">{t("infrastructure.instances", "Instances")}</span>
                      <span className="text-foreground font-medium">{resource.currentInstances}</span>
                    </div>
                  )}
                </div>
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Button
                    variant="outline"
                    size="sm"
                    className="flex-1"
                    onClick={() => window.location.href = `/infrastructure/cloud-resource-detail?id=${resource.id}`}
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
          ))}
        </div>

            {filteredResources.length === 0 && (
              <Card className="border-border">
            <CardContent className="py-12 text-center">
              <Database className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noResourcesFound", "No resources found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


