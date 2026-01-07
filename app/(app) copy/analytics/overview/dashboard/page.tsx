"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DevelopmentBanner from "@/components/ui/development-banner";
import { Input } from "@/components/ui/input";
import {
  BarChart3,
  Search,
  TrendingUp,
  TrendingDown,
  Activity,
  Package,
  Server,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

interface AnalyticsOverviewItem {
  id: number;
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  generatedAt: string;
}

const mockAnalyticsOverview: AnalyticsOverviewItem[] = [
  {
    id: 1,
    activeItems: 45,
    deployedItems: 32,
    trainingItems: 8,
    offlineItems: 5,
    generatedAt: "2024-01-15T10:30:00Z",
  },
  {
    id: 2,
    activeItems: 52,
    deployedItems: 38,
    trainingItems: 10,
    offlineItems: 4,
    generatedAt: "2024-01-14T09:15:00Z",
  },
  {
    id: 3,
    activeItems: 48,
    deployedItems: 35,
    trainingItems: 9,
    offlineItems: 4,
    generatedAt: "2024-01-13T14:20:00Z",
  },
  {
    id: 4,
    activeItems: 41,
    deployedItems: 30,
    trainingItems: 7,
    offlineItems: 4,
    generatedAt: "2024-01-12T11:45:00Z",
  },
  {
    id: 5,
    activeItems: 55,
    deployedItems: 40,
    trainingItems: 11,
    offlineItems: 4,
    generatedAt: "2024-01-11T16:30:00Z",
  },
];

export default function AnalyticsOverviewDashboardPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [searchTerm, setSearchTerm] = useState("");

  const filteredItems = mockAnalyticsOverview.filter((item) => {
    if (!searchTerm) return true;
    return (
      item.id.toString().includes(searchTerm) ||
      item.activeItems.toString().includes(searchTerm) ||
      item.deployedItems.toString().includes(searchTerm)
    );
  });

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("es-ES", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
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
        <div className="text-center space-y-2">
          <div className="flex items-center justify-center gap-3 mb-4">
            <BarChart3 className="w-8 h-8 text-primary" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
              {t("analytics.overview.dashboard.title", "Analytics Overview")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "analytics.overview.dashboard.subtitle",
              "Gestión de Analytics Overview"
            )}
          </p>
        </div>

        {/* Filtros */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Search className="w-5 h-5" />
              {t("common.search", "Buscar")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex gap-4">
              <div className="flex-1">
                <Input
                  placeholder={t("common.search", "Buscar...")}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full"
                />
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Tabla de Analytics Overview */}
        <Card className="backdrop-blur-md bg-background/60 border-border/50">
          <CardHeader>
            <CardTitle>
              {t("analytics.overview.dashboard.list", "Listado de Analytics")}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-border/50">
                    <th className="text-left p-4 font-semibold">ID</th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.overview.deployedItems", "Deployed Items")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.overview.trainingItems", "Training Items")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.overview.offlineItems", "Offline Items")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("analytics.overview.generatedAt", "Generated At")}
                    </th>
                    <th className="text-left p-4 font-semibold">
                      {t("common.actions", "Acciones")}
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {filteredItems.map((item) => (
                    <tr
                      key={item.id}
                      className="border-b border-border/30 hover:bg-background/50 transition-colors cursor-pointer"
                      onClick={() => {
                        // Navegar a detalles si es necesario
                      }}
                    >
                      <td className="p-4">{item.id}</td>
                      <td className="p-4">
                        <Badge variant="outline" className="bg-blue-500/10">
                          {item.deployedItems}
                        </Badge>
                      </td>
                      <td className="p-4">
                        <Badge variant="outline" className="bg-green-500/10">
                          {item.trainingItems}
                        </Badge>
                      </td>
                      <td className="p-4">
                        <Badge variant="outline" className="bg-gray-500/10">
                          {item.offlineItems}
                        </Badge>
                      </td>
                      <td className="p-4 text-sm text-muted-foreground">
                        {formatDate(item.generatedAt)}
                      </td>
                      <td className="p-4">
                        <div className="flex gap-2">
                          <button
                            className="p-2 hover:bg-primary/10 rounded transition-colors"
                            onClick={(e) => {
                              e.stopPropagation();
                              // Ver detalles
                            }}
                          >
                            <Activity className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}


