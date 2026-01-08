"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { BarChart3, TrendingUp, AlertTriangle, CheckCircle } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export default function QualityMetricsPage() {
  const { t } = useTranslation();
  const router = useRouter();
  const [metrics, setMetrics] = useState({
    overallAverage: 0.87,
    dimensions: [
      { name: "COMPLETENESS", label: "Completitud", score: 0.85, threshold: 0.90, status: "WARNING" },
      { name: "ACCURACY", label: "Precisión", score: 0.92, threshold: 0.90, status: "PASS" },
      { name: "CONSISTENCY", label: "Consistencia", score: 0.88, threshold: 0.85, status: "PASS" },
      { name: "VALIDITY", label: "Validez", score: 0.90, threshold: 0.90, status: "PASS" },
      { name: "TIMELINESS", label: "Puntualidad", score: 0.95, threshold: 0.80, status: "PASS" },
      { name: "UNIQUENESS", label: "Unicidad", score: 0.98, threshold: 0.95, status: "PASS" },
    ],
    datasetsBelowThreshold: 5,
    totalDatasets: 45,
  });

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PASS": return "bg-green-100 text-green-800";
      case "WARNING": return "bg-yellow-100 text-yellow-800";
      case "FAIL": return "bg-red-100 text-red-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <div className="w-full p-6 space-y-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold">
            {t("governance.data.quality.title", "Métricas de Calidad de Datos")}
          </h1>
          <p className="text-muted-foreground mt-2">
            {t("governance.data.quality.description", "Análisis de calidad según ISO 8000 - 6 dimensiones")}
          </p>
        </div>
        <Button onClick={() => router.push("/governance/data/dashboard")}>
          Ver Dashboard
        </Button>
      </div>

      {/* Score Global */}
      <Card>
        <CardHeader>
          <CardTitle>Score Global de Calidad</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="text-center">
            <div className="text-6xl font-bold mb-2">
              {(metrics.overallAverage * 100).toFixed(0)}%
            </div>
            <p className="text-muted-foreground">Promedio de las 6 dimensiones ISO 8000</p>
            <div className="mt-4">
              <Badge className="bg-green-100 text-green-800">
                <CheckCircle className="h-4 w-4 mr-1" />
                Cumple con estándares
              </Badge>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Dimensiones */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {metrics.dimensions.map((dim) => (
          <Card key={dim.name} className="border-l-4 border-l-blue-500">
            <CardHeader>
              <CardTitle className="text-lg">{dim.label}</CardTitle>
            </CardHeader>
            <CardBody>
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-2xl font-bold">{(dim.score * 100).toFixed(0)}%</span>
                  <Badge className={getStatusColor(dim.status)}>{dim.status}</Badge>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${
                      dim.status === "PASS" ? "bg-green-500" : dim.status === "WARNING" ? "bg-yellow-500" : "bg-red-500"
                    }`}
                    style={{ width: `${dim.score * 100}%` }}
                  />
                </div>
                <div className="text-xs text-muted-foreground">
                  Umbral mínimo: {(dim.threshold * 100).toFixed(0)}%
                </div>
              </div>
            </CardBody>
          </Card>
        ))}
      </div>

      {/* Alertas */}
      <Card className="border-l-4 border-l-yellow-500">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <AlertTriangle className="h-5 w-5 text-yellow-500" />
            Datasets Requiriendo Atención
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="flex items-center justify-between">
            <div>
              <p className="font-medium">{metrics.datasetsBelowThreshold} datasets</p>
              <p className="text-sm text-muted-foreground">
                con calidad por debajo del umbral mínimo
              </p>
            </div>
            <Button
              variant="outline"
              onClick={() => router.push("/governance/data/datasets/overview?quality=low")}
            >
              Ver Datasets
            </Button>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
