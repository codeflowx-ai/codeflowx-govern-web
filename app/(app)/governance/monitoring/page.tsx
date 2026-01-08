"use client";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import {
  Activity,
  AlertTriangle,
  CheckCircle,
  Clock,
  Eye,
  RefreshCw,
  Settings,
  Shield,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import Link from "next/link";
import { useEffect, useState } from "react";
import {
  mockComplianceMetrics,
  mockPolicyViolations,
  mockRiskIndicators,
} from "./_mock";
export default function GovernanceMonitoring() {
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [selectedRiskLevel, setSelectedRiskLevel] = useState("all");
  const [selectedViolationStatus, setSelectedViolationStatus] = useState("all");
  const [autoRefresh, setAutoRefresh] = useState(true);
  useEffect(() => {
    if (autoRefresh) {
      const interval = setInterval(() => {
        console.log("Actualizando métricas de gobernanza...");
      }, 60000); // 1 minuto
      return () => clearInterval(interval);
    }
  }, [autoRefresh]);
  const getStatusColor = (status: string) => {
    switch (status) {
      case "compliant":
        return "bg-green-100 text-green-800";
      case "at-risk":
        return "bg-yellow-100 text-yellow-800";
      case "non-compliant":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getMetricStatusLabel = (status: string) => {
    switch (status) {
      case "compliant":
        return "Cumple";
      case "at-risk":
        return "En Riesgo";
      case "non-compliant":
        return "No Cumple";
      default:
        return status;
    }
  };
  const getRiskLevelColor = (level: string) => {
    switch (level) {
      case "low":
        return "bg-green-100 text-green-800";
      case "medium":
        return "bg-yellow-100 text-yellow-800";
      case "high":
        return "bg-orange-100 text-orange-800";
      case "critical":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getRiskLevelLabel = (level: string) => {
    switch (level) {
      case "low":
        return "Bajo";
      case "medium":
        return "Medio";
      case "high":
        return "Alto";
      case "critical":
        return "Crítico";
      default:
        return level;
    }
  };
  const getMitigationStatusLabel = (status: string) => {
    switch (status) {
      case "none":
        return "Ninguna";
      case "planned":
        return "Planificada";
      case "in-progress":
        return "En Progreso";
      case "completed":
        return "Completada";
      default:
        return status;
    }
  };
  const getTrendLabel = (trend: string) => {
    switch (trend) {
      case "up":
        return "Mejorando";
      case "down":
        return "Empeorando";
      case "stable":
        return "Estable";
      default:
        return trend;
    }
  };
  const getViolationSeverityColor = (severity: string) => {
    switch (severity) {
      case "low":
        return "bg-blue-100 text-blue-800";
      case "medium":
        return "bg-yellow-100 text-yellow-800";
      case "high":
        return "bg-orange-100 text-orange-800";
      case "critical":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getViolationSeverityLabel = (severity: string) => {
    switch (severity) {
      case "low":
        return "Baja";
      case "medium":
        return "Media";
      case "high":
        return "Alta";
      case "critical":
        return "Crítica";
      default:
        return severity;
    }
  };
  const getViolationStatusColor = (status: string) => {
    switch (status) {
      case "open":
        return "bg-red-100 text-red-800";
      case "investigating":
        return "bg-yellow-100 text-yellow-800";
      case "resolved":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getViolationStatusLabel = (status: string) => {
    switch (status) {
      case "open":
        return "Abierta";
      case "investigating":
        return "En Investigación";
      case "resolved":
        return "Resuelta";
      default:
        return status;
    }
  };
  const filteredComplianceMetrics =
    selectedCategory === "all"
      ? mockComplianceMetrics
      : mockComplianceMetrics.filter(
        (metric) => metric.category === selectedCategory
      );
  const filteredRiskIndicators =
    selectedRiskLevel === "all"
      ? mockRiskIndicators
      : mockRiskIndicators.filter(
        (risk) => risk.riskLevel === selectedRiskLevel
      );
  const filteredPolicyViolations =
    selectedViolationStatus === "all"
      ? mockPolicyViolations
      : mockPolicyViolations.filter(
        (violation) => violation.status === selectedViolationStatus
      );
  const overallCompliance = Math.round(
    mockComplianceMetrics.reduce(
      (sum, metric) => sum + metric.currentValue,
      0
    ) / mockComplianceMetrics.length
  );
  const criticalRisks = mockRiskIndicators.filter(
    (r) => r.riskLevel === "critical"
  ).length;
  const openViolations = mockPolicyViolations.filter(
    (v) => v.status === "open"
  ).length;
  const resolvedViolations = mockPolicyViolations.filter(
    (v) => v.status === "resolved"
  ).length;
  return (
    <div className="w-full p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Monitoreo de Gobernanza
          </h1>
          <p className="text-gray-600 mt-2">
            Supervisa el cumplimiento, riesgos y violaciones de políticas en
            tiempo real
          </p>
        </div>
        <div className="flex items-center space-x-3">
          <Button
            variant="outline"
            onClick={() => console.log("Refrescando métricas...")}
            className="flex items-center space-x-2"
          >
            <RefreshCw className="h-4 w-4" />
            <span>Actualizar</span>
          </Button>
          <Button
            variant={autoRefresh ? "primary" : "outline"}
            onClick={() => setAutoRefresh(!autoRefresh)}
            className="flex items-center space-x-2"
          >
            <Clock className="h-4 w-4" />
            <span>Auto-refresh</span>
          </Button>
        </div>
      </div>
      {/* Métricas de resumen */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardBody className="flex items-center space-x-3">
            <Shield className="h-8 w-8 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Cumplimiento General</p>
              <p className="text-2xl font-bold text-blue-600">
                {overallCompliance}%
              </p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <AlertTriangle className="h-8 w-8 text-red-600" />
            <div>
              <p className="text-sm text-gray-600">Riesgos Críticos</p>
              <p className="text-2xl font-bold text-red-600">{criticalRisks}</p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <Eye className="h-8 w-8 text-yellow-600" />
            <div>
              <p className="text-sm text-gray-600">Violaciones Abiertas</p>
              <p className="text-2xl font-bold text-yellow-600">
                {openViolations}
              </p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <CheckCircle className="h-8 w-8 text-green-600" />
            <div>
              <p className="text-sm text-gray-600">Violaciones Resueltas</p>
              <p className="text-2xl font-bold text-green-600">
                {resolvedViolations}
              </p>
            </div>
          </CardBody>
        </Card>
      </div>
      {/* Filtros */}
      <div className="flex flex-wrap items-center gap-4">
        <span className="text-sm font-medium text-gray-700">Filtros:</span>
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">Todas las categorías</option>
          <option value="Seguridad">Seguridad</option>
          <option value="Privacidad">Privacidad</option>
          <option value="Financiero">Financiero</option>
          <option value="Seguridad de la Información">
            Seguridad de la Información
          </option>
        </select>
        <select
          value={selectedRiskLevel}
          onChange={(e) => setSelectedRiskLevel(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">Todos los niveles de riesgo</option>
          <option value="low">Bajo</option>
          <option value="medium">Medio</option>
          <option value="high">Alto</option>
          <option value="critical">Crítico</option>
        </select>
        <select
          value={selectedViolationStatus}
          onChange={(e) => setSelectedViolationStatus(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">Todos los estados</option>
          <option value="open">Abiertas</option>
          <option value="investigating">En Investigación</option>
          <option value="resolved">Resueltas</option>
        </select>
      </div>
      {/* Métricas de Cumplimiento */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Métricas de Cumplimiento
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {filteredComplianceMetrics.map((metric) => (
            <Card key={metric.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{metric.name}</CardTitle>
                  <Badge className={getStatusColor(metric.status)}>
                    {getMetricStatusLabel(metric.status)}
                  </Badge>
                </div>
                <p className="text-sm text-gray-600">{metric.category}</p>
              </CardHeader>
              <CardBody className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Cumplimiento</span>
                    <span className="font-medium">{metric.currentValue}%</span>
                  </div>
                  <Progress value={metric.currentValue} className="h-2" />
                  <div className="flex items-center justify-between text-sm text-gray-500">
                    <span>Meta: {metric.targetValue}%</span>
                    <span>Actualizado: {metric.lastUpdated}</span>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  {metric.trend === "up" && (
                    <TrendingUp className="h-4 w-4 text-green-500" />
                  )}
                  {metric.trend === "down" && (
                    <TrendingDown className="h-4 w-4 text-red-500" />
                  )}
                  {metric.trend === "stable" && (
                    <Activity className="h-4 w-4 text-gray-500" />
                  )}
                  <span className="text-sm text-gray-600">
                    {getTrendLabel(metric.trend)}
                  </span>
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      </div>
      {/* Indicadores de Riesgo */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Indicadores de Riesgo
        </h2>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {filteredRiskIndicators.map((risk) => (
            <Card key={risk.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{risk.name}</CardTitle>
                  <Badge className={getRiskLevelColor(risk.riskLevel)}>
                    {getRiskLevelLabel(risk.riskLevel)}
                  </Badge>
                </div>
              </CardHeader>
              <CardBody className="space-y-4">
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span className="text-gray-600">Probabilidad:</span>
                    <span className="ml-2 font-medium">
                      {risk.probability}%
                    </span>
                  </div>
                  <div>
                    <span className="text-gray-600">Impacto:</span>
                    <span className="ml-2 font-medium">{risk.impact}%</span>
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Estado de Mitigación</span>
                    <Badge variant="outline" className="text-xs">
                      {getMitigationStatusLabel(risk.mitigationStatus)}
                    </Badge>
                  </div>
                </div>
                <div className="space-y-2 text-sm text-gray-500">
                  <div className="flex justify-between">
                    <span>Última evaluación:</span>
                    <span>{risk.lastAssessment}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Próxima evaluación:</span>
                    <span>{risk.nextAssessment}</span>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <Link
                    href={`/governance/monitoring/risks/${risk.id}`}
                    className="flex-1"
                  >
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="h-4 w-4 mr-2" />
                      Ver Detalles
                    </Button>
                  </Link>
                  <Button variant="outline" size="sm">
                    <Settings className="h-4 w-4" />
                  </Button>
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      </div>
      {/* Violaciones de Políticas */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Violaciones de Políticas
        </h2>
        <div className="space-y-4">
          {filteredPolicyViolations.map((violation) => (
            <Card
              key={violation.id}
              className="hover:shadow-lg transition-shadow"
            >
              <CardBody className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-3">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {violation.policy}
                      </h3>
                      <Badge
                        className={getViolationSeverityColor(
                          violation.severity
                        )}
                      >
                        {getViolationSeverityLabel(violation.severity)}
                      </Badge>
                      <Badge
                        className={getViolationStatusColor(violation.status)}
                      >
                        {getViolationStatusLabel(violation.status)}
                      </Badge>
                    </div>
                    <p className="text-gray-600 mb-3">
                      {violation.description}
                    </p>
                    <div className="flex items-center space-x-6 text-sm text-gray-500">
                      <span>
                        Recursos afectados:{" "}
                        {violation.affectedResources.join(", ")}
                      </span>
                      <span>Detectado: {violation.detectedAt}</span>
                      <span>Asignado a: {violation.assignedTo}</span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Link href={`/governance/monitoring/violations/${violation.id}`}>
                      <Button variant="outline" size="sm">
                        <Eye className="h-4 w-4 mr-2" />
                        Ver Detalles
                      </Button>
                    </Link>
                    <Button variant="outline" size="sm">
                      <Settings className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      </div>
      {filteredComplianceMetrics.length === 0 &&
        filteredRiskIndicators.length === 0 &&
        filteredPolicyViolations.length === 0 && (
          <div className="text-center py-12">
            <Activity className="h-16 w-16 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              No se encontraron datos
            </h3>
            <p className="text-gray-600">
              Intenta ajustar los filtros de búsqueda.
            </p>
          </div>
        )}
    </div>
  );
}
