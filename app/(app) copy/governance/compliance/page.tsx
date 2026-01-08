'use client';

import { useTranslation } from '@/app/config/i18n';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardBody, CardHeader, CardTitle } from '@/components/ui/card';
import {
  AlertTriangle,
  BarChart3,
  CheckCircle,
  Clock,
  Eye,
  Play,
  RefreshCw,
  Settings,
  Shield,
  TrendingUp,
  XCircle
} from 'lucide-react';
import { useEffect, useState } from 'react';
import { governanceService } from '../services/governanceService';
import { ComplianceCheck, ComplianceData } from '../types/governance';

export default function CompliancePage() {
    const { t } = useTranslation();
    const [complianceData, setComplianceData] = useState<ComplianceData | null>(null);
    const [activeChecks, setActiveChecks] = useState<ComplianceCheck[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            const [complianceResponse, checksResponse] = await Promise.all([
                governanceService.getComplianceData(),
                governanceService.getActiveComplianceChecks()
            ]);

            if (complianceResponse.success) {
                setComplianceData(complianceResponse.data);
            }

            if (checksResponse.success) {
                setActiveChecks(checksResponse.checks);
            }
        } catch (error) {
            console.error('Error loading compliance data:', error);
        } finally {
            setLoading(false);
        }
    };

    const runComplianceCheck = async (modelName: string, framework: string) => {
        try {
            await governanceService.runComplianceCheck({ modelName, framework });
            await loadData(); // Reload data
        } catch (error) {
            console.error('Error running compliance check:', error);
        }
    };

    const getStatusIcon = (status: string) => {
        switch (status) {
            case 'completed': return <CheckCircle className="h-4 w-4 text-green-500" />;
            case 'running': return <Clock className="h-4 w-4 text-blue-500" />;
            case 'failed': return <XCircle className="h-4 w-4 text-red-500" />;
            default: return <AlertTriangle className="h-4 w-4 text-gray-500" />;
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'completed': return 'bg-green-100 text-green-800';
            case 'running': return 'bg-blue-100 text-blue-800';
            case 'failed': return 'bg-red-100 text-red-800';
            default: return 'bg-gray-100 text-gray-800';
        }
    };

    const getStatusText = (status: string) => {
        switch (status) {
            case 'completed': return t('governance.compliance.statusText.completed');
            case 'running': return t('governance.compliance.statusText.running');
            case 'failed': return t('governance.compliance.statusText.failed');
            default: return t('governance.compliance.statusText.unknown');
        }
    };

    const getScoreColor = (score: number) => {
        if (score >= 90) return 'text-green-600';
        if (score >= 80) return 'text-yellow-600';
        return 'text-red-600';
    };

    const getScoreIcon = (score: number) => {
        if (score >= 90) return <CheckCircle className="h-4 w-4 text-green-500" />;
        if (score >= 80) return <AlertTriangle className="h-4 w-4 text-yellow-500" />;
        return <XCircle className="h-4 w-4 text-red-500" />;
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64">

                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">{t('governance.compliance.title')}</h1>
                    <p className="text-gray-600">{t('governance.compliance.subtitle')}</p>
                </div>
                <Button onClick={loadData} variant="outline" className="flex items-center gap-1">
                    <RefreshCw className="h-4 w-4" />
                    {t('governance.compliance.refresh')}
                </Button>
            </div>

            {/* Métricas de Compliance */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">{t('governance.compliance.generalScore')}</p>
                                <p className={`text-2xl font-bold ${getScoreColor((complianceData?.frameworkScores?.reduce((acc, f) => acc + f.score, 0) || 0) / (complianceData?.frameworkScores?.length || 1) || 0)}`}>
                                    {Math.round(((complianceData?.frameworkScores?.reduce((acc, f) => acc + f.score, 0) || 0) / (complianceData?.frameworkScores?.length || 1)) || 0)}%
                                </p>
                            </div>
                            <Shield className="h-8 w-8 text-blue-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">+2.1% {t('governance.compliance.vsPreviousMonth')}</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>

                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">{t('governance.compliance.totalChecks')}</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {complianceData?.totalChecks.toLocaleString()}
                                </p>
                            </div>
                            <BarChart3 className="h-8 w-8 text-purple-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">+15.3% {t('governance.compliance.vsPreviousMonth')}</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>

                <Card>
                    <CardBody className="p-6">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">{t('governance.compliance.successfulChecks')}</p>
                                <p className="text-2xl font-bold text-gray-900">
                                    {complianceData?.compliantChecks.toLocaleString()}
                                </p>
                            </div>
                            <CheckCircle className="h-8 w-8 text-green-600" />
                        </div>
                        <div className="mt-4">
                            <div className="flex items-center text-sm">
                                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                                <span className="text-green-600">+8.7% {t('governance.compliance.vsPreviousMonth')}</span>
                            </div>
                        </div>
                    </CardBody>
                </Card>
            </div>

            {/* Scores por Framework */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Shield className="h-5 w-5" />
                        {t('governance.compliance.scoresByFramework')}
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="space-y-4">
                        {complianceData?.frameworkScores.map((framework) => (
                            <div key={framework.name} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                                <div className="flex items-center gap-3">
                                    {getScoreIcon(framework.score)}
                                    <div>
                                        <h4 className="font-semibold text-gray-900">{framework.name}</h4>
                                        <p className="text-sm text-gray-600">{t('governance.compliance.frameworkCompliance')}</p>
                                    </div>
                                </div>
                                <div className="flex items-center gap-4">
                                    <div className="text-right">
                                        <div className={`text-2xl font-bold ${getScoreColor(framework.score)}`}>
                                            {framework.score}%
                                        </div>
                                        <div className="text-sm text-gray-600">{t('governance.compliance.frameworks.score')}</div>
                                    </div>
                                    <Badge className={getStatusColor(framework.status)}>
                                        {framework.status === 'success' ? t('governance.compliance.status.success') :
                                         framework.status === 'warning' ? t('governance.compliance.status.warning') : t('governance.compliance.status.error')}
                                    </Badge>
                                </div>
                            </div>
                        ))}
                    </div>
                </CardBody>
            </Card>

            {/* Gráfico de Compliance */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <BarChart3 className="h-5 w-5" />
                        {t('governance.compliance.trends.title')}
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="h-64 flex items-end justify-between gap-1">
                        {complianceData?.chartData.map((day, index) => (
                            <div key={index} className="flex flex-col items-center flex-1">
                                <div className="flex flex-col-reverse w-full h-full gap-1">
                                    <div
                                        className="w-full bg-blue-500 rounded-t transition-all duration-300 hover:bg-blue-600"
                                        style={{ height: `${day.gdpr}%` }}
                                        title={`${day.date}: GDPR ${day.gdpr}%`}
                                    />
                                    <div
                                        className="w-full bg-green-500 rounded-t transition-all duration-300 hover:bg-green-600"
                                        style={{ height: `${day.sox}%` }}
                                        title={`${day.date}: SOX ${day.sox}%`}
                                    />
                                    <div
                                        className="w-full bg-purple-500 rounded-t transition-all duration-300 hover:bg-purple-600"
                                        style={{ height: `${day.hipaa}%` }}
                                        title={`${day.date}: HIPAA ${day.hipaa}%`}
                                    />
                                </div>
                                <span className="text-xs text-gray-500 mt-1">
                                    {new Date(day.date).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit' })}
                                </span>
                            </div>
                        ))}
                    </div>
                    <div className="mt-4 flex justify-center gap-6 text-sm">
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-blue-500 rounded"></div>
                            <span>GDPR</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-green-500 rounded"></div>
                            <span>SOX</span>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="w-3 h-3 bg-purple-500 rounded"></div>
                            <span>HIPAA</span>
                        </div>
                    </div>
                </CardBody>
            </Card>

            {/* Verificaciones Activas */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Eye className="h-5 w-5" />
                        {t('governance.compliance.activeChecks.title')} ({activeChecks.length})
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="space-y-4">
                        {activeChecks.length === 0 ? (
                            <div className="text-center py-8">
                                <CheckCircle className="h-12 w-12 text-green-500 mx-auto mb-4" />
                                <h3 className="text-lg font-medium text-gray-900 mb-2">{t('governance.compliance.activeChecks.empty')}</h3>
                                <p className="text-gray-600">{t('governance.compliance.activeChecks.emptyDescription')}</p>
                            </div>
                        ) : (
                            activeChecks.map((check) => (
                                <div key={check.id} className="border rounded-lg p-4 hover:bg-gray-50 transition-colors">
                                    <div className="flex items-center justify-between mb-3">
                                        <div className="flex items-center gap-3">
                                            {getStatusIcon(check.status)}
                                            <div>
                                                <h4 className="font-semibold text-gray-900">{check.modelName}</h4>
                                                <p className="text-sm text-gray-600">{t('governance.compliance.activeChecks.framework')}: {check.framework}</p>
                                            </div>
                                        </div>
                                        <Badge className={getStatusColor(check.status)}>
                                            {getStatusText(check.status)}
                                        </Badge>
                                    </div>

                                    <div className="mb-3">
                                        <div className="flex justify-between items-center mb-1">
                                            <span className="text-sm text-gray-600">{t('governance.compliance.progress')}</span>
                                            <span className="text-sm font-medium">{check.progress}%</span>
                                        </div>
                                        <div className="w-full bg-gray-200 rounded-full h-2">
                                            <div
                                                className={`h-2 rounded-full transition-all duration-300 ${
                                                    check.status === 'completed' ? 'bg-green-500' :
                                                    check.status === 'running' ? 'bg-blue-500' : 'bg-red-500'
                                                }`}
                                                style={{ width: `${check.progress}%` }}
                                            />
                                        </div>
                                    </div>

                                    <div className="flex items-center justify-between text-sm text-gray-500">
                                        <span>{t('governance.compliance.started')}: {new Date(check.startTime).toLocaleString()}</span>
                                        {check.endTime && (
                                            <span>{t('governance.compliance.completed')}: {new Date(check.endTime).toLocaleString()}</span>
                                        )}
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </CardBody>
            </Card>

            {/* Acciones de Compliance */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Settings className="h-5 w-5" />
                        {t('governance.compliance.actions.title')}
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        <Button
                            onClick={() => runComplianceCheck('all', 'GDPR')}
                            className="flex items-center gap-2"
                        >
                            <Play className="h-4 w-4" />
                            {t('governance.compliance.actions.verifyGDPR')}
                        </Button>
                        <Button
                            onClick={() => runComplianceCheck('all', 'SOX')}
                            variant="outline"
                            className="flex items-center gap-2"
                        >
                            <Play className="h-4 w-4" />
                            {t('governance.compliance.actions.verifySOX')}
                        </Button>
                        <Button
                            onClick={() => runComplianceCheck('all', 'HIPAA')}
                            variant="outline"
                            className="flex items-center gap-2"
                        >
                            <Play className="h-4 w-4" />
                            {t('governance.compliance.actions.verifyHIPAA')}
                        </Button>
                    </div>
                </CardBody>
            </Card>

            {/* Configuración de Compliance */}
            <Card>
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                        <Settings className="h-5 w-5" />
                        {t('governance.compliance.configuration.title')}
                    </CardTitle>
                </CardHeader>
                <CardBody>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <div>
                            <h4 className="font-semibold mb-3">{t('governance.compliance.configuration.thresholds')}</h4>
                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.minScoreGDPR')}:</span>
                                    <span className="font-medium">95%</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.minScoreSOX')}:</span>
                                    <span className="font-medium">90%</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.minScoreHIPAA')}:</span>
                                    <span className="font-medium">92%</span>
                                </div>
                            </div>
                        </div>
                        <div>
                            <h4 className="font-semibold mb-3">{t('governance.compliance.configuration.verificationSettings')}</h4>
                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.autoVerifications')}:</span>
                                    <Badge className="bg-green-100 text-green-800">{t('governance.compliance.configuration.active')}</Badge>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.verificationFrequency')}:</span>
                                    <span className="font-medium">{t('governance.compliance.configuration.daily')}</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-sm text-gray-600">{t('governance.compliance.configuration.alertNotifications')}:</span>
                                    <Badge className="bg-green-100 text-green-800">{t('governance.compliance.configuration.active')}</Badge>
                                </div>
                            </div>
                        </div>
                    </div>
                </CardBody>
            </Card>
        </div>
    );
}


