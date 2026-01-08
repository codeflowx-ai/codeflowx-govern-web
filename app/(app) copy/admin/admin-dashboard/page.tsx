"use client";

import { useTranslation } from "@/app/config/i18n";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  BarChart3,
  Activity,
  Users,
  Server,
  TrendingUp,
  Shield,
  CheckCircle,
  AlertTriangle,
} from "lucide-react";
import { useState, useEffect } from "react";

interface DashboardSummary {
  activeItems: number;
  deployedItems: number;
  trainingItems: number;
  offlineItems: number;
  generatedAt: string;
}

export default function AdminDashboardPage() {
  const { t } = useTranslation();
  const [loading, setLoading] = useState(true);
  const [summary, setSummary] = useState<DashboardSummary>({
    activeItems: 0,
    deployedItems: 0,
    trainingItems: 0,
    offlineItems: 0,
    generatedAt: new Date().toISOString(),
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      // Mock data - En producción esto vendría de la API
      const mockData: DashboardSummary = {
        activeItems: 45,
        deployedItems: 32,
        trainingItems: 8,
        offlineItems: 5,
        generatedAt: new Date().toISOString(),
      };
      setSummary(mockData);
    } catch (error) {
      console.error("Error loading dashboard:", error);
    } finally {
      setLoading(false);
    }
  };

  const metrics = [
    {
      title: t("core.dashboard.activeItems", "Items Activos"),
      value: summary.activeItems,
      icon: Activity,
      description: "Items activos en el sistema",
    },
    {
      title: t("core.dashboard.deployedItems", "Items Desplegados"),
      value: summary.deployedItems,
      icon: Server,
      description: "Items desplegados en producción",
    },
    {
      title: t("core.dashboard.trainingItems", "Items en Entrenamiento"),
      value: summary.trainingItems,
      icon: TrendingUp,
      description: "Items actualmente en entrenamiento",
    },
    {
      title: t("core.dashboard.offlineItems", "Items Offline"),
      value: summary.offlineItems,
      icon: AlertTriangle,
      description: "Items fuera de línea",
    },
  ];

  return (
    <div className="min-h-screen bg-background w-full">
      <div className="w-full px-6 py-6 space-y-6">
        {/* Header */}
        <div className="space-y-2">
          <div className="flex items-center gap-3 mb-4">
            <BarChart3 className="w-8 h-8 text-foreground" />
            <h1 className="text-3xl font-semibold text-foreground">
              {t("core.dashboard.title", "Admin Dashboard Summary")}
            </h1>
          </div>
          <p className="text-muted-foreground">
            {t(
              "core.dashboard.description",
              "Resumen general del sistema administrativo"
            )}
          </p>
        </div>

        {/* Métricas principales */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {metrics.map((metric, index) => (
            <Card
              key={index}
              className="bg-card border border-border shadow-sm hover:shadow-md transition-shadow"
            >
              <CardHeader className="pb-2">
                <div className="flex items-center justify-between">
                  <CardDescription className="text-sm text-muted-foreground">
                    {metric.title}
                  </CardDescription>
                  <metric.icon className="w-5 h-5 text-muted-foreground" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-4xl font-semibold mb-2 text-foreground">{metric.value}</div>
                <p className="text-xs text-muted-foreground">{metric.description}</p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Información adicional */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="bg-card border border-border shadow-sm">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-foreground">
                <Shield className="w-5 h-5 text-foreground" />
                Estado del Sistema
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50 border border-border">
                <div className="flex items-center gap-2">
                  <CheckCircle className="w-5 h-5 text-muted-foreground" />
                  <span className="font-medium text-foreground">Sistema Operativo</span>
                </div>
                <span className="text-sm text-muted-foreground">100%</span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50 border border-border">
                <div className="flex items-center gap-2">
                  <Server className="w-5 h-5 text-muted-foreground" />
                  <span className="font-medium text-foreground">Servidores Activos</span>
                </div>
                <span className="text-sm text-muted-foreground">
                  {summary.deployedItems}
                </span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-lg bg-muted/50 border border-border">
                <div className="flex items-center gap-2">
                  <Users className="w-5 h-5 text-muted-foreground" />
                  <span className="font-medium text-foreground">Usuarios Activos</span>
                </div>
                <span className="text-sm text-muted-foreground">
                  {summary.activeItems}
                </span>
              </div>
            </CardContent>
          </Card>

          <Card className="bg-card border border-border shadow-sm">
            <CardHeader>
              <CardTitle className="flex items-center gap-2 text-foreground">
                <Activity className="w-5 h-5 text-foreground" />
                Actividad Reciente
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="text-sm text-muted-foreground">
                  Última actualización:{" "}
                  {new Date(summary.generatedAt).toLocaleString("es-ES")}
                </div>
                <div className="space-y-2">
                  <div className="flex items-center gap-2 text-sm text-foreground">
                    <div className="w-2 h-2 rounded-full bg-muted-foreground" />
                    <span>Items activos: {summary.activeItems}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-foreground">
                    <div className="w-2 h-2 rounded-full bg-muted-foreground" />
                    <span>Items desplegados: {summary.deployedItems}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-foreground">
                    <div className="w-2 h-2 rounded-full bg-muted-foreground" />
                    <span>Items en entrenamiento: {summary.trainingItems}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-foreground">
                    <div className="w-2 h-2 rounded-full bg-muted-foreground" />
                    <span>Items offline: {summary.offlineItems}</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}


