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
import { useEffect, useState } from "react";
interface ComplianceMetric {
  id: string;
  name: string;
  category: string;
  currentValue: number;
  targetValue: number;
  status: "compliant" | "non-compliant" | "at-risk";
  lastUpdated: string;
  trend: "up" | "down" | "stable";
}
interface RiskIndicator {
  id: string;
  name: string;
  riskLevel: "low" | "medium" | "high" | "critical";
  probability: number;
  impact: number;
  mitigationStatus: "none" | "planned" | "in-progress" | "completed";
  lastAssessment: string;
  nextAssessment: string;
}
interface PolicyViolation {
  id: string;
  policy: string;
  severity: "low" | "medium" | "high" | "critical";
  description: string;
  detectedAt: string;
  status: "open" | "investigating" | "resolved";
  assignedTo: string;
  affectedResources: string[];
}
const mockComplianceMetrics: ComplianceMetric[] = [
  {
    id: "1",
    name: "Cumplimiento de Políticas de Seguridad",
    category: "Seguridad",
    currentValue: 87,
    targetValue: 95,
    status: "at-risk",
    lastUpdated: "2 horas atrás",
    trend: "up",
  },
  {
    id: "2",
    name: "Cumplimiento de GDPR",
    category: "Privacidad",
    currentValue: 92,
    targetValue: 90,
    status: "compliant",
    lastUpdated: "1 día atrás",
    trend: "stable",
  },
  {
    id: "3",
    name: "Cumplimiento de SOX",
    category: "Financiero",
    currentValue: 78,
    targetValue: 85,
    status: "non-compliant",
    lastUpdated: "3 días atrás",
    trend: "down",
  },
  {
    id: "4",
    name: "Cumplimiento de ISO 27001",
    category: "Seguridad de la Información",
    currentValue: 95,
    targetValue: 90,
    status: "compliant",
    lastUpdated: "1 semana atrás",
    trend: "up",
  },
];
const mockRiskIndicators: RiskIndicator[] = [
  {
    id: "1",
    name: "Riesgo de Brecha de Datos",
    riskLevel: "high",
    probability: 75,
    impact: 90,
    mitigationStatus: "in-progress",
    lastAssessment: "2024-01-15",
    nextAssessment: "2024-04-15",
  },
  {
    id: "2",
    name: "Riesgo de Cumplimiento Regulatorio",
    riskLevel: "medium",
    probability: 60,
    impact: 80,
    mitigationStatus: "planned",
    lastAssessment: "2024-01-10",
    nextAssessment: "2024-03-10",
  },
  {
    id: "3",
    name: "Riesgo de Continuidad del Negocio",
    riskLevel: "low",
    probability: 30,
    impact: 70,
    mitigationStatus: "completed",
    lastAssessment: "2024-01-05",
    nextAssessment: "2024-07-05",
  },
];
const mockPolicyViolations: PolicyViolation[] = [
  {
    id: "1",
    policy: "Política de Acceso a Datos",
    severity: "high",
    description: "Acceso no autorizado a datos sensibles del cliente",
    detectedAt: "2 horas atrás",
    status: "investigating",
    assignedTo: "Equipo de Seguridad",
    affectedResources: ["Customer Database", "User Service"],
  },
  {
    id: "2",
    policy: "Política de Cifrado",
    severity: "medium",
    description: "Datos en tránsito sin cifrar detectados",
    detectedAt: "1 día atrás",
    status: "open",
    assignedTo: "DevOps Team",
    affectedResources: ["API Gateway", "Payment Service"],
  },
  {
    id: "3",
    policy: "Política de Auditoría",
    severity: "low",
    description: "Logs de auditoría incompletos",
    detectedAt: "3 días atrás",
    status: "resolved",
    assignedTo: "IT Operations",
    affectedResources: ["Audit System", "Log Aggregator"],
  },
];
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
  const totalRisks = mockRiskIndicators.length;
  const criticalRisks = mockRiskIndicators.filter(
    (r) => r.riskLevel === "critical"
  ).length;
  const highRisks = mockRiskIndicators.filter(
    (r) => r.riskLevel === "high"
  ).length;
  const totalViolations = mockPolicyViolations.length;
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
                    {metric.status === "compliant"
                      ? "Cumple"
                      : metric.status === "at-risk"
                      ? "En Riesgo"
                      : "No Cumple"}
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
                    {metric.trend === "up"
                      ? "Mejorando"
                      : metric.trend === "down"
                      ? "Empeorando"
                      : "Estable"}
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
                    {risk.riskLevel === "low"
                      ? "Bajo"
                      : risk.riskLevel === "medium"
                      ? "Medio"
                      : risk.riskLevel === "high"
                      ? "Alto"
                      : "Crítico"}
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
                      {risk.mitigationStatus === "none"
                        ? "Ninguna"
                        : risk.mitigationStatus === "planned"
                        ? "Planificada"
                        : risk.mitigationStatus === "in-progress"
                        ? "En Progreso"
                        : "Completada"}
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
                  <Button variant="outline" size="sm" className="flex-1">
                    <Eye className="h-4 w-4 mr-2" />
                    Ver Detalles
                  </Button>
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
                        {violation.severity === "low"
                          ? "Baja"
                          : violation.severity === "medium"
                          ? "Media"
                          : violation.severity === "high"
                          ? "Alta"
                          : "Crítica"}
                      </Badge>
                      <Badge
                        className={getViolationStatusColor(violation.status)}
                      >
                        {violation.status === "open"
                          ? "Abierta"
                          : violation.status === "investigating"
                          ? "En Investigación"
                          : "Resuelta"}
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
                    <Button variant="outline" size="sm">
                      <Eye className="h-4 w-4 mr-2" />
                      Ver Detalles
                    </Button>
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
