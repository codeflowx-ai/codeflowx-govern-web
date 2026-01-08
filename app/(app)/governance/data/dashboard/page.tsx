"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { AlertTriangle, TrendingUp, Shield, FileCheck, Database, BarChart3, Users, CheckCircle } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export default function DataGovernanceDashboardPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [stats, setStats] = useState({
    totalDatasets: 0,
    totalOrigins: 0,
    datasetsWithRisks: 0,
    datasetsWithLowQuality: 0,
    datasetsWithPII: 0,
    datasetsCompliant: 0,
    averageQualityScore: 0,
    averageBiasScore: 0,
  });

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      // Mock stats - En producción vendría del backend
      setStats({
        totalDatasets: 45,
        totalOrigins: 12,
        datasetsWithRisks: 8,
        datasetsWithLowQuality: 5,
        datasetsWithPII: 15,
        datasetsCompliant: 32,
        averageQualityScore: 0.87,
        averageBiasScore: 0.75,
      });
    } catch (error) {
      console.error("Error loading stats:", error);
    }
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.dashboard.title", "Dashboard de Gobierno del Dato")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.dashboard.description", "Vista consolidada de datasets, calidad, riesgos y compliance")}
          </p>
        </div>
      </div>

      {/* KPIs Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Total Datasets</p>
                <p className="text-2xl font-bold">{stats.totalDatasets}</p>
              </div>
              <Database className="h-8 w-8 text-blue-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Orígenes de Datos</p>
                <p className="text-2xl font-bold">{stats.totalOrigins}</p>
              </div>
              <FileCheck className="h-8 w-8 text-green-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Calidad Promedio</p>
                <p className="text-2xl font-bold">{(stats.averageQualityScore * 100).toFixed(0)}%</p>
              </div>
              <TrendingUp className="h-8 w-8 text-green-500" />
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody>
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Compliance</p>
                <p className="text-2xl font-bold">{stats.datasetsCompliant}/{stats.totalDatasets}</p>
              </div>
              <Shield className="h-8 w-8 text-blue-500" />
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Alertas y Riesgos */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card className="border-l-4 border-l-red-500">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-red-500" />
              Alertas y Riesgos
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center justify-between p-3 bg-red-50 dark:bg-red-950/80 border border-red-200 dark:border-red-800 rounded-lg">
                <div>
                  <p className="font-medium text-red-900 dark:text-red-100">Datasets con Riesgos</p>
                  <p className="text-sm text-red-800 dark:text-red-200">{stats.datasetsWithRisks} datasets requieren atención</p>
                </div>
                <Badge className="bg-red-200 dark:bg-red-800 text-red-900 dark:text-red-50">{stats.datasetsWithRisks}</Badge>
              </div>
              <div className="flex items-center justify-between p-3 bg-yellow-50 dark:bg-yellow-950/80 border border-yellow-200 dark:border-yellow-800 rounded-lg">
                <div>
                  <p className="font-medium text-yellow-900 dark:text-yellow-100">Calidad Baja</p>
                  <p className="text-sm text-yellow-800 dark:text-yellow-200">{stats.datasetsWithLowQuality} datasets bajo umbral</p>
                </div>
                <Badge className="bg-yellow-200 dark:bg-yellow-800 text-yellow-900 dark:text-yellow-50">{stats.datasetsWithLowQuality}</Badge>
              </div>
              <div className="flex items-center justify-between p-3 bg-orange-50 dark:bg-orange-950/80 border border-orange-200 dark:border-orange-800 rounded-lg">
                <div>
                  <p className="font-medium text-orange-900 dark:text-orange-100">PII Detectado</p>
                  <p className="text-sm text-orange-800 dark:text-orange-200">{stats.datasetsWithPII} datasets con datos personales</p>
                </div>
                <Badge className="bg-orange-200 dark:bg-orange-800 text-orange-900 dark:text-orange-50">{stats.datasetsWithPII}</Badge>
              </div>
            </div>
            <Button
              variant="outline"
              className="w-full mt-4"
              onClick={() => router.push("/governance/data/risks")}
            >
              Ver Todos los Riesgos
            </Button>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <BarChart3 className="h-5 w-5" />
              Métricas de Calidad
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-sm text-foreground">Calidad Promedio</span>
                  <span className="text-sm font-medium text-foreground">{(stats.averageQualityScore * 100).toFixed(0)}%</span>
                </div>
                <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                  <div
                    className="bg-green-500 h-2 rounded-full"
                    style={{ width: `${stats.averageQualityScore * 100}%` }}
                  />
                </div>
              </div>
              <div>
                <div className="flex justify-between mb-1">
                  <span className="text-sm text-foreground">Sesgos Promedio</span>
                  <span className="text-sm font-medium text-foreground">{(stats.averageBiasScore * 100).toFixed(0)}%</span>
                </div>
                <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                  <div
                    className="bg-blue-500 h-2 rounded-full"
                    style={{ width: `${stats.averageBiasScore * 100}%` }}
                  />
                </div>
              </div>
            </div>
            <Button
              variant="outline"
              className="w-full mt-4"
              onClick={() => router.push("/governance/data/quality")}
            >
              Ver Métricas Detalladas
            </Button>
          </CardBody>
        </Card>
      </div>

      {/* Accesos Rápidos */}
      <Card>
        <CardHeader>
          <CardTitle>Accesos Rápidos</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/datasets/overview")}
            >
              <Database className="h-6 w-6" />
              <span>Datasets</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/origins")}
            >
              <FileCheck className="h-6 w-6" />
              <span>Orígenes</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/risks")}
            >
              <AlertTriangle className="h-6 w-6" />
              <span>Riesgos</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/quality")}
            >
              <BarChart3 className="h-6 w-6" />
              <span>Calidad</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/privacy")}
            >
              <Shield className="h-6 w-6" />
              <span>Privacidad</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/lineage")}
            >
              <FileCheck className="h-6 w-6" />
              <span>Línea de Base</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/documentation")}
            >
              <FileCheck className="h-6 w-6" />
              <span>Documentación</span>
            </Button>
            <Button
              variant="outline"
              className="flex flex-col items-center gap-2 h-auto py-4"
              onClick={() => router.push("/governance/data/roles")}
            >
              <Users className="h-6 w-6" />
              <span>Roles</span>
            </Button>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
