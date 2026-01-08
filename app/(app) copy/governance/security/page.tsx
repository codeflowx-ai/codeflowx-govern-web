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
  Plus,
  RefreshCw,
  Settings,
  Shield,
  TrendingDown,
  TrendingUp,
} from "lucide-react";
import { useState } from "react";
interface SecurityThreat {
  id: string;
  name: string;
  severity: "low" | "medium" | "high" | "critical";
  status: "active" | "investigating" | "resolved";
  description: string;
  affectedSystems: string[];
  detectedAt: string;
  lastUpdated: string;
  assignedTo: string;
}
interface SecurityPolicy {
  id: string;
  name: string;
  category: string;
  status: "active" | "draft" | "archived";
  compliance: number;
  lastReview: string;
  nextReview: string;
  owner: string;
}
interface SecurityMetric {
  name: string;
  value: number;
  target: number;
  trend: "up" | "down" | "stable";
  status: "good" | "warning" | "critical";
}
const mockThreats: SecurityThreat[] = [
  {
    id: "1",
    name: "Intento de acceso no autorizado",
    severity: "high",
    status: "investigating",
    description: "Múltiples intentos de login fallidos desde IPs desconocidas",
    affectedSystems: ["User Service", "Admin Portal"],
    detectedAt: "2 horas atrás",
    lastUpdated: "30 min atrás",
    assignedTo: "Equipo de Seguridad",
  },
  {
    id: "2",
    name: "Vulnerabilidad en dependencia",
    severity: "medium",
    status: "active",
    description: "CVE-2024-1234 detectado en librería de terceros",
    affectedSystems: ["API Gateway", "Payment Service"],
    detectedAt: "1 día atrás",
    lastUpdated: "2 horas atrás",
    assignedTo: "DevOps Team",
  },
  {
    id: "3",
    name: "Anomalía en tráfico de red",
    severity: "low",
    status: "resolved",
    description: "Pico inusual en tráfico hacia endpoints de API",
    affectedSystems: ["Load Balancer", "API Gateway"],
    detectedAt: "3 días atrás",
    lastUpdated: "1 día atrás",
    assignedTo: "Network Team",
  },
];
const mockPolicies: SecurityPolicy[] = [
  {
    id: "1",
    name: "Política de Contraseñas",
    category: "Autenticación",
    status: "active",
    compliance: 95,
    lastReview: "2024-01-15",
    nextReview: "2024-04-15",
    owner: "CISO",
  },
  {
    id: "2",
    name: "Política de Acceso a Datos",
    category: "Autorización",
    status: "active",
    compliance: 87,
    lastReview: "2024-01-10",
    nextReview: "2024-04-10",
    owner: "Data Protection Officer",
  },
  {
    id: "3",
    name: "Política de Cifrado",
    category: "Protección de Datos",
    status: "draft",
    compliance: 0,
    lastReview: "N/A",
    nextReview: "2024-03-01",
    owner: "Security Architect",
  },
];
const mockMetrics: SecurityMetric[] = [
  {
    name: "Tiempo de Respuesta a Incidentes",
    value: 2.5,
    target: 4.0,
    trend: "up",
    status: "good",
  },
  {
    name: "Tasa de Vulnerabilidades Críticas",
    value: 0.5,
    target: 1.0,
    trend: "down",
    status: "good",
  },
  {
    name: "Cumplimiento de Políticas",
    value: 91,
    target: 95,
    trend: "stable",
    status: "warning",
  },
  {
    name: "Tiempo de Parcheo",
    value: 3.2,
    target: 2.0,
    trend: "up",
    status: "warning",
  },
];
export default function GovernanceSecurity() {
  const [selectedSeverity, setSelectedSeverity] = useState("all");
  const [selectedStatus, setSelectedStatus] = useState("all");
  const [showNewPolicyModal, setShowNewPolicyModal] = useState(false);
  const getSeverityColor = (severity: string) => {
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
  const getStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-red-100 text-red-800";
      case "investigating":
        return "bg-yellow-100 text-yellow-800";
      case "resolved":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getPolicyStatusColor = (status: string) => {
    switch (status) {
      case "active":
        return "bg-green-100 text-green-800";
      case "draft":
        return "bg-yellow-100 text-yellow-800";
      case "archived":
        return "bg-gray-100 text-gray-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const getMetricStatusColor = (status: string) => {
    switch (status) {
      case "good":
        return "bg-green-100 text-green-800";
      case "warning":
        return "bg-yellow-100 text-yellow-800";
      case "critical":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  const filteredThreats = mockThreats.filter((threat) => {
    const matchesSeverity =
      selectedSeverity === "all" || threat.severity === selectedSeverity;
    const matchesStatus =
      selectedStatus === "all" || threat.status === selectedStatus;
    return matchesSeverity && matchesStatus;
  });
  const activeThreats = mockThreats.filter((t) => t.status === "active").length;
  const investigatingThreats = mockThreats.filter(
    (t) => t.status === "investigating"
  ).length;
  const resolvedThreats = mockThreats.filter(
    (t) => t.status === "resolved"
  ).length;
  return (
    <div className="w-full p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Seguridad y Gobernanza
          </h1>
          <p className="text-gray-600 mt-2">
            Supervisa y gestiona la postura de seguridad de la organización
          </p>
        </div>
        <div className="flex items-center space-x-3">
          <Button
            variant="outline"
            onClick={() => console.log("Refrescando datos de seguridad...")}
            className="flex items-center space-x-2"
          >
            <RefreshCw className="h-4 w-4" />
            <span>Actualizar</span>
          </Button>
          <Button
            onClick={() => setShowNewPolicyModal(true)}
            className="bg-blue-600 hover:bg-blue-700"
          >
            <Plus className="h-4 w-4 mr-2" />
            Nueva Política
          </Button>
        </div>
      </div>
      {/* Métricas de resumen */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardBody className="flex items-center space-x-3">
            <Shield className="h-8 w-8 text-blue-600" />
            <div>
              <p className="text-sm text-gray-600">Postura de Seguridad</p>
              <p className="text-2xl font-bold text-green-600">85%</p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <AlertTriangle className="h-8 w-8 text-red-600" />
            <div>
              <p className="text-sm text-gray-600">Amenazas Activas</p>
              <p className="text-2xl font-bold text-red-600">{activeThreats}</p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <Clock className="h-8 w-8 text-yellow-600" />
            <div>
              <p className="text-sm text-gray-600">En Investigación</p>
              <p className="text-2xl font-bold text-yellow-600">
                {investigatingThreats}
              </p>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody className="flex items-center space-x-3">
            <CheckCircle className="h-8 w-8 text-green-600" />
            <div>
              <p className="text-sm text-gray-600">Resueltas</p>
              <p className="text-2xl font-bold text-green-600">
                {resolvedThreats}
              </p>
            </div>
          </CardBody>
        </Card>
      </div>
      {/* Métricas de Seguridad */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Métricas de Seguridad
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {mockMetrics.map((metric, index) => (
            <Card key={index}>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{metric.name}</CardTitle>
                  <Badge className={getMetricStatusColor(metric.status)}>
                    {metric.status === "good"
                      ? "Bueno"
                      : metric.status === "warning"
                      ? "Advertencia"
                      : "Crítico"}
                  </Badge>
                </div>
              </CardHeader>
              <CardBody>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <span className="text-2xl font-bold">{metric.value}</span>
                    <span className="text-sm text-gray-500">
                      Meta: {metric.target}
                    </span>
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
                </div>
              </CardBody>
            </Card>
          ))}
        </div>
      </div>
      {/* Filtros de Amenazas */}
      <div className="flex items-center space-x-4">
        <span className="text-sm font-medium text-gray-700">
          Filtrar amenazas:
        </span>
        <select
          value={selectedSeverity}
          onChange={(e) => setSelectedSeverity(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">Todas las severidades</option>
          <option value="low">Baja</option>
          <option value="medium">Media</option>
          <option value="high">Alta</option>
          <option value="critical">Crítica</option>
        </select>
        <select
          value={selectedStatus}
          onChange={(e) => setSelectedStatus(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        >
          <option value="all">Todos los estados</option>
          <option value="active">Activas</option>
          <option value="investigating">En Investigación</option>
          <option value="resolved">Resueltas</option>
        </select>
      </div>
      {/* Amenazas de Seguridad */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Amenazas de Seguridad
        </h2>
        <div className="space-y-4">
          {filteredThreats.map((threat) => (
            <Card key={threat.id} className="hover:shadow-lg transition-shadow">
              <CardBody className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-3 mb-3">
                      <h3 className="text-lg font-semibold text-gray-900">
                        {threat.name}
                      </h3>
                      <Badge className={getSeverityColor(threat.severity)}>
                        {threat.severity === "low"
                          ? "Baja"
                          : threat.severity === "medium"
                          ? "Media"
                          : threat.severity === "high"
                          ? "Alta"
                          : "Crítica"}
                      </Badge>
                      <Badge className={getStatusColor(threat.status)}>
                        {threat.status === "active"
                          ? "Activa"
                          : threat.status === "investigating"
                          ? "En Investigación"
                          : "Resuelta"}
                      </Badge>
                    </div>
                    <p className="text-gray-600 mb-3">{threat.description}</p>
                    <div className="flex items-center space-x-6 text-sm text-gray-500">
                      <span>
                        Sistemas afectados: {threat.affectedSystems.join(", ")}
                      </span>
                      <span>Detectado: {threat.detectedAt}</span>
                      <span>Asignado a: {threat.assignedTo}</span>
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
      {/* Políticas de Seguridad */}
      <div>
        <h2 className="text-xl font-semibold text-gray-900 mb-4">
          Políticas de Seguridad
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockPolicies.map((policy) => (
            <Card key={policy.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{policy.name}</CardTitle>
                  <Badge className={getPolicyStatusColor(policy.status)}>
                    {policy.status === "active"
                      ? "Activa"
                      : policy.status === "draft"
                      ? "Borrador"
                      : "Archivada"}
                  </Badge>
                </div>
                <p className="text-sm text-gray-600">{policy.category}</p>
              </CardHeader>
              <CardBody className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span className="text-gray-600">Cumplimiento</span>
                    <span className="font-medium">{policy.compliance}%</span>
                  </div>
                  <Progress value={policy.compliance} className="h-2" />
                </div>
                <div className="space-y-2 text-sm text-gray-500">
                  <div className="flex justify-between">
                    <span>Propietario:</span>
                    <span>{policy.owner}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Última revisión:</span>
                    <span>{policy.lastReview}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Próxima revisión:</span>
                    <span>{policy.nextReview}</span>
                  </div>
                </div>
                <div className="flex space-x-2">
                  <Button variant="outline" size="sm" className="flex-1">
                    <Eye className="h-4 w-4 mr-2" />
                    Ver
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
      {filteredThreats.length === 0 && (
        <div className="text-center py-12">
          <Shield className="h-16 w-16 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            No se encontraron amenazas
          </h3>
          <p className="text-gray-600">
            Intenta ajustar los filtros de búsqueda.
          </p>
        </div>
      )}
    </div>
  );
}


