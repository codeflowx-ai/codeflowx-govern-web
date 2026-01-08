"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  Activity,
  Heart,
  Search,
  TrendingDown,
  TrendingUp,
  X,
} from "lucide-react";
import { useState } from "react";

// Mock data basado en IndexHealthDashboardOverviewViewModel
interface HealthDashboardItem {
  totalItems: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  avgScore: number;
}

const mockHealthData: HealthDashboardItem[] = [
  {
    totalItems: 150,
    activeItems: 120,
    deployedItems: 100,
    trainingItems: 20,
    offlineItems: 10,
    avgScore: 92.5,
  },
  {
    totalItems: 200,
    activeItems: 180,
    deployedItems: 150,
    trainingItems: 30,
    offlineItems: 20,
    avgScore: 88.3,
  },
  {
    totalItems: 95,
    activeItems: 85,
    deployedItems: 75,
    trainingItems: 10,
    offlineItems: 5,
    avgScore: 95.1,
  },
  {
    totalItems: 175,
    activeItems: 160,
    deployedItems: 140,
    trainingItems: 20,
    offlineItems: 15,
    avgScore: 90.7,
  },
];

export default function HealthDashboardPage() {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState("");
  const [healthData, setHealthData] = useState(mockHealthData);

  const filteredData = healthData.filter((item) => {
    if (!searchTerm) return true;
    return (
      item.totalItems.toString().includes(searchTerm) ||
      item.activeItems.toString().includes(searchTerm) ||
      item.avgScore.toString().includes(searchTerm)
    );
  });

  const clearFilters = () => {
    setSearchTerm("");
  };

  const getScoreColor = (score: number) => {
    if (score >= 90) return "text-green-500";
    if (score >= 75) return "text-yellow-500";
    return "text-red-500";
  };

  const getScoreBadgeVariant = (score: number) => {
    if (score >= 90) return "primary";
    if (score >= 75) return "secondary";
    return "danger";
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-background/80 relative overflow-hidden">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-2 h-2 bg-blue-400/30 rounded-full animate-pulse" />
        <div className="absolute top-3/4 right-1/4 w-1 h-1 bg-purple-400/40 rounded-full animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 w-1.5 h-1.5 bg-cyan-400/35 rounded-full animate-pulse delay-500" />
      </div>

      <div className="relative z-10">
        <DevelopmentBanner className="backdrop-blur-md bg-background/60 border-border/50" />
      </div>

      <div className="relative z-10 container mx-auto px-4 py-6 space-y-6">
        {/* Header */}
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Heart className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("rag.healthDashboard.title", "Index Health Dashboard")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "rag.healthDashboard.description",
              "Gestión de Index Health Dashboard"
            )}
          </p>
        </div>

        {/* Filters */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardBody className="p-4">
            <div className="flex items-center gap-4">
              <div className="flex-1 relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      // Filter is applied automatically
                    }
                  }}
                />
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={clearFilters}
                className="gap-2"
              >
                <X className="h-4 w-4" />
                {t("common.clearFilters", "Limpiar")}
              </Button>
            </div>
          </CardBody>
        </Card>

        {/* Health Metrics Table */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="w-5 h-5 text-primary" />
              {t("rag.healthDashboard.metrics", "Métricas de Salud")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border">
                    <th className="text-left p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.totalItems", "Total Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.activeItems", "Active Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t(
                        "rag.healthDashboard.deployedItems",
                        "Deployed Items"
                      )}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t(
                        "rag.healthDashboard.trainingItems",
                        "Training Items"
                      )}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.offlineItems", "Offline Items")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("rag.healthDashboard.avgScore", "Avg Score")}
                    </th>
                    <th className="text-center p-4 font-semibold text-sm">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((item, index) => (
                    <tr
                      key={index}
                      className="border-b border-border/50 hover:bg-muted/30 transition-colors cursor-pointer"
                    >
                      <td className="p-4">
                        <div className="font-medium">{item.totalItems}</div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge
                          variant="outline"
                          className="bg-green-100 text-green-800 dark:bg-green-500/20 dark:text-green-300"
                        >
                          {item.activeItems}
                        </Badge>
                      </td>
                      <td className="p-4 text-center">
                        <div className="flex items-center justify-center gap-1">
                          <TrendingUp className="h-4 w-4 text-blue-500" />
                          <span>{item.deployedItems}</span>
                        </div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge variant="secondary">{item.trainingItems}</Badge>
                      </td>
                      <td className="p-4 text-center">
                        <div className="flex items-center justify-center gap-1">
                          <TrendingDown className="h-4 w-4 text-red-500" />
                          <span className="text-red-600 dark:text-red-400">
                            {item.offlineItems}
                          </span>
                        </div>
                      </td>
                      <td className="p-4 text-center">
                        <Badge
                          variant={getScoreBadgeVariant(item.avgScore)}
                          className="text-lg font-semibold"
                        >
                          {item.avgScore.toFixed(1)}%
                        </Badge>
                      </td>
                      <td className="p-4 text-center">
                        <Button
                          variant="ghost"
                          size="sm"
                          className="gap-2"
                          onClick={() => {
                            // Navigate to details
                            console.log("View details for", item.totalItems);
                          }}
                        >
                          <Search className="h-4 w-4" />
                          {t("common.details", "Ver detalles")}
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {filteredData.length === 0 && (
                <div className="text-center py-8 text-muted-foreground">
                  {t("common.noResults", "No se encontraron resultados")}
                </div>
              )}
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
}




