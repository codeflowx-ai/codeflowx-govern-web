'use client';

import React from 'react';
import { Card, CardBody, CardHeader, CardTitle, CardText } from './card';

// Componente de gráfico de líneas para métricas de entrenamiento
interface LineChartProps {
  data: Array<{
    label: string;
    value: number;
    color?: string;
  }>;
  title: string;
  description?: string;
  height?: number;
  showGrid?: boolean;
}

export const LineChart: React.FC<LineChartProps> = ({
  data,
  title,
  description,
  height = 200,
  showGrid = true
}) => {
  const maxValue = Math.max(...data.map(d => d.value));
  const minValue = Math.min(...data.map(d => d.value));
  const range = maxValue - minValue;

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        {description && <CardText>{description}</CardText>}
      </CardHeader>
      <CardBody>
        <div className="relative" style={{ height }}>
          <svg width="100%" height="100%" className="overflow-visible">
            {showGrid && (
              <defs>
                <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
                  <path d="M 40 0 L 0 0 0 40" fill="none" stroke="#f3f4f6" strokeWidth="1"/>
                </pattern>
              </defs>
            )}
            <rect width="100%" height="100%" fill={showGrid ? "url(#grid)" : "transparent"} />
            
            {/* Líneas de datos */}
            {data.map((point, index) => {
              const x = (index / (data.length - 1)) * 100;
              const y = range > 0 ? 100 - ((point.value - minValue) / range) * 100 : 50;
              
              return (
                <React.Fragment key={index}>
                  {/* Punto */}
                  <circle
                    cx={`${x}%`}
                    cy={`${y}%`}
                    r="4"
                    fill={point.color || "#3b82f6"}
                    stroke="white"
                    strokeWidth="2"
                  />
                  
                  {/* Línea conectando puntos */}
                  {index > 0 && (
                    <line
                      x1={`${((index - 1) / (data.length - 1)) * 100}%`}
                      y1={`${range > 0 ? 100 - ((data[index - 1].value - minValue) / range) * 100 : 50}%`}
                      x2={`${x}%`}
                      y2={`${y}%`}
                      stroke={point.color || "#3b82f6"}
                      strokeWidth="2"
                      fill="none"
                    />
                  )}
                </React.Fragment>
              );
            })}
          </svg>
          
          {/* Etiquetas de valores */}
          <div className="flex justify-between text-xs text-gray-500 mt-2">
            {data.map((point, index) => (
              <span key={index} className="text-center">
                {point.label}
              </span>
            ))}
          </div>
        </div>
      </CardBody>
    </Card>
  );
};

// Componente de gráfico de barras
interface BarChartProps {
  data: Array<{
    label: string;
    value: number;
    color?: string;
  }>;
  title: string;
  description?: string;
  height?: number;
  showValues?: boolean;
}

export const BarChart: React.FC<BarChartProps> = ({
  data,
  title,
  description,
  height = 200,
  showValues = true
}) => {
  const maxValue = Math.max(...data.map(d => d.value));

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        {description && <CardText>{description}</CardText>}
      </CardHeader>
      <CardBody>
        <div className="space-y-3" style={{ height }}>
          {data.map((item, index) => (
            <div key={index} className="flex items-center space-x-3">
              <div className="flex-1">
                <div className="flex items-center justify-between mb-1">
                  <span className="text-sm font-medium">{item.label}</span>
                  {showValues && (
                    <span className="text-sm text-gray-500">{item.value}</span>
                  )}
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className="h-2 rounded-full transition-all duration-300"
                    style={{
                      width: `${(item.value / maxValue) * 100}%`,
                      backgroundColor: item.color || "#3b82f6"
                    }}
                  />
                </div>
              </div>
            </div>
          ))}
        </div>
      </CardBody>
    </Card>
  );
};

// Componente de gráfico de dona
interface DonutChartProps {
  data: Array<{
    label: string;
    value: number;
    color: string;
  }>;
  title: string;
  description?: string;
  size?: number;
  showLegend?: boolean;
}

export const DonutChart: React.FC<DonutChartProps> = ({
  data,
  title,
  description,
  size = 200,
  showLegend = true
}) => {
  const total = data.reduce((sum, item) => sum + item.value, 0);
  let currentAngle = 0;

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        {description && <CardText>{description}</CardText>}
      </CardHeader>
      <CardBody>
        <div className="flex items-center justify-center">
          <div className="relative" style={{ width: size, height: size }}>
            <svg width={size} height={size} className="transform -rotate-90">
              {data.map((item, index) => {
                const percentage = item.value / total;
                const angle = percentage * 360;
                const startAngle = currentAngle;
                currentAngle += angle;

                const radius = size / 2 - 10;
                const startX = size / 2 + radius * Math.cos((startAngle * Math.PI) / 180);
                const startY = size / 2 + radius * Math.sin((startAngle * Math.PI) / 180);
                const endX = size / 2 + radius * Math.cos((currentAngle * Math.PI) / 180);
                const endY = size / 2 + radius * Math.sin((currentAngle * Math.PI) / 180);

                const largeArcFlag = angle > 180 ? 1 : 0;

                return (
                  <path
                    key={index}
                    d={`M ${startX} ${startY} A ${radius} ${radius} 0 ${largeArcFlag} 1 ${endX} ${endY}`}
                    fill="none"
                    stroke={item.color}
                    strokeWidth="20"
                    strokeLinecap="round"
                  />
                );
              })}
            </svg>
            
            {/* Texto central */}
            <div className="absolute inset-0 flex items-center justify-center">
              <div className="text-center">
                <div className="text-2xl font-bold">{total}</div>
                <div className="text-xs text-gray-500">Total</div>
              </div>
            </div>
          </div>
        </div>

        {showLegend && (
          <div className="mt-4 space-y-2">
            {data.map((item, index) => (
              <div key={index} className="flex items-center space-x-2">
                <div
                  className="w-3 h-3 rounded-full"
                  style={{ backgroundColor: item.color }}
                />
                <span className="text-sm">{item.label}</span>
                <span className="text-sm text-gray-500 ml-auto">
                  {((item.value / total) * 100).toFixed(1)}%
                </span>
              </div>
            ))}
          </div>
        )}
      </CardBody>
    </Card>
  );
};

// Componente de métricas con icono
interface MetricCardProps {
  title: string;
  value: string | number;
  description?: string;
  icon?: React.ReactNode;
  trend?: {
    value: number;
    isPositive: boolean;
  };
  color?: string;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  title,
  value,
  description,
  icon,
  trend,
  color = "blue"
}) => {
  const colorClasses = {
    blue: "text-blue-600",
    green: "text-green-600",
    red: "text-red-600",
    yellow: "text-yellow-600",
    purple: "text-purple-600"
  };

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
        {icon && <div className={colorClasses[color as keyof typeof colorClasses]}>{icon}</div>}
      </CardHeader>
      <CardBody>
        <div className="text-2xl font-bold">{value}</div>
        {description && (
          <p className="text-xs text-muted-foreground">{description}</p>
        )}
        {trend && (
          <div className="flex items-center space-x-1 mt-1">
            <span className={`text-xs ${trend.isPositive ? 'text-green-600' : 'text-red-600'}`}>
              {trend.isPositive ? '↗' : '↘'} {Math.abs(trend.value)}%
            </span>
            <span className="text-xs text-muted-foreground">vs mes anterior</span>
          </div>
        )}
      </CardBody>
    </Card>
  );
};

// Componente de progreso circular
interface CircularProgressProps {
  value: number;
  maxValue: number;
  size?: number;
  strokeWidth?: number;
  color?: string;
  showLabel?: boolean;
}

export const CircularProgress: React.FC<CircularProgressProps> = ({
  value,
  maxValue,
  size = 120,
  strokeWidth = 8,
  color = "#3b82f6",
  showLabel = true
}) => {
  const radius = (size - strokeWidth) / 2;
  const circumference = radius * 2 * Math.PI;
  const progress = (value / maxValue) * circumference;
  const strokeDasharray = `${progress} ${circumference}`;

  return (
    <div className="relative inline-flex items-center justify-center">
      <svg width={size} height={size} className="transform -rotate-90">
        {/* Fondo del círculo */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke="#e5e7eb"
          strokeWidth={strokeWidth}
          fill="none"
        />
        {/* Progreso */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke={color}
          strokeWidth={strokeWidth}
          fill="none"
          strokeDasharray={strokeDasharray}
          strokeLinecap="round"
          className="transition-all duration-300"
        />
      </svg>
      
      {showLabel && (
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="text-center">
            <div className="text-lg font-bold">{value}</div>
            <div className="text-xs text-gray-500">de {maxValue}</div>
          </div>
        </div>
      )}
    </div>
  );
}; 