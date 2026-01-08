'use client';

import React, { useState, useEffect } from 'react';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Progress } from '@/components/ui/progress';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { CircularProgress } from '@/components/ui/charts';
import { 
  Activity, 
  Clock, 
  CheckCircle, 
  XCircle, 
  AlertTriangle,
  Play,
  Square,
  Eye,
  Download,
  Settings,
  Zap,
  Target,
  Gauge
} from 'lucide-react';

interface TrainingProgressProps {
  jobId: string;
  jobName: string;
  status: string;
  progress: number;
  metrics?: {
    training_loss?: number;
    validation_loss?: number;
    accuracy?: number;
    f1_score?: number;
  };
  costs: {
    total_cost_usd: number;
    hours_used: number;
    estimated_remaining_cost_usd: number;
  };
  timeline: {
    created_at: string;
    started_at?: string;
    completed_at?: string;
    estimated_completion?: string;
  };
  onViewDetails: () => void;
  onCancel: () => void;
  onStart: () => void;
}

export const TrainingProgress: React.FC<TrainingProgressProps> = ({
  jobId,
  jobName,
  status,
  progress,
  metrics,
  costs,
  timeline,
  onViewDetails,
  onCancel,
  onStart
}) => {
  const [currentProgress, setCurrentProgress] = useState(progress);
  const [isRealTime, setIsRealTime] = useState(status === 'running');

  useEffect(() => {
    setCurrentProgress(progress);
  }, [progress]);

  useEffect(() => {
    if (status === 'running' && isRealTime) {
      const interval = setInterval(() => {
        setCurrentProgress(prev => {
          const newProgress = prev + Math.random() * 2;
          return Math.min(newProgress, 100);
        });
      }, 5000); // Actualizar cada 5 segundos

      return () => clearInterval(interval);
    }
  }, [status, isRealTime]);

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'running': return 'bg-green-100 text-green-800';
      case 'completed': return 'bg-blue-100 text-blue-800';
      case 'failed': return 'bg-red-100 text-red-800';
      case 'queued': return 'bg-yellow-100 text-yellow-800';
      case 'cancelled': return 'bg-gray-100 text-gray-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status.toLowerCase()) {
      case 'running': return <Activity className="h-4 w-4" />;
      case 'completed': return <CheckCircle className="h-4 w-4" />;
      case 'failed': return <XCircle className="h-4 w-4" />;
      case 'queued': return <Clock className="h-4 w-4" />;
      case 'cancelled': return <Square className="h-4 w-4" />;
      default: return <AlertTriangle className="h-4 w-4" />;
    }
  };

  const getEstimatedTime = () => {
    if (timeline.estimated_completion) {
      const now = new Date();
      const estimated = new Date(timeline.estimated_completion);
      const diff = estimated.getTime() - now.getTime();
      const hours = Math.floor(diff / (1000 * 60 * 60));
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
      
      if (hours > 0) {
        return `${hours}h ${minutes}m`;
      } else {
        return `${minutes}m`;
      }
    }
    return 'N/A';
  };

  return (
    <Card className="hover:shadow-md transition-shadow">
      <CardBody className="p-6">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-3">
            {getStatusIcon(status)}
            <div>
              <h3 className="text-lg font-semibold">{jobName}</h3>
              <p className="text-sm text-gray-500">ID: {jobId}</p>
            </div>
          </div>
          <Badge className={getStatusColor(status)}>
            {status}
          </Badge>
        </div>

        {/* Progreso Principal */}
        <div className="mb-6">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-gray-500">Progreso del Entrenamiento</span>
            <span className="text-sm font-medium">{currentProgress.toFixed(1)}%</span>
          </div>
          <Progress value={currentProgress} />
          
          {status === 'running' && (
            <div className="flex items-center space-x-2 mt-2">
              <Zap className="h-4 w-4 text-green-500 animate-pulse" />
              <span className="text-xs text-green-600">Actualizando en tiempo real</span>
            </div>
          )}
        </div>

        {/* Métricas de Rendimiento */}
        {metrics && Object.keys(metrics).length > 0 && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
            {metrics.accuracy && (
              <div className="text-center">
                <div className="text-2xl font-bold text-blue-600">
                  {(metrics.accuracy * 100).toFixed(1)}%
                </div>
                <div className="text-xs text-gray-500">Accuracy</div>
              </div>
            )}
            {metrics.f1_score && (
              <div className="text-center">
                <div className="text-2xl font-bold text-green-600">
                  {(metrics.f1_score * 100).toFixed(1)}%
                </div>
                <div className="text-xs text-gray-500">F1-Score</div>
              </div>
            )}
            {metrics.training_loss && (
              <div className="text-center">
                <div className="text-2xl font-bold text-orange-600">
                  {metrics.training_loss.toFixed(3)}
                </div>
                <div className="text-xs text-gray-500">Training Loss</div>
              </div>
            )}
            {metrics.validation_loss && (
              <div className="text-center">
                <div className="text-2xl font-bold text-purple-600">
                  {metrics.validation_loss.toFixed(3)}
                </div>
                <div className="text-xs text-gray-500">Validation Loss</div>
              </div>
            )}
          </div>
        )}

        {/* Información de Costes y Tiempo */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="text-center">
            <div className="text-lg font-bold text-green-600">
              ${costs.total_cost_usd.toFixed(2)}
            </div>
            <div className="text-xs text-gray-500">Coste Total</div>
          </div>
          <div className="text-center">
            <div className="text-lg font-bold text-blue-600">
              {costs.hours_used}h
            </div>
            <div className="text-xs text-gray-500">Horas Usadas</div>
          </div>
          <div className="text-center">
            <div className="text-lg font-bold text-orange-600">
              {getEstimatedTime()}
            </div>
            <div className="text-xs text-gray-500">Tiempo Restante</div>
          </div>
        </div>

        {/* Timeline */}
        <div className="mb-6">
          <h4 className="text-sm font-medium mb-3">Timeline</h4>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-500">Creado:</span>
              <span>{new Date(timeline.created_at).toLocaleString()}</span>
            </div>
            {timeline.started_at && (
              <div className="flex justify-between">
                <span className="text-gray-500">Iniciado:</span>
                <span>{new Date(timeline.started_at).toLocaleString()}</span>
              </div>
            )}
            {timeline.completed_at && (
              <div className="flex justify-between">
                <span className="text-gray-500">Completado:</span>
                <span>{new Date(timeline.completed_at).toLocaleString()}</span>
              </div>
            )}
            {timeline.estimated_completion && (
              <div className="flex justify-between">
                <span className="text-gray-500">Estimado:</span>
                <span>{new Date(timeline.estimated_completion).toLocaleString()}</span>
              </div>
            )}
          </div>
        </div>

        {/* Acciones */}
        <div className="flex space-x-2">
          <Button
            size="sm"
            onClick={onViewDetails}
            variant="outline"
            className="flex items-center space-x-1"
          >
            <Eye className="h-4 w-4" />
            <span>Ver Detalles</span>
          </Button>
          
          {status === 'queued' && (
            <Button
              size="sm"
              onClick={onStart}
              className="flex items-center space-x-1"
            >
              <Play className="h-4 w-4" />
              <span>Iniciar</span>
            </Button>
          )}
          
          {status === 'running' && (
            <Button
              size="sm"
              onClick={onCancel}
              variant="danger"
              className="flex items-center space-x-1"
            >
              <Square className="h-4 w-4" />
              <span>Cancelar</span>
            </Button>
          )}
          
          {status === 'completed' && (
            <Button
              size="sm"
              variant="outline"
              className="flex items-center space-x-1"
            >
              <Download className="h-4 w-4" />
              <span>Descargar</span>
            </Button>
          )}
        </div>
      </CardBody>
    </Card>
  );
};

// Componente para mostrar múltiples jobs en una vista de cuadrícula
interface TrainingJobsGridProps {
  jobs: Array<{
    job_id: string;
    name: string;
    status: string;
    progress: number;
    metrics?: any;
    costs: any;
    timeline: any;
  }>;
  onViewDetails: (jobId: string) => void;
  onCancel: (jobId: string) => void;
  onStart: (jobId: string) => void;
}

export const TrainingJobsGrid: React.FC<TrainingJobsGridProps> = ({
  jobs,
  onViewDetails,
  onCancel,
  onStart
}) => {
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      {jobs.map((job) => (
        <TrainingProgress
          key={job.job_id}
          jobId={job.job_id}
          jobName={job.name}
          status={job.status}
          progress={job.progress}
          metrics={job.metrics}
          costs={job.costs}
          timeline={job.timeline}
          onViewDetails={() => onViewDetails(job.job_id)}
          onCancel={() => onCancel(job.job_id)}
          onStart={() => onStart(job.job_id)}
        />
      ))}
    </div>
  );
}; 