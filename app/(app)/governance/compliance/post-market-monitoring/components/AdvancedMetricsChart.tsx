"use client";

import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, TrendingDown, Minus } from "lucide-react";

interface MetricDataPoint {
  date: string;
  value: number;
  baseline?: number;
  prediction?: number;
}

interface AdvancedMetricsChartProps {
  title: string;
  data: MetricDataPoint[];
  metricName: string;
  unit?: string;
}

export default function AdvancedMetricsChart({
  title,
  data,
  metricName,
  unit = "",
}: AdvancedMetricsChartProps) {
  if (!data || data.length === 0) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>{title}</CardTitle>
        </CardHeader>
        <CardBody>
          <p className="text-muted-foreground text-center py-8">No hay datos disponibles</p>
        </CardBody>
      </Card>
    );
  }

  // Calcular tendencia
  const firstValue = data[0]?.value || 0;
  const lastValue = data[data.length - 1]?.value || 0;
  const trend = lastValue > firstValue ? "up" : lastValue < firstValue ? "down" : "stable";
  const trendPercentage = firstValue !== 0
    ? ((lastValue - firstValue) / firstValue) * 100
    : 0;

  // Encontrar valores máximos para escalado
  const maxValue = Math.max(
    ...data.map(d => Math.max(d.value, d.baseline || 0, d.prediction || 0))
  );
  const minValue = Math.min(
    ...data.map(d => Math.min(d.value, d.baseline || Infinity, d.prediction || Infinity))
  );
  const range = maxValue - minValue || 1;

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>{title}</CardTitle>
          <div className="flex items-center gap-2">
            {trend === "up" && <TrendingUp className="w-4 h-4 text-green-500" />}
            {trend === "down" && <TrendingDown className="w-4 h-4 text-red-500" />}
            {trend === "stable" && <Minus className="w-4 h-4 text-gray-500" />}
            <span className={`text-sm font-semibold ${
              trend === "up" ? "text-green-500" :
              trend === "down" ? "text-red-500" :
              "text-gray-500"
            }`}>
              {trendPercentage > 0 ? "+" : ""}{trendPercentage.toFixed(1)}%
            </span>
          </div>
        </div>
      </CardHeader>
      <CardBody>
        <div className="space-y-4">
          {/* Gráfico simplificado (en producción usar librería como recharts) */}
          <div className="h-64 relative border rounded-lg p-4 bg-muted/30">
            <svg width="100%" height="100%" className="overflow-visible">
              {/* Eje Y */}
              <line
                x1="40"
                y1="10"
                x2="40"
                y2="240"
                stroke="currentColor"
                strokeWidth="1"
                className="text-border"
              />

              {/* Eje X */}
              <line
                x1="40"
                y1="240"
                x2="100%"
                y2="240"
                stroke="currentColor"
                strokeWidth="1"
                className="text-border"
              />

              {/* Baseline */}
              {data[0]?.baseline !== undefined && (
                <line
                  x1="40"
                  y1={240 - ((data[0].baseline - minValue) / range) * 220}
                  x2="100%"
                  y2={240 - ((data[0].baseline - minValue) / range) * 220}
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeDasharray="5,5"
                  className="text-blue-500"
                />
              )}

              {/* Línea de valores actuales */}
              <polyline
                points={data.map((d, i) => {
                  const x = 40 + (i * (100 - 40) / (data.length - 1 || 1));
                  const y = 240 - ((d.value - minValue) / range) * 220;
                  return `${x},${y}`;
                }).join(" ")}
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                className="text-primary"
              />

              {/* Línea de predicciones */}
              {data.some(d => d.prediction !== undefined) && (
                <polyline
                  points={data.map((d, i) => {
                    if (d.prediction === undefined) return "";
                    const x = 40 + (i * (100 - 40) / (data.length - 1 || 1));
                    const y = 240 - ((d.prediction - minValue) / range) * 220;
                    return `${x},${y}`;
                  }).filter(Boolean).join(" ")}
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeDasharray="3,3"
                  className="text-orange-500"
                />
              )}

              {/* Puntos de datos */}
              {data.map((d, i) => {
                const x = 40 + (i * (100 - 40) / (data.length - 1 || 1));
                const y = 240 - ((d.value - minValue) / range) * 220;
                return (
                  <circle
                    key={i}
                    cx={x}
                    cy={y}
                    r="3"
                    fill="currentColor"
                    className="text-primary"
                  />
                );
              })}
            </svg>

            {/* Leyenda */}
            <div className="absolute bottom-2 left-4 flex gap-4 text-xs">
              <div className="flex items-center gap-1">
                <div className="w-3 h-0.5 bg-primary"></div>
                <span>Actual</span>
              </div>
              {data[0]?.baseline !== undefined && (
                <div className="flex items-center gap-1">
                  <div className="w-3 h-0.5 bg-blue-500 border-dashed border-t-2"></div>
                  <span>Baseline</span>
                </div>
              )}
              {data.some(d => d.prediction !== undefined) && (
                <div className="flex items-center gap-1">
                  <div className="w-3 h-0.5 bg-orange-500 border-dashed border-t-2"></div>
                  <span>Predicción</span>
                </div>
              )}
            </div>
          </div>

          {/* Estadísticas */}
          <div className="grid grid-cols-3 gap-4 text-sm">
            <div>
              <p className="text-muted-foreground">Actual</p>
              <p className="font-semibold">
                {lastValue.toFixed(2)}{unit}
              </p>
            </div>
            {data[0]?.baseline !== undefined && (
              <div>
                <p className="text-muted-foreground">Baseline</p>
                <p className="font-semibold">
                  {data[0].baseline.toFixed(2)}{unit}
                </p>
              </div>
            )}
            {data[data.length - 1]?.prediction !== undefined && (
              <div>
                <p className="text-muted-foreground">Predicción</p>
                <p className="font-semibold text-orange-500">
                  {data[data.length - 1]?.prediction?.toFixed(2) || '0.00'}{unit}
                </p>
              </div>
            )}
          </div>
        </div>
      </CardBody>
    </Card>
  );
}
