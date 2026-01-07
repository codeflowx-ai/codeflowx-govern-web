"use client";
import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Trophy, TrendingUp, BarChart3, FileText } from "lucide-react";
import { useRouter } from "next/navigation";

interface ABTestResult {
  runA: {
    id: number;
    name: string;
    metrics: Record<string, any>;
  };
  runB: {
    id: number;
    name: string;
    metrics: Record<string, any>;
  };
  comparison: {
    winner: "A" | "B" | "TIE";
    averageScoreA: number;
    averageScoreB: number;
    winsA: number;
    winsB: number;
    ties: number;
    statisticalSignificance: number;
    improvement: Record<string, string>;
    metricComparisons?: Record<string, {
      metricName: string;
      valueA: number;
      valueB: number;
      difference: number;
      improvementPercentage: string;
      winner: string;
    }>;
  };
  comparisonRunId?: number;
}

interface ABTestResultsProps {
  result: ABTestResult;
}

export default function ABTestResults({ result }: ABTestResultsProps) {
  const { t } = useTranslation();
  const router = useRouter();

  const getWinnerColor = (winner: string) => {
    if (winner === "TIE") return "bg-gray-100 text-gray-800";
    return winner === "A" ? "bg-blue-100 text-blue-800" : "bg-green-100 text-green-800";
  };

  const getWinnerIcon = (winner: string) => {
    if (winner === "TIE") return "🤝";
    return winner === "A" ? "🏆 A" : "🏆 B";
  };

  return (
    <div className="space-y-6">
      {/* Resultado Principal */}
      <Card className="border-2">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-xl">
              {t("governance.testing.abTest.results.title", "Resultados de Prueba A/B")}
            </CardTitle>
            <Badge className={getWinnerColor(result.comparison.winner)}>
              {getWinnerIcon(result.comparison.winner)}
            </Badge>
          </div>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-2 gap-6">
            {/* Run A */}
            <div className="space-y-4">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-blue-500 rounded"></div>
                <h3 className="font-semibold">{result.runA.name}</h3>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">
                    {t("governance.testing.abTest.results.averageScore", "Score Promedio")}:
                  </span>
                  <span className="font-semibold">
                    {(result.comparison.averageScoreA * 100).toFixed(2)}%
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">
                    {t("governance.testing.abTest.results.wins", "Victorias")}:
                  </span>
                  <span className="font-semibold">{result.comparison.winsA}</span>
                </div>
                {result.runA.metrics.totalTests && (
                  <div className="flex justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("governance.testing.abTest.results.totalTests", "Total Tests")}:
                    </span>
                    <span>{result.runA.metrics.totalTests}</span>
                  </div>
                )}
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => {
                  if (typeof window !== "undefined") {
                    window.location.assign(`/governance/testing/runs/${result.runA.id}`);
                  }
                }}
              >
                <FileText className="w-4 h-4 mr-2" />
                {t("governance.testing.abTest.results.viewRun", "Ver Run")}
              </Button>
            </div>

            {/* Run B */}
            <div className="space-y-4">
              <div className="flex items-center gap-2">
                <div className="w-4 h-4 bg-green-500 rounded"></div>
                <h3 className="font-semibold">{result.runB.name}</h3>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">
                    {t("governance.testing.abTest.results.averageScore", "Score Promedio")}:
                  </span>
                  <span className="font-semibold">
                    {(result.comparison.averageScoreB * 100).toFixed(2)}%
                  </span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-muted-foreground">
                    {t("governance.testing.abTest.results.wins", "Victorias")}:
                  </span>
                  <span className="font-semibold">{result.comparison.winsB}</span>
                </div>
                {result.runB.metrics.totalTests && (
                  <div className="flex justify-between">
                    <span className="text-sm text-muted-foreground">
                      {t("governance.testing.abTest.results.totalTests", "Total Tests")}:
                    </span>
                    <span>{result.runB.metrics.totalTests}</span>
                  </div>
                )}
              </div>
              <Button
                variant="outline"
                size="sm"
                onClick={() => {
                  if (typeof window !== "undefined") {
                    window.location.assign(`/governance/testing/runs/${result.runB.id}`);
                  }
                }}
              >
                <FileText className="w-4 h-4 mr-2" />
                {t("governance.testing.abTest.results.viewRun", "Ver Run")}
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Comparación Detallada */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <BarChart3 className="w-5 h-5" />
            {t("governance.testing.abTest.results.detailedComparison", "Comparación Detallada")}
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            {/* Significancia Estadística */}
            <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
              <span className="font-medium">
                {t("governance.testing.abTest.results.statisticalSignificance", "Significancia Estadística")}:
              </span>
              <Badge variant="outline" className="text-lg">
                {(result.comparison.statisticalSignificance * 100).toFixed(1)}%
              </Badge>
            </div>

            {/* Mejoras */}
            {result.comparison.improvement && Object.keys(result.comparison.improvement).length > 0 && (
              <div className="space-y-2">
                <h4 className="font-semibold flex items-center gap-2">
                  <TrendingUp className="w-4 h-4" />
                  {t("governance.testing.abTest.results.improvements", "Mejoras")}
                </h4>
                <div className="grid grid-cols-2 gap-2">
                  {Object.entries(result.comparison.improvement).map(([key, value]) => (
                    <div key={key} className="flex justify-between p-2 bg-muted rounded">
                      <span className="text-sm capitalize">{key}:</span>
                      <span className={`font-semibold ${value.startsWith("+") ? "text-green-600" : "text-red-600"}`}>
                        {value}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Comparación de Métricas */}
            {result.comparison.metricComparisons && Object.keys(result.comparison.metricComparisons).length > 0 && (
              <div className="space-y-2">
                <h4 className="font-semibold">
                  {t("governance.testing.abTest.results.metricComparisons", "Comparación de Métricas")}
                </h4>
                <div className="space-y-2">
                  {Object.entries(result.comparison.metricComparisons).map(([key, metric]) => (
                    <div key={key} className="p-3 border rounded-lg">
                      <div className="flex justify-between items-center mb-2">
                        <span className="font-medium capitalize">{metric.metricName}</span>
                         <Badge variant={metric.winner === "B" ? "primary" : "secondary"}>
                          {metric.winner === "TIE" ? "EMPATE" : `Ganador: ${metric.winner}`}
                        </Badge>
                      </div>
                      <div className="grid grid-cols-3 gap-2 text-sm">
                        <div>
                          <span className="text-muted-foreground">A:</span> {metric.valueA.toFixed(3)}
                        </div>
                        <div>
                          <span className="text-muted-foreground">B:</span> {metric.valueB.toFixed(3)}
                        </div>
                        <div className={`font-semibold ${metric.improvementPercentage.startsWith("+") ? "text-green-600" : "text-red-600"}`}>
                          {metric.improvementPercentage}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </CardBody>
      </Card>

      {/* Acciones */}
      <div className="flex gap-2">
        {result.comparisonRunId && (
          <Button
            variant="outline"
            onClick={() => {
              if (typeof window !== "undefined") {
                window.location.assign(`/governance/testing/runs/${result.comparisonRunId}`);
              }
            }}
          >
            <FileText className="w-4 h-4 mr-2" />
            {t("governance.testing.abTest.results.viewComparisonRun", "Ver Run de Comparación")}
          </Button>
        )}
        <Button
          onClick={() => {
            if (typeof window !== "undefined") {
              window.location.assign(`/governance/testing/compare?runIds=${result.runA.id},${result.runB.id}`);
            }
          }}
        >
          <BarChart3 className="w-4 h-4 mr-2" />
          {t("governance.testing.abTest.results.compareRuns", "Comparar Runs")}
        </Button>
      </div>
    </div>
  );
}
