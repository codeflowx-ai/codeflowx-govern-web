"use client";

import { Doughnut } from "react-chartjs-2";
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(ArcElement, Tooltip, Legend);

interface GaugeChartProps {
  value: number; // 0-100 o 0-1
  max?: number; // 100 por defecto
  label: string;
  meta?: number; // Meta objetivo
  criticalThreshold?: number; // Umbral crítico
  size?: "sm" | "md" | "lg";
  showValue?: boolean;
}

export function GaugeChart({
  value,
  max = 100,
  label,
  meta,
  criticalThreshold,
  size = "md",
  showValue = true,
}: GaugeChartProps) {
  const percentage = (value / max) * 100;
  const metaPercentage = meta ? (meta / max) * 100 : null;
  const criticalPercentage = criticalThreshold
    ? (criticalThreshold / max) * 100
    : null;

  // Determinar color según umbrales
  const getColor = () => {
    if (criticalPercentage && percentage < criticalPercentage) {
      return "#ef4444"; // Rojo
    }
    if (metaPercentage && percentage >= metaPercentage) {
      return "#22c55e"; // Verde
    }
    return "#eab308"; // Amarillo
  };

  const color = getColor();

  const sizeClasses = {
    sm: "w-24 h-24",
    md: "w-32 h-32",
    lg: "w-40 h-40",
  };

  const data = {
    datasets: [
      {
        data: [percentage, 100 - percentage],
        backgroundColor: [color, "#e5e7eb"],
        borderWidth: 0,
        cutout: "75%",
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        enabled: false,
      },
    },
  };

  return (
    <div className="flex flex-col items-center space-y-2">
      <div className={`relative ${sizeClasses[size]}`}>
        <Doughnut data={data} options={options} />
        {showValue && (
          <div className="absolute inset-0 flex items-center justify-center">
            <div className="text-center">
              <div
                className="text-2xl font-bold"
                style={{ color }}
              >
                {percentage.toFixed(1)}%
              </div>
              {meta && (
                <div className="text-xs text-muted-foreground">
                  Meta: {meta.toFixed(1)}
                </div>
              )}
            </div>
          </div>
        )}
      </div>
      <div className="text-sm font-medium text-center max-w-[120px]">
        {label}
      </div>
    </div>
  );
}


