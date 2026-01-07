"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ArrowLeft,
  Edit,
  FileText,
  Settings,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useSearchParams } from "next/navigation";

interface InfrastructureTemplateDetail {
  id: number;
  templateName: string;
  templateType: string;
  templateContent: string;
  description?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

const mockTemplate: InfrastructureTemplateDetail = {
  id: 1,
  templateName: "Kubernetes Cluster Template",
  templateType: "KUBERNETES",
  templateContent: JSON.stringify({
    apiVersion: "v1",
    kind: "Deployment",
    metadata: { name: "example" },
    spec: { replicas: 3 },
  }, null, 2),
  description: "Standard Kubernetes cluster deployment template",
  isActive: true,
  createdAt: "2024-01-01T00:00:00Z",
  updatedAt: "2024-01-15T00:00:00Z",
};

export default function InfrastructureTemplateDetailPage() {   const { t } = useTranslation();
  const searchParams = useSearchParams();
  const id = searchParams.get("id") || "1";
  const template = mockTemplate;

  return (
    <div className="min-h-screen bg-background">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="w-full px-4 py-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <span onClick={() => window.location.href = "/infrastructure/infrastructure-template-overview"}>
              <Button variant="ghost" size="sm">
                <ArrowLeft className="w-4 h-4" />
              </Button>
            </span>
            <div>
              <h1 className="text-3xl font-bold text-foreground flex items-center gap-3">
                <FileText className="w-8 h-8 text-primary" />
                {template.templateName}
              </h1>
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Badge variant={template.isActive ? "primary" : "secondary"}>
              {template.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
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
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.basicInformation", "Basic Information")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-muted-foreground">
                      {t("infrastructure.templateType", "Template Type")}
                    </label>
                    <p className="text-foreground font-medium">{template.templateType}</p>
                  </div>
                  {template.description && (
                    <div>
                      <label className="text-sm text-muted-foreground">
                        {t("infrastructure.description", "Description")}
                      </label>
                      <p className="text-foreground font-medium">{template.description}</p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="w-5 h-5 text-primary" />
                  {t("infrastructure.templateContent", "Template Content")}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <pre className="text-sm bg-muted/50 p-4 rounded-lg overflow-x-auto">
                  {template.templateContent}
                </pre>
              </CardContent>
            </Card>
          </div>

          <div className="space-y-6">
            <Card className="border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <FileText className="w-5 h-5 text-primary" />
                  {t("infrastructure.metadata", "Metadata")}
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.createdAt", "Created At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(template.createdAt).toLocaleString()}
                  </p>
                </div>
                <div>
                  <label className="text-sm text-muted-foreground">
                    {t("infrastructure.updatedAt", "Updated At")}
                  </label>
                  <p className="text-sm text-foreground">
                    {new Date(template.updatedAt).toLocaleString()}
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


