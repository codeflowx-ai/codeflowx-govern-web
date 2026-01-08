"use client";

import { useTranslation } from "@/app/config/i18n";
import { Card, CardBody, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Shield,
  FileCheck,
  AlertTriangle,
  CheckCircle,
  Clock,
  ExternalLink,
  Award,
  FileText,
  Globe,
  UserCheck,
} from "lucide-react";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

interface ComplianceStatus {
  classification: {
    status: string;
    riskCategory: string;
    annexIIICategories: string[];
    lastUpdated: string;
  };
  fria: {
    status: string;
    assessmentId?: number;
    lastAssessment: string;
    riskLevel: string;
  };
  euRegistration: {
    status: string;
    registrationId?: string;
    registeredAt?: string;
    expiryDate?: string;
  };
  certification: {
    status: string;
    certificateId?: string;
    certifiedAt?: string;
    expiryDate?: string;
    certifyingBody?: string;
  };
  conformityDeclaration: {
    status: string;
    declarationId?: string;
    declaredAt?: string;
  };
  overallScore: number;
}

interface ComplianceTabProps {
  agentId: string | number;
  agentUuid?: string;
  agentName?: string;
}

export default function ComplianceTab({
  agentId,
  agentUuid,
  agentName,
}: ComplianceTabProps) {
  const { t, mounted } = useTranslation();
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const [compliance, setCompliance] = useState<ComplianceStatus | null>(null);

  useEffect(() => {
    loadComplianceData();
  }, [agentId, agentUuid]);

  const loadComplianceData = async () => {
    try {
      setLoading(true);
      // TODO: Implementar llamadas API reales
      // const response = await fetch(`/api/v1/governance/compliance/agents/${agentUuid}`);
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Mock data
      setCompliance({
        classification: {
          status: "CLASSIFIED",
          riskCategory: "HIGH_RISK",
          annexIIICategories: ["8a", "8b"],
          lastUpdated: "2024-01-15T10:00:00",
        },
        fria: {
          status: "COMPLETED",
          assessmentId: 123,
          lastAssessment: "2024-01-15T10:00:00",
          riskLevel: "MODERATE",
        },
        euRegistration: {
          status: "REGISTERED",
          registrationId: "EU-REG-2024-001234",
          registeredAt: "2024-01-16T09:00:00",
          expiryDate: "2025-01-16T09:00:00",
        },
        certification: {
          status: "CERTIFIED",
          certificateId: "CERT-2024-001234",
          certifiedAt: "2024-01-17T14:00:00",
          expiryDate: "2025-01-17T14:00:00",
          certifyingBody: "Internal Compliance Team",
        },
        conformityDeclaration: {
          status: "DECLARED",
          declarationId: "DEC-2024-001234",
          declaredAt: "2024-01-18T11:00:00",
        },
        overallScore: 92,
      });
    } catch (error) {
      console.error("Error loading compliance data:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const colors: Record<string, string> = {
      CLASSIFIED: "bg-blue-500/20 text-blue-500",
      NOT_CLASSIFIED: "bg-gray-500/20 text-gray-400",
      COMPLETED: "bg-green-500/20 text-green-500",
      IN_PROGRESS: "bg-yellow-500/20 text-yellow-400",
      PENDING: "bg-yellow-500/20 text-yellow-400",
      REGISTERED: "bg-green-500/20 text-green-500",
      NOT_REGISTERED: "bg-red-500/20 text-red-400",
      CERTIFIED: "bg-green-500/20 text-green-500",
      NOT_CERTIFIED: "bg-gray-500/20 text-gray-400",
      DECLARED: "bg-green-500/20 text-green-500",
      NOT_DECLARED: "bg-gray-500/20 text-gray-400",
    };
    return (
      <Badge className={colors[status] || "bg-gray-500/20 text-gray-400"}>
        {status}
      </Badge>
    );
  };

  const getRiskCategoryBadge = (category: string) => {
    const colors: Record<string, string> = {
      MINIMAL_RISK: "bg-green-500/20 text-green-500",
      LIMITED_RISK: "bg-yellow-500/20 text-yellow-400",
      HIGH_RISK: "bg-orange-500/20 text-orange-500",
      PROHIBITED: "bg-red-500/20 text-red-400",
    };
    return (
      <Badge className={colors[category] || "bg-gray-500/20 text-gray-400"}>
        {category.replace("_", " ")}
      </Badge>
    );
  };

  const getScoreColor = (score: number) => {
    if (score >= 90) return "text-green-600";
    if (score >= 70) return "text-yellow-600";
    return "text-red-600";
  };

  if (!mounted || loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <Shield className="w-8 h-8 animate-spin mx-auto mb-2 text-muted-foreground" />
          <p className="text-muted-foreground">{t("common.loading", "Cargando...")}</p>
        </div>
      </div>
    );
  }

  if (!compliance) {
    return (
      <Card>
        <CardBody>
          <p className="text-center text-muted-foreground py-8">
            No hay datos de cumplimiento disponibles para este agente.
          </p>
        </CardBody>
      </Card>
    );
  }

  return (
    <div className="space-y-6">
      {/* Puntuación General */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Shield className="h-5 w-5" />
            Puntuación de Cumplimiento
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-muted-foreground mb-2">
                Puntuación general de cumplimiento AI Act
              </p>
              <p className={`text-4xl font-bold ${getScoreColor(compliance.overallScore)}`}>
                {compliance.overallScore}%
              </p>
            </div>
            <div className="text-right">
              <Button
                variant="outline"
                size="sm"
                onClick={() => router.push(`/governance/compliance/classification?agentUuid=${agentUuid}`)}
              >
                <ExternalLink className="h-4 w-4 mr-2" />
                Ver en Compliance
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Clasificación */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <FileText className="h-5 w-5" />
            Clasificación de Riesgo
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Estado:</span>
              {getStatusBadge(compliance.classification.status)}
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Categoría de Riesgo:</span>
              {getRiskCategoryBadge(compliance.classification.riskCategory)}
            </div>
            {compliance.classification.annexIIICategories.length > 0 && (
              <div>
                <span className="text-sm font-medium block mb-2">
                  Categorías Anexo III:
                </span>
                <div className="flex gap-2 flex-wrap">
                  {compliance.classification.annexIIICategories.map((cat, idx) => (
                    <Badge key={idx} variant="outline">
                      {cat}
                    </Badge>
                  ))}
                </div>
              </div>
            )}
            <div className="flex items-center justify-between text-sm text-muted-foreground">
              <span>Última actualización:</span>
              <span>{new Date(compliance.classification.lastUpdated).toLocaleString()}</span>
            </div>
            <Button
              variant="outline"
              size="sm"
              className="w-full"
              onClick={() => router.push(`/governance/compliance/classification?agentUuid=${agentUuid}`)}
            >
              <ExternalLink className="h-4 w-4 mr-2" />
              Ver Clasificación Completa
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* FRIA */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <UserCheck className="h-5 w-5" />
            Evaluación de Impacto en Derechos Fundamentales (FRIA)
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Estado:</span>
              {getStatusBadge(compliance.fria.status)}
            </div>
            {compliance.fria.assessmentId && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">ID de Evaluación:</span>
                <span className="text-sm font-mono">{compliance.fria.assessmentId}</span>
              </div>
            )}
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Nivel de Riesgo:</span>
              {getRiskCategoryBadge(compliance.fria.riskLevel)}
            </div>
            <div className="flex items-center justify-between text-sm text-muted-foreground">
              <span>Última evaluación:</span>
              <span>{new Date(compliance.fria.lastAssessment).toLocaleString()}</span>
            </div>
            <Button
              variant="outline"
              size="sm"
              className="w-full"
              onClick={() => router.push(`/governance/compliance/fria/${compliance.fria.assessmentId || 'new'}?agentUuid=${agentUuid}`)}
            >
              <ExternalLink className="h-4 w-4 mr-2" />
              Ver Evaluación FRIA
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Registro EU */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Globe className="h-5 w-5" />
            Registro EU AI Act
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Estado:</span>
              {getStatusBadge(compliance.euRegistration.status)}
            </div>
            {compliance.euRegistration.registrationId && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">ID de Registro:</span>
                <span className="text-sm font-mono">{compliance.euRegistration.registrationId}</span>
              </div>
            )}
            {compliance.euRegistration.registeredAt && (
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Registrado el:</span>
                <span>{new Date(compliance.euRegistration.registeredAt).toLocaleString()}</span>
              </div>
            )}
            {compliance.euRegistration.expiryDate && (
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Válido hasta:</span>
                <span>{new Date(compliance.euRegistration.expiryDate).toLocaleString()}</span>
              </div>
            )}
            <Button
              variant="outline"
              size="sm"
              className="w-full"
              onClick={() => router.push(`/governance/compliance/eu-registration?agentUuid=${agentUuid}`)}
            >
              <ExternalLink className="h-4 w-4 mr-2" />
              Ver Registro EU
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Certificación */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Award className="h-5 w-5" />
            Certificación
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Estado:</span>
              {getStatusBadge(compliance.certification.status)}
            </div>
            {compliance.certification.certificateId && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">ID de Certificado:</span>
                <span className="text-sm font-mono">{compliance.certification.certificateId}</span>
              </div>
            )}
            {compliance.certification.certifyingBody && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Organismo Certificador:</span>
                <span className="text-sm">{compliance.certification.certifyingBody}</span>
              </div>
            )}
            {compliance.certification.certifiedAt && (
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Certificado el:</span>
                <span>{new Date(compliance.certification.certifiedAt).toLocaleString()}</span>
              </div>
            )}
            {compliance.certification.expiryDate && (
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Válido hasta:</span>
                <span>{new Date(compliance.certification.expiryDate).toLocaleString()}</span>
              </div>
            )}
            <Button
              variant="outline"
              size="sm"
              className="w-full"
              onClick={() => router.push(`/governance/agents/registry/${agentId}?tab=certification`)}
            >
              <Award className="h-4 w-4 mr-2" />
              Gestionar Certificación
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Declaración de Conformidad */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <FileCheck className="h-5 w-5" />
            Declaración de Conformidad
          </CardTitle>
        </CardHeader>
        <CardBody>
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium">Estado:</span>
              {getStatusBadge(compliance.conformityDeclaration.status)}
            </div>
            {compliance.conformityDeclaration.declarationId && (
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">ID de Declaración:</span>
                <span className="text-sm font-mono">{compliance.conformityDeclaration.declarationId}</span>
              </div>
            )}
            {compliance.conformityDeclaration.declaredAt && (
              <div className="flex items-center justify-between text-sm text-muted-foreground">
                <span>Declarado el:</span>
                <span>{new Date(compliance.conformityDeclaration.declaredAt).toLocaleString()}</span>
              </div>
            )}
            <Button
              variant="outline"
              size="sm"
              className="w-full"
              onClick={() => router.push(`/governance/compliance/conformity-declaration?agentUuid=${agentUuid}`)}
            >
              <ExternalLink className="h-4 w-4 mr-2" />
              Ver Declaración de Conformidad
            </Button>
          </div>
        </CardBody>
      </Card>
    </div>
  );
}
