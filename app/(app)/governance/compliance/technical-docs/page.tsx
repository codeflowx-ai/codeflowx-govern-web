"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  FileText,
  CheckCircle,
  AlertCircle,
  Search,
  Filter,
  X,
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  mockModelsSummary,
  type ModelDocumentationSummary,
} from "@/app/(app)/governance/data/mockTechnicalDocs";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export default function TechnicalDocsPage() {
  const { t } = useTranslation();
  const [models, setModels] = useState<ModelDocumentationSummary[]>(mockModelsSummary);
  const [loading, setLoading] = useState(true);
  const [filterModel, setFilterModel] = useState<string>("");
  const [filterScore, setFilterScore] = useState<string>("all");
  const [filterCompleteness, setFilterCompleteness] = useState<string>("all");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      await new Promise((resolve) => setTimeout(resolve, 500));
      // In production, fetch from API
      setModels(mockModelsSummary);
    } catch (error) {
      console.error("Error loading models:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleViewDocumentation = (modelId: number) => {
    window.location.href = `/governance/compliance/technical-docs/${modelId}`;
  };

  const filteredModels = models.filter((model) => {
    if (filterModel && !model.modelName.toLowerCase().includes(filterModel.toLowerCase())) {
      return false;
    }
    if (filterScore !== "all") {
      const scoreThreshold = filterScore === "high" ? 0.8 : filterScore === "medium" ? 0.5 : 0;
      if (model.overallScore < scoreThreshold) return false;
      if (filterScore === "high" && model.overallScore < 0.8) return false;
      if (filterScore === "medium" && (model.overallScore < 0.5 || model.overallScore >= 0.8)) return false;
      if (filterScore === "low" && model.overallScore >= 0.5) return false;
    }
    if (filterCompleteness !== "all") {
      if (filterCompleteness === "complete" && !model.isComplete) return false;
      if (filterCompleteness === "incomplete" && model.isComplete) return false;
    }
    return true;
  });

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      {/* Partículas flotantes */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-teal-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1.5 h-1.5 bg-teal-400/25 rounded-full animate-pulse delay-1000" />
      </div>

      <div className="relative z-10 w-full px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-left space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <FileText className="w-8 h-8 text-teal-500" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-teal-500 to-teal-700 bg-clip-text text-transparent">
              {t("governance.compliance.technicalDocs.title")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t("governance.compliance.technicalDocs.subtitle")}
          </p>
        </div>

        {/* Models List */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>{t("governance.compliance.technicalDocs.modelsList")}</CardTitle>
          </CardHeader>
          <CardBody>
            {/* Filters */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("governance.compliance.technicalDocs.filterByModel")}
                  value={filterModel}
                  onChange={(e) => setFilterModel(e.target.value)}
                  className="pl-9"
                />
              </div>
              <Select value={filterScore} onValueChange={setFilterScore}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.compliance.technicalDocs.filterByScore")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">{t("governance.compliance.technicalDocs.all")}</SelectItem>
                  <SelectItem value="high">High (≥80%)</SelectItem>
                  <SelectItem value="medium">Medium (50-79%)</SelectItem>
                  <SelectItem value="low">Low (&lt;50%)</SelectItem>
                </SelectContent>
              </Select>
              <Select value={filterCompleteness} onValueChange={setFilterCompleteness}>
                <SelectTrigger>
                  <SelectValue placeholder={t("governance.compliance.technicalDocs.filterByCompleteness")} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">{t("governance.compliance.technicalDocs.all")}</SelectItem>
                  <SelectItem value="complete">{t("governance.compliance.technicalDocs.complete")}</SelectItem>
                  <SelectItem value="incomplete">{t("governance.compliance.technicalDocs.incomplete")}</SelectItem>
                </SelectContent>
              </Select>
              <Button
                variant="outline"
                onClick={() => {
                  setFilterModel("");
                  setFilterScore("all");
                  setFilterCompleteness("all");
                }}
              >
                <X className="h-4 w-4 mr-2" />
                Clear
              </Button>
            </div>

            {/* Models Table */}
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">{t("governance.compliance.technicalDocs.modelName")}</th>
                    <th className="text-center p-2">{t("governance.compliance.technicalDocs.overallScore")}</th>
                    <th className="text-center p-2">{t("governance.compliance.technicalDocs.completed")}</th>
                    <th className="text-center p-2">Status</th>
                    <th className="text-center p-2">{t("governance.compliance.technicalDocs.actions")}</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredModels.map((model) => (
                    <tr
                      key={model.modelId}
                      className="border-b hover:bg-muted/50"
                    >
                      <td className="p-2 font-semibold">{model.modelName}</td>
                      <td className="p-2 text-center">
                        <Badge variant={model.overallScore >= 0.8 ? "primary" : model.overallScore >= 0.5 ? "secondary" : "danger"}>
                          {(model.overallScore * 100).toFixed(0)}%
                        </Badge>
                      </td>
                      <td className="p-2 text-center">
                        {model.completedSections}/{model.totalSections}
                      </td>
                      <td className="p-2 text-center">
                        {model.isComplete ? (
                          <Badge className="bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-300">
                            <CheckCircle className="w-3 h-3 mr-1 inline" />
                            {t("governance.compliance.technicalDocs.complete")}
                          </Badge>
                        ) : (
                          <Badge variant="secondary">
                            <AlertCircle className="w-3 h-3 mr-1 inline" />
                            {t("governance.compliance.technicalDocs.incomplete")}
                          </Badge>
                        )}
                      </td>
                      <td className="p-2 text-center">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleViewDocumentation(model.modelId)}
                        >
                          {t("governance.compliance.technicalDocs.view")}
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}
