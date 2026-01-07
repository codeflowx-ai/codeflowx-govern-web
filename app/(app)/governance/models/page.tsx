"use client";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Package, Plus, Search, Filter, Download, Edit, Trash2 } from "lucide-react";
import { useState } from "react";
export default function CatalogModelsPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  // Mock data
  const models = [
    {
      id: "1",
      name: "GPT-4 Turbo",
      version: "4.0",
      type: "LLM",
      status: "active",
      riskLevel: "high",
      lastUpdated: "2024-01-15",
    },
    {
      id: "2",
      name: "Claude 3 Opus",
      version: "3.0",
      type: "LLM",
      status: "active",
      riskLevel: "medium",
      lastUpdated: "2024-01-10",
    },
    {
      id: "3",
      name: "Llama 2",
      version: "2.1",
      type: "LLM",
      status: "active",
      riskLevel: "low",
      lastUpdated: "2024-01-05",
    },
  ];
  const filteredModels = models.filter((model) =>
    model.name.toLowerCase().includes(searchTerm.toLowerCase())
  );
  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold flex items-center gap-2">
            <Package className="h-8 w-8" />
            {t("catalog.models.title", "Modelos de IA")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("catalog.models.subtitle", "Catálogo de modelos de inteligencia artificial")}
          </p>
        </div>
        <Button>
          <Plus className="h-4 w-4 mr-2" />
          {t("common.add")}
        </Button>
      </div>
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>{t("catalog.models.list", "Lista de Modelos")}</CardTitle>
            <div className="flex items-center gap-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <input
                  type="text"
                  placeholder={t("common.search")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10 pr-4 py-2 border rounded-md"
                />
              </div>
              <Button variant="outline">
                <Filter className="h-4 w-4 mr-2" />
                {t("common.filter")}
              </Button>
              <Button variant="outline">
                <Download className="h-4 w-4 mr-2" />
                {t("common.export")}
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            {filteredModels.map((model) => (
              <div
                key={model.id}
                className="p-4 border rounded-lg flex items-center justify-between"
              >
                <div className="flex items-center gap-4">
                  <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center">
                    <Package className="h-6 w-6 text-primary" />
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <h3 className="font-semibold">{model.name}</h3>
                      <Badge variant="outline">{model.version}</Badge>
                      <Badge
                        variant={
                          model.riskLevel === "high"
                            ? "danger"
                            : model.riskLevel === "medium"
                            ? "primary"
                            : "secondary"
                        }
                      >
                        {model.riskLevel}
                      </Badge>
                    </div>
                    <p className="text-sm text-muted-foreground">
                      {t("catalog.models.type", "Tipo")}: {model.type} | {t("catalog.models.lastUpdated", "Última actualización")}: {model.lastUpdated}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <Button variant="outline" size="sm">
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button variant="outline" size="sm">
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))}
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
