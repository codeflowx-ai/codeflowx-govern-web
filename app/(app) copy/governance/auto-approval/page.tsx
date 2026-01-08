'use client';
import React, { useState, useEffect } from 'react';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import {
    CheckCircle,
    XCircle,
    Clock,
    Users,
    TrendingUp,
    TrendingDown,
    BarChart3,
    Eye,
    RefreshCw,
    AlertTriangle,
    Settings
} from 'lucide-react';
import { governanceService } from '../services/governanceService';
import { AutoApprovalData, Decision } from '../types/governance';
export default function AutoApprovalPage() {
    const [approvalData, setApprovalData] = useState<AutoApprovalData | null>(null);
    const [pendingDecisions, setPendingDecisions] = useState<Decision[]>([]);
    const [loading, setLoading] = useState(true);
    useEffect(() => {
        loadData();
    }, []);
    const loadData = async () => {
        try {
            setLoading(true);
            const [approvalResponse, decisionsResponse] = await Promise.all([
                governanceService.getAutoApprovalData(),
                governanceService.getPendingDecisions()
            ]);
            if (approvalResponse.success) {
                setApprovalData(approvalResponse.data);
            }
            if (decisionsResponse.success) {
                setPendingDecisions(decisionsResponse.decisions);
            }
        } catch (error) {
            console.error('Error loading auto approval data:', error);
        } finally {
            setLoading(false);
        }
    };
    const handleManualReview = async (decisionId: string) => {
        try {
            await governanceService.requireManualReview({
                decisionId,
                reason: 'Manual review requested by user'
            });
            await loadData(); // Reload data
        } catch (error) {
            console.error('Error requiring manual review:', error);
        }
    };
    const handleApprove = async (decisionId: string) => {
        try {
            await governanceService.approveDecision(decisionId, 'Approved by governance dashboard');
            await loadData(); // Reload data
        } catch (error) {
            console.error('Error approving decision:', error);
        }
    };
    const handleReject = async (decisionId: string) => {
        try {
            await governanceService.rejectDecision(decisionId, 'Rejected by governance dashboard');
            await loadData(); // Reload data
        } catch (error) {
            console.error('Error rejecting decision:', error);
        }
    };
    const getStatusIcon = (status: string) => {
        switch (status) {
            case 'approved': return <CheckCircle className="h-4 w-4 text-green-500" />;
            case 'rejected': return <XCircle className="h-4 w-4 text-red-500" />;
            case 'pending': return <Clock className="h-4 w-4 text-yellow-500" />;
            case 'human_review': return <Users className="h-4 w-4 text-blue-500" />;
            default: return <AlertTriangle className="h-4 w-4 text-gray-500" />;
        }
    };
    const getStatusColor = (status: string) => {
        switch (status) {
            case 'approved': return 'bg-green-100 text-green-800';
            case 'rejected': return 'bg-red-100 text-red-800';
            case 'pending': return 'bg-yellow-100 text-yellow-800';
            case 'human_review': return 'bg-blue-100 text-blue-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };
    const getStatusText = (status: string) => {
        switch (status) {
            case 'approved': return 'Aprobado';
            case 'rejected': return 'Rechazado';
            case 'pending': return 'Pendiente';
            case 'human_review': return 'Revisión Humana';
            default: return 'Desconocido';
        }
    };
    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
        );
    }
    return (
        <div className="w-full px-4 py-6 space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Centro de Aprobación Automática</h1>
                    <p className="text-gray-600">Gestiona las aprobaciones automáticas de modelos de IA</p>
                </div>
                <Button onClick={loadData} variant="outline" className="flex items-center gap-1">
                    <RefreshCw className="h-4 w-4" />
                    Actualizar
                </Button>
            </div>
            {/* Métricas de Aprobación */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Tasa de Aprobación</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {approvalData?.approvalRate.toFixed(1)}%
                                </p>
                            </div>
                            <CheckCircle className="h-8 w-8 text-green-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">+3.2% vs semana anterior</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>
                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Decisiones Automáticas</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {approvalData?.autoDecisions.toLocaleString()}
                                </p>
                            </div>
                            <BarChart3 className="h-8 w-8 text-blue-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">+12.5% vs semana anterior</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>
                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Revisión Humana Requerida</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {approvalData?.humanReviewRequired}
                                </p>
                            </div>
                            <Users className="h-8 w-8 text-yellow-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingDown className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">-8.1% vs semana anterior</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>
            </div>
            {/* Gráfico de Aprobaciones */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <BarChart3 className="h-5 w-5" />
                        Tasa de Aprobación (Últimos 7 días)
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="h-64 flex items-end justify-between gap-1">
                        {approvalData?.chartData.map((day, index) => {
                            const total = day.approved + day.rejected + day.pending;
                            const approvedHeight = total > 0 ? (day.approved / total) * 100 : 0;
                            const rejectedHeight = total > 0 ? (day.rejected / total) * 100 : 0;
                            const pendingHeight = total > 0 ? (day.pending / total) * 100 : 0;
                            return (
                                <div key={index} className="flex flex-col items-center flex-1">
                                    <div className="flex flex-col-reverse w-full h-full gap-1">
                                        <div
                                            className="w-full bg-green-500 rounded-t transition-all duration-300 hover:bg-green-600"
                                            style={{ height: `${approvedHeight}%` }}
                                            title={`${day.date}: ${day.approved} aprobados`}
                                        />
                                        <div
                                            className="w-full bg-red-500 rounded-t transition-all duration-300 hover:bg-red-600"
                                            style={{ height: `${rejectedHeight}%` }}
                                            title={`${day.date}: ${day.rejected} rechazados`}
                                        />
                                        <div
                                            className="w-full bg-yellow-500 rounded-t transition-all duration-300 hover:bg-yellow-600"
                                            style={{ height: `${pendingHeight}%` }}
                                            title={`${day.date}: ${day.pending} pendientes`}
                                        />
                                    </div>
                                    <span className="text-xs text-gray-500 mt-1">
                                        {new Date(day.date).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit' })}
                                    </span>
                                </div>
                            );
                        })}
                    </div>
                    <div className="mt-4 flex justify-center gap-6 text-sm">
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-green-500 rounded"></div>
                            <span>Aprobados</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-red-500 rounded"></div>
                            <span>Rechazados</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-yellow-500 rounded"></div>
                            <span>Pendientes</span>
                        </div>
                    </div>
                </CardBody>
            </Card>
            {/* Decisiones Pendientes */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Eye className="h-5 w-5" />
                        Decisiones Pendientes de Revisión ({pendingDecisions.length})
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="space-y-4">
                        {pendingDecisions.length === 0 ? (
                            <div className="text-center py-8">
                                <CheckCircle className="h-12 w-12 text-green-500 mx-auto mb-4" />
                                <h3 className="text-lg font-medium text-gray-900 mb-2">No hay decisiones pendientes</h3>
                                <p className="text-gray-600">Todas las decisiones han sido procesadas automáticamente</p>
                            </div>
                        ) : (
                            pendingDecisions.map((decision) => (
                                <div key={decision.id} className="border rounded-lg p-4 hover:bg-gray-50 transition-colors">
                                    <div className="flex items-center justify-between mb-3">
                                        <div className="flex items-center gap-3">
                                            {getStatusIcon(decision.status)}
                                            <div>
                                                <h4 className="font-semibold text-gray-900">{decision.modelName}</h4>
                                                <p className="text-sm text-gray-600">Versión {decision.version}</p>
                                            </div>
                                        </div>
                                        <Badge className={getStatusColor(decision.status)}>
                                            {getStatusText(decision.status)}
                                        </Badge>
                                    </div>
                                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-3">
                                        <div>
                                            <span className="text-sm text-gray-600">Confianza:</span>
                                            <span className="ml-2 font-medium">{decision.confidence.toFixed(1)}%</span>
                                        </div>
                                        <div>
                                            <span className="text-sm text-gray-600">Compliance Score:</span>
                                            <span className="ml-2 font-medium">{decision.complianceScore.toFixed(1)}%</span>
                                        </div>
                                        <div>
                                            <span className="text-sm text-gray-600">Nivel de Riesgo:</span>
                                            <span className="ml-2 font-medium">{decision.riskLevel}</span>
                                        </div>
                                    </div>
                                    <div className="mb-3">
                                        <span className="text-sm text-gray-600">Razón:</span>
                                        <p className="text-sm text-gray-900 mt-1">{decision.reason}</p>
                                    </div>
                                    <div className="flex items-center justify-between">
                                        <span className="text-xs text-gray-500">
                                            {new Date(decision.decisionTime).toLocaleString('es-ES')}
                                        </span>
                                        <div className="flex gap-2">
                                            {decision.status === 'human_review' && (
                                                <>
                                                    <Button
                                                        size="sm"
                                                        onClick={() => handleApprove(decision.id)}
                                                        className="flex items-center gap-1"
                                                    >
                                                        <CheckCircle className="h-3 w-3" />
                                                        Aprobar
                                                    </Button>
                                                    <Button
                                                        size="sm"
                                                        variant="outline"
                                                        onClick={() => handleReject(decision.id)}
                                                        className="flex items-center gap-1"
                                                    >
                                                        <XCircle className="h-3 w-3" />
                                                        Rechazar
                                                    </Button>
                                                </>
                                            )}
                                            {decision.status === 'pending' && (
                                                <Button
                                                    size="sm"
                                                    variant="outline"
                                                    onClick={() => handleManualReview(decision.id)}
                                                    className="flex items-center gap-1"
                                                >
                                                    <Users className="h-3 w-3" />
                                                    Revisión Manual
                                                </Button>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </CardBody>
            </Card>
            {/* Configuración del Sistema */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Settings className="h-5 w-5" />
                        Configuración del Sistema de Aprobación
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <h4 className="font-semibold mb-3">Umbrales de Aprobación</h4>
                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Confianza mínima para auto-aprobación:</span>
                                    <span className="font-medium">85%</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Compliance score mínimo:</span>
                                    <span className="font-medium">90%</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Nivel de riesgo máximo:</span>
                                    <span className="font-medium">MEDIUM</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <h4 className="font-semibold mb-3">Configuración de Escalación</h4>
                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Auto-escalación activada:</span>
                                    <Badge className="bg-green-100 text-green-800">Sí</Badge>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Tiempo máximo de espera:</span>
                                    <span className="font-medium">24 horas</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">Notificaciones automáticas:</span>
                                    <Badge className="bg-green-100 text-green-800">Activas</Badge>
                                </div>
                            </div>
                        </div>
                    </div>
                </CardBody>
            </Card>
        </div>
    );
}


