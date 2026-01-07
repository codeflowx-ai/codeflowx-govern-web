"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import {
  Edit,
  Eye,
  FileText,
  Plus,
  Search,
  Trash2,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface InfrastructureTemplate {
  id: number;
  templateName: string;
  templateType: string;
  description?: string;
  isActive: boolean;
}

const mockTemplates: InfrastructureTemplate[] = [
  {
    id: 1,
    templateName: "Kubernetes Cluster Template",
    templateType: "KUBERNETES",
    description: "Standard Kubernetes cluster deployment template",
    isActive: true,
  },
  {
    id: 2,
    templateName: "GPU Instance Template",
    templateType: "GPU",
    description: "Template for GPU instance deployment",
    isActive: true,
  },
];

export default function InfrastructureTemplateOverviewPage() {   const { t } = useTranslation();
  const [templates, setTemplates] = useState<InfrastructureTemplate[]>(mockTemplates);
  const [searchTerm, setSearchTerm] = useState("");

  const filteredTemplates = templates.filter((template) =>
    template.templateName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    template.templateType.toLowerCase().includes(searchTerm.toLowerCase())
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
            <FileText className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold text-foreground">
              {t("infrastructure.infrastructureTemplateOverview", "Infrastructure Template Overview")}
            </h1>
          </div>
          <Button>
            <Plus className="w-4 h-4 mr-2" />
            {t("common.add", "Add Template")}
          </Button>
        </div>

        <Card className="border-border">
          <CardContent className="pt-6">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-muted-foreground" />
              <Input
                placeholder={t("common.search", "Search templates...")}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredTemplates.map((template) => (
            <Card
              key={template.id}
              className="border-border hover:border-primary/50 transition-all duration-300"
            >
              <CardHeader>
                <CardTitle className="text-lg">{template.templateName}</CardTitle>
                <div className="flex items-center gap-2 mt-2">
                  <Badge variant="outline">{template.templateType}</Badge>
                  <Badge variant={template.isActive ? "primary" : "secondary"}>
                    {template.isActive ? t("common.active", "Active") : t("common.inactive", "Inactive")}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {template.description && (
                  <p className="text-sm text-muted-foreground">{template.description}</p>
                )}
                <div className="flex items-center gap-2 pt-2 border-t border-border">
                  <Button
                    variant="outline"
                    size="sm"
                    className="flex-1"
                    onClick={() => window.location.href = `/infrastructure/infrastructure-template-detail?id=${template.id}`}
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

        {filteredTemplates.length === 0 && (
          <Card className="border-border">
            <CardContent className="py-12 text-center">
              <FileText className="w-12 h-12 text-muted-foreground mx-auto mb-4" />
              <p className="text-muted-foreground">
                {t("infrastructure.noTemplatesFound", "No templates found")}
              </p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}


