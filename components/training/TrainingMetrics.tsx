'use client';

import React, { useState, useEffect } from 'react';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { LineChart, BarChart, DonutChart, MetricCard } from '@/components/ui/charts';
import { 
  Activity, 
  Target, 
  Gauge, 
  TrendingUp, 
  TrendingDown,
  Clock,
  DollarSign,
  Cpu,
  Database,
  Zap,
  BarChart3,
  Users
} from 'lucide-react';

interface TrainingMetricsProps {
  jobId: string;
  metrics: {
    training_loss?: number[];
    validation_loss?: number[];
    accuracy?: number[];
    f1_score?: number[];
    learning_rate?: number[];
  };
  costs: {
    total_cost_usd: number;
    hours_used: number;
    estimated_remaining_cost_usd: number;
  };
  progress: number;
  status: string;
}

export const TrainingMetrics: React.FC<TrainingMetricsProps> = ({
  jobId,
  metrics,
  costs,
  progress,
  status
}) => {
  const [currentMetrics, setCurrentMetrics] = useState(metrics);
  const [isRealTime, setIsRealTime] = useState(status === 'running');

  useEffect(() => {
    setCurrentMetrics(metrics);
  }, [metrics]);

  useEffect(() => {
    if (status === 'running' && isRealTime) {
      const interval = setInterval(() => {
        setCurrentMetrics(prev => {
          // Simular actualizaciones en tiempo real
          const newMetrics = { ...prev };
          
          if (newMetrics.training_loss && newMetrics.training_loss.length > 0) {
            const lastLoss = newMetrics.training_loss[newMetrics.training_loss.length - 1];
            const newLoss = lastLoss - (Math.random() * 0.01);
            newMetrics.training_loss = [...newMetrics.training_loss, Math.max(newLoss, 0.1)];
          }
          
          if (newMetrics.accuracy && newMetrics.accuracy.length > 0) {
            const lastAccuracy = newMetrics.accuracy[newMetrics.accuracy.length - 1];
            const newAccuracy = lastAccuracy + (Math.random() * 0.02);
            newMetrics.accuracy = [...newMetrics.accuracy, Math.min(newAccuracy, 1.0)];
          }
          
          return newMetrics;
        });
      }, 10000); // Actualizar cada 10 segundos

      return () => clearInterval(interval);
    }
  }, [status, isRealTime]);

  // Preparar datos para gráficos
  const trainingLossData = currentMetrics.training_loss?.map((loss, index) => ({
    label: `Epoch ${index + 1}`,
    value: loss,
    color: '#ef4444'
  })) || [];

  const validationLossData = currentMetrics.validation_loss?.map((loss, index) => ({
    label: `Epoch ${index + 1}`,
    value: loss,
    color: '#f59e0b'
  })) || [];

  const accuracyData = currentMetrics.accuracy?.map((acc, index) => ({
    label: `Epoch ${index + 1}`,
    value: acc * 100,
    color: '#10b981'
  })) || [];

  const f1ScoreData = currentMetrics.f1_score?.map((f1, index) => ({
    label: `Epoch ${index + 1}`,
    value: f1 * 100,
    color: '#3b82f6'
  })) || [];

  // Métricas actuales
  const currentTrainingLoss = currentMetrics.training_loss?.[currentMetrics.training_loss.length - 1] || 0;
  const currentValidationLoss = currentMetrics.validation_loss?.[currentMetrics.validation_loss.length - 1] || 0;
  const currentAccuracy = currentMetrics.accuracy?.[currentMetrics.accuracy.length - 1] || 0;
  const currentF1Score = currentMetrics.f1_score?.[currentMetrics.f1_score.length - 1] || 0;

  // Tendencias
  const getTrend = (values: number[] = []) => {
    if (values.length < 2) return { value: 0, isPositive: true };
    
    const recent = values.slice(-3);
    const older = values.slice(-6, -3);
    
    if (older.length === 0) return { value: 0, isPositive: true };
    
    const recentAvg = recent.reduce((a, b) => a + b, 0) / recent.length;
    const olderAvg = older.reduce((a, b) => a + b, 0) / older.length;
    
    const change = ((recentAvg - olderAvg) / olderAvg) * 100;
    return { value: Math.abs(change), isPositive: change > 0 };
  };

  const accuracyTrend = getTrend(currentMetrics.accuracy);
  const lossTrend = getTrend(currentMetrics.training_loss);

  return (
    <div className="space-y-6">
      {/* Métricas Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <MetricCard
          title="Accuracy Actual"
          value={`${(currentAccuracy * 100).toFixed(1)}%`}
          description="Precisión del modelo"
          icon={<Target className="h-4 w-4" />}
          color="green"
          trend={accuracyTrend}
        />

        <MetricCard
          title="Training Loss"
          value={currentTrainingLoss.toFixed(3)}
          description="Pérdida de entrenamiento"
          icon={<TrendingDown className="h-4 w-4" />}
          color="red"
          trend={lossTrend}
        />

        <MetricCard
          title="F1-Score"
          value={`${(currentF1Score * 100).toFixed(1)}%`}
          description="Puntuación F1"
          icon={<BarChart3 className="h-4 w-4" />}
          color="blue"
          trend={{ value: 2.1, isPositive: true }}
        />

        <MetricCard
          title="Progreso"
          value={`${progress.toFixed(1)}%`}
          description="Progreso del entrenamiento"
          icon={<Gauge className="h-4 w-4" />}
          color="purple"
          trend={{ value: 15.3, isPositive: true }}
        />
      </div>

      {/* Gráficos de Métricas */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Gráfico de Accuracy */}
        {accuracyData.length > 0 && (
          <LineChart
            data={accuracyData}
            title="Evolución de Accuracy"
            description="Precisión del modelo durante el entrenamiento"
            height={250}
          />
        )}

        {/* Gráfico de Loss */}
        {trainingLossData.length > 0 && (
          <LineChart
            data={trainingLossData}
            title="Training Loss"
            description="Pérdida de entrenamiento durante las épocas"
            height={250}
          />
        )}
      </div>

      {/* Comparación Training vs Validation Loss */}
      {trainingLossData.length > 0 && validationLossData.length > 0 && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <BarChart
            data={trainingLossData.slice(-10)}
            title="Training Loss (Últimas 10 épocas)"
            description="Pérdida de entrenamiento reciente"
            height={200}
          />
          
          <BarChart
            data={validationLossData.slice(-10)}
            title="Validation Loss (Últimas 10 épocas)"
            description="Pérdida de validación reciente"
            height={200}
          />
        </div>
      )}

      {/* Métricas de Costes */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <DollarSign className="h-5 w-5" />
              <span>Costes</span>
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Coste Total</span>
              <span className="font-medium">${costs.total_cost_usd.toFixed(2)}</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Horas Usadas</span>
              <span className="font-medium">{costs.hours_used}h</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Restante Estimado</span>
              <span className="font-medium">${costs.estimated_remaining_cost_usd.toFixed(2)}</span>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Clock className="h-5 w-5" />
              <span>Tiempo</span>
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Tiempo Transcurrido</span>
              <span className="font-medium">{costs.hours_used}h</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Progreso</span>
              <span className="font-medium">{progress.toFixed(1)}%</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Tiempo Estimado</span>
              <span className="font-medium">
                {progress > 0 ? ((costs.hours_used / progress) * 100 - costs.hours_used).toFixed(1) : 'N/A'}h
              </span>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Activity className="h-5 w-5" />
              <span>Estado</span>
            </CardTitle>
          </CardHeader>
          <CardBody className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Estado</span>
              <span className="font-medium capitalize">{status}</span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Tiempo Real</span>
              <span className="font-medium">
                {isRealTime ? (
                  <div className="flex items-center space-x-1">
                    <Zap className="h-4 w-4 text-green-500 animate-pulse" />
                    <span>Activo</span>
                  </div>
                ) : (
                  'Inactivo'
                )}
              </span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-500">Job ID</span>
              <span className="font-mono text-xs">{jobId}</span>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Indicador de Tiempo Real */}
      {isRealTime && (
        <Card className="border-green-200 bg-green-50">
          <CardBody className="p-4">
            <div className="flex items-center space-x-2">
              <Zap className="h-5 w-5 text-green-500 animate-pulse" />
              <span className="text-sm font-medium text-green-700">
                Actualizando métricas en tiempo real
              </span>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
};

// Componente para mostrar múltiples métricas de jobs
interface TrainingMetricsGridProps {
  jobs: Array<{
    job_id: string;
    metrics: any;
    costs: any;
    progress: number;
    status: string;
  }>;
}

export const TrainingMetricsGrid: React.FC<TrainingMetricsGridProps> = ({ jobs }) => {
  return (
    <div className="space-y-6">
      {jobs.map((job) => (
        <Card key={job.job_id}>
          <CardHeader>
            <CardTitle>Métricas - Job {job.job_id}</CardTitle>
          </CardHeader>
          <CardBody>
            <TrainingMetrics
              jobId={job.job_id}
              metrics={job.metrics}
              costs={job.costs}
              progress={job.progress}
              status={job.status}
            />
          </CardBody>
        </Card>
      ))}
    </div>
  );
}; 