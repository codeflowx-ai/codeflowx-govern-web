"use client";

import { useTranslation } from "@/app/config/i18n";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Activity,
  AlertTriangle,
  BarChart3,
  CheckCircle,
  Clock,
  Eye,
  RefreshCw,
  Settings,
  Shield,
  TrendingDown,
  TrendingUp,
  Users,
} from "lucide-react";
import { useEffect, useState } from "react";
import { governanceService } from "../governance/services/governanceService";
import { GovernanceMetricsData } from "../governance/types/governance";

export default function GovernanceDashboardPage() {
  const { t, language } = useTranslation();

  const [metrics, setMetrics] = useState<GovernanceMetricsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [showConfigModal, setShowConfigModal] = useState(false);

  useEffect(() => {
    loadMetrics();
  }, []);

  // Forzar re-render cuando cambie el idioma
  useEffect(() => {
    // El componente se re-renderizará automáticamente cuando cambie el idioma
    // porque el hook useTranslation actualiza el estado
  }, [language]);

  const loadMetrics = async () => {
    try {
      setLoading(true);
      const response = await governanceService.getMetrics();
      if (response.success) {
        setMetrics(response.metrics);
      }
    } catch (error) {
      console.error("Error loading governance metrics:", error);
    } finally {
      setLoading(false);
    }
  };

  const getRiskLevelColor = (level: string) => {
    switch (level) {
      case "LOW":
        return "bg-green-100 text-green-800";
      case "MEDIUM":
        return "bg-yellow-100 text-yellow-800";
      case "HIGH":
        return "bg-red-100 text-red-800";
      case "CRITICAL":
        return "bg-purple-100 text-purple-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getRiskLevelIcon = (level: string) => {
    switch (level) {
      case "LOW":
        return <CheckCircle className="h-4 w-4 text-green-500" />;
      case "MEDIUM":
        return <AlertTriangle className="h-4 w-4 text-yellow-500" />;
      case "HIGH":
        return <AlertTriangle className="h-4 w-4 text-red-500" />;
      case "CRITICAL":
        return <Shield className="h-4 w-4 text-purple-500" />;
      default:
        return <Clock className="h-4 w-4 text-gray-500" />;
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
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            {t("governance.dashboard.title")}
          </h1>
          <p className="text-gray-600">
            {t("governance.dashboard.subtitle")}
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            onClick={loadMetrics}
            variant="outline"
            className="flex items-center gap-1"
          >
            <RefreshCw className="h-4 w-4" />
            {t("governance.dashboard.refresh")}
          </Button>
          <Button
            className="flex items-center gap-1"
            onClick={() => setShowConfigModal(true)}
          >
            <Settings className="h-4 w-4" />
            {t("governance.dashboard.configure")}
          </Button>
        </div>
      </div>

      {/* Métricas Principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {t("governance.dashboard.autoApprovalRate")}
                </p>
                <p className="text-2xl font-bold text-gray-900">
                  {metrics?.autoApprovalRate.toFixed(1)}%
                </p>
              </div>
              <CheckCircle className="h-8 w-8 text-green-600" />
            </div>
            <div className="mt-4">
              <div className="flex items-center text-sm">
                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600">+5.2% {t("governance.dashboard.vsPreviousMonth")}</span>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {t("governance.dashboard.complianceScore")}
                </p>
                <p className="text-2xl font-bold text-gray-900">
                  {metrics?.complianceScore.toFixed(1)}%
                </p>
              </div>
              <Shield className="h-8 w-8 text-blue-600" />
            </div>
            <div className="mt-4">
              <div className="flex items-center text-sm">
                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600">+2.1% {t("governance.dashboard.vsPreviousMonth")}</span>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {t("governance.dashboard.averageRisk")}
                </p>
                <div className="flex items-center gap-2">
                  <p className="text-2xl font-bold text-gray-900">
                    {metrics?.averageRiskLevel}
                  </p>
                  {getRiskLevelIcon(metrics?.averageRiskLevel || "UNKNOWN")}
                </div>
              </div>
              <Activity className="h-8 w-8 text-purple-600" />
            </div>
            <div className="mt-4">
              <div className="flex items-center text-sm">
                <TrendingDown className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600">-10.5% {t("governance.dashboard.vsPreviousMonth")}</span>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">
                  {t("governance.dashboard.policiesApplied")}
                </p>
                <p className="text-2xl font-bold text-gray-900">
                  {metrics?.policiesApplied}
                </p>
              </div>
              <Settings className="h-8 w-8 text-orange-600" />
            </div>
            <div className="mt-4">
              <div className="flex items-center text-sm">
                <TrendingUp className="h-4 w-4 text-green-500 mr-1" />
                <span className="text-green-600">+15.3% {t("governance.dashboard.vsPreviousMonth")}</span>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Métricas Secundarias */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardBody className="p-6">
            <div className="text-center">
              <div className="text-3xl font-bold text-blue-600 mb-2">
                {metrics?.totalDecisions.toLocaleString()}
              </div>
              <div className="text-sm text-gray-600">{t("governance.dashboard.totalDecisions")}</div>
              <div className="mt-2">
                <Badge variant="outline" className="text-xs">
                  <Clock className="h-3 w-3 mr-1" />
                  {t("governance.dashboard.last24h")}
                </Badge>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-600 mb-2">
                {metrics?.humanReviewRequired}
              </div>
              <div className="text-sm text-gray-600">
                {t("governance.dashboard.humanReviewRequired")}
              </div>
              <div className="mt-2">
                <Badge variant="outline" className="text-xs">
                  <Users className="h-3 w-3 mr-1" />
                  {t("governance.dashboard.pending")}
                </Badge>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardBody className="p-6">
            <div className="text-center">
              <div className="text-3xl font-bold text-green-600 mb-2">
                {metrics?.lastUpdated
                  ? new Date(metrics.lastUpdated).toLocaleTimeString()
                  : "--:--"}
              </div>
              <div className="text-sm text-gray-600">{t("governance.dashboard.lastUpdate")}</div>
              <div className="mt-2">
                <Badge variant="outline" className="text-xs">
                  <Activity className="h-3 w-3 mr-1" />
                  {t("governance.dashboard.realTime")}
                </Badge>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Estado del Sistema */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Shield className="h-5 w-5" />
              {t("governance.dashboard.systemStatus")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
                <div className="flex items-center gap-2">
                  <CheckCircle className="h-4 w-4 text-green-500" />
                  <span className="font-medium text-green-800">
                    {t("governance.dashboard.autoApproval")}
                  </span>
                </div>
                <Badge className="bg-green-100 text-green-800">{t("governance.dashboard.active")}</Badge>
              </div>

              <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                <div className="flex items-center gap-2">
                  <Shield className="h-4 w-4 text-blue-500" />
                  <span className="font-medium text-blue-800">
                    {t("governance.dashboard.complianceMonitoring")}
                  </span>
                </div>
                <Badge className="bg-blue-100 text-blue-800">{t("governance.dashboard.active")}</Badge>
              </div>

              <div className="flex items-center justify-between p-3 bg-purple-50 rounded-lg">
                <div className="flex items-center gap-2">
                  <Activity className="h-4 w-4 text-purple-500" />
                  <span className="font-medium text-purple-800">
                    {t("governance.dashboard.riskAssessment")}
                  </span>
                </div>
                <Badge className="bg-purple-100 text-purple-800">{t("governance.dashboard.active")}</Badge>
              </div>

              <div className="flex items-center justify-between p-3 bg-orange-50 rounded-lg">
                <div className="flex items-center gap-2">
                  <Settings className="h-4 w-4 text-orange-500" />
                  <span className="font-medium text-orange-800">
                    {t("governance.dashboard.policyEnforcement")}
                  </span>
                </div>
                <Badge className="bg-orange-100 text-orange-800">{t("governance.dashboard.active")}</Badge>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <BarChart3 className="h-5 w-5" />
              {t("governance.dashboard.activitySummary")}
            </CardTitle>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">
                  {t("governance.dashboard.automaticDecisions")}
                </span>
                <span className="font-semibold text-green-600">
                  {Math.round(
                    ((metrics?.autoApprovalRate || 0) / 100) *
                      (metrics?.totalDecisions || 0)
                  )}
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-green-500 h-2 rounded-full"
                  style={{ width: `${metrics?.autoApprovalRate || 0}%` }}
                />
              </div>

              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">{t("governance.dashboard.humanReview")}</span>
                <span className="font-semibold text-yellow-600">
                  {metrics?.humanReviewRequired || 0}
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-yellow-500 h-2 rounded-full"
                  style={{
                    width: `${
                      ((metrics?.humanReviewRequired || 0) /
                        (metrics?.totalDecisions || 1)) *
                      100
                    }%`,
                  }}
                />
              </div>

              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">{t("governance.dashboard.complianceScore")}</span>
                <span className="font-semibold text-blue-600">
                  {metrics?.complianceScore.toFixed(1)}%
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-blue-500 h-2 rounded-full"
                  style={{ width: `${metrics?.complianceScore || 0}%` }}
                />
              </div>

              <div className="flex justify-between items-center">
                <span className="text-sm text-gray-600">{t("governance.dashboard.activePolicies")}</span>
                <span className="font-semibold text-orange-600">
                  {metrics?.policiesApplied || 0}
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-orange-500 h-2 rounded-full"
                  style={{
                    width: `${Math.min(
                      ((metrics?.policiesApplied || 0) / 200) * 100,
                      100
                    )}%`,
                  }}
                />
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Acciones Rápidas */}
      <Card>
        <CardHeader>
          <CardTitle>{t("governance.dashboard.quickActions")}</CardTitle>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <Button variant="outline" className="flex items-center gap-2">
              <Eye className="h-4 w-4" />
              {t("governance.dashboard.viewPendingDecisions")}
            </Button>
            <Button variant="outline" className="flex items-center gap-2">
              <Shield className="h-4 w-4" />
              {t("governance.dashboard.runComplianceCheck")}
            </Button>
            <Button variant="outline" className="flex items-center gap-2">
              <Activity className="h-4 w-4" />
              {t("governance.dashboard.evaluateRisks")}
            </Button>
            <Button variant="outline" className="flex items-center gap-2">
              <Settings className="h-4 w-4" />
              {t("governance.dashboard.managePolicies")}
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Modal de Configuración */}
      {showConfigModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold">
                {t("governance.dashboard.governanceConfig")}
              </h3>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setShowConfigModal(false)}
              >
                ✕
              </Button>
            </div>

            <div className="space-y-6">
              <div>
                <h4 className="font-semibold mb-3">{t("governance.dashboard.generalConfig")}</h4>
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.autoApprovalLabel")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="enabled">{t("governance.dashboard.enabled")}</option>
                      <option value="disabled">{t("governance.dashboard.disabled")}</option>
                    </select>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.confidenceThreshold")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="0.8">80%</option>
                      <option value="0.9">90%</option>
                      <option value="0.95">95%</option>
                    </select>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.notifications")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="all">{t("governance.dashboard.all")}</option>
                      <option value="critical">{t("governance.dashboard.criticalOnly")}</option>
                      <option value="none">{t("governance.dashboard.none")}</option>
                    </select>
                  </div>
                </div>
              </div>

              <div>
                <h4 className="font-semibold mb-3">
                  {t("governance.dashboard.complianceConfig")}
                </h4>
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.autoChecks")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="enabled">{t("governance.dashboard.enabledLabel")}</option>
                      <option value="disabled">{t("governance.dashboard.disabledLabel")}</option>
                    </select>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.evaluationFrequency")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="daily">{t("governance.dashboard.daily")}</option>
                      <option value="weekly">{t("governance.dashboard.weekly")}</option>
                      <option value="monthly">{t("governance.dashboard.monthly")}</option>
                    </select>
                  </div>
                </div>
              </div>

              <div>
                <h4 className="font-semibold mb-3">{t("governance.dashboard.riskConfig")}</h4>
                <div className="space-y-3">
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.autoEvaluation")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="enabled">{t("governance.dashboard.enabled")}</option>
                      <option value="disabled">{t("governance.dashboard.disabled")}</option>
                    </select>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">
                      {t("governance.dashboard.riskThreshold")}
                    </span>
                    <select className="px-3 py-1 border border-gray-300 rounded-md">
                      <option value="low">{t("governance.dashboard.low")}</option>
                      <option value="medium">{t("governance.dashboard.medium")}</option>
                      <option value="high">{t("governance.dashboard.high")}</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <div className="flex justify-end gap-2 mt-6">
              <Button
                variant="outline"
                onClick={() => setShowConfigModal(false)}
              >
                {t("governance.dashboard.cancel")}
              </Button>
              <Button
                onClick={() => {
                  // Aquí iría la lógica para guardar la configuración
                  setShowConfigModal(false);
                }}
              >
                {t("governance.dashboard.saveConfig")}
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
